package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SentrySprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Wrench extends Wand {

    {
        image = ItemSpriteSheet.WRENCH;
    }

    @Override
    protected int collisionProperties(int target) {
        if (Dungeon.level.heroFOV[target]) return Ballistica.STOP_TARGET;
        else return Ballistica.PROJECTILE;
    }

    private boolean sentryAvailable = true;

    @Override
    public boolean tryToZap(Hero owner, int target) {

        int currentSentryEnergy = 0;
        for (Char ch : Actor.chars()) {
            if (ch instanceof Sentry) {
                currentSentryEnergy += ((Sentry) ch).tier;
            }
        }

        int maxSentryEnergy = 0;
        for (Buff buff : curUser.buffs()) {
            if (buff instanceof Wand.Charger) {
                if (((Charger) buff).wand() instanceof Wrench) {
                    maxSentryEnergy += 2 + ((Charger) buff).wand().level();
                }
            }
        }

        sentryAvailable = (currentSentryEnergy < maxSentryEnergy);

        Char ch = Actor.findChar(target);
        if (ch instanceof Sentry) {
            if (!sentryAvailable) {
                GLog.w(Messages.get(this, "no_more_wards"));
                return false;
            }
        } else {
            if ((currentSentryEnergy + 1) > maxSentryEnergy) {
                GLog.w(Messages.get(this, "no_more_wards"));
                return false;
            }
        }

        return super.tryToZap(owner, target);
    }

    @Override
    protected void onZap(Ballistica bolt) {

        int target = bolt.collisionPos;
        Char ch = Actor.findChar(target);
        if (ch != null && !(ch instanceof Sentry)) {
            if (bolt.dist > 1) target = bolt.path.get(bolt.dist - 1);

            ch = Actor.findChar(target);
            if (ch != null && !(ch instanceof Sentry)) {
                GLog.w(Messages.get(this, "bad_location"));
                Dungeon.level.pressCell(bolt.collisionPos);
                return;
            }
        }

        if (!Dungeon.level.passable[target]) {
            GLog.w(Messages.get(this, "bad_location"));
            Dungeon.level.pressCell(target);

        } else if (ch != null) {
            if (ch instanceof Sentry) {
                ((Sentry) ch).upgrade(buffedLvl());
                ch.sprite.emitter().burst(MagicMissile.WardParticle.UP, ((Sentry) ch).tier);
            } else {
                GLog.w(Messages.get(this, "bad_location"));
                Dungeon.level.pressCell(target);
            }

        } else {
            Sentry sentry = new Sentry();
            sentry.pos = target;
            sentry.wandLevel = buffedLvl();
            GameScene.add(sentry, 1f);
            Dungeon.level.occupyCell(sentry);
            sentry.sprite.emitter().burst(MagicMissile.WardParticle.UP, sentry.tier);
            Dungeon.level.pressCell(target);

        }
    }

    @Override
    protected void fx(Ballistica bolt, Callback callback) {
        MagicMissile m = MagicMissile.boltFromChar(curUser.sprite.parent,
                MagicMissile.WARD,
                curUser.sprite,
                bolt.collisionPos,
                callback);

        if (bolt.dist > 10) {
            m.setSpeed(bolt.dist * 20);
        }
        Sample.INSTANCE.play(Assets.Sounds.ZAP);
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        //sentries are single-use and cannot be healed, so there is no on-hit effect
    }

    @Override
    public String statsDesc() {
        if (levelKnown)
            return Messages.get(this, "stats_desc", level() + 2);
        else
            return Messages.get(this, "stats_desc", 2);
    }

    public static class Sentry extends NPC {

        public int tier = 1;
        private int wandLevel = 1;

        public int totalZaps = 0;

        {
            spriteClass = SentrySprite.class;

            alignment = Alignment.ALLY;

            properties.add(Property.IMMOVABLE);
            properties.add(Property.INORGANIC);

            viewDistance = 4;
            state = WANDERING;
        }

        @Override
        public String name() {
            return Messages.get(this, "name_" + tier);
        }

        public void upgrade(int wandLevel) {
            if (this.wandLevel < wandLevel) {
                this.wandLevel = wandLevel;
            }

            if (tier < 3) {
                tier++;
                viewDistance++;
                if (sprite != null) {
                    ((SentrySprite) sprite).updateTier(tier);
                    sprite.place(pos);
                }
                GameScene.updateFog(pos, viewDistance + 1);
            }

        }

        @Override
        public int defenseSkill(Char enemy) {
            return super.defenseSkill(enemy);
        }

        @Override
        public int drRoll() {
            return 0;
        }

        @Override
        protected float attackDelay() {
            return 2f;
        }

        @Override
        protected boolean canAttack(Char enemy) {
            return new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
        }

        @Override
        protected boolean doAttack(Char enemy) {
            boolean visible = fieldOfView[pos] || fieldOfView[enemy.pos];
            if (visible) {
                sprite.zap(enemy.pos);
            } else {
                zap();
            }

            return !visible;
        }

        private void zap() {
            spend(1f);

            //always hits
            int dmg = Random.NormalIntRange(2 + wandLevel, 8 + 4 * wandLevel);
            enemy.damage(dmg, this);
            if (enemy.isAlive()) {
                Wand.processSoulMark(enemy, wandLevel, 1);
            }

            if (!enemy.isAlive() && enemy == Dungeon.hero) {
                Dungeon.fail(getClass());
            }

            totalZaps++;
            if (totalZaps >= (2 * tier - 1)) {
                die(this);
            }
        }

        public void onZapComplete() {
            zap();
            next();
        }

        @Override
        protected boolean getCloser(int target) {
            return false;
        }

        @Override
        protected boolean getFurther(int target) {
            return false;
        }

        @Override
        public CharSprite sprite() {
            SentrySprite sprite = (SentrySprite) super.sprite();
            sprite.linkVisuals(this);
            return sprite;
        }

        @Override
        public void updateSpriteState() {
            super.updateSpriteState();
            ((SentrySprite) sprite).updateTier(tier);
            sprite.place(pos);
        }

        @Override
        public void destroy() {
            super.destroy();
            Dungeon.observe();
            GameScene.updateFog(pos, viewDistance + 1);
        }

        @Override
        public boolean canInteract(Char c) {
            return true;
        }

        @Override
        public boolean interact(Char c) {
            if (c != Dungeon.hero) {
                return true;
            }
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndOptions(Messages.get(Sentry.this, "dismiss_title"),
                            Messages.get(Sentry.this, "dismiss_body"),
                            Messages.get(Sentry.this, "dismiss_confirm"),
                            Messages.get(Sentry.this, "dismiss_cancel")) {
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0) {
                                die(null);
                            }
                        }
                    });
                }
            });
            return true;
        }

        @Override
        public String description() {
            return Messages.get(this, "desc_" + tier, 2 + wandLevel, 8 + 4 * wandLevel, tier);
        }

        {
            immunities.add(AllyBuff.class);
        }

        private static final String TIER = "tier";
        private static final String WAND_LEVEL = "wand_level";
        private static final String TOTAL_ZAPS = "total_zaps";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(TIER, tier);
            bundle.put(WAND_LEVEL, wandLevel);
            bundle.put(TOTAL_ZAPS, totalZaps);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            tier = bundle.getInt(TIER);
            viewDistance = 3 + tier;
            wandLevel = bundle.getInt(WAND_LEVEL);
            totalZaps = bundle.getInt(TOTAL_ZAPS);
        }

        {
            properties.add(Property.IMMOVABLE);
        }
    }
}

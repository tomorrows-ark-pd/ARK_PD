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

    @Override
    public boolean tryToZap(Hero owner, int target) {

        //upgrading an existing sentry no longer costs energy, only placing a new one does
        Char ch = Actor.findChar(target);
        if (!(ch instanceof Sentry)) {

            int currentSentryEnergy = 0;
            for (Char c : Actor.chars()) {
                if (c instanceof Sentry) {
                    currentSentryEnergy += ((Sentry) c).tier;
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
                Sentry sentry = (Sentry) ch;
                //upgrading requires repeated zaps while at max HP, otherwise the zap just heals
                if (sentry.HP >= sentry.HT) {
                    sentry.registerUpgradeZap(buffedLvl());
                } else {
                    sentry.wandHeal(buffedLvl());
                }
                ch.sprite.emitter().burst(MagicMissile.WardParticle.UP, sentry.tier);
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

        int level = Math.max(0, staff.buffedLvl());

        if (Random.Int(level + 5) >= 4) {
            for (Char ch : Actor.chars()) {
                if (ch instanceof Sentry) {
                    ((Sentry) ch).wandHeal(staff.buffedLvl());
                    ch.sprite.emitter().burst(MagicMissile.WardParticle.UP, ((Sentry) ch).tier);
                }
            }
        }
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
        private int upgradeProgress = 0;

        {
            spriteClass = SentrySprite.class;

            alignment = Alignment.ALLY;

            properties.add(Property.IMMOVABLE);
            properties.add(Property.INORGANIC);

            viewDistance = 4;
            state = WANDERING;

            HT = HP = 50;
        }

        @Override
        public String name() {
            return Messages.get(this, "name_" + tier);
        }

        //upgrading requires being zapped while at max HP: twice for tier 1->2, thrice for tier 2->3
        public void registerUpgradeZap(int wandLevel) {
            if (this.wandLevel < wandLevel) {
                this.wandLevel = wandLevel;
            }

            int threshold;
            switch (tier) {
                case 1: threshold = 2; break;
                case 2: threshold = 3; break;
                case 3: default:
                    wandHeal(wandLevel);
                    return;
            }

            if (++upgradeProgress < threshold) return;
            upgradeProgress = 0;

            switch (tier) {
                case 1: HT = 90; break;
                case 2: HT = 150; break;
            }
            HP = HT;

            tier++;
            viewDistance++;
            if (sprite != null) {
                ((SentrySprite) sprite).updateTier(tier);
                sprite.place(pos);
            }
            GameScene.updateFog(pos, viewDistance + 1);
        }

        private void wandHeal(int wandLevel) {
            if (this.wandLevel < wandLevel) {
                this.wandLevel = wandLevel;
            }

            int heal;
            switch (tier) {
                default:
                    return;
                case 1:
                    heal = 13;
                    break;
                case 2:
                    heal = 20;
                    break;
                case 3:
                    heal = 30;
                    break;
            }

            HP = Math.min(HT, HP + heal);
            if (sprite != null) sprite.showStatus(CharSprite.POSITIVE, Integer.toString(heal));
        }

        @Override
        public int defenseSkill(Char enemy) {
            defenseSkill = Dungeon.depth + 8;
            return super.defenseSkill(enemy);
        }

        @Override
        public int drRoll() {
            //1.5x Ward's DR roll, tiers 1-3 mapped to Ward's tier 4-6 denominators
            return Math.round(1.5f * Random.NormalIntRange(0, 3 + Dungeon.depth / 2) / (4f - tier));
        }

        @Override
        protected float attackDelay() {
            //tier 1 attacks every 3 turns, tiers 2-3 attack every turn
            return tier <= 1 ? 3f : 1f;
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

            //always hits, tier base range scaling with wandLevel (min +1/lvl, max +2/lvl)
            //avg dmg at wandLevel 0 accounts for fire rate: tier 1 = 10, tier 2 = 7, tier 3 = 10
            int dmg;
            switch (tier) {
                case 1: default: dmg = Random.NormalIntRange(5 + wandLevel, 15 + 2 * wandLevel); break;
                case 2: dmg = Random.NormalIntRange(5 + wandLevel, 9 + 2 * wandLevel); break;
                case 3: dmg = Random.NormalIntRange(wandLevel, 20 + 2 * wandLevel); break;
            }
            enemy.damage(dmg, this);
            //gun-style hit effect
            enemy.sprite.burst(0xFFFFFFFF, tier + 2);
            Sample.INSTANCE.play(Assets.Sounds.HIT, 1, Random.Float(0.87f, 1.15f));
            if (enemy.isAlive()) {
                Wand.processSoulMark(enemy, wandLevel, 1);
            }

            totalZaps++;
            //tier 3 knocks back every 4th attack, weaker than WandOfBlastWave
            if (tier >= 3 && enemy.isAlive() && totalZaps % 4 == 0) {
                Ballistica line = new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT);
                if (line.path.size() > line.dist + 1) {
                    Ballistica knockback = new Ballistica(enemy.pos, line.path.get(line.dist + 1), Ballistica.MAGIC_BOLT);
                    WandOfBlastWave.throwChar(enemy, knockback, 2);
                }
            }

            if (!enemy.isAlive() && enemy == Dungeon.hero) {
                Dungeon.fail(getClass());
            }

            //no zap-count death limit, always takes self-damage instead
            int selfDmg;
            switch (tier) {
                case 1: default: selfDmg = 20; break;
                case 2: selfDmg = 8; break;
                case 3: selfDmg = 10; break;
            }
            damage(selfDmg, this);
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
            int dmgMin;
            int dmgMax;
            switch (tier) {
                case 1: default: dmgMin = 5 + wandLevel; dmgMax = 15 + 2 * wandLevel; break;
                case 2: dmgMin = 5 + wandLevel; dmgMax = 9 + 2 * wandLevel; break;
                case 3: dmgMin = wandLevel; dmgMax = 20 + 2 * wandLevel; break;
            }
            return Messages.get(this, "desc_" + tier, dmgMin, dmgMax, tier);
        }

        {
            immunities.add(AllyBuff.class);
        }

        private static final String TIER = "tier";
        private static final String WAND_LEVEL = "wand_level";
        private static final String TOTAL_ZAPS = "total_zaps";
        private static final String UPGRADE_PROGRESS = "upgrade_progress";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(TIER, tier);
            bundle.put(WAND_LEVEL, wandLevel);
            bundle.put(TOTAL_ZAPS, totalZaps);
            bundle.put(UPGRADE_PROGRESS, upgradeProgress);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            tier = bundle.getInt(TIER);
            viewDistance = 3 + tier;
            wandLevel = bundle.getInt(WAND_LEVEL);
            totalZaps = bundle.getInt(TOTAL_ZAPS);
            upgradeProgress = bundle.getInt(UPGRADE_PROGRESS);
        }

        {
            properties.add(Property.IMMOVABLE);
        }
    }
}

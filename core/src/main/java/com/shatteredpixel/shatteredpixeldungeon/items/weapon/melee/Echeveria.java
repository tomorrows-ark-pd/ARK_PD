package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hallucination;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silence;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.items.Bonk;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CustomeSet;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfWealth;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Door;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DroneSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Glow_dronSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Rock_CrabSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Echeveria extends MeleeWeapon{
    public static final String AC_SUMON = "SUMON";
    {
        image = ItemSpriteSheet.ECHEVERIA;
        hitSound = Assets.Sounds.ATK_SPIRITBOW;
        hitSoundPitch = 1.32f;

        tier = 5;
        RCH = 2;

        defaultAction = AC_SUMON;
        usesTargeting = true;
    }

    protected int collisionProperties = Ballistica.STOP_TARGET;

    @Override
    public int max(int lvl) {
        return  3*(tier-1) +            	//12 + 3
                lvl*(tier-2);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_SUMON);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_SUMON)) {
            if (!isEquipped(hero)) return;
            if (charge >= 30) GameScene.selectCell(zapper);
        }
    }

    @Override
    public void SPCharge(float value) {
        if (Dungeon.bossLevel()) value = 2;
        super.SPCharge(value);
    }

    protected static CellSelector.Listener zapper = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer target) {

            if (target != null) {

                final Echeveria ss;
                if (curItem instanceof Echeveria) {
                    ss = (Echeveria) Echeveria.curItem;

                    Ballistica shot = new Ballistica(curUser.pos, target, Ballistica.PROJECTILE);
                    int cell = shot.collisionPos;

                    if (target == curUser.pos || cell == curUser.pos) {
                        return;
                    }

                    curUser.sprite.zap(cell);

                    //attempts to target the cell aimed at if something is there, otherwise targets the collision pos.
                    if (Actor.findChar(target) != null)
                        QuickSlotButton.target(Actor.findChar(target));
                    else
                        QuickSlotButton.target(Actor.findChar(cell));

                    if (ss.tryToZap(curUser, target)) {
                        curUser.busy();
                        ss.fx(shot, new Callback() {
                            public void call() {
                                ss.onZap(shot);
                            }
                        });
                    }

                }
            }
        }

        @Override
        public String prompt() {
            return Messages.get(MidnightSword.class, "prompt");
        }
    };

    protected void fx( Ballistica bolt, Callback callback ) {
        MagicMissile.boltFromChar( curUser.sprite.parent,
                MagicMissile.FORCE,
                curUser.sprite,
                bolt.collisionPos,
                callback);
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }

    public boolean tryToZap(Hero owner, int target) {
        return true;
    }

    protected void onZap( Ballistica bolt ) {
        ArrayList<Integer> respawnPoints = new ArrayList<>();
        Char ch = Actor.findChar( bolt.collisionPos );
        if (ch != null && ch instanceof Mob) {
            if (ch.alignment == Char.Alignment.ALLY) return;
            for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                int p = ch.pos + PathFinder.NEIGHBOURS8[i];
                if (Actor.findChar(p) == null && Dungeon.level.passable[p]) {
                    respawnPoints.add(p);
                }
            }
            int spawnd = 0;
            while (respawnPoints.size() > 0 && spawnd == 0) {
                int index = Random.index(respawnPoints);

                Echeveria.PinkdogDrone drone = new Echeveria.PinkdogDrone();
                drone.setting(buffedLvl(), (Mob)ch);
                GameScene.add(drone);
                ScrollOfTeleportation.appear(drone, respawnPoints.get(index));

                charge -=50;
                updateQuickslot();

                respawnPoints.remove(index);
                spawnd++;
            }



        } else {
            Dungeon.level.pressCell(bolt.collisionPos);
        }

        Buff buff = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
        if (buff != null) buff.detach();
        buff = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
        if (buff != null) buff.detach();

        if (Dungeon.hero.buff(Bonk.BonkBuff.class) != null) Buff.detach(Dungeon.hero, Bonk.BonkBuff.class);

        Invisibility.dispel();

        if (setbouns()) curUser.spendAndNext(0.25f);
        else curUser.spendAndNext(1f);
    }

    @Override
    public String status() {
        if (chargeCap == 100)
            return Messages.format("%d%%", (int)charge);
        return null;
    }

    private boolean setbouns() {
        if (Dungeon.hero.belongings.getItem(RingOfWealth.class) != null) {
            if (Dungeon.hero.belongings.getItem(RingOfWealth.class).isEquipped(Dungeon.hero))
                return true;
        }
        return false;
    }

    @Override
    public String desc() {
        String info = Messages.get(this, "desc");
        if (setbouns()) info += "\n\n" + Messages.get( Echeveria.class, "setbouns");

        return info;
    }


    public static class PinkdogDrone extends NPC {
        {
            HP = 20;
            defenseSkill = 5;

            spriteClass = Glow_dronSprite.class;
            baseSpeed = 2f;

            immunities.add(Silence.class);
            alignment = Alignment.ALLY;
            flying = true;

            state = HUNTING;
        }

        Mob LockTarget;
        int targetId = -1;
        int AttackCount = 1;

        @Override
        public int damageRoll() {
            return Random.IntRange(1+(maxLvl/2), 2+maxLvl);
        }

        @Override
        public int attackSkill(Char target) {
            return INFINITE_ACCURACY;
        }

        public void setting(int setlvl, Mob target)
        {
            maxLvl = setlvl;
            LockTarget = target;
        }

        @Override
        protected boolean act() {
            if (AttackCount >= 6) { die(this); return true;}

            HP -= 2;
            if (HP < 1) {
                die(this);
                return true;
            }

            if (targetId != -1){
                Actor ch = Actor.findById(targetId);
                targetId = -1;
                if (ch instanceof Mob){
                    LockTarget = (Mob) ch;
                }}

            if (LockTarget != null && LockTarget.isAlive()) enemy = LockTarget;
            else {
                die(this);
                return true;
            }
            return super.act();
        }

        @Override
        protected boolean canAttack(Char enemy) {
            return this.fieldOfView[enemy.pos] && Dungeon.level.distance(this.pos, enemy.pos) <= 4;
        }

        @Override
        public boolean attack(Char enemy) {
            if (enemy == null) return false;

            boolean visibleFight = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[enemy.pos];

            if (enemy.isInvulnerable(getClass())) {

                if (visibleFight) {
                    enemy.sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "invulnerable"));

                    Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY, 1f, Random.Float(0.96f, 1.05f));
                }

                return false;
            }
            else if (hit( this, enemy, true )) {

                int dmg = damageRoll() * AttackCount;
                enemy.damage( dmg, this );
                enemy.sprite.flash();

                ArrayList<Integer> respawnPoints = new ArrayList<>();
                for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                    int p = enemy.pos + PathFinder.NEIGHBOURS8[i];
                    if (Actor.findChar(p) == null && Dungeon.level.passable[p]) {
                        respawnPoints.add(p);
                    }
                }
                int spawnd = 0;
                while (respawnPoints.size() > 0 && spawnd == 0) {
                    int index = Random.index(respawnPoints);
                    ScrollOfTeleportation.appear(this, respawnPoints.get(index));
                    respawnPoints.remove(index);
                    spawnd++;
                }

                AttackCount++;

                if (Dungeon.level.heroFOV[pos]) Sample.INSTANCE.play(Assets.Sounds.HIT_MAGIC, 1f, Random.Float(0.96f, 1.05f));
            } else {
                enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
            }
            return true;
        }



        public static final String TARGET = "targetId";
        public static final String ATTACK = "AttackCount";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(ATTACK, AttackCount);
            if (LockTarget != null) bundle.put(TARGET, LockTarget.id());
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            if (bundle.contains( TARGET )){
            targetId = bundle.getInt(TARGET);}
            AttackCount = bundle.getInt(ATTACK);
            enemySeen = true;
        }
    }
}

package com.shatteredpixel.shatteredpixeldungeon.items.wands.SP;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfAmplified;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.DamageWand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLivingEarth;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.EarthGuardianSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class StaffOfMudrock extends DamageWand {
    private static ItemSprite.Glowing COL = new ItemSprite.Glowing( 0x8B4513 );
    {
        image = ItemSpriteSheet.WAND_LIVING_EARTH;
    }

    @Override
    public int min(int lvl) {
        return 2;
    }

    @Override
    public int max(int lvl) {
        return 4 + lvl+ RingOfAmplified.DamageBonus(Dungeon.hero);
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return COL;
    }

    @Override
    protected void onZap(Ballistica bolt) {
        Char ch = Actor.findChar(bolt.collisionPos);
        int damage = damageRoll();
        int armorToAdd = buffedLvl() + damage * 2;

        StaffOfMudrock.EarthGuardian guardian = null;
        for (Mob m : Dungeon.level.mobs){
            if (m instanceof StaffOfMudrock.EarthGuardian){
                guardian = (StaffOfMudrock.EarthGuardian) m;
                break;
            }
        }

        StaffOfMudrock.RockArmor buff = curUser.buff(StaffOfMudrock.RockArmor.class);
        if (ch == null){
            armorToAdd = 0;
        } else {
            if (buff == null && guardian == null) {
                buff = Buff.affect(curUser, StaffOfMudrock.RockArmor.class);
            }
            if (buff != null) {
                buff.addArmor( buffedLvl(), armorToAdd);
            }
        }

        //shooting at the guardian
        if (guardian != null && guardian == ch){
            guardian.sprite.centerEmitter().burst(MagicMissile.EarthParticle.ATTRACT, 8 + buffedLvl() / 2);
            guardian.setInfo(curUser, buffedLvl(), armorToAdd);
            processSoulMark(guardian, chargesPerCast());
            Sample.INSTANCE.play( Assets.Sounds.HIT_MAGIC, 1, 0.9f * Random.Float(0.87f, 1.15f) );

            //shooting the guardian at a location
        } else if ( guardian == null && buff != null && buff.armor >= buff.armorToGuardian()){

            //create a new guardian
            guardian = new StaffOfMudrock.EarthGuardian();
            guardian.setInfo(curUser, buffedLvl(), buff.armor);

            //if the collision pos is occupied (likely will be), then spawn the guardian in the
            //adjacent cell which is closes to the user of the wand.
            if (ch != null){

                ch.sprite.centerEmitter().burst(MagicMissile.EarthParticle.BURST, 5 + buffedLvl()/2);

                processSoulMark(ch, chargesPerCast());
                if (ch.properties().contains(Char.Property.SARKAZ) == true) {
                    damage *= 2f;
                }
                ch.damage(damage, this);

                int closest = -1;
                boolean[] passable = Dungeon.level.passable;

                for (int n : PathFinder.NEIGHBOURS9) {
                    int c = bolt.collisionPos + n;
                    if (passable[c] && Actor.findChar( c ) == null
                            && (closest == -1 || (Dungeon.level.trueDistance(c, curUser.pos) < (Dungeon.level.trueDistance(closest, curUser.pos))))) {
                        closest = c;
                    }
                }

                if (closest == -1){
                    curUser.sprite.centerEmitter().burst(MagicMissile.EarthParticle.ATTRACT, 8 + buffedLvl()/2);
                    return; //do not spawn guardian or detach buff
                } else {
                    guardian.pos = closest;
                    GameScene.add(guardian, 1);
                    Dungeon.level.occupyCell(guardian);
                }

                if (ch.alignment == Char.Alignment.ENEMY || ch.buff(Amok.class) != null) {
                    guardian.aggro(ch);
                }

            } else {
                guardian.pos = bolt.collisionPos;
                GameScene.add(guardian, 1);
                Dungeon.level.occupyCell(guardian);
            }

            guardian.sprite.centerEmitter().burst(MagicMissile.EarthParticle.ATTRACT, 8 + buffedLvl()/2);
            buff.detach();
            Sample.INSTANCE.play( Assets.Sounds.HIT_MAGIC, 1, 0.9f * Random.Float(0.87f, 1.15f) );

            //shooting at a location/enemy with no guardian being shot
        } else {

            if (ch != null) {

                ch.sprite.centerEmitter().burst(MagicMissile.EarthParticle.BURST, 5 + buffedLvl() / 2);

                processSoulMark(ch, chargesPerCast());
                if (ch.properties().contains(Char.Property.SARKAZ) == true) {
                    damage *= 1.5f;
                }
                ch.damage(damage, this);
                Sample.INSTANCE.play( Assets.Sounds.HIT_MAGIC, 1, 0.8f * Random.Float(0.87f, 1.15f) );

                if (guardian == null) {
                    curUser.sprite.centerEmitter().burst(MagicMissile.EarthParticle.ATTRACT, 8 + buffedLvl() / 2);
                } else {
                    guardian.sprite.centerEmitter().burst(MagicMissile.EarthParticle.ATTRACT, 8 + buffedLvl() / 2);
                    guardian.setInfo(curUser, buffedLvl(), armorToAdd);
                    if (ch.alignment == Char.Alignment.ENEMY || ch.buff(Amok.class) != null) {
                        guardian.aggro(ch);
                    }
                }

            } else {
                Dungeon.level.pressCell(bolt.collisionPos);
            }
        }

    }

    @Override
    protected void fx(Ballistica bolt, Callback callback) {
        MagicMissile.boltFromChar(curUser.sprite.parent,
                MagicMissile.EARTH,
                curUser.sprite,
                bolt.collisionPos,
                callback);
        Sample.INSTANCE.play(Assets.Sounds.ZAP);
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        StaffOfMudrock.EarthGuardian guardian = null;
        for (Mob m : Dungeon.level.mobs){
            if (m instanceof StaffOfMudrock.EarthGuardian){
                guardian = (StaffOfMudrock.EarthGuardian) m;
                break;
            }
        }

        int armor = Math.round(damage*0.33f);

        if (guardian != null){
            guardian.sprite.centerEmitter().burst(MagicMissile.EarthParticle.ATTRACT, 8 + buffedLvl() / 2);
            guardian.setInfo(Dungeon.hero, buffedLvl(), armor);
        } else {
            attacker.sprite.centerEmitter().burst(MagicMissile.EarthParticle.ATTRACT, 8 + buffedLvl() / 2);
            Buff.affect(attacker, StaffOfMudrock.RockArmor.class).addArmor( buffedLvl(), armor);
        }
    }

    public static class RockArmor extends Buff {

        private int wandLevel;
        private int armor;

        private void addArmor( int wandLevel, int toAdd ){
            this.wandLevel = Math.max(this.wandLevel, wandLevel);
            armor += toAdd;
            armor = Math.min(armor, 2*armorToGuardian());
        }

        private int armorToGuardian(){
            return 8 + wandLevel*4;
        }

        public int absorb( int damage ) {
            int block = damage - damage/2;
            if (armor <= block) {
                detach();
                return damage - armor;
            } else {
                armor -= block;
                return damage - block;
            }
        }

        @Override
        public int icon() {
            return BuffIndicator.ARMOR;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (armorToGuardian() - armor) / (float)armorToGuardian());
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get( this, "desc", armor, armorToGuardian());
        }

        private static final String WAND_LEVEL = "wand_level";
        private static final String ARMOR = "armor";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(WAND_LEVEL, wandLevel);
            bundle.put(ARMOR, armor);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            wandLevel = bundle.getInt(WAND_LEVEL);
            armor = bundle.getInt(ARMOR);
        }
    }

    public static class EarthGuardian extends NPC {

        {
            spriteClass = EarthGuardianSprite.class;

            alignment = Alignment.ALLY;
            state = HUNTING;
            intelligentAlly = true;
            WANDERING = new StaffOfMudrock.EarthGuardian.Wandering();

            //before other mobs
            actPriority = MOB_PRIO + 1;

            HP = HT = 0;
        }

        private int wandLevel = -1;

        private void setInfo(Hero hero, int wandLevel, int healthToAdd){
            if (wandLevel > this.wandLevel) {
                this.wandLevel = wandLevel;
                HT = 22 + 9 * wandLevel;
            }
            HP = Math.min(HT, HP + healthToAdd);
            //half of hero's evasion
            defenseSkill = (hero.lvl + 4)/2;
        }

        @Override
        public int attackSkill(Char target) {
            //same as the hero
            return 2*defenseSkill + 5;
        }

        @Override
        public int attackProc(Char enemy, int damage) {
            if (enemy instanceof Mob) ((Mob)enemy).aggro(this);
            return super.attackProc(enemy, damage);
        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange(4, 7 + Dungeon.depth/2);
        }

        @Override
        public int drRoll() {
            if (Dungeon.isChallenged(Challenges.NO_ARMOR)){
                return Random.NormalIntRange(wandLevel, 4 + wandLevel);
            } else {
                return Random.NormalIntRange(2 + wandLevel, 5 + 3 * wandLevel);
            }
        }

        @Override
        public String description() {
            if (Dungeon.isChallenged(Challenges.NO_ARMOR)){
                return Messages.get(this, "desc", wandLevel, 2 + wandLevel);
            } else {
                return Messages.get(this, "desc", wandLevel, 3 + 3*wandLevel);
            }

        }

        {
            immunities.add( AllyBuff.class );
        }

        private static final String DEFENSE = "defense";
        private static final String WAND_LEVEL = "wand_level";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(DEFENSE, defenseSkill);
            bundle.put(WAND_LEVEL, wandLevel);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            defenseSkill = bundle.getInt(DEFENSE);
            wandLevel = bundle.getInt(WAND_LEVEL);
        }

        private class Wandering extends Mob.Wandering{

            @Override
            public boolean act(boolean enemyInFOV, boolean justAlerted) {
                if (!enemyInFOV){
                    Buff.affect(Dungeon.hero, StaffOfMudrock.RockArmor.class).addArmor(wandLevel, HP);
                    Dungeon.hero.sprite.centerEmitter().burst(MagicMissile.EarthParticle.ATTRACT, 8 + wandLevel/2);
                    destroy();
                    sprite.die();
                    return true;
                } else {
                    return super.act(enemyInFOV, justAlerted);
                }
            }

        }

    }
}
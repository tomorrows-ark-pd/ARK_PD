package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silence;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ChaliceOfBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CustomeSet;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.UnstableSpellbook;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfMistress;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAggression;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Mon3terSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class CatGun extends MeleeWeapon {
    public static final String AC_ZAP = "ZAP";
    {
        image = ItemSpriteSheet.CATGUN;
        hitSound = Assets.Sounds.ATK_SPIRITBOW;
        hitSoundPitch = 1f;
        defaultAction = AC_ZAP;

        tier = 5;
        RCH = 2;
    }

    @Override
    public int max(int lvl) {
        return  3*(tier+1) +   //18 + 4
                lvl*(tier-1); }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        SPCharge_cat(Random.IntRange(2,3));
        return super.proc(attacker, defender, damage);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_ZAP);
        return actions;
    }

    public void SPCharge_cat(int value) {
        charge = Math.min(charge+value, chargeCap);
        updateQuickslot();
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_ZAP)) {
            if (charge >= chargeCap) {
                ArrayList<Integer> respawnPoints = new ArrayList<>();

                for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                    int p = hero.pos + PathFinder.NEIGHBOURS8[i];
                    if (Actor.findChar(p) == null && Dungeon.level.passable[p]) {
                        respawnPoints.add(p);
                    }
                }
                int spawnd = 0;
                while (respawnPoints.size() > 0 && spawnd == 0) {
                    int index = Random.index(respawnPoints);

                    CatGun.Mon3tr tr = new CatGun.Mon3tr();
                    tr.setting(buffedLvl());
                    GameScene.add(tr);
                    ScrollOfTeleportation.appear(tr, respawnPoints.get(index));

                    respawnPoints.remove(index);
                    Sample.INSTANCE.play( Assets.Sounds.SKILL_MON1 );
                    spawnd++;
                }
                charge = 0;
            }
            else if (charge < chargeCap && catsetbouns()) {
                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                    if (mob instanceof Mon3tr) {
                        int gaincharge;
                        if (mob.HT / 2 > mob.HP) gaincharge = 25;
                        else if (mob.HT / 4 > mob.HP) gaincharge = 15;
                        else gaincharge = 40;

                        mob.die(this);
                        charge = Math.min(charge + gaincharge, chargeCap);
                        updateQuickslot();
                    }
                }
            }
        }
    }

    @Override
    public String status() {

        //if the artifact isn't IDed, or is cursed, don't display anything
        if (!isIdentified() || cursed) {
            return null;
        }
        //display as percent
        if (chargeCap == 100)
            return Messages.format("%d%%", (int)charge);


        //otherwise, if there's no charge, return null.
        return null;
    }

    @Override
    public String desc() {
        String info;
        if (catsetbouns()) {
                info = Messages.get(this, "desc_sp");
                info += "\n\n" + Messages.get(CatGun.class, "setbouns");
                return info;
            }
        info = Messages.get(this, "desc");
        return info;
    }

    public static boolean catsetbouns() {
        if (!(Dungeon.hero.belongings.weapon instanceof CatGun)) return false;

        if (Dungeon.hero.belongings.getItem(RingOfMistress.class) != null && Dungeon.hero.belongings.getItem(ChaliceOfBlood.class) != null
                && Dungeon.hero.belongings.getItem(UnstableSpellbook.class) != null) {
            if (Dungeon.hero.belongings.getItem(RingOfMistress.class).isEquipped(Dungeon.hero) && Dungeon.hero.belongings.getItem(ChaliceOfBlood.class).isEquipped(Dungeon.hero)
                    && Dungeon.hero.belongings.getItem(UnstableSpellbook.class).isEquipped(Dungeon.hero))
                return true;
        }
        return false;
    }


    public static class Mon3tr extends NPC {
        {
            spriteClass = Mon3terSprite.class;
            baseSpeed = 3f;

            state = HUNTING;
            immunities.add(Silence.class);
            alignment = Alignment.ALLY;

            WANDERING = new Wandering();
        }

        private int blinkCooldown = 0;

        @Override
        protected boolean getCloser( int target ) {
            if (fieldOfView[target] && Dungeon.level.distance( pos, target ) > 2 && blinkCooldown <= 0 && target != Dungeon.hero.pos) {

                blink( target );
                spend( -1 / speed() );
                return true;

            } else {

                blinkCooldown--;
                return super.getCloser( target );

            }
        }

        private class Wandering extends Mob.Wandering {

            @Override
            public boolean act( boolean enemyInFOV, boolean justAlerted ) {
                if ( enemyInFOV ) {

                    enemySeen = true;

                    notice();
                    alerted = true;
                    state = HUNTING;
                    target = enemy.pos;

                } else {

                    enemySeen = false;

                    int oldPos = pos;
                    target = Dungeon.hero.pos;
                    //always move towards the hero when wandering
                    if (getCloser( target )) {
                        //moves 2 tiles at a time when returning to the hero
                        if (!Dungeon.level.adjacent(target, pos)){
                            getCloser( target );
                        }
                        spend( 1 / speed() );
                        return moveSprite( oldPos, pos );
                    } else {
                        spend( TICK );
                    }

                }
                return true;
            }

        }

        private void blink( int target ) {

            Ballistica route = new Ballistica( pos, target, Ballistica.PROJECTILE);
            int cell = route.collisionPos;

            //can't occupy the same cell as another char, so move back one.
            if (Actor.findChar( cell ) != null && cell != this.pos)
                cell = route.path.get(route.dist-1);

            if (Dungeon.level.avoid[ cell ]){
                ArrayList<Integer> candidates = new ArrayList<>();
                for (int n : PathFinder.NEIGHBOURS8) {
                    cell = route.collisionPos + n;
                    if (Dungeon.level.passable[cell] && Actor.findChar( cell ) == null) {
                        candidates.add( cell );
                    }
                }
                if (candidates.size() > 0)
                    cell = Random.element(candidates);
                else {
                    blinkCooldown = 1;
                    return;
                }
            }

            ScrollOfTeleportation.appear( this, cell );
            Sample.INSTANCE.play( Assets.Sounds.SKILL_MON2 );

            blinkCooldown = 1;
        }

        @Override
        protected boolean act() {
            if (this.buff(StoneOfAggression.Aggression.class) == null) {
                Buff.prolong(this, StoneOfAggression.Aggression.class, StoneOfAggression.Aggression.DURATION);}

            CustomeSet.CustomSetBuff setBuff = Dungeon.hero.buff( CustomeSet.CustomSetBuff.class);
            if (isAlive() || HP <= 1) {

                if (setBuff != null) {
                    int adddamage = HT / (50 + setBuff.itemLevel() * 3);
                    if (adddamage < 1) adddamage = 1;
                    if (state == WANDERING && adddamage > 1) HP -= adddamage / 2;
                    else HP -= adddamage;
                } else {
                    int adddamage = HT /50;
                    if (adddamage < 1) adddamage = 1;
                    if (state == WANDERING && adddamage > 1) HP -= adddamage / 2;
                    else  HP -= adddamage;
                }
            }

            if (HP < 1) {
                this.die(this);
                return true;
            }
            return super.act();
        }

        @Override
        public int attackProc(Char enemy, int damage) {
            CustomeSet.CustomSetBuff setBuff = Dungeon.hero.buff( CustomeSet.CustomSetBuff.class);
            if (setBuff != null) {
                int n = 20 + setBuff.itemLevel() * 3;
                HP -= HT/n;
            }
            else HP -= HT/20;

            if (catsetbouns()) {
                damage *= 1.3f;
                Buff.affect(Dungeon.hero, ArtifactRecharge.class).prolong(2).ignoreHornOfPlenty=false;
                Buff.affect(Dungeon.hero, Bless.class, 2f);
            }

            return super.attackProc(enemy, damage);
        }

        @Override
        public void die(Object cause) {
            if (catsetbouns()) {
                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                    if (Dungeon.level.adjacent(mob.pos, this.pos) && mob.alignment != Char.Alignment.ALLY) {
                        int dmg = this.damageRoll();
                        CellEmitter.get( mob.pos ).burst(BlastParticle.FACTORY, 6 );
                        mob.damage(dmg, this);
                    }
                }
            }
            super.die(cause);
        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange( 18 + maxLvl * 4, 24 + maxLvl * 6 );
        }

        @Override
        public int attackSkill( Char target ) {
            return 25 + maxLvl * 3;
        }

        @Override
        public int drRoll() {
            return Random.NormalIntRange(maxLvl / 2, 2 + maxLvl);
        }

        public void setting(int setlvl)
        {
            CustomeSet.CustomSetBuff setBuff = Dungeon.hero.buff( CustomeSet.CustomSetBuff.class);
            int itembuff = 0;
            if (setBuff != null) itembuff = setBuff.itemLevel();
            HP=HT=120 + setlvl * 20;
            defenseSkill = 10 + setlvl * 2;
            maxLvl = setlvl + itembuff / 2;
        }

        private static final String BLINK = "blinkcooldown";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(BLINK, blinkCooldown);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            blinkCooldown = bundle.getInt(BLINK);
            enemySeen = true;
        }
    }

}

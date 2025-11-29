package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.LeatherArmor;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KazemaruSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class KazemaruWeapon extends MeleeWeapon {
    {
        image = ItemSpriteSheet.KAZEMARU;
        hitSound = Assets.Sounds.HIT_SLASH2;
        hitSoundPitch = 1f;

        tier = 4;
    }

    @Override
    public int max(int lvl) {
        return  5*(tier) +
                lvl*(tier+1);
    }

    @Override
    public int damageRoll(Char owner) {
        if (owner instanceof Hero) {
            Hero hero = (Hero) owner;
            Char enemy = hero.enemy();
            if (enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)) {
                //deals 50% toward max to max on surprise, instead of min to max.
                int diff = max() - min();
                int damage;
                if (Dungeon.hero.belongings.armor instanceof LeatherArmor) {
                    damage = augment.damageFactor(Random.NormalIntRange(min() + Math.round(diff * 0.30f), max()));
                }
                else {
                    damage = augment.damageFactor(Random.NormalIntRange(min() + Math.round(diff * 0.30f), max()));
                }
                int exStr = hero.STR() - STRReq();
                if (exStr > 0) {
                    damage += Random.IntRange(0, exStr);
                }
                return damage;

            }
        }
        return super.damageRoll(owner);
    }

    public String statsInfo() {
        if (isIdentified()) {
            return Messages.get(this, "stats_desc", 15+buffedLvl(), 20+(buffedLvl()*5));
        } else {
            return Messages.get(this, "typical_stats_desc", 15, 20);
        }
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        int procDamage = super.proc(attacker, defender, damage);

        if (defender.HP > procDamage && Random.Int(7) == 0) {
            ArrayList<Integer> respawnPoints = new ArrayList<>();

            for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                int p = defender.pos + PathFinder.NEIGHBOURS8[i];
                if (Actor.findChar(p) == null && Dungeon.level.passable[p]) {
                    respawnPoints.add(p);
                }
            }

            Integer[] spawnPoints = respawnPoints.toArray(new Integer[0]);
            int pos = Random.element(spawnPoints);
            summonSubstitute(pos, defender.pos);
        }

        return procDamage;
    }

    private void summonSubstitute(int pos, int target) {
        KazemaruSummon summon = new KazemaruSummon();
        summon.setLevel(buffedLvl());
        summon.setTarget(target);
        summon.pos = pos;
        GameScene.add(summon);
    }

    public static class KazemaruSummon extends Mob {
        {
            HP = HT = 1;

            spriteClass = KazemaruSprite.class;

            flying = true;
            state = HUNTING;
            alignment = Alignment.ALLY;
        }

        @Override
        public void onAttackComplete() {
            attack( enemy );
            die(this);
            next();
        }

        @Override
        public int attackSkill( Char target ) {
            return INFINITE_ACCURACY;
        }

        @Override
        public boolean attack(Char enemy) {
            if (enemy == null) return false;

            boolean visibleFight = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[enemy.pos];

            if (hit( this, enemy, true )) {

                int dmg = damageRoll();
                enemy.damage( dmg, this );
                enemy.sprite.bloodBurstA( sprite.center(), dmg );
                enemy.sprite.flash();

                if (Dungeon.level.heroFOV[pos]) Sample.INSTANCE.play(Assets.Sounds.HIT_SLASH, 1f, Random.Float(0.96f, 1.05f));

                if (enemy == Dungeon.hero && !enemy.isAlive()) {
                    Dungeon.fail( getClass() );
                }
            } else {
                enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
            }

            if (!enemy.isAlive() && visibleFight) {
                if (enemy == Dungeon.hero) {

                    Dungeon.fail( getClass() );
                    GLog.n( Messages.capitalize(Messages.get(Char.class, "kill", name())) );
                }
            }

            return true;

        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange(maxLvl + 15, 20 + (maxLvl * 5));
        }

        public void setLevel(int wlvl) {
            maxLvl = wlvl;
        }

        public void setTarget(int pos) {
            target = pos;
        }
    }
}

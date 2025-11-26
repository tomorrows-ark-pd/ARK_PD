package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.NervousImpairment;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Dario;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Sea_DrifterSprite;
import com.watabou.utils.Random;

public class FloatingSeaDrifter extends Mob {
        {
            spriteClass = Sea_DrifterSprite.class;

            HP = HT = 65;

            EXP = 14;
            maxLvl = 29;

            flying = true;

            loot = Gold.class;
            lootChance = 0.34f;

            properties.add(Property.SEA);
            immunities.add(Paralysis.class);
        }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(25, 33);
    }

    @Override
    public int attackSkill( Char target ) {
        return 32;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 14);
    }

    @Override
    public int defenseSkill(Char enemy) {
            if (enemy instanceof Hero) {
                if (Dungeon.hero.belongings.weapon.getClass() == MissileWeapon.class) {
                    return 0;
                }
            }

            return 70;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (enemy.buff(NervousImpairment.class) == null) {
            Buff.affect(enemy, NervousImpairment.class);
        }
        else enemy.buff(NervousImpairment.class).Sum(10);

        return super.attackProc(enemy, damage);
    }

    @Override
    public void die( Object cause ) {
        super.die(cause);
        Dario.Quest.process();
    }
}

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.NervousImpairment;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Dario;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SanityPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.GunWeapon;
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
        defenseSkill = 50;

        loot = new SanityPotion();
        lootChance = 0.1f;

        properties.add(Property.SEA);
        immunities.add(Paralysis.class);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(25, 33);
    }

    @Override
    public int attackSkill(Char target) {
        return 32;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 14);
    }

    @Override
    public int defenseSkill(Char enemy) {
        if (enemy instanceof Hero) {
            KindOfWeapon weapon = Dungeon.hero.belongings.attackingWeapon();
            if (weapon != null &&
                    (weapon instanceof MissileWeapon
                            || weapon instanceof GunWeapon)) {
                return 0;
            }
        }

        return super.defenseSkill(enemy);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (enemy.alignment == Alignment.ALLY) {
            Buff.affect(enemy, NervousImpairment.class).sum(10);
        }

        return super.attackProc(enemy, damage);
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        Dario.Quest.process();
    }

    @Override
    public void onConvertKilled(Object cause) {
        super.onConvertKilled(cause);
        Dario.Quest.process();
    }
}

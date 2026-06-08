package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silence;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Mutant_SpiderSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.StrikerSprite;
import com.watabou.utils.Random;

public class MutantSpider extends Mob {
    {
        spriteClass = Mutant_SpiderSprite.class;

        HP = HT = 250;
        defenseSkill = 18;

        EXP = 23;
        maxLvl = 38;

        loot = new StoneOfEnchantment();
        lootChance = 1f;

        immunities.add(AllyBuff.class);
        immunities.add(Silence.class);
        immunities.add(Ooze.class);
        properties.add(Property.INFECTED);
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return this.fieldOfView[enemy.pos] && Dungeon.level.distance(this.pos, enemy.pos) <= 2;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        Buff.affect(enemy, Slow.class, 2f);
        return super.attackProc(enemy, damage);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(32,40);
    }

    @Override
    public int attackSkill( Char target ) {
        return 37;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(4, 18); }

}

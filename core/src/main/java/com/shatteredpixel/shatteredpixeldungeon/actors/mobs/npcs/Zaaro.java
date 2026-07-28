/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZaaroSprite;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

//summoned by Catastrofe; uses its own FOV like any other mob, but that FOV is shared back to the hero (see Level.updateFieldOfView)
public class Zaaro extends NPC {

    {
        spriteClass = ZaaroSprite.class;

        HP = HT = 1;

        alignment = Alignment.ALLY;
        state = WANDERING;

        baseSpeed = 2f;

        immunities.add(Roots.class);
        immunities.add(Paralysis.class);
        immunities.add(Vertigo.class);
        immunities.add(AllyBuff.class);
    }

    //flips this Zaaro hostile: used by Catastrofe's cursed backfire, which turns one summon against the hero
    public void corrupt() {
        alignment = Alignment.ENEMY;
        state = HUNTING;
    }

    @Override
    public int defenseSkill(Char enemy) {
        return INFINITE_EVASION;
    }

    @Override
    public int damageRoll() {
        int max = maxDamage();
        return Random.NormalIntRange(max / 2, max);
    }

    //mirrors the max end of damageRoll(); used by Catastrofe's desc() to display the same range without duplicating the formula
    public static int maxDamage() {
        Hero hero = Dungeon.hero;
        KindOfWeapon weapon = hero.belongings.weapon();
        if (weapon != null) {
            return weapon.max() + hero.lvl / 2;
        } else {
            return hero.damageRoll();
        }
    }

    @Override
    public boolean attack(Char enemy) {
        if (enemy == null) return false;

        int dmg = damageRoll();
        int splash = Math.round(dmg * 0.66f);

        enemy.damage(dmg, this);

        for (int n : PathFinder.NEIGHBOURS8) {
            Char ch = Actor.findChar(enemy.pos + n);
            if (ch != null && ch != enemy && ch.alignment != Alignment.ALLY) {
                ch.damage(splash, this);
            }
        }

        CellEmitter.get(enemy.pos).burst(SmokeParticle.FACTORY, 15);
        for (int n : PathFinder.NEIGHBOURS8) {
            int c = enemy.pos + n;
            if (c >= 0 && c < Dungeon.level.length() && Dungeon.level.passable[c]) {
                CellEmitter.get(c).burst(SmokeParticle.FACTORY, 4);
            }
        }

        die(null);

        return true;
    }
}

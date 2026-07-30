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

package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Zaaro;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Catastrofe extends Artifact {

    {
        //placeholder art; ARTIFACT_HOLDER is the generic 16x16 "empty slot" icon, swap for real art later
        image = ItemSpriteSheet.ARTIFACT_ZAARO;

        levelCap = 0;

        chargeCap = 1;
        charge = chargeCap;

        defaultAction = AC_SUMMON;
    }

    public static final String AC_SUMMON = "SUMMON";

    private static final int SUMMON_COUNT = 3;
    private static final int RECHARGE_TURNS = 150;
    private static final int CURSED_SPAWN_MIN_DIST = 10;
    //effectively lasts as long as the Zaaro does; CharAwareness auto-detaches once its target is gone
    private static final int VISION_SHARE_DURATION = 999;

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (isEquipped(hero)) actions.add(AC_SUMMON);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (action.equals(AC_SUMMON)) {
            if (!isEquipped(hero)) {
                GLog.i(Messages.get(Artifact.class, "need_to_equip"));
            } else if (charge < 1) {
                GLog.i(Messages.get(this, "no_charge"));
            } else {
                charge = 0;
                updateQuickslot();

                if (cursed) {
                    summonCursed(hero);
                } else {
                    summonAllies(hero);
                }

                Talent.onArtifactUsed(hero);

                curUser.spendAndNext(1f);
            }
        }
    }

    private void summonAllies(Hero hero) {
        ArrayList<Integer> respawnPoints = new ArrayList<>();
        for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
            int p = hero.pos + PathFinder.NEIGHBOURS8[i];
            if (Actor.findChar(p) == null && Dungeon.level.passable[p]) {
                respawnPoints.add(p);
            }
        }

        int spawned = 0;
        while (spawned < SUMMON_COUNT && !respawnPoints.isEmpty()) {
            int index = Random.index(respawnPoints);

            Zaaro zaaro = new Zaaro();
            GameScene.add(zaaro);
            ScrollOfTeleportation.appear(zaaro, respawnPoints.get(index));
            Buff.append(hero, TalismanOfForesight.CharAwareness.class, VISION_SHARE_DURATION).charID = zaaro.id();

            respawnPoints.remove(index);
            spawned++;
        }
    }

    private void summonCursed(Hero hero) {
        int cell = findCursedSpawnCell(hero);

        Zaaro zaaro = new Zaaro();
        zaaro.corrupt();
        GameScene.add(zaaro);
        ScrollOfTeleportation.appear(zaaro, cell);

        GLog.n(Messages.get(this, "summon_cursed"));
    }

    //picks a passable, unoccupied cell at least CURSED_SPAWN_MIN_DIST away from the hero;
    private static int findCursedSpawnCell(Hero hero) {
        for (int i = 0; i < 200; i++) {
            int candidate = Random.Int(Dungeon.level.length());
            if (Dungeon.level.passable[candidate] && Actor.findChar(candidate) == null
                    && Dungeon.level.distance(hero.pos, candidate) >= CURSED_SPAWN_MIN_DIST) {
                return candidate;
            }
        }
        return Dungeon.level.randomRespawnCell(null);
    }

    @Override
    public String desc() {
        int max = Zaaro.maxDamage();
        return Messages.get(this, "desc", max / 2, max);
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new zaaroCharge();
    }

    public class zaaroCharge extends ArtifactBuff {

        private float turnsSinceCharge = 0;

        @Override
        public boolean act() {
            LockedFloor lock = target.buff(LockedFloor.class);
            if ((lock == null || lock.regenOn()) && !Dungeon.isInRhodes()) {
                if (charge < chargeCap) {
                    turnsSinceCharge += RingOfEnergy.artifactChargeMultiplier(target);
                    if (turnsSinceCharge >= RECHARGE_TURNS) {
                        turnsSinceCharge = 0;
                        charge = chargeCap;
                        updateQuickslot();
                    }
                }
            } else {
                turnsSinceCharge = 0;
            }

            spend(TICK);
            return true;
        }

        private static final String TURNS_SINCE_CHARGE = "turns_since_charge";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(TURNS_SINCE_CHARGE, turnsSinceCharge);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            turnsSinceCharge = bundle.getFloat(TURNS_SINCE_CHARGE);
        }
    }
}

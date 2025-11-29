/*
 * Pixel Dungeon
 * Copyright (C) 2025-2025 Junwoo Lee
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

package com.shatteredpixel.shatteredpixeldungeon.levels.features;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.TomorrowRogueNight;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.NervousImpairment;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class SeaTerror implements Bundlable {

    public int pos;
    public boolean isActive;

	public void activate() {
        if (isCovered()) {
            return;
        }

        boolean updated = false;
        int evaporatedTiles = 0;
        evaporatedTiles = Random.chances(new float[]{0, 0, 0, 2, 1, 1});
        for (int i = 0; i < evaporatedTiles; i++) {
            int relativeCell = Random.Int(8);
            if (Dungeon.level.map[pos+ PathFinder.NEIGHBOURS8[relativeCell]] == Terrain.EMPTY
                    || Dungeon.level.map[pos+PathFinder.NEIGHBOURS8[relativeCell]] == Terrain.EMPTY_SP
                    || Dungeon.level.map[pos+PathFinder.NEIGHBOURS8[relativeCell]] == Terrain.EMPTY_DECO
                    || Dungeon.level.map[pos+PathFinder.NEIGHBOURS8[relativeCell]] == Terrain.WATER
                    || Dungeon.level.map[pos+PathFinder.NEIGHBOURS8[relativeCell]] == Terrain.SEA_TERROR) {

                if (Dungeon.level.seaTerrors.get(pos+PathFinder.NEIGHBOURS8[relativeCell]) != null) {
                    // skip creation if brand already exists
                    continue;
                }

                Dungeon.level.addSeaTerror(pos+PathFinder.NEIGHBOURS8[relativeCell]);
                //Dungeon.level.map[pos+PathFinder.NEIGHBOURS8[i]] = Terrain.SEA_TERROR;

                if (TomorrowRogueNight.scene() instanceof GameScene) {
                    if (Dungeon.level.heroFOV[pos]) {
                        CellEmitter.get(pos+PathFinder.NEIGHBOURS8[relativeCell]).burst(Speck.factory(Speck.BUBBLE), 10);
                        GameScene.updateMap( pos+PathFinder.NEIGHBOURS8[relativeCell] );
                        Dungeon.observe();
                    }
                }
            }
        }
	}

    public void destroy() {
        Dungeon.level.destroySeaTerror( pos );

        if (Dungeon.level.heroFOV[pos]) {
            CellEmitter.get( pos ).burst(FlameParticle.FACTORY, 6);
        }
    }

    public void spendTime( Char ch, float time ) {
        if (isCovered()) {
            return;
        }

        if (ch instanceof Hero) {
            if (ch.buff(NervousImpairment.class) == null) {
                Buff.affect(ch, NervousImpairment.class);
            }
            float nervousDamage = 2 * time;

            if (Dungeon.extrastage_Sea && Dungeon.depth >= 40) {
                // 이샤믈라 보스전일 경우 명흔 신경손상 2배
                nervousDamage *= 2;
            }

            ch.buff(NervousImpairment.class).sum(nervousDamage);
        }

        if (ch instanceof Mob) {
            ch.activateSeaTerror();
        }
    }

    private boolean isCovered() {
        // ignore all interactions if same position has a platform
        Platform platform = Dungeon.level.platforms.get(pos);
        return platform instanceof SeaPlatform;
    }

    private static final String POS = "pos";
    private static final String SEA_TERROR_ACTIVE = "seaTerrorActive";

    @Override
    public void restoreFromBundle(Bundle bundle) {
        pos = bundle.getInt(POS);
        isActive = bundle.getBoolean(SEA_TERROR_ACTIVE);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(POS, pos);
        bundle.put(SEA_TERROR_ACTIVE, isActive);
    }
}

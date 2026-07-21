package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Gavial;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC_Irene;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC_PhantomShadow;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC_Pilot;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Group;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.Arrays;

public class NewRhodesLevel4 extends Level {
    {
        color1 = 0x801500;
        color2 = 0xa68521;

        viewDistance = 99;
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILSE_RHODES;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_PRISON;
    }

    @Override
    public void create() {
        super.create();
        for (int i = 0; i < length(); i++) {
            int flags = Terrain.flags[map[i]];
            if ((flags & Terrain.PIT) != 0) {
                passable[i] = avoid[i] = false;
                solid[i] = true;
            }
        }
    }

    private static final int ROOM_TOP = 6;

    @Override
    protected boolean build() {

        setSize(65, 24);
        Arrays.fill(map, Terrain.CHASM);

        // 0이 가장 왼쪽 ~ 위쪽임. 1 늘어날 때마다 오른쪽 / 아래쪽으로 1칸
        // X랑 Y는 위치. W랑 H는 각각 길이/너비 같은거임. W랑 H는 최소 1 이상의 값을 줘야함.
        // 0,0,10,1 을 하면 X,Y가 0,0인 위치에서 시작해서 가로로 10칸, 세로로 1칸 크기로 한다는 것.
        // 주의 사항 : 최대값은 위의 setSize 값의 -1임. 그러니까 x값 합이 18이 되도록 하면 버그남.

        // 베이스. 건드리지 말 것
        Painter.fill(this, 0, 0, 65, 24, Terrain.EMPTY);
        Painter.fill(this, 0, 0, 65, 1, Terrain.AVOID);
        Painter.fill(this, 0, 23, 65, 1, Terrain.AVOID);
        Painter.fill(this, 0, 0, 1, 24, Terrain.AVOID);
        Painter.fill(this, 64, 0, 1, 24, Terrain.AVOID);

        Painter.fill(this, 0, 1, 65, 9, Terrain.AVOID);
        Painter.fill(this, 0, 18, 65, 5, Terrain.AVOID);

        Painter.fill(this, 23, 10, 7, 3, Terrain.AVOID);

        Painter.fill(this, 41, 8, 11, 3, Terrain.ENTRANCE);


        map[802] = Terrain.AVOID;
        map[679] = Terrain.AVOID;
        map[744] = Terrain.EMPTY;

        map[687] = Terrain.AVOID;
        map[752] = Terrain.AVOID;

        map[863] = Terrain.EMPTY;
        map[864] = Terrain.EMPTY;
        map[865] = Terrain.EMPTY;
        map[927] = Terrain.EMPTY;
        map[928] = Terrain.EMPTY;
        map[929] = Terrain.EMPTY;
        map[930] = Terrain.EMPTY;

        entrance = 695;
        exit = 870;

        feeling = Feeling.NONE;

        NewRhodesLevel4.CustomeMap vis = new NewRhodesLevel4.CustomeMap();
        vis.setRect(0, 0, width(), height());
        customTiles.add(vis);

        return true;
    }

    @Override
    protected void syncTransitionsFromFields() {
        transitions.clear();
        //entrance terrain leads back toward Rhodes 3 (branch 3)
        //center cell first so arrivals land at the middle of the area
        int centerCell = 9 * width() + 45; // (46,9) = center of (41,8)-(51,10)
        transitions.add(new LevelTransition(this, centerCell,
                LevelTransition.Type.BRANCH_ENTRANCE, 0, 3, LevelTransition.Type.BRANCH_EXIT));
        //remaining entrance terrain cells (41,8) to (51,10)
        for (int y = 8; y <= 10; y++) {
            for (int x = 41; x <= 51; x++) {
                int cell = y * width() + x;
                if (cell != centerCell) {
                    transitions.add(new LevelTransition(this, cell,
                            LevelTransition.Type.BRANCH_ENTRANCE, 0, 3, LevelTransition.Type.BRANCH_EXIT));
                }
            }
        }
        //exit leads to extra stages (depth 31, branch 0)
        transitions.add(new LevelTransition(this, exit,
                LevelTransition.Type.BRANCH_EXIT, 31, 0, LevelTransition.Type.REGULAR_ENTRANCE));
    }

    @Override
    protected void createMobs() {
    }

    public Actor addRespawner() {
        return null;
    }

    @Override
    protected void createItems() {
        Gavial.spawn(this, 748);
        NPC_Irene.spawn(this, 749);
        NPC_Pilot.spawn(this, 870);

        if (Dungeon.QuestCatPoint == 2 && !NPC_PhantomShadow.Clear) {
            NPC_PhantomShadow.spawn(this, 2);
        }
    }

    @Override
    public int randomRespawnCell(Char ch) {
        int cell;
        int count = 0;
        do {
            if (++count > 30) {
                return -1;
            }
            cell = entrance + PathFinder.NEIGHBOURS8[Random.Int(8)];
        } while (!passable[cell]
                || (Char.hasProp(ch, Char.Property.LARGE) && !openSpace[cell])
                || Actor.findChar(cell) != null);
        return cell;
    }


    @Override
    public Group addVisuals() {
        super.addVisuals();
        HallsLevel.addHallsVisuals(this, visuals);
        return visuals;
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
    }

    public static class CustomeMap extends CustomTilemap {

        {
            texture = Assets.Environment.RHODES_30F;
        }

        @Override
        public Tilemap create() {
            Tilemap v = super.create();
            int[] data = new int[tileW * tileH];
            for (int i = 0; i < data.length; i++) {
                data[i] = i;
            }

            v.map(data, tileW);
            return v;
        }
    }
}

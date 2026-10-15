package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.GreenCat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC_PhantomShadow;
import com.shatteredpixel.shatteredpixeldungeon.journal.quests.PhantomCatQuestLine;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Group;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.Arrays;

public class NewRhodesLevel3 extends Level {

    {
        color1 = 0x801500;
        color2 = 0xa68521;

        viewDistance = 34;
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

    @Override
    protected boolean build() {

        setSize(25, 27);
        Arrays.fill(map, Terrain.CHASM);

        // 0이 가장 왼쪽 ~ 위쪽임. 1 늘어날 때마다 오른쪽 / 아래쪽으로 1칸
        // X랑 Y는 위치. W랑 H는 각각 길이/너비 같은거임. W랑 H는 최소 1 이상의 값을 줘야함.
        // 0,0,10,1 을 하면 X,Y가 0,0인 위치에서 시작해서 가로로 10칸, 세로로 1칸 크기로 한다는 것.
        // 주의 사항 : 최대값은 위의 setSize 값의 -1임. 그러니까 x값 합이 18이 되도록 하면 버그남.

        // 베이스. 건드리지 말 것
        Painter.fill(this, 0, 0, 25, 27, Terrain.EMPTY);
        Painter.fill(this, 0, 0, 25, 6, Terrain.AVOID);
        Painter.fill(this, 0, 25, 25, 2, Terrain.AVOID);
        Painter.fill(this, 0, 0, 1, 27, Terrain.AVOID);
        Painter.fill(this, 24, 0, 1, 27, Terrain.AVOID);

        // 왼쪽 외곽 위
        Painter.fill(this, 1, 11, 1, 15, Terrain.AVOID);
        Painter.fill(this, 0, 11, 10, 6, Terrain.AVOID);

        // 왼쪽 방 아래
        Painter.fill(this, 2, 21, 4, 1, Terrain.AVOID);
        Painter.fill(this, 7, 21, 3, 1, Terrain.AVOID);
        Painter.fill(this, 9, 17, 1, 4, Terrain.AVOID);

        // 오른쪽
        Painter.fill(this, 15, 12, 9, 10, Terrain.AVOID);
        Painter.fill(this, 23, 21, 1, 6, Terrain.AVOID);

        // 계단 관련
        Painter.fill(this, 2, 17, 2, 1, Terrain.ENTRANCE); // 1층
        Painter.fill(this, 22, 22, 1, 3, Terrain.EXIT);

        // 계단 옆에 잡오브젝트
        Painter.fill(this, 5, 17, 4, 1, Terrain.AVOID); // 1층

        // 중앙 통로 문처리
        Painter.fill(this, 0, 11, 10, 1, Terrain.WALL);
        Painter.fill(this, 10, 11, 5, 1, Terrain.DOOR);
        Painter.fill(this, 15, 11, 9, 1, Terrain.WALL);

        entrance = 453;
        exit = 596;

        feeling = Feeling.NONE;

        NewRhodesLevel3.CustomeMap vis = new NewRhodesLevel3.CustomeMap();
        vis.setRect(0, 0, width(), height());
        customTiles.add(vis);

        return true;
    }

    @Override
    protected void syncTransitionsFromFields() {
        transitions.clear();
        //entrance terrain leads back toward Rhodes 2 (branch 2) (middle cell first)
        transitions.add(new LevelTransition(this, 428,
                LevelTransition.Type.BRANCH_ENTRANCE, 0, 2, LevelTransition.Type.BRANCH_EXIT));
        transitions.add(new LevelTransition(this, 427,
                LevelTransition.Type.BRANCH_ENTRANCE, 0, 2, LevelTransition.Type.BRANCH_EXIT));
        //exit terrain leads deeper into Rhodes (floor 4, branch 4) (middle cell first)
        transitions.add(new LevelTransition(this, 597,
                LevelTransition.Type.BRANCH_EXIT, 0, 4, LevelTransition.Type.BRANCH_ENTRANCE));
        transitions.add(new LevelTransition(this, 572,
                LevelTransition.Type.BRANCH_EXIT, 0, 4, LevelTransition.Type.BRANCH_ENTRANCE));
        transitions.add(new LevelTransition(this, 622,
                LevelTransition.Type.BRANCH_EXIT, 0, 4, LevelTransition.Type.BRANCH_ENTRANCE));
    }

    @Override
    protected void createMobs() {
    }

    public Actor addRespawner() {
        return null;
    }

    @Override
    protected void createItems() {
        //   if (Dungeon.hero.belongings.getItem(Amulet.class) == null) GreenCat.spawn(this, exit);
        //   SkinModel.spawn(this, 255);
        GreenCat.spawn(this, 162);

        if (Dungeon.QuestCatPoint == 1 && PhantomCatQuestLine.catStillOut()) {
            NPC_PhantomShadow.spawn(this, 1);
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
            texture = Assets.Environment.RHODES_29F;
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

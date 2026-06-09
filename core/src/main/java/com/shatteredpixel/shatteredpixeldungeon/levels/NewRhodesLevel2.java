package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Closure;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Dobermann;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Dummy;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Firewall;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.FrostLeaf;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Jessica;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC_Gglow;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC_Mage;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC_Phantom;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC_PhantomShadow;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Npc_Astesia;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.SkinModel;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Weedy;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.NewGameItem.Closure_FoodBox;
import com.shatteredpixel.shatteredpixeldungeon.items.NewGameItem.Closure_HealingBox;
import com.shatteredpixel.shatteredpixeldungeon.items.NewGameItem.Closure_IdentifyBox;
import com.shatteredpixel.shatteredpixeldungeon.items.NewGameItem.Closure_PotionBox;
import com.shatteredpixel.shatteredpixeldungeon.items.NewGameItem.Closure_RingBox;
import com.shatteredpixel.shatteredpixeldungeon.items.NewGameItem.Closure_ScrollBox;
import com.shatteredpixel.shatteredpixeldungeon.items.NewGameItem.Closure_TGBox;
import com.shatteredpixel.shatteredpixeldungeon.items.NewGameItem.Closure_TransBox;
import com.shatteredpixel.shatteredpixeldungeon.items.NewGameItem.Closure_WandBox;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Group;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.Arrays;

public class NewRhodesLevel2 extends Level {

    {
        color1 = 0x801500;
        color2 = 0xa68521;
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

        setSize(68, 85);
        Arrays.fill(map, Terrain.CHASM);

        // 0이 가장 왼쪽 ~ 위쪽임. 1 늘어날 때마다 오른쪽 / 아래쪽으로 1칸
        // X랑 Y는 위치. W랑 H는 각각 길이/너비 같은거임. W랑 H는 최소 1 이상의 값을 줘야함.
        // 0,0,10,1 을 하면 X,Y가 0,0인 위치에서 시작해서 가로로 10칸, 세로로 1칸 크기로 한다는 것.
        // 주의 사항 : 최대값은 위의 setSize 값의 -1임. 그러니까 x값 합이 18이 되도록 하면 버그남.

        // 베이스. 건드리지 말 것
        Painter.fill(this, 0, 0, 68, 85, Terrain.WALL);

        // 개발팀 구역 A1 + 상점쪽 처리
        Painter.fill(this, 2, 54, 23, 4, Terrain.EMPTY);

        Painter.fill(this, 2, 53, 24, 1, Terrain.AVOID);
        Painter.fill(this, 8, 53, 1, 4, Terrain.AVOID);
        Painter.fill(this, 0, 57, 31, 1, Terrain.AVOID);

        // + 상점 부분
        Painter.fill(this, 18, 53, 1, 4, Terrain.AVOID);

        map[3608] = Terrain.EMPTY;
        map[3617] = Terrain.EMPTY;
        map[3882] = Terrain.EMPTY;
        map[3889] = Terrain.EMPTY;
        map[3750] = Terrain.AVOID;
        map[3758] = Terrain.EMPTY;

        // 훈련실 구역 A2
        Painter.fill(this, 2, 62, 28, 4, Terrain.EMPTY);
        Painter.fill(this, 0, 61, 31, 1, Terrain.WALL);

        Painter.fill(this, 12, 62, 1, 2, Terrain.AVOID);
        Painter.fill(this, 12, 65, 1, 1, Terrain.AVOID);
        Painter.fill(this, 15, 62, 1, 2, Terrain.AVOID);
        Painter.fill(this, 15, 65, 1, 1, Terrain.AVOID);
        Painter.fill(this, 17, 62, 1, 2, Terrain.AVOID);
        Painter.fill(this, 17, 65, 1, 1, Terrain.AVOID);
        Painter.fill(this, 23, 62, 1, 4, Terrain.AVOID);

        // 복도 A3
        Painter.fill(this, 2, 58, 33, 3, Terrain.EMPTY);
        Painter.fill(this, 2, 58, 1, 3, Terrain.ENTRANCE);

        // 숙소 구역 B1-왼쪽 부분 (복도 포함)
        Painter.fill(this, 2, 43, 26, 6, Terrain.EMPTY);
        Painter.fill(this, 2, 46, 26, 1, Terrain.AVOID);
        Painter.fill(this, 20, 42, 6, 4, Terrain.WALL);

        // 숙소 B1구역-세부
        Painter.fill(this, 2, 42, 3, 4, Terrain.AVOID);
        map[2861] = Terrain.EMPTY;
        map[3063] = Terrain.EMPTY;
        map[3064] = Terrain.EMPTY;
        map[3066] = Terrain.AVOID;
        map[3131] = Terrain.EMPTY;

        Painter.fill(this, 8, 42, 3, 4, Terrain.AVOID);
        map[2867] = Terrain.EMPTY;
        map[3069] = Terrain.EMPTY;
        map[3070] = Terrain.EMPTY;
        map[3072] = Terrain.AVOID;
        map[3137] = Terrain.EMPTY;

        Painter.fill(this, 14, 42, 3, 4, Terrain.AVOID);
        map[2873] = Terrain.EMPTY;
        map[3075] = Terrain.EMPTY;
        map[3076] = Terrain.EMPTY;
        map[3078] = Terrain.AVOID;
        map[3143] = Terrain.EMPTY;

        // A->B 계단 구역
        Painter.fill(this, 26, 48, 2, 11, Terrain.EMPTY);

        // 맵 경계선. 못나가도록 막음
        Painter.fill(this, 28, 42, 1, 16, Terrain.WALL);
        map[4164] = Terrain.EMPTY;

        // 확장 상점구역
        Painter.fill(this, 29, 53, 22, 4, Terrain.EMPTY);
        Painter.fill(this, 34, 57, 17, 6, Terrain.EMPTY);

        // 확장 상점구역 선반
        Painter.fill(this, 37, 57, 2, 1, Terrain.WALL);
        Painter.fill(this, 37, 58, 2, 1, Terrain.STATUE);
        Painter.fill(this, 42, 57, 2, 1, Terrain.WALL);
        Painter.fill(this, 42, 58, 2, 1, Terrain.STATUE);
        Painter.fill(this, 37, 60, 2, 1, Terrain.WALL);
        Painter.fill(this, 37, 61, 2, 1, Terrain.STATUE);
        Painter.fill(this, 42, 60, 2, 1, Terrain.WALL);
        Painter.fill(this, 42, 61, 2, 1, Terrain.STATUE);

        // 위디 옆 확장통로 벽
        Painter.fill(this, 29, 57, 4, 1, Terrain.WALL);
        Painter.fill(this, 30, 60, 1, 5, Terrain.WALL);
        Painter.fill(this, 33, 57, 1, 6, Terrain.WALL);
        Painter.fill(this, 33, 63, 18, 1, Terrain.WALL);
        Painter.fill(this, 31, 60, 2, 6, Terrain.EMPTY);
        Painter.fill(this, 31, 64, 23, 2, Terrain.EMPTY);

        map[4319] = Terrain.EMPTY;
        map[4332] = Terrain.EMPTY;
        map[4587] = Terrain.EMPTY;
        map[4588] = Terrain.EMPTY;

        entrance = 4015;
        exit = 3607;

        map[exit - 1] = Terrain.EXIT;
        map[exit] = Terrain.EXIT;

        feeling = Level.Feeling.NONE;

        NewRhodesLevel2.CustomeMap vis = new NewRhodesLevel2.CustomeMap();
        vis.setRect(0, 0, width(), height());
        customTiles.add(vis);

        return true;
    }

    @Override
    protected void syncTransitionsFromFields() {
        transitions.clear();
        //entrance terrain leads back toward Rhodes 1 (branch 1) (middle cell first)
        transitions.add(new LevelTransition(this, 4014,
                LevelTransition.Type.BRANCH_ENTRANCE, 0, 1, LevelTransition.Type.BRANCH_EXIT));
        transitions.add(new LevelTransition(this, 3946,
                LevelTransition.Type.BRANCH_ENTRANCE, 0, 1, LevelTransition.Type.BRANCH_EXIT));
        transitions.add(new LevelTransition(this, 4082,
                LevelTransition.Type.BRANCH_ENTRANCE, 0, 1, LevelTransition.Type.BRANCH_EXIT));
        //exit terrain leads deeper into Rhodes (floor 3, branch 3) (middle cell first)
        transitions.add(new LevelTransition(this, 3607,
                LevelTransition.Type.BRANCH_EXIT, 0, 3, LevelTransition.Type.BRANCH_ENTRANCE));
        transitions.add(new LevelTransition(this, 3606,
                LevelTransition.Type.BRANCH_EXIT, 0, 3, LevelTransition.Type.BRANCH_ENTRANCE));
    }

    @Override
    protected void createMobs() {
    }

    public Actor addRespawner() {
        return null;
    }

    @Override
    protected void createItems() {

        Closure.spawn(this, 3683);
        SkinModel.spawn(this, 3751);
        Firewall.spawn(this, 3882);
        Weedy.spawn(this, 4404);
        Dummy.spawn(this, 4286);
        Dummy.spawn(this, 4354);
        Jessica.spawn(this, 4295);
        Dobermann.spawn(this, 4298);
        FrostLeaf.spawn(this, 4305);
        NPC_Phantom.spawn(this, 3010);
        NPC_Gglow.spawn(this, 3910);
        NPC_Mage.spawn(this, 3202);

        if (Random.Int(2) == 0) Npc_Astesia.spawn(this, 3004);
        else Npc_Astesia.spawn(this, 3218);


        if (Dungeon.QuestCatPoint == 0 && !NPC_PhantomShadow.Clear) {
            NPC_PhantomShadow.spawn(this, 0);
        }

        // 특수 상점 관련

        drop(new Closure_FoodBox(), 3709).type = Heap.Type.FOR_SALE_28F;
        drop(new Closure_PotionBox(), 3710).type = Heap.Type.FOR_SALE_28F;
        drop(new Closure_ScrollBox(), 3777).type = Heap.Type.FOR_SALE_28F;
        drop(new Closure_IdentifyBox(), 3778).type = Heap.Type.FOR_SALE_28F;
        drop(new Closure_HealingBox(), 3714).type = Heap.Type.FOR_SALE_28F;
        drop(new Closure_WandBox(), 3715).type = Heap.Type.FOR_SALE_28F;
        drop(new Closure_TransBox(), 3782).type = Heap.Type.FOR_SALE_28F;
        drop(new Closure_RingBox(), 3783).type = Heap.Type.FOR_SALE_28F;
        drop(new Closure_TGBox(), 3784).type = Heap.Type.FOR_SALE_28F;
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
            texture = Assets.Environment.RHODES_28F;
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

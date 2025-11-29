package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SeaBoss1;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.SeaObject;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Group;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.Arrays;

public class SeaBossLevel1 extends Level {
    {
        color1 = 0x801500;
        color2 = 0xa68521;
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILSE_IBERIA2;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_IBERIA2;
    }

    @Override
    public void create() {
        super.create();
    }

    private static final int ROOM_TOP = 12;

    @Override
    protected boolean build() {

        setSize(17, 12);
        Arrays.fill( map, Terrain.EMPTY );

        feeling = Feeling.NONE;

        setMap();

        return true;
    }

    private static int W = Terrain.WALL; // 벽
    private static int D = Terrain.WALL_DECO; // 장식된 벽
    private static int e = Terrain.EMPTY; // 평범한 땅
    private static int E = Terrain.EMPTY_DECO; // 빈 땅_장식
    private static int S = Terrain.AVOID; // 구조물


    // 현재 이 값을 기준으로하면, 외곽부분에 벽만 둘러지고 전부 일반땅인 맵이 생성됨.
    // 대문자 E로 표시한 부분은 "입구"와 "출구"임으로 벽으로 막지 말거나 바꾸기전에 연락하면 따로 설명해드림 (출구는 사실 별 의미없긴함)
    // 맵 사이즈를 늘릴 생각이라면 그것도 추가로 연락바람
    private static final int[] endMap = new int[]{
            S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S,
            S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S,
            S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S,
            S, S, S, S, S, S, S, S, D, S, S, S, S, S, S, S, S,
            S, E, e, e, e, e, e, e, e, e, e, e, e, e, e, S, S,
            S, S, e, S, e, e, e, S, S, e, e, e, e, e, e, S, S,
            S, S, e, e, e, e, e, e, e, S, e, e, S, e, e, S, S,
            S, S, e, e, e, S, S, S, E, S, e, e, e, e, e, S, S,
            S, S, e, e, e, e, e, e, e, e, e, e, e, e, e, S, S,
            S, S, S, e, e, e, e, e, e, e, S, S, S, S, S, S, S,
            S, S, S, e, e, e, e, e, e, e, e, e, e, e, e, E, S,
            S, S, S, S, S, S, S, S, E, S, S, S, S, S, S, S, S,
    };

    private void setMap(){

        int cell = 0;
        int i = 0;
        while (cell < length()){
            System.arraycopy(endMap, i, map, cell, 17);
            i += 17;
            cell += width();
        }

        entrance = 178; // 입구 위치
        exit = 59; // 출구 위치

        map[entrance] = Terrain.ENTRANCE;

        CustomeMap vis = new CustomeMap();
        vis.setRect(0, 0, width(), height());
        customTiles.add(vis);
    }

    public static class CustomeMap extends CustomTilemap {

        {
            texture = Assets.Environment.IBERIA_BOSS1_1;
        }

        @Override
        public Tilemap create() {
            Tilemap v = super.create();
            int[] data = new int[tileW*tileH];
            for (int i = 0; i < data.length; i++){
                data[i] = i;
            }

            v.map( data, tileW );
            return v;
        }
    }

    public static class CustomeMap2 extends CustomTilemap {

        {
            texture = Assets.Environment.IBERIA_BOSS1_2;
        }

        @Override
        public Tilemap create() {
            Tilemap v = super.create();
            int[] data = new int[tileW*tileH];
            for (int i = 0; i < data.length; i++){
                data[i] = i;
            }

            v.map( data, tileW );
            return v;
        }
    }

    @Override
    public void occupyCell( Char ch ) {

        super.occupyCell( ch );

        if (ch.pos != map[entrance] && map[exit] == Terrain.WALL_DECO) seal();

    }

    @Override
    public void seal() {
        super.seal();

        set( entrance, Terrain.EMPTY );
        set( exit, Terrain.EMPTY );
        GameScene.updateMap( entrance );
        GameScene.updateMap( exit );


        SeaObject obj = new SeaObject();
        obj.pos = 127;
        GameScene.add( obj );

        SeaBoss1 boss = new SeaBoss1();
        boss.pos = 76; // 여길 수정
        GameScene.add( boss );

        Dungeon.observe();
    }

    @Override
    public void unseal() {
        super.unseal();
        set( entrance, Terrain.ENTRANCE );
        GameScene.updateMap( entrance );

        set( exit, Terrain.EXIT );
        GameScene.updateMap( exit );

        CustomeMap2 vis = new CustomeMap2();
        vis.setRect(0, 0, width(), height());
        customTiles.add(vis);

        Dungeon.observe();
    }

    @Override
    protected void createMobs() {
    }

    public Actor addRespawner() {
        return null;
    }

    @Override
    protected void createItems() {
    }

    @Override
    public String tileName( int tile ) {
        switch (tile) {
            case Terrain.SEA_TERROR:
                return Messages.get(SeaLevel_part1.class, "sea_terror_name");
            default:
                return super.tileName( tile );
        }
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.ENTRANCE:
                return Messages.get(CityLevel.class, "entrance_desc");
            case Terrain.EXIT:
                return Messages.get(CityLevel.class, "exit_desc");
            case Terrain.WALL_DECO:
            case Terrain.EMPTY_DECO:
                return Messages.get(CityLevel.class, "deco_desc");
            case Terrain.EMPTY_SP:
                return Messages.get(CityLevel.class, "sp_desc");
            case Terrain.STATUE:
            case Terrain.STATUE_SP:
                return Messages.get(CityLevel.class, "statue_desc");
            case Terrain.BOOKSHELF:
                return Messages.get(CityLevel.class, "bookshelf_desc");
            case Terrain.SEA_TERROR:
                return Messages.get(SeaLevel_part1.class, "sea_terror_desc");
            default:
                return super.tileDesc( tile );
        }
    }

    @Override
    public int randomRespawnCell( Char ch ) {
        int cell;
        do {
            cell = entrance + PathFinder.NEIGHBOURS8[Random.Int(8)];
        } while (!passable[cell]
                || (Char.hasProp(ch, Char.Property.LARGE) && !openSpace[cell])
                || Actor.findChar(cell) != null);
        return cell;
    }

    @Override
    public Group addVisuals () {
        super.addVisuals();
        HallsLevel.addHallsVisuals(this, visuals);
        return visuals;
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
    }
}

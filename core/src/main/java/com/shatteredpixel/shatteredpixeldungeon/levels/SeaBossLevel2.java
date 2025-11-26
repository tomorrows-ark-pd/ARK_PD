package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Isharmla;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Group;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.Arrays;

public class SeaBossLevel2 extends Level {
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

        setSize(21, 24);
        Arrays.fill( map, Terrain.EMPTY );

        feeling = Feeling.NONE;

        setMap();

        return true;
    }

    private static int W = Terrain.WALL; // 벽
    private static int D = Terrain.WALL_DECO; // 장식된 벽
    private static int e = Terrain.EMPTY; // 평범한 땅
    private static int E = Terrain.EMPTY_DECO; // 빈 땅_장식
    private static int S = Terrain.STATUE; // 구조물
    private static int T = Terrain.SEA_TERROR; // 구조물


    // 현재 이 값을 기준으로하면, 외곽부분에 벽만 둘러지고 전부 일반땅인 맵이 생성됨.
    // 대문자 E로 표시한 부분은 "입구"와 "출구"임으로 벽으로 막지 말거나 바꾸기전에 연락하면 따로 설명해드림 (출구는 사실 별 의미없긴함)
    // 맵 사이즈를 늘릴 생각이라면 그것도 추가로 연락바람

    private static final int[] endMap = new int[]{
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, W,
            W, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, W,
            W, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, W,
            W, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, W,
            W, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, W,
            W, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, W,
            W, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, W,
            W, T/*169*/, T, T, T, T, T, T, T, T, T/*178*/, T, T, T, T, T, T, T, T, T,/*187*/ W,
            W, T, T, T, T, T, T, T, T, T, T, T, T/*199*/, T, T, T, T, T, T, T, W,
            W, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, W,
            W, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, W,
            W, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, W,
            W, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, W,
            W, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, W,
            W, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, W,
            W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W,
            W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W,
            W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W,
            W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W,
            W, W, W, W, W, W, W, W, W, e, e, e, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, e, e, e, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, e, E, e, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
    };

    public static class CustomeMap extends CustomTilemap {

        {
            texture = Assets.Environment.IBERIA_BOSS2;
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

    private void setMap(){

        int cell = 0;
        int i = 0;
        while (cell < length()){
            System.arraycopy(endMap, i, map, cell, 21);
            i += 21;
            cell += width();
        }

        setBrand();

        entrance = 430; // 입구 위치
        exit = 31; // 출구 위치

        map[entrance] = Terrain.ENTRANCE;
        map[exit] = Terrain.EXIT;

        SeaBossLevel2.CustomeMap vis = new SeaBossLevel2.CustomeMap();
        vis.setRect(0, 0, width(), height());
        customTiles.add(vis);
    }

    private void setBrand() {
        for (int i = 168; i < 368; i++) {
            if (map[i] == Terrain.SEA_TERROR) {
                addSeaTerror(i);
            }
        }
    }

    @Override
    public void occupyCell( Char ch ) {

        super.occupyCell( ch );


        if (ch.pos != map[entrance] && map[exit] == Terrain.EXIT) {
            Isharmla boss = new Isharmla();
            boss.pos = 178;
            GameScene.add( boss );
            seal();
        }
    }

    @Override
    public void seal() {
        super.seal();
        set( entrance, Terrain.EMPTY );
        set( exit, Terrain.EMPTY );
        GameScene.updateMap( entrance );
        GameScene.updateMap( exit );
        Dungeon.observe();
    }

    @Override
    public void unseal() {
        super.unseal();
        set( entrance, Terrain.ENTRANCE );
        GameScene.updateMap( entrance );

        set( exit, Terrain.EXIT );
        GameScene.updateMap( exit );

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

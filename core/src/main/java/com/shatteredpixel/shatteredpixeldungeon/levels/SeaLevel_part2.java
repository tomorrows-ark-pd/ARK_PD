package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SanityPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.IberiaPainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CursingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FlashingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FrostTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrimTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.HallucinationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.OozeTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.OriginiumTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PoisonDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.StormTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ToxicTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WarpingTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.noosa.Group;
import com.watabou.utils.Random;

public class SeaLevel_part2 extends RegularLevel {
    {
        color1 = 0x4b6636;
        color2 = 0xf2f2f2;
    }

    @Override
    protected int standardRooms(boolean forceMax) {
        if (forceMax) return 7;
        //8 to 10, average 8.67
        return 7+ Random.chances(new float[]{3, 2, 1});
    }

    @Override
    protected int specialRooms(boolean forceMax) {
        if (forceMax) return 3;
        return 1 + Random.chances(new float[]{1, 1});
    }

    @Override
    protected void createItems() {
        if (Dungeon.depth == 37 || Dungeon.depth == 39) {
            addItemToSpawn(new PotionOfHealing());}
        else addItemToSpawn(new PotionOfStrength());
        addItemToSpawn(new SanityPotion());
        super.createItems();
    }

    @Override
    public int nMobs() {
        // 다른 계층보다 몬스터가 2마리 많이 등장합니다.
        return super.nMobs()+2;
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
    protected Painter painter() {
        return new IberiaPainter()
                .setWater(feeling == Feeling.WATER ? 0.33f : 0.1f, 4)
                .setGrass(feeling == Feeling.GRASS ? 0.05f : 0.03f, 3)
                .setTraps(nTraps(), trapClasses(), trapChances());
    }

    @Override
    protected Class<?>[] trapClasses() {
        return new Class[]{
                FrostTrap.class, ToxicTrap.class, OozeTrap.class, PoisonDartTrap.class,
                OriginiumTrap.class, FlashingTrap.class, StormTrap.class, CursingTrap.class,
                GrimTrap.class, WarpingTrap.class, HallucinationTrap.class
        };
    }

    @Override
    protected float[] trapChances() {
        return new float[]{
                4,4,4,4,
                2,2,2,2,
                1,1,1
        };
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
    public Group addVisuals() {
        super.addVisuals();
        return visuals;
    }
}

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silence;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.SeaTerror;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NetherseaBrandguiderSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class NetherseaBrandguider extends Mob {
    {
        spriteClass = NetherseaBrandguiderSprite.class;

        HP = HT = 160;
        EXP = 18;
        maxLvl = 38;

        defenseSkill = 20;

        loot = Generator.Category.SCROLL;
        lootChance = 0.33f;

        properties.add(Property.SEA);
    }

    private boolean terrorSpawned = false;

    @Override
    public int damageRoll() { return Random.NormalIntRange(38, 55); }

    @Override
    public int attackSkill( Char target ) {return 44; }

    @Override
    public int drRoll() {
        if (HT /2 >= HP) return Random.NormalIntRange(10, 55);
        return Random.NormalIntRange(0, 20);
    }

    @Override
    protected boolean act() {

        //스폰시 첫 행동하면서 명흔을 깝니다.
        if (!terrorSpawned) {
            SeaTerror seaTerror = Dungeon.level.addSeaTerror(this.pos);
            seaTerror.activate();
            //Level.set(this.pos, Terrain.SEA_TERROR);
            GameScene.updateMap(this.pos);

            terrorSpawned = true;
        }

        if (HT /2 >= HP && this.buff(Silence.class) == null) {
            if (Dungeon.level.map[this.pos] == Terrain.EMPTY || Dungeon.level.map[this.pos] == Terrain.WATER) {
                Dungeon.level.addSeaTerror(this.pos);
                //Level.set(this.pos, Terrain.SEA_TERROR);
                CellEmitter.get(pos).burst(Speck.factory(Speck.BUBBLE), 10);
                GameScene.updateMap( pos );
                Dungeon.observe();
            }
        }
        return super.act();
    }

    private static final String VAL   = "firstTEEROR";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( VAL, terrorSpawned);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        terrorSpawned = bundle.getBoolean(VAL);
    }
}

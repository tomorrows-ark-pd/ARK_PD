package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ActiveOriginium;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Sample;

public class OriginiumTrap extends Trap {

    {
        color = 0;
        shape = SP1;
        disarmedByActivation = false;

        canBeHidden = false;
    }

    @Override
    public void activate() {

        Char c = Actor.findChar( pos );

        if (c != null) {
            Buff.affect(c, ActiveOriginium.class).set(c.HT * 0.1f);

            if (c instanceof Mob) {
                if (((Mob)c).state == ((Mob)c).HUNTING) ((Mob)c).state = ((Mob)c).WANDERING;
                ((Mob)c).beckon( Dungeon.level.randomDestination( c ) );
            }
        }

        if (Dungeon.level.heroFOV[pos]) {
            GameScene.flash(0x80FF0000);
            Sample.INSTANCE.play( Assets.Sounds.BLAST );
        }

    }
}

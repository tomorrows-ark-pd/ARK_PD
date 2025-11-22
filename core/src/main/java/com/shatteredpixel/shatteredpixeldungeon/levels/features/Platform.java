package com.shatteredpixel.shatteredpixeldungeon.levels.features;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Reflection;

import java.util.List;

public abstract class Platform implements Bundlable {

    public String platformName = Messages.get(this, "name");

    public int image;
    public int pos;

    protected Class<? extends Platform.Generator> generatorClass;

    public void trigger() {
        Char ch = Actor.findChar(pos);
        activate(ch);
    }

    public abstract void activate( Char ch );

    public void destroy() {
        Dungeon.level.destroyPlatform( pos );

        if (Dungeon.level.heroFOV[pos]) {
            CellEmitter.get( pos ).burst(FlameParticle.FACTORY, 6);
        }
    }

    private static final String POS = "pos";

    @Override
    public void restoreFromBundle(Bundle bundle) {
        pos = bundle.getInt( POS );
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put( POS, pos );
    }

    public String desc() {
        String desc = Messages.get(this, "desc");
        return desc;
    }

    public static class Generator extends Item {

        {
            stackable = true;
            defaultAction = AC_THROW;
        }

        protected Class<? extends Platform> platformClass;

        public List<Platform> generate(int pos, Level level ) {
            Platform platform = Reflection.newInstance(platformClass);
            platform.pos = pos;
            return List.of(platform);
        }

        @Override
        public boolean isUpgradable() {
            return false;
        }

        @Override
        public boolean isIdentified() {
            return true;
        }

        @Override
        public int value() {
            return 5 * quantity;
        }

        @Override
        public String desc() {
            return Messages.get(platformClass, "desc");
        }

        @Override
        public String info() {
            return Messages.get( Platform.class, "info", desc() );
        }
    }
}

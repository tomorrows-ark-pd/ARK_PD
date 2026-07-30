package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wrench;
import com.watabou.noosa.Game;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Callback;

public class SentrySprite extends MobSprite {

    private Animation tierIdles[] = new Animation[4];

    public SentrySprite() {
        super();

        texture(Assets.Sprites.SENTRY);

        TextureFilm frames = new TextureFilm(texture, 40, 46);

        tierIdles[1] = new Animation(1, true);
        tierIdles[1].frames(frames, 0);

        tierIdles[2] = new Animation(1, true);
        tierIdles[2].frames(frames, 1);

        tierIdles[3] = new Animation(1, true);
        tierIdles[3].frames(frames, 2);

    }

    @Override
    public void zap(int pos) {
        turnTo(ch.pos, pos);
        idle();
        flash();
        //gun-style shot, matches GunWeapon's fx()
        MagicMissile.boltFromChar(parent, MagicMissile.GUN_SHOT, this, pos, new Callback() {
            @Override
            public void call() {
                ((Wrench.Sentry) ch).onZapComplete();
            }
        });
    }

    @Override
    public void turnTo(int from, int to) {
        //sprite art faces left by default, opposite of CharSprite's default assumption
        int fx = from % Dungeon.level.width();
        int tx = to % Dungeon.level.width();
        if (tx > fx) {
            flipHorizontal = true;
        } else if (tx < fx) {
            flipHorizontal = false;
        }
        //idle animations are single-frame loops, so MovieClip never re-calls frame() on its
        //own to pick up the flip; force the vertex rebuild here instead
        updateFrame();
    }

    @Override
    public void die() {
        super.die();
        //cancels die animation and fades out immediately
        play(idle, true);
        emitter().burst(MagicMissile.WardParticle.UP, 10);
        parent.add(new AlphaTweener(this, 0, 2f) {
            @Override
            protected void onComplete() {
                SentrySprite.this.killAndErase();
                parent.erase(this);
            }
        });
    }

    public void linkVisuals(Char ch) {

        if (ch == null) return;
        updateTier(((Wrench.Sentry) ch).tier);

    }

    public void updateTier(int tier) {

        idle = tierIdles[tier];
        run = idle.clone();
        attack = idle.clone();
        die = idle.clone();

        //always render first
        if (parent != null) {
            parent.sendToBack(this);
        }

        resetColor();
        if (ch != null) place(ch.pos);
        idle();

        if (tier <= 3) {
            shadowWidth = 1.2f;
            shadowHeight = 0.25f;
            perspectiveRaise = 6 / 16f; //6 pixels
        } else {
            shadowWidth = 1.2f;
            shadowHeight = 0.25f;
            perspectiveRaise = 6 / 16f; //6 pixels
        }

    }

    private float baseY = Float.NaN;

    @Override
    public void place(int cell) {
        super.place(cell);
        baseY = y;
    }

    @Override
    public void update() {
        super.update();
        //if tier is greater than 3
        if (perspectiveRaise >= 6 / 16f && !paused) {
            if (Float.isNaN(baseY)) baseY = y;
            y = baseY + (float) Math.sin(Game.timeTotal);
            shadowOffset = 0.25f - 0.8f * (float) Math.sin(Game.timeTotal);
        }
    }

    @Override
    public int blood() {
        return 0xFFCC33FF;
    }
}

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CloserangeShot;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ShotgunWeapon extends GunWeapon {

    protected int PELLET_COUNT = 5;
    protected float CONE_DEGREES = 60f;
    protected float EXTRA_PELLET_MULT = 0.33f; // damage multiplier for 2nd+ pellets on same target

    @Override
    public int max(int lvl) {
        return 3 + 4 * tier +         //base: between gun (3*tier) and melee (5*(tier+1))
                lvl * (tier - 1);        //scaling: between gun (tier-2) and melee (tier+1)
    }

    protected float effectiveCone() {
        float cone = CONE_DEGREES;
        if (gunAccessories != null) cone *= gunAccessories.GetCONEcorrectionvalue();
        return cone;
    }

    protected String coneDesc() {
        float cone = effectiveCone();
        if (cone <= 50f) return Messages.get(ShotgunWeapon.class, "cone_narrow");
        if (cone >= 70f) return Messages.get(ShotgunWeapon.class, "cone_wide");
        return Messages.get(ShotgunWeapon.class, "cone_typical");
    }

    @Override
    public String statsInfo() {
        if (specialBullet > 0)
            return Messages.get(this, "stats_desc_sp", fireMin(), fireMax(), specialBullet, getMinRange(), getMaxRange(), PELLET_COUNT, coneDesc());
        return Messages.get(this, "stats_desc", fireMin(), fireMax(), getMinRange(), getMaxRange(), PELLET_COUNT, coneDesc());
    }

    private ArrayList<Ballistica> cachedRays;

    protected ArrayList<Ballistica> computePelletRays(Ballistica centerBolt) {
        int from = Dungeon.hero.pos;
        int w = Dungeon.level.width();
        int h = Dungeon.level.height();

        PointF fromP = new PointF(from % w + 0.5f, from / w + 0.5f);
        int target = centerBolt.collisionPos;
        PointF toP = new PointF(target % w + 0.5f, target / w + 0.5f);

        float centerAngle = PointF.angle(fromP, toP) / PointF.G2R; // degrees
        // Scan at long distance so the grid produces many unique direction cells;
        // actual pellet travel is capped to getMaxRange() via rayEndPos()
        float scanDist = getMaxRange() * 3f;
        float halfCone = effectiveCone() / 2f;

        // --- Center pellet: far cell in the exact aimed direction ---
        int centerFarCell = angleFarCell(fromP, centerAngle, scanDist, w, h);

        // --- Cone scan: find all unique direction cells across the arc ---
        ArrayList<Integer> coneCells = new ArrayList<>();
        for (float a = centerAngle - halfCone; a <= centerAngle + halfCone; a += 0.5f) {
            int cell = angleFarCell(fromP, a, scanDist, w, h);
            if (cell != from && cell != centerFarCell
                    && !coneCells.contains(cell)) {
                coneCells.add(cell);
            }
        }

        // --- Build rays: 1 center + (PELLET_COUNT-1) stratified random ---
        ArrayList<Ballistica> rays = new ArrayList<>();

        // Guaranteed center pellet
        if (centerFarCell != from) {
            rays.add(new Ballistica(from, centerFarCell, Ballistica.PROJECTILE));
        } else {
            rays.add(centerBolt);
        }

        if (coneCells.isEmpty()) {
            // No spread cells found — duplicate center pellet for all remaining
            for (int i = 0; i < PELLET_COUNT - 1; i++) {
                rays.add(new Ballistica(from, centerFarCell, Ballistica.PROJECTILE));
            }
        } else if (coneCells.size() <= PELLET_COUNT - 1) {
            // Fewer unique spread cells than needed — sample with replacement
            for (int i = 0; i < PELLET_COUNT - 1; i++) {
                int cell = coneCells.get(Random.Int(coneCells.size()));
                rays.add(new Ballistica(from, cell, Ballistica.PROJECTILE));
            }
        } else {
            // Stratified random: divide cone cells into equal buckets, pick 1 per bucket
            int buckets = PELLET_COUNT - 1;
            for (int b = 0; b < buckets; b++) {
                int bucketStart = b * coneCells.size() / buckets;
                int bucketEnd = (b + 1) * coneCells.size() / buckets;
                int idx = Random.IntRange(bucketStart, bucketEnd - 1);
                rays.add(new Ballistica(from, coneCells.get(idx), Ballistica.PROJECTILE));
            }
        }

        return rays;
    }

    // Converts an angle (degrees) + distance into a cell index.
    // Clamps to map bounds so it always returns a valid cell.
    protected static int angleFarCell(PointF from, float angleDeg, float dist, int w, int h) {
        PointF p = new PointF();
        p.polar(angleDeg * PointF.G2R, dist);
        p.offset(from);
        int cx = Math.max(0, Math.min((int) Math.floor(p.x), w - 1));
        int cy = Math.max(0, Math.min((int) Math.floor(p.y), h - 1));
        return cy * w + cx;
    }

    // Returns the range-capped endpoint for a ray.
    // The ray may trace far (for direction diversity), but pellets stop at MAX_RANGE.
    protected int rayEndPos(Ballistica ray) {
        int cappedDist = Math.min(ray.dist, getMaxRange());
        return ray.path.get(cappedDist);
    }

    @Override
    protected void fx(Ballistica bolt, Callback callback) {
        cachedRays = computePelletRays(bolt);
        if (cachedRays.isEmpty()) {
            callback.call();
            return;
        }

        // Fire visual missiles for each pellet ray, capped to max range
        int callbackIdx = cachedRays.size() / 2;
        for (int i = 0; i < cachedRays.size(); i++) {
            Callback cb = (i == callbackIdx) ? callback : null;
            ((MagicMissile) curUser.sprite.parent.recycle(MagicMissile.class)).reset(
                    MagicMissile.GUN_SHOT,
                    curUser.sprite,
                    rayEndPos(cachedRays.get(i)),
                    cb
            );
        }
        Sample.INSTANCE.play(this.hitSound);
    }

    @Override
    protected void onZap(Ballistica bolt) {
        CloserangeShot closerRange = Dungeon.hero.buff(CloserangeShot.class);
        float oldacc = ACC;
        boolean anyKill = false;

        try {
            ArrayList<Ballistica> rays = cachedRays;
            if (rays == null) rays = computePelletRays(bolt);

            // Count pellets per target, using range-capped endpoints
            LinkedHashMap<Char, Integer> targetPellets = new LinkedHashMap<>();
            ArrayList<Integer> emptyCells = new ArrayList<>();

            for (Ballistica ray : rays) {
                int endPos = rayEndPos(ray);
                Char ch = Actor.findChar(endPos);
                if (ch != null && ch != Dungeon.hero) {
                    Integer count = targetPellets.get(ch);
                    targetPellets.put(ch, (count != null ? count : 0) + 1);
                } else if (endPos != Dungeon.hero.pos) {
                    emptyCells.add(endPos);
                }
            }

            // Attach RangedAttackTracker once for all pellets
            if (!targetPellets.isEmpty()) {
                Buff.affect(Dungeon.hero, RangedAttackTracker.class);
            }

            // Process each target with diminishing damage per additional pellet.
            // DR is rolled once per target; extras see scaled DR
            for (Map.Entry<Char, Integer> entry : targetPellets.entrySet()) {
                Char ch = entry.getKey();
                int pelletCount = entry.getValue();

                int targetDr = ch.drRoll();
                int extraDr = (targetDr > 0)
                        ? Math.max(1, Math.round(targetDr * EXTRA_PELLET_MULT))
                        : 0;

                for (int i = 0; i < pelletCount; i++) {
                    if (!ch.isAlive()) break;

                    float dmgMult = (i == 0) ? 1f : EXTRA_PELLET_MULT;
                    boolean triggerProcs = (i == 0);
                    int pelletDr = (i == 0) ? targetDr : extraDr;
                    processGunHit(ch, dmgMult, triggerProcs, pelletDr);
                }

                if (!ch.isAlive()) anyKill = true;
            }

            // Press cells where no enemy was hit (grass/plants etc.); skips TenguDartTraps only
            for (int cell : emptyCells) {
                Dungeon.level.pressCellGunfire(cell);
            }

            postShotCleanup(closerRange, false, anyKill);
        } finally {
            ACC = oldacc;
            cachedRays = null;
        }
    }
}

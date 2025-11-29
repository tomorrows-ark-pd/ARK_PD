package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Web;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Platform;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.SeaTerror;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndChenCombo;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ChenCombo extends Buff implements ActionIndicator.Action {
    private int count = 0;
    private int hits = 4;

    @Override
    public int icon() {
        return BuffIndicator.COMBO;
    }

    @Override
    public void tintIcon(Image icon) {
        ChenCombo.ComboMove move = getHighestMove();
        if (move != null) {
            icon.hardlight(move.tintColor & 0x00FFFFFF);
        } else {
            icon.resetColor();
        }
    }

    @Override
    public float iconFadePercent() {
        return 1f;
    }

    @Override
    public boolean act() {
        return super.act();
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    public void hit(Char enemy) {

        count++;
        if ((getHighestMove() != null)) {
            ActionIndicator.setAction(this);
        }

        GLog.p(Messages.get(this, "combo", count));

        BuffIndicator.refreshHero(); //refresh the buff visually on-hit
    }

    public void bounshit(Char enemy) {

        if ((getHighestMove() != null)) {

            ActionIndicator.setAction(this);
        }

        BuffIndicator.refreshHero(); //refresh the buff visually on-hit
    }

    @Override
    public void detach() {
        super.detach();
        ActionIndicator.clearAction(this);
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc");
    }

    private static final String COUNT = "count";


    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(COUNT, count);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        count = bundle.getInt(COUNT);

        if (getHighestMove() != null) ActionIndicator.setAction(this);
    }

    @Override
    public Image getIcon() {
        Image icon;
        if (((Hero) target).belongings.weapon != null) {
            icon = new ItemSprite(((Hero) target).belongings.weapon.image, null);
        } else {
            icon = new ItemSprite(new Item() {
                {
                    image = ItemSpriteSheet.WEAPON_HOLDER;
                }
            });
        }

        icon.tint(getHighestMove().tintColor);
        return icon;
    }

    @Override
    public void doAction() {
        GameScene.show(new WndChenCombo(this));
    }

    public enum ComboMove {
        SKILL1(5, 0xFF00FF00),
        SKILL2(10, 0xFFFFCC00),
        SKILL3(15, 0xFFFF0000);

        public int comboReq, tintColor;

        ComboMove(int comboReq, int tintColor) {
            this.comboReq = comboReq;
            this.tintColor = tintColor;
        }

        public String desc() {
            return Messages.get(this, name() + "_desc");
        }

    }

    public ChenCombo.ComboMove getHighestMove() {
        ChenCombo.ComboMove best = null;
        for (ChenCombo.ComboMove move : ChenCombo.ComboMove.values()) {
            if (count >= move.comboReq) {
                best = move;
            }
        }
        return best;
    }

    public int getComboCount() {
        return count;
    }

    public static class ParalysisTracker extends Buff {
    }


    public boolean canUseMove(ComboMove move) {
        return move.comboReq <= count;
    }

    public void useMove(ComboMove move) {
        moveBeingUsed = move;
        GameScene.selectCell(listener);
    }

    private static ComboMove moveBeingUsed;

    private void doAttack(final Char enemy) {
        hits -= 1;

        AttackIndicator.target(enemy);

        boolean wasAlly = enemy.alignment == target.alignment;
        Hero hero = (Hero) target;

        if (enemy.defenseSkill(target) >= Char.INFINITE_EVASION) {
            enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
            Sample.INSTANCE.play(Assets.Sounds.MISS);

        } else if (enemy.isInvulnerable(target.getClass())) {
            enemy.sprite.showStatus(CharSprite.POSITIVE, Messages.get(Char.class, "invulnerable"));
            Sample.INSTANCE.play(Assets.Sounds.MISS);

        } else {

            int dmg = target.damageRoll();
            if (hits < 3 && hero.hasTalent(Talent.UP_EX3)) dmg *= 1 + hero.pointsInTalent(Talent.UP_EX3) * 0.1f;

            //variance in damage dealt
            switch (moveBeingUsed) {
                case SKILL1:
                    break;
                case SKILL2:
                    break;
                case SKILL3:
                    break;
            }

            dmg = enemy.defenseProc(target, dmg);
            if (hits > 3 && !hero.hasTalent(Talent.UP_SP3) && Random.Int(2) > hero.pointsInTalent(Talent.UP_SP3)) dmg -= enemy.drRoll();

            if (enemy.buff(Vulnerable.class) != null) {
                dmg *= 1.33f;
            }

            dmg = target.attackProc(enemy, dmg);
            enemy.damage(dmg, target);

            //special effects
            switch (moveBeingUsed) {
                case SKILL1:
                    hits = 0;
                    if (enemy.isAlive() && enemy.buff(ParalysisTracker.class) == null) {
                        float ptime = 1f;
                        if (hero.hasTalent(Talent.UP_EX1)) ptime += hero.pointsInTalent(Talent.UP_EX1) * 0.5f;
                        Buff.affect(enemy, ParalysisTracker.class);
                        Buff.affect(enemy, Paralysis.class, ptime);

                        if (hero.hasTalent(Talent.UP_SP1)) {
                            Buff.affect(enemy, Vulnerable.class, hero.pointsInTalent(Talent.UP_SP1) * 2);
                        }
                    }

                    Sample.INSTANCE.play(Assets.Sounds.HIT_DUALSTRIKE, 1.25f, 1.33f);
                    break;
                case SKILL2:
                    hits = 0;
                    int dist = 3;
                    if (hero.hasTalent(Talent.UP_EX2)) dist += hero.pointsInTalent(Talent.UP_EX2);

                    Ballistica beam = new Ballistica(target.pos, enemy.pos, Ballistica.WONT_STOP);
                    int maxDistance = Math.min(dist, beam.dist);
                    int cell = beam.path.get(Math.min(beam.dist, maxDistance));
                    target.sprite.parent.add(new Beam.DeathRay(target.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(cell)));
                    boolean terrainAffected = false;

                    ArrayList<Char> chars = new ArrayList<>();

                    Blob web = Dungeon.level.blobs.get(Web.class);

                    int terrainPassed = 2;
                    for (int c : beam.subPath(1, maxDistance)) {

                        Char ch;
                        if ((ch = Actor.findChar(c)) != null) {

                            //we don't want to count passed terrain after the last enemy hit. That would be a lot of bonus levels.
                            //terrainPassed starts at 2, equivalent of rounding up when /3 for integer arithmetic.
                            terrainPassed = terrainPassed % 3;

                            chars.add(ch);
                        }

                        if (Dungeon.level.solid[c]) {
                            terrainPassed++;
                        }

                        if (Dungeon.level.flamable[c]) {

                            Dungeon.level.destroy(c);
                            GameScene.updateMap(c);
                            terrainAffected = true;

                        }

                        SeaTerror seaTerror = Dungeon.level.seaTerrors.get(c);
                        if (seaTerror != null) {
                            seaTerror.destroy();
                            GameScene.updateMap( c );
                            terrainAffected = true;
                        }

                        Platform platform = Dungeon.level.platforms.get(c);
                        if (platform != null) {
                            platform.destroy();
                            GameScene.updateMap( c );
                            terrainAffected = true;
                        }

                        CellEmitter.center(c).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
                    }

                    if (terrainAffected) {
                        Dungeon.observe();
                    }

                    int beamdamage = target.damageRoll();
                    float dmgbouns = 1.5f;
                    if (hero.hasTalent(Talent.UP_SP2)) dmgbouns += hero.pointsInTalent(Talent.UP_SP2) * 0.2f;
                    beamdamage *= dmgbouns;
                    for (Char ch : chars) {
                        ch.damage(beamdamage, this);
                        ch.sprite.centerEmitter().burst(PurpleParticle.BURST, Random.IntRange(1, 2));
                        ch.sprite.flash();
                    }
                    Sample.INSTANCE.play(Assets.Sounds.HIT_SLASH);
                    break;
                case SKILL3:
                    break;
                default:
                    //nothing
                    break;
            }

            if (target.buff(FireImbue.class) != null) target.buff(FireImbue.class).proc(enemy);
            if (target.buff(FrostImbue.class) != null) target.buff(FrostImbue.class).proc(enemy);

            target.hitSound(Random.Float(0.87f, 1.15f));
            enemy.sprite.bloodBurstA(target.sprite.center(), dmg);
            enemy.sprite.flash();

            if (!enemy.isAlive()) {
                GLog.i(Messages.capitalize(Messages.get(Char.class, "defeat", enemy.name())));
            }

        }

        Invisibility.dispel();
        switch (moveBeingUsed) {
            case SKILL3:
                if (hits > 0 && enemy.isAlive() && hero.canAttack(enemy) &&
                        (wasAlly || enemy.alignment != target.alignment)) {
                    target.sprite.Sattack(enemy.pos, new Callback() {
                        @Override
                        public void call() {
                            doAttack(enemy);
                        }
                    });
                } else {
                    detach();
                    Sample.INSTANCE.play(Assets.Sounds.HIT_SLASH, 1.33f, 1.33f);
                    ActionIndicator.clearAction(ChenCombo.this);
                    hero.spendAndNext(hero.attackDelay());
                }

                break;

            default:
                detach();
                ActionIndicator.clearAction(this);
                hero.spendAndNext(hero.attackDelay());
                break;
        }

        if (Dungeon.hero.subClass== HeroSubClass.SWORDMASTER && hits == 0) {
            if (Random.Int(4) == 0) Buff.affect(target, Adrenaline.class, 1f);
            else if (Random.Int(2) == 0) Buff.affect(target, Bless.class, 3f);
            else Buff.affect(target, Barrier.class).incShield(1 + hero.lvl / 5);
        }

        if (hero.hasTalent(Talent.DRAGONS_SWORD)) Buff.affect(hero, DoubleSwordBuff.class);

    }

    private CellSelector.Listener listener = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer cell) {
            if (cell == null) return;
            final Char enemy = Actor.findChar(cell);
            if (enemy == null
                    || enemy == target
                    || !Dungeon.level.heroFOV[cell]
                    || target.isCharmedBy(enemy)) {
                GLog.w(Messages.get(ChenCombo.class, "bad_target"));

            } else if (!((Hero) target).canAttack(enemy)) {
                Ballistica c = new Ballistica(target.pos, enemy.pos, Ballistica.PROJECTILE);
                if (c.collisionPos == enemy.pos) {
                    final int leapPos = c.path.get(c.dist - 1);
                    if (!Dungeon.level.passable[leapPos]) {
                        GLog.w(Messages.get(ChenCombo.class, "bad_target"));
                    } else {
                        Dungeon.hero.busy();
                        target.sprite.jump(target.pos, leapPos, new Callback() {
                            @Override
                            public void call() {
                                target.move(leapPos);
                                Dungeon.level.occupyCell(target);
                                Dungeon.observe();
                                GameScene.updateFog();
                                target.sprite.attack(cell, new Callback() {
                                    @Override
                                    public void call() {
                                        doAttack(enemy);
                                    }
                                });
                            }
                        });

                    }
                } else {
                    GLog.w(Messages.get(ChenCombo.class, "bad_target"));
                }
            } else {
                Dungeon.hero.busy();
                target.sprite.attack(cell, new Callback() {
                    @Override
                    public void call() {
                        doAttack(enemy);
                    }
                });
            }
        }

        @Override
        public String prompt() {
            return Messages.get(ChenCombo.class, "prompt");
        }
    };


    public static class DoubleSwordBuff extends Buff {

    }
}
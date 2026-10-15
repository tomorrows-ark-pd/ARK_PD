package com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.AnnihilationGear;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK3.TerminationT;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.Skill;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HandclapSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Thoughts extends Skill {

    @Override
    public void doSkill() {
        GameScene.selectCell(Shot);}

    protected CellSelector.Listener Shot = new  CellSelector.Listener() {
        @Override
        public void onSelect( Integer target ) {
            if (target != null && target != curUser.pos) {
                int targetCell = target;

                final int finalTargetCell = targetCell;

                image = ItemSpriteSheet.SPIRIT_ARROW;
                final Thoughts.SpiritArrow Arrow = new Thoughts.SpiritArrow();

                curUser.sprite.zap(targetCell);

                ((MissileSprite) curUser.sprite.parent.recycle(MissileSprite.class)).
                        reset(curUser.sprite,
                                finalTargetCell,
                                Arrow,
                                new Callback() {
                                    @Override
                                    public void call() {
                                        Arrow.onThrow(target);
                                    }
                                });
            }
        }

        @Override
        public String prompt() {
            return Messages.get(Thoughts.class, "prompt");
        }


    };


    public class SpiritArrow extends MissileWeapon {

        {
            image = ItemSpriteSheet.EX44;

            hitSound = Assets.Sounds.HIT_ARROW;
        }

        @Override
        protected void onThrow( int cell ) {
            Char enemy = Actor.findChar( cell );
            parent = null;
            Splash.at( cell, 0xCC99FFFF, 1 );
            isHit(cell);

        }

        protected void isHit(int target)
        {
            Char mob = Actor.findChar(target);
            if (mob != null) {
                dohit(mob);
                mob.sprite.burst(CharSprite.NEGATIVE, 10);
                CellEmitter.center(target).burst(BlastParticle.FACTORY, 6);
                Sample.INSTANCE.play(Assets.Sounds.HIT_WALL1);
                AnnihilationGear Gear = Dungeon.hero.belongings.getItem(AnnihilationGear.class);
                if (Dungeon.hero.belongings.getItem(AnnihilationGear.class) != null && Gear.charge < Gear.chargeCap) {
                    Gear.SPCharge(1);
                }
            }
            else {
                CellEmitter.center(target).burst(BlastParticle.FACTORY, 6);
                Sample.INSTANCE.play(Assets.Sounds.HIT_WALL1);
            }

            Invisibility.dispel();
            curUser.spendAndNext(1);
        }

        @Override
        public float accuracyFactor(Char owner) {
            return Float.POSITIVE_INFINITY;
        }
    }

    public void dohit(final Char enemy) {
        int dmg = Random.NormalIntRange(6, 7 + curUser.lvl + curUser.STR);
        enemy.damage(dmg, enemy);
    }
}

package com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.TomorrowRogueNight;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.Skill;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CustomeSet;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.SP.StaffOfCorrupting;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCorruption;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Corrupting;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SeabornSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.TargetHealthIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class AncientKin extends Skill {
    public void doSkill() {
        GameScene.selectCell(Kin);
    }

    protected CellSelector.Listener Kin = new CellSelector.Listener() {
        public void onSelect(Integer target) {
            if (target != null) {
                Char mob = Actor.findChar(target);

                if (mob instanceof Mob) {
                    if (mob.alignment != Char.Alignment.ALLY) {
                        if (!mob.properties().contains(Char.Property.BOSS)
                                && !mob.properties().contains(Char.Property.MINIBOSS)
                                && !mob.properties().contains(Char.Property.NPC)) {
                            Seaborn seaborn = new Seaborn();
                            seaborn.pos = mob.pos;

                            try {
                                ((Mob) mob).rollToDropLoot();
                                ((Mob) mob).destroy();
                                if (mob.sprite != null) mob.sprite.killAndErase();
                                TargetHealthIndicator.instance.target(null);
                                GameScene.add(seaborn);
                                CellEmitter.get(seaborn.pos).burst(Speck.factory(Speck.WOOL), 4);
                            } catch (RuntimeException e) {
                                TomorrowRogueNight.reportException(
                                        new RuntimeException("AncientKin Seaborn conversion failed; victim="
                                                + mob.getClass().getSimpleName(), e));
                                throw e;
                            }
                        } else Buff.affect(mob, Paralysis.class, 5f);
                    }
                } else GLog.n(Messages.get(AncientKin.class, "fail"));
            }

            for (Mob Mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (Mob.alignment != Char.Alignment.ALLY && Dungeon.level.heroFOV[Mob.pos]) {
                    Terror t = Buff.affect(Mob, Terror.class, Terror.DURATION);
                    if (t != null) t.object = curUser.id();
                }
            }
        }

        @Override
        public String prompt() {
            return Messages.get(AncientKin.class, "prompt");
        }
    };

    public static class Seaborn extends Mob {
        {
            spriteClass = SeabornSprite.class;

            HP = HT = 75;
            baseSpeed = 1f;

            state = HUNTING;

            alignment = Alignment.ALLY;
            immunities.add(WandOfCorruption.class);
            immunities.add(StaffOfCorrupting.class);
            immunities.add(Corrupting.class);
            immunities.add(Terror.class);
        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange(1 + Dungeon.hero.lvl, Dungeon.hero.lvl * 2);
        }

        @Override
        public int drRoll() {
            return Random.NormalIntRange(0, 6 + Dungeon.hero.lvl / 2);
        }

        @Override
        public int attackSkill(Char target) {
            return 10 + Dungeon.hero.lvl;
        }

        @Override
        public int defenseSkill(Char enemy) {
            CustomeSet.CustomSetBuff setBuff = Dungeon.hero.buff(CustomeSet.CustomSetBuff.class);
            int itembuff = 0;
            if (setBuff != null) itembuff = setBuff.itemLevel();
            return 5 + (Dungeon.hero.lvl / 2) + itembuff;
        }

        @Override
        public float speed() {
            return super.speed() * 1.5f;
        }
    }
}

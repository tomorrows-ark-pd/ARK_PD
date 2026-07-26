package com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK3;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RipperDemon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.AncientKin;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.Skill;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAggression;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.SP.StaffOfCorrupting;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCorruption;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Fadeleaf;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.EX42_GroundSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TentacleSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.TargetHealthIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class YourWish extends Skill {
    public void doSkill() { GameScene.selectCell(Kin); }

    protected CellSelector.Listener Kin = new CellSelector.Listener() {
        public void onSelect(Integer target) {
            YourWish.EX43 EX43 = new YourWish.EX43();
            if (target != null) {
                Camera.main.shake(5, 0.5f);
                Char mob = Actor.findChar(target);
                int targetCell = target;
                if (mob == null) {
                    EX43.pos = targetCell;
                    GameScene.add(EX43);
                    CellEmitter.get(EX43.pos).burst(Speck.factory(Speck.WOOL), 4);
                    Buff.prolong(EX43, StoneOfAggression.Aggression.class, StoneOfAggression.Aggression.DURATION);
                    Sample.INSTANCE.play(Assets.Sounds.SKILL_YOUWISH);

                    if (Dungeon.level.map[targetCell] != Terrain.EMPTY && Dungeon.level.map[targetCell] != Terrain.GRASS && Dungeon.level.map[targetCell] != Terrain.WATER
                            && Dungeon.level.map[targetCell] != Terrain.HIGH_GRASS  && Dungeon.level.map[targetCell] != Terrain.EMPTY_SP && Dungeon.level.map[targetCell] != Terrain.EMPTY_DECO
                            && Dungeon.level.map[targetCell] != Terrain.FURROWED_GRASS &&  Dungeon.level.map[targetCell] != Terrain.EMBERS)
                    {
                        ScrollOfTeleportation.teleportChar_unobstructed(EX43);
                    }
                }
                else {
                    EX43.pos = targetCell;
                    GameScene.add(EX43);
                        ScrollOfTeleportation.teleportChar_unobstructed(EX43);
                    } }}

        @Override
        public String prompt() { return Messages.get(YourWish.class, "prompt");
        }};

    public void dohit(final Char enemy) {
        int dmg = Random.NormalIntRange(35 + Dungeon.hero.lvl, 55 + Dungeon.hero.lvl * 4);
        Buff.prolong( enemy, Paralysis.class, 10 );
        enemy.damage(dmg, enemy);
    }

    public static class EX43 extends NPC
    {
        {
            spriteClass = EX42_GroundSprite.class;

            HP=HT=200;
            state = HUNTING;

            properties.add(Property.IMMOVABLE);
            alignment = Alignment.ALLY;
            immunities.add(WandOfCorruption.class);
            immunities.add(StaffOfCorrupting.class);
            immunities.add(Terror.class);
            immunities.add(Amok.class);
        }

        @Override
        protected boolean act() {
            if (this.buff(StoneOfAggression.Aggression.class) == null) {
            Buff.prolong(this, StoneOfAggression.Aggression.class, StoneOfAggression.Aggression.DURATION);}
            return super.act();
        }

        @Override
        public int attackProc(Char enemy, int damage) {
            Sample.INSTANCE.play(Assets.Sounds.HIT_WALL1);
            return super.attackProc(enemy, damage);
        }

        @Override
        public int damageRoll() { return Random.NormalIntRange( Dungeon.hero.lvl, Dungeon.hero.lvl * 2); }

        @Override
        public int drRoll() { return Random.NormalIntRange( 4, 8 + Dungeon.hero.lvl / 2 ); }

        @Override
        public int attackSkill(Char target) {
            return 20 + Dungeon.hero.lvl; }

        @Override
        public int defenseSkill(Char enemy) { return 0; }

        @Override
        public int defenseProc(Char enemy, int damage) {
            if (enemy instanceof RipperDemon) damage *= 2;
            return super.defenseProc(enemy, damage);
        }

        @Override
        protected boolean getCloser(int target) {
            return true;
        }

        @Override
        protected boolean getFurther(int target) {
            return true;
        }
    }


}

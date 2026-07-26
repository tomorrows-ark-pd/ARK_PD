package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Levitation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MeatPower_Chargrilled;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MeatPower_Frozen;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MeatPower_Mystery;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MeatPower_Stewed;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Rose_Force;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silence;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.NewDM300;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Talulah;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.miniboss.Mon3tr;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CustomeSet;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.SP.StaffOfCorrupting;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCorruption;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.EX42_GroundSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class AnnihilationGear extends Item {

    public static final String AC_ACTIVE = "active";

    {
        image = ItemSpriteSheet.ARTIFACT_ROSEMARY;

        defaultAction = AC_ACTIVE;
        unique = true;
    }

    public int charge = 4;
    public int chargeCap = 4;
    public int arts = 0; // 0 = false, 1,2 = true 파괴1=부유 파괴2=충격 파괴3=저격 수호1=면역 수호2=압살 수호3=제압
    public int artsused = 0; // 일정 횟수 이상이면 마법부여 발동안함.

    public int min() {
        return 6 + buffedLvl() + (Dungeon.hero.pointsInTalent(Talent.RHODES_WEAPON) * 2);
    }

    public int max() {
        if (Dungeon.hero.subClass == HeroSubClass.DESTROYER)
            return 10 + Dungeon.hero.lvl + (buffedLvl() * 2) + (Dungeon.hero.pointsInTalent(Talent.RHODES_WEAPON) * 2);
        return 10 + Dungeon.hero.lvl + buffedLvl() + (Dungeon.hero.pointsInTalent(Talent.RHODES_WEAPON) * 2);
    }

    @Override
    public String desc() {
        if (arts != 0) {
            if (Dungeon.hero.subClass == HeroSubClass.DESTROYER) {
                switch (arts) {
                    case 0:
                    default:
                        break;
                    case 1:
                        return Messages.get(this, "desc_arts1_destroy", min(), max());
                    case 2:
                        return Messages.get(this, "desc_arts2_destroy", min(), max());
                    case 3:
                        return Messages.get(this, "desc_arts3_destroy", min(), max());
                }
            } else if (Dungeon.hero.subClass == HeroSubClass.GUARDIAN) {
                switch (arts) {
                    case 0:
                    default:
                        break;
                    case 1:
                        return Messages.get(this, "desc_arts1_guardian", min(), max());
                    case 2:
                        return Messages.get(this, "desc_arts2_guardian", min(), max());
                    case 3:
                        return Messages.get(this, "desc_arts3_guardian", min(), max());
                }
            } else if (Dungeon.hero.subClass == HeroSubClass.WAR) {
                switch (arts) {
                    case 0:
                    default:
                        break;
                    case 1:
                        return Messages.get(this, "desc_arts1_war", min(), max());
                    case 2:
                        return Messages.get(this, "desc_arts2_war", min(), max());
                    case 3:
                        return Messages.get(this, "desc_arts3_war", min(), max());
                }
            }
        }
        return Messages.get(this, "desc", min(), max());
    }

    @Override
    public String info() {
        if (Dungeon.hero.subClass != HeroSubClass.WAR) return desc();
        String aug;
        switch (weaponAug()) {
            case NONE:
            default:
                aug = Messages.get(this, "desc_augment_none");
                break;
            case DAMAGE:
                aug = Messages.get(this, "desc_augment_damage");
                break;
            case SPEED:
                aug = Messages.get(this, "desc_augment_speed");
                break;
            case OVERLOAD:
                aug = Messages.get(this, "desc_augment_overload");
                break;
        }
        return desc() + "\n\n" + aug;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_ACTIVE);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_ACTIVE)) {
            if (charge > 0) GameScene.selectCell(Shot);
            else {
                if (Dungeon.hero.hasTalent(Talent.MYWISH) && hero.buff(Talent.MyWishCooldown.class) == null) {
                    float oldtime = 0;
                    if (hero.buff(MindVision.class) != null) {
                        oldtime = Dungeon.hero.buff(MindVision.class).visualcooldown();
                        hero.buff(MindVision.class).detach();
                    }

                    float CoolDown = 5000 - (hero.pointsInTalent(Talent.MYWISH) * 1000);
                    Buff.affect(hero, Talent.MyWishCooldown.class, CoolDown);

                    for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                        if (mob.alignment != Char.Alignment.ALLY && Dungeon.level.heroFOV[mob.pos] && !mob.properties().contains(Char.Property.BOSS) && !mob.properties().contains(Char.Property.MINIBOSS)) {
                            mob.damage(Random.NormalIntRange(5675, 8784), hero);
                        }
                    }

                    GameScene.flash(0x80FFFFFF);
                    Camera.main.shake(2, 0.5f);
                    Sample.INSTANCE.play(Assets.Sounds.SKILL_YOUWISH);

                    charge = chargeCap;
                    updateQuickslot();

                    hero.HP /= 4;
                    if (oldtime != 0) Buff.affect(hero, MindVision.class, oldtime);

                    hero.spendAndNext(1f);
                } else GLog.w(Messages.get(this, "nocharge"));
            }
        }
    }

    public void SPCharge(int n) {
        charge += n;
        if (chargeCap < charge) charge = chargeCap;
        updateQuickslot();
    }

    public void discharge() {
        charge--;
        updateQuickslot();
    }

    @Override
    public String status() {
        return charge + "/" + chargeCap;
    }

    @Override
    public Item upgrade() {
        charge++;
        chargeCap++;
        chargeCap = Math.min(chargeCap, 10);
        return super.upgrade();
    }

    @Override
    public boolean isUpgradable() {
        return true;
    }

    private static final String CHARGE = "charge";
    private static final String MAGICARTS = "arts";
    private static final String ARTSUSED = "artsused";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(CHARGE, charge);
        bundle.put(MAGICARTS, arts);
        bundle.put(ARTSUSED, artsused);

    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        charge = bundle.getInt(CHARGE);
        arts = bundle.getInt(MAGICARTS);
        artsused = bundle.getInt(ARTSUSED);
    }


    protected CellSelector.Listener Shot = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer target) {
            if (target != null && target != curUser.pos) {
                int targetCell = target;

                final int finalTargetCell = targetCell;

                final AnnihilationGear.Spriteex Arrow = new AnnihilationGear.Spriteex();

                curUser.sprite.zap(targetCell);

                if (Dungeon.hero.hasTalent(Talent.LIGHTNESSMEAL) && Dungeon.hero.buff(Levitation.class) != null) {
                    Buff.detach(Dungeon.hero, Levitation.class);
                } else charge--;
                ((MissileSprite) curUser.sprite.parent.recycle(MissileSprite.class)).
                        reset(curUser.sprite,
                                finalTargetCell,
                                Arrow,
                                new Callback() {
                                    @Override
                                    public void call() {
                                        Arrow.onThrow(target);
                                        updateQuickslot();
                                    }
                                });
            }
        }

        @Override
        public String prompt() {
            return Messages.get(AnnihilationGear.class, "prompt");
        }

    };

    public class Spriteex extends MissileWeapon {

        {
            image = ItemSpriteSheet.EX44;
            hitSound = Assets.Sounds.HIT_SLASH;
        }

        @Override
        protected void onThrow(int cell) {
            parent = null;
            Splash.at(cell, 0xCC99FFFF, 1);
            isHit(cell);
        }

        protected void isHit(int target) {
            Char mob = Actor.findChar(target);
            if (mob != null) {
                if (mob instanceof EX44 && arts == 3 && Dungeon.hero.subClass == HeroSubClass.WAR) {
                    CellEmitter.center(target).burst(BlastParticle.FACTORY, 10);
                    Sample.INSTANCE.play(Assets.Sounds.HIT_WALL2);
                    mob.die(new WarCatArts3());
                } else {
                    dohit(mob);
                    CellEmitter.center(target).burst(BlastParticle.FACTORY, 10);
                    Sample.INSTANCE.play(Assets.Sounds.HIT_WALL2);
                }
            } else {
                if (Dungeon.hero.subClass == HeroSubClass.WAR) SpawnEX44(target);

                CellEmitter.center(target).burst(BlastParticle.FACTORY, 10);
                Sample.INSTANCE.play(Assets.Sounds.HIT_WALL2);
            }

            Buff buff = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
            if (buff != null) buff.detach();
            buff = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
            if (buff != null) buff.detach();

            Invisibility.dispel();
            if (Dungeon.hero.buff(MeatPower_Stewed.class) != null) curUser.spendAndNext(0.7f);
            else curUser.spendAndNext(1);
        }

        @Override
        public float accuracyFactor(Char owner) {
            return Float.POSITIVE_INFINITY;
        }
    }

    public void dohit(final Char enemy) {
        // 고기 파워들
        if (Dungeon.hero.buff(MeatPower_Mystery.class) != null) {
            Buff.affect(enemy, Silence.class, 5f);
        }
        if (Dungeon.hero.buff(MeatPower_Chargrilled.class) != null) {
            Buff.affect(enemy, Weakness.class, 7f);
        }
        if (Dungeon.hero.buff(MeatPower_Frozen.class) != null) {
            Dungeon.hero.HP += Dungeon.hero.HT / 20;
            Dungeon.hero.updateHT(true);
        }

        if (Dungeon.hero.hasTalent(Talent.POWERGEAR)) {
            Ballistica trajectory = new Ballistica(Dungeon.hero.pos, enemy.pos, Ballistica.STOP_TARGET);
            trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size() - 1), Ballistica.PROJECTILE);
            WandOfBlastWave.throwChar(enemy, trajectory, Dungeon.hero.pointsInTalent(Talent.POWERGEAR)); // 넉백 효과
        }

        int dmg = Random.NormalIntRange(min(), max());
        if (Dungeon.hero.buff(Rose_Force.class) != null) {
            if (Dungeon.hero.hasTalent(Talent.FOCUSED_ATTACK)) {
                dmg *= 1.5f + (float) Dungeon.hero.pointsInTalent(Talent.FOCUSED_ATTACK) * 0.15f;
            } else dmg *= 1.5f;

        }

        if (Dungeon.hero.hasTalent(Talent.ESTHESIA)) {
            if (enemy instanceof Mob) {
                if (enemy.properties().contains(Char.Property.BOSS) || enemy.properties().contains(Char.Property.MINIBOSS)) {
                    dmg *= 1.03f + (float) Dungeon.hero.pointsInTalent(Talent.ESTHESIA) * 0.03f;
                }
            }
        }

        if (Dungeon.hero.buff(MeatPower_Frozen.class) != null ||
                Dungeon.hero.buff(MeatPower_Chargrilled.class) != null ||
                Dungeon.hero.buff(MeatPower_Mystery.class) != null ||
                Dungeon.hero.buff(MeatPower_Stewed.class) != null) {
            dmg *= 1.08f;
        }

        // 마법 부여 효과

        if (Dungeon.hero.subClass == HeroSubClass.DESTROYER) {
            switch (arts) {
                case 0:
                default:
                    break;
                case 1:
                    if (5 + buffedLvl() > Random.Int(100))
                        Buff.affect(Dungeon.hero, Levitation.class, 3f);
                    break;
                case 2:
                    for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                        if (Dungeon.level.adjacent(mob.pos, enemy.pos) && mob.alignment != Char.Alignment.ALLY && mob != enemy) {
                            mob.damage(5 + buffedLvl() * 3, Dungeon.hero);
                        }
                    }
                    break;
                case 3:
                    int distance = Dungeon.level.distance(Dungeon.hero.pos, enemy.pos) - 1;
                    float DamageLevel = 1.1f + (0.008f * buffedLvl());
                    if (distance < 3) break;
                    else if (distance < 5) dmg = Math.round(dmg * DamageLevel);
                    else dmg = Math.round(dmg * (DamageLevel * 1.2f));
                    break;
            }
        } else if (Dungeon.hero.subClass == HeroSubClass.GUARDIAN) {
            switch (arts) {
                case 0:
                default:
                    break;
                case 1:
                    if (artsused < 2) {
                        Buff.affect(Dungeon.hero, MagicImmune.class, 3f);
                        artsused++;
                    }
                    break;
                case 2:
                    if (artsused < 2) {
                        if (enemy.HP < enemy.HT / 2 && !enemy.properties().contains(Char.Property.BOSS) && !enemy.properties().contains(Char.Property.MINIBOSS)) {
                            dmg = 999;
                            artsused++;
                        }
                    }
                    break;
                case 3:
                    if (artsused < 4) {
                        Buff.affect(enemy, Roots.class, 4f);
                        Buff.affect(enemy, Silence.class, 4f);
                        artsused++;
                    }
                    break;
            }
        }

        // 보스별 피해 저항

        if (enemy instanceof NewDM300) {
            dmg *= 0.88f;
        } // 머드락을 상대로 피해 12%감소
        else if (enemy instanceof Talulah) {
            dmg *= 0.65f;
        } // 탈룰라? 를 상대로 피해 35%감소
        else if (enemy instanceof Mon3tr) {
            dmg *= 0.85f;
        } // Mon3tr를 상대로 피해 15%감소

        enemy.damage(dmg, Dungeon.hero);

        // 서브 직업이 파괴라면, 집중 버프 부여
        if (Dungeon.hero.subClass == HeroSubClass.DESTROYER)
            Buff.affect(Dungeon.hero, Rose_Force.class, Rose_Force.DURATION);

        // 마비
        if (enemy.isAlive()) {
            if (Dungeon.hero.hasTalent(Talent.PHYSICAL_ATTACK)) {
                if (Dungeon.hero.pointsInTalent(Talent.PHYSICAL_ATTACK) > Random.Int(5)) {
                    Buff.affect(enemy, Paralysis.class, 1f);
                }
            }
        }
    }

    private void SpawnEX44(int point) {
        if (Actor.findChar(point) == null && Dungeon.level.passable[point]) {
            int augtype;
            switch (weaponAug()) {
                case NONE:
                default:
                    augtype = 0;
                    break;
                case DAMAGE:
                    augtype = 1;
                    break;
                case SPEED:
                    augtype = 2;
                    break;
                case OVERLOAD:
                    augtype = 3;
                    break;
            }

            EX44 w = new EX44();
            w.pos = point;
            w.setting(Dungeon.hero, this.level(), augtype);

            if (arts == 1) Buff.affect(w, WarCatBuff1.class);
            else if (arts == 2) Buff.affect(w, WarCatBuff2.class);

            GameScene.add(w);

        }
    }

    public Weapon.Augment weaponAug() {
        if (!(Dungeon.hero.belongings.weapon instanceof MeleeWeapon)) return Weapon.Augment.NONE;
        return ((MeleeWeapon) Dungeon.hero.belongings.weapon).augment;
    }

    public static class WarCatBuff1 extends Buff {
    }

    public static class WarCatBuff2 extends Buff {
    }

    public static class WarCatArts3 {
    }

    public static class EX44 extends NPC {
        {
            spriteClass = EX42_GroundSprite.class;

            state = HUNTING;

            properties.add(Property.IMMOVABLE);
            alignment = Alignment.ALLY;

            immunities.add(WandOfCorruption.class);
            immunities.add(StaffOfCorrupting.class);
            immunities.add(Terror.class);
            immunities.add(Amok.class);
        }

        private int lifecount = 30;
        private int weaponAug;

        public void setting(Hero hero, int gearLevel, int augmentationType) {
            CustomeSet.CustomSetBuff setBuff = Dungeon.hero.buff(CustomeSet.CustomSetBuff.class);
            int itembuff = 0;
            if (setBuff != null) itembuff = setBuff.itemLevel();

            int armorlevel = 0;
            if (hero.belongings.armor != null) armorlevel = hero.belongings.armor.level();

            if (augmentationType != 3) HP = HT = 30 + (armorlevel * 6) + (itembuff * 3);
            else HP = HT = 2 + (armorlevel / 2) + (itembuff / 5);

            maxLvl = gearLevel + (itembuff / 3);

            if (augmentationType == 1) lifecount = 40;

            weaponAug = augmentationType;
        }

        @Override
        public int damageRoll() {
            int dmg = Random.NormalIntRange(3, 5 + (maxLvl * 3));
            if (weaponAug == 1) dmg *= 1.35f;
            else if (weaponAug == 2) dmg *= 0.7f;

            return dmg;
        }

        @Override
        public void damage(int dmg, Object src) {
            if (Dungeon.hero.hasTalent(Talent.MENTALAMPLIFICATION)) {
                dmg *= 1f - (0.1f * Dungeon.hero.pointsInTalent(Talent.MENTALAMPLIFICATION));
            }

            if (buff(WarCatBuff2.class) != null) {
                dmg /= 4;
                Buff.detach(this, WarCatBuff2.class);
            }

            if (weaponAug == 3) dmg = 1;
            super.damage(dmg, src);
        }

        @Override
        public int drRoll() {
            return Random.NormalIntRange(0, maxLvl);
        }

        @Override
        public int attackSkill(Char target) {
            return 15 + maxLvl;
        }

        @Override
        protected boolean canAttack(Char enemy) {
            if (weaponAug == 2)
                return this.fieldOfView[enemy.pos] && Dungeon.level.distance(this.pos, enemy.pos) <= 2;
            else return super.canAttack(enemy);
        }

        @Override
        public int defenseSkill(Char enemy) {
            return 0;
        }

        @Override
        protected boolean act() {
            lifecount--;
            if (buff(WarCatBuff1.class) != null && lifecount > 15) lifecount--;
            if (lifecount < 1) {
                this.die(this);
                return true;
            }

            return super.act();
        }

        @Override
        public void die(Object cause) {
            if (cause == this) {
                if (Dungeon.hero.belongings.getItem(AnnihilationGear.class) != null) {
                    AnnihilationGear Gear = Dungeon.hero.belongings.getItem(AnnihilationGear.class);
                    Gear.SPCharge(1);
                }
            } else if (cause instanceof WarCatArts3) {
                for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                    Char ch = findChar(pos + PathFinder.NEIGHBOURS8[i]);
                    if (ch != null && ch.isAlive() && !(ch instanceof Hero) && !(ch instanceof EX44)) {
                        int damage = damageRoll() * 2;
                        ch.damage(damage, this);
                        Buff.affect(ch, Weakness.class, 5f);
                    }
                }
                if (Dungeon.hero.belongings.getItem(AnnihilationGear.class) != null) {
                    AnnihilationGear Gear = Dungeon.hero.belongings.getItem(AnnihilationGear.class);
                    Gear.SPCharge(1);
                }
            } else if (Dungeon.hero.hasTalent(Talent.OBLIVION)) {
                if (Random.Int(100) < Random.IntRange(0, Dungeon.hero.pointsInTalent(Talent.OBLIVION) * 15)) {
                    if (Dungeon.hero.belongings.getItem(AnnihilationGear.class) != null) {
                        AnnihilationGear Gear = Dungeon.hero.belongings.getItem(AnnihilationGear.class);
                        Gear.SPCharge(1);
                    }
                }
            }
            super.die(cause);
        }

        @Override
        public int attackProc(Char enemy, int damage) {
            if (Dungeon.level.heroFOV[this.pos]) {
                if (Dungeon.hero.hasTalent(Talent.CRYSTALLIZE)) {
                    damage *= 1.05f + (Dungeon.hero.pointsInTalent(Talent.CRYSTALLIZE) * 0.05f);
                }
            }

            return super.attackProc(enemy, damage);
        }

        {
            immunities.add(Paralysis.class);
            immunities.add(Amok.class);
            immunities.add(Sleep.class);
            immunities.add(Terror.class);
            immunities.add(Vertigo.class);
        }

        @Override
        protected boolean getCloser(int target) {
            return true;
        }

        @Override
        protected boolean getFurther(int target) {
            return true;
        }

        private static final String LIFE = "lifecount";
        private static final String AUG = "weaponAug";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(LIFE, lifecount);
            bundle.put(AUG, weaponAug);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            lifecount = bundle.getInt(LIFE);
            weaponAug = bundle.getInt(AUG);
            enemySeen = true;
        }
    }
}

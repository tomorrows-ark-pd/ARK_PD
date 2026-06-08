package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Camouflage;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChenShooterBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CloserangeShot;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Combo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Momentum;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SnipersMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.Bonk;
import com.shatteredpixel.shatteredpixeldungeon.items.Gunaccessories.Accessories;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfAccuracy;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfFuror;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Projecting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Thunderbolt;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.UpMagazine;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class GunWeapon extends MeleeWeapon {
    public static final String AC_ZAP = "ZAP";
    public static final String AC_RELOAD = "RELOAD";
    public static final String AC_REMOVE = "REMOVE";

    protected static final int RELOAD_AMOUNT = 31;

    protected int bulletTier = 3;

    protected int bulletMax = 25;
    protected int bullet = 0;
    protected int specialBullet = 0;
    protected boolean specialFire = false; // 특수 사격 여부
    protected boolean gamza = false; // 썬더볼트 장착 여부
    protected float FIRE_DELAY_MULT = 1f;
    protected int MIN_RANGE = 1;
    protected int MAX_RANGE = 4;

    protected float RELOAD_DELAY = 2f;

    @Override
    public int max(int lvl) {
        return 3 * (tier) +    // 3티어 기준 9+1, 5티어는 15+3
                lvl * (tier - 2);
    }

    public int fireMin() {
        return (int) ((tier - 1) + bulletTier + level())
                + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
    }

    public int fireMax() {
        return (int) 4
                + tier * 2
                + bulletTier * 3
                + level() * tier
                + RingOfSharpshooting.levelDamageBonus(Dungeon.hero) * 2;
    }

    public int getDistance(int from, int to) {
        return Dungeon.level.distance(from, to);
    }

    public int getMaxRange() {
        boolean projecting = hasEnchant(Projecting.class, Dungeon.hero);
        int range = projecting ? MAX_RANGE + 1 : MAX_RANGE;
        return range + RingOfSharpshooting.rangeBonus(Dungeon.hero);
    }

    public int getMinRange() {
        return MIN_RANGE;
    }

    public boolean isWithinRange(int distance) {
        return getMinRange() <= distance && distance <= getMaxRange();
    }

    public float getFireAcc(int from, int to) {
        int distance = getDistance(from, to);

        return getMaxRange() >= distance && distance >= getMinRange() ? 1f : 0.5f;
    }

    public int fireDamageRoll() {
        return Random.Int(fireMin(), fireMax());
    }

    public Accessories gunAccessories;

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        if (attacker instanceof Hero) {
            if (Dungeon.hero.subClass == HeroSubClass.GLADIATOR) {
                if (Random.Int(4) < 1) {
                    bullet = Math.min(bullet + 1, bulletMax);
                    updateQuickslot();
                }
            }
        }
        return super.proc(attacker, defender, damage);
    }

    protected void specialFire(Char ch) {
    }

    protected float fireAccuracyFactor(float acc) {
        if (gunAccessories != null) {
            acc *= gunAccessories.GetACCcorrectionvalue();

            if (Dungeon.hero.hasTalent(Talent.SHARPSHOOTER)) {
                acc += Dungeon.hero.pointsInTalent(Talent.SHARPSHOOTER) * 0.2f;
            }
        }

        if (Dungeon.hero.hasTalent(Talent.BLITZKRIEG)) {
            acc += (Dungeon.hero.pointsInTalent(Talent.BLITZKRIEG) * 0.1f);
        }

        CloserangeShot closerrange = Dungeon.hero.buff(CloserangeShot.class);
        if (closerrange != null && Dungeon.hero.hasTalent(Talent.PINPOINT)) {
            acc += Dungeon.hero.pointsInTalent(Talent.PINPOINT) * 0.2f;
        }

        acc *= RingOfAccuracy.shootAccuracyMultiplier(Dungeon.hero);

        return acc;
    }

    protected float fireDelayFactor(Char owner, float dly) {
        if (gunAccessories != null) dly *= gunAccessories.GetDLYcorrectionvalue();
        dly *= RingOfFuror.shootDelayMultiplier(owner);
        return dly;
    }

    protected int fireDamageFactor(int dmg) {
        float accessoriesBonus = 1f;
        if (gunAccessories != null) accessoriesBonus = gunAccessories.GetDMGcorrectionvalue();

        float talentBonus = 1f;
        if (Dungeon.hero.hasTalent(Talent.PROJECTILE_MOMENTUM) && Dungeon.hero.buff(Momentum.class) != null && Dungeon.hero.buff(Momentum.class).freerunning()) {
            talentBonus += (Dungeon.hero.pointsInTalent(Talent.PROJECTILE_MOMENTUM) * 0.1f);
        }

        if (Dungeon.hero.hasTalent(Talent.BLITZKRIEG)) {
            talentBonus += (Dungeon.hero.pointsInTalent(Talent.BLITZKRIEG) * 0.1f);
        }

        CloserangeShot closerRange = Dungeon.hero.buff(CloserangeShot.class);
        if (closerRange != null) {
            if (closerRange.state()) {
                talentBonus += 0.5f;
                if (Dungeon.hero.hasTalent(Talent.ZERO_RANGE_SHOT))
                    talentBonus += Dungeon.hero.pointsInTalent(Talent.ZERO_RANGE_SHOT) * 0.1f;
            }
        }

        dmg *= accessoriesBonus * talentBonus;
        return dmg;
    }

    public boolean affixAccessories(Accessories accessories) {
        if (gunAccessories != null) return false;
        else {
            gunAccessories = accessories;
            return true;
        }
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_ZAP);
        actions.add(AC_RELOAD);
        if (gunAccessories != null) actions.add(AC_REMOVE);
        return actions;
    }

    @Override
    public void execute(Hero hero) {
        if (this.bullet <= 0) {
            this.execute(hero, AC_RELOAD);
        } else {
            this.execute(hero, defaultAction);
        }
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_ZAP)) {

            if (Dungeon.hero.belongings.weapon != this) {
                GLog.n(Messages.get(this, "not_equipped"));
                QuickSlotButton.cancel();
            } else if (this.cursed) {
                Buff.affect(Dungeon.hero, Burning.class).reignite(Dungeon.hero, 4f);
                cursedKnown = true;
                bullet = Math.max(0, bullet - 1);
            } else {
                curUser = hero;
                curItem = this;
                cursedKnown = true;
                GameScene.selectCell(zapper);
            }
        }

        if (action.equals(AC_RELOAD)) {
            curUser = hero;
            GameScene.selectItem(itemSelector, WndBag.Mode.MISSILEWEAPON, Messages.get(this, "reload_prompt"));
            QuickSlotButton.cancel();
        }

        if (action.equals(AC_REMOVE)) {
            curUser = hero;
            Accessories ac = gunAccessories;
            if (ac.doPickUp(Dungeon.hero)) {
                GLog.i(Messages.get(Dungeon.hero, "you_now_have", ac.name()));
            } else {
                Dungeon.level.drop(ac, curUser.pos).sprite.drop();
            }
            gunAccessories = null;
            curUser.spendAndNext(1f);
        }
    }

    public void reload(int tier, boolean sp) {
        bulletTier = tier;
        bullet = Math.min(bullet + RELOAD_AMOUNT, bulletMax);
        specialBullet = sp ? Math.min(specialBullet + RELOAD_AMOUNT, bulletMax) : specialBullet;

        if (Dungeon.hero.subClass == HeroSubClass.FREERUNNER)
            Dungeon.hero.spendAndNext(RELOAD_DELAY / 2);
        else Dungeon.hero.spendAndNext(RELOAD_DELAY);
        Dungeon.hero.sprite.operate(Dungeon.hero.pos);
    }

    protected static CellSelector.Listener zapper = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer target) {

            if (target != null) {

                final GunWeapon ss;
                if (curItem instanceof GunWeapon) {
                    ss = (GunWeapon) GunWeapon.curItem;

                    Ballistica shot = new Ballistica(curUser.pos, target, Ballistica.PROJECTILE);
                    int cell = shot.collisionPos;

                    if (target == curUser.pos || cell == curUser.pos) {
                        GLog.i(Messages.get(GunWeapon.class, "self_target"));
                        return;
                    }

                    curUser.sprite.zap(cell);

                    //attempts to target the cell aimed at if something is there, otherwise targets the collision pos.
                    if (Actor.findChar(target) != null)
                        QuickSlotButton.target(Actor.findChar(target));
                    else
                        QuickSlotButton.target(Actor.findChar(cell));

                    if (ss.tryToZap(curUser, target)) {
                        curUser.busy();
                        ss.fx(shot, new Callback() {
                            public void call() {
                                ss.onZap(shot);
                            }
                        });
                    }

                }
            }

        }

        @Override
        public String prompt() {
            return Messages.get(GunWeapon.class, "prompt");
        }
    };

    protected void fx(Ballistica bolt, Callback callback) {
        MagicMissile.boltFromChar(curUser.sprite.parent,
                MagicMissile.GUN_SHOT,
                curUser.sprite,
                bolt.collisionPos,
                callback);
        Sample.INSTANCE.play(this.hitSound);
    }

    public boolean tryToZap(Hero owner, int target) {
        if (owner.STR() < STRReq()) {
            GLog.w(Messages.get(this, "weak"));
            return false;
        } else if (bullet >= 1) {
            specialFire = specialBullet > 0 || Random.Int(12 - bulletTier) == 0;
            return true;
        } else {
            GLog.w(Messages.get(this, "fizzles"));
            return false;
        }
    }

    protected void onZap(Ballistica bolt) {
        CloserangeShot closerRange = Dungeon.hero.buff(CloserangeShot.class);
        float oldacc = ACC;
        boolean anyKill = false;
        try {
            Char ch = Actor.findChar(bolt.collisionPos);
            if (ch != null) {
                Buff.affect(Dungeon.hero, RangedAttackTracker.class);
                processGunHit(ch, 1f, true);
                if (!ch.isAlive()) anyKill = true;
            } else {
                Dungeon.level.pressCellGunfire(bolt.collisionPos);
            }
            postShotCleanup(closerRange, false, anyKill);
        } finally {
            ACC = oldacc;
        }
    }

    protected boolean processGunHit(Char ch, float dmgMult, boolean triggerTalentProcs) {
        return processGunHit(ch, dmgMult, triggerTalentProcs, -1);
    }

    // use a caller-supplied DR value instead of rolling. Pass -1 to roll normally.
    protected boolean processGunHit(Char ch, float dmgMult, boolean triggerTalentProcs, int preRolledDr) {
        float dmg = fireDamageFactor(fireDamageRoll()) * dmgMult;
        int trueDmg = 0;
        if (ch.buff(Blindness.class) != null && Dungeon.hero.hasTalent(Talent.FLASH_SPEAR)) {
            trueDmg += (int) (dmg * (Dungeon.hero.pointsInTalent(Talent.FLASH_SPEAR) * 0.075f));
        }

        ACC = fireAccuracyFactor(getFireAcc(Dungeon.hero.pos, ch.pos));
        if (ACC <= 0f) {
            String missed = Messages.get(ch, "missed");
            ch.sprite.showStatus(CharSprite.NEUTRAL, missed);
            Sample.INSTANCE.play(Assets.Sounds.MISS);
            return false;
        } else if (Char.hit(Dungeon.hero, ch, false)) {

            // 첸 특성
            if (Dungeon.hero.hasTalent(Talent.TARGET_FOCUSING)) {
                if (Random.Int(3) < Dungeon.hero.pointsInTalent(Talent.TARGET_FOCUSING)) {
                    Buff.detach(ch, Camouflage.class);
                }
            }

            int dr = (preRolledDr >= 0) ? preRolledDr : ch.drRoll();

            int effectiveDamage = ch.defenseProc(Dungeon.hero, (int) dmg);

            // 사격 스롯 판정
            if (Dungeon.hero.subClass == HeroSubClass.SNIPER) dr /= 2;
            effectiveDamage = Math.max(effectiveDamage - dr, 0);

            if (ch.buff(Vulnerable.class) != null) {
                effectiveDamage = (int) (effectiveDamage * 1.33f);
            }

            if (triggerTalentProcs) {
                effectiveDamage = Dungeon.hero.attackProc(ch, effectiveDamage);
            }

            // If the enemy is already dead, interrupt the attack.
            // This matters as defence procs can sometimes inflict self-damage, such as armor glyphs.
            if (!ch.isAlive()) {
                return true;
            }

            ch.damage(effectiveDamage, Dungeon.hero);

            // if enemy is not dead from the main attack, process true damage
            if (ch.isAlive() && trueDmg > 0) {
                ch.trueDamage(trueDmg, this);
            }

            Sample.INSTANCE.play(Assets.Sounds.HIT, 1, Random.Float(0.87f, 1.15f));

            if (triggerTalentProcs && specialFire) {
                specialFire(ch);
            }

            ch.sprite.burst(0xFFFFFFFF, buffedLvl() / 2 + 2);

            if (triggerTalentProcs) {
                if (this instanceof C1_9mm) {
                    if (Random.Int(8) == 0) Buff.affect(ch, Chill.class, 2f);
                }
                // 사격 그레이스롯 판정
                int bonusTurns = Dungeon.hero.hasTalent(Talent.SHARED_UPGRADES) ? this.buffedLvl() : 0;
                if (Dungeon.hero.subClass == HeroSubClass.SNIPER)
                    Buff.prolong(Dungeon.hero, SnipersMark.class, SnipersMark.DURATION).set(ch.id(), bonusTurns);

                // 연계 블레이즈 판정
                if (Dungeon.hero.subClass == HeroSubClass.GLADIATOR) {
                    Buff.affect(Dungeon.hero, Combo.class).hit(ch);

                    if (Dungeon.hero.hasTalent(Talent.CLEAVE)) {
                        if (Random.Int(10) < Dungeon.hero.pointsInTalent(Talent.CLEAVE)) {
                            Buff.affect(Dungeon.hero, Combo.class).hit(ch);
                        }
                    }
                }

                if (Dungeon.hero.hasTalent(Talent.SPARKOFLIFE)) {
                    if (1 + Dungeon.hero.pointsInTalent(Talent.SPARKOFLIFE) > Random.Int(33)) {
                        Dungeon.hero.HP = Math.min(Dungeon.hero.HP + Dungeon.hero.HT / 20, Dungeon.hero.HT);
                    }
                }

                // 산사수 첸 판정
                if (Dungeon.hero.subClass == HeroSubClass.SPSHOOTER && ch.isAlive() && Dungeon.hero.buff(ChenShooterBuff.TACMoveCooldown.class) == null) {
                    Buff.prolong(Dungeon.hero, ChenShooterBuff.class, 5f).set(ch.id());
                }

                CloserangeShot closerRange = Dungeon.hero.buff(CloserangeShot.class);
                if (closerRange != null && ch.isAlive() && closerRange.state()) {
                    if (Dungeon.hero.hasTalent(Talent.WATER_PLAY) && Random.Int(5) < Dungeon.hero.pointsInTalent(Talent.WATER_PLAY)) {
                        Buff.affect(ch, Blindness.class, 1f);
                    }

                    if (Dungeon.hero.hasTalent(Talent.TAC_SHOT) && Dungeon.hero.buff(ChenShooterBuff.TACMove_tacshot.class) != null) {
                        int min = Dungeon.hero.pointsInTalent(Talent.TAC_SHOT) / 2;
                        int max = 1 + Dungeon.hero.pointsInTalent(Talent.TAC_SHOT) / 3;

                        Ballistica trajectory = new Ballistica(curUser.pos, ch.pos, Ballistica.STOP_TARGET);
                        trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size() - 1), Ballistica.PROJECTILE);
                        WandOfBlastWave.throwChar(ch, trajectory, Random.IntRange(min, max)); // 넉백 효과

                        Buff.detach(Dungeon.hero, ChenShooterBuff.TACMove_tacshot.class);
                    }
                }
            }
            return true;
        } else {
            String defense = ch.defenseVerb();
            ch.sprite.showStatus(CharSprite.NEUTRAL, defense);
            Sample.INSTANCE.play(Assets.Sounds.MISS);
            return false;
        }
    }

    protected void postShotCleanup(CloserangeShot closerRange, boolean pala, boolean anyTargetKilled) {
        Buff buff = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
        if (buff != null) buff.detach();
        buff = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
        if (buff != null) buff.detach();
        buff = Dungeon.hero.buff(RangedAttackTracker.class);
        if (buff != null) buff.detach();

        if (Dungeon.hero.buff(Bonk.BonkBuff.class) != null)
            Buff.detach(Dungeon.hero, Bonk.BonkBuff.class);

        Invisibility.dispel();

        // Each source independently rolls to save the bullet
        boolean savedBullet = false;
        if (gunAccessories != null && Random.Int(100) < gunAccessories.GetSavingChance()) {
            savedBullet = true;
        }
        if (closerRange != null && closerRange.state() && Dungeon.hero.hasTalent(Talent.FRUGALITY)
                && Random.Int(100) < Dungeon.hero.pointsInTalent(Talent.FRUGALITY) * 15) {
            savedBullet = true;
        }
        if (Random.Float() < RingOfSharpshooting.ammoMultiplier(Dungeon.hero)) {
            savedBullet = true;
        }

        // Only consume if no source saved the bullet
        if (!savedBullet) {
            bullet = Math.max(0, bullet - 1);
            specialBullet = Math.max(0, specialBullet - 1);
        }
        updateQuickslot();

        if (pala) {
            curUser.spendAndNext(fireDelayFactor(curUser, FIRE_DELAY_MULT / 4));
        } else curUser.spendAndNext(fireDelayFactor(curUser, FIRE_DELAY_MULT));

        if (anyTargetKilled && Dungeon.hero.hasTalent(Talent.BF_RULL) && Random.Int(5) < Dungeon.hero.pointsInTalent(Talent.BF_RULL)) {
            Buff.affect(Dungeon.hero, Swiftthistle.TimeBubble.class).bufftime(1f);
        }
    }

    @Override
    public String status() {
        return bullet + "";
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", bulletTier);
    }

    @Override
    public String info() {
        String info = super.info();
        if (gunAccessories != null) {
            info += "\n\n" + Messages.get(gunAccessories, "desc");
        }
        return info;
    }

    private final WndBag.Listener itemSelector = new WndBag.Listener() {
        @Override
        public void onSelect(final Item item) {
            if (item != null) {
                if (item instanceof Thunderbolt) {
                    bulletMax += 3;
                    gamza = true;
                }
                if (item instanceof UpMagazine) {
                    reload(((MissileWeapon) item).tier, true);
                } else reload(((MissileWeapon) item).tier, false);
                item.detach(Dungeon.hero.belongings.backpack);
            }
        }
    };

    @Override
    public String name() {
        if (gamza) return Messages.get(this, "gamza_name");
        return super.name();
    }

    public String statsInfo() {
        if (specialBullet > 0)
            return Messages.get(this, "stats_desc_sp", fireMin(), fireMax(), specialBullet, getMinRange(), getMaxRange());
        return Messages.get(this, "stats_desc", fireMin(), fireMax(), getMinRange(), getMaxRange());
    }

    private static final String BULLET = "bullet";
    private static final String BULLET_CAP = "bulletCap";
    private static final String GAMZA = "gamza";
    private static final String TIER = "bullettier";
    private static final String SP = "spshot";
    private static final String SP_BULLET_COUNT = "spBulletCount";
    private static final String ACCESSORIES = "GunAccessories";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(BULLET, bullet);
        bundle.put(BULLET_CAP, bulletMax);
        bundle.put(GAMZA, gamza);
        bundle.put(TIER, bulletTier);
        bundle.put(SP_BULLET_COUNT, specialBullet);
        bundle.put(ACCESSORIES, gunAccessories);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        bulletMax = bundle.getInt(BULLET_CAP);
        if (bulletMax > 0) bullet = Math.min(bulletMax, bundle.getInt(BULLET));
        else bullet = bundle.getInt(BULLET);

        bulletTier = bundle.getInt(TIER);
        specialBullet = bundle.getInt(SP_BULLET_COUNT);
        gamza = bundle.getBoolean(GAMZA);
        gunAccessories = (Accessories) bundle.get(ACCESSORIES);
    }

    // Marker buff to indicate the current attack is a ranged attack from a gun weapon
    // Used to distinguish ranged attacks (onZap) from melee attacks (proc) with the same weapon
    public static class RangedAttackTracker extends Buff {
    }
}

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.miniboss;

import static com.shatteredpixel.shatteredpixeldungeon.actors.Char.Property.STATIC;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EndspeakerAspect;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.NervousImpairment;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silence;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.NetherseaBrandguider;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SeaLeef;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Sea_Octo;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.NewGameItem.Certificate;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SanityPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.levels.SeaLevel_part2;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.SeaPlatform;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Endspeaker1Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Endspeaker2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Endspeaker3Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Endspeaker4Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

import lombok.Getter;

public class TheEndspeaker extends Mob {
    {
        spriteClass = Status.getSprite();

        HP = HT = Status.getMaxHp();
        defenseSkill = Status.getDefense();

        EXP = Status.getExp();
        maxLvl = 45;

        actPriority = MOB_PRIO - 1;

        state = PASSIVE;

        properties.add(Property.BOSS);
        addImmunities();
    }

    private int spellAbsorptionCooldown = 0;
    private int chargeCooldown = 6;
    private int chargePos = -1;
    private int chargesRemaining = 0;

    @Override
    public int damageRoll() {
        int low = 10 + (Status.abilityCount / 2) * 8;
        int high = 27 + (Status.abilityCount / 2) * 11;
        int damage = Random.NormalIntRange(low, high);

        // Apply Ramp Up damage bonus (5% per stack)
        RampUpStacks rampBuff = buff(RampUpStacks.class);
        if (Status.abilityRampUp && rampBuff != null) {
            damage = Math.round(damage * (1f + (rampBuff.getStacks() * 0.05f)));
        }

        return damage;
    }

    @Override
    public int attackSkill(Char target) {
        return 60;
    }

    @Override
    public int drRoll() {
        int low = 5 + (Status.abilityCount / 2) * 4;
        int high = 13 + (Status.abilityCount / 2) * 6;
        return Random.NormalIntRange(low, high);
    }

    @Override
    public void damage(int dmg, Object src) {
        if (Status.abilitySpellAbsorption && !isSilenced() && spellAbsorptionCooldown <= 0 && src != null && AntiMagic.RESISTS.contains(src.getClass())) {
            dmg = dmg / 4;
            Buff.affect(this, SpellAbsorptionActive.class);
            spellAbsorptionCooldown = Dungeon.isChallenged(Challenges.DECISIVE_BATTLE) ? 4 : 5;
        }
        if (buff(HardeningActive.class) != null) {
            dmg = dmg / 2;
        }
        if (state == PASSIVE) state = HUNTING;
        super.damage(dmg, src);
        if (Status.abilityHardening && !isSilenced() && isAlive() && HP < HT / 4) {
            Buff.affect(this, HardeningActive.class);
        }
    }

    @Override
    protected boolean canAttack(Char enemy) {
        if (Status.abilityIncreasedRange) {
            return this.fieldOfView[enemy.pos] && Dungeon.level.distance(this.pos, enemy.pos) <= 3;
        }
        return Dungeon.level.adjacent(this.pos, enemy.pos);
    }

    @Override
    protected boolean doAttack(Char enemy) {
        SpellAbsorptionActive spellBuff = buff(SpellAbsorptionActive.class);
        if (Status.abilitySpellAbsorption && !isSilenced() && spellBuff != null) {
            spellBuff.detach();
            return zap();
        } else {
            return super.doAttack(enemy);
        }
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        int dmg = super.attackProc(enemy, damage);

        // Ramp Up: gain stack only on successful hit
        if (dmg > 0) {
            if (Status.abilityRampUp) {
                Buff.affect(this, RampUpStacks.class).addStack();
            }
            Buff.affect(enemy, NervousImpairment.class).sum(Status.getNerveDamage());
            if (Status.abilityCount > 4) {
                this.HP = Math.min(HT, this.HP + (int) (dmg * 0.25));
            }
        }

        return dmg;
    }

    protected boolean zap() {
        if (enemy == null) return false;

        spend(1f);

        if (hit(this, enemy, true)) {
            int dmg = this.damageRoll();
            enemy.damage(dmg, this);

            // Ramp Up: grant stack on successful zap hit
            if (dmg > 0 && Status.abilityRampUp) {
                Buff.affect(this, RampUpStacks.class).addStack();
            }

            if (enemy == Dungeon.hero) {

                Camera.main.shake(2, 0.3f);

                if (!enemy.isAlive()) {
                    Dungeon.fail(getClass());
                    GLog.n(Messages.get(this, "kill"));
                }
            }

        } else {
            enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
        }

        if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
            sprite.attack(enemy.pos);
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected boolean act() {
        if (state == PASSIVE) return super.act();

        // Spell absorption cooldown ticks independently so it's not frozen during a charge sequence
        spellAbsorptionCooldown--;

        // Maintain Ramp Up stacks while a charge sequence is in progress so they don't decay between charges
        if (Status.abilityCharge && Status.abilityRampUp && (chargesRemaining > 0 || chargePos != -1)) {
            RampUpStacks rampBuff = buff(RampUpStacks.class);
            if (rampBuff != null) rampBuff.turnsWithoutDamage = 0;
        }

        // Execute charge if position is set, cancel sequence if silenced
        if (Status.abilityCharge && chargePos != -1) {
            if (isSilenced()) {
                endChargeSequence();
            } else {
                executeCharge();
                return false;
            }
        }

        // Start a new charge sequence when cooldown is ready; retry next turn on setup failure
        if (Status.abilityCharge && !isSilenced() && chargeCooldown <= 0 && chargesRemaining == 0
                && enemy != null && enemy.isAlive() && !rooted) {
            if (setupCharge()) {
                chargesRemaining = Dungeon.isChallenged(Challenges.DECISIVE_BATTLE) ? 5 : 4;
                if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[chargePos]) {
                    GLog.w(Messages.get(this, "charge"));
                }
                spend(TICK);
                return true;
            }
            // Setup failed (no line of sight, no landing tile, etc) — fall through and retry next act
        }

        // Re-apply hardening if silence wore off and HP is still low
        if (Status.abilityHardening && !isSilenced() && HP < HT / 4 && buff(HardeningActive.class) == null) {
            Buff.affect(this, HardeningActive.class);
        }

        reduceCooldown();
        return super.act();
    }

    private void endChargeSequence() {
        chargesRemaining = 0;
        chargePos = -1;
        chargeCooldown = Dungeon.isChallenged(Challenges.DECISIVE_BATTLE) ? Random.NormalIntRange(5, 7) : Random.NormalIntRange(6, 8);
    }

    private boolean setupCharge() {
        if (enemy == null || !enemy.isAlive()) return false;

        // Verify path to enemy is not blocked by terrain
        Ballistica b = new Ballistica(pos, enemy.pos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
        if (b.collisionPos != enemy.pos) {
            return false;
        }

        // Target one cell past the enemy in the direction of travel; fall back to enemy's cell
        int w = Dungeon.level.width();
        int dx = Integer.signum((enemy.pos % w) - (pos % w));
        int dy = Integer.signum((enemy.pos / w) - (pos / w));
        int beyondPos = enemy.pos + dy * w + dx;

        int targetPos;
        if (beyondPos >= 0 && beyondPos < Dungeon.level.length() && Dungeon.level.passable[beyondPos]) {
            targetPos = beyondPos;
        } else {
            targetPos = enemy.pos;
        }

        // Check if we can land at target
        Char targetChar = Actor.findChar(targetPos);
        if (targetChar != null) {
            // Find landing spot next to target
            int bouncepos = -1;
            for (int i : PathFinder.NEIGHBOURS8) {
                if ((bouncepos == -1 || Dungeon.level.trueDistance(pos, targetPos + i) < Dungeon.level.trueDistance(pos, bouncepos))
                        && Actor.findChar(targetPos + i) == null && Dungeon.level.passable[targetPos + i]) {
                    bouncepos = targetPos + i;
                }
            }
            if (bouncepos == -1) {
                return false;
            }
        }

        // Setup charge
        chargePos = targetPos;

        // Visual warning — GLog is handled by the caller so chained charges don't spam it
        if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[chargePos]) {
            sprite.parent.addToBack(new TargetedCell(chargePos, 0xFF0000));
            Dungeon.hero.interrupt();
        }

        return true;
    }

    private void executeCharge() {
        Ballistica b = new Ballistica(pos, chargePos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);

        // If path is now blocked, cancel the whole sequence
        if (rooted || b.collisionPos != chargePos) {
            endChargeSequence();
            spend(TICK);
            next();
            return;
        }

        final Char chargeVictim = Actor.findChar(chargePos);
        final int endPos;

        // Find landing position
        if (chargeVictim != null) {
            int bouncepos = -1;
            for (int i : PathFinder.NEIGHBOURS8) {
                if ((bouncepos == -1 || Dungeon.level.trueDistance(pos, chargePos + i) < Dungeon.level.trueDistance(pos, bouncepos))
                        && Actor.findChar(chargePos + i) == null && Dungeon.level.passable[chargePos + i]) {
                    bouncepos = chargePos + i;
                }
            }
            if (bouncepos == -1) {
                endChargeSequence();
                spend(TICK);
                next();
                return;
            }
            endPos = bouncepos;
        } else {
            endPos = chargePos;
        }

        // Do the charge with animation
        sprite.visible = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[chargePos] || Dungeon.level.heroFOV[endPos];
        sprite.dash(pos, chargePos, new Callback() {
            @Override
            public void call() {
                // Damage all characters along the path
                for (int cell : b.path) {
                    Char ch = Actor.findChar(cell);
                    if (ch != null && ch != TheEndspeaker.this) {
                        int base = damageRoll();
                        int damage = Random.NormalIntRange(base / 2, base);
                        int hpBefore = ch.HP;
                        ch.damage(damage, TheEndspeaker.this);
                        // Ramp Up: grant stack on successful charge hit
                        if (Status.abilityRampUp && ch.HP < hpBefore) {
                            Buff.affect(TheEndspeaker.this, RampUpStacks.class).addStack();
                        }
                        if (ch instanceof Hero && !ch.isAlive()) {
                            Dungeon.fail(getClass());
                            GLog.n(Messages.get(Char.class, "kill", name()));
                        } else if (ch.isAlive()) {
                            Buff.prolong(ch, Cripple.class, 3f);
                        }
                        ch.sprite.flash();
                        Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
                    }
                }

                // Move to end position
                if (endPos != chargePos) {
                    Actor.addDelayed(new Pushing(TheEndspeaker.this, chargePos, endPos), -1);
                }

                pos = endPos;
                chargePos = -1;
                chargesRemaining--;
                sprite.idle();
                Dungeon.level.occupyCell(TheEndspeaker.this);

                // Chain the next charge in the sequence, or end it
                boolean chained = false;
                if (chargesRemaining > 0 && !isSilenced() && enemy != null && enemy.isAlive() && !rooted) {
                    chained = setupCharge();
                }
                if (!chained) {
                    endChargeSequence();
                }

                spend(TICK);
                next();
            }
        });
    }

    private void reduceCooldown() {
        chargeCooldown--;
    }

    @Override
    public boolean isImmune(Class effect) {
        if (effect == Silence.class && !Status.abilityCcImmune) {
            return false;
        }
        return super.isImmune(effect);
    }

    private boolean isSilenced() {
        return buff(Silence.class) != null;
    }

    private void addImmunities() {
        if (Status.abilityCcImmune) {
            properties.add(STATIC);
            immunities.add(Silence.class);
        }
    }

    @Override
    public void die(Object cause) {
        super.die(cause);

        int lootTier = Status.abilityCount / 2;
        Ankh lootAnkh;
        dropLoot(new PotionOfExperience());
        dropLoot(new SanityPotion().quantity(5));
        switch (lootTier) {
            case 1:
                // 1단계 폼 (2 형태)
                Dungeon.level.drop(new Certificate(25), pos).sprite.drop(pos);
                dropLoot(new ScrollOfUpgrade());
                dropLoot(Generator.random(Generator.Category.EXOTIC_POTION));
                dropLoot(Generator.random(Generator.Category.EXOTIC_SCROLL));
                dropLoot(Generator.random(Generator.Category.SKL_T3));
                dropLoot(new SeaPlatform.LittleHandy().quantity(10));
                break;
            case 2:
                // 2딘계 폼 (3 형태)
                Dungeon.level.drop(new Certificate(40), pos).sprite.drop(pos);
                dropLoot(new ScrollOfUpgrade());
                dropLoot(Generator.random(Generator.Category.EXOTIC_POTION));
                dropLoot(Generator.random(Generator.Category.EXOTIC_SCROLL));
                dropLoot(Generator.random(Generator.Category.ELIXIR_BREW));
                dropLoot(Generator.random(Generator.Category.SKL_T2));
                dropLoot(Generator.random(Generator.Category.SKL_T3));
                lootAnkh = new Ankh();
                lootAnkh.bless();
                dropLoot(lootAnkh);
                dropLoot(new SeaPlatform.LittleHandy().quantity(10));
                break;
            case 3:
                // 3단계 폼 (4 형태)
                Dungeon.level.drop(new Certificate(75), pos).sprite.drop(pos);
                dropLoot(new ScrollOfUpgrade());
                dropLoot(Generator.random(Generator.Category.EXOTIC_POTION));
                dropLoot(Generator.random(Generator.Category.EXOTIC_SCROLL));
                dropLoot(Generator.random(Generator.Category.ELIXIR_BREW));
                dropLoot(Generator.random(Generator.Category.SKL_T2));
                dropLoot(Generator.random(Generator.Category.SKL_T3));
                lootAnkh = new Ankh();
                lootAnkh.bless();
                dropLoot(lootAnkh);
                dropLoot(new SeaPlatform.EnhancedLittleHandy());
                Badges.validateEndspeakerTier4Kill();
                break;
            case 0:
            default:
                // 기본 폼 (1 형태)
                Dungeon.level.drop(new Certificate(15), pos).sprite.drop(pos);
                dropLoot(Generator.random(Generator.Category.POTION));
                dropLoot(Generator.random(Generator.Category.SCROLL));
                dropLoot(new SeaPlatform.LittleHandy().quantity(5));
                break;
        }
    }

    private void dropLoot(Item item) {
        ArrayList<Integer> candidates = new ArrayList<>();
        for (int offset : PathFinder.NEIGHBOURS8) {
            if (Dungeon.level.passable[pos + offset]) {
                candidates.add(pos + offset);
            }
        }
        int lootPos = candidates.isEmpty() ? pos : Random.element(candidates);
        Dungeon.level.drop(item, lootPos).sprite.drop(pos);
    }

    @Override
    public String description() {
        String desc = Messages.get(this, "desc");
        desc += Messages.get(this, "desc_" + Status.abilityCount / 2);
        if (Status.abilityCount > 0) {
            desc += Messages.get(this, "desc_sp");
            if (Status.abilitySpellAbsorption)
                desc += Messages.get(this, "desc_sp_spellabsorption");
            if (Status.abilityIncreasedRange) desc += Messages.get(this, "desc_sp_increasedrange");
            if (Status.abilityRampUp) desc += Messages.get(this, "desc_sp_rampup");
            if (Status.abilityCharge) desc += Messages.get(this, "desc_sp_charge");
            if (Status.abilityHardening) desc += Messages.get(this, "desc_sp_hardening");
            if (Status.abilityCcImmune) desc += Messages.get(this, "desc_sp_crowdcontrolimmune");
        }
        return desc;
    }

    private static final String SPELL_ABSORPTION_CD = "spell_absorption_cooldown";
    private static final String CHARGE_CD = "charge_cooldown";
    private static final String CHARGE_POS = "charge_pos";
    private static final String CHARGES_REMAINING = "charges_remaining";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(SPELL_ABSORPTION_CD, spellAbsorptionCooldown);
        bundle.put(CHARGE_CD, chargeCooldown);
        bundle.put(CHARGE_POS, chargePos);
        bundle.put(CHARGES_REMAINING, chargesRemaining);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        spellAbsorptionCooldown = bundle.getInt(SPELL_ABSORPTION_CD);
        chargeCooldown = bundle.contains(CHARGE_CD) ? bundle.getInt(CHARGE_CD) : 3;
        chargePos = bundle.contains(CHARGE_POS) ? bundle.getInt(CHARGE_POS) : -1;
        chargesRemaining = bundle.contains(CHARGES_REMAINING) ? bundle.getInt(CHARGES_REMAINING) : 0;
    }

    /**
     * Buff that tracks and displays Ramp Up stacks
     * Duration automatically manages the reset cooldown
     */
    public static class RampUpStacks extends Buff {

        @Getter
        private int stacks = 0;
        private int turnsWithoutDamage = 0;

        {
            type = buffType.POSITIVE;
            announced = true;
        }

        @Override
        public int icon() {
            return BuffIndicator.UPGRADE;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0xFF9900);
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", stacks, (int) (stacks * 5));
        }

        public void addStack() {
            stacks = Math.min(stacks + 1, 30);
            turnsWithoutDamage = 0;
        }

        public void removeStack() {
            stacks = stacks - 2;
            if (stacks <= 0) {
                detach();
            } else {
                spend(TICK);
            }
        }

        @Override
        public boolean act() {
            turnsWithoutDamage++;
            if (turnsWithoutDamage >= 3) {
                removeStack();
            } else {
                spend(TICK);
            }
            return true;
        }

        private static final String STACKS = "stacks";
        private static final String TURNS_NO_DMG = "turnsWithoutDamage";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(STACKS, stacks);
            bundle.put(TURNS_NO_DMG, turnsWithoutDamage);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            stacks = bundle.getInt(STACKS);
            turnsWithoutDamage = bundle.getInt(TURNS_NO_DMG);
        }
    }

    /**
     * Buff that indicates Spell Absorption counter-attack is ready
     */
    public static class SpellAbsorptionActive extends Buff {

        {
            type = buffType.POSITIVE;
            announced = true;
        }

        @Override
        public int icon() {
            return BuffIndicator.ARMOR;
        }

        @Override
        public void tintIcon(com.watabou.noosa.Image icon) {
            icon.hardlight(0x00FFFF);
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc");
        }

        @Override
        public boolean act() {
            // This buff is manually controlled, not time-based
            spend(TICK);
            return true;
        }
    }

    /**
     * Visual indicator buff for Hardening ability — shown while HP < 25% of max
     */
    public static class HardeningActive extends Buff {

        {
            type = buffType.POSITIVE;
        }

        @Override
        public int icon() {
            return BuffIndicator.ARMOR;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0x2244CC);
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc");
        }

        @Override
        public void fx(boolean on) {
            if (on && target.sprite != null) {
                target.sprite.shieldHalo(0x2244CC);
            } else if (!on && target.sprite != null) {
                target.sprite.clearShieldHalo();
            }
        }

        @Override
        public boolean act() {
            if (target.HP >= target.HT / 4 || target.buff(Silence.class) != null) {
                detach();
            } else {
                spend(TICK);
            }
            return true;
        }
    }

    public static class AspectSmall extends Sea_Octo {
        {
            state = PASSIVE;
            loot = new PotionOfStrength();
            lootChance = 1f;
            immunities.add(AllyBuff.class);
        }

        private boolean isEmpowered = false;

        @Override
        public boolean act() {
            EndspeakerAspect.Empowering buff = buff(EndspeakerAspect.Empowering.class);
            if (!isEmpowered && buff != null) {
                HP = HT = 250;
                defenseSkill = 16;
                loot = new ScrollOfUpgrade();
                isEmpowered = true;
            }
            return super.act();
        }

        @Override
        public int damageRoll() {
            if (isEmpowered) {
                return Random.NormalIntRange(40, 62);
            }
            return super.damageRoll();
        }

        @Override
        protected boolean canAttack(Char enemy) {
            if (isEmpowered) {
                return fieldOfView[enemy.pos] && Dungeon.level.distance(pos, enemy.pos) <= 8;
            }
            return super.canAttack(enemy);
        }

        @Override
        public int drRoll() {
            if (isEmpowered) {
                return Random.NormalIntRange(9, 27);
            }
            return super.drRoll();
        }

        @Override
        public void damage(int dmg, Object src) {
            // Only the hero can initially activate/attack this aspect — ally/clone attacks are blocked while PASSIVE
            if (state == PASSIVE && src instanceof Char && !(src instanceof Hero)) {
                if (sprite != null)
                    sprite.showStatus(CharSprite.POSITIVE, Messages.get(Char.class, "invulnerable"));
                return;
            }
            if (state == PASSIVE) state = HUNTING;
            super.damage(dmg, src);
        }

        @Override
        public void die(Object cause) {
            removeAbility(this);
            empowerRemainingAspect();
            super.die(cause);
        }

        @Override
        public void destroy() {
            grantAbility(this);
            super.destroy();
        }

        @Override
        public String description() {
            String description = super.description();
            description += Messages.get(TheEndspeaker.class, "aspect_desc");
            if (isEmpowered) {
                description += Messages.get(this, "empowered");
            }
            return description;
        }

        private static final String IS_EMPOWERED = "is_empowered";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(IS_EMPOWERED, isEmpowered);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            isEmpowered = bundle.getBoolean(IS_EMPOWERED);
        }
    }

    public static class AspectMedium extends SeaLeef {
        {
            state = PASSIVE;
            loot = new PotionOfStrength();
            lootChance = 1f;
            immunities.add(AllyBuff.class);
        }

        private boolean isEmpowered = false;

        @Override
        public boolean act() {
            EndspeakerAspect.Empowering buff = buff(EndspeakerAspect.Empowering.class);
            if (!isEmpowered && buff != null) {
                HP = HT = 270;
                defenseSkill = 20;
                loot = new ScrollOfUpgrade();
                isEmpowered = true;
            }
            return super.act();
        }

        @Override
        public int damageRoll() {
            if (isEmpowered) {
                int bonus = 0;
                SeaLeef.DamageRampUp ramp = buff(SeaLeef.DamageRampUp.class);
                if (ramp != null) bonus = ramp.getBonus();
                return Random.NormalIntRange(24 + (bonus / 2), 36 + bonus);
            }
            return super.damageRoll();
        }

        @Override
        protected float attackDelay() {
            if (isEmpowered) {
                return 1f / 3f;
            }
            return super.attackDelay();
        }

        @Override
        public int drRoll() {
            if (isEmpowered) {
                return Random.NormalIntRange(5, 15);
            }
            return super.drRoll();
        }

        @Override
        public void damage(int dmg, Object src) {
            // Only the hero can initially activate/attack this aspect — ally/clone attacks are blocked while PASSIVE
            if (state == PASSIVE && src instanceof Char && !(src instanceof Hero)) {
                if (sprite != null)
                    sprite.showStatus(CharSprite.POSITIVE, Messages.get(Char.class, "invulnerable"));
                return;
            }
            if (state == PASSIVE) state = HUNTING;
            super.damage(dmg, src);
        }

        @Override
        public void die(Object cause) {
            removeAbility(this);
            empowerRemainingAspect();
            super.die(cause);
        }

        @Override
        public void destroy() {
            grantAbility(this);
            super.destroy();
        }

        @Override
        public String description() {
            String description = super.description();
            description += Messages.get(TheEndspeaker.class, "aspect_desc");
            if (isEmpowered) {
                description += Messages.get(this, "empowered");
            }
            return description;
        }

        private static final String IS_EMPOWERED = "is_empowered";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(IS_EMPOWERED, isEmpowered);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            isEmpowered = bundle.getBoolean(IS_EMPOWERED);
        }
    }

    public static class AspectLarge extends NetherseaBrandguider {
        {
            state = PASSIVE;
            loot = new PotionOfStrength();
            lootChance = 1f;
            immunities.add(AllyBuff.class);
        }

        private boolean isEmpowered = false;

        @Override
        public boolean act() {
            EndspeakerAspect.Empowering buff = buff(EndspeakerAspect.Empowering.class);
            if (!isEmpowered && buff != null) {
                HP = HT = 320;
                defenseSkill = 25;
                loot = new ScrollOfUpgrade();
                isEmpowered = true;
            }
            return super.act();
        }

        @Override
        public int damageRoll() {
            if (isEmpowered) {
                return Random.NormalIntRange(47, 68);
            }
            return super.damageRoll();
        }

        @Override
        protected boolean shouldAlwaysGenerateSeaTerror() {
            return isEmpowered;
        }

        @Override
        public int drRoll() {
            if (isEmpowered) {
                if (buff(NetherseaBrandguider.Reinforced.class) != null)
                    return Random.NormalIntRange(20, 60);
                return Random.NormalIntRange(10, 30);
            }
            return super.drRoll();
        }

        @Override
        public void damage(int dmg, Object src) {
            // Only the hero can initially activate/attack this aspect — ally/clone attacks are blocked while PASSIVE
            if (state == PASSIVE && src instanceof Char && !(src instanceof Hero)) {
                if (sprite != null)
                    sprite.showStatus(CharSprite.POSITIVE, Messages.get(Char.class, "invulnerable"));
                return;
            }
            if (state == PASSIVE) state = HUNTING;
            super.damage(dmg, src);
        }

        @Override
        public void die(Object cause) {
            removeAbility(this);
            empowerRemainingAspect();
            super.die(cause);
        }

        @Override
        public void destroy() {
            grantAbility(this);
            super.destroy();
        }

        @Override
        public String description() {
            String description = super.description();
            description += Messages.get(TheEndspeaker.class, "aspect_desc");
            if (isEmpowered) {
                description += Messages.get(this, "empowered");
            }
            return description;
        }

        private static final String IS_EMPOWERED = "is_empowered";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(IS_EMPOWERED, isEmpowered);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            isEmpowered = bundle.getBoolean(IS_EMPOWERED);
        }
    }

    protected static void removeAbility(Mob mob) {
        for (EndspeakerAspect buff : mob.buffs(EndspeakerAspect.class)) {
            buff.detach();
        }
    }

    protected static void grantAbility(Mob mob) {
        boolean granted = false;
        for (EndspeakerAspect buff : mob.buffs(EndspeakerAspect.class)) {
            if (buff.giveAbility()) {
                TheEndspeaker.Status.abilityCount++;
                granted = true;
            }
        }
        if (granted) {
            Status.pendingDestroyMessage = true;
        }
    }

    protected static void empowerRemainingAspect() {
        boolean empowered = false;
        for (Mob mob : Dungeon.level.mobs) {
            if (mob.isAlive() && (mob instanceof AspectSmall
                    || mob instanceof AspectMedium
                    || mob instanceof AspectLarge)) {
                Buff.affect(mob, EndspeakerAspect.Empowering.class);
                empowered = true;
            }
        }
        if (empowered) {
            GLog.w(Messages.get(TheEndspeaker.class, "aspect_die_empower"));
        } else {
            GLog.w(Messages.get(TheEndspeaker.class, "aspect_die_last"));
        }
    }

    public static class Status {
        public static boolean spawned;
        public static boolean pendingDestroyMessage;
        public static boolean spawnMsgShown;
        public static boolean abilitySpellAbsorption;
        public static boolean abilityHardening;
        public static boolean abilityCcImmune;
        public static boolean abilityIncreasedRange;
        public static boolean abilityRampUp;
        public static boolean abilityCharge;

        public static int abilityCount;

        public static Class<? extends MobSprite> getSprite() {
            switch (abilityCount / 2) {
                case 1:
                    return Endspeaker2Sprite.class;
                case 2:
                    return Endspeaker3Sprite.class;
                case 3:
                    return Endspeaker4Sprite.class;
                case 0:
                default:
                    return Endspeaker1Sprite.class;
            }
        }

        public static int getMaxHp() {
            switch (abilityCount / 2) {
                case 1:
                    return 1000;
                case 2:
                    return 1200;
                case 3:
                    return 1500;
                case 0:
                default:
                    return 600;
            }
        }

        public static int getDefense() {
            switch (abilityCount / 2) {
                case 1:
                    return 20;
                case 2:
                    return 23;
                case 3:
                    return 25;
                case 0:
                default:
                    return 10;
            }
        }

        public static int getExp() {
            switch (abilityCount / 2) {
                case 1:
                    return 100;
                case 2:
                    return 125;
                case 3:
                    return 200;
                case 0:
                default:
                    return 50;
            }
        }

        public static int getNerveDamage() {
            switch (abilityCount / 2) {
                case 1:
                    return 15;
                case 2:
                    return 5;
                case 3:
                    return 10;
                case 0:
                default:
                    return 2;
            }
        }

        public static void spawnAspects(SeaLevel_part2 level) {
            if (Dungeon.depth > 35 && Dungeon.depth < 40) {
                switch (Dungeon.depth) {
                    case 36:
                        AspectSmall aspectRange = new AspectSmall();
                        AspectSmall aspectSpell = new AspectSmall();
                        summonMob(level, aspectSpell, EndspeakerAspect.SpellAbsorption.class);
                        summonMob(level, aspectRange, EndspeakerAspect.IncreasedRange.class);
                        spawnMsgShown = false;
                        break;
                    case 37:
                        AspectMedium aspectRamp = new AspectMedium();
                        AspectMedium aspectCharge = new AspectMedium();
                        summonMob(level, aspectRamp, EndspeakerAspect.RampUp.class);
                        summonMob(level, aspectCharge, EndspeakerAspect.Charge.class);
                        spawnMsgShown = false;
                        break;
                    case 38:
                        AspectLarge aspectHarden = new AspectLarge();
                        AspectLarge aspectCcImmune = new AspectLarge();
                        summonMob(level, aspectHarden, EndspeakerAspect.Hardening.class);
                        summonMob(level, aspectCcImmune, EndspeakerAspect.CrowdControlImmune.class);
                        spawnMsgShown = false;
                        break;
                    default:
                        break;
                }
            }
        }

        public static void destroyAspects() {
            if (!(Dungeon.level instanceof SeaLevel_part2)
                    || Dungeon.depth < 36 || Dungeon.depth > 38
                    || Dungeon.level.mobs == null) {
                return;
            }
            ArrayList<Mob> toDestroy = new ArrayList<>();
            for (Mob mob : Dungeon.level.mobs) {
                if ((mob instanceof AspectSmall
                        || mob instanceof AspectMedium
                        || mob instanceof AspectLarge) && mob.isAlive()) {
                    toDestroy.add(mob);
                }
            }
            for (Mob mob : toDestroy) {
                mob.EXP = 0;
                mob.destroy();
            }
        }

        private static <T extends EndspeakerAspect> void summonMob(SeaLevel_part2 level, Mob mob, Class<T> ability) {
            if (spawned) {
                return;
            }
            do {
                mob.pos = level.randomRespawnCell(mob);
            } while (
                    mob.pos == -1 ||
                            level.heaps.get(mob.pos) != null ||
                            level.traps.get(mob.pos) != null ||
                            level.findMob(mob.pos) != null ||
                            !(level.passable[mob.pos + PathFinder.CIRCLE4[0]] && level.passable[mob.pos + PathFinder.CIRCLE4[2]]) ||
                            !(level.passable[mob.pos + PathFinder.CIRCLE4[1]] && level.passable[mob.pos + PathFinder.CIRCLE4[3]])
            );
            Buff.affect(mob, ability);
            level.mobs.add(mob);
        }

        public static boolean activate(EndspeakerAspect.EndspeakerAbility ability) {
            if (spawned) {
                return false;
            }
            switch (ability) {
                case SPELL_ABSORPTION:
                    abilitySpellAbsorption = true;
                    break;
                case INCREASED_RANGE:
                    abilityIncreasedRange = true;
                    break;
                case RAMP_UP:
                    abilityRampUp = true;
                    break;
                case CHARGE:
                    abilityCharge = true;
                    break;
                case HARDENING:
                    abilityHardening = true;
                    break;
                case CROWD_CONTROL_IMMUNE:
                    abilityCcImmune = true;
                    break;
                default:
                    return false;
            }
            return true;
        }

        public static void reset() {
            spawned = false;
            pendingDestroyMessage = false;
            abilitySpellAbsorption = false;
            abilityHardening = false;
            abilityCcImmune = false;
            abilityIncreasedRange = false;
            abilityRampUp = false;
            abilityCharge = false;
            abilityCount = 0;
            spawnMsgShown = false;
        }

        private static final String NODE = "theEndspeakerStatus";

        private static final String SPAWNED = "spawned";
        private static final String PENDING_DESTROY = "pendingDestroyMessage";
        private static final String ABILITY_SPELL = "abilitySpell";
        private static final String ABILITY_HARDENING = "abilityHardening";
        private static final String ABILITY_CC = "abilityCcImmune";
        private static final String ABILITY_RANGE = "abilityRange";
        private static final String ABILITY_RAMP = "abilityRamp";
        private static final String ABILITY_CHARGE = "abilityCharge";
        private static final String ABILITY_COUNT = "abilityCount";
        private static final String SPAWN_MSG = "spawnMsgShown";

        public static void storeInBundle(Bundle bundle) {
            Bundle node = new Bundle();

            node.put(SPAWNED, spawned);
            node.put(PENDING_DESTROY, pendingDestroyMessage);

            node.put(ABILITY_SPELL, abilitySpellAbsorption);
            node.put(ABILITY_HARDENING, abilityHardening);
            node.put(ABILITY_CC, abilityCcImmune);
            node.put(ABILITY_RANGE, abilityIncreasedRange);
            node.put(ABILITY_RAMP, abilityRampUp);
            node.put(ABILITY_CHARGE, abilityCharge);
            node.put(ABILITY_COUNT, abilityCount);
            node.put(SPAWN_MSG, spawnMsgShown);

            bundle.put(NODE, node);
        }

        public static void restoreFromBundle(Bundle bundle) {
            Bundle node = bundle.getBundle(NODE);

            if (!node.isNull()) {
                spawned = node.getBoolean(SPAWNED);
                pendingDestroyMessage = node.getBoolean(PENDING_DESTROY);
                abilitySpellAbsorption = node.getBoolean(ABILITY_SPELL);
                abilityHardening = node.getBoolean(ABILITY_HARDENING);
                abilityCcImmune = node.getBoolean(ABILITY_CC);
                abilityIncreasedRange = node.getBoolean(ABILITY_RANGE);
                abilityRampUp = node.getBoolean(ABILITY_RAMP);
                abilityCharge = node.getBoolean(ABILITY_CHARGE);
                abilityCount = node.getInt(ABILITY_COUNT);
                spawnMsgShown = node.getBoolean(SPAWN_MSG);
            }
        }
    }
}

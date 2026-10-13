package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SealOfLight;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.GunWeapon;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndKnightSkill;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
public class KnightSKILL extends Buff implements ActionIndicator.Action {
    private float comboTime = 0f;
    private float initialComboTime = 5f;

    @Override
    public int icon() {
        return BuffIndicator.BLESS;
    }

    @Override
    public void tintIcon(Image icon) {
        KnightSKILL.ComboMove move = getHighestMove();
        if (move != null){
            icon.hardlight(move.tintColor & 0x00FFFFFF);
        } else {
            icon.resetColor();
        }
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (initialComboTime - comboTime)/ initialComboTime);
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    public void hit( Char enemy ) {
        comboTime = 5f;

        if (!enemy.isAlive() || (enemy.buff(Corruption.class) != null && enemy.HP == enemy.HT)){
            comboTime = Math.max(comboTime, 15*((Hero)target).pointsInTalent(Talent.CLEAVE));
        }

        initialComboTime = comboTime;

        if ((getHighestMove() != null)) {

            ActionIndicator.setAction( this );
        }

        BuffIndicator.refreshHero(); //refresh the buff visually on-hit
    }

    @Override
    public void detach() {
        super.detach();
        ActionIndicator.clearAction(this);
    }

    @Override
    public boolean act() {
        comboTime-=TICK;
        spend(TICK);
        if (comboTime <= 0) {
            detach();
        }
        return true;
    }

    @Override
    public String desc() {
        KnightSkillCombo counter = ((Hero)target).buff(KnightSkillCombo.class);
        int stacks = counter != null ? (int) counter.count() : 0;
        return Messages.get(this, "desc") + "\n\n" + Messages.get(this, "desc_stacks", stacks);
    }

    public KnightSKILL.ComboMove getHighestMove(){
        KnightSKILL.ComboMove best = null;
        for (KnightSKILL.ComboMove move : KnightSKILL.ComboMove.values()){
            best = move;
        }
        return best;
    }

    private static final String TIME  = "combotime";
    private static final String INITIAL_TIME  = "initialComboTime";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(TIME, comboTime);
        bundle.put(INITIAL_TIME, initialComboTime);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        comboTime = bundle.getFloat( TIME );

        //pre-0.9.2
        if (bundle.contains(INITIAL_TIME))  initialComboTime = bundle.getFloat( INITIAL_TIME );
        else                                initialComboTime = 5;

        if (getHighestMove() != null) ActionIndicator.setAction(this);
    }

    @Override
    public Image getIcon() {
        Image icon;
        if (((Hero)target).belongings.weapon() != null){
            icon = new ItemSprite(((Hero)target).belongings.weapon().image, null);
        } else {
            icon = new ItemSprite(new Item(){ {image = ItemSpriteSheet.WEAPON_HOLDER; }});
        }

        return icon;
    }

    @Override
    public void doAction() {
        GameScene.show(new WndKnightSkill(this));
    }

    public enum ComboMove {
        KNOCKBACK(0xFF00FF00),
        SMASH   (0xFFCCFF00),
        KILLBLOW  (0xFFFFFF00),
        LIGHTSWORD  (0xFFFFCC00);
        public int tintColor;

        ComboMove(int tintColor){
            this.tintColor = tintColor;
        }

        public String desc(){
            if (this == LIGHTSWORD) {
                int dmgper = 0;
                int bouns = 25;
                if (Dungeon.hero.hasTalent(Talent.SKILL_MASTERY)) bouns = 35;

                KnightSkillCombo counter = Dungeon.hero.buff(KnightSkillCombo.class);
                if (counter != null) {
                    dmgper += counter.count() * bouns;
                }

                return Messages.get(this, "lightsword_desc", dmgper, bouns * 10);
            }
            return Messages.get(this, name()+"_desc");

        }

    }

    public boolean canUseMove(KnightSKILL.ComboMove move){
        if (move == ComboMove.SMASH)   return true;
        return true;
    }

    public void useMove(KnightSKILL.ComboMove move){
            moveBeingUsed = move;
            GameScene.selectCell(listener);

    }


    public static class RiposteTracker extends Buff{
        { actPriority = VFX_PRIO;}

        public Char enemy;

        @Override
        public boolean act() {
            if (target.buff(KnightSKILL.class) != null) {
                target.sprite.attack(enemy.pos, new Callback() {
                    @Override
                    public void call() {
                        target.buff(KnightSKILL.class).doAttack(enemy);
                        next();
                    }
                });
                detach();
                return false;
            } else {
                detach();
                return true;
            }
        }
    }

    private static KnightSKILL.ComboMove moveBeingUsed;

    private void doAttack(final Char enemy){

        AttackIndicator.target(enemy);

        boolean wasAlly = enemy.alignment == target.alignment;
        Hero hero = (Hero)target;

        if (enemy.defenseSkill(target) >= Char.INFINITE_EVASION){
            enemy.sprite.showStatus( CharSprite.NEUTRAL, enemy.defenseVerb() );
            Sample.INSTANCE.play(Assets.Sounds.MISS);

        } else if (enemy.isInvulnerable(target.getClass())){
            enemy.sprite.showStatus( CharSprite.POSITIVE, Messages.get(Char.class, "invulnerable") );
            Sample.INSTANCE.play(Assets.Sounds.MISS);

        } else {

            int dmg = target.damageRoll();
            KnightSkillCombo counter = Buff.affect(Dungeon.hero, KnightSkillCombo.class);

            //variance in damage dealt
            switch (moveBeingUsed) {
                case KNOCKBACK:
                    dmg = 0;
                    break;
                case SMASH:
                    dmg = Math.round(dmg * 1.4f);
                    break;
                case KILLBLOW:
                    dmg = Math.round(dmg * 0.7f);
                    break;
                case LIGHTSWORD:
                    float countdamage = 0.25f;
                    if (hero.pointsInTalent(Talent.SKILL_MASTERY) >= 1) countdamage = 0.35f;
                    dmg = Math.round(dmg * (counter.count() *countdamage));
                    break;
            }

            dmg = enemy.defenseProc(target, dmg);
            dmg -= enemy.drRoll();

            if (enemy.buff(Vulnerable.class) != null) {
                dmg *= 1.33f;
            }

            dmg = target.attackProc(enemy, dmg);
            enemy.damage(dmg, target);

            //special effects
            switch (moveBeingUsed) {
                case KNOCKBACK:
                    hit(enemy);
                    if (enemy.isAlive()) {
                        //trace a ballistica to our target (which will also extend past them
                        Ballistica trajectory = new Ballistica(target.pos, enemy.pos, Ballistica.STOP_TARGET);
                        //trim it to just be the part that goes past them
                        trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size() - 1), Ballistica.PROJECTILE);
                        //knock them back along that ballistica, ensuring they don't fall into a pit
                        int dist = 1;
                        WandOfBlastWave.throwChar(enemy, trajectory, dist, true, true);

                        if (hero.pointsInTalent(Talent.SKILL_MASTERY) >= 3) {
                            Buff.affect(enemy, Silence.class, 2f);
                        }
                    }
                    if (counter.count() < 10) {
                        counter.countUp(1);
                        if (counter.count() == 10) GLog.p(Messages.get(KnightSKILL.class, "combo_max"));
                        else GLog.i(Messages.get(KnightSKILL.class, "combo_stack", (int)counter.count()));
                    }
                    break;
                case SMASH:
                    hit(enemy);
                    if (counter.count() < 10) {
                        counter.countUp(1);
                        if (counter.count() == 10) GLog.p(Messages.get(KnightSKILL.class, "combo_max"));
                        else GLog.i(Messages.get(KnightSKILL.class, "combo_stack", (int)counter.count()));
                    }
                    break;
                case KILLBLOW:
                    hit(enemy);
                    if (counter.count() < 10) {
                        counter.countUp(1);
                        if (counter.count() == 10) GLog.p(Messages.get(KnightSKILL.class, "combo_max"));
                        else GLog.i(Messages.get(KnightSKILL.class, "combo_stack", (int)counter.count()));
                    }
                    break;
                case LIGHTSWORD:
                    hit(enemy);
                        if (enemy.isAlive()) {
                            GameScene.flash( 0x80FFFFFF );
                            Sample.INSTANCE.play(Assets.Sounds.HIT_SWORD, 1.1f, 1.26f);

                            if (counter.count() == 10) Buff.affect(enemy, Paralysis.class, 2f);
                        }
                        Buff.detach(hero, KnightSkillCombo.class);
                    break;
                default:
                    //nothing
                    break;
            }

            if (((Hero) target).belongings.weapon instanceof GunWeapon && ((Hero) target).hasTalent(Talent.BLITZKRIEG)) Buff.affect(target, Haste.class, 2f);
            if (target.buff(FireImbue.class) != null)   target.buff(FireImbue.class).proc(enemy);
            if (target.buff(FrostImbue.class) != null)  target.buff(FrostImbue.class).proc(enemy);

            target.hitSound(Random.Float(0.87f, 1.15f));
            enemy.sprite.bloodBurstA(target.sprite.center(), dmg);
            enemy.sprite.flash();

            if (!enemy.isAlive()) {
                GLog.i(Messages.capitalize(Messages.get(Char.class, "defeat", enemy.name())));
                if(hero.hasTalent(Talent.INTO_FRAY)) {
                    Buff.affect(hero, Haste.class, 1 + hero.pointsInTalent(Talent.INTO_FRAY));
                }
            }

        }

        Invisibility.dispel();

        //Post-attack behaviour
        switch(moveBeingUsed){
            case KNOCKBACK:
                detach();
                ActionIndicator.clearAction(KnightSKILL.this);
                hero.spendAndNext(hero.attackDelay());
                break;

            case SMASH:
                detach();
                ActionIndicator.clearAction(KnightSKILL.this);
                hero.spendAndNext(hero.attackDelay());
                break;

            case KILLBLOW:
                if (!enemy.isAlive()) {
                    detach();
                    Buff.affect(hero, KnightSKILL.class).hit(enemy);
                    hero.spendAndNext(0);
                        SealOfLight Seal = hero.belongings.getItem(SealOfLight.class);
                        if (Seal != null && hero.pointsInTalent(Talent.SKILL_MASTERY) >= 2) {
                            Seal.charge(hero, 3);
                            Seal.updateQuickslot();
                            SpellSprite.show(hero, SpellSprite.CHARGE);
                    }
                }
                else {
                    detach();
                    ActionIndicator.clearAction(KnightSKILL.this);
                    hero.spendAndNext(hero.attackDelay());
                    break;
                }
                break;

            case LIGHTSWORD:
                detach();
                ActionIndicator.clearAction(KnightSKILL.this);
                hero.spendAndNext(hero.attackDelay());
                break;

            default:
                detach();
                ActionIndicator.clearAction(KnightSKILL.this);
                hero.spendAndNext(hero.attackDelay());
                break;
        }
        }

    private CellSelector.Listener listener = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer cell) {
            if (cell == null) return;
            final Char enemy = Actor.findChar( cell );
            if (enemy == null
                    || enemy == target
                    || !Dungeon.level.heroFOV[cell]
                    || target.isCharmedBy( enemy )) {
                GLog.w(Messages.get(Combo.class, "bad_target"));

            } else if (!((Hero)target).canAttack(enemy)){
                GLog.w(Messages.get(Combo.class, "bad_target"));
            }
            else {
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
            return Messages.get(Combo.class, "prompt");
        }
    };
}

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silence;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BreakerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.OriginiutantSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Originiutant extends Mob {
    {
        spriteClass = OriginiutantSprite.class;

        HP = HT = 800;
        defenseSkill = 12;

        EXP = 27;
        maxLvl = 39;

        baseSpeed = 0.5f;

        loot = new ScrollOfTransmutation();
        lootChance = 1f;

        immunities.add(Silence.class);
        immunities.add(AllyBuff.class);
        properties.add(Property.INFECTED);
    }

    @Override
    public boolean attack(Char enemy) {
        if (enemy == null) return false;

        boolean visibleFight = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[enemy.pos];

        if (enemy.isInvulnerable(getClass())) {

            if (visibleFight) {
                enemy.sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "invulnerable"));

                Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY, 1f, Random.Float(0.96f, 1.05f));
            }

            return false;
        }
        else if (hit( this, enemy, true )) {

            int dmg = damageRoll();
            enemy.damage( dmg, this );
            enemy.sprite.bloodBurstA( sprite.center(), dmg );
            enemy.sprite.flash();

            if (Dungeon.level.heroFOV[pos]) Sample.INSTANCE.play(Assets.Sounds.HIT_MAGIC, 1.35f, Random.Float(0.65f, 1.76f));

            if (enemy == Dungeon.hero && !enemy.isAlive()) {
                Dungeon.fail( getClass() );
            }
        } else {
            enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
        }

        if (!enemy.isAlive() && visibleFight) {
            if (enemy == Dungeon.hero) {

                Dungeon.fail( getClass() );
                GLog.n( Messages.capitalize(Messages.get(Char.class, "kill", name())) );
            }
        }

        return true;

    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 70, 90 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 44;
    }

    @Override
    public int drRoll() {
        return 0;
    }
}

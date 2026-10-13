package com.shatteredpixel.shatteredpixeldungeon.items.NewGameItem;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Certificate extends Item {
    private static final String TXT_VALUE	= "%+d";
    {
        image = ItemSpriteSheet.INFO_CERTI;
    }

    //needed for reflection (journal catalog, bundle restore)
    public Certificate() {
    }

    public Certificate( int value ) {
        this.quantity = value;
        if (Challenges.activeChallenges() > 7) this.quantity += 50;
        else if (Challenges.activeChallenges() > 5) this.quantity += 40;
        else if (Challenges.activeChallenges() > 2) this.quantity += 20;
        else if (Challenges.activeChallenges() > 0) this.quantity += 10;

        if (Dungeon.eazymode == 1) this.quantity = 0;
    }

    @Override
    public boolean doPickUp(Hero hero) {
        SPDSettings.addSpecialcoin(this.quantity);
        Catalog.setSeen(getClass());
        GameScene.pickUp( this, hero.pos );
        hero.sprite.showStatus( CharSprite.NEUTRAL, TXT_VALUE, quantity );
        hero.spendAndNext( TIME_TO_PICK_UP );

        Sample.INSTANCE.play( Assets.Sounds.EVOKE, 1, 1, Random.Float( 1.35f, 1.45f ) );
        Badges.validateCertificate();
        return true;
    }

    public static void specialEndingBouns() {
        if (Dungeon.eazymode != 1) {

            int bouns = 0;
            if (Challenges.activeChallenges() > 7) bouns += 50;
            else if (Challenges.activeChallenges() > 5) bouns += 15;
            else if (Challenges.activeChallenges() > 2) bouns += 10;
            else if (Challenges.activeChallenges() > 0) bouns += 5;
            SPDSettings.addSpecialcoin(10 + bouns);
        }
    }

    private static final String VALUE	= "value";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( VALUE, quantity );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        quantity = bundle.getInt( VALUE );
    }
}

/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.GreenCat;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.journal.quests.AmuletQuestLine;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.SurfaceScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Game;
import com.watabou.noosa.ui.Component;

public class WndKaltsit extends Window {

    protected static final int WIDTH_MIN    = 120;
    protected static final int WIDTH_MAX    = 220;
    private static final int BTN_HEIGHT = 20;
    private static final int GAP        = 4;

	public WndKaltsit(GreenCat kaltsit, Amulet doctor) {
        super();

        int width = WIDTH_MIN;

        Component titlebar = new IconTitle( kaltsit.sprite(), Messages.titleCase( kaltsit.name() ));
        titlebar.setRect( 0, 0, width, 0 );
        add(titlebar);

        RenderedTextBlock text = PixelScene.renderTextBlock( 6 );
        String message = Dungeon.doctorSaved
                ? Messages.get(kaltsit, "ask_again", Dungeon.hero.heroClass.title())
                : Messages.get(kaltsit, "ask", Dungeon.hero.heroClass.title());
        text.text( message, width );
        text.setPos( titlebar.left(), titlebar.bottom() + 2*GAP );
        add( text );

        while (PixelScene.landscape()
                && text.bottom() > (PixelScene.MIN_HEIGHT_L - 10)
                && width < WIDTH_MAX){
            width += 20;
            text.maxWidth(width);
        }

        RedButton btnContinue = new RedButton( Messages.get(Hero.class, "continue") ) {
            @Override
            protected void onClick() {
                continueRun(doctor);
            }
        };
        btnContinue.setRect( 0, text.top() + text.height() + GAP, width, BTN_HEIGHT );
        add( btnContinue );

        RedButton btnExit = new RedButton( Messages.get(Hero.class, "exit") ) {
            @Override
            protected void onClick() {
                completeRun();
            }
        };
        btnExit.setRect( 0, btnContinue.top() + btnContinue.height() + GAP, width, BTN_HEIGHT );
        add( btnExit );

        resize( width, (int)btnExit.bottom() );
	}

    private void completeRun() {
        Badges.silentValidateHappyEnd();
        if (Dungeon.isPray) Badges.validatepray();
        AmuletQuestLine.complete();
        Dungeon.win(Amulet.class);
        Dungeon.deleteGame(GamesInProgress.curSlot, true);
        Game.switchScene(SurfaceScene.class);
    }

    private void continueRun(Amulet doctor) {
        hide();

        Badges.silentValidateHappyEnd();
        if (Dungeon.isPray) Badges.validatepray();
        Dungeon.doctorSaved = true;
        AmuletQuestLine.dismiss();
        if (doctor != null) {
            doctor.detachAll(Dungeon.hero.belongings.backpack);
        }
    }
}

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.journal.quests.BonusStageQuestLine;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Game;

public class WndPilot extends Window {
    private static final int WIDTH = 120;
    private static final int BTN_HEIGHT = 20;
    private static final int GAP = 2;

    public WndPilot() {

        super();

        IconTitle titlebar = new IconTitle();
        if (Dungeon.extrastage_Gavial) {
            titlebar.icon(new ItemSprite(ItemSpriteSheet.GAVIAL, null));
            titlebar.label(Messages.get(this, "gavial_name"));
        } else if (Dungeon.extrastage_Sea) {
            titlebar.icon(new ItemSprite(ItemSpriteSheet.IBERIA, null));
            titlebar.label(Messages.get(this, "sea_name"));
        } else {
            titlebar.icon(new ItemSprite(ItemSpriteSheet.SIESTA, null));
            titlebar.label(Messages.get(this, "siesta_name"));
        }
        titlebar.setRect(0, 0, WIDTH, 0);
        add(titlebar);

        RenderedTextBlock message;
        if (Dungeon.extrastage_Gavial)
            message = PixelScene.renderTextBlock(Messages.get(this, "gavial"), 6);
        else if (Dungeon.extrastage_Sea)
            message = PixelScene.renderTextBlock(Messages.get(this, "sea"), 6);
        else message = PixelScene.renderTextBlock(Messages.get(this, "siesta"), 6);
        message.maxWidth(WIDTH);
        message.setPos(0, titlebar.bottom() + GAP);
        add(message);

        RedButton btnReward = new RedButton(Messages.get(this, "move")) {
            @Override
            protected void onClick() {
                move();
            }
        };
        btnReward.setRect(0, message.top() + message.height() + GAP, WIDTH, BTN_HEIGHT);
        add(btnReward);

        resize(WIDTH, (int) btnReward.bottom());
    }

    private void move() {
        hide();
        BonusStageQuestLine.start();
        InterlevelScene.curTransition = Dungeon.level.getTransition(LevelTransition.Type.BRANCH_EXIT);
        InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
        Game.switchScene(InterlevelScene.class);
    }
}

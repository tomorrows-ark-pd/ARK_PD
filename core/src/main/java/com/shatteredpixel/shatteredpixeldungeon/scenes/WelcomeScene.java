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

package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.Rankings;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.TomorrowRogueNight;
import com.shatteredpixel.shatteredpixeldungeon.effects.BannerSprites;
import com.shatteredpixel.shatteredpixeldungeon.journal.Journal;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndError;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndHardNotification;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.FileUtils;
import com.watabou.utils.RectF;

public class WelcomeScene extends PixelScene {

    private static final int LATEST_UPDATE = TomorrowRogueNight.v0_5_4;

    //used so that the game does not keep showing the window forever if cleaning fails
    private static boolean triedCleaningTemp = false;

    @Override
    public void create() {
        super.create();

        final int previousVersion = SPDSettings.version();

        if (!triedCleaningTemp && FileUtils.cleanTempFiles()) {
            add(new WndHardNotification(Icons.get(Icons.WARNING),
                    Messages.get(WndError.class, "title"),
                    Messages.get(this, "save_warning"),
                    Messages.get(this, "continue"),
                    5) {
                @Override
                public void hide() {
                    super.hide();
                    triedCleaningTemp = true;
                    TomorrowRogueNight.resetScene();
                }
            });
            return;
        }

        if (TomorrowRogueNight.versionCode == previousVersion && !SPDSettings.intro()) {
            TomorrowRogueNight.switchNoFade(TitleScene.class);
            return;
        }

        uiCamera.visible = false;

        int w = Camera.main.width;
        int h = Camera.main.height;
        RectF insets = getCommonInsets();

        w -= insets.left + insets.right;
        h -= insets.top + insets.bottom;

        Image title = BannerSprites.get(BannerSprites.Type.PIXEL_DUNGEON);
        title.brightness(0.6f);
        add(title);

        float topRegion = Math.max(title.height - 6, h * 0.45f);

        title.x = insets.left + (w - title.width()) / 2f;
        title.y = insets.top + 2 + (topRegion - title.height()) / 2f;

        align(title);

        Image signs = new Image(BannerSprites.get(BannerSprites.Type.PIXEL_DUNGEON_SIGNS)) {
            private float time = 0;

            @Override
            public void update() {
                super.update();
                am = Math.max(0f, (float) Math.sin(time += Game.elapsed));
                if (time >= 1.5f * Math.PI) time = 0;
            }

            @Override
            public void draw() {
                Blending.setLightMode();
                super.draw();
                Blending.setNormalMode();
            }
        };
        signs.x = title.x + (title.width() - signs.width()) / 2f;
        signs.y = title.y;
        add(signs);

        StyledButton okay = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this, "continue")) {
            @Override
            protected void onClick() {
                super.onClick();
                if (previousVersion == 0 || SPDSettings.intro()) {

                    if (previousVersion > 0) {
                        updateVersion(previousVersion);
                    }

                    SPDSettings.version(TomorrowRogueNight.versionCode);
                    GamesInProgress.selectedClass = null;
                    GamesInProgress.curSlot = GamesInProgress.firstEmpty();
                    if (GamesInProgress.curSlot == -1 || Rankings.INSTANCE.totalNumber > 0) {
                        SPDSettings.intro(false);
                        TomorrowRogueNight.switchScene(TitleScene.class);
                    } else {
                        TomorrowRogueNight.switchScene(HeroSelectScene.class);
                    }
                } else {
                    updateVersion(previousVersion);
                    TomorrowRogueNight.switchScene(TitleScene.class);
                }
            }
        };

        float buttonY = insets.top + Math.min(topRegion + (PixelScene.landscape() ? 60 : 120), h - 24);

        float buttonAreaWidth = landscape() ? PixelScene.MIN_WIDTH_L - 6 : PixelScene.MIN_WIDTH_P - 2;
        float btnAreaLeft = insets.left + (w - buttonAreaWidth) / 2f;

        if (previousVersion != 0 && !SPDSettings.intro()) {
            StyledButton changes = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(TitleScene.class, "changes")) {
                @Override
                protected void onClick() {
                    super.onClick();
                    updateVersion(previousVersion);
                    TomorrowRogueNight.switchScene(ChangesScene.class);
                }
            };
            okay.setRect(btnAreaLeft, buttonY, (buttonAreaWidth / 2) - 1, 20);
            add(okay);

            changes.setRect(okay.right() + 1, buttonY, okay.width(), 20);
            changes.icon(Icons.get(Icons.CHANGES));
            add(changes);
        } else {
            okay.text(Messages.get(TitleScene.class, "enter"));
            okay.setRect(btnAreaLeft, buttonY, buttonAreaWidth, 20);
            okay.icon(Icons.get(Icons.ENTER));
            add(okay);
        }

        RenderedTextBlock text = PixelScene.renderTextBlock(6);
        String message;
        if (previousVersion == 0 || SPDSettings.intro()) {
            message = Messages.get(this, "welcome_msg");
        } else if (previousVersion <= TomorrowRogueNight.versionCode) {
            if (previousVersion < LATEST_UPDATE) {
                message = Messages.get(this, "update_intro");
                message += "\n\n" + Messages.get(this, "update_msg");
            } else {
                //TODO: change the messages here in accordance with the type of patch.
                message = Messages.get(this, "patch_intro");
                message += "\n";
                //message += "\n" + Messages.get(this, "patch_balance");
                message += "\n" + Messages.get(this, "patch_bugfixes");
                message += "\n" + Messages.get(this, "patch_translations");

            }
        } else {
            message = Messages.get(this, "what_msg");
        }

        text.text(message, Math.min(w - 20, 300));
        float titleBottom = title.y + title.height();
        float textSpace = okay.top() - titleBottom - 4;
        text.setPos(insets.left + (w - text.width()) / 2f, (titleBottom + 2) + (textSpace - text.height()) / 2);
        add(text);

    }

    private void updateVersion(int previousVersion) {

        //update rankings, to update any data which may be outdated
        if (previousVersion < LATEST_UPDATE) {

            Badges.loadGlobal();
            Journal.loadGlobal();

            int highestChalInRankings = 0;

            try {
                Rankings.INSTANCE.load();
                for (Rankings.Record rec : Rankings.INSTANCE.records.toArray(new Rankings.Record[0])) {
                    try {
                        Rankings.INSTANCE.loadGameData(rec);
                        Rankings.INSTANCE.saveGameData(rec);
                    } catch (Exception e) {
                        //if we encounter a fatal per-record error, then clear that record's data
                        rec.gameData = null;
                        Game.reportException(new RuntimeException("Rankings Updating Failed!", e));
                    }
                }
                Rankings.INSTANCE.save();
            } catch (Exception e) {
                //if we encounter a fatal error, then just clear the rankings
                FileUtils.deleteFile(Rankings.RANKINGS_FILE);
                Game.reportException(new RuntimeException("Rankings Updating Failed!", e));
            }

            Badges.saveGlobal(true);
            Journal.saveGlobal();
        }

        SPDSettings.version(TomorrowRogueNight.versionCode);
    }

}

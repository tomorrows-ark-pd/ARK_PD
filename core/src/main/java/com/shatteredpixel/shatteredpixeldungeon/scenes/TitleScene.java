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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.TomorrowRogueNight;
import com.shatteredpixel.shatteredpixeldungeon.effects.BannerSprites;
import com.shatteredpixel.shatteredpixeldungeon.effects.Fireball;
import com.shatteredpixel.shatteredpixeldungeon.messages.Languages;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.services.news.News;
import com.shatteredpixel.shatteredpixeldungeon.services.updates.AvailableUpdateData;
import com.shatteredpixel.shatteredpixeldungeon.services.updates.Updates;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndSettings;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.ColorMath;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.RectF;

import java.util.Date;

public class TitleScene extends PixelScene {
	
	@Override
	public void create() {
		
		super.create();

		Music.INSTANCE.play( Assets.Music.GAME6_IBERIA1, true );

		uiCamera.visible = false;
		
		int w = Camera.main.width;
		int h = Camera.main.height;

		RectF insets = getCommonInsets();

		w -= insets.left + insets.right;
		h -= insets.top + insets.bottom;
		
		Archs archs = new Archs();
		archs.setSize( w, h );
		add( archs );
		
		Image title = BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON );
		add( title );

		float topRegion = Math.max(title.height - 6, h*0.45f);

		title.x = insets.left + (w - title.width()) / 2f;
		title.y = insets.top + 2 + (topRegion - title.height()) / 2f;

		align(title);

		placeTorch(title.x + 22, title.y + 46);
		placeTorch(title.x + title.width - 22, title.y + 46);

		Image signs = new Image( BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON_SIGNS ) ) {
			private float time = 0;
			@Override
			public void update() {
				super.update();
				am = Math.max(0f, (float)Math.sin( time += Game.elapsed ));
				if (time >= 1.5f*Math.PI) time = 0;
			}
			@Override
			public void draw() {
				Blending.setLightMode();
				super.draw();
				Blending.setNormalMode();
			}
		};
		signs.x = title.x + (title.width() - signs.width())/2f;
		signs.y = title.y;
		add( signs );

		final Chrome.Type GREY_TR = Chrome.Type.GREY_BUTTON_TR;
		
		StyledButton btnPlay = new StyledButton(GREY_TR, Messages.get(this, "enter")){
			@Override
			protected void onClick() {
				if (GamesInProgress.checkAll().size() == 0){
					GamesInProgress.selectedClass = null;
					GamesInProgress.curSlot = 1;
					TomorrowRogueNight.switchScene(HeroSelectScene.class);
				} else {
					TomorrowRogueNight.switchNoFade( StartScene.class );
				}
			}
			
			@Override
			protected boolean onLongClick() {
				//making it easier to start runs quickly while debugging
				if (DeviceCompat.isDebug()) {
					GamesInProgress.selectedClass = null;
					GamesInProgress.curSlot = 1;
					TomorrowRogueNight.switchScene(HeroSelectScene.class);
					return true;
				}
				return super.onLongClick();
			}
		};
		btnPlay.icon(Icons.get(Icons.ENTER));
		add(btnPlay);

		StyledButton btnSupport = new SupportButton(GREY_TR, Messages.get(this, "support"));
		add(btnSupport);

		StyledButton btnRankings = new StyledButton(GREY_TR,Messages.get(this, "rankings")){
			@Override
			protected void onClick() {
				TomorrowRogueNight.switchNoFade( RankingsScene.class );
			}
		};
		btnRankings.icon(Icons.get(Icons.RANKINGS));
		add(btnRankings);

		StyledButton btnBadges = new StyledButton(GREY_TR, Messages.get(this, "badges")){
			@Override
			protected void onClick() {
				TomorrowRogueNight.switchNoFade( BadgesScene.class );
			}
		};
		btnBadges.icon(Icons.get(Icons.BADGES));
		add(btnBadges);

		StyledButton btnNews = new NewsButton(GREY_TR, Messages.get(this, "news"));
		btnNews.icon(Icons.get(Icons.NEWS));
		add(btnNews);

		StyledButton btnChanges = new ChangesButton(GREY_TR, Messages.get(this, "changes"));
		btnChanges.icon(Icons.get(Icons.CHANGES));
		add(btnChanges);

		StyledButton btnSettings = new SettingsButton(GREY_TR, Messages.get(this, "settings"));
		add(btnSettings);

		StyledButton btnAbout = new StyledButton(GREY_TR, Messages.get(this, "about")){
			@Override
			protected void onClick() {
				TomorrowRogueNight.switchScene( AboutScene.class );
			}
		};
		btnAbout.icon(Icons.get(Icons.ARKPD));
		add(btnAbout);
		
		final int BTN_HEIGHT = 20;
		int GAP = (int)(h - topRegion - (landscape() ? 3 : 4)*BTN_HEIGHT)/3;
		GAP /= landscape() ? 3 : 5;
		GAP = Math.max(GAP, 2);

		float buttonAreaWidth = landscape() ? PixelScene.MIN_WIDTH_L-6 : PixelScene.MIN_WIDTH_P-2;
		float btnAreaLeft = insets.left + (w - buttonAreaWidth) / 2f;
		if (landscape()) {
			btnPlay.setRect(btnAreaLeft, insets.top + topRegion+GAP, (buttonAreaWidth/2)-1, BTN_HEIGHT);
			align(btnPlay);
			btnSupport.setRect(btnPlay.right()+2, btnPlay.top(), btnPlay.width(), BTN_HEIGHT);
			btnRankings.setRect(btnPlay.left(), btnPlay.bottom()+ GAP, (float) (Math.floor(buttonAreaWidth/3f)-1), BTN_HEIGHT);
			btnBadges.setRect(btnRankings.right()+2, btnRankings.top(), btnRankings.width(), BTN_HEIGHT);
			btnNews.setRect(btnBadges.right()+2, btnBadges.top(), btnRankings.width(), BTN_HEIGHT);
			btnSettings.setRect(btnRankings.left(), btnRankings.bottom() + GAP, btnRankings.width(), BTN_HEIGHT);
			btnChanges.setRect(btnSettings.right()+2, btnSettings.top(), btnRankings.width(), BTN_HEIGHT);
			btnAbout.setRect(btnChanges.right()+2, btnSettings.top(), btnRankings.width(), BTN_HEIGHT);
		} else {
			btnPlay.setRect(btnAreaLeft, insets.top + topRegion+GAP, buttonAreaWidth, BTN_HEIGHT);
			align(btnPlay);
			btnSupport.setRect(btnPlay.left(), btnPlay.bottom()+ GAP, btnPlay.width(), BTN_HEIGHT);
			btnRankings.setRect(btnPlay.left(), btnSupport.bottom()+ GAP, (btnPlay.width()/2)-1, BTN_HEIGHT);
			btnBadges.setRect(btnRankings.right()+2, btnRankings.top(), btnRankings.width(), BTN_HEIGHT);
			btnNews.setRect(btnRankings.left(), btnRankings.bottom()+ GAP, btnRankings.width(), BTN_HEIGHT);
			btnChanges.setRect(btnNews.right()+2, btnNews.top(), btnNews.width(), BTN_HEIGHT);
			btnSettings.setRect(btnNews.left(), btnNews.bottom()+GAP, btnRankings.width(), BTN_HEIGHT);
			btnAbout.setRect(btnSettings.right()+2, btnSettings.top(), btnSettings.width(), BTN_HEIGHT);
		}

		BitmapText version = new BitmapText( "v" + Game.version, pixelFont);
		version.measure();
		version.hardlight( 0x888888 );
		version.x = insets.left + w - version.width() - (DeviceCompat.isDesktop() ? 4 : 8);
		version.y = insets.top + h - version.height() - (DeviceCompat.isDesktop() ? 2 : 4);
		add( version );

		fadeIn();
	}
	
	private void placeTorch( float x, float y ) {
		Fireball fb = new Fireball();
		fb.setPos( x, y );
		add( fb );
	}

	private static class NewsButton extends StyledButton {

		public NewsButton(Chrome.Type type, String label ){
			super(type, label);
			if (SPDSettings.news()) News.checkForNews();
		}

		@Override
		protected void onClick() {
			super.onClick();
			TomorrowRogueNight.switchNoFade( NewsScene.class );
		}
	}

	private static class ChangesButton extends StyledButton {

		public ChangesButton(Chrome.Type type, String label) {
			super(type, label);
			if (SPDSettings.updates()) Updates.checkForUpdate();
		}

		boolean updateShown = false;

		@Override
		public void update() {
			super.update();

			if (!updateShown && (Updates.updateAvailable() || Updates.isInstallable())) {
				updateShown = true;
				if (Updates.isInstallable()) text(Messages.get(TitleScene.class, "install"));
				else text(Messages.get(TitleScene.class, "update"));
			}

			if (updateShown) {
				textColor(ColorMath.interpolate(0xFFFFFF, Window.ARKPD_COLOR, 0.5f + (float) Math.sin(Game.timeTotal * 5) / 2f));
			}
		}

		@Override
		protected void onClick() {
			TomorrowRogueNight.scene().addToFront(new WndOptions(
					Messages.get(this, "versioned_title") + "ver0.5.0-B4",
					Messages.get(this, "desc"),
					Messages.get(this, "update"),
					Messages.get(this, "changes")
			) {
				@Override
				protected void onSelect(int index) {
					if (index == 0) {
						String link = "https://mizq4482.tistory.com/";
						DeviceCompat.openURI(link);
					} else if (index == 1) {
						ChangesScene.changesSelected = 0;
						TomorrowRogueNight.switchNoFade(ChangesScene.class);
					}
				}
			});

		}
	}

	private static class SettingsButton extends StyledButton {

		public SettingsButton( Chrome.Type type, String label ){
			super(type, label);
			if (Messages.lang().status() == Languages.Status.INCOMPLETE){
				icon(Icons.get(Icons.LANGS));
				icon.hardlight(1.5f, 0, 0);
			} else {
				icon(Icons.get(Icons.PREFS));
			}
		}

		@Override
		public void update() {
			super.update();

			if (Messages.lang().status() == Languages.Status.INCOMPLETE){
				textColor(ColorMath.interpolate( 0xFFFFFF, CharSprite.NEGATIVE, 0.5f + (float)Math.sin(Game.timeTotal*5)/2f));
			}
		}

		@Override
		protected void onClick() {
			if (Messages.lang().status() == Languages.Status.INCOMPLETE){
				WndSettings.last_index = 4;
			}
			TomorrowRogueNight.scene().add(new WndSettings());
		}
	}

	private static class SupportButton extends StyledButton{

		public SupportButton( Chrome.Type type, String label ){
			super(type, label);
			icon(Icons.get(Icons.GOLD));
			textColor(Window.TITLE_COLOR);
		}

		@Override
		protected void onClick() {
			TomorrowRogueNight.switchNoFade(SupporterScene.class);
		}
	}
}
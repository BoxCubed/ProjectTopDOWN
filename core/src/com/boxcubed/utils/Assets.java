package com.boxcubed.utils;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader.ParticleEffectParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.boxcubed.utils.AnimationLoader.AnimationPrefs;

public class Assets extends com.badlogic.gdx.assets.AssetManager {
	public static final String nineMMGunSOUND = "sounds/9_mm_gunshot.mp3", gunSOUND = "sounds/gunshot.mp3",
			ZAttackSOUND = "sounds/zombie_attack.mp3", ZGroanSOUND = "sounds/zombie_groan.wav",
			ambientMUSIC = "sounds/ambient_music.mp3", ZScreamsSOUND = "sounds/zombie_screams.mp3",buttonChangeSOUND="sounds/button-choose.wav",

			MainMAP = "maps/map2.tmx",

			scrollMenuIMAGE = "img/menu.jpg", bulletIMAGE = "img/bullet.png", crossHairIMAGE = "img/crosshair.png",
			logoIMAGE = "img/logo.png", healthIMAGE = "img/health.png", mflashIMAGE = "img/muzzle_flash.png",
			staminaIMAGE = "img/stamina.png", particleIMAGE = "img/particle.png", playerIMAGE = "img/player.png",
			zombieIMAGE = "img/skeleton-idle_0.png", playerRIFLE = "img/survivor-idle_rifle.png",
			inventoryAK47 = "img/invbar.png", inventoryPISTOL = "img/invbarpistol.png",ak47ActiveIMAGE="img/invbar_active.png",
			pistolActiveIMAGE="img/invbarpistol_active.png",
			
			playerATLAS = "spritesheets/playersheet.atlas", legATLAS = "spritesheets/leganim.atlas",
			zombieAttackATLAS = "spritesheets/zombieanim.atlas", zombieWalkATLAS = "spritesheets/zombie_walk.atlas",
			startATLAS = "button/start.atlas", rifleWalkATLAS = "spritesheets/rifle_walk.atlas",

			menuFONT = "fonts/menuFont.ttf",

			visSKIN = "button/skins/visUI/uiskin.json",starSKIN="button/skins/starSoldier/star-soldier-ui.json",neutSKIN="button/skins/neutralizer/neutralizer-ui.json",

			flameEFFECT = "maps/effects/flame.p", bloodEFFECT = "maps/effects/blood.p"

	;

	public Assets() {
		super();
		setLoader(Animation.class, new AnimationLoader(getFileHandleResolver()));
		setLoader(TiledMap.class, new TmxMapLoader());
		setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(getFileHandleResolver()));

		setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(new InternalFileHandleResolver()));

		loadImages();
		loadFonts();
		loadMaps();
		loadAtlases();
		loadEffects();
		loadSkins();
		loadSounds();
		loadAnimations();

	}

	private void loadSkins() {
		load(visSKIN, Skin.class);
		load(neutSKIN, Skin.class);
		load(starSKIN, Skin.class);

	}

	private void loadEffects() {
		ParticleEffectParameter param = new ParticleEffectParameter();
		// param.imagesDir=Gdx.files.internal("maps/effects");
		load(flameEFFECT, ParticleEffect.class, param);
		load(bloodEFFECT, ParticleEffect.class, param);

	}

	private void loadFonts() {
		FreeTypeFontLoaderParameter menuFont = new FreeTypeFontLoaderParameter();
		menuFont.fontFileName = menuFONT;

		menuFont.fontParameters.size = 50;

		load(menuFONT, BitmapFont.class, menuFont);

	}
 
	private void loadMaps() {
		load("maps/map2.tmx", TiledMap.class);
		
	}

	private void loadSounds() {
		load(nineMMGunSOUND, Sound.class);
		load(gunSOUND, Sound.class);
		load(ZAttackSOUND, Sound.class);
		load(ZGroanSOUND, Sound.class);
		load(ambientMUSIC, Music.class);
		load(ZScreamsSOUND, Sound.class);
		load(buttonChangeSOUND, Sound.class);

	}

	private void loadImages() {
		load(logoIMAGE, Texture.class);
		load(scrollMenuIMAGE, Texture.class);
		load(bulletIMAGE, Texture.class);
		load(crossHairIMAGE, Texture.class);
		load(healthIMAGE, Texture.class);
		load(mflashIMAGE, Texture.class);
		load(particleIMAGE, Texture.class);
		load(playerIMAGE, Texture.class);
		load(zombieIMAGE, Texture.class);
		load(staminaIMAGE, Texture.class);
		load(playerRIFLE, Texture.class);
		load(inventoryAK47, Texture.class);
		load(inventoryPISTOL, Texture.class);
		load(ak47ActiveIMAGE, Texture.class);
		load(pistolActiveIMAGE, Texture.class);
	}

	private void loadAtlases() {
		load(playerATLAS, TextureAtlas.class);
		load(zombieAttackATLAS, TextureAtlas.class);
		load(legATLAS, TextureAtlas.class);
		load(zombieWalkATLAS, TextureAtlas.class);
		load(startATLAS, TextureAtlas.class);
		load(rifleWalkATLAS, TextureAtlas.class);
	}

	@SuppressWarnings("rawtypes")
	private void loadAnimations() {
		load(new AssetDescriptor<Animation>(playerATLAS + ":anim", Animation.class));

		load(new AssetDescriptor<Animation>(legATLAS + ":anim", Animation.class));

		load(new AssetDescriptor<Animation>(zombieAttackATLAS + ":anim", Animation.class));
		AnimationPrefs prefs = new AnimationPrefs();
		prefs.duration = 1 / 30f;
		load(new AssetDescriptor<Animation>(zombieWalkATLAS + ":anim", Animation.class, prefs));
		load(new AssetDescriptor<Animation>(rifleWalkATLAS + ":anim", Animation.class));

	}

}

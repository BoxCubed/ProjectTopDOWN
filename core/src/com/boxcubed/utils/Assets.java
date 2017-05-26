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
	private static final String nineMMGunSOUND = "sounds/9_mm_gunshot.mp3";
	public static final String gunSOUND = "sounds/gunshot.mp3";
	public static final String ZAttackSOUND = "sounds/zombie_attack.mp3";
	private static final String ZGroanSOUND = "sounds/zombie_groan.ogg";
	public static final String ambientMUSIC = "sounds/ambient_music.mp3";
	public static final String ZScreamsSOUND = "sounds/zombie_screams.mp3";
	public static final String buttonChangeSOUND="sounds/button-choose.ogg";
	public static final String menuMUSIC="sounds/Drafty-Places.mp3";

	public static final String MainMAP = "maps/map2.tmx";

	public static final String scrollMenuIMAGE = "img/menu.jpg";
	public static final String bulletIMAGE = "img/bullet.png";
	public static final String crossHairIMAGE = "img/crosshair.png";
	public static final String logoIMAGE = "img/logo.png";
	public static final String healthIMAGE = "img/health.png";
	public static final String mflashIMAGE = "img/muzzle_flash.png";
	public static final String staminaIMAGE = "img/stamina.png";
	private static final String particleIMAGE = "img/particle.png";
	public static final String playerIMAGE = "img/player.png";
	public static final String zombieIMAGE = "img/skeleton-idle_0.png";
	private static final String playerRIFLE = "img/survivor-idle_rifle.png";
	public static final String inventoryAK47 = "img/invbar.png";
	public static final String inventoryPISTOL = "img/invbarpistol.png";
	public static final String ak47ActiveIMAGE="img/invbar_active.png";
	public static final String pistolActiveIMAGE="img/invbarpistol_active.png";

	public static final String playerATLAS = "spritesheets/playersheet.atlas";
	public static final String legATLAS = "spritesheets/leganim.atlas";
	public static final String zombieAttackATLAS = "spritesheets/zombieanim.atlas";
	public static final String zombieWalkATLAS = "spritesheets/zombie_walk.atlas";
	private static final String startATLAS = "button/start.atlas";
	public static final String rifleWalkATLAS = "spritesheets/rifle_walk.atlas";

	private static final String menuFONT = "fonts/menuFont.ttf";

	private static final String visSKIN = "button/skins/visUI/uiskin.json";
	public static final String starSKIN="button/skins/starSoldier/star-soldier-ui.json";
	public static final String neutSKIN="button/skins/neutralizer/neutralizer-ui.json";

	public static final String flameEFFECT = "maps/effects/flame.p";
	public static final String bloodEFFECT = "maps/effects/blood.p"

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
		load(menuMUSIC,Music.class);
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

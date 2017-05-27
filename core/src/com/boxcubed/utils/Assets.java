package com.boxcubed.utils;

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

import java.lang.reflect.Field;

public class Assets extends com.badlogic.gdx.assets.AssetManager {
    //TODO Move to a txt file
    public static final String nineMMGun_SOUND = "sounds/9_mm_gunshot.mp3", gun_SOUND = "sounds/gunshot.mp3",
            ZAttack_SOUND = "sounds/zombie_attack.mp3", //ZGroan_SOUND = "sounds/zombie_groan.wav",
            ambient_MUSIC = "sounds/ambient_music.mp3", ZScreams_SOUND = "sounds/zombie_screams.mp3",buttonChange_SOUND="sounds/button-choose.ogg",
            menu_MUSIC="sounds/Drafty-Places.mp3",

            Main_MAP = "maps/map2.tmx",

            scrollMenu_IMAGE = "img/menu.jpg", bullet_IMAGE = "img/bullet.png", crossHair_IMAGE = "img/crosshair.png",
            logo_IMAGE = "img/logo.png", health_IMAGE = "img/health.png", mflash_IMAGE = "img/muzzle_flash.png",
            stamina_IMAGE = "img/stamina.png", particle_IMAGE = "img/particle.png", player_IMAGE = "img/player.png",
            zombie_IMAGE = "img/skeleton-idle_0.png", playerRifle_IMAGE = "img/survivor-idle_rifle.png",
            inventoryAK47_IMAGE = "img/invbar.png", inventoryPistol_IMAGE = "img/invbarpistol.png",ak47Active_IMAGE="img/invbar_active.png",
            pistolActive_IMAGE="img/invbarpistol_active.png",

            player_ATLAS = "spritesheets/playersheet.atlas", leg_ATLAS = "spritesheets/leganim.atlas",
            zombieAttack_ATLAS = "spritesheets/zombieanim.atlas", zombieWalk_ATLAS_30 = "spritesheets/zombie_walk.atlas",
            /*start_ATLAS = "button/start.atlas",*/ rifleWalk_ATLAS = "spritesheets/rifle_walk.atlas",

            //menu_FONT = "fonts/menuFont.ttf",

            vis_SKIN = "button/skins/visUI/uiskin.json",star_SKIN="button/skins/starSoldier/star-soldier-ui.json",neut_SKIN="button/skins/neutralizer/neutralizer-ui.json",

            flame_EFFECT = "maps/effects/flame.p", blood_EFFECT = "maps/effects/blood.p"

            ;

    public Assets() {
        super();
        setLoader(Animation.class, new AnimationLoader(getFileHandleResolver()));
        setLoader(TiledMap.class, new TmxMapLoader());
        setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(getFileHandleResolver()));

        setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(new InternalFileHandleResolver()));
        final Field[] fieldsArray=Assets.class.getFields();
        loadAll(fieldsArray);
    }






    private void loadAll(Field... fields){
        load(logo_IMAGE,Texture.class);

        for(Field field:fields){
            String name=field.getName();
            if(name.contains("_")&&!name.equals("logo_IMAGE")){

                String load="";
                String[] array=name.split("_");
                String type=array[1];
                try {
                    load = (String)field.get(null);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                switch(type){
                    case "IMAGE":
                        load(load,Texture.class);
                        break;
                    case "SOUND":
                        load(load, Sound.class);
                        break;
                    case "ATLAS":
                        load(load, TextureAtlas.class);
                        if(array.length==2)
                            load(load+":anim", Animation.class);
                        else{
                            AnimationPrefs prefs = new AnimationPrefs();
                            prefs.duration = 1f / Float.parseFloat(array[2]);
                            load(load+":anim", Animation.class,prefs);
                        }
                        break;
                    case "MUSIC":
                        load(load, Music.class);
                        break;
                    case "MAP":
                        load(load,TiledMap.class);
                        break;
                    case "SKIN":
                        load(load, Skin.class);
                        break;
                    case "FONT":
                        FreeTypeFontLoaderParameter menuFont = new FreeTypeFontLoaderParameter();
                        menuFont.fontFileName = load;

                        menuFont.fontParameters.size = 50;
                        load(load,BitmapFont.class,menuFont);
                        break;
                    case "EFFECT":
                        load(load,ParticleEffect.class);
                        break;


                }
            }
        }
    }



}


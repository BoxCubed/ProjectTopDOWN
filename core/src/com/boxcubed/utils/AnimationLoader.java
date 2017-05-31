package com.boxcubed.utils;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.boxcubed.utils.AnimationLoader.AnimationPrefs;

/**
 * A basic Animation loader
 *
 * @param <T> The type of animation to return
 * @author ryan9
 */
@SuppressWarnings("rawtypes")
public class AnimationLoader extends SynchronousAssetLoader<Animation, AnimationPrefs> {


    public AnimationLoader(FileHandleResolver resolver) {
        super(resolver);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Animation load(AssetManager manager, String fileName, FileHandle file, AnimationPrefs parameter) {
        fileName = fileName.replaceAll(":anim", "");
        if (parameter != null)
            return new Animation<>(parameter.duration, manager.get(fileName, TextureAtlas.class).getRegions(), parameter.mode);
        else {

            return new Animation<>(1f / 30f * 100, manager.get(fileName, TextureAtlas.class).getRegions());
        }


    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, AnimationPrefs param) {
        Array<AssetDescriptor> deps = null;
        if (param != null && param.frames != null) {
            deps = new Array();
            deps.add(new AssetDescriptor<TextureAtlas>(param.frames, TextureAtlas.class));
        }
        return deps;
    }

    public static class AnimationPrefs extends AssetLoaderParameters<Animation> {
        String frames = null;
        float duration = 1f / 30f * 100;
        PlayMode mode = PlayMode.NORMAL;

        /**
         * Utility Method to return new instance without memory attachment
         *
         * @return
         */
        public AnimationPrefs cpy() {
            AnimationPrefs cpy = new AnimationPrefs();
            cpy.frames = frames;
            cpy.duration = duration;
            cpy.mode = mode;
            return cpy;
        }


    }

}

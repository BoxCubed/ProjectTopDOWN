package me.boxcubed.main;

import com.badlogic.gdx.Game;
import com.boxcubed.utils.Assets;
import com.boxcubed.utils.BoxoUtil;
import de.tomgrill.gdxdialogs.core.GDXDialogs;
import de.tomgrill.gdxdialogs.core.GDXDialogsSystem;
import me.boxcubed.main.States.GameState;
import me.boxcubed.main.States.SplashState;

public class TopDown extends Game {
    public static TopDown instance;
    public static boolean debug;
    public static Assets assets;
    public static GDXDialogs dialogs;

    public TopDown(boolean debug) {
        TopDown.debug = debug;
        // TODO Move every single asset into this
        assets = new Assets();

    }

    @Override
    public void create() {
        instance = this;
        BoxoUtil.reset();
        dialogs = GDXDialogsSystem.install();
        if (debug) {
            assets.finishLoading();
            setScreen(new GameState());
            GameState.instance.noTime = true;
            GameState.instance.noZombie = true;
        } else
            setScreen(new SplashState());
        // new ClientServerTest(2);
    }

    @Override
    public void render() {
        BoxoUtil.clearScreen();

        super.render();
        BoxoUtil.update();
    }

    @Override
    public void dispose() {
        super.dispose();
        assets.dispose();
    }
}

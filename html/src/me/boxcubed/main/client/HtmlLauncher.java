package me.boxcubed.main.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import me.boxcubed.main.TopDown;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(480, 320);
        }

        @Override
        public ApplicationListener createApplicationListener () {
<<<<<<< HEAD
                return new TopDown(true);
=======
                return new TopDown(false);
>>>>>>> Ballistic-ServerBranch
        }
}
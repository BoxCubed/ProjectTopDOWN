package net.dermetfan.someLibgdxTests;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import net.dermetfan.gdx.assets.AnnotationAssetManager;
import net.dermetfan.gdx.assets.AnnotationAssetManager.Asset;

public abstract class Assets {

	public static final AnnotationAssetManager manager = new AnnotationAssetManager();

	@Asset(Texture.class)
	public static final String ball = "assets/img/ball.png", someImage = "assets/img/someImage.png", japanischeFlagge = "assets/img/japanische Flagge.jpg", bruteWithTcng = "assets/img/bruteWithTcng.png", drop = "assets/img/drop.png", luigiFront = "assets/img/luigiFront.png", luigiSide = "assets/img/luigiSide.png", player = "assets/img/player.png", animationTest = "assets/img/animationTest.png", tank = "assets/img/tank.png", tankCannon = "assets/img/tankCannon.png", testImage = "assets/img/testImage.png", rockImg = "assets/img/rock.jpg";

	@Asset(PolygonRegion.class)
	public static final String rock = "assets/img/rock.psh";

	@Asset(Skin.class)
	public static final String uiskin = "assets/ui/uiskin.json";

	public static final String map1 = "assets/maps/map.tmx", isometricMap1 = "assets/maps/isometric.tmx", testMap1 = "assets/maps/test.tmx", testMap2 = "assets/maps/test2.tmx", box2DMapObjectParserIsometricMap = "assets/maps/Box2DMapObjectParserIsometric.tmx", box2DMapObjectParserStaggeredMap = "assets/maps/Box2DMapObjectParserIsometricStaggered.tmx", aStarTestMap = "assets/maps/AStarTestMap.tmx", box2DMapObjectParserTutorial = "assets/maps/Box2DMapObjectParserTutorial.tmx", box2DMapObjectParserTutorialTest = "assets/maps/Box2DMapObjectParserTutorialTest.tmx", box2DMapObjectParserTutorialTry1 = "assets/maps/Box2DMapObjectParserTutorialTry1.tmx", amiga4everPro2 = "assets/ui/amiga4ever pro2.ttf", explosion = "assets/effects/explosion.p", coloredFlame = "assets/effects/coloredFlame.p", andreBirthdayMap = "assets/maps/andreBirthayMap.tmx", rabbitAtlas = "assets/img/ninja-rabbit.pack";

}
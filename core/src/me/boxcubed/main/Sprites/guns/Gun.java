package me.boxcubed.main.Sprites.guns;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.World;
import com.boxcubed.net.NetworkManager;

import me.boxcubed.main.Sprites.Player;

public interface Gun {
/**
 * Whether the conditions are met for a bullet to fire
 * @return true if bullet should fire, false otherwise
 * 
 */
public boolean willFire(Input input, float delta,Player player);

/**
 * Fire this gun. Calls every update as long as {@link Gun#willFire(Input)} is true
 */
public void fire(World world,Player player);
/**
 * The code to run in order to tell server bullet was fired.<br>
 * Will be deprecated soon since we are moving to anti cheat method of server client communication
 */
public void netFire(NetworkManager net,World world, Player player);
}

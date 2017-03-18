package com.boxcubed.utils;

public abstract interface MenuListener {

/**
 * Once run when clicked
 */
abstract void clicked(MenuButton m);
/**
 * Once run when right clicked
 */
abstract void rightclicked(MenuButton m);
/**
 * Always run with game loop when chosen is true
 * 
 */
abstract void chosen(MenuButton m);
/**
 * Always run when not chosen
 */
abstract void notChosen(MenuButton m);

}

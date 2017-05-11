package com.boxcubed.utils;

public abstract interface MenuListener {

/**
 * Once run when clicked
 */
public default void clicked(MenuButton m){};
/**
 * Once run when right clicked
 */
public default void rightclicked(MenuButton m){};
/**
 * Always run with game loop when chosen is true
 * 
 */
public default void chosen(MenuButton m){};
/**
 * Always run when not chosen
 */
public default void notChosen(MenuButton m){};

}

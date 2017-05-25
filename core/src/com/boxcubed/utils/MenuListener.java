package com.boxcubed.utils;

public interface MenuListener {

/**
 * Once run when clicked
 */
void clicked(MenuButton m);

    /**
 * Once run when right clicked
 */
void rightclicked(MenuButton m);

    /**
 * Always run with game loop when chosen is true
 * 
 */
void chosen(MenuButton m);

    /**
 * Always run when not chosen
 */
void notChosen(MenuButton m);

}

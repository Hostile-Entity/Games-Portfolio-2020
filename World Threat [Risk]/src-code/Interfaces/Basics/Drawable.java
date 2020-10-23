package com.strat7.game.Interfaces.Basics;

/**
 * Created by Евгений on 21.09.2017.
 */

public interface Drawable {
    void draw(boolean considerBorders);
    void hide();
    void show();

    boolean getConsider();
    void setConsider(boolean considerBorders);
}

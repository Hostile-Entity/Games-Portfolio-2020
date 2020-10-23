package com.strat7.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.strat7.game.Money.Economy;

/**
 * Created by Юра on 11.08.2017.
 */

public interface Graphics {
     BitmapFont getFont();

     float getDeltaW();
     float getDeltaH();

     int getScreenW();
     int getScreenH();

     Batch getBatch();
     float getNormalFontSize();
     float getTimePassed();

     TextureManager getGraphics();
}

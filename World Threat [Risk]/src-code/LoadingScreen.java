package com.strat7.game;

import com.badlogic.gdx.graphics.Color;
import com.strat7.game.Interfaces.Basics.BoundBasics.Frame;
import com.strat7.game.Interfaces.Basics.BoundTexture;
import com.strat7.game.Screens.ListenableScreen;
import com.strat7.game.Screens.Manager.AppScreen;

/**
 * Created by Евгений on 13.09.2017.
 */

public class LoadingScreen extends ListenableScreen {
    private static final int FRAMES_BEFORE_RELOAD_SCREEN = 11;
    BoundTexture logo;

    int cycleNum = 0;
    boolean loaded;

    public LoadingScreen (boolean loaded) {
        super();
        logo = new BoundTexture(
                this, Strat7.graphics.getTextureByName("Interface/Logo.png"),
                (Frame.screenWidth - Frame.screenHeight) / 2, 0, Frame.screenHeight, Frame.screenHeight, 1, Color.WHITE
        );
        this.loaded = loaded;
    }

    @Override
    public void draw(boolean considerBorders) {
        render(AppScreen.getDelta());
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if(cycleNum == 1 && !loaded) {
            Strat7.loadAll();
            background = new BoundTexture(this,Strat7.graphics.getTextureByName("Interface/BackForText.png"),0,0,getLocalWidth(),getLocalHeight(), 1,Strat7.backColor);
            logo = new BoundTexture(
                    this, Strat7.graphics.getTextureByName("Interface/Logo.png"),
                    (Frame.screenWidth - Frame.screenHeight) / 2, 0, Frame.screenHeight, Frame.screenHeight, 1, Color.WHITE
            );
        }
        if(cycleNum == FRAMES_BEFORE_RELOAD_SCREEN && !loaded) {
            Strat7.reloadStartScreen();
        }
        background.draw(false);
        logo.draw(false);
        cycleNum ++;
    }
}

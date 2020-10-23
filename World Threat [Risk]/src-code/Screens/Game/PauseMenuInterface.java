package com.strat7.game.Screens.Game;

import com.badlogic.gdx.graphics.Color;
import com.strat7.game.Interfaces.Basics.*;
import com.strat7.game.Interfaces.Basics.BoundBasics.Carcass;
import com.strat7.game.Strat7;

/**
 * Created by Евгений on 06.08.2017.
 */

public class PauseMenuInterface extends Interface {
    public static final int EXIT_BUTTON = 0;
    public static final int EXIT_TO_MAIN_MENU_BUTTON = 1;
    public static final int STATS = 2;
    public static final int SETTINGS_BUTTON = 3;
    public static final int SAVE_GAME_BUTTON = 4;
    public static final int RESUME_BUTTON = 5;
    public static final int BACK_TO_PREVIOUS_SCREEN_BUTTON = 6;

    private BoundTexture background;

    public PauseMenuInterface (Carcass screen) {
        super(screen, NARROW_INTERFACE);
        scale = 1f;

        buttonAmount = BACK_TO_PREVIOUS_SCREEN_BUTTON;
        buttonArray = new Button[buttonAmount];
        rulesForButtons = new Runnable[buttonAmount + 1];

        background = new BoundTexture(this,Strat7.graphics.getTextureByName("Interface/SmallMenu.png"),0,0,width,height,1,Strat7.backColor);

        //exit Button
        int shift = EXIT_BUTTON;
        for (int i = 0; i <= RESUME_BUTTON; i ++) {
            buttonArray[i + shift] = new Button( this, deltaW, (3 + i*(4 + 1)) * deltaH, 14 * deltaW, 4 * deltaH, "Interface/Button1.png", Strat7.buttonColor, "Interface/Button2.png");
            buttonArray[i + shift].setVisible(true);
        }
        buttonArray[RESUME_BUTTON].setText(Strat7.myBundle.get("resume"));
        buttonArray[SAVE_GAME_BUTTON].setText(Strat7.myBundle.get("save_game"));
        buttonArray[SETTINGS_BUTTON].setText(Strat7.myBundle.get("settings"));
        buttonArray[STATS].setText(Strat7.myBundle.get("stats"));
        buttonArray[EXIT_TO_MAIN_MENU_BUTTON].setText(Strat7.myBundle.get("exit_to_main_menu"));
        buttonArray[EXIT_BUTTON].setText(Strat7.myBundle.get("exit"));

        initDrawables();
    }

    private void initDrawables() {
        addDrawable(background,false);
        for (int i = 0; i <= RESUME_BUTTON; i++) {
            addDrawable(buttonArray[i + EXIT_BUTTON],false);
        }
    }

    @Override
    public boolean outOfBorders(int X, int Y) {
        return (X < getPosX() || X > getPosX() + getWidth()) || (Y < getPosY() || Y > getPosY() + getHeight());
    }
    @Override
    protected boolean callBackButton() {
        rulesForButtons[BACK_TO_PREVIOUS_SCREEN_BUTTON].run();
        return true;
    }

}

package com.strat7.game.Screens.MainMenu;

import com.badlogic.gdx.graphics.Color;
import com.strat7.game.GameInfo.PlayersList;
import com.strat7.game.Interfaces.Basics.*;
import com.strat7.game.Interfaces.Basics.BoundBasics.Carcass;
import com.strat7.game.Strat7;


public class SettingsInterface extends Interface {
    public static final int GAME_SPEED_BUTTON = 0;
    public static final int GAME_DIFFICULTY_BUTTON = 1;
    public static final int GAME_CLICK_SPEED_BUTTON = 2;
    public static final int FOG_OF_WAR_BUTTON = 3;
    public static final int BACK_TO_PREVIOUS_SCREEN_BUTTON = 4;

    private PlayersList playersList;
    private BoundTexture background;

    public SettingsInterface(Carcass screen, PlayersList playersList) {
        super(screen, BIG_INTERFACE);
        scale = 1f;

        this.playersList = playersList;
        buttonAmount = FOG_OF_WAR_BUTTON + 1;
        buttonArray = new Button[buttonAmount];
        rulesForButtons = new Runnable[buttonAmount + 1];

        background = new BoundTexture(this,Strat7.graphics.getTextureByName("Interface/SmallMenu.png"),0,0,width,height,1,Strat7.backColor);

        for (int i = 0; i < 2; i ++) {
            buttonArray[2 * i] = new Button(this,4*deltaW, (5 + (4 + 1) * (4-i)) * deltaH, (15) * deltaW, 4 * deltaH, "Interface/Button1.png", Strat7.buttonColor, "Interface/Button2.png");
            buttonArray[2 * i].setVisible(true);

            buttonArray[2 * i + 1] = new Button(this,(15 + 2 + 4)*deltaW, (5 + (4 + 1) * (4-i)) * deltaH, (15) * deltaW, 4 * deltaH, "Interface/Button1.png", Strat7.buttonColor, "Interface/Button2.png");
            buttonArray[2 * i + 1].setVisible(true);
        }
        buttonArray[GAME_CLICK_SPEED_BUTTON] =
                new Button(
                        this,
                        (7.5f + 1 + 4)*deltaW,
                        (5 + (4 + 1) * (4-1)) * deltaH,
                        (15) * deltaW,
                        4 * deltaH,
                        "Interface/Button1.png", Strat7.buttonColor,
                        "Interface/Button2.png"
                );
        buttonArray[GAME_CLICK_SPEED_BUTTON].setVisible(true);

        buttonArray[FOG_OF_WAR_BUTTON] =
                new Button(
                        this,
                        (7.5f + 1 + 4)*deltaW,
                        (5 + (4 + 1) * (4-2)) * deltaH,
                        (15) * deltaW, 4 * deltaH,
                        "Interface/Button1.png", Strat7.buttonColor,
                        "Interface/Button2.png"
                );
        buttonArray[FOG_OF_WAR_BUTTON].setVisible(true);

        Strat7.changeAISpeed(Strat7.preferences.getInteger("AISpeed", 2) + 1);
        setGameSpeedButtonText();
        Strat7.changeClickSpeed(Strat7.preferences.getInteger("ClickSpeed", 2) + 1);
        setClickSpeedButtonText();
        setFogOfWarButtonText();
    }

    public void setGameSpeedButtonText() {
        buttonArray[GAME_SPEED_BUTTON].setText(Strat7.myBundle.get("ai_speed") + " " + Strat7.myBundle.get("speed" + Strat7.preferences.getInteger("AISpeed", 2)));
    }

    public void setClickSpeedButtonText() {
        buttonArray[GAME_CLICK_SPEED_BUTTON].setText(Strat7.myBundle.get("click_speed") + " " + Strat7.myBundle.get("speed" + Strat7.preferences.getInteger("ClickSpeed", 2)));
    }

    public void setFogOfWarButtonText() {
        if (Strat7.preferences.getBoolean("Fog", false)) {
            buttonArray[FOG_OF_WAR_BUTTON].setText(Strat7.myBundle.get("fog") + " " + Strat7.myBundle.get("on"));
        } else {
            buttonArray[FOG_OF_WAR_BUTTON].setText(Strat7.myBundle.get("fog") + " " + Strat7.myBundle.get("off"));
            Strat7.preferences.putBoolean("Fog", false);
            Strat7.preferences.flush();
        }
    }

    private void checkSettings() {
        int difficulty = -1;
        for(int i = 0; i < PlayersList.MAX_PLAYER_AMOUNT; i ++) {
            if(difficulty == -1)
                difficulty = playersList.getInactivePlayer(i).getArtificialType();
            else
                if(difficulty != playersList.getInactivePlayer(i).getArtificialType()) {
                    difficulty = 4;
                    break;
                }
        }
        buttonArray[GAME_DIFFICULTY_BUTTON].setText(Strat7.myBundle.get("difficulty") + " " + Strat7.myBundle.get("difficulty_" + difficulty));
    }

    @Override
    public boolean outOfBorders(int X, int Y) {
        return (X < getPosX() || X > getPosX() + getWidth()) || (Y < getPosY() || Y > getPosY() + getHeight());
    }

    @Override
    public void setRuleForButton(Runnable ruleForButton, int num) {
        rulesForButtons[num] = ruleForButton;
    }

    @Override
    protected boolean callBackButton() {
        rulesForButtons[BACK_TO_PREVIOUS_SCREEN_BUTTON].run();
        return true;
    }

    @Override
    protected void commonDraw() {
        checkSettings();
        if (pressedButton != -1) {
            buttonArray[pressedButton].setPressed(true);
        }
        int shift;
        background.draw(false);
        Strat7.font.setColor(Color.WHITE);
        shift = GAME_SPEED_BUTTON;
        for (int i =0; i < 4; i++) {
            buttonArray[i + shift].draw(false);
        }
    }

}

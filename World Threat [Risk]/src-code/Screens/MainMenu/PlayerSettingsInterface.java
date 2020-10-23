package com.strat7.game.Screens.MainMenu;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.strat7.game.GameInfo.PlayersList;
import com.strat7.game.Interfaces.Basics.BoundBasics.Carcass;
import com.strat7.game.Interfaces.Basics.BoundTexture;
import com.strat7.game.Interfaces.Basics.Button;
import com.strat7.game.Interfaces.Basics.Interface;
import com.strat7.game.Strat7;


public class PlayerSettingsInterface extends Interface {
    public static final int   FIRST_COLOR_BUTTON = 0;
    public static final int  SECOND_COLOR_BUTTON = 1;
    public static final int   THIRD_COLOR_BUTTON = 2;
    public static final int  FOURTH_COLOR_BUTTON = 3;
    public static final int   FIFTH_COLOR_BUTTON = 4;
    public static final int   SIXTH_COLOR_BUTTON = 5;
    public static final int SEVENTH_COLOR_BUTTON = 6;
    public static final int  EIGHTH_COLOR_BUTTON = 7;
    public static final int  MAKE_PLAYER_BUTTON = 8;
    public static final int      MAKE_AI_BUTTON = 9;
    public static final int DUMB_DIFFICULTY_BUTTON = 10;
    public static final int EASY_DIFFICULTY_BUTTON = 11;
    public static final int MEDIUM_DIFFICULTY_BUTTON = 12;
    public static final int HARD_DIFFICULTY_BUTTON = 13;
    public static final int BACK_TO_PREVIOUS_SCREEN_BUTTON = 14;


    private int player;
    private PlayersList playersList;
    private BoundTexture background;

    public PlayerSettingsInterface(Carcass screen, PlayersList playersList) {
        super(screen, BIG_INTERFACE);
        scale = 1f;
        this.playersList = playersList;

        buttonAmount = HARD_DIFFICULTY_BUTTON + 1;
        buttonArray = new Button[buttonAmount];
        rulesForButtons = new Runnable[buttonAmount + 1];

        background = new BoundTexture(this,Strat7.graphics.getTextureByName("Interface/SmallMenu.png"),0,0,width,height,1,Strat7.backColor);

        //ChooseColor
        int shift = FIRST_COLOR_BUTTON;
        for(int i = 0; i < 8; i++){
            buttonArray[i + shift] = new  Button(this,4*deltaW + i*4*deltaW, 35*deltaH - 7*deltaW, 4*deltaW, 4*deltaW, "Interface/Turn.png");
            buttonArray[i + shift].setVisible(true);
        }
        //change AI
        buttonArray[MAKE_PLAYER_BUTTON] = new  Button(this, 4*deltaW, 5*deltaH , 8*deltaW, 3*deltaH, "Interface/BackForText.png" , new Color(0, 1, 1, 0.5f), new Color(0, 1, 1, 0.2f));
        buttonArray[MAKE_PLAYER_BUTTON].setText(Strat7.myBundle.get("player"));
        buttonArray[MAKE_AI_BUTTON] = new  Button(this,4*deltaW, (5 + 3 + 1)*deltaH , 8*deltaW, 3*deltaH,"Interface/BackForText.png", new Color(0, 1, 1, 0.5f), new Color(0, 1, 1, 0.2f));
        buttonArray[MAKE_AI_BUTTON].setText(Strat7.myBundle.get("AI"));

        //Difficulty level
        shift = DUMB_DIFFICULTY_BUTTON;
        for (int i = shift; i < 4 + shift; i++){
            buttonArray[i] = new  Button(this,(4 + 8 + 2)*deltaW, (5 + (i - shift)*(3+1))*deltaH , 8*deltaW, 3*deltaH,"Interface/BackForText.png", new Color(0, 1, 1, 0.5f), new Color(0, 1, 1, 0.2f));
        }
        buttonArray[DUMB_DIFFICULTY_BUTTON].setVisible(true);
        buttonArray[DUMB_DIFFICULTY_BUTTON].setText(Strat7.myBundle.get("ai_difficulty0"));
        buttonArray[EASY_DIFFICULTY_BUTTON].setText(Strat7.myBundle.get("ai_difficulty1"));
        buttonArray[MEDIUM_DIFFICULTY_BUTTON].setText(Strat7.myBundle.get("ai_difficulty2"));
        buttonArray[HARD_DIFFICULTY_BUTTON].setText(Strat7.myBundle.get("ai_difficulty3"));
    }

    public void setPlayer(int player) {
        this.player =player;
        int shift = FIRST_COLOR_BUTTON;
        for(int i = 0; i < PlayersList.MAX_PLAYER_AMOUNT; i ++) {
            if(i < player)
                buttonArray[i + shift].boundWithNewCarcass(this,(4-1)*deltaW + i*4*deltaW, 35*deltaH - 7*deltaW, 4*deltaW, 4*deltaW, 1);
            if(i == player)
                buttonArray[i + shift].boundWithNewCarcass(this,(4-1)*deltaW + i*4*deltaW, 35*deltaH - (7 + 2) * deltaW, 4 * deltaW, 4 * deltaW, 1.5);
            if(i > player)
                buttonArray[i + shift].boundWithNewCarcass(this,(4+1)*deltaW + i*4*deltaW, 35*deltaH - 7*deltaW, 4*deltaW, 4*deltaW, 1);
        }

        buttonArray[MAKE_AI_BUTTON].setVisible(playersList.getActivePlayer(player).isArtificial());
        buttonArray[MAKE_PLAYER_BUTTON].setVisible(!playersList.getActivePlayer(player).isArtificial());
        for(int i = DUMB_DIFFICULTY_BUTTON; i <= HARD_DIFFICULTY_BUTTON; i++) {
            buttonArray[i].setVisible((i - DUMB_DIFFICULTY_BUTTON) == playersList.getActivePlayer(player).getArtificialType());
            buttonArray[i].setBlocked(!playersList.getActivePlayer(player).isArtificial());
        }
    }

    public void configPlayersAI () {
        buttonArray[MAKE_AI_BUTTON].setVisible(playersList.getActivePlayer(player).isArtificial());
        buttonArray[MAKE_PLAYER_BUTTON].setVisible(!playersList.getActivePlayer(player).isArtificial());
        for(int i = DUMB_DIFFICULTY_BUTTON; i <= HARD_DIFFICULTY_BUTTON; i++) {
            buttonArray[i].setVisible((i - DUMB_DIFFICULTY_BUTTON) == playersList.getActivePlayer(player).getArtificialType());
            buttonArray[i].setBlocked(!playersList.getActivePlayer(player).isArtificial());
        }
    }

    public int getPlayer() {
        return player;
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

        background.draw(false);
        Strat7.font.setColor(Color.WHITE);
        for (int i = 0; i < PlayersList.MAX_PLAYER_AMOUNT; i ++) {
            if (buttonArray[i + FIRST_COLOR_BUTTON].isVisible()) {
                buttonArray[i + FIRST_COLOR_BUTTON].setColor(playersList.getInactivePlayer(i).getColor());
                buttonArray[i + FIRST_COLOR_BUTTON].draw(false);
            }
        }

        for (int i = MAKE_PLAYER_BUTTON; i < buttonAmount; i++) {
            if (i > MAKE_AI_BUTTON && !playersList.getInactivePlayer(player).isArtificial())
                break;
            //important color
            buttonArray[i].drawChooseButton(false);
        }
    }
}

package com.strat7.game.Screens.MainMenu;

import com.badlogic.gdx.graphics.Color;
import com.strat7.game.GameInfo.Player;
import com.strat7.game.GameInfo.PlayersList;
import com.strat7.game.Interfaces.Basics.*;
import com.strat7.game.Interfaces.Basics.BoundBasics.Carcass;
import com.strat7.game.Interfaces.Basics.Text.PlainText;
import com.strat7.game.Interfaces.InterfaceCommon;
import com.strat7.game.Strat7;

/**
 * Created by Юра on 20.06.2017.
 */

public class InterfaceMainMenu extends InterfaceCommon {
    public static final int GAME_BUTTON = 0;
    public static final int MAP_BUTTON = 1;
    public static final int SETTINGS_BUTTON = 2;
    public static final int BACK_BUTTON = 3;
    public static final int ADD_PLAYER_BUTTON = 4;
    public static final int   FIRST_PLAYER_SETTINGS_BUTTON = 5 ;
    public static final int  SECOND_PLAYER_SETTINGS_BUTTON = 6 ;
    public static final int   THIRD_PLAYER_SETTINGS_BUTTON = 7 ;
    public static final int  FOURTH_PLAYER_SETTINGS_BUTTON = 8 ;
    public static final int   FIFTH_PLAYER_SETTINGS_BUTTON = 9 ;
    public static final int   SIXTH_PLAYER_SETTINGS_BUTTON = 10;
    public static final int SEVENTH_PLAYER_SETTINGS_BUTTON = 11;
    public static final int  EIGHTH_PLAYER_SETTINGS_BUTTON = 12;
    public static final int   FIRST_PLAYER_DELETE_BUTTON = 13;
    public static final int  SECOND_PLAYER_DELETE_BUTTON = 14;
    public static final int   THIRD_PLAYER_DELETE_BUTTON = 15;
    public static final int  FOURTH_PLAYER_DELETE_BUTTON = 16;
    public static final int   FIFTH_PLAYER_DELETE_BUTTON = 17;
    public static final int   SIXTH_PLAYER_DELETE_BUTTON = 18;
    public static final int SEVENTH_PLAYER_DELETE_BUTTON = 19;
    public static final int  EIGHTH_PLAYER_DELETE_BUTTON = 20;
    public static final int  BACK_TO_PREVIOUS_SCREEN_BUTTON = 21;

    private PlayersList playersList;


    private BoundTexture[] playersBackground;
    private PlainText[] playersInfo;
    private PlainText[] numbers;

    public InterfaceMainMenu(Carcass screen, PlayersList playersIdList) {
        super(screen, FULL_SCREEN_INTERFACE);
        scale = 1;

        this.playersList = playersIdList;

        buttonAmount = EIGHTH_PLAYER_DELETE_BUTTON + 1;
        buttonArray = new Button[buttonAmount];
        rulesForButtons = new Runnable[buttonAmount + 1];
        playersBackground = new BoundTexture[PlayersList.MAX_PLAYER_AMOUNT];
        playersInfo = new PlainText[PlayersList.MAX_PLAYER_AMOUNT];
        numbers = new PlainText[PlayersList.MAX_PLAYER_AMOUNT];
        for(int i = 0; i < PlayersList.MAX_PLAYER_AMOUNT; i++) {
            if (i < 4) {
                playersBackground[i] = new BoundTexture(this,Strat7.graphics.getTextureByName("Interface/Window.png"),2*deltaW, (39 - i*(8 + 2))*deltaH, 22*deltaW, 8*deltaH,1, null);
            } else {
                playersBackground[i] = new BoundTexture(this,Strat7.graphics.getTextureByName("Interface/Window.png"),(2 + 22 + 2)*deltaW, (39 - (i-4)*(8 + 2))*deltaH, 22*deltaW, 8*deltaH,1, null);
            }
            playersInfo[i] = new PlainText(playersBackground[i],8.5f * deltaH,5f * deltaH, 20 * deltaH, 3 * deltaH,"", Color.BLACK);
            numbers[i] =     new PlainText(playersBackground[i],- 1f * deltaW,3 * deltaH, 2 * deltaH, 3 * deltaH,Integer.toString(i + 1),Color.WHITE);

        }

        int shift = GAME_BUTTON;
        for (int i = GAME_BUTTON; i <= BACK_BUTTON; i++) {
            buttonArray[i] = new Button(
                    this,
                    (2+i*(10+2))*deltaW,
                    2*deltaH,
                    10*deltaW,
                    5*deltaH,
                    "Interface/Button1.png",
                    Strat7.buttonColor,
                    "Interface/Button2.png"
            );
            buttonArray[i].setVisible(true);
        }
        buttonArray[GAME_BUTTON].setText(Strat7.myBundle.get("play"));
        buttonArray[MAP_BUTTON].setText(Strat7.myBundle.get("map"));
        buttonArray[SETTINGS_BUTTON].setText(Strat7.myBundle.get("settings"));
        buttonArray[BACK_BUTTON].setText(Strat7.myBundle.get("back"));

        //Plus button
        buttonArray[ADD_PLAYER_BUTTON] = new Button(
                playersBackground[playersIdList.getActivePlayersAmount() == PlayersList.MAX_PLAYER_AMOUNT ? playersIdList.getActivePlayersAmount() - 1 : playersIdList.getActivePlayersAmount()],
                0,
                0,
                22*deltaW,
                8*deltaH,
                "Interface/Button1.png",
                new BoundTexture(null,Strat7.graphics.getTextureByName("Icons/Plus.png"), 11 * deltaW - 4 * deltaH,- 4 * deltaH / 11f,8*deltaH, 8*deltaH,1, null),
                new Color(1, 1, 1, 0.3f), new Color(1,1,1,0.3f),
                "Interface/Button2.png"
        );
        if(playersIdList.getActivePlayersAmount() == PlayersList.MAX_PLAYER_AMOUNT) {
            buttonArray[ADD_PLAYER_BUTTON].setVisible(false);
            buttonArray[ADD_PLAYER_BUTTON].setBlocked(true);
        }
        buttonArray[ADD_PLAYER_BUTTON].setVisible(true);
        int active = playersList.getActivePlayersAmount();
        if (active < 4) {
            buttonArray[ADD_PLAYER_BUTTON].setPosX((2 + 11) * deltaW - buttonArray[4].getWidth()/2);
            buttonArray[ADD_PLAYER_BUTTON].setPosY((39 - active * (8 + 2)) * deltaH);
        } else {
            buttonArray[ADD_PLAYER_BUTTON].setPosX((2 + 22 + 2 + 11) * deltaW - buttonArray[4].getWidth()/2);
            buttonArray[ADD_PLAYER_BUTTON].setPosY((39 - (active - 4) * (8 + 2)) * deltaH);
        }

        //Settings
        shift = FIRST_PLAYER_SETTINGS_BUTTON;
        for(int i = 0; i < PlayersList.MAX_PLAYER_AMOUNT; i++){
            if (i < 4)
                buttonArray[i + shift] = new  Button(
                        this,
                        (2)*deltaW, (39 - i*(8 + 2))*deltaH, 8*deltaH, 8*deltaH, "Interface/Turn.png",
                        new BoundTexture(null, Strat7.graphics.getTextureByName("Icons/Settings.png"),0,0,8*deltaH, 8*deltaH,1, null),
                        Color.WHITE
                );
            else
                buttonArray[i + shift] = new  Button(
                        this,
                        (2 + 22 + 2) * deltaW, (39 - (i - 4) * (8 + 2)) * deltaH, 8*deltaH, 8 * deltaH,
                        "Interface/Turn.png",
                        new BoundTexture(null, Strat7.graphics.getTextureByName("Icons/Settings.png"),0,0,8*deltaH, 8*deltaH,1, null),
                        Color.WHITE);
            if(i >= playersIdList.getActivePlayersAmount())
                buttonArray[i + shift].setBlocked(true);
        }

        //Minuses
        shift = FIRST_PLAYER_DELETE_BUTTON;
        for(int i = 0; i < PlayersList.MAX_PLAYER_AMOUNT; i++){
            if (i < 4) {
                buttonArray[i + shift] = new  Button(this,(2+22)*deltaW - 8*deltaH, (39 - i*(8 + 2))*deltaH, 8*deltaH, 8*deltaH, "Interface/Minus1.png", Strat7.buttonColor, "Interface/Minus2.png");
            } else {
                buttonArray[i + shift] = new  Button(this,(2 + 22 + 2 + 22) * deltaW - 8*deltaH, (39 - (i - 4) * (8 + 2)) * deltaH, 8*deltaH, 8 * deltaH, "Interface/Minus1.png", Strat7.buttonColor, "Interface/Minus2.png");
            }
            if(i >= playersIdList.getActivePlayersAmount())
                buttonArray[i + shift].setBlocked(true);
        }


        initDrawables();
    }

    private void initDrawables() {
        int i;

        //First buttons
        for (i = 0; i < ADD_PLAYER_BUTTON; i++) {
            addDrawable(buttonArray[i], false);
        }

        for (i = 0; i < PlayersList.MAX_PLAYER_AMOUNT; i ++) {
            addDrawable(playersBackground[i],false);
            addDrawable(playersInfo[i],false);
            addDrawable(buttonArray[i + FIRST_PLAYER_SETTINGS_BUTTON],false);
            addDrawable(buttonArray[i + FIRST_PLAYER_DELETE_BUTTON],false);
        }
        addDrawable(buttonArray[ADD_PLAYER_BUTTON],false);
        //Numbers
        for (i = 0; i < 8; i++) {
            addDrawable(numbers[i],false);
        }
    }


    @Override
    public void setRuleForButton(Runnable ruleForButton, int num) {
        rulesForButtons[num] = ruleForButton;
    }

    @Override
    protected void commonDraw() {

        initPlayerButtons();

        super.commonDraw();

        drawMoney();
    }

    private void initPlayerButtons() {
        String artificial;
        int i;

        //Active players
        for (i = 0; i < playersList.getActivePlayersAmount(); i++) {
            if (!playersList.getActivePlayer(i).isArtificial()){
                artificial = Strat7.myBundle.get("player");
            } else {
                artificial = Strat7.myBundle.get("AI") + " (" + Strat7.myBundle.get("ai_difficulty" + playersList.getActivePlayer(i).getArtificialType()) + ")";
            }
            playersBackground[i].setColor(new Color(1,1,1,0.5f));
            playersInfo[i].setText(artificial);
            playersBackground[i].show();
            playersInfo[i].show();

            //minus and settings
            buttonArray[i + FIRST_PLAYER_SETTINGS_BUTTON].setColor(playersList.getActivePlayer(i).getColor());
            buttonArray[i + FIRST_PLAYER_SETTINGS_BUTTON].setVisible(true);
            buttonArray[i + FIRST_PLAYER_DELETE_BUTTON].setVisible(true);
        }

        //Plus
        if (i < 8) {
            buttonArray[ADD_PLAYER_BUTTON].setBlocked(false);
            buttonArray[ADD_PLAYER_BUTTON].setVisible(true);
            buttonArray[ADD_PLAYER_BUTTON].boundWithNewCarcass(playersBackground[i],0,0,1);
            playersBackground[i].hide();
            playersInfo[i].hide();
        } else {
            buttonArray[ADD_PLAYER_BUTTON].setVisible(false);
            buttonArray[ADD_PLAYER_BUTTON].setBlocked(true);
        }

        for(i = playersList.getActivePlayersAmount() + 1; i < PlayersList.MAX_PLAYER_AMOUNT; i ++) {
            playersBackground[i].setColor(new Color(1, 1, 1, 0.25f));
            playersBackground[i].show();
            playersInfo[i].hide();
        }
    }

    @Override
    protected boolean callBackButton() {
        rulesForButtons[BACK_TO_PREVIOUS_SCREEN_BUTTON].run();
        return true;
    }

    @Override
    public boolean outOfBorders(int X, int Y) {
        return (X < getPosX() || X > getPosX() + getWidth()) || (Y < getPosY() || Y > getPosY() + getHeight());
    }

    public void addPlayerPressed() {
        get(playersList.getActivePlayersAmount() + FIRST_PLAYER_SETTINGS_BUTTON).setVisible(true);
        get(playersList.getActivePlayersAmount() + FIRST_PLAYER_DELETE_BUTTON).setVisible(true);
        get(playersList.getActivePlayersAmount() + FIRST_PLAYER_SETTINGS_BUTTON).setBlocked(false);
        get(playersList.getActivePlayersAmount() + FIRST_PLAYER_DELETE_BUTTON).setBlocked(false);
        playersList.makePlayerActive(playersList.getActivePlayersAmount());
        if(playersList.getActivePlayersAmount() == 8)
            get(InterfaceMainMenu.ADD_PLAYER_BUTTON).setBlocked(true);
    }

    public void deletePlayer(int playerNum) {
        if(playersList.getActivePlayersAmount() == 8)
            get(InterfaceMainMenu.ADD_PLAYER_BUTTON).setBlocked(false);
        playersList.makePlayerInactive(playersList.getActivePlayersAmount() - 1);
        playersList.shiftAllPlayersFrom(playerNum);
        get(playersList.getActivePlayersAmount() + InterfaceMainMenu.FIRST_PLAYER_SETTINGS_BUTTON).setVisible(false);
        get(playersList.getActivePlayersAmount() + InterfaceMainMenu.FIRST_PLAYER_DELETE_BUTTON).setVisible(false);
        get(playersList.getActivePlayersAmount() + InterfaceMainMenu.FIRST_PLAYER_SETTINGS_BUTTON).setBlocked(true);
        get(playersList.getActivePlayersAmount() + InterfaceMainMenu.FIRST_PLAYER_DELETE_BUTTON).setBlocked(true);
    }
}
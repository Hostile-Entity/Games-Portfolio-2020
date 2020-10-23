package com.strat7.game.Screens.StartMenu;

import com.badlogic.gdx.graphics.Color;
import com.strat7.game.GameInfo.GameSaver;
import com.strat7.game.GameInfo.PlayersList;
import com.strat7.game.Interfaces.Basics.BoundBasics.BoundWithFrame;
import com.strat7.game.Interfaces.Basics.BoundBasics.Carcass;
import com.strat7.game.Interfaces.Basics.BoundTexture;
import com.strat7.game.Interfaces.Basics.Button;
import com.strat7.game.Interfaces.Basics.Text.BoundText;
import com.strat7.game.Interfaces.Basics.Text.TextField;
import com.strat7.game.Interfaces.InterfaceCommon;
import com.strat7.game.Strat7;

/**
 * Created by Евгений on 20.08.2017.
 */

public class SaveInterface extends InterfaceCommon {
    public static final int FIRST_SAVE_BUTTON = 0;
    public static final int SECOND_SAVE_BUTTON = 1;
    public static final int THIRD_SAVE_BUTTON = 2;
    public static final int FORTH_SAVE_BUTTON = 3;
    public static final int FIFTH_SAVE_BUTTON = 4;
    public static final int SIXTH_SAVE_BUTTON = 5;
    public static final int SEVENTH_SAVE_BUTTON = 6;
    public static final int EIGHTH_SAVE_BUTTON = 7;
    public static final int FIRST_DELETE_SAVE_BUTTON = 8;
    public static final int SECOND_DELETE_SAVE_BUTTON = 9;
    public static final int THIRD_DELETE_SAVE_BUTTON = 10;
    public static final int FORTH_DELETE_SAVE_BUTTON = 11;
    public static final int FIFTH_DELETE_SAVE_BUTTON = 12;
    public static final int SIXTH_DELETE_SAVE_BUTTON = 13;
    public static final int SEVENTH_DELETE_SAVE_BUTTON = 14;
    public static final int EIGHTH_DELETE_SAVE_BUTTON = 15;
    public static final int BACK_BUTTON = 16;

    protected BoundText[] mapNames;
    protected BoundText[] playerAmounts;

    protected BoundTexture[] textBackgrounds;
    protected BoundTexture background;
    public static final int MAX_SAVES_AMOUNT = 8;

    public SaveInterface(Carcass screen) {
        super(screen, FULL_SCREEN_INTERFACE);
        scale = 1;

        background = new BoundTexture(this, Strat7.graphics.getTextureByName("Interface/BackForText.png"),SAVES_INTERFACE.getPosX(),SAVES_INTERFACE.getPosY(),SAVES_INTERFACE.getLocalWidth(),SAVES_INTERFACE.getLocalHeight(),SAVES_INTERFACE.getLocalScale(),new Color(0,0,0,0.3f));

        buttonAmount = 16;
        buttonArray = new  Button[buttonAmount];

        rulesForButtons = new Runnable[buttonAmount + 1];

        textFieldAmount = MAX_SAVES_AMOUNT;
        textFields = new TextField[textFieldAmount];

        mapNames = new BoundText[textFieldAmount];
        playerAmounts = new BoundText[textFieldAmount];
        textBackgrounds = new BoundTexture[textFieldAmount];

        //Save Button
        int shift = FIRST_SAVE_BUTTON;
        for (int i = 0; i < 8; i++) {
            buttonArray[i + shift] = new Button(
                    this,
                    (2+14+2)*deltaW,
                    50*deltaH - 6*deltaH - (i+1)*(5)*deltaH,
                    30*deltaW, 5*deltaH,
                    "Interface/BackForText.png",
                    new Color(0, 1, 1, 0.5f),
                    new Color(0, 1, 1, 0.2f)
            );
            if(i >= GameSaver.savesAmount())
                buttonArray[i + shift].setBlocked(true);
        }
        if (Strat7.preferences.contains("Save0")) {
            buttonArray[FIRST_SAVE_BUTTON].setVisible(true);
        }

        for(int i = 0; i < GameSaver.savesAmount(); i ++) {
            String gameName = Strat7.preferences.getString("Save" + Integer.toString(GameSaver.savesAmount()-i-1));
            PlayersList playersIdList = Strat7.json.fromJson(PlayersList.class, Strat7.preferences.getString(gameName + "/Players"));
            mapNames[i] = new BoundText(
                    buttonArray[i],
                    Strat7.myBundle.get("map") + ": " + Strat7.preferences.getString(gameName + "/Map"),
                    Color.WHITE,
                    deltaW + 10 *deltaW, 3 *deltaH,
                    15 * deltaW,  1.5 * deltaH, 1
            );
            playerAmounts[i] = new BoundText(
                    buttonArray[i],
                    Strat7.myBundle.get("players") + ": " + Integer.toString(playersIdList.getActivePlayersAmount()),
                    Color.WHITE,
                    deltaW + 10 *deltaW, 0.75 * deltaH,
                    15 * deltaW,  1 * deltaH, 1/2d
            );
            textBackgrounds[i] = new BoundTexture(
                    buttonArray[i],
                    Strat7.graphics.getTextureByName("Interface/BackForText.png"),
                    0, 0,buttonArray[i].getLocalWidth(),buttonArray[i].getLocalHeight(), 1, Color.BLACK
            );
        }

        //Change Name
        for (int i = 0; i < 8; i++) {
            textFields[i] = new TextField(
                    buttonArray[i],
                    0, 2*deltaH,
                    10 *deltaW, 3*deltaH,
                    "Interface/Text.png", Color.WHITE);
            if(i >= GameSaver.savesAmount())
                textFields[i].setBlocked(true);
        }

        //Delete Save
        shift = FIRST_DELETE_SAVE_BUTTON;
        for (int i = 0; i < 8; i++) {
            buttonArray[i + shift] = new  Button(
                    buttonArray[i],
                    30*deltaW - 5*deltaH, 0,
                    5*deltaH, 5*deltaH,
                    "Interface/Minus1.png",
                    Strat7.buttonColor,
                    "Interface/Minus2.png"
            );
            buttonArray[i + shift].setVisible(true);
            if(i >= GameSaver.savesAmount())
                buttonArray[i + shift].setBlocked(true);
        }
    }

    @Override
    protected void commonDraw() {
        int i;
        background.draw(false);
        for (i = 0; i < GameSaver.savesAmount(); i++) {
            textFields[i].setVisible(true);
            buttonArray[FIRST_DELETE_SAVE_BUTTON + i].setVisible(true);
            textBackgrounds[i].draw(false);

            buttonArray[FIRST_SAVE_BUTTON + i].drawChooseButton(false);
            mapNames[i].draw(false);
            playerAmounts[i].draw(false);
            buttonArray[FIRST_DELETE_SAVE_BUTTON + i].draw(false);

            Strat7.font.getData().setScale(Strat7.normalFontSize);
            textFields[i].draw(false);
        }
        for (;i < 8; i++) {
            buttonArray[FIRST_DELETE_SAVE_BUTTON + i].setVisible(false);
            textFields[i].setVisible(false);
        }
        drawLine((2 + 14 + 1) * deltaW, 4 * deltaH);
        drawLine((49) * deltaW, 4 * deltaH);
    }

    @Override
    public boolean outOfBorders(int X, int Y) {
        return !(new BoundWithFrame(getMainFrame(), SAVES_INTERFACE)).contains(X,Y);
    }

    @Override
    protected boolean callBackButton() {
        rulesForButtons[BACK_BUTTON].run();
        return true;
    }
}

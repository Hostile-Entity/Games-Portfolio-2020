package com.strat7.game.Screens.StartMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.strat7.game.GameInfo.GameSaver;
import com.strat7.game.GameInfo.PlayersList;
import com.strat7.game.Interfaces.Basics.*;
import com.strat7.game.Interfaces.Basics.BoundBasics.Carcass;
import com.strat7.game.Interfaces.Basics.Text.BoundText;
import com.strat7.game.Interfaces.Basics.Text.TextField;
import com.strat7.game.Interfaces.ButtonWithCoin;
import com.strat7.game.Strat7;

/**
 * Created by Юра on 12.07.2017.
 */

public class InterfaceStartMenu extends Interface {
    public static final int PLAY_BUTTON = 0;
    public static final int CONTINUE_BUTTON = 1;
    public static final int LOAD_BUTTON = 2;
    public static final int ONLINE_BUTTON = 3;
    public static final int SHOP_BUTTON = 4;
    public static final int TUTORIAL_BUTTON = 5;
    public static final int LANGUAGE_CHANGE_BUTTON = 6;
    public static final int BACK_BUTTON = 7;


    public InterfaceStartMenu(Carcass screen) {
        super(screen, FULL_SCREEN_INTERFACE);
        scale = 1f;
        int shift;

        buttonAmount = 7;
        buttonArray = new  Button[buttonAmount];

        rulesForButtons = new Runnable[buttonAmount + 1];


        for (int i = 0; i < 6; i++) {
            buttonArray[i] = new  Button(
                    this,
                    2*deltaW,
                    50*deltaH - i*(5+2)*deltaH - 11*deltaH,
                    14*deltaW,
                    5*deltaH,"Interface/Button1.png",
                    Strat7.buttonColor,
                    "Interface/Button2.png"
            );
            buttonArray[i].setVisible(true);
        }
        buttonArray[PLAY_BUTTON].setText(Strat7.myBundle.get("single"));
        buttonArray[CONTINUE_BUTTON].setText(Strat7.myBundle.get("continue"));
        buttonArray[LOAD_BUTTON].setText(Strat7.myBundle.get("load"));
        buttonArray[ONLINE_BUTTON].setText(Strat7.myBundle.get("online"));
        buttonArray[SHOP_BUTTON].setText(Strat7.myBundle.get("shop"));
        buttonArray[TUTORIAL_BUTTON].setText(Strat7.myBundle.get("tutorial"));


        //Language
        if (Gdx.files.internal("Icons/" +Strat7.locale.getLanguage() + ".png").exists()){
            buttonArray[LANGUAGE_CHANGE_BUTTON] = new  Button(
                    this,
                    50 * deltaW - 3 * deltaH * 1.45f - deltaW,
                    50 * deltaH - 3 * deltaH - deltaH,
                    3 * deltaH * 1.45f, 3 * deltaH,
                    "Icons/eng.png",
                    new BoundTexture(null,Strat7.graphics.getTextureByName("Icons/" +Strat7.locale.getLanguage() + ".png"),0,0,3 * deltaH * 1.45f,3 * deltaH,1, Color.WHITE)
            );
        } else {
            buttonArray[LANGUAGE_CHANGE_BUTTON] = new  Button(
                    this,
                    50 * deltaW - 3 * deltaH * 1.45f - deltaW,
                    50 * deltaH - 3 * deltaH - deltaH,
                    3 * deltaH * 1.45f,
                    3 * deltaH,
                    "Icons/eng.png",
                    new BoundTexture(null,Strat7.graphics.getTextureByName("Icons/eng.png"),0,0,3 * deltaH * 1.45f,3 * deltaH,1, Color.WHITE)
            );
        }
        buttonArray[LANGUAGE_CHANGE_BUTTON].setVisible(true);


    }

    @Override
    public void commonDraw() {
        for(int i = 0; i < 7; i++) {
            buttonArray[i].draw(false);
        }
    }

/*
    public void drawTutorial() {
        float deltaH = (float) com.strat7.game.Interfaces.Basics.BoundBasics.Frame.deltaH;
        float deltaW = (float) com.strat7.game.Interfaces.Basics.BoundBasics.Frame.deltaW;
        Strat7.font.setColor(Color.WHITE);
        Strat7.font.draw(Strat7.batch, Strat7.myBundle.get("tutorial"), (2+14+2)*deltaW, 50*deltaH - 6*deltaH);
        Strat7.font.getData().setScale(Strat7.normalFontSize*2/3);
        layout.setText(Strat7.font, "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
        if (layout.width > 30*deltaW) {
            Strat7.font.getData().setScale(Strat7.normalFontSize*2/3 * 30*deltaW / layout.width);
        }
        Strat7.font.draw(Strat7.batch, Strat7.myBundle.get("tutorial_part1").substring(0,67), (2+14+2)*deltaW, 50*deltaH - 11*deltaH);
        Strat7.font.draw(Strat7.batch, Strat7.myBundle.get("tutorial_part1").substring(67, 134), (2+14+2)*deltaW, 50*deltaH - 11*deltaH - layout.height);
        if (Strat7.myBundle.get("tutorial_part1").length() > 200) {
            Strat7.font.draw(Strat7.batch, Strat7.myBundle.get("tutorial_part1").substring(134, 201), (2 + 14 + 2) * deltaW, 50 * deltaH - 11*deltaH - 2*layout.height);
            if (Strat7.myBundle.get("tutorial_part1").length() > 267) {
                Strat7.font.draw(Strat7.batch, Strat7.myBundle.get("tutorial_part1").substring(201, 268), (2 + 14 + 2) * deltaW, 50 * deltaH - 11*deltaH - 3*layout.height);
                if (Strat7.myBundle.get("tutorial_part1").length() > 335) {
                    Strat7.font.draw(Strat7.batch, Strat7.myBundle.get("tutorial_part1").substring(268, 335), (2 + 14 + 2) * deltaW, 50 * deltaH - 11*deltaH - 4*layout.height);
                } else {
                    Strat7.font.draw(Strat7.batch, Strat7.myBundle.get("tutorial_part1").substring(268), (2 + 14 + 2) * deltaW, 50 * deltaH - 11*deltaH - 4*layout.height);
                }
            } else {
                Strat7.font.draw(Strat7.batch, Strat7.myBundle.get("tutorial_part1").substring(201), (2 + 14 + 2) * deltaW, 50 * deltaH - 11*deltaH - 3*layout.height);
            }
        } else {
            Strat7.font.draw(Strat7.batch, Strat7.myBundle.get("tutorial_part1").substring(134), (2 + 14 + 2) * deltaW, 50 * deltaH - 11*deltaH - 2*layout.height);
        }

        Strat7.font.draw(Strat7.batch, Strat7.myBundle.get("tutorial_part2").substring(0,67), (2+14+2)*deltaW, 50*deltaH - 11*deltaH - 6*layout.height);
        Strat7.font.draw(Strat7.batch, Strat7.myBundle.get("tutorial_part2").substring(67, 134), (2+14+2)*deltaW, 50*deltaH - 11*deltaH - 7*layout.height);
        Strat7.font.draw(Strat7.batch, Strat7.myBundle.get("tutorial_part2").substring(134, 201), (2+14+2)*deltaW, 50*deltaH - 11*deltaH - 8*layout.height);
        if (Strat7.myBundle.get("tutorial_part2").length() > 267) {
            Strat7.font.draw(Strat7.batch, Strat7.myBundle.get("tutorial_part2").substring(201, 268), (2 + 14 + 2) * deltaW, 50 * deltaH - 11*deltaH - 9*layout.height);
            if (Strat7.myBundle.get("tutorial_part2").length() > 335) {
                Strat7.font.draw(Strat7.batch, Strat7.myBundle.get("tutorial_part2").substring(268, 335), (2 + 14 + 2) * deltaW, 50 * deltaH - 11*deltaH - 10*layout.height);
            } else {
                Strat7.font.draw(Strat7.batch, Strat7.myBundle.get("tutorial_part2").substring(268), (2 + 14 + 2) * deltaW, 50 * deltaH - 11*deltaH - 10*layout.height);
            }
        } else {
            Strat7.font.draw(Strat7.batch, Strat7.myBundle.get("tutorial_part2").substring(201), (2+14+2)*deltaW, 50*deltaH - 11*deltaH - 9*layout.height);
        }
        Strat7.font.getData().setScale(Strat7.normalFontSize);
    }
*/
    @Override
    protected boolean callBackButton() {
        rulesForButtons[BACK_BUTTON].run();
        return true;
    }
}

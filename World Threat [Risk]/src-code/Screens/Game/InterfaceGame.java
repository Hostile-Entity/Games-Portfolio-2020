
package com.strat7.game.Screens.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.strat7.game.GameInfo.GameState;
import com.strat7.game.Interfaces.Basics.*;
import com.strat7.game.Interfaces.Basics.BoundBasics.Carcass;
import com.strat7.game.Interfaces.Basics.Text.PlainText;
import com.strat7.game.Strat7;

/**
 * Created by Юра on 11.06.2017.
 */

public class InterfaceGame extends Interface {
    public static final int MAIN_BUTTON = 0;
    public static final int PAUSE_BUTTON = 1;

    public static final int SHOW_CONTINENTS_BUTTON = 2;
    public static final int SHOW_CIRCLES_BUTTON = 3;

    private GameState state;

    private BoundTexture menuBar;
    private BoundTexture[] turn;

    private PlainText fps;
    private PlainText stateName;

    public InterfaceGame(Carcass screen, GameState state) {
        super(screen, FULL_SCREEN_INTERFACE);
        scale = 1f;

        this.state = state;
        buttonAmount = SHOW_CIRCLES_BUTTON + 1 ;
        buttonArray = new  Button[buttonAmount];
        rulesForButtons = new Runnable[buttonAmount];

        int shift;

        menuBar = new BoundTexture(this, Strat7.graphics.getTextureByName("Interface/Menu_bar.png"), 0, 0, screenWidth, 7 * deltaH, 1,Color.WHITE);
        turn = new BoundTexture[state.getPlayersIdList().getActivePlayersAmount()];
        turn[0] = new BoundTexture(this, Strat7.graphics.getTextureByName("Interface/Turn.png"),deltaH,deltaH,5*deltaH, 5*deltaH, 1,Color.WHITE);
        for(int i = 1; i < turn.length; i ++) {
            turn[i] = new BoundTexture(this, Strat7.graphics.getTextureByName("Interface/Turn.png"),deltaH + i*5*deltaH,deltaH,5*deltaH, deltaH, 1,Color.WHITE);
        }
        fps = new PlainText(turn[0], deltaH,deltaH, 3 * deltaH, 3 * deltaH,"",Color.BLACK);
        stateName = new PlainText(this, deltaH * (1 + 5 + 1), 2f * deltaH, 20 * deltaH, 3 * deltaH,"", Color.BLACK);


        //Main button
        buttonArray[MAIN_BUTTON] = new  Button(
                this,
                50*deltaW - deltaW * 8 - deltaW , deltaH,
                deltaW*8, 5*deltaH,
                "Interface/Button1.png", Color.GRAY, "Interface/Button2.png"
        );
        buttonArray[MAIN_BUTTON].setVisible(true);

        //pause Button
        buttonArray[PAUSE_BUTTON] = new Button(
                this,
                50*deltaW - 4*deltaH -deltaW, 50*deltaH - 4*deltaH - deltaH, 4*deltaH, 4*deltaH, "Interface/Button1.png",
                new BoundTexture(null,Strat7.graphics.getTextureByName("Icons/Pause.png"),0,0,4*deltaH, 4*deltaH,1,Color.WHITE),
                Strat7.buttonColor, Strat7.buttonColor, "Interface/Button2.png");
        buttonArray[PAUSE_BUTTON].setVisible(true);



        buttonArray[SHOW_CONTINENTS_BUTTON] = new  Button(
                this,
                deltaW, 50*deltaH - 4*deltaH - deltaH, 4*deltaH, 4*deltaH,  "Interface/Button1.png",
                new BoundTexture(null,Strat7.graphics.getTextureByName("Icons/Continent.png"),4*deltaH/11/2,0,4*deltaH - 4*deltaH/11,4*deltaH - 4*deltaH/11, 1,Color.WHITE),
                Strat7.buttonColor, Strat7.buttonColor,  "Interface/Button2.png");
        buttonArray[SHOW_CONTINENTS_BUTTON].setVisible(true);

        buttonArray[SHOW_CIRCLES_BUTTON] = new  Button(
                this,
                deltaW + 4*deltaH + deltaW, 50*deltaH - 4*deltaH - deltaH, 4*deltaH, 4*deltaH,"Interface/Button1.png",
                new BoundTexture(null,Strat7.graphics.getTextureByName("Icons/Circle.png"),4*deltaH/11/2,0,4*deltaH - 4*deltaH/11,4*deltaH - 4*deltaH/11, 1,Color.WHITE),
                Strat7.buttonColor, Strat7.buttonColor, "Interface/Button2.png");
        buttonArray[SHOW_CIRCLES_BUTTON].setVisible(true);
    }

    @Override
    protected void commonDraw() {
        int slider = state.getPlayersIdList().getCurrentPlayerID();
        int i = 0;
        menuBar.draw(false);

        while(true){
            if (state.getPlayersIdList().getActivePlayer(slider).getAvailableProvinces() > 0) {
                turn[i].setColor(state.getPlayersIdList().getPlayerColor(slider));
                turn[i].draw(false);
                i++;
            }
            slider++;
            if (slider == state.getPlayersIdList().getActivePlayersAmount())
                slider = 0;
            if (slider == state.getPlayersIdList().getCurrentPlayerID())
                break;
        }

        //FPS
        fps.setText(Integer.toString(Gdx.graphics.getFramesPerSecond()));
        fps.draw(false);

        //Current state
        stateName.setText(state.getStateName());
        stateName.draw(false);

        //print Buttons
        for (i = 0; i < 2; i++) {
            buttonArray[i].draw(false);
        }
        buttonArray[SHOW_CONTINENTS_BUTTON].draw(false);
        buttonArray[SHOW_CIRCLES_BUTTON].draw(false);
    }

    @Override
    public void setRuleForButton(Runnable ruleForButton, int number){
        rulesForButtons[number] = ruleForButton;
    }

    @Override
    public boolean outOfBorders(int X, int Y) {
        return true;
    }
}

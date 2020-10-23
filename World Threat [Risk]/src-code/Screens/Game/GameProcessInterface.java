package com.strat7.game.Screens.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.strat7.game.GameInfo.GameState;
import com.strat7.game.GameInfo.PictureChanges.CameraMovement;
import com.strat7.game.GameInfo.ProvincesList;
import com.strat7.game.Interfaces.Basics.BoundBasics.Carcass;
import com.strat7.game.Interfaces.Basics.BoundTexture;
import com.strat7.game.Interfaces.Basics.Button;
import com.strat7.game.Interfaces.Basics.BoundBasics.Frame;
import com.strat7.game.Interfaces.Basics.Interface;
import com.strat7.game.Interfaces.Basics.MovableRegion;
import com.strat7.game.Strat7;


public class GameProcessInterface extends Interface {
    public final static int WHOLE_MAP_REGION = 0;

    public final int OCEAN ;
    public final int PROVINCE_AMOUNT;
    public final int CIRCLES_AMOUNT;

    public final int FIRST_PROVINCE ;
    public final int FIRST_CIRCLE;

    protected BoundTexture gameMap;
    protected BoundTexture links;

    class OceanButton extends Button {
        OceanButton() {
            super(MAIN_FRAME,0,0,0,0,null);
        }
        @Override
        public boolean isPressed(double X, double Y) {
            return true;
        }
        @Override
        protected void commonDraw() {
        }
        @Override
        protected void commonDrawChooseButton() {
        }
        @Override
        protected void commonDrawText() {
            super.commonDrawText();
        }
    }

    public GameProcessInterface (Carcass screen, GameState state) {
        super(screen, FULL_SCREEN_INTERFACE);
        ProvincesList provincesList = state.getProvincesArray();
        CIRCLES_AMOUNT = provincesList.getContinentAmount();
        PROVINCE_AMOUNT = provincesList.getProvinceAmount();
        OCEAN = 0;
        FIRST_PROVINCE = OCEAN + 1;
        FIRST_CIRCLE = PROVINCE_AMOUNT + 1;

        buttonAmount = CIRCLES_AMOUNT + PROVINCE_AMOUNT + 1;
        buttonArray = new Button[buttonAmount];
        rulesForButtons = new Runnable[buttonAmount];

        movableRegionAmount = 1;
        movableRegions = new MovableRegion[movableRegionAmount];
        movableRegions[0] = new MovableRegion(this,0, 7 * Frame.deltaH, Frame.screenWidth , Frame.screenHeight - 7 * Frame.deltaH);
        movableRegions[0].getCameraMovement().setResolutions(true,true,true,true);

        gameMap = new BoundTexture(movableRegions[0].getCameraMovement(),provincesList.getGameMapMain(),0,0,1,Color.WHITE);
        links = new BoundTexture(gameMap,provincesList.getGameMap(),0,0,1,Color.BLACK);
        movableRegions[0].setObjectParameters(gameMap, CameraMovement.AUTO_POSITION);


        int shift = FIRST_PROVINCE;
        for(int i = 0; i < PROVINCE_AMOUNT; i ++) {
            buttonArray[i + shift] = provincesList.get(i);
            buttonArray[i + shift].boundWithNewCarcass(gameMap,buttonArray[i + shift].getLocalPosX(),buttonArray[i + shift].getLocalPosY(), 1);
        }

        shift = FIRST_CIRCLE;
        for (int i = 0; i < CIRCLES_AMOUNT; i++) {
            buttonArray[i + shift] = new Button(
                    gameMap,
                    provincesList.getCircleXArray()[i],
                    provincesList.getCircleYArray()[i],
                    gameMap.getLocalHeight()/15, gameMap.getLocalHeight()/15, "Interface/BonusCircle.png",
                    provincesList.getContinentColor(i), provincesList.getContinentColor(i));
            buttonArray[i + shift].setText(Integer.toString(provincesList.getContinentBonus()[i]));
            buttonArray[i + shift].setVisible(false);
            buttonArray[i + shift].setBlocked(true);
            buttonArray[i + shift].getText().setMaxHeight(buttonArray[i + shift].getLocalHeight() * (1 - 1/2f));
        }

        buttonArray[OCEAN] = new OceanButton();
    }

    public void showCircles() {
        int shift = FIRST_CIRCLE;
        for (int i = 0; i < CIRCLES_AMOUNT ; i++) {
            buttonArray[i + shift].setVisible(!buttonArray[i + shift].isVisible());
            buttonArray[i + shift].setBlocked(!buttonArray[i + shift].isVisible());
        }
    }

    @Override
    protected void commonDraw() {
        links.draw(false);
        gameMap.draw(false);

        int shift = FIRST_PROVINCE;
        if(pressedButton >= shift && pressedButton < shift + PROVINCE_AMOUNT) {
            buttonArray[pressedButton].draw(false) ;
        }
        for(int i = 0; i < PROVINCE_AMOUNT; i ++) {
            if(i+shift == pressedButton)
                continue;
            buttonArray[i + shift].draw(false);
        }

        Strat7.font.setColor(Color.BLACK) ;

        for(int i = 0; i < PROVINCE_AMOUNT; i ++) {
            buttonArray[i + shift].drawText(false);
        }

        shift = FIRST_CIRCLE;
        for (int i = 0; i < CIRCLES_AMOUNT; i++) {
            buttonArray[i + shift].draw(false);
        }

    }

    @Override
    public boolean outOfBorders(int X, int Y) {
        return true;
    }

    @Override
    protected boolean callBackButton() {
        rulesForButtons[OCEAN].run();
        return true;
    }
}

package com.strat7.game.Screens.Game.Stats;

import com.badlogic.gdx.graphics.Color;
import com.strat7.game.GameInfo.GameState;
import com.strat7.game.GameInfo.PictureChanges.CameraMovement;
import com.strat7.game.GameInfo.PlayersList;
import com.strat7.game.GameInfo.ProvincesList;
import com.strat7.game.GameInfo.StatTracker;
import com.strat7.game.Interfaces.Basics.BoundBasics.Carcass;
import com.strat7.game.Interfaces.Basics.BoundBasics.Frame;
import com.strat7.game.Interfaces.Basics.BoundTexture;
import com.strat7.game.Interfaces.Basics.Button;
import com.strat7.game.Interfaces.Basics.Interface;
import com.strat7.game.Interfaces.Basics.MovableRegion;
import com.strat7.game.Interfaces.Buttons.ButtonWitchTick;
import com.strat7.game.Strat7;

/**
 * Created by Евгений on 03.10.2017.
 */

public class StatsInterface extends Interface {
    public final static Frame BIG_SIZE = new Frame(3 * deltaW, 3 * deltaH, 44 * deltaW, 44 * deltaH, 1);

    public final static int ACTIVE_TROOPS = 0;
    public final static int ALL_BUTTON = 1;
    public final static int FIRST_PLAYER = 2;

    public static final int BACK_BUTTON = FIRST_PLAYER + PlayersList.MAX_PLAYER_AMOUNT;
    private static final int STAT_SHOWER = 0;


    private final BoundTexture background;
    private StatShower statShower;

    public StatsInterface (Carcass screen, StatTracker statTracker) {
        super(screen, BIG_SIZE);

        background = new BoundTexture(this, Strat7.graphics.getTextureByName("Interface/SmallMenu.png"), 0,0,getLocalWidth(),getLocalHeight(),1,Strat7.backColor);

        buttonAmount = 2 + PlayersList.MAX_PLAYER_AMOUNT;
        buttonArray = new Button[buttonAmount];
        movableRegionAmount = 1;
        movableRegions = new MovableRegion[movableRegionAmount];

        buttonArray[ACTIVE_TROOPS] = new ButtonWitchTick(this, BIG_SIZE.getLocalWidth() -(13 + 1) * deltaW, (2.25) * deltaH, 13 * deltaW, 4 *deltaH,Color.GRAY, Color.RED);
        buttonArray[ACTIVE_TROOPS].setText("Active Troops");
        buttonArray[ACTIVE_TROOPS].setVisible(true);
        buttonArray[ALL_BUTTON] = new ButtonWitchTick(this, deltaW, BIG_SIZE.getLocalHeight() - (2.25 + 3.5) * deltaH, 10 * deltaW, 3.5 *deltaH, Color.GRAY, Color.RED);
        buttonArray[ALL_BUTTON].setVisible(true);
        buttonArray[ALL_BUTTON].setText("ALL");

        int shift = FIRST_PLAYER;
        for(int i = 0; i < PlayersList.MAX_PLAYER_AMOUNT; i ++) {
            buttonArray[shift + i] = new ButtonWitchTick(this, deltaW,buttonArray[shift + i - 1].getLocalPosY() - (3.5 + 1)* deltaH, 10 * deltaW, 3.5 *deltaH, Color.GRAY, Color.RED);
            buttonArray[shift + i].setText("Player");
            buttonArray[shift + i].setVisible(true);
        }

        rulesForButtons = new Runnable[2 + PlayersList.MAX_PLAYER_AMOUNT + 1];

        movableRegions[STAT_SHOWER] = statShower = new StatShower(this,(1 + 10 + 1) * deltaW, (2.25 + 4 + 1) * deltaH, BIG_SIZE.getLocalWidth() - (1 + 10 + 1 + 1) * deltaW, BIG_SIZE.getLocalHeight() - (2.25 + 4 + 1 + 2.25) * deltaH, statTracker);

        initDrawables();
    }

    @Override
    public void activate() {
        super.activate();
        for(MovableRegion movableRegion :movableRegions)
            movableRegion.getCameraMovement().refresh();
        statShower.activate();
    }

    private void initDrawables() {
        addDrawable(background,false);
        for(Button button :buttonArray)
            addDrawable(button,false);
        addDrawable(statShower, false);
    }

    @Override
    protected boolean callBackButton() {
        rulesForButtons[BACK_BUTTON].run();
        return true;
    }

    @Override
    protected void commonDraw() {
        super.commonDraw();
    }

    @Override
    protected void runRule(int pressedButton) {
        if(pressedButton == ALL_BUTTON) {
            final int shift = FIRST_PLAYER;
            boolean activate = false;
            for(int i = 0; i < PlayersList.MAX_PLAYER_AMOUNT; i ++){
                if(!((ButtonWitchTick)buttonArray[shift + i]).isActive()) {
                    activate = true;
                    break;
                }
            }
            for(int i = 0; i < PlayersList.MAX_PLAYER_AMOUNT; i ++){
                if(activate)
                    ((ButtonWitchTick)buttonArray[shift + i]).activate();
                else
                    ((ButtonWitchTick)buttonArray[shift + i]).deactivate();
            }
        }
        ((ButtonWitchTick)buttonArray[pressedButton]).pressed();
        if(pressedButton - FIRST_PLAYER < PlayersList.MAX_PLAYER_AMOUNT && pressedButton >= FIRST_PLAYER) {
            boolean activated = true;
            int shift = FIRST_PLAYER;
            for(int i = 0; i < PlayersList.MAX_PLAYER_AMOUNT; i ++){
                if(!((ButtonWitchTick)buttonArray[shift + i]).isActive()) {
                    activated = false;
                    break;
                }
            }
            if(activated)
                ((ButtonWitchTick)buttonArray[ALL_BUTTON]).activate();
            else
                ((ButtonWitchTick)buttonArray[ALL_BUTTON]).deactivate();

        }
    }
}

package com.strat7.game.Tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.strat7.game.GameInfo.Player;
import com.strat7.game.GameInfo.Province;
import com.strat7.game.GameInfo.ProvincesList;
import com.strat7.game.Interfaces.Basics.BoundBasics.Carcass;
import com.strat7.game.Interfaces.Basics.BoundBasics.Frame;
import com.strat7.game.Strat7;

/**
 * Created by Евгений on 17.08.2017.
 */

public class TutorialProvince extends Province {
    private boolean chosenOne;
    private float chosenOneTime;

    TutorialProvince(Carcass carcass, int xPos, int yPos, Player player, String texture, ProvincesList provincesList) {
        super(carcass,xPos,yPos,player,texture, provincesList);
    }

    @Override
    public void commonDraw() {
        if(!chosenOne) {
            super.commonDraw();
            return;
        }
        boolean chosen = isChosen();
        boolean lightened = isLightened();
        double lightenedPassed = lightenedTime;
        double chosenPassed    = chosenTime;

        super.setChosen(false);
        super.setLightened(false);
        super.commonDraw();

        Strat7.getCurrentBatch().setColor(0, 0, 0, Math.abs(chosenOneTime) / 2);
        background.draw(false);
        chosenOneTime += Gdx.graphics.getDeltaTime();
        if(chosenOneTime >= 1)
            chosenOneTime = -1;

        setChosen(chosen);
        setLightened(lightened);
        lightenedTime = lightenedPassed;
        chosenTime = chosenPassed ;

    }

    public void setChosenOne(boolean chosenOne) {
        chosenOneTime = 0;
        this.chosenOne = chosenOne;
    }
    public boolean isChosenOne() {
        return chosenOne;
    }

    public boolean isInRightTopCorner() {
        return ((getPosX() + getLocalCenterX() * getScale()) > Frame.screenWidth / 2 && (getPosY() + getLocalCenterY() * getScale()) > Frame.screenHeight / 2);
    }
}

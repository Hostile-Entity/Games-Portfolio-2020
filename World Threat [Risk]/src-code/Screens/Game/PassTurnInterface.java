package com.strat7.game.Screens.Game;

import com.strat7.game.GameInfo.GameState;
import com.strat7.game.Interfaces.Basics.BoundBasics.Carcass;
import com.strat7.game.Interfaces.Basics.BoundBasics.Frame;
import com.strat7.game.Interfaces.Basics.BoundTexture;
import com.strat7.game.Interfaces.InfoWindowInterface;
import com.strat7.game.Strat7;

/**
 * Created by Юра on 03.09.2017.
 */

public class PassTurnInterface extends InfoWindowInterface{

    protected GameState state;

    protected BoundTexture playerColor;

    public PassTurnInterface(Carcass screen, Frame size, String mainText, String leftButtonText, String rightButtonText, GameState state) {
        super(screen, size, mainText, leftButtonText, rightButtonText);
        this.state = state;

        playerColor = new BoundTexture(this, Strat7.graphics.getTextureByName("Interface/Turn.png"), getLocalWidth()/2 - 5*deltaH/2, getLocalHeight() * 14/15, 5*deltaH,5*deltaH, 1,state.getPlayersIdList().getCurrentPlayer().getColor());
    }

    @Override
    public void commonDraw() {
        super.commonDraw();
        playerColor.setColor(state.getPlayersIdList().getCurrentPlayer().getColor());
        playerColor.draw(false);
    }

}


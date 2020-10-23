package com.strat7.game.Screens.Game.Stats;

import com.badlogic.gdx.graphics.Color;
import com.strat7.game.GameInfo.StatTracker;
import com.strat7.game.Interfaces.Basics.BoundBasics.BoundWithFrame;
import com.strat7.game.Interfaces.Basics.BoundBasics.Carcass;
import com.strat7.game.Interfaces.Basics.BoundBasics.Frame;
import com.strat7.game.Interfaces.Basics.BoundTexture;
import com.strat7.game.Interfaces.Basics.Drawable;
import com.strat7.game.Interfaces.Basics.DrawableList;
import com.strat7.game.Interfaces.Basics.MovableRegion;
import com.strat7.game.Interfaces.Basics.Text.BoundText;
import com.strat7.game.Strat7;

/**
 * Created by Евгений on 10.10.2017.
 */

public class StatShower extends MovableRegion implements Drawable {
    boolean hided;
    boolean consider;

    protected StatTracker statTracker;

    protected DrawableList background;
    protected DrawableList playerTurns;

    protected int turnsAmount = 0;

    public StatShower(Frame frame, double l,double  b,double  w,double  h, StatTracker statTracker){
        super(frame, l,b,w,h);
        this.statTracker = statTracker;
        BoundTexture layoutBackgroundTop    = new BoundTexture(this, Strat7.graphics.getTextureByName("Interface/BackForText.png"), 0, 3 * 2 * deltaH,getLocalWidth(), getLocalHeight() - 3 * 2 * deltaH, 1, new Color(0.95f, 1,1,1));
        BoundTexture layoutBackgroundBottom = new BoundTexture(this, Strat7.graphics.getTextureByName("Interface/BackForText.png"), 0, 0,getLocalWidth(), 3 * 2 * deltaH, 1, new Color(Color.BLACK));
        System.out.println(layoutBackgroundTop );
        System.out.println(layoutBackgroundBottom );
        background = new DrawableList(this, this);
        background.addDrawable(layoutBackgroundTop);
        background.addDrawable(layoutBackgroundBottom);
        playerTurns = new DrawableList(this,this);
    }

    public void activate(){
        playerTurns.clear();
        int playersTurnsAmount;
        int cycleAmount;
        BoundTexture turn;
        for(StatTracker.AllPlayersStats stat :statTracker.getStats()){

        }
    }

    @Override
    public void draw(boolean considerBorders) {
        if (isHided())
            return;
        if (considerBorders) {
            Frame absolute = this;
            Strat7.openBufferStream();
            draw(false);
            Strat7.closeBufferStream();
            Strat7.flushBuffer(absolute);
        } else {
            commonDraw();
        }
    }

    public void commonDraw() {
        background.draw(false);
    }


    @Override
    public void hide() {
        setHided(true);
    }

    @Override
    public void show() {
        setHided(false);
    }

    @Override
    public boolean getConsider() {
        return consider;
    }

    @Override
    public void setConsider(boolean considerBorders) {
        this.consider = considerBorders;
    }

    public void setHided(boolean hided) {
        this.hided = hided;
    }

    public boolean isHided() {
        return hided;
    }
}

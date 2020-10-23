package com.strat7.game.Interfaces.InterfaceStacks;

import com.badlogic.gdx.Gdx;
import com.strat7.game.Interfaces.Basics.BoundBasics.BoundWithFrame;
import com.strat7.game.Interfaces.Basics.BoundBasics.Frame;
import com.strat7.game.Screens.Manager.AppearParameter;
import com.strat7.game.Screens.Manager.ProcessingArea;


public class AppearanceParameters extends AppearParameter{
    public static final int  FROM_LEFT   = 1;
    public static final int  FROM_TOP    = 2;
    public static final int  FROM_RIGHT  = 3;
    public static final int  FROM_BOTTOM = 4;

    public AppearanceParameters(BoundWithFrame appearing, double animationTime, int appearType) {
        super(appearing, animationTime,animationTime, appearType, appearType);
    }
    public AppearanceParameters(BoundWithFrame appearing, double animationTime, int appearType, int disappearType) {
        super(appearing, animationTime,animationTime, appearType, disappearType);
    }

    public AppearanceParameters(BoundWithFrame appearing) {
        super(appearing, LIGHTENING,LIGHTENING, AUTO, AUTO);
        setAppearType(FromWhere());
    }

    protected void setAppearPoints() {
        super.setAppearPoints();
        switch (getAppearType()) {
            case FROM_LEFT:
                appearStart.setPosX(-appearStart.getLocalWidth());
                break;
            case FROM_TOP:
                appearStart.setPosY(getObject().getMainFrame().getLocalHeight());
                break;
            case FROM_RIGHT:
                appearStart.setPosX(getObject().getMainFrame().getLocalWidth());
                break;
            case FROM_BOTTOM:
                appearStart.setPosY(-appearStart.getLocalHeight());
                break;

            default:
                break;
        }
    }

    protected void setDisappearPoints() {
        super.setDisappearPoints();
        switch (getDisappearType()) {
            case FROM_LEFT:
                disappearTarget.setPosX(-disappearStart.getLocalWidth());
                break;
            case FROM_TOP:
                disappearTarget.setPosY(disappearStart.getMainFrame().getLocalHeight());
                break;
            case FROM_RIGHT:
                disappearTarget.setPosX(disappearStart.getMainFrame().getLocalWidth());
                break;
            case FROM_BOTTOM:
                disappearTarget.setPosY(-disappearStart.getLocalHeight());
                break;

            default:
                break;
        }
    }

    @Override
    public void draw(ProcessingArea anInterface, float delta, boolean considerBorders) {
        if(isAnimated()) {
            incTimePassed(delta);
            setInterimPosition();
        }

        if(!isHiding())
            anInterface.draw(considerBorders);
    }


}


package com.strat7.game.Interfaces;

import com.badlogic.gdx.Gdx;
import com.strat7.game.GameInfo.PictureChanges.CameraMovement;
import com.strat7.game.Interfaces.Basics.BoundBasics.BoundWithFrame;
import com.strat7.game.Interfaces.Basics.BoundBasics.Frame;
import com.strat7.game.Interfaces.Basics.MovableRegion;
import com.strat7.game.Screens.Manager.AppearParameter;

import java.util.ArrayList;

/**
 * Created by Евгений on 20.09.2017.
 */

public class SlidingList extends MovableRegion {
    public static final int X_COORDINATE = 1;
    public static final int Y_COORDINATE = 1 << 1;
    public static final int CENTER_IN_FIRST_BUTTON = 1 << 2;

    private static final double MOVE_TIME = AppearParameter.FAST;
    private static final double FREE_SLIDE_TIME = 0.75;
    private static final double TOTAL_SLIDE_TIME = CameraMovement.SLIDE_TIME / (double) 1000000000;


    public static final int BOUND = 1 << 2;
    public static final int LOCAL = 1 << 3;

    private int currentField = -1;

    private class doublePair {
        double x;
        double y;
        doublePair(double a, double b) {
            this.x = a; this.y = b;
        }
    }

    private boolean moving;
    private int moveTo = -1;
    private BoundWithFrame moveFrom;
    private double moveTime = 0;

    private int slideTo   = -1;


    private int parameters = 0;
    private ArrayList<BoundWithFrame> list;

    public SlidingList(Frame frame, double l, double b, double w, double h, ArrayList<BoundWithFrame> list, int parameters) {
        super(frame, l, b, w, h);
        this.list = list;
        this.parameters = parameters;
        if (list.size() == 0)
            return;
        BoundWithFrame bound = createFrame(list, parameters | BOUND | LOCAL);
        getCameraMovement().setResolutions(
                (parameters & X_COORDINATE) != 0,
                (parameters & Y_COORDINATE) != 0,
                false,
                true
        );
        super.setObjectParameters(bound, (parameters & X_COORDINATE) == 1 ? CameraMovement.FIT_IN_Y_BORDERS : CameraMovement.FIT_IN_X_BORDERS);
        if ((parameters & CENTER_IN_FIRST_BUTTON) != 0) {
            setCenterX(getLocalOffsetX(list.get(0).getCenterX()));
            setCenterY(getLocalOffsetY(list.get(0).getCenterY()));
        } else
            setUpCenter();

        setUpPos(0);
    }

    public BoundWithFrame createFrame(ArrayList<BoundWithFrame> list,  int parameters) {
        double width = 0, height = 0;
        double maxWidth = -1, maxHeight = -1;
        BoundWithFrame bound = new BoundWithFrame(null, 0,0,0,0,1);

        //todo get max height / width
        for(BoundWithFrame boundWithFrame :list){
            double value;
            if((parameters & LOCAL) != 0) {
                if((parameters & X_COORDINATE) != 0) {
                    value = boundWithFrame.getLocalScaledHeight();
                    if(maxHeight < value)
                        maxHeight = value;
                }
                else {
                    value = boundWithFrame.getLocalScaledWidth();
                    if(maxWidth < value)
                        maxWidth = value;
                }
            }
            else {
                if ((parameters & X_COORDINATE) != 0) {
                    value = boundWithFrame.getHeight();
                    if(maxHeight < value)
                        maxHeight = value;
                }
                else {
                    value = boundWithFrame.getWidth();
                    if (maxHeight < value)
                        maxHeight = value;
                }
            }
        }

        for(BoundWithFrame boundWithFrame :list){

            if ((parameters & BOUND) != 0) {
                if((parameters & X_COORDINATE) != 0) {
                    if((parameters & LOCAL) != 0) {
                        boundWithFrame.boundWithNewCarcass(bound, width, 0, maxHeight / (boundWithFrame.getLocalScaledHeight()) /*scale relatively max height!!! */);
                    }
                    else {
                        boundWithFrame.boundWithNewCarcass(bound, width, 0, maxHeight / boundWithFrame.getHeight()/*scale relatively max height!!! */);
                    }

                }
                else {
                    if((parameters & LOCAL) != 0) {
                        boundWithFrame.boundWithNewCarcass(bound, 0, height, maxWidth / (boundWithFrame.getLocalScaledWidth())/*scale relatively max width!!! */);
                    }
                    else {
                        boundWithFrame.boundWithNewCarcass(bound, 0, height, maxWidth / boundWithFrame.getWidth()/*scale relatively max width!!! */);
                    }
                }
            }


            if((parameters & LOCAL) != 0) {
                if((parameters & X_COORDINATE) != 0)
                    width += boundWithFrame.getLocalScaledWidth();
                else
                    height += boundWithFrame.getLocalScaledHeight();
            }
            else {
                if ((parameters & X_COORDINATE) != 0)
                    width += boundWithFrame.getWidth();
                else
                    height += boundWithFrame.getHeight();
            }
        }


        if ((parameters & BOUND) != 0) {
            if((parameters & X_COORDINATE) != 0) {
                bound.setParameters(0,0,width,maxHeight);
            }
            else {
                bound.setParameters(0,0,maxWidth,height);
            }
        }
        return bound;
    }

    protected void setUpPos(int num) {

    }


    public ArrayList<BoundWithFrame> getList() {
        return list;
    }

    public int getParameters() {
        return parameters;
    }
}

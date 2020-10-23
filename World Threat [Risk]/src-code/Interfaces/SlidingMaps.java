package com.strat7.game.Interfaces;

import com.badlogic.gdx.Gdx;
import com.strat7.game.GameInfo.PictureChanges.CameraMovement;
import com.strat7.game.Interfaces.Basics.BoundBasics.BoundWithFrame;
import com.strat7.game.Interfaces.Basics.BoundBasics.Frame;
import com.strat7.game.Screens.Manager.AppearParameter;
import java.util.ArrayList;


public class SlidingMaps extends SlidingList {

    private int currentField = -1;

    private boolean moving;
    private int moveTo = -1;
    private double moveTime = 0;

    private int slideTo   = -1;

    private static final double MOVE_TIME = AppearParameter.FAST;
    private static final double FREE_SLIDE_TIME = 0.75;
    private static final double TOTAL_SLIDE_TIME = CameraMovement.SLIDE_TIME / (double) 1000000000;

    public SlidingMaps (Frame frame, double l, double b, double w, double h, ArrayList<BoundWithFrame> list, int parameters) {
        super(frame,l,b,w,h,list,parameters);
        setUpFieldAndCenterPos(0);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(!isMoving())
            return super.touchDown(screenX, screenY, pointer, button);
        else
            return contains(screenX,screenY);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (!isMoving())
            return super.touchDragged(screenX, screenY, pointer);
        else
            return contains(screenX, screenY);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(!isMoving())
            return super.touchUp(screenX, screenY, pointer, button);
        else
            return contains(screenX,screenY);
    }

    @Override
    public void slideForward(int framesPerSecond) {
        if(!isMoving()) {
            super.slideForward(framesPerSecond);
            double dif = (System.nanoTime() - getCameraMovement().getVelocity().getTime()) / (double) CameraMovement.SLIDE_TIME;
            if( dif > FREE_SLIDE_TIME && dif < TOTAL_SLIDE_TIME ) {
                if(getSlideTo() == -1) {
                    setSlideTo(getCurrentField());
                }
                getCameraMovement().setPosX(getCameraMovement().getLocalPosX() - (getList().get(getSlideTo()).getCenterX() - getCenterX()) / getScale() * getCameraMovement().getLocalScale() * (dif - FREE_SLIDE_TIME) / (TOTAL_SLIDE_TIME - FREE_SLIDE_TIME));
                getCameraMovement().setPosY(getCameraMovement().getLocalPosY() - (getList().get(getSlideTo()).getCenterY() - getCenterY()) / getScale() * getCameraMovement().getLocalScale() * (dif - FREE_SLIDE_TIME) / (TOTAL_SLIDE_TIME - FREE_SLIDE_TIME));
            }
            else {
                setSlideTo(-1);
            }
        }
        else {
            incMoveTime(Gdx.graphics.getDeltaTime());
            if(moveTime >= MOVE_TIME) {
                moveTime = MOVE_TIME;
                setMoving(false);
            }
            System.out.println((getList().get(getMoveTo()).getCenterX() - getCenterX()) * moveTime / MOVE_TIME / getScale() * getCameraMovement().getLocalScale() );
            if((getParameters() & X_COORDINATE) != 0) {
                getCameraMovement().setPosX(getCameraMovement().getLocalPosX() - (getList().get(getMoveTo()).getCenterX() - getCenterX()) / getScale() * getCameraMovement().getLocalScale() * moveTime / MOVE_TIME );
            }
            if((getParameters() & Y_COORDINATE) != 0) {
                getCameraMovement().setPosY(getCameraMovement().getLocalPosY() - (getList().get(getMoveTo()).getCenterY() - getCenterY()) / getScale() * getCameraMovement().getLocalScale() * moveTime / MOVE_TIME );
            }

        }
        initPosition();
    }


    public void moveTo(int frameNum) {
        getCameraMovement().setVelocity(0,0,0);
        setMoveTo(frameNum);
        setMoveTime(0);
        setMoving(true);
    }
    public void moveToNext(){
        moveTo((getCurrentField() + 1) % getList().size());
    }
    public void moveToPrevious(){
        moveTo((getCurrentField() + getList().size() - 1) % getList().size());
    }

    public int getCurrentField() {
        return currentField;
    }
    public void setCurrentField(int currentField) {
        this.currentField = currentField;
    }

    protected boolean isMoving() {
        return moving;
    }
    protected void setMoving(boolean moving) {
        this.moving = moving;
    }

    protected int getMoveTo() {
        return moveTo;
    }
    protected void setMoveTo(int moveTo) {
        this.moveTo = moveTo;
    }

    protected int getSlideTo() {
        return slideTo;
    }
    protected void setSlideTo(int slideTo) {
        this.slideTo = slideTo;
    }

    protected double getMoveTime() {
        return moveTime;
    }
    protected void setMoveTime(double moveTime) {
        this.moveTime = moveTime;
    }
    protected void incMoveTime(double deltaTime) {
        setMoveTime(getMoveTime() + deltaTime);
    }
    protected void decMoveTime(double deltaTime) {
        setMoveTime(getMoveTime() - deltaTime);
    }

    protected void initPosition() {
        int num = -1, i = 0;
        double distance = Double.POSITIVE_INFINITY;
        double value;
        for(BoundWithFrame boundWithFrame : getList()) {
            if((getParameters() & X_COORDINATE) != 0)
                value = Math.abs(getCenterX() - boundWithFrame.getCenterX());
            else
                value = Math.abs(getCenterY() - boundWithFrame.getCenterY());
            if(distance > value) {
                distance = value;
                num = i;
            }
            i ++;
        }
        setCurrentField(num);
    }
    public void setUpFieldAndCenterPos(int currentField) {
        setCurrentField(currentField);
        getCameraMovement().setPosX(getCameraMovement().getLocalPosX() - (getList().get(currentField).getCenterX() - getCenterX()) / getScale() * getCameraMovement().getLocalScale());
        getCameraMovement().setPosY(getCameraMovement().getLocalPosY() - (getList().get(currentField).getCenterY() - getCenterY()) / getScale() * getCameraMovement().getLocalScale());
    }

}

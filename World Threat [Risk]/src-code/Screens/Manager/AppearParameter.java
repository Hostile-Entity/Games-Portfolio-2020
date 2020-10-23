package com.strat7.game.Screens.Manager;

import com.badlogic.gdx.Gdx;
import com.strat7.game.Interfaces.Basics.BoundBasics.BoundWithFrame;

public abstract class AppearParameter  {
    public static final int AUTO = -1;


    public static final double LIGHTENING  = 0d;
    public static final double FAST  = 0.2d;
    public static final double NORMAL= 0.4d;
    public static final double SLOW = 0.6d;
    public static final double VERY_SLOW = 0.8d;

    private BoundWithFrame appearingFrame;
    private BoundWithFrame object;


    protected BoundWithFrame appearStart;
    protected BoundWithFrame appearTarget;
    protected BoundWithFrame disappearStart;
    protected BoundWithFrame disappearTarget;
    protected BoundWithFrame moveStart;
    protected BoundWithFrame moveTarget;


    protected double appearTime;
    protected double disappearTime;


    protected double moveTime;
    protected double timePassed;

    private double delayBeforeAppearStart;
    private double delayAfterDisappearEnd;

    private int appearType;
    private int disappearType;


    private boolean appearing;
    private boolean appeared;
    private boolean moving;
    private boolean moved;
    private boolean disappearing;
    private boolean disappeared;
    private boolean waiting;
    private boolean hiding;
    private boolean reversed;

    public AppearParameter (BoundWithFrame appearing,double appearTime, double disappearTime,int appearType, int disappearType) {
        this.appearingFrame = new BoundWithFrame(appearing);
        object = appearing;

        initState();

        setAppearType(appearType);
        setDisappearType(disappearType);
        setAppearTime(appearTime);
        setDisappearTime(disappearTime);
    }

    protected void initState() {
        setAppearing(false);
        setDisappearing(false);
        setMoving(false);
        setDisappeared(false);
        setWaiting(false);
        setHiding(false);

        setTimePassed(0);
    }

    public void appear() {
        if(isMoving())
            return;
        if(getAppearStart().equals(getDisappearTarget()) && getAppearTarget().equals(getDisappearStart()) && isDisappearing())
            setTimePassed(getAppearTime() - getTimePassed());
        else
            setTimePassed(0);
        stopAnimation();
        setAppearing(true);
        setInterimPosition();
    }
    public void disappear() {
        if(isMoving())
            return;
        if(getDisappearTarget().equals(getAppearStart()) && getDisappearStart().equals(getAppearTarget()) && isAppearing())
            setTimePassed(getDisappearTime() - getTimePassed());
        else
            setTimePassed(0);

        stopAnimation();
        setDisappearing(true);
        setInterimPosition();
    }

    protected void setAppearPoints() {
        appearStart = new BoundWithFrame(object);
        appearTarget= new BoundWithFrame(object);
        /*
        switch (getAppearType()) {
            default:
                appearStart = new BoundWithFrame(appearing);
                appearTarget= new BoundWithFrame(appearing);
                break;
        }
        */
    }
    protected void setDisappearPoints() {
        disappearStart = new BoundWithFrame(object);
        disappearTarget= new BoundWithFrame(object);
        /*
        switch (getDisappearType()) {
            default:
                disappearStart = new BoundWithFrame(disappearing);
                disappearTarget= new BoundWithFrame(disappearing);
                break;
        }
        */
    }

    public void moveTo(BoundWithFrame newPosition, double moveTime) {
        if(isAnimated())
            return;
        this.moveTime = moveTime;
        moveStart = new BoundWithFrame(object);
        moveTarget = new BoundWithFrame(newPosition);
        setTimePassed(0);
        setMoving(true);
    }

    public void moveBack() {
        if(isAnimated() && !isMoving())
            return;
        moveStart = new BoundWithFrame(object);
        moveTarget = new BoundWithFrame(getAppearingFrame());
        stopAnimation();
        setTimePassed(0);
        setMoving(true);
    }

    public void setInterimPosition() {
        BoundWithFrame interim = null;
        if(isAppearing()) {
            if (timePassed >= getAppearTime() || (interim = object).equals(getAppearTarget())) {
                interim = getAppearTarget();
                setAppearing(false);
                setReversed(false);
            }
            else
                interim = new BoundWithFrame(getAppearStart(), getAppearTarget(), getAppearTime() != 0 ? timePassed / getAppearTime() : 1);
        }

        if(isDisappearing())
            if(timePassed >= getDisappearTime()) {
                interim = getDisappearTarget();
                setDisappearing(false);
                setDisappeared(true);
                setReversed(false);
            }
            else
                interim = new BoundWithFrame(getDisappearStart(), getDisappearTarget(), getDisappearTime() != 0 ? timePassed / getDisappearTime() : 1);

        if(isMoving())
            if(timePassed >= moveTime) {
                interim = getMoveTarget();
                setAppearType(FromWhere());
                setMoving(false);
                setReversed(false);
            }
            else
                interim = new BoundWithFrame(getMoveStart(), getMoveTarget(), moveTime != 0 ? timePassed / moveTime : 1);

        object.copyLocal(interim);
    }

    public abstract void draw(ProcessingArea anInterface, float delta, boolean considerBorders);

    public void incTimePassed(double time) {
        if(!isWaiting())
            this.timePassed += time;
    }
    public void setTimePassed(double time) {
        this.timePassed = time;
    }
    public double getTimePassed() {
        return timePassed;
    }

    public void setAppearTime(double appearTime) {
        this.appearTime = appearTime;
    }
    public void setDisappearTime(double disappearTime) {
        this.disappearTime = disappearTime;
    }
    public double getAppearTime() {
        if(isReversed())
            return disappearTime;
        else
            return appearTime;
    }
    public double getDisappearTime() {
        if(isReversed())
            return appearTime;
        else
            return disappearTime;
    }

    public BoundWithFrame getAppearingFrame() {
        return appearingFrame;
    }
    public BoundWithFrame getObject() {
        return object;
    }

    public BoundWithFrame getAppearStart() {
        if(isReversed())
            return disappearTarget;
        else
            return appearStart;
    }
    public BoundWithFrame getAppearTarget() {
        if(isReversed())
            return disappearStart;
        else
            return appearTarget;
    }

    public BoundWithFrame getDisappearStart() {
        if(isReversed())
            return appearTarget;
        else
            return disappearStart;
    }
    public BoundWithFrame getDisappearTarget() {
        if(isReversed())
            return appearStart;
        else
            return disappearTarget;
    }

    public BoundWithFrame getMoveStart() {
        return moveStart;
    }
    public BoundWithFrame getMoveTarget() {
        return moveTarget;
    }

    public boolean isAppearing   () {
        return appearing;
    }
    public boolean isAppeared() {
        return appeared;
    }
    public boolean isMoving      () {
        return moving;
    }
    public boolean isMoved() {
        return moved;
    }
    public boolean isDisappearing() {
        return disappearing;
    }
    public boolean isDisappeared () {
        return disappeared;
    }
    public boolean isHiding      () {
        return hiding;
    }

    protected void setAppearing     (boolean appearing) {
        this.appearing = appearing;
    }
    protected void setAppeared(boolean appeared) {
        this.appeared = appeared;
    }
    protected void setMoving        (boolean moving) {
        this.moving = moving;
    }
    protected void setMoved(boolean moved) {
        this.moved = moved;
    }
    protected void setDisappearing  (boolean disappearing) {
        this.disappearing = disappearing;
    }
    protected void setDisappeared   (boolean disappeared) {
        this.disappeared = disappeared;
    }
    protected void setHiding(boolean hiding) {
        this.hiding = hiding;
    }


    public void setReversed(boolean reversed) {
        this.reversed = reversed;
    }
    public boolean isReversed() {
        return reversed;
    }
    public boolean reverse() {
        return (reversed = !reversed);
    }

    public int getAppearType() {
        if(isReversed()) {
            return disappearType;
        }
        else
            return appearType;
    }
    public void setAppearType(int appearType) {
        this.appearType = appearType;
        setAppearPoints();
    }

    public int getDisappearType() {
        if(isReversed())
            return appearType;
        else
            return disappearType;
    }
    public void setDisappearType(int disappearType) {
        this.disappearType = disappearType;
        setDisappearPoints();
    }

    public boolean isWaiting() {
        return waiting;
    }
    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }
    public void pause() {
        setWaiting(true);
    }
    public void resumption() {
        setWaiting(false);
    }
    public void hide() {
        setHiding(true);
    }
    public void show() {
        setHiding(false);
    }



    public boolean isAnimated() {
        return isAppearing() || isDisappearing() || isMoving();
    }

    public void stopAnimation() {
        if (!isAnimated())
            return;
        if (isAppearing()) {
            getObject().copyLocal(getAppearTarget());
            setAppearing(false);
        }
        if (isDisappearing()) {
            getObject().copyLocal(getDisappearTarget());
            setDisappearing(false);
        }
        if (isMoving()) {
            getObject().copyLocal(getMoveTarget());
            setMoving(false);
        }

    }
    public int FromWhere() {
        double distance = getObject().getPosX() + getObject().getWidth();
        int nearestEdge = 1;
        if(distance < Gdx.graphics.getWidth() - getObject().getPosX()) {
            nearestEdge = 3;
        }
        return nearestEdge;
    }

    public double getDelayBeforeAppearStart() {
        if(isReversed())
            return delayAfterDisappearEnd;
        else
            return delayBeforeAppearStart;
    }
    public double getDelayAfterDisappearEnd() {
        if(isReversed())
            return delayBeforeAppearStart;
        else
            return delayAfterDisappearEnd;
    }

    public void setDelayBeforeAppearStart(double delayBeforeAppearStart) {
        this.delayBeforeAppearStart = delayBeforeAppearStart;
    }
    public void setDelayAfterDisappearEnd(double delayAfterDisappearEnd) {
        this.delayAfterDisappearEnd = delayAfterDisappearEnd;
    }

    public void setDelays(double delayBeforeAppearStart, double delayAfterDisappearEnd) {
        this.delayBeforeAppearStart = delayBeforeAppearStart;
        this.delayAfterDisappearEnd = delayAfterDisappearEnd;
    }
}

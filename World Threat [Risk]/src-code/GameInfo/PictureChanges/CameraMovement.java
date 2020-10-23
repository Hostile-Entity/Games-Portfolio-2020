package com.strat7.game.GameInfo.PictureChanges;

import com.badlogic.gdx.Gdx;
import com.strat7.game.Interfaces.Basics.BoundBasics.BoundWithFrame;
import com.strat7.game.Interfaces.Basics.BoundBasics.Carcass;
import com.strat7.game.Interfaces.Basics.BoundBasics.Frame;

/**
 * Created by Евгений on 20.07.2017.
 */

public class CameraMovement extends BoundWithFrame {
    public static final int AUTO_POSITION    = 1;
    public static final int FIT_IN_X_BORDERS = 1 << 1;
    public static final int FIT_IN_Y_BORDERS = 1 << 2;
    public static final int X_LEFT_ALIGN =  1 << 3;
    public static final int X_RIGHT_ALIGN = 1 << 4;
    public static final int Y_TOP_ALIGN =   1 << 5;
    public static final int Y_BOTTOM_ALIGN =1 << 6;




    public static final long SLIDE_TIME = 1500000000;



    private OldPointer[] oldFingers ;
    private OldPointer[] newFingers ;


    private boolean allowMoveX = true;
    private boolean allowMoveY = true;
    private boolean allowChangeScale = true;
    private boolean allowSlideForward= true;

    private double xPosAfter;
    private double yPosAfter;

    private double scaleTopBorder = 3;
    private double scaleBottomBorder = 1;
    private int timer;

    private SpeedAndDirection velocity;


    private double xLeftBorder = 0;
    private double xRightBorder = 0;
    private double yTopBorder = 0;
    private double yBottomBorder = 0;
    private int parameters = 0;

    public CameraMovement(Carcass bounded) {
        super(bounded,0,0,bounded.getWidth(),bounded.getHeight(), 1);

        timer = 0;
        velocity = new SpeedAndDirection();
        oldFingers = new OldPointer[2];
        newFingers = new OldPointer[2];
        oldFingers[0] = new OldPointer();
        oldFingers[1] = new OldPointer();
        newFingers[0] = new OldPointer();
        newFingers[1] = new OldPointer();

    }

    public void setObjectParameters(Carcass object, int parameters){
        scale = 1;
        this.parameters = parameters;
        width = object.getLocalWidth();
        height = object.getLocalHeight();
        initScale();
        refresh();
    }

    private void initScale () {
        if((parameters & AUTO_POSITION) != 0 || ((parameters & FIT_IN_X_BORDERS) != 0 && (parameters & FIT_IN_Y_BORDERS) != 0)) {
            if (width / height > mainFrame.getLocalWidth() / mainFrame.getLocalHeight()) {
                scaleBottomBorder = mainFrame.getLocalWidth() / width;
            } else {
                scaleBottomBorder = mainFrame.getLocalHeight() / height;
            }
            scaleTopBorder = scaleBottomBorder * 4;
            return;
        }
        if((parameters & FIT_IN_X_BORDERS) != 0) {
            scaleBottomBorder = mainFrame.getLocalWidth() / width;
            scaleTopBorder = scaleBottomBorder * 4;
            return;
        }
        if((parameters & FIT_IN_Y_BORDERS) != 0) {
            scaleBottomBorder = mainFrame.getLocalHeight() / height;
            scaleTopBorder = scaleBottomBorder * 4;
            return;
        }
        parameters = AUTO_POSITION;
        initScale();
    }

    private void initBorders () {
        if(width / height > mainFrame.getLocalWidth() / mainFrame.getLocalHeight()) {
            xLeftBorder = 0;
            xRightBorder = mainFrame.getLocalWidth() - getLocalWidth() * getLocalScale();

            if(getLocalHeight()*scale < mainFrame.getLocalHeight()) {
                yBottomBorder = (mainFrame.getLocalHeight() - getLocalHeight()*scale) / 2;
                yTopBorder = yBottomBorder;
            }
            else {
                yBottomBorder = 0;
                yTopBorder = - (getLocalHeight()*scale - mainFrame.getLocalHeight()) ;
            }
        }
        else {
            yBottomBorder = 0;
            yTopBorder = mainFrame.getLocalHeight() - getLocalHeight() * getLocalScale();

            if(getLocalWidth()*scale < mainFrame.getLocalWidth()) {
                xLeftBorder  = (mainFrame.getLocalWidth() - getLocalWidth()*scale) / 2;
                xRightBorder = xLeftBorder;
            }
            else {
                xLeftBorder = 0;
                xRightBorder = - (getLocalWidth()*scale - mainFrame.getLocalWidth());
            }
        }
    }

    public void setResolutions (boolean allowMoveX, boolean allowMoveY, boolean allowChangeScale, boolean allowSlideForward) {
        this.allowMoveX = allowMoveX;
        this.allowMoveY = allowMoveY;
        this.allowChangeScale = allowChangeScale;
        this.allowSlideForward= allowSlideForward;
    }

    public boolean moved (int screenX, int screenY,int pointer) {
        double oldLeftOffset = getLocalPosX();
        double oldBottomOffset = getLocalPosY();
        double oldScale = scale;
        if (oldFingers[0].getPointerX() != -1 && oldFingers[1].getPointerX() != -1) {
            zoomed(screenX, screenY, pointer);
            if(pointer == 0)
                return false;

        }
        else {
            drag(screenX, screenY, pointer);
        }
        oldFingers = newFingers;
        newFingers = new OldPointer[2];
        newFingers[0] = new OldPointer();
        newFingers[1] = new OldPointer();

        return ((getLocalPosY() != oldBottomOffset) || (getLocalPosX() != oldLeftOffset) || (getLocalScale() != oldScale));
    }

    public void zoomed (int screenX, int screenY,int pointer ) {
        if(pointer == 0) {
            newFingers[0].set(screenX,Frame.screenHeight - screenY);
            return;
        }
        if(pointer == 1) {
            newFingers[1].set(screenX,Frame.screenHeight - screenY);
        }

        double oldDistance = oldFingers[0].getDistance(oldFingers[1].getPointerX(),oldFingers[1].getPointerY());
        double newDistance = newFingers[0].getDistance(newFingers[1].getPointerX(),newFingers[1].getPointerY());
        double scaleCoef = newDistance / oldDistance;

        if(allowChangeScale)
            scaleCoef = controlScale(scaleCoef);
        initBorders();

        xPosAfter = getLocalPosX() * scaleCoef - (
                ((oldFingers[0].getPointerX() + oldFingers[1].getPointerX())*scaleCoef - (newFingers[0].getPointerX() + newFingers[1].getPointerX())) / 2 - mainFrame.getPosX()*(scaleCoef - 1)
        ) / mainFrame.getScale();
        controlOffsetX();

        yPosAfter = getLocalPosY() * scaleCoef - (
                ((oldFingers[0].getPointerY() + oldFingers[1].getPointerY())*scaleCoef - (newFingers[0].getPointerY() + newFingers[1].getPointerY())) / 2 - mainFrame.getPosY()*(scaleCoef - 1)
        ) / mainFrame.getScale();
        controlOffsetY();


    }

    private void drag (int screenX, int screenY,int pointer) {
        newFingers[pointer].set(screenX,Frame.screenHeight - screenY);
        if (timer == 0) {
            velocity.setTime(System.nanoTime());
            timer ++;
        }
        else {
            xPosAfter = getLocalPosX() + (screenX - oldFingers[pointer].getPointerX()) / mainFrame.getScale();
            yPosAfter = getLocalPosY() + (Frame.screenHeight - screenY - oldFingers[pointer].getPointerY()) / mainFrame.getScale();
            velocity.set(
                    screenX - oldFingers[pointer].getPointerX(),
                    Frame.screenHeight - screenY - oldFingers[pointer].getPointerY(),
                    System.nanoTime()
            );

            initBorders();
            if(allowMoveX)
                controlOffsetX();
            if(allowMoveY)
                controlOffsetY();
        }
    }

    public void controlOffsetX() {
        if (xPosAfter > xLeftBorder)
            setPosX(xLeftBorder);
        else
            if (xPosAfter < xRightBorder)
                setPosX(xRightBorder);
            else
                setPosX(xPosAfter);
    }
    public void  controlOffsetY() {
        if (yPosAfter < yTopBorder)
            setPosY(yTopBorder);
        else
            if (yPosAfter  > yBottomBorder)
                setPosY(yBottomBorder);
            else
                setPosY(yPosAfter);
    }
    private double controlScale (double scaleCoef) {
        if(scaleCoef >= 1) {
            if (scale * scaleCoef < scaleTopBorder)
                scale *= scaleCoef;
            else
                scale = (scaleCoef = scaleTopBorder / scale) * scale;
        }
        else {
            if (scale * scaleCoef > scaleBottomBorder)
                scale *= scaleCoef;
            else
                scale = (scaleCoef = scaleBottomBorder / scale) * scale;
        }
        return scaleCoef;
    }

    public void slideForward(int curFPS) {
        if (oldFingers[0].getPointerX() == -1 && oldFingers[1].getPointerX() == -1 && allowSlideForward) {
            if (velocity.getDeltaTime() != 0) {
                xPosAfter = getLocalPosX();
                yPosAfter = getLocalPosY();
                long dif = System.nanoTime() - velocity.getTime();

                double speedX = velocity.getSpeedX();
                double speedY = velocity.getSpeedY();

                if (Math.sqrt(speedX * speedX + speedY * speedY) > (Frame.screenWidth + Frame.screenHeight) / 6 &&
                        dif < SLIDE_TIME) {
                    xPosAfter += (speedX * (SLIDE_TIME - dif) / SLIDE_TIME) / curFPS / mainFrame.getScale();
                    yPosAfter += (speedY * (SLIDE_TIME - dif) / SLIDE_TIME) / curFPS / mainFrame.getScale();
                }
                initBorders();
                if(allowMoveX)
                    controlOffsetX();
                if(allowMoveY)
                    controlOffsetY();
            }
        }
    }

    public double getXBias() {
        return (velocity.getSpeedX() * (SLIDE_TIME - System.nanoTime() - velocity.getTime()) / SLIDE_TIME) / Gdx.graphics.getFramesPerSecond() / mainFrame.getScale();
    }
    public double getYBias() {
        return (velocity.getSpeedY() * (SLIDE_TIME - System.nanoTime() - velocity.getTime()) / SLIDE_TIME) / Gdx.graphics.getFramesPerSecond() / mainFrame.getScale();
    }

    public void refresh() {
        setScale(scaleBottomBorder);
        initBorders();
        boolean left   = (parameters & X_LEFT_ALIGN) != 0;
        boolean right  = (parameters & X_RIGHT_ALIGN) != 0;
        boolean top    = (parameters & Y_TOP_ALIGN) != 0;
        boolean bottom = (parameters & Y_BOTTOM_ALIGN) != 0;
        if(left != right){
            if(left)
                setPosX(xLeftBorder);
            else
                setPosX(xRightBorder);
        }
        else {
            setPosX(xLeftBorder + (xRightBorder - xLeftBorder) / 2);
        }
        if(top != bottom) {
            if(top)
                setPosY(yTopBorder);
            else
                setPosY(yBottomBorder);
        }
        else {
            setPosY(yBottomBorder + (yTopBorder - yBottomBorder) / 2);
        }
    }

    public void setVelocity(double x, double y, long time) {
        if(time == 0)
            time = System.nanoTime();
        velocity.set(x,y,time);
    }

    public void setFinger(int number, int x, int y ) {
        oldFingers[number].set(x,y);
    }
    public void clearFinger(int number) {
        oldFingers[number].clear();
    }



    public SpeedAndDirection getVelocity() {
        return velocity;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public OldPointer[] getOldFinger() {
        return oldFingers;
    }

    @Override
    public double getHeight() {
        return height * getScale();
    }
    @Override
    public double getWidth() {
        return width * getScale();
    }
    /*
    @Override
    public double getPosX() {
        return mainFrame.getPosX() + leftOffset;
    }
    @Override
    public double getPosY() {
        return mainFrame.getPosY() + bottomOffset;
    }
    */
    public boolean scrolled(int amount) {
        if(allowChangeScale) {
            setScale(scale - amount / 8f);
            setPosX((double) ((int) Frame.screenWidth / 2 + (getScale() / (getScale() + amount / (double) 5)) * (getLocalPosX() - (int) Frame.screenWidth / 2)));
            setPosY((double) ((int) Frame.screenHeight / 2 + (getScale() / (getScale() + amount / (double) 5)) * (getLocalPosY() - (int) Frame.screenHeight / 2)));
            return true;
        }
        return false;
    }

    public void clearNewFingers() {
        newFingers = new OldPointer[2];
        newFingers[0] = new OldPointer();
        newFingers[1] = new OldPointer();
    }
}
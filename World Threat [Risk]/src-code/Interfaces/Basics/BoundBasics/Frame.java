package com.strat7.game.Interfaces.Basics.BoundBasics;

import com.badlogic.gdx.Gdx;

/**
 * Created by Евгений on 11.08.2017.
 */

public class Frame implements Carcass {
    public final static int screenWidth  = Gdx.graphics.getWidth();
    public final static int screenHeight = Gdx.graphics.getHeight();
    public final static double deltaW = screenWidth / 50d;
    public final static double deltaH = screenHeight / 50d;

    public final static Frame MAIN_FRAME = new Frame(0,0,screenWidth,screenHeight,1);

    protected double posX;
    protected double posY;
    protected double width;
    protected double height;

    protected double scale;
    private double centerX;
    private double centerY;

    public Frame() {
        posX = posY = width = height = -1;
        setUpCenter();
        scale = 1;
    }

    public Frame(double x,double y,double w,double h, double s) {
        posX = x;
        posY = y;
        width = w;
        height = h;
        scale = s;
        setUpCenter();
    }

    public Frame(Carcass start, Carcass target, double part) {
        if(start.getMainFrame() != target.getMainFrame())
            throw new IllegalArgumentException("Start and Target must have same main frames");
        if(part >= 1) {
            this.copyLocal(target);
            return;
        }
        if(part <= 0) {
            this.copyLocal(start);
            return;
        }

        posX  = start.getLocalPosX  () + (target.getLocalPosX  () - start.getLocalPosX  ()) * part;
        posY  = start.getLocalPosY  () + (target.getLocalPosY  () - start.getLocalPosY  ()) * part;
        width = start.getLocalWidth () + (target.getLocalWidth () - start.getLocalWidth ()) * part;
        height= start.getLocalHeight() + (target.getLocalHeight() - start.getLocalHeight()) * part;
        scale = start.getLocalScale () + (target.getLocalScale () - start.getLocalScale ()) * part;
        centerX = start.getLocalCenterX() + (target.getLocalCenterX() - start.getLocalCenterX()) * part;
        centerY = start.getLocalCenterY() + (target.getLocalCenterY() - start.getLocalCenterY()) * part;
        setMainFrame(start.getMainFrame());
    }

    @Override
    public boolean equals(Object frame) {
        if(this == frame) {
            return true;
        } else {
            if(frame instanceof Frame) {
                Frame frame1 = (Frame)frame;
                return (getMainFrame().equals(frame1.getMainFrame())
                        && getLocalHeight() == frame1.getLocalHeight()
                        && getLocalWidth() == frame1.getLocalWidth()
                        && getLocalPosX() == getLocalPosX()
                        && getLocalPosY() == getLocalPosY()
                );
            }
            return false;
        }
    }

    @Override
    public Carcass toAbsoluteFrame() {
        return this;
    }

    public Frame(Carcass start) {
        this.copyLocal(start);
    }
    public void setUpCenter() {
        setCenterX(getLocalWidth () / 2);
        setCenterY(getLocalHeight() / 2);
    }
    public void setUpCenter(double centerX, double centerY) {
        setCenterX(centerX);
        setCenterY(centerY);
    }


    @Override
    public double getWidth() {
        return width * scale;
    }
    @Override
    public double getHeight() {
        return height * scale;
    }

    @Override
    public double getLocalWidth() {
        return width;
    }
    @Override
    public double getLocalHeight() {
        return height;
    }

    @Override
    public double getScale() {
        return scale;
    }

    @Override
    public double getPosX() {
        return posX;
    }
    @Override
    public double getPosY() {
        return posY;
    }

    @Override
    public double getLocalPosX() {
        return posX;
    }
    @Override
    public double getLocalPosY() {
        return posY;
    }

    @Override
    public void setScale(double scale) {
        this.scale = scale;
    }

    @Override
    public Carcass getMainFrame() {
        return MAIN_FRAME;
    }

    public double getLocalScale() {
        return scale ;
    }

    @Override
    public Frame allowedAbsoluteRegion() {
        Frame frame = new Frame();
        frame.copyAbsolute(this);
        return frame;
    }

    public Frame allowedAbsoluteIntegerRegion() {
        Frame allowedRegion = allowedAbsoluteRegion();

        if(allowedRegion == null)
            return null;
        double right = Math.round(allowedRegion.getPosX() + allowedRegion.getWidth());
        allowedRegion.setPosX(Math.round(allowedRegion.getPosX()));
        allowedRegion.setWidth(right - allowedRegion.getPosX());

        double top = Math.round(allowedRegion.getPosY() + allowedRegion.getHeight());
        allowedRegion.setPosY(Math.round(allowedRegion.getPosY()));
        allowedRegion.setHeight(top - allowedRegion.getPosY());
        return allowedRegion;
    }

    @Override
    public Frame allowedLocalRegion() {
        Frame frame = new Frame();
        frame.copyLocal(this);
        return frame;
    }

    @Override
    public String toString() {
        return "PosX: " + getPosX() + "; " + "PosY: " + getPosY() + "; " +
                "Width: " + getWidth() + "; " + "Height: " + getHeight() + "; " +  "Scale: " + getScale() + ";" ;
    }

    @Override
    public void setPosX(double posX) {
        this.posX = posX;
    }
    @Override
    public void setPosY(double posY) {
        this.posY = posY;
    }
    @Override
    public void setWidth(double width) {
        this.width = width;
    }
    @Override
    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public double getCenterX() {
        return getPosX() + centerX * getScale();
    }
    @Override
    public double getCenterY() {
        return getPosY() + centerY * getScale();
    }

    @Override
    public double getLocalCenterX() {
        return centerX;
    }
    @Override
    public double getLocalCenterY() {
        return centerY;
    }

    @Override
    public double getLocalScaledWidth() {
        return getLocalScale() * getLocalWidth();
    }

    @Override
    public double getLocalScaledHeight() {
        return getLocalScale() * getLocalHeight();
    }

    @Override
    public double getLocalScaledCenterX() {
        return getLocalScale() * getLocalCenterX();
    }

    @Override
    public double getLocalScaledCenterY() {
        return getLocalScale() * getLocalCenterY();
    }

    @Override
    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }
    @Override
    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }

    public void setParameters(double x, double y, double w, double h){
        posX = x;
        posY = y;
        width = w;
        height = h;
    }
    public void setParameters(double x, double y, double w, double h, double scale){
        setParameters(x,y,w,h);
        this.scale = scale;
    }

    public void copyAbsolute(Carcass target) {
        setMainFrame(null);
        posX = target.getPosX();
        posY = target.getPosY();
        width = target.getWidth();
        height= target.getHeight();
        centerX = target.getCenterX();
        centerY = target.getCenterY();
        scale = 1;
    }

    public void copyLocal(Carcass target) {
        setMainFrame(target.getMainFrame());
        posX = target.getLocalPosX();
        posY = target.getLocalPosY();
        width = target.getLocalWidth();
        height= target.getLocalHeight();
        centerX = target.getLocalCenterX();
        centerY = target.getLocalCenterY();
        scale = target.getLocalScale();
    }

    public void setMainFrame(Carcass mainFrame) {
    }


    public boolean contains(double x, double y) {
        return x > getPosX() && x < getPosX() + getWidth() && y > getPosY() && y < getPosY() + getHeight();
    }


}

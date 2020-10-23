package com.strat7.game.Interfaces.Basics.BoundBasics;

/**
 * Created by Евгений on 12.08.2017.
 */

public class BoundWithFrame extends Frame implements Carcass{
    protected Carcass mainFrame;

    public BoundWithFrame(Carcass frame, double l, double b, double w,double h, double scale) {
        super(l,b,w,h,scale);
        this.mainFrame = frame == null ? MAIN_FRAME : frame;
        this.scale = scale;
    }

    public BoundWithFrame(Carcass frame,  Carcass copyParameters) {
        super(copyParameters);
        mainFrame = frame;
    }
    public BoundWithFrame(Carcass start, Carcass target, double part) {
        super(start,target,part);
    }
    public BoundWithFrame(Frame bound) {
        super();
        copyLocal(bound);
    }

    public void copy(BoundWithFrame bound) {
        copyLocal(bound);
    }

    public void boundWithNewCarcass(Carcass frame,  double l, double b, double scale) {
        mainFrame = frame;
        super.setParameters(l,b,super.getLocalWidth(),super.getLocalHeight(),scale);
    }

    public void boundWithNewCarcass(Carcass frame,  double l, double b, double w,double h, double scale) {
        mainFrame = frame;
        super.setParameters(l,b,w,h,scale);
    }

    @Override
    public void setMainFrame(Carcass mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public Carcass getMainFrame() {
        return mainFrame;
    }

    @Override
    public double getHeight() {
        return super.getLocalHeight() * getScale();
    }
    @Override
    public double getWidth () {
        return super.getLocalWidth() * getScale();
    }

    @Override
    public double getPosX() {
        return mainFrame.getPosX() + getLocalPosX() * mainFrame.getScale();
    }
    @Override
    public double getPosY() {
        return mainFrame.getPosY() + getLocalPosY() * mainFrame.getScale();
    }

    @Override
    public double getScale() {
        return super.getScale() * mainFrame.getScale();
    }

    public double getLocalOffsetX(double absoluteX) {
        return (absoluteX - mainFrame.getPosX()) / mainFrame.getScale();
    }
    public double getLocalOffsetY(double absoluteY) {
        return (absoluteY - mainFrame.getPosY()) / mainFrame.getScale();
    }


    @Override
    public Frame allowedAbsoluteRegion(){
        Frame allowedRegion = new Frame();
        Carcass carcass = mainFrame.allowedAbsoluteRegion();
        if(carcass == null)
            return null;
        allowedRegion.setPosX(Math.max(carcass.getPosX(),getPosX()));
        allowedRegion.setWidth(Math.min(carcass.getPosX() + carcass.getWidth(), getPosX() + getWidth()) - allowedRegion.getPosX());
        if(allowedRegion.getWidth() <= 0) {
            return null;
        }
        if(getPosY() < carcass.getPosY())
            allowedRegion.setPosY(carcass.getPosY());
        else
            allowedRegion.setPosY(getPosY());
        allowedRegion.setHeight(Math.min(carcass.getPosY() + carcass.getHeight(), getPosY() + getHeight()) - allowedRegion.getPosY());
        if(allowedRegion.getHeight() <= 0) {
            return null;
        }
        return allowedRegion;
    }

    @Override
    public Frame allowedLocalRegion(){
        Frame local = new Frame();
        Carcass carcass = allowedAbsoluteRegion();
        if(carcass == null)
            return null;

        local.setParameters(getLocalOffsetX(carcass.getPosX()),getLocalOffsetY(carcass.getPosY()),carcass.getWidth() / getScale(), carcass.getHeight() / getScale());
        local.setScale(getScale());

        return local;
    }

    public Frame toAbsoluteFrame() {
        return new Frame(getPosX(), getPosY(), getWidth(), getHeight(), 1);
    }
    public Frame toLocalFrame() {
        return new Frame(getLocalPosX(), getLocalPosY(), getLocalWidth(), getLocalHeight(), getLocalScale());
    }
}


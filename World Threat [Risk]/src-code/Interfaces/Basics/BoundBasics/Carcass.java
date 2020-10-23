package com.strat7.game.Interfaces.Basics.BoundBasics;


public interface Carcass {
    Carcass getMainFrame();

    double getPosX ();
    double getPosY ();

    double getLocalPosX ();
    double getLocalPosY ();

    double getWidth();
    double getHeight();

    double getLocalWidth();
    double getLocalHeight();



    double getScale();
    double getLocalScale();

    void setScale(double scale);

    void setPosX(double posX) ;
    void setPosY(double posY);
    void setWidth(double width);
    void setHeight(double height);


    double getCenterX();
    double getCenterY();
    double getLocalCenterX();
    double getLocalCenterY();

    double getLocalScaledWidth();
    double getLocalScaledHeight();
    double getLocalScaledCenterX();
    double getLocalScaledCenterY();

    void setCenterX(double centerX);
    void setCenterY(double centerY);

    Carcass allowedAbsoluteRegion();
    Carcass allowedAbsoluteIntegerRegion();
    Carcass allowedLocalRegion();

    boolean equals(Object o);

    Carcass toAbsoluteFrame();

    boolean contains(double x, double y);
}

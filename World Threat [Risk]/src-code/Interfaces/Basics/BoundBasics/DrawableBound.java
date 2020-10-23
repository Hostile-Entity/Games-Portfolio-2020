package com.strat7.game.Interfaces.Basics.BoundBasics;

import com.strat7.game.Interfaces.Basics.Drawable;

/**
 * Created by Евгений on 21.09.2017.
 */

public abstract class DrawableBound extends BoundWithFrame implements Drawable{
    private boolean hided;
    private boolean consider;

    public DrawableBound(Carcass frame, double l, double b, double w,double h, double scale) {
        super(frame,l,b,w,h,scale);
    }

    public DrawableBound(Carcass frame,  Carcass copyParameters) {
        super(frame,copyParameters);
    }
    public DrawableBound(Carcass start, Carcass target, double part) {
        super(start,target,part);
    }
    public DrawableBound(Frame bound) {
        super(bound);
    }

    public void
    setHided(boolean hided) {
        this.hided = hided;
    }

    public boolean isHided() {
        return hided;
    }

    @Override
    public void hide() {
        setHided(true);
    }

    @Override
    public void show() {
        setHided(false);
    }

    public boolean getConsider(){
        return consider;
    }

    @Override
    public void setConsider(boolean consider) {
        this.consider = consider;
    }


}

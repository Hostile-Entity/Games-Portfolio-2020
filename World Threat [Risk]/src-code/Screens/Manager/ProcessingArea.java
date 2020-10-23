package com.strat7.game.Screens.Manager;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Disposable;
import com.strat7.game.Interfaces.Basics.BoundBasics.BoundWithFrame;
import com.strat7.game.Interfaces.Basics.BoundBasics.Carcass;
import com.strat7.game.Interfaces.Basics.BoundBasics.DrawableBound;
import com.strat7.game.Interfaces.Basics.BoundBasics.Frame;
import com.strat7.game.Interfaces.Basics.Drawable;


public abstract class ProcessingArea extends DrawableBound implements InputProcessor, Activatable, Disposable{
    private double lift;

    public ProcessingArea(Carcass frame, double l, double b, double w, double h, double scale) {
        super(frame,l,b,w,h,scale);
    }
    public ProcessingArea(Carcass frame,  Carcass copyParameters) {
        super(frame,copyParameters);
    }
    public ProcessingArea(Carcass start, Carcass target, double part) {
        super(start,target,part);
    }
    public ProcessingArea(Frame bound) {
        super(bound);
    }

    public abstract void setPressed (boolean pressed);

    public abstract void liftDown();

    public abstract void setBlocked(boolean blocked);
}

package com.strat7.game.Interfaces.Basics;

import com.strat7.game.Interfaces.Basics.BoundBasics.Carcass;
import com.strat7.game.Interfaces.Basics.BoundBasics.DrawableBound;
import com.strat7.game.Interfaces.Basics.BoundBasics.Frame;
import com.strat7.game.Strat7;

import java.util.ArrayList;

/**
 * Created by Евгений on 24.09.2017.
 */

public class DrawableList extends DrawableBound {
    private ArrayList<Drawable> drawables;
    public DrawableList (Carcass frame, double l, double b, double w, double h, double scale) {
        super(frame,l,b,w,h,scale);
        drawables = new ArrayList<Drawable>();
    }

    public DrawableList(Carcass frame,  Carcass copyParameters) {
        super(frame,copyParameters);
        drawables = new ArrayList<Drawable>();
    }
    public DrawableList(Carcass start, Carcass target, double part) {
        super(start,target,part);
        drawables = new ArrayList<Drawable>();
    }
    public DrawableList(Frame bound) {
        super(bound);
        drawables = new ArrayList<Drawable>();
    }

    public void addDrawable(Drawable drawable) {
        addDrawable(drawable,false);
    }
    public void addDrawable(Drawable drawable, boolean consider) {
        drawables.add(drawable);
        drawable.setConsider(consider);
    }

    @Override
    public void draw(boolean considerBorders) {
        if (isHided())
            return;
        if(considerBorders) {
            Frame absolute = allowedAbsoluteIntegerRegion();
            if (absolute == null)
                return;
            Strat7.openBufferStream();
            draw(false);
            Strat7.closeBufferStream();
            Strat7.flushBuffer(absolute);
        }
        else {
            for(Drawable drawable : drawables)
                drawable.draw(drawable.getConsider());
        }
    }

    public void clear() {
        drawables.clear();
    }
}

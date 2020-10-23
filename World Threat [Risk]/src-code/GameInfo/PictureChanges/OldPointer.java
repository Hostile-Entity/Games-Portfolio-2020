package com.strat7.game.GameInfo.PictureChanges;

/**
 * Created by Евгений on 11.06.2017.
 */

public class OldPointer {
    private double pointerX = -1;
    private double pointerY = -1;


    public void set( double x, double y) {
        pointerX = x;
        pointerY = y;
    }
    public void clear() {
        pointerX = -1;
        pointerY = -1;
    }

    public double getPointerX() {
        return pointerX;
    }
    public double getPointerY() {
        return pointerY;
    }

    public double getDistance(double x, double y) {
        return Math.sqrt((pointerX - x) * (pointerX - x) +
                (pointerY - y) * (pointerY - y)
        );
    }
}

package com.strat7.game.GameInfo.PictureChanges;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Евгений on 15.06.2017.
 */

public class SpeedAndDirection {
    private long time = 0;
    private int count;
    private LinkedList<Double> xBias;
    private LinkedList<Double> yBias;
    private LinkedList<Long> deltaTime;

    public SpeedAndDirection() {
        time = System.nanoTime();
        count = 0;
        xBias = new LinkedList<Double>();
        yBias = new LinkedList<Double>();
        deltaTime = new LinkedList<Long>();
    }

    public void set(double X, double Y, long time) {
        if(time == 0) {
            deltaTime.clear();
            xBias.clear();
            yBias.clear();
            count = 0;
        }
        else {
            if(count < 5)
                count ++;
            else {
                xBias.removeFirst();
                yBias.removeFirst();
                deltaTime.removeFirst();
            }
            xBias.addLast(X);
            yBias.addLast(Y);
            deltaTime.addLast(time - this.time);
        }
        this.time = time;
    }

    public void setTime(long time) {
        this.time = time;
        deltaTime.clear();
        xBias.clear();
        yBias.clear();
        count = 0;
    }

    public int changeOffsetX(int offsetX) {
        return 0;
    }
    public int changeOffsetY(int offsetX) {
        return 0;
    }

    public double getxBias() {
        if(xBias.size() == 0)
            return 0;
        else {
            double sum = 0;
            for(Double slider : xBias) {
                sum += slider;
            }
            return sum / xBias.size() ;
            // return xBias.getLast();
        }
    }

    public double getyBias() {
        if(yBias.size() == 0)
            return 0;
        else {
            double sum = 0;
            for(Double slider : yBias) {
                sum += slider;
            }
            return sum / yBias.size();
            // return yBias.getLast();
        }
    }

    public long getDeltaTime() {
        if(deltaTime.size() == 0)
            return 0;
        else {
            long sum = 0;
            for(Long slider : deltaTime) {
                sum += slider;
            }
            return sum / deltaTime.size();
        }
    }
    public long getTime() {
        return time;
    }


    public double getSpeedX() {
        long delta = getDeltaTime();
        double xBias = getxBias();
        return xBias / (delta / 1000000000d);
    }

    public double getSpeedY() {
        long delta = getDeltaTime();
        double yBias = getyBias();
        return yBias / (delta / 1000000000d);
    }
}

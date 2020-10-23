package com.strat7.game.Interfaces.Basics;

import com.strat7.game.GameInfo.PictureChanges.CameraMovement;
import com.strat7.game.Interfaces.Basics.BoundBasics.*;

/**
 * Created by Евгений on 13.08.2017.
 */

public class MovableRegion extends BoundWithFrame {
    private CameraMovement cameraMovement;

    private boolean pressed;

    public MovableRegion(Frame frame, double l, double b, double w, double h){
        super(frame,l,b,w,h,1);
        cameraMovement = new CameraMovement(this);
    }

    public Carcass setObjectParameters(BoundWithFrame object, int parameters) {
        object.boundWithNewCarcass(cameraMovement,0,0,1);
        cameraMovement.setObjectParameters(object,parameters);
        return cameraMovement;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(!contains(screenX, Frame.screenHeight - screenY))
            return false;
        cameraMovement.setTimer(0);
        cameraMovement.setVelocity(0, 0, 0);
        if (pointer == 0 || pointer == 1) {
            cameraMovement.setFinger(pointer, screenX,(int) Frame.screenHeight - screenY);
        }
        return true;
    }
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(!contains(screenX, Frame.screenHeight - screenY))
            return false;
        if (pointer == 1 || pointer == 0) {
            cameraMovement.clearFinger(pointer);
            cameraMovement.clearNewFingers();
            if(cameraMovement.getOldFinger()[(pointer + 1) % 2].getPointerX() == -1)
                pressed = false;
        }

        return true;
    }
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        boolean moved = false;
        if(!contains(screenX, Frame.screenHeight - screenY))
            return false;
        if (pointer == 0 || pointer == 1) {
            if(!pressed) {
                cameraMovement.clearFinger(pointer);
                cameraMovement.setTimer(0);
                cameraMovement.setVelocity(0, 0, 0);
                cameraMovement.clearNewFingers();
            }
            else {
                if (cameraMovement.getOldFinger()[pointer].getPointerX() == -1) {
                    cameraMovement.setFinger(pointer, screenX, screenY);
                    cameraMovement.setTimer(0);
                    cameraMovement.setVelocity(0, 0, 0);
                }
                moved = cameraMovement.moved(screenX, screenY, pointer);
            }
        }
        return moved;
    }

    public boolean scrolled(int amount) {
        cameraMovement.scrolled(amount);
        return true;
    }

    public CameraMovement getCameraMovement() {
        return cameraMovement;
    }

    public void setPressed(boolean pressed) {
        if(pressed != this.pressed) {
            this.pressed = pressed;
            cameraMovement.setTimer(0);
            cameraMovement.setVelocity(0, 0, 0);
            cameraMovement.clearNewFingers();
        }
    }

    public void slideForward(int framesPerSecond) {
        cameraMovement.slideForward(framesPerSecond);
    }

    @Override
    public boolean contains(double x, double y) {
        Frame frame= allowedAbsoluteRegion();
        if(frame == null)
            return false;
        return frame.contains(x,y);
    }
}

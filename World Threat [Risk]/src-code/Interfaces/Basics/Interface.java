package com.strat7.game.Interfaces.Basics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.strat7.game.Interfaces.Basics.BoundBasics.BoundWithFrame;
import com.strat7.game.Interfaces.Basics.BoundBasics.Carcass;
import com.strat7.game.Interfaces.Basics.BoundBasics.Frame;
import com.strat7.game.Interfaces.Basics.Text.PlainText;
import com.strat7.game.Interfaces.Basics.Text.TextField;
import com.strat7.game.Screens.Manager.ProcessingArea;
import com.strat7.game.Strat7;

import java.util.ArrayList;


public abstract class Interface extends ProcessingArea {
    public static final Frame FULL_SCREEN_INTERFACE       = new Frame(0       ,0         ,screenWidth,screenHeight,1);
    public static final Frame BIG_INTERFACE               = new Frame(5 *deltaW,12*deltaH,40*deltaW  ,32*deltaH   ,1);
    public static final Frame NARROW_INTERFACE            = new Frame(17*deltaW,10*deltaH,16*deltaW  ,35*deltaH   ,1);

    public static final Frame SMALL_INTERFACE_CENTER      = new Frame(15*deltaW,17*deltaH,20*deltaW,16*deltaH,1);
    public static final Frame SMALL_INTERFACE_RIGHT_TOP   = new Frame(26*deltaW,30*deltaH,20*deltaW,16*deltaH,1);
    public static final Frame SMALL_INTERFACE_RIGHT_BOTTOM= new Frame(26*deltaW,10*deltaH,20*deltaW,16*deltaH,1);
    public static final Frame SMALL_INTERFACE_LEFT_TOP    = new Frame( 4*deltaW,30*deltaH,20*deltaW,16*deltaH,1);
    public static final Frame SMALL_INTERFACE_LEFT_BOTTOM = new Frame( 4*deltaW,10*deltaH,20*deltaW,16*deltaH,1);

    public static final Frame AVERAGE_INTERFACE_CENTER      = new Frame(13*deltaW, 9*deltaH,24*deltaW,32*deltaH   ,1);
    public static final Frame AVERAGE_INTERFACE_RIGHT_TOP   = new Frame(22*deltaW,14*deltaH,24*deltaW,32*deltaH   ,1);
    public static final Frame AVERAGE_INTERFACE_RIGHT_BOTTOM= new Frame(22*deltaW,10*deltaH,24*deltaW,32*deltaH   ,1);
    public static final Frame AVERAGE_INTERFACE_LEFT_TOP    = new Frame( 4*deltaW,14*deltaH,24*deltaW,32*deltaH   ,1);
    public static final Frame AVERAGE_INTERFACE_LEFT_BOTTOM = new Frame( 4*deltaW,10*deltaH,24*deltaW,32*deltaH   ,1);

    public static final Frame SAVES_INTERFACE = new Frame((2+14+1)*deltaW, 0, 32*deltaW, (50 - 6) * deltaH, 1);


    protected int textFieldAmount;

    public int pressedButton = -1;

    protected int buttonAmount;
    protected  Button[] buttonArray;

    protected int movableRegionAmount;
    protected MovableRegion[] movableRegions;

    private Array<Drawable> drawables;

    protected  TextField[] textFields;

    public TextField getTextField(int num) {
        return textFields[num];
    }

    protected boolean dontCheckBorders = false;
    protected boolean pressed = false;
    private boolean blocked;

    protected PlainText plainText;
    protected double liftedUpOn = 0;

    protected Runnable [] rulesForButtons;


    public Interface(Carcass screen, Frame position) {
        super(screen, position);

        buttonAmount = 0;
        buttonArray = new Button[buttonAmount];
        movableRegionAmount = 0;
        movableRegions = new MovableRegion[movableRegionAmount];
        textFieldAmount = 0;
        textFields = new TextField[textFieldAmount];
        drawables = new Array<Drawable>();
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean interfaceWasPressed = true;
        if(!dontCheckBorders && outOfBorders(screenX,Frame.screenHeight - screenY))
            interfaceWasPressed = false;

        for(MovableRegion movableRegion: movableRegions) {
            if(movableRegion.touchDown(screenX,screenY,pointer,button)) {
                interfaceWasPressed = true;
                movableRegion.setPressed(true);
            }
            else {
                movableRegion.setPressed(false);
            }
        }
        if(pointer != 0) {
            return interfaceWasPressed;
        }

        for(TextField textField :textFields) {
            textField.setPressed(false);
            if(textField.isPressed(screenX,Frame.screenHeight - screenY)) {
                textField.setCurrentSymbol(screenX);
                interfaceWasPressed = true;
                textField.setPressed(true);

            }
        }

        for(int i = buttonAmount - 1; i >= 0; i --) {
            if(buttonArray[i].isPressed(screenX, Frame.screenHeight - screenY)) {
                pressedButton = i ;
                buttonArray[pressedButton].setPressed(true);
                return true;
            }
        }
        pressedButton = -1;
        return interfaceWasPressed;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        boolean interfaceWasPressed = true;
        if (!dontCheckBorders && outOfBorders(screenX, Frame.screenHeight - screenY)) {
            interfaceWasPressed = false;
        }
        for (MovableRegion movableRegion : movableRegions) {
            if (movableRegion.touchUp(screenX, screenY, pointer, button))
                interfaceWasPressed = true;
        }
        if (pointer != 0)
            return interfaceWasPressed;

        interfaceWasPressed = interfaceWasPressed || pressedButton != -1;
        if (pressedButton != -1) {
            buttonArray[pressedButton].setPressed(false);
            if(isBlocked()) {
                pressedButton = -1;
                return true;
            }
            if (buttonArray[pressedButton].isPressed(screenX, Frame.screenHeight - screenY)) {
                runRule(pressedButton);
            }
            pressedButton = -1;
        }

        for (int i = textFieldAmount - 1; i >= 0; i--) {
            textFields[i].setPressed(false);
            if (textFields[i].isPressed(screenX, Frame.screenHeight - screenY)) {
                liftDown();
                textFields[i].setCurrentSymbol(screenX);
                textFields[i].setPressed(true);
                liftUp(textFields[i]);
                return true;
            }
        }
        liftDown();

        if (!interfaceWasPressed && !isBlocked() && ! isPressed())
            callBackButton();

        return interfaceWasPressed;
    }

    protected void runRule(int pressedButton) {
        rulesForButtons[pressedButton].run();
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        boolean interfaceWasPressed = true;
        if(!dontCheckBorders && outOfBorders(screenX,Frame.screenHeight - screenY))
            interfaceWasPressed = false;

        boolean alreadyMoved = false;
        for(MovableRegion movableRegion: movableRegions) {
            if(!alreadyMoved) {
                alreadyMoved = movableRegion.touchDragged(screenX,screenY,pointer);
                interfaceWasPressed = true;
                movableRegion.setPressed(true);
            }
            else {
                movableRegion.setPressed(false);
            }
        }
        if(pointer != 0)
            return interfaceWasPressed;

        for(TextField textField :textFields) {
            textField.setPressed(false);
            if(textField.isPressed(screenX,Frame.screenHeight - screenY)) {
                textField.setCurrentSymbol(screenX);
                interfaceWasPressed = true;
                textField.setPressed(true);
            }
        }

        if(pressedButton != -1) {
            buttonArray[pressedButton].setPressed(false);
            pressedButton = -1;
        }
        for(int i = buttonAmount - 1; i >= 0; i --) {
            if(buttonArray[i].isPressed(screenX, Frame.screenHeight - screenY)) {
                pressedButton = i;
                buttonArray[pressedButton].setPressed(true);

                return true;
            }
        }
        return interfaceWasPressed;
    }

    @Override
    public boolean keyDown(int keycode) {
        return Interface.this.keyTyped(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }
    @Override
    public boolean keyTyped(char character) {
        return false;
    }
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }
    @Override
    public boolean scrolled(int amount) {
        for(MovableRegion movableRegion :movableRegions)
            movableRegion.scrolled(amount);
        return false;
    }


    private void liftUp(BoundWithFrame textField) {
        Gdx.input.setOnscreenKeyboardVisible(true);
        Strat7.keyboardVisible = true;
        if(textField.getPosY() > screenHeight / 2 + deltaH)
            return;
        liftedUpOn =  screenHeight / 2 + deltaH - textField.getPosY() ;
        posY += liftedUpOn;
    }

    @Override
    public void liftDown() {
        posY -= liftedUpOn;
        liftedUpOn = 0;
        for(TextField textField: textFields)
            textField.setPressed(false);
        if(Strat7.keyboardVisible) {
            Gdx.input.setOnscreenKeyboardVisible(false);
            Strat7.keyboardVisible = false;
        }
    }

    protected boolean keyTyped(int keycode) {
        for (TextField textField :textFields) {
            if (textField.getPressed()) {
                textField.inputText(keycode);
                switch(keycode) {
                    case Input.Keys.BACK:
                    case Input.Keys.ENTER:
                        textField.setPressed(false);
                        Gdx.input.setOnscreenKeyboardVisible(false);
                        Strat7.keyboardVisible = false;
                        return true;
                }
                return true;
            }
        }
        if(keycode == Input.Keys.BACK){
            return callBackButton();
        }
        return false;
    }

    public void setRuleForButton(Runnable ruleForButton, int num) {
        rulesForButtons[num] = ruleForButton;
    }
    protected boolean callBackButton() {
        return false;
    }

    public  Button get(int num) {
        return buttonArray[num];
    }

    public boolean outOfBorders(int X, int Y) {
        return (X < getPosX() || X > getPosX() + getWidth()) || (Y < getPosY() || Y > getPosY() + getHeight());
    }

    public void setDontCheckBorders(boolean dontCheckBorders) {
        this.dontCheckBorders = dontCheckBorders;
    }
    public boolean isDontCheckBorders() {
        return dontCheckBorders;
    }

    protected float meanColor(Color color) {
        return (color.r + color.g + color.b) / 3;
    }

    @Override
    public void dispose() {
    }

    @Override
    public void setPressed(boolean pressed) {
        if(!pressed) {
            for (MovableRegion movableRegion : movableRegions)
                movableRegion.setPressed(false);
            for(Button button :buttonArray)
                button.setPressed(false);
            for(TextField textField :textFields)
                textField.setPressed(false);
        }
        this.pressed = pressed;
    }

    public boolean isPressed() {
        return pressed;
    }

    @Override
    public void draw(boolean considerBorders) {
        if (isHided())
            return;
        if(considerBorders) {
            Frame absolute = this;
            Strat7.openBufferStream();
            draw(false);
            Strat7.closeBufferStream();
            Strat7.flushBuffer(absolute);
        }
        else {
            slideForward();
            commonDraw();
        }
    }

    protected void slideForward() {
        for(MovableRegion movable :movableRegions) {
             movable.slideForward(Gdx.graphics.getFramesPerSecond());
        }
    }

    @Override
    public boolean equals(Object frame) {
        return this == frame;
    }

    public void activate() {}
    public void deactivate() {
    }

    @Override
    public boolean isDeactivated() {
        return false;
    }

    @Override
    public boolean isActivated() {
        return false;
    }

    @Override
    public void activateTextures() {

    }

    @Override
    public void deactivateTextures() {

    }

    @Override
    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    protected void commonDraw() {
        for(Drawable drawable :drawables) {
            drawable.draw(drawable.getConsider());
        }
    }

    protected void addDrawable(Drawable drawable, boolean consider) {
        drawables.add(drawable);
        drawable.setConsider(consider);
    }

    public void hide(int num) {
        drawables.get(num).hide();
    }
    public void show(int num) {
        drawables.get(num).show();
    }

    public boolean isBlocked() {
        return blocked;
    }



    @Override
    public boolean contains(double x, double y) {
        Frame frame= allowedAbsoluteRegion();
        if(frame == null)
            return false;
        return frame.contains(x,y);
    }
}

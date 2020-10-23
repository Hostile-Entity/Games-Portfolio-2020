package com.strat7.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.strat7.game.Interfaces.Basics.BoundBasics.BoundWithFrame;
import com.strat7.game.Interfaces.Basics.BoundBasics.Frame;
import com.strat7.game.Interfaces.Basics.BoundTexture;
import com.strat7.game.Interfaces.Basics.Interface;
import com.strat7.game.Interfaces.InterfaceStacks.ScreensStack;
import com.strat7.game.Screens.Manager.Activatable;
import com.strat7.game.Screens.Manager.AppScreen;
import com.strat7.game.Screens.Manager.ProcessingArea;
import com.strat7.game.Strat7;


public abstract class ListenableScreen extends ProcessingArea implements Screen{
    protected ScreensStack stack;
    protected int previousInterface = -1;
    protected BoundTexture background;

    public ListenableScreen() {
        super(MAIN_FRAME, MAIN_FRAME);
        // setScale(0.5);

        stack = new ScreensStack();
        //Gdx.input.setInputProcessor(this);
        background = new BoundTexture(this,Strat7.graphics.getTextureByName("Interface/BackForText.png"),0,0,getLocalWidth(),getLocalHeight(), 1,Strat7.backColor);

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        previousInterface = stack.checkTouchDown(screenX,screenY,pointer,button);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(pointer == 0)
            previousInterface = -1;
        if(stack.checkTouchUp(screenX,screenY,pointer,button) < 0 &&
                stack.getActiveInterfacesAmount() > 1)
            backButtonClicked();
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        previousInterface = stack.checkTouchDragged(screenX,screenY,pointer);
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return stack.checkScrolled(amount);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return stack.checkMouseMoved(screenX,screenY);
    }

    @Override
    public boolean keyDown(int keycode) {
        return stack.checkKeyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return stack.checkKeyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return stack.checkKeyTyped(character);
    }



    @Override
    public void draw(boolean considerBorders) {
        if (isHided())
            return;
        if(considerBorders) {
            Frame absolute = allowedAbsoluteIntegerRegion();
            if (absolute == null) {
                return;
            }
            Strat7.openBufferStream();
            draw(false);
            Strat7.closeBufferStream();
            Strat7.flushBuffer(absolute);
        }
        else {
            commonDraw();
        }
    }

    private void commonDraw() {
        render(AppScreen.getDelta());
    }

    @Override
    public boolean equals(Object frame) {
        return this == frame;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    }

    public void backButtonClicked() {

    }

    public void deactivateInterface(int num, boolean type){
        if(num < 0 || num >= stack.size())
            return ;
        if(type != stack.getInterfaceType(num))
            return;
        stack.deactivateInterface(num);
    }
    public void deactivateInterface(Interface inter, boolean type){
        deactivateInterface(stack.getInterfaceNum(inter), type);
    }

    public void liftDown() {
        stack.liftDown();
    }


    @Override
    public void deactivate() {

    }

    @Override
    public void activate() {

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
    public void deactivateTextures() {

    }

    @Override
    public void activateTextures() {

    }

    @Override
    public void setPressed(boolean pressed) {

    }

    @Override
    public void setBlocked(boolean blocked) {
        stack.setBlocked(blocked);
    }
}

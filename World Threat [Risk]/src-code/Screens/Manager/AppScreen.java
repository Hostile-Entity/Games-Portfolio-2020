package com.strat7.game.Screens.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.strat7.game.Interfaces.Basics.BoundBasics.Frame;
import com.strat7.game.Screens.ListenableScreen;
import com.strat7.game.Strat7;

public class AppScreen extends ListenableScreen {
    public static final int NORMAL_APPEAR = 0;
    public static final int BACK_BUTTON   = 1; // reversed
    public static final int INSTANTLY     = 2; // without delay and at once on position (normal disappear)


    private static float delta = 0;
    private static double time = 0;

    public AppScreen () {
        super();
        Gdx.input.setInputProcessor(this);
    }



    @Override
    public void show() {

    }



    @Override
    public void render(float delta) {





        //delta = 1 / 60f;
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Strat7.clearBuffers();

        Strat7.batch.begin();
        AppScreen.delta = delta;
        for(int i = 0; i < stack.size(); i ++) {
            stack.draw(i,delta,true);
        }
        //System.out.print(stack.size());

        stack.clean();

        //fps
        Strat7.font.setColor(Color.WHITE);
        Strat7.font.draw(Strat7.getCurrentBatch(),Integer.toString(Gdx.graphics.getFramesPerSecond()),0,screenHeight);

        Strat7.batch.end();
    }



    @Override
    public void resize(int width, int height) {
        for(int i = 0; i < stack.size(); i ++) {
            ((ListenableScreen) stack.getInterface(i)).resize(width,height);
        }
    }

    @Override
    public void pause() {
        for(int i = 0; i < stack.size(); i ++) {
            ((ListenableScreen) stack.getInterface(i)).pause();
        }
    }

    @Override
    public void resume() {
        for(int i = 0; i < stack.size(); i ++) {
            ((ListenableScreen) stack.getInterface(i)).resume();
        }
    }

    @Override
    public void hide() {
        for(int i = 0; i < stack.size(); i ++) {
            ((ListenableScreen) stack.getInterface(i)).hide();
        }
    }

    @Override
    public void dispose() {
        for(int i = 0; i < stack.size(); i ++) {
            stack.getInterface(i).dispose();
        }
    }

    public void addScreen(ListenableScreen screen) {
        addScreen(screen, NORMAL_APPEAR);
    }
    public void addScreen(ListenableScreen screen, int appearType) {
        BasicScreenAppear basicScreenAppear;
        switch (appearType) {
            case NORMAL_APPEAR:
                basicScreenAppear = new BasicScreenAppear(screen);
                basicScreenAppear.setDelays(BasicScreenAppear.STANDARD_DISAPPEAR_TIME,BasicScreenAppear.STANDARD_APPEAR_TIME);

                // todo remake in future
                if(stack.size() > 0)
                    stack.deactivateInterface(0);
                else
                    basicScreenAppear.setDelayBeforeAppearStart(0);
                break;

            case BACK_BUTTON:
                basicScreenAppear = new BasicScreenAppear(screen,true);
                basicScreenAppear.setDelays(BasicScreenAppear.STANDARD_DISAPPEAR_TIME,BasicScreenAppear.STANDARD_APPEAR_TIME);

                // todo remake in future
                if(stack.size() > 0) {
                    stack.deactivateInterface(0);
                    stack.getParameter(0).setReversed(true);
                }
                stack.activateInterface(screen,false, basicScreenAppear, stack.getLastPassiveInterfaceNum());
                return;

            case INSTANTLY:
                basicScreenAppear = new BasicScreenAppear(screen);
                basicScreenAppear.setDelays(0,BasicScreenAppear.STANDARD_APPEAR_TIME);

                // todo remake in future
                if(stack.size() > 0) {
                    stack.deactivateInterface(0);
                }
                stack.activateInterface(screen,false, basicScreenAppear);
                basicScreenAppear.stopAnimation();

                return;

            default:
                throw new RuntimeException("Wrong appear type");
        }
        stack.activateInterface(screen,false, basicScreenAppear);
    }

    public static double getTime() {
        return time;
    }
    public static float getDelta() {
        return delta;
    }
}

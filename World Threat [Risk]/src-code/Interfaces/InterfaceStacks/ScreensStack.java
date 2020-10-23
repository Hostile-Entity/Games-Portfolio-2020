package com.strat7.game.Interfaces.InterfaceStacks;

import com.badlogic.gdx.InputProcessor;
import com.strat7.game.Interfaces.Basics.BoundBasics.BoundWithFrame;
import com.strat7.game.Interfaces.Basics.BoundBasics.Frame;
import com.strat7.game.Interfaces.Basics.Interface;
import com.strat7.game.Interfaces.InfoWindowInterface;
import com.strat7.game.Screens.Manager.AppearParameter;
import com.strat7.game.Screens.Manager.ProcessingArea;

import java.util.ArrayList;

/**
 * Created by Евгений on 05.08.2017.
 */

public class ScreensStack {
    private ArrayList<ProcessingArea> interfaces;
    private ArrayList<Boolean> interfacesTypes;
    private ArrayList<AppearParameter> showParameters;
    private boolean blocked;

    public ScreensStack () {
        interfaces = new ArrayList<ProcessingArea>();
        interfacesTypes = new ArrayList<Boolean>();
        showParameters = new ArrayList<AppearParameter>();
    }

    public InputProcessor activateInterface (ProcessingArea newInterface, boolean type, AppearParameter parameters) {
        return activateInterface(newInterface, type, parameters, size());
    }
    public InputProcessor activateInterface (ProcessingArea newInterface, boolean type, AppearParameter parameters, ProcessingArea pointer, boolean before) {
        int num = getInterfaceNum(pointer);
        if(num == -1)
            throw new RuntimeException("Pointer must be in stack");
        num = before ? num : num + 1;
        return activateInterface(newInterface, type, parameters, num);
    }

    public InputProcessor activateInterface (ProcessingArea newInterface, boolean type, AppearParameter parameters, int place) {
        if(place < 0)
            place = 0;
        if(place > size())
            place = size();

        if(newInterface == null)
            return null;
        if(parameters == null)
            throw new IllegalArgumentException("Parameters must be not NULL");

        int num = getInterfaceNum(newInterface);
        if(num != -1) {
            return null;
        }
        interfaces     .add(place,newInterface);
        interfacesTypes.add(place,type);
        showParameters .add(place,parameters);
        newInterface.activate();
        parameters.appear();
        return newInterface;
    }

    public void deleteInterface(ProcessingArea inter) {
        deleteInterface(interfaceNumber(inter));
    }
    public void deleteInterface(int num) {
        interfaces.remove(num).dispose();
        interfacesTypes.remove(num);
        showParameters.remove(num);

    }

    public void deactivateLastActiveInterface() {
        int num = getLastActiveInterfaceNum();
        if(num == -1)
            return ;
        deactivateInterface(num);
    }
    public void deactivateInterface(int num) {
        if(num < 0 || num >= interfaces.size())
            return ;
        if(showParameters.get(num).isDisappearing() || showParameters.get(num).isDisappeared() )
            return;
        showParameters.get(num).disappear();
        interfaces.get(num).deactivate();
    }
    public void deactivateInterface(ProcessingArea inter) {
        deactivateInterface(interfaces.indexOf(inter));
    }

    public ProcessingArea getInterface(int num) {
        if(num < 0 || num >= interfaces.size())
            return null;
        return interfaces.get(num);
    }
    public boolean getInterfaceType(int num) {
        if(num < 0 || num >= interfaces.size())
            return false;
        return interfacesTypes.get(num);
    }

    public int getActiveInterfacesAmount() {
        int count = 0;
        for(int i = 0; i < interfacesTypes.size(); i ++)
            if(interfacesTypes.get(i) && !showParameters.get(i).isDisappearing())
                count ++;
        return count;
    }
    public int getPassiveInterfacesAmount() {
        int count = 0;
        for(int i = 0; i < interfacesTypes.size(); i ++)
            if(!interfacesTypes.get(i) && !showParameters.get(i).isDisappearing())
                count ++;
        return count;
    }

    public int size ()  {
        return interfaces.size();
    }
    public int interfaceNumber(ProcessingArea inter) {
        return interfaces.indexOf(inter);
    }

    public void moveInterface(ProcessingArea inter, BoundWithFrame newPosition, double time){
        int number = interfaceNumber(inter);
        showParameters.get(number).moveTo(newPosition,time);

    }

    public void draw(int i, float delta) {
        draw(i,delta,false);
    }
    public void draw(int i, float delta, boolean considerBorders) {
        interfaces.get(i).setBlocked(showParameters.get(i).isAnimated() || isBlocked());
        showParameters.get(i).draw(getInterface(i),delta,considerBorders);
    }

    public void hide (ProcessingArea inter) {
        hide(getInterfaceNum(inter));
    }
    public void hide (int num) {
        if (num < 0 || num > interfaces.size())
            return;
        showParameters.get(num).hide();
    }
    public void show (ProcessingArea inter) {
        hide(getInterfaceNum(inter));
    }
    public void show (int num) {
        if (num < 0 || num > interfaces.size())
            return;
        showParameters.get(num).show();
    }

    public void clean(){
        for(int i = size() - 1; i >= 0; i --) {
            if(showParameters.get(i).isDisappeared()) {
                interfaces.get(i).copyLocal(showParameters.get(i).getAppearingFrame());
                deleteInterface(i);
            }
        }
    }

    public int checkTouchDown(int screenX, int screenY, int pointer, int button) {
        boolean interfacePressed = false;
        int pressedInterface = -1;
        for(int i = size() - 1; i >= 0; i --) {
            if(!interfacePressed) {
                if (getInterfaceType(i)) {
                    getInterface(i).setPressed(getInterface(i).touchDown(screenX, screenY, pointer, button));
                    interfacePressed = true;
                    pressedInterface = i;
                    continue;
                }
                if (getInterface(i).touchDown(screenX, screenY, pointer, button)) {
                    getInterface(i).setPressed(true);
                    interfacePressed = true;
                    pressedInterface = i;
                }
                else
                    getInterface(i).setPressed(false);
            }
            else {
                getInterface(i).setPressed(false);
            }
        }
        return pressedInterface;
    }
    public int checkTouchUp(int screenX, int screenY, int pointer, int button){
        boolean interfacePressed = false;
        int pressedInterface = -1;
        for(int i = size() - 1; i >= 0; i --) {
            if(!interfacePressed) {
                if (getInterfaceType(i)) {
                    pressedInterface = i;
                    interfacePressed = true;
                    getInterface(i).touchUp(screenX, screenY, pointer, button);
                    getInterface(i).setPressed(true);
                    continue;
                }
                if (getInterface(i).touchUp(screenX, screenY, pointer, button)) {
                    getInterface(i).setPressed(true);
                    pressedInterface = i;
                    interfacePressed = true;
                }
                else
                    getInterface(i).setPressed(false);
            }
            else {
                getInterface(i).setPressed(false);
            }
        }
        return pressedInterface;
    }
    public int checkTouchDragged(int screenX, int screenY, int pointer) {
        boolean interfacePressed = false;
        int pressedInterface = -1;
        for(int i = size() - 1; i >= 0; i --) {
            if(!interfacePressed) {
                if (getInterfaceType(i)) {
                    getInterface(i).touchDragged(screenX,screenY,pointer);
                    interfacePressed = true;
                    pressedInterface = i;
                    continue;
                }
                if(getInterface(i).touchDragged(screenX,screenY,pointer)) {
                    getInterface(i).setPressed(true);
                    interfacePressed = true;
                    pressedInterface = i;
                }
            }
            else {
                getInterface(i).setPressed(false);
            }
        }
        return pressedInterface;
    }

    public boolean checkMouseMoved(int screenX, int screenY){
        for(int i = size() - 1; i >= 0; i --) {
            if(getInterface(i).mouseMoved(screenX, screenY))
                return true;
        }
        return false;
    }
    public boolean checkScrolled(int amount){
        for(int i = size() - 1; i >= 0; i --) {
            if(getInterface(i).scrolled(amount))
                return true;
        }
        return false;
    }

    public boolean checkKeyDown(int keycode){
        for(int i = size() - 1; i >= 0; i --) {
            if(getInterface(i).keyDown(keycode)) {
                return true;
            }
            if(getInterfaceType(i))
                return false;
        }
        return false;
    }
    public boolean checkKeyUp(int keycode){
        for(int i = size() - 1; i >= 0; i --) {
            if(getInterfaceType(i))
                return getInterface(i).keyUp(keycode);
        }
        return false;
    }
    public boolean checkKeyTyped(char character){
        for(int i = size() - 1; i >= 0; i --) {
            if(getInterfaceType(i))
                return getInterface(i).keyTyped(character);
        }
        return false;
    }

    public boolean isActivated(ProcessingArea inter) {
        int num = interfaces.indexOf(inter);
        return num >= 0 && !showParameters.get(num).isDisappearing();
    }
    public boolean isInStack(ProcessingArea inter) {
        return interfaces.indexOf(inter) >= 0;
    }

    public int getInterfaceNum(ProcessingArea inter) {
        return interfaces.indexOf(inter);
    }


    public void replaceInterface(int num, ProcessingArea inter) {
        if(num < 0 || num >= interfaces.size())
            return;
        interfaces.remove(num);
        interfaces.add(num,inter);
    }

    public final AppearParameter getParameter(ProcessingArea inter) {
        return getParameter(getInterfaceNum(inter));
    }
    public final AppearParameter getParameter(int num) {
        if(num < 0 || num >= interfaces.size())
            return null;
        return showParameters.get(num);
    }

    public void liftDown() {
        for(ProcessingArea inter: interfaces) {
            inter.liftDown();
        }
    }

    public void moveBack(ProcessingArea inter) {
        showParameters.get(getInterfaceNum(inter)).moveBack();
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
    public boolean isBlocked() {
        return blocked;
    }

    public int getLastActiveInterfaceNum() {
        return interfacesTypes.lastIndexOf(true);
    }
    public int getLastPassiveInterfaceNum() {
        return interfacesTypes.lastIndexOf(false);
    }
    public ProcessingArea getLastActiveInterface() {
        return interfaces.get(getLastActiveInterfaceNum());
    }
    public ProcessingArea getLastPassiveInterface() {
        return interfaces.get(getLastPassiveInterfaceNum());
    }
}

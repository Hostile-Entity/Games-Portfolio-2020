package com.strat7.game.Interfaces.Buttons;

import com.badlogic.gdx.graphics.Color;
import com.strat7.game.Interfaces.Basics.BoundBasics.Carcass;
import com.strat7.game.Interfaces.Basics.BoundTexture;
import com.strat7.game.Interfaces.Basics.Button;
import com.strat7.game.Interfaces.Basics.Text.BoundText;
import com.strat7.game.Strat7;

/**
 * Created by Евгений on 07.10.2017.
 */

public class ButtonWitchTick extends Button {
    BoundTexture tick;
    boolean active;

    public ButtonWitchTick(Carcass mainFrame, double l, double b, double w, double h, Color color, Color tickColor) {
        super(mainFrame,l,b,w,h,"Interface/Button1.png",null,color,color,"Interface/Button2.png");
        tick = new BoundTexture(this, Strat7.graphics.getTextureByName("Interface/Turn.png"),0,0 ,getLocalHeight() * (9 / 11d),getLocalHeight() * (9 / 11d),1,tickColor);
        setCenterX((getLocalWidth() - getLocalHeight()) / 2 + getLocalHeight());
    }
    protected void initText() {
        text = new BoundText(this, "", meanColor(color) > 0.5 ? Color.BLACK : Color.WHITE, height, height / 11 / 2, width * (1 - 1 / 22f) - height, height * (1 - 1 / 11f), Strat7.normalFontSize);
    }

    @Override
    protected void commonDraw() {
        super.commonDraw();
        tick.drawCenterIn(getLocalHeight() / 2, getLocalCenterY(), false);
    }

    public void pressed(){
        active = !active;
        if(active) {
            tick.setColor(Color.BLUE);
        }
        else {
            tick.setColor(Color.RED);
        }
    }

    public void activate(){
        if(!isActive())
            pressed();
    }
    public void deactivate(){
        if(isActive())
            pressed();
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);

    }
}

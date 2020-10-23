package com.strat7.game.Interfaces;

import com.badlogic.gdx.graphics.Color;
import com.strat7.game.Interfaces.Basics.*;
import com.strat7.game.Interfaces.Basics.BoundBasics.Carcass;
import com.strat7.game.Interfaces.Basics.BoundBasics.Frame;
import com.strat7.game.Interfaces.Basics.Text.PlainText;
import com.strat7.game.Strat7;

/**
 * Created by Евгений on 06.08.2017.
 */

public class InfoWindowInterface extends Interface {

    public static int INFO_BUTTON_1 = 0;
    public static int INFO_BUTTON_2 = 1;

    private BoundTexture backgroundTexture;
    private BoundTexture textBackgroundTexture;
    private class underPanelText{}

    public InfoWindowInterface(Carcass screen, final Frame size, String mainText, String leftButtonText, String rightButtonText){
        super(screen, size);

        // todo scale
        scale = 1f;

        backgroundTexture = new BoundTexture(this,Strat7.graphics.getTextureByName("Interface/SmallMenu.png"),0 ,0 ,getLocalWidth(),getLocalHeight(),1,Strat7.backColor);
        textBackgroundTexture = new BoundTexture(this,Strat7.graphics.getTextureByName("Interface/SmallMenu.png"),deltaH,5*deltaH + deltaH/2,getLocalWidth() - 2*deltaH,getLocalHeight()- 6.5*deltaH,1,new Color(1,1,1,0.8f));

        buttonAmount = 2;
        buttonArray = new Button[buttonAmount];
        rulesForButtons = new Runnable[buttonAmount];

        plainText = new PlainText(this,1.5f * deltaW, 5.5f*deltaH, width - 3 * deltaW,height - 7 * deltaH , "",Color.BLACK);
        buttonArray[INFO_BUTTON_1] = new Button(
                this,
                deltaH , deltaH,
                this.getLocalWidth() / 2 - 2f * deltaH, 4*deltaH,
                "Interface/Button1.png",
                Strat7.buttonColor, "Interface/Button2.png"
        );

        buttonArray[INFO_BUTTON_2] = new Button(
                this,
                deltaH + this.getLocalWidth() / 2, deltaH,
                this.getLocalWidth() / 2 - 2 * deltaH, 4*deltaH,
                "Interface/Button1.png",
                Strat7.buttonColor, "Interface/Button2.png"
        );
        setInfo(mainText,leftButtonText,rightButtonText);
    }

    public void setInfo (String mainText, String leftButtonText, String rightButtonText) {
        plainText.setText(mainText);
        if(leftButtonText == null) {
            buttonArray[INFO_BUTTON_1].setBlocked(true);
            buttonArray[INFO_BUTTON_1].setVisible(false);
        }
        else {
            buttonArray[INFO_BUTTON_1].setBlocked(false);
            buttonArray[INFO_BUTTON_1].setVisible(true);
            buttonArray[INFO_BUTTON_1].setText(leftButtonText);
        }

        if(rightButtonText == null) {
            buttonArray[INFO_BUTTON_2].setBlocked(true);
            buttonArray[INFO_BUTTON_2].setVisible(false);
        }
        else {
            buttonArray[INFO_BUTTON_2].setBlocked(false);
            buttonArray[INFO_BUTTON_2].setVisible(true);
            buttonArray[INFO_BUTTON_2].setText(rightButtonText);
        }
    }

    public void setTextScale(double scale) {
        plainText.setTextScale(scale);
    }

    @Override
    protected void commonDraw() {
        backgroundTexture.draw(false);
        textBackgroundTexture.draw(false);

        Strat7.font.setColor(Color.BLACK);
        plainText.draw(false);

        buttonArray[INFO_BUTTON_1].draw(false);
        buttonArray[INFO_BUTTON_2].draw(false);
    }

    @Override
    public boolean outOfBorders(int X, int Y){
        return (X < getPosX() || X > getPosX() + getWidth()) || (Y < getPosY() || Y > getPosY() + getHeight());
    }
}

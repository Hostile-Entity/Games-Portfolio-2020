package com.strat7.game.Interfaces;

import com.badlogic.gdx.graphics.Color;
import com.strat7.game.Interfaces.Basics.BoundBasics.Carcass;
import com.strat7.game.Interfaces.Basics.BoundTexture;
import com.strat7.game.Interfaces.Basics.BoundBasics.Frame;
import com.strat7.game.Interfaces.Basics.Button;
import com.strat7.game.Interfaces.Basics.Interface;
import com.strat7.game.Interfaces.Basics.Text.BoundText;
import com.strat7.game.Money.Economy;
import com.strat7.game.Strat7;

/**
 * Created by Юра on 11.08.2017.
 */

public class InterfaceCommon extends Interface{

    private BoundTexture coin;
    private BoundText money;

    private BoundTexture logo;
    private BoundTexture line;
    private BoundTexture triangle;

    public InterfaceCommon(Carcass screen, final Frame position) {
        super(screen,position);
        coin = new BoundTexture(
                this, Strat7.graphics.getTextureByName("Icons/Coin.png"),
                deltaH, 47*deltaH, 2*deltaH,2*deltaH, 1, Color.WHITE
        );
        money = new BoundText(this, Integer.toString(Strat7.economy.getMoney()),Color.WHITE, 4 * deltaH, 47 * deltaH + 2 * deltaH/ 11, 20 * deltaH, (2 - 4 / 11d) * deltaH, 1);
        logo = new BoundTexture(
                this, Strat7.graphics.getTextureByName("Interface/Logo.png"),
                (2+14+2+15)*deltaW - 20*deltaH, 5*deltaH, 40*deltaH,40*deltaH, 1, Color.WHITE
        );
        line = new BoundTexture(
                this, Strat7.graphics.getTextureByName("Interface/BackForText.png"),
                0, 0, 2,40*deltaH, 1, Color.WHITE
        );
        triangle = new BoundTexture(
                this, Strat7.graphics.getTextureByName("Interface/Triangle.png"),
                0, 0, deltaW,2 * deltaW, 1, Color.WHITE
        );
    }

    public void drawMoney() {
        coin.draw(false);
        if (meanColor(Strat7.backColor) < 0.5) {
            Strat7.font.setColor(Color.WHITE);
        } else {
            Strat7.font.setColor(Color.BLACK);
        }
        money.setText(Integer.toString(Strat7.economy.getMoney()));
        money.draw(false);
    }

    public void drawLogo() {
        logo.draw(false);
    }

    public void drawLine(double X, double Y) {
        line.setPosX(X);
        line.setPosY(Y);
        line.draw(false);
    }

    public void drawTriangle(Button button) {
        triangle.boundWithNewCarcass(button,button.getLocalWidth(), (button.getLocalHeight() - triangle.getLocalHeight()) / 2, 1);
        triangle.draw(false);
    }
}

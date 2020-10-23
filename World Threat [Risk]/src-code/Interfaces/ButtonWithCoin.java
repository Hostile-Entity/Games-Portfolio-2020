package com.strat7.game.Interfaces;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.strat7.game.Interfaces.Basics.BoundBasics.Carcass;
import com.strat7.game.Interfaces.Basics.BoundTexture;
import com.strat7.game.Interfaces.Basics.Button;
import com.strat7.game.Strat7;

/**
 * Created by Юра on 18.08.2017.
 */

public class ButtonWithCoin extends Button {

    public ButtonWithCoin (Carcass mainFrame, double xPos, double yPos, double Width , double Height, String unpressed , Color color, String pressedTexture) {
        super(mainFrame, xPos, yPos, Width, Height, unpressed, null, color, color, pressedTexture);
    }

    @Override
    public void setText (String s) {
        text.setText(s + " O");
        icon = new BoundTexture(this, Strat7.graphics.getTextureByName("Icons/Coin.png"), 0, 0, new GlyphLayout(Strat7.font, "O").height * 9/8, new GlyphLayout(Strat7.font, "O").height * 9/8, 1, null);
    }

    @Override
    protected void commonDraw(){
        Color fontColor = new Color(Strat7.font.getColor());
        if (!visible) {
            return;
        }

        if (pressedTexture != background){ // 3D Button
            Strat7.getCurrentBatch().setColor(color);
            if (meanColor(color) > 0.5f) {
                Strat7.font.setColor(Color.BLACK);
            }
            else {
                Strat7.font.setColor(Color.WHITE);
            }
            if (pressed) {
                pressedTexture.draw(false);
                text.drawCenterIn(getLocalCenterX(),getLocalCenterY(),false);
            } else if (MCHammer){
                pressedTexture.draw(false);
                Strat7.getCurrentBatch().setColor(Color.BLACK.r,Color.BLACK.g, Color.BLACK.b, 0.3f);
                pressedTexture.draw(false);
                text.drawCenterIn(getLocalCenterX(),getLocalCenterY(),false);
            } else {
                background.draw(false);
                text.drawCenterIn(getLocalCenterX(),getLocalCenterY(),false);
            }
        }

        Strat7.getCurrentBatch().setColor(Color.WHITE);

        icon.drawCenterIn(getLocalCenterX() + text.getLocalWidth()/2 - new GlyphLayout(Strat7.font, "O").height * 9/8/2,getLocalCenterY(),false);
        Strat7.font.getData().setScale(Strat7.normalFontSize);
        Strat7.font.setColor(fontColor);
    }
}

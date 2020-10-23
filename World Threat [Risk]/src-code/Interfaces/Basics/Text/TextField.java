package com.strat7.game.Interfaces.Basics.Text;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.strat7.game.Interfaces.Basics.BoundBasics.Carcass;
import com.strat7.game.Interfaces.Basics.BoundBasics.Frame;
import com.strat7.game.Interfaces.Basics.BoundTexture;
import com.strat7.game.Interfaces.Basics.Button;
import com.strat7.game.Interfaces.Basics.Drawable;
import com.strat7.game.Strat7;


/**
 * Created by Юра on 23.07.2017.
 */

public class TextField extends Button implements Drawable{

    private boolean shift;
    private int maxSize;
    private BoundTexture cursor;
    private int currentSymbol;
    private boolean show;
    private double localTime;
    private double cursorBlinkTime = 0.5f;

    private double offsetX = 0;


    public TextField(Carcass carcass, double xPos, double yPos, double Width , double Height, String textureName , Color color) {
        super(carcass,xPos, yPos, Width, Height, textureName, color);
        shift = false;
        maxSize = 30;
        cursor = new BoundTexture(this,Strat7.graphics.getTextureByName("Interface/BackForText.png"), 0,Height / 9, 2, Height * 7 / 9 ,1, Color.WHITE);
    }

    @Override
    protected void initText() {
        text = new BoundText(this,"",Color.WHITE, 0,height / 11 / 2,width * (1 - 1 / 22f),height * (1 - 1/11f), Strat7.normalFontSize);
    }


    @Override
    protected void commonDraw() {
        if (visible) {
            Strat7.font.getData().setScale((float) (text.getScale()));
            if (pressed) {
                Strat7.getCurrentBatch().setColor(colorPressed);
                localTime += Strat7.timePassed;
                if (localTime > cursorBlinkTime) {
                    localTime -= cursorBlinkTime;
                    show = !show;
                }
            } else {
                Strat7.getCurrentBatch().setColor(colorPressed);
            }
            background.draw(false);
            text.drawCenterIn(getLocalCenterX(),getLocalCenterY(),false);
            if (show && pressed) {
                layout.setText(Strat7.font, text.getText().substring(0, currentSymbol));
                cursor.setPosX(text.getLocalPosX() + ((text.getMaxWidth() - text.getLocalWidth() * getScale() ) / 2 + layout.width + 5 * text.getScale()) / getScale() );
                cursor.draw(false);
            }
            Strat7.font.getData().setScale(Strat7.normalFontSize);
        }
    }

    @Override
    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public void inputText(int key) {


        switch(key) {
            case Input.Keys.BACKSPACE:
                if (text.getText().length() > 0 && currentSymbol > 0) {
                    text.setText(text.getText().substring(0, currentSymbol - 1) + text.getText().substring(currentSymbol));
                    currentSymbol--;
                }
                break;
            case Input.Keys.SPACE:
                addSymbol(" ");
                break;
            case Input.Keys.GRAVE:
                addSymbol("`");
                break;
            case Input.Keys.SHIFT_LEFT:
            case Input.Keys.SHIFT_RIGHT:
                shift = true;
                break;
            case Input.Keys.MINUS:
                if (shift) {
                    shift = false;
                    addSymbol("_");
                }
                break;
            default:
                //text += Integer.toString(key);
                break;
        }
        for (int i = Input.Keys.A; i < Input.Keys.Z + 1; i++) {
            if (key == i) {
                if (shift) {
                    addSymbol(Character.toString((char) ('A' + i - Input.Keys.A)));
                    shift = false;
                } else {
                    addSymbol(Character.toString((char) ('a' + i - Input.Keys.A)));
                }
            }
        }
        for (int i = Input.Keys.NUM_0; i < Input.Keys.NUM_9 + 1; i++) {
            if (key == i) {
                addSymbol(Character.toString((char)('0' + i - Input.Keys.NUM_0)));
            }
        }
        if (text.getText().length() > maxSize) {
            text.setText(text.getText().substring(0, currentSymbol - 1) + text.getText().substring(currentSymbol));
            currentSymbol--;
        }
    }

    private void addSymbol(String symbol) {
        text.setText(text.getText().substring(0,currentSymbol) +symbol + text.getText().substring(currentSymbol));
        currentSymbol++;
    }

    public void setCurrentSymbol(double X) {
        show = true;
        localTime = 0;
        currentSymbol = text.getText().length();
        Strat7.font.getData().setScale((float) (text.getScale()));
        layout.setText(Strat7.font, text.getText());
        offsetX = getPosX() + getWidth() / 2 - layout.width /2;
        for (int i = 0; i < text.getText().length(); i++) {
            layout.setText(Strat7.font, text.getText().substring(i,i + 1));
            offsetX += layout.width/2;
            if (X < offsetX) {
                currentSymbol = i;
                break;
            }
            offsetX += layout.width/2;
        }
        Strat7.font.getData().setScale(Strat7.normalFontSize );
    }
}
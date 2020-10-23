package com.strat7.game.Interfaces.Basics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.strat7.game.Strat7;
import com.strat7.game.Interfaces.Basics.BoundBasics.*;
import com.strat7.game.Interfaces.Basics.Text.*;




public class Button extends DrawableBound {

    protected BoundTexture background;
    protected BoundTexture pressedTexture;
    protected BoundTexture icon;

    protected GlyphLayout layout = new GlyphLayout();
    protected BoundText text;

    protected boolean visible;
    protected boolean pressed;
    protected boolean MCHammer;

    protected Color color;
    protected Color colorPressed;

    public Button (
            Carcass mainFrame,
            double leftOffset, double bottomOffset,
            double width ,double height,
            String unpressed,
            BoundTexture icon,
            Color color, Color colorPressed,
            String pressedTexture
    ) {
        super(mainFrame, leftOffset,bottomOffset, width, height,1);
        if(unpressed != null)
            background = new BoundTexture(this,Strat7.graphics.getTextureByName(unpressed),0,0, width, height,1, null);
        else {
            background = null;
            setVisible(false);
        }

        if(unpressed != null && pressedTexture != null || unpressed == null )
            setCenterY(getLocalCenterY() * (1 - 1 / 11d));


        this.icon = icon;
        if(icon != null)
            icon.boundWithNewCarcass(this,icon.getLocalPosX(),icon.getLocalPosY(), 1);
        if(pressedTexture != null)
            this.pressedTexture = new BoundTexture(this,Strat7.graphics.getTextureByName(pressedTexture),0,0, width, height,1, null);
        else
            this.pressedTexture = background;

        visible = false;
        this.color = color;
        this.colorPressed = colorPressed;
        pressed = false;
        MCHammer = false;

        initText();
    }

    protected void initText() {
        text = new BoundText(this, "", meanColor(color) > 0.5 ? Color.BLACK : Color.WHITE, 0, height / 11 / 2, width * (1 - 1 / 22f), height * (1 - 1 / 11f), Strat7.normalFontSize);
    }

    public Button (Carcass mainFrame, double xPos, double yPos, double Width , double Height, String unpressed, BoundTexture icon , Color color, Color colorPressed) {
        this(mainFrame, xPos, yPos, Width, Height, unpressed, icon, color, colorPressed, null);
    }

    public Button (Carcass mainFrame, double xPos, double yPos, double Width , double Height, String unpressed, BoundTexture icon, Color color) {
        this(mainFrame, xPos, yPos, Width, Height, unpressed, icon, color, color, null);
    }

    public Button (Carcass mainFrame, double xPos, double yPos, double Width , double Height, String unpressed , Color color, Color colorPressed) {
        this(mainFrame, xPos, yPos, Width, Height, unpressed,  null, color, colorPressed, null);
    }
    public Button (Carcass mainFrame, double xPos, double yPos, double Width , double Height, String unpressed , Color color, String pressedTexture) {
        this(mainFrame, xPos, yPos, Width, Height, unpressed, null, color, color, pressedTexture);
    }

    public Button (Carcass mainFrame, double xPos, double yPos, double Width , double Height, String unpressed , Color color) {
        this(mainFrame, xPos, yPos, Width, Height, unpressed, null, color, color, null);
    }
    public Button (Carcass mainFrame, double xPos, double yPos, double Width , double Height, String unpressed, BoundTexture icon) {
        this(mainFrame, xPos, yPos, Width, Height, unpressed, icon, Color.WHITE, Color.WHITE, null);
    }
    public Button (Carcass mainFrame, double xPos, double yPos, double Width , double Height, String unpressed) {
        this(mainFrame, xPos, yPos, Width, Height, unpressed, null, Color.WHITE, Color.WHITE, null);
    }

    public BoundTexture getTexture () {
        return background;
    }

    public void setColor(Color color) {
        if (this.color == this.colorPressed) {
            this.colorPressed = color;
        }
        this.color = color;
    }
    public void setColorPressed(Color color) {
        this.colorPressed = color;
    }

    public void setText (String s) {
        text.setText(s);
    }
    public BoundText getText () {
        return text;
    }

    public boolean isVisible () {
        return visible;
    }
    public void setVisible (boolean v) {
        visible = v;
    }

    public void setPressed(boolean pressed) {
        if(pressed && !this.pressed && background != pressedTexture) {
            setCenterY(getLocalCenterY() + getLocalHeight() / 11 );
        }
        if(!pressed && this.pressed && background != pressedTexture) {
            setCenterY(getLocalCenterY() - getLocalHeight() / 11 );
        }
        this.pressed = pressed;
    }
    public boolean getPressed() {
        return pressed;
    }
    public void setBlocked(boolean MCHammer) {
        if(MCHammer && !isBlocked() && background != pressedTexture) {
            setCenterY(getLocalCenterY() + getLocalHeight() / 11 );
        }
        if(!MCHammer && isBlocked() && background != pressedTexture) {
            setCenterY(getLocalCenterY() - getLocalHeight() / 11 );
        }
        this.MCHammer = MCHammer;
    }

    public boolean isBlocked() { return MCHammer; }


    public boolean isPressed (double X, double Y) {
        Carcass allowed = allowedAbsoluteIntegerRegion();
        return  !isBlocked() && allowed != null && allowed.contains(X,Y);
    }


    public void draw (boolean considerBorders) {
        if (isHided())
            return;
        if(considerBorders) {
            Frame absolute = allowedAbsoluteIntegerRegion();
            if(absolute == null)
                return;
            Strat7.openBufferStream();
            draw(false);
            Strat7.closeBufferStream();
            Strat7.flushBuffer(absolute);
        }
        else {
            commonDraw();
        }
    }

    public void drawChooseButton(boolean considerBorders) {
        if (isHided())
            return;
        if(considerBorders) {
            Frame absolute = allowedAbsoluteIntegerRegion();
            if(absolute == null)
                return;
            Strat7.openBufferStream();
            drawChooseButton(false);
            Strat7.closeBufferStream();
            Strat7.flushBuffer(absolute);
        }
        else {
            commonDrawChooseButton();
        }
    }

    public void drawText (boolean considerBorders) {
        if (isHided())
            return;
        if(considerBorders) {
            Frame absolute = allowedAbsoluteIntegerRegion();
            if(absolute == null)
                return;
            Strat7.openBufferStream();
            drawText(false);
            Strat7.closeBufferStream();
            Strat7.flushBuffer(absolute);
        }
        else {
            commonDrawText();
        }
    }

    protected void commonDraw(){
        Color fontColor = new Color(Strat7.font.getColor());
        if (!visible)
            return;

        if(background == null || pressedTexture == null)
            return;

        // Just regular Button with 2 colors
        if (!color.equals(colorPressed)) {
            if (pressed) {
                Strat7.getCurrentBatch().setColor(colorPressed);
            } else {
                Strat7.getCurrentBatch().setColor(color);
            }
            text.drawCenterIn(getLocalCenterX(),getLocalCenterY(),false);
        } else if (pressedTexture != background){ // 3D Button
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
            } else if (isBlocked()){
                pressedTexture.draw(false);
                Strat7.getCurrentBatch().setColor(Color.BLACK.r,Color.BLACK.g, Color.BLACK.b, 0.4f);
                pressedTexture.draw(false);
                text.drawCenterIn(getLocalCenterX(),getLocalCenterY(),false);
            } else {
                background.draw(false);
                text.drawCenterIn(getLocalCenterX(),getLocalCenterY(),false);
            }
        } else { // Just regular Button
            Strat7.getCurrentBatch().setColor(color);
            if (meanColor(color) > 0.5f) {
                Strat7.font.setColor(Color.BLACK);
            }
            background.draw(false);
            if (pressed) {
                Strat7.getCurrentBatch().setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, 0.5f);
                background.draw(false);
            }
            text.drawCenterIn(getLocalCenterX(),getLocalCenterY(),false);
        }

        Strat7.getCurrentBatch().setColor(Color.WHITE);
        if (icon != null) {
            icon.drawCenterIn(getLocalCenterX(),getLocalCenterY(),false);
        }
        Strat7.font.getData().setScale(Strat7.normalFontSize);
        Strat7.font.setColor(fontColor);
    }

    protected void commonDrawChooseButton(){
        if (visible) {
            Strat7.getCurrentBatch().setColor(color);
        } else {
            Strat7.getCurrentBatch().setColor(1, 1, 1, 0f);
            if (pressed) {
                Strat7.getCurrentBatch().setColor(colorPressed);
            }
        }
        background.draw(false);
        text.setColor(Color.WHITE);
        if (icon != null) {
            icon.draw(false);
        }
        layout.setText(Strat7.font, text.getText());
        if (layout.width*9/8 > width) {
            Strat7.font.getData().setScale((float) (Strat7.normalFontSize*width/layout.width*8/9));
            layout.setText(Strat7.font, text.getText());
        }
        text.drawCenterIn(getLocalCenterX(),getLocalCenterY(),false);
        Strat7.font.getData().setScale(Strat7.normalFontSize);
    }


    protected void commonDrawText() {
        text.drawCenterIn(getLocalCenterX(),getLocalCenterY(),false);
    }


    protected double meanColor(Color color) {
        return (color.r + color.g + color.b) / 3;
    }

    public void dispose() {

    }
}

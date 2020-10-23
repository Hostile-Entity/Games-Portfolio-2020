package com.strat7.game.Interfaces.Basics.Text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.strat7.game.Interfaces.Basics.BoundBasics.BoundWithFrame;
import com.strat7.game.Interfaces.Basics.BoundBasics.Carcass;
import com.strat7.game.Interfaces.Basics.BoundBasics.DrawableBound;
import com.strat7.game.Interfaces.Basics.BoundBasics.Frame;
import com.strat7.game.Interfaces.Basics.Drawable;
import com.strat7.game.Strat7;

public class PlainText extends DrawableBound {
    private double textScale;

    protected String text;
    protected GlyphLayout layout = new GlyphLayout();
    protected Color color;

    public PlainText(Carcass carcass, double l, double b, double w, double h, String text, Color color) {
        super(carcass, l, b, w, h,1);
        this.text = text;
        textScale = 1;
        this.color = color;
    }

    public void draw(boolean considerBorders) {
        if(isHided())
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
        else
            commonDraw();
    }

    private void commonDraw() {
        Color color = Strat7.font.getColor();
        Strat7.font.setColor(this.color);
        Strat7.font.getData().setScale((float) (Strat7.normalFontSize * getScale() * textScale));
        layout.setText(Strat7.font, text);
        /*
        if (width*height < (layout.height * 9/8) * layout.width) {
            game.getFont().getData().setScale(game.getNormalFontSize() * 8/9 * width /layout.width * height /layout.height);
            layout.setText(game.getFont(), text);
        }*/
        //int strings = (int) (layout.width / width) + 1;
        int stringLength = (int)( getWidth() /  (layout.width / text.length()));

        int currentSymbol = 0;
        int stringNumber = 0;
        int currentStringLength;
        while (text.substring(currentSymbol).length() > stringLength) {
            currentStringLength = stringLength;
            while (!text.substring(currentSymbol, currentSymbol + currentStringLength).endsWith(" ")) {
                currentStringLength--;
            }
            Strat7.font.draw(Strat7.getCurrentBatch(), text.substring(currentSymbol, currentSymbol + currentStringLength), (float)getPosX(), (float) (getPosY() + getHeight() - layout.height * 9/8 * stringNumber - layout.height/8));
            currentSymbol += currentStringLength;
            stringNumber++;
        }
        Strat7.font.draw(Strat7.getCurrentBatch(), text.substring(currentSymbol),(float) getPosX(), (float) (getPosY() + getHeight() - layout.height * 9/8 * stringNumber - layout.height/8));


        Strat7.font.getData().setScale(Strat7.normalFontSize);
        Strat7.font.setColor(color);
    }

    public String getText() {
        return text;
    }
    public void setText (String s) {
        text = s;
    }

    public void setTextScale(double textScale) {
        this.textScale = textScale;
    }
}
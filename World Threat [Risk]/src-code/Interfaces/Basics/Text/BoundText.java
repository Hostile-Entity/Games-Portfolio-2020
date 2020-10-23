
package com.strat7.game.Interfaces.Basics.Text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.strat7.game.Interfaces.Basics.BoundBasics.BoundWithFrame;
import com.strat7.game.Interfaces.Basics.BoundBasics.Carcass;
import com.strat7.game.Interfaces.Basics.BoundBasics.DrawableBound;
import com.strat7.game.Interfaces.Basics.BoundBasics.Frame;
import com.strat7.game.Interfaces.Basics.Drawable;
import com.strat7.game.Strat7;

/**
 * Created by Евгений on 14.08.2017.
 */

public class BoundText extends DrawableBound {
    private String text;
    protected GlyphLayout layout = new GlyphLayout();
    private Color color;

    private double textWidth;
    private double textHeight;

    private double maxHeight;
    private double maxWidth;

    private double textScale;
    private double previousScale = -1;
    private boolean textChanged = false;

    private GlyphLayout [] layouts;
    private double [] sizes;
    private int align;


    public BoundText(Carcass frame, String text, Color color, double l, double b, double w, double h, double s ) {
        this(frame,text,color,l,b,w,h,s,Align.topLeft);
    }

    public BoundText(Carcass frame, String text, Color color, double l, double b, double w, double h, double s , int align) {
        super(frame, l, b,  w, h,s);
        maxHeight = h;
        maxWidth = w;
        this.color = color;
        this.text = text;
        textChanged = true;

        this.align = align;

        textScale = 1;
        setTextScale();
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
        else {
            setTextScale();
            layout.setText(Strat7.font,text,color,(float)maxWidth,align,true);
            Strat7.font.draw(Strat7.getCurrentBatch(), layout, (float) getPosX(), (float) getPosY());
        }
    }

    public void drawCenterIn(double x, double y, boolean considerBorders) {
        if(considerBorders) {
            Frame absolute = allowedAbsoluteIntegerRegion();
            if(absolute == null)
                return;
            Strat7.openBufferStream();
            drawCenterIn(x,y,false);
            Strat7.closeBufferStream();
            Strat7.flushBuffer(absolute);
        }
        else {
            setTextScale();
            Strat7.font.draw(Strat7.getCurrentBatch(), layout, (float) getPosXInCenter(x), (float) getPosYInCenter(y));
        }
    }

    @Override
    public double getPosY() {
        return super.getPosY() + textHeight;
    }

    @Override
    public double getScale() {
        return super.getScale() * textScale;
    }

    private double getPosXInCenter(double x) {
        return mainFrame.getPosX() + mainFrame.getScale() * x - getMaxWidth()  / 2;
    }
    private double getPosYInCenter(double y) {
        return mainFrame.getPosY() + mainFrame.getScale() * y + textHeight / 2;
    }

    public void setTextScale() {
        Strat7.font.getData().setScale((float) getScale());
        if(previousScale != getScale() || textChanged) {
            textChanged = false;
            controlSize();
        }
    }

    private void controlSize() {

        textScale = 1;
        previousScale = getScale();
        Strat7.font.getData().setScale((float) previousScale);


        //text = text + "123";
        layout.setText(Strat7.font, text, color,(float)getMaxWidth(), Align.center,false);
        //height = layout.runs.size * Strat7.font.getLineHeight();
        textHeight = layout.height;
        setHeight(textHeight / mainFrame.getScale());
        double widthCoef = layout.width / (getMaxWidth() * 0.99d);
/*
        System.out.println(height);
        System.out.println(layout);
        System.out.println();
        System.out.println(layout.height);
        System.out.println(new GlyphLayout(Strat7.font, "(").height);
        System.out.println(new GlyphLayout(Strat7.font, "1").height);
        System.out.println();
        System.out.println();
*/
        if(maxWidth <= 0 || widthCoef  < 1) {
            if(textHeight * textScale > getMaxHeight()) {
                textScale *= getMaxHeight() / (textHeight * textScale);
                previousScale = getScale();
                Strat7.font.getData().setScale((float) previousScale);
                layout.setText(Strat7.font, text, color,(float)getMaxWidth(), Align.center,false);
                //height = layout.runs.size * Strat7.font.getLineHeight();
                textHeight = layout.height;
                setHeight(textHeight / mainFrame.getScale());
            }
            textWidth = layout.width;
            setWidth(textWidth / mainFrame.getScale());
            return;
        }
        textScale = textScale / widthCoef;
        if(textHeight * textScale > getMaxHeight()) {
            textScale *= getMaxHeight() / (textHeight * textScale);
        }
        previousScale = getScale();
        Strat7.font.getData().setScale((float) previousScale);
        layout.setText(Strat7.font, text, color,(float)getMaxWidth(), Align.center,true);


/*
        System.out.println(height);
        System.out.println(layout);
        System.out.println();
        System.out.println(layout.height);
        System.out.println(new GlyphLayout(Strat7.font, "(").height);
        System.out.println();
        System.out.println();
*/
        /*
        String[] resultStrings = new String [layout.runs.size];
        Array<GlyphLayout.GlyphRun> runs = layout.runs;
        for(GlyphLayout.GlyphRun run :runs) {
            System.out.println("run ");
            if(run.glyphs.size > 0) {
                for(BitmapFont.Glyph glyph :run.glyphs) {
                    System.out.println(Character.toString((char) glyph.id) + " " + glyph.height + " " + glyph.height * Strat7.font.getData().scaleY);
                }
            }
        }
        */



        textWidth  = layout.width;
        setWidth(textWidth / mainFrame.getScale());
        //height = layout.runs.size * Strat7.font.getLineHeight();
        textHeight = layout.height;
        setHeight(textHeight / mainFrame.getScale());

        double heightCoef = textHeight / getMaxHeight();
        if(getMaxHeight() <= 0 || heightCoef < 1) {
            return;
        }

/*
        for(GlyphLayout.GlyphRun run :runs) {
            if(run.glyphs.size > 0) {
                if(run.glyphs.get(run.glyphs.size - 1).toString().equals(" ")) {
                    if(run.width == layout.width)
                        layout.width -= run.glyphs.get(run.glyphs.size - 1).width * Strat7.font.getData().scaleX;
                    run.width -= run.glyphs.get(run.glyphs.size - 1).width * Strat7.font.getData().scaleX;
                    run.glyphs.removeIndex(run.glyphs.size - 1);
                }
            }
        }
*/



        // System.out.println(width);
        // System.out.println(height);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        textChanged = true;
        textScale = 1;
    }

    public void setColor(Color color) {
        this.color = color;
        textChanged = true;
    }

    public double getMaxHeight() {
        return maxHeight > 0 ? maxHeight * mainFrame.getScale() : screenHeight;
    }

    public double getMaxWidth() {
        return maxWidth > 0 ?maxWidth * mainFrame.getScale() : screenWidth;
    }

    public void setMaxHeight(double maxHeight) {
        this.maxHeight = maxHeight;
    }
    public void setMaxWidth(double maxWidth) {
        this.maxWidth = maxWidth;
    }

    public double getTextScale() {
        return textScale ;
    }

    @Override
    public double getWidth() {
        return textWidth;
    }

    @Override
    public double getHeight() {
        return textHeight;
    }

    @Override
    public double getLocalWidth() {
        return super.getLocalWidth();
    }

    @Override
    public double getLocalHeight() {
        return super.getLocalHeight();
    }
}


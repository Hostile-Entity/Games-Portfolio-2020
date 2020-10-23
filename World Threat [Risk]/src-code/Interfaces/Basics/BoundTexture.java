package com.strat7.game.Interfaces.Basics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.strat7.game.Interfaces.Basics.BoundBasics.*;
import com.strat7.game.Strat7;

/**
 * Created by Евгений on 14.08.2017.
 */

public class BoundTexture extends DrawableBound{
    private Texture texture;
    private double textureScaleX;
    private double textureScaleY;

    private Color color;

    public BoundTexture(Carcass frame, Texture texture, double l, double b, double s, Color color) {
        this(frame,texture,l,b,texture == null? 0 : texture.getWidth(),texture == null? 0 : texture.getHeight(),s,color);
    }

    public BoundTexture(Carcass frame, Texture texture, double l, double b, double w, double h, double s, Color color ) {
        super(frame, l, b, w, h,s);
        this.texture = texture;
        this.color = color;
        if(texture != null) {
            textureScaleX = w / texture.getWidth();
            textureScaleY = h / texture.getHeight();
        }
    }
    public BoundTexture(Carcass frame, Carcass copyParameters, Texture texture, Color color ) {
        super(frame, copyParameters);
        this.texture = texture;
        this.color = color;
        if(texture != null) {
            textureScaleX = getLocalWidth() / texture.getWidth();
            textureScaleY = getLocalHeight()/ texture.getHeight();
        }
    }

    public void draw(boolean considerBorders) {
        if(isHided())
            return;
        if (considerBorders) {
            Carcass absolute = allowedAbsoluteIntegerRegion();
            if (absolute == null)
                return;
            Strat7.openBufferStream();
            draw(false);
            Strat7.closeBufferStream();
            Strat7.flushBuffer(absolute);
        } else {
            commonDraw();
        }
    }

    private void commonDraw() {
        Color color = null;
        if(this.color != null) {
            color = Strat7.getCurrentBatch().getColor();
            Strat7.getCurrentBatch().setColor(this.color);
        }
        Strat7.getCurrentBatch().draw(texture, (float) getPosX(), (float) getPosY(), (float) getWidth(), (float) getHeight());
        if(this.color != null)
            Strat7.getCurrentBatch().setColor(color);
    }

    public void drawCenterIn(double x, double y,boolean considerBorders) {
        if(isHided()){
            return;
        }
        if(considerBorders) {
            Carcass absolute = allowedAbsoluteIntegerRegion();
            if(absolute == null)
                return;
            Strat7.openBufferStream();
            drawCenterIn(x,y,false);
            Strat7.closeBufferStream();
            Strat7.flushBuffer(absolute);
        }
        else {
            commonDrawInCenter(x, y);
        }
    }
    private void commonDrawInCenter(double x, double y) {
        Color color = null;
        if(this.color != null) {
            color = Strat7.getCurrentBatch().getColor();
            Strat7.getCurrentBatch().setColor(this.color);
        }
        Strat7.getCurrentBatch().draw(texture,(float)getPosXInCenter(x),(float)getPosYInCenter(y),(float)getWidth(),(float)getHeight());
        if(this.color != null)
            Strat7.getCurrentBatch().setColor(color);
    }

    private double getPosXInCenter(double x) {
        return mainFrame.getPosX() + mainFrame.getScale() * (x - getLocalWidth() / 2);
    }
    private double getPosYInCenter(double y) {
        return mainFrame.getPosY() + mainFrame.getScale() * (y - getLocalHeight() / 2);
    }

    @Override
    public Frame allowedLocalRegion() {
        Frame frame = super.allowedLocalRegion();
        if(frame == null)
            return null;
        frame.setPosX(frame.getLocalPosX() - getLocalPosX());
        frame.setPosY(texture.getHeight() - (int)(frame.getLocalPosY() - getLocalPosY() + (frame.getLocalHeight() / textureScaleY)));
        frame.setWidth(frame.getLocalWidth() / textureScaleX);
        frame.setHeight(frame.getLocalHeight() / textureScaleY);
        return frame;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    public Color getColor() {
        return color;
    }
}

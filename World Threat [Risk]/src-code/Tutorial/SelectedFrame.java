package com.strat7.game.Tutorial;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.strat7.game.Interfaces.Basics.BoundBasics.BoundWithFrame;
import com.strat7.game.Interfaces.Basics.BoundBasics.Carcass;
import com.strat7.game.Interfaces.Basics.BoundBasics.DrawableBound;
import com.strat7.game.Interfaces.Basics.BoundBasics.Frame;
import com.strat7.game.Interfaces.Basics.Interface;
import com.strat7.game.Screens.Manager.AppScreen;
import com.strat7.game.Screens.Manager.ProcessingArea;
import com.strat7.game.Strat7;

import java.util.ArrayList;

/**
 * Created by Евгений on 10.08.2017.
 */

public class SelectedFrame extends DrawableBound {
    private static final float STANDARD_TIME = 0.2f;
    private static final float BORDER_LOOP_PART = 0.2f;
    private final float BORDER_LOOP_TIME;

    private float timePassed;
    private float moveTime;
    private float deltaTime;
    private Color color;

    Texture topLeftCorner;
    Texture topRightCorner;
    Texture bottomRightCorner;
    Texture bottomLeftCorner;
    Texture topEdge;
    Texture rightEdge;
    Texture bottomEdge;
    Texture leftEdge;

    private Interface background;

    private Carcass oldFrame;
    private Carcass newFrame;

    private float topEdgeWidth;
    private float rightEdgeWidth;
    private float bottomEdgeWidth;
    private float leftEdgeWidth;

    public SelectedFrame(){
        this("Interface\\Tutorial\\Corner.png",
                "Interface\\Tutorial\\Edge.png",
                STANDARD_TIME, 0, Frame.deltaH
        );
    }

    public SelectedFrame(String cornerTexture, String edgeTexture, float moveTime, float borderLoop, double edgeWidth) {
        this(
                cornerTexture,cornerTexture,cornerTexture,cornerTexture,
                edgeTexture,edgeTexture,edgeTexture,edgeTexture,
                moveTime, borderLoop,edgeWidth,edgeWidth,edgeWidth,edgeWidth
        );
    }
    public SelectedFrame(
            String topLeftCorner,
            String topRightCorner,
            String bottomRightCorner,
            String bottomLeftCorner,
            String topEdge,
            String rightEdge,
            String bottomEdge,
            String leftEdge,
            float moveTime,
            float borderLoop,
            double topEdgeWidth,
            double rightEdgeWidth,
            double bottomEdgeWidth,
            double leftEdgeWidth
    ) {
        super(MAIN_FRAME);
        this.topLeftCorner     = Strat7.graphics.getTextureByName(topLeftCorner);
        this.topRightCorner    = Strat7.graphics.getTextureByName(topRightCorner);
        this.bottomRightCorner = Strat7.graphics.getTextureByName(bottomRightCorner);
        this.bottomLeftCorner  = Strat7.graphics.getTextureByName(bottomLeftCorner);
        this.topEdge    = Strat7.graphics.getTextureByName(topEdge);
        this.rightEdge  = Strat7.graphics.getTextureByName(rightEdge);
        this.bottomEdge = Strat7.graphics.getTextureByName(bottomEdge);
        this.leftEdge   = Strat7.graphics.getTextureByName(leftEdge);

        BORDER_LOOP_TIME = borderLoop;
        this.moveTime = moveTime;
        this.topEdgeWidth    = (float)topEdgeWidth;
        this.rightEdgeWidth  = (float)rightEdgeWidth;
        this.bottomEdgeWidth = (float)bottomEdgeWidth;
        this.leftEdgeWidth   = (float)leftEdgeWidth;
        oldFrame = newFrame = null;
        deltaTime = 0;
        timePassed = 0;
        color = Color.WHITE;
    }

    @Override
    public Frame allowedAbsoluteRegion() {
        return ((Frame)getMainFrame()).allowedAbsoluteRegion();
    }

    public void setNewFrame(ArrayList<Carcass> buttons, Interface background) throws UnsupportedOperationException {
        this.background = background;
        deltaTime = 0;
        if(buttons.size() == 0)
            return;
        double x = -1, y = -1, w = -1, h = -1;
        Carcass frame = buttons.get(0).getMainFrame();
        for (Carcass button : buttons) {
            if(button.getMainFrame() != frame)
                throw new UnsupportedOperationException ("different main Frames");
            if (x == -1) {
                x = button.getLocalPosX();
                y = button.getLocalPosY();
                w = button.getLocalWidth();
                h = button.getLocalHeight();
            }
            if (x <= button.getLocalPosX() && w < button.getLocalPosX() - x + button.getWidth()) {
                w = button.getLocalPosX() - x + button.getWidth();
            }
            if (x > button.getLocalPosX()) {
                w = Math.max(x - button.getLocalPosX() + w, button.getWidth());
                x = button.getLocalPosX();
            }

            if (y <= button.getLocalPosY() && h < button.getLocalPosY() - y + button.getLocalHeight()) {
                h = button.getLocalPosY() - y + button.getLocalHeight();
            }
            if (y > button.getLocalPosY()) {
                h = Math.max(y - button.getLocalPosX() + h, button.getLocalHeight());
                y = button.getLocalPosY();
            }
        }
        if(newFrame != null) {
            oldFrame = newFrame;
            newFrame = new BoundWithFrame(frame,x, y, w, h, 1);
        }
        else
            oldFrame = newFrame = new BoundWithFrame(frame,x, y, w, h, 1);
    }
    public void setNewFrame(Carcass relativeFrame, Interface background) {
        deltaTime = 0;
        oldFrame = newFrame;
        newFrame = relativeFrame;
        this.background = background;
    }

    public void draw(float delta, ProcessingArea curDrawnInterface){
        if(background != curDrawnInterface)
            return;
        deltaTime += delta;
        if(newFrame == null)
            return;
        if(oldFrame == null || deltaTime >= moveTime) {
            draw(newFrame.toAbsoluteFrame(), curDrawnInterface, delta);
            return;
        }
        Frame intermediateFrame = new Frame(
                oldFrame.getPosX() + (newFrame.getPosX() - oldFrame.getPosX()) * deltaTime / moveTime,
                oldFrame.getPosY() + (newFrame.getPosY() - oldFrame.getPosY()) * deltaTime / moveTime,
                oldFrame.getWidth() + (newFrame.getWidth() - oldFrame.getWidth()) * deltaTime / moveTime,
                oldFrame.getHeight() + (newFrame.getHeight() - oldFrame.getHeight()) * deltaTime / moveTime,
                1
        );
        draw(intermediateFrame, curDrawnInterface, delta);
    }

    void draw(Carcass frame,ProcessingArea curDrawnInterface, float delta) {
        if(background != curDrawnInterface)
            return;
        draw(
                (float)frame.getPosX(),
                (float)frame.getPosY(),
                (float)frame.getWidth(),
                (float)frame.getHeight(),
                delta
        );
    }

    private void draw(float x, float y, float w, float h, float delta) {
        Color colorBuf = Strat7.getCurrentBatch().getColor();
        Strat7.getCurrentBatch().setColor(getColor());
        float coef;
        if (BORDER_LOOP_TIME != 0)
            coef = (1 + (1 - Math.abs(timePassed / BORDER_LOOP_TIME)) * BORDER_LOOP_PART);
        else
            coef = 1;

        float topEdgeWidth = this.topEdgeWidth * coef;
        float rightEdgeWidth = this.rightEdgeWidth * coef;
        float bottomEdgeWidth = this.bottomEdgeWidth * coef;
        float leftEdgeWidth = this.leftEdgeWidth * coef;

        timePassed += (timePassed > BORDER_LOOP_TIME * 2 ? -4 * BORDER_LOOP_TIME : 0) + delta;


        // left edge
        Strat7.getCurrentBatch().draw(leftEdge, x - leftEdgeWidth, y,
                leftEdgeWidth / 2, leftEdgeWidth / 2, h, leftEdgeWidth,
                1, 1, 90, 0, 0, leftEdge.getWidth(), leftEdge.getHeight(), false, false);
        // top edge
        Strat7.getCurrentBatch().draw(topEdge, x, y + h,
                w / 2, topEdgeWidth / 2, w, topEdgeWidth,
                1, 1, 0, 0, 0, topEdge.getWidth(), topEdge.getHeight(), false, false);
        // right edge
        Strat7.getCurrentBatch().draw(rightEdge, x + w, y,
                rightEdgeWidth / 2, rightEdgeWidth / 2, h, rightEdgeWidth,
                1, 1, 90, 0, 0, rightEdge.getWidth(), rightEdge.getHeight(), false, true);
        // bottom edge
        Strat7.getCurrentBatch().draw(bottomEdge, x, y - bottomEdgeWidth,
                w / 2, bottomEdgeWidth / 2, w, bottomEdgeWidth,
                1, 1, 0, 0, 0, bottomEdge.getWidth(), bottomEdge.getHeight(), false, true);


        // left bottom corner
        Strat7.getCurrentBatch().draw(bottomLeftCorner, x - leftEdgeWidth, y - bottomEdgeWidth,
                leftEdgeWidth / (float) 2, bottomEdgeWidth / (float) 2, leftEdgeWidth, bottomEdgeWidth,
                1, 1, 0, 0, 0, bottomLeftCorner.getWidth(), bottomLeftCorner.getHeight(), false, true);
        // left top corner
        Strat7.getCurrentBatch().draw(topLeftCorner, x - leftEdgeWidth, y + h,
                leftEdgeWidth / (float) 2, topEdgeWidth / (float) 2, leftEdgeWidth, topEdgeWidth,
                1, 1, 0, 0, 0, topLeftCorner.getWidth(), topLeftCorner.getHeight(), false, false);
        // right top corner
        Strat7.getCurrentBatch().draw(topRightCorner, x + w, y + h,
                rightEdgeWidth / (float) 2, topEdgeWidth / (float) 2, rightEdgeWidth, topEdgeWidth,
                1, 1, 0, 0, 0, topRightCorner.getWidth(), topRightCorner.getHeight(), true, false);
        // right bottom corner
        Strat7.getCurrentBatch().draw(bottomRightCorner, x + w, y - bottomEdgeWidth,
                rightEdgeWidth / (float) 2, bottomEdgeWidth / (float) 2, rightEdgeWidth, bottomEdgeWidth,
                1, 1, 0, 0, 0, bottomRightCorner.getWidth(), bottomRightCorner.getHeight(), true, true);

        Strat7.getCurrentBatch().setColor(colorBuf);
    }

    @Override
    public void draw(boolean considerBorders) {
        if (isHided())
            return;
        if(considerBorders) {
            Frame absolute = allowedAbsoluteRegion();
            if (absolute == null) {
                return;
            }
            Strat7.openBufferStream();
            draw(false);
            Strat7.closeBufferStream();
            Strat7.flushBuffer(absolute);
        }
        else {
            draw(AppScreen.getDelta(), background);
        }
    }

    public void clear() {
        deltaTime = 0;
        oldFrame = newFrame = null;
    }

    public void setTopEdgeWidth(float topEdgeWidth) {
        this.topEdgeWidth = topEdgeWidth;
    }
    public void setRightEdgeWidth(float rightEdgeWidth) {
        this.rightEdgeWidth = rightEdgeWidth;
    }
    public void setBottomEdgeWidth(float bottomEdgeWidth) {
        this.bottomEdgeWidth = bottomEdgeWidth;
    }
    public void setLeftEdgeWidth(float leftEdgeWidth) {
        this.leftEdgeWidth = leftEdgeWidth;
    }

    public void setAllEdgesWidth (float topEdgeWidth, float rightEdgeWidth, float bottomEdgeWidth, float leftEdgeWidth) {
        setTopEdgeWidth(topEdgeWidth);
        setRightEdgeWidth(rightEdgeWidth);
        setBottomEdgeWidth(bottomEdgeWidth);
        setLeftEdgeWidth(leftEdgeWidth);
    }

    public void setAllEdgesWidth (float edgeWidth) {
        setAllEdgesWidth(edgeWidth,edgeWidth,edgeWidth,edgeWidth);
    }
    public void setAllEdgesWidth (float topEdgesWidth, float lateralEdgesWidth) {
        setAllEdgesWidth(topEdgesWidth,lateralEdgesWidth,topEdgesWidth,lateralEdgesWidth);
    }

    public Carcass getNewFrame() {
        return newFrame;
    }
    public Carcass getOldFrame() {
        return oldFrame;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}

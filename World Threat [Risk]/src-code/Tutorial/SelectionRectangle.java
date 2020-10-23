package com.strat7.game.Tutorial;

import com.badlogic.gdx.graphics.Texture;
import com.strat7.game.Interfaces.Basics.BoundBasics.BoundWithFrame;
import com.strat7.game.Interfaces.Basics.BoundBasics.Frame;
import com.strat7.game.Strat7;

import java.util.ArrayList;

/**
 * Created by Евгений on 10.08.2017.
 */

class SelectionRectangle {
    Texture cornerTexture;
    Texture edgeTexture;

    private ArrayList<BoundWithFrame> frames;
    private boolean together;

    SelectionRectangle() {
        cornerTexture = new Texture("Interface\\Tutorial\\Corner.png");
        edgeTexture = new Texture("Interface\\Tutorial\\Edge.png");
    }

    public void setButtons(ArrayList<BoundWithFrame> frames, boolean together) {
        this.frames = frames;
        this.together = together;
    }

    public int size() {
        return frames == null ? 0 : frames.size();
    }

    public void clear() {
        if(frames != null)
            frames.clear();
    }

    void draw() {
        if(size() == 0)
            return;
        double x = -1, y = -1, w = -1, h = -1;
        if(together) {
            for (BoundWithFrame button : frames) {
                if (x == -1) {
                    x = button.getPosX();
                    y = button.getPosY();
                    w = button.getWidth();
                    h = button.getHeight();
                }
                if (x <= button.getPosX() && w < button.getPosX() - x + button.getWidth()) {
                    w = button.getPosX() - x + button.getWidth();
                }
                if (x > button.getPosX()) {
                    w = Math.max(x - button.getPosX() + w, button.getWidth());
                    x = button.getPosX();
                }

                if (y <= button.getPosY() && h < button.getPosY() - y + button.getHeight()) {
                    h = button.getPosY() - y + button.getHeight();
                }
                if (y > button.getPosY()) {
                    h = Math.max(y - button.getPosX() + h, button.getHeight());
                    y = button.getPosY();
                }
            }
        }
        else {
            for (BoundWithFrame button : frames) {
                draw(button);
            }
            return;
        }
        draw((float)x,(float)y,(float)w,(float)h);
    }
    void draw(BoundWithFrame frame) {
        draw(
                (float)frame.getPosX(),
                (float)frame.getPosY(),
                (float)frame.getWidth(),
                (float)frame.getHeight()
        );
    }

    void draw (float x, float y, float w, float h) {
        float deltaH = (float) Frame.deltaH;
        // left edge
        Strat7.getCurrentBatch().draw(edgeTexture,x - deltaH, y , deltaH , h);
        // top edge
        Strat7.getCurrentBatch().draw(edgeTexture,x , y + h, w, deltaH);
        // right edge
        Strat7.getCurrentBatch().draw(edgeTexture,x + w, y , deltaH, h);
        // bottom edge
        Strat7.getCurrentBatch().draw(edgeTexture,x , y - deltaH, w, deltaH);


        // left bottom edge
        Strat7.getCurrentBatch().draw(cornerTexture,x - deltaH, y - deltaH,
                deltaH / (float) 2, deltaH/ (float) 2, deltaH , deltaH,
                1, 1, 0, 0, 0, cornerTexture.getWidth(),cornerTexture.getWidth(),false,true);
        // left top edge
        Strat7.getCurrentBatch().draw(cornerTexture,x - deltaH, y + h,
                deltaH / (float) 2, deltaH/ (float) 2, deltaH , deltaH,
                1, 1, 0, 0, 0, cornerTexture.getWidth(),cornerTexture.getWidth(),false,false);
        // right top edge
        Strat7.getCurrentBatch().draw(cornerTexture,x + w, y + h,
                deltaH / (float) 2, deltaH/ (float) 2, deltaH , deltaH,
                1, 1, 0, 0, 0, cornerTexture.getWidth(),cornerTexture.getWidth(),true,false);
        // right bottom edge
        Strat7.getCurrentBatch().draw(cornerTexture,x + w, y - deltaH,
                deltaH / (float) 2, deltaH/ (float) 2, deltaH , deltaH,
                1, 1, 0, 0, 0, cornerTexture.getWidth(),cornerTexture.getWidth(),true,true);

    }
}

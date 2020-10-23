package com.strat7.game.GameInfo.PictureChanges;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.strat7.game.GameInfo.GameState;
import com.strat7.game.GameInfo.Province;
import com.strat7.game.Interfaces.Basics.BoundBasics.Frame;
import com.strat7.game.Strat7;

/**
 * Created by Евгений on 27.07.2017.
 */

public class Arrows {
    private GameState state;
    private Texture arrowTexture ;
    private Texture swordTexture ;
    private Texture circleTexture ;

    private long drawBegins;

    private Province selectedProvince ;

    public Arrows(GameState state) {
        this.state = state;
        drawBegins = System.nanoTime();
        arrowTexture = new Texture("Icons/Arrows/Arrow.png");
        swordTexture = new Texture("Icons/Substrates/Sword.png");
        circleTexture= new Texture("Icons/Substrates/MoveCircle.png");
    }


    public void draw (CameraMovement cameraMovement) {
        Strat7.getCurrentBatch().setColor(Color.FIREBRICK);
        Strat7.getCurrentBatch().draw(
                arrowTexture, // Texture
                150,150 + 8 * (float)Frame.deltaH,          // offsets
                10,//arrowTexture.getWidth() / 2 + arrowTexture.getWidth() % 2,         // centre position
                10,//arrowTexture.getTopOffset()/ 2 + arrowTexture.getTopOffset()% 2,         // centre position
                20,20,      // total size in pixels
                1,1,          // as i understand its scale
                -45,            // rotation in degrees
                0,0,          // don`t understand
                arrowTexture.getWidth(),    // texture parameters
                arrowTexture.getHeight(),   // texture parameters
                false,false
        ); // flip
        switch (state.getCurrentState()) {
            case 0:
                break;
            case 1:
                if(state.getLightenedProvinces().size() == 0)
                    return;
                Province chosenProvince = state.getLightenedProvinces().get(0);
                if(selectedProvince != null) {

                }
                else {

                }
                break;
            case 2:
                break;
            case 3:
                break;

        }
    }




    public void setSelectedProvince(Province selectedProvince) {
        this.selectedProvince = selectedProvince;
    }
    public Province getSelectedProvince() {
        return selectedProvince;
    }
}

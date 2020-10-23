package com.strat7.game.Interfaces.Basics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.strat7.game.Interfaces.Basics.BoundBasics.BoundWithFrame;
import com.strat7.game.Interfaces.Basics.BoundBasics.Carcass;
import com.strat7.game.Interfaces.Basics.BoundBasics.Frame;

/**
 * Created by Евгений on 14.08.2017.
 */

public class CarcassManager {
    public static final int LOCAL = 1 ; // ABSOLUTE - 0
    public static final int BOUND_FRAMES = 1 ; // DO NOT BOUND - 0


    public static Carcass textureToCarcass(Texture texture) {
        Frame frame = new Frame(
                0, 0,
                texture.getWidth(),
                texture.getHeight(),
                1
        );
        return frame;
    }

}

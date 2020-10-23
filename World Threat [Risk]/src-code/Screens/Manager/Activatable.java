package com.strat7.game.Screens.Manager;

public interface Activatable {
    void activate   ();
    void deactivate ();

    boolean isDeactivated();
    boolean isActivated  ();

    void activateTextures();
    void deactivateTextures();
}

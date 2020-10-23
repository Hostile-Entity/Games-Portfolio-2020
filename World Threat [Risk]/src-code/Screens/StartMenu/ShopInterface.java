package com.strat7.game.Screens.StartMenu;


import com.badlogic.gdx.graphics.Color;
import com.strat7.game.Interfaces.Basics.BoundBasics.BoundWithFrame;
import com.strat7.game.Interfaces.Basics.BoundBasics.Carcass;
import com.strat7.game.Interfaces.Basics.BoundTexture;
import com.strat7.game.Interfaces.Basics.Button;
import com.strat7.game.Interfaces.Basics.Interface;
import com.strat7.game.Interfaces.Basics.Text.BoundText;
import com.strat7.game.Interfaces.Basics.Text.PlainText;
import com.strat7.game.Interfaces.ButtonWithCoin;
import com.strat7.game.Interfaces.InterfaceCommon;
import com.strat7.game.Strat7;

public class ShopInterface extends InterfaceCommon{
    public static final int WATCH_VIDEO = 0;
    public static final int NO_ADS = 1;
    public static final int BACK_BUTTON = 2;

    private PlainText watchVideo;
    private PlainText noAds;
    private PlainText shop;

    private BoundTexture background;

    public ShopInterface(Carcass screen) {
        super(screen, FULL_SCREEN_INTERFACE);
        scale = 1;

        buttonAmount = 2;
        buttonArray = new Button[buttonAmount];

        rulesForButtons = new Runnable[buttonAmount + 1];

        background = new BoundTexture(
                this,
                Strat7.graphics.getTextureByName("Interface/BackForText.png"),
                (2 + 14 + 1) * deltaW, 4 * deltaH,
                32 * deltaW, 40 * deltaH, 1, Strat7.backColor
        );

        int shift = WATCH_VIDEO;
        for (int i = 0; i < 2; i++) {
            buttonArray[i + shift] = new ButtonWithCoin(
                    this,
                    (50 - 14 - 2)*deltaW,
                    50*deltaH - (i + 1)*(5+2)*deltaH - 11*deltaH,
                    14*deltaW, 5*deltaH,
                    "Interface/Button1.png",
                    Strat7.buttonColor,
                    "Interface/Button2.png"
            );
            buttonArray[i + shift].setVisible(true);
        }
        buttonArray[WATCH_VIDEO].setText("+10");
        buttonArray[NO_ADS].setText("-90");
        if(!Strat7.economy.adsOn())
            buttonArray[NO_ADS].setBlocked(true);

        shop = new PlainText(
                this,
                (2+14+2)*deltaW, 50*deltaH - 11*deltaH,
                30 * deltaW, 4 * deltaH, Strat7.myBundle.get("shop"), Color.WHITE
        );
        watchVideo = new PlainText(
                this,
                (2+14+2)*deltaW, buttonArray[WATCH_VIDEO].getLocalPosY() + 1*deltaH,
                30 * deltaW, 3 * deltaH, Strat7.myBundle.get("watch_video") + ": ", Color.WHITE
        );
        noAds = new PlainText(
                this,
                (2+14+2)*deltaW, buttonArray[NO_ADS].getLocalPosY() + 1*deltaH,
                30 * deltaW, 3 * deltaH,  Strat7.myBundle.get("remove_ads") + ": ", Color.WHITE
        );
    }

    @Override
    protected void commonDraw() {
        background.draw(false);

        Strat7.font.setColor(Color.WHITE);
        shop.draw(false);
        buttonArray[WATCH_VIDEO].draw(false);
        watchVideo.draw(false);
        buttonArray[NO_ADS].draw(false);
        noAds.draw(false);

        Strat7.getCurrentBatch().setColor(Color.WHITE);
        drawLine((2 + 14 + 1) * deltaW, 4 * deltaH);
        drawLine((49) * deltaW, 4 * deltaH);
    }

    @Override
    public boolean outOfBorders(int X, int Y) {
        return !(new BoundWithFrame(getMainFrame(), SAVES_INTERFACE)).contains(X,Y);
    }

    @Override
    protected boolean callBackButton() {
        rulesForButtons[BACK_BUTTON].run();
        return true;
    }
}

package com.strat7.game.Screens.StartMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.strat7.game.GameInfo.GameSaver;
import com.strat7.game.Interfaces.Basics.Interface;
import com.strat7.game.Interfaces.InterfaceCommon;
import com.strat7.game.Interfaces.InterfaceStacks.AppearanceParameters;
import com.strat7.game.Screens.Game.GameScreen;
import com.strat7.game.Screens.ListenableScreen;
import com.strat7.game.Screens.MainMenu.MainMenuScreen;
import com.strat7.game.Screens.Manager.AppearParameter;
import com.strat7.game.Strat7;
import com.strat7.game.Tutorial.TutorialMainMenuScreen;


/**
 * Created by Юра on 12.07.2017.
 */

public class StartMenuScreen extends ListenableScreen {

    private InterfaceStartMenu buttons;
    private InterfaceCommon common;
    private SaveInterface saves;
    private ShopInterface shop;

    // passive interfaces now : shop, load
    // main interface         : buttons

    public StartMenuScreen() {
        Gdx.gl.glClearColor(Strat7.backColor.r, Strat7.backColor.g, Strat7.backColor.b,Strat7.backColor.a);

        initStartMenuInterface();
        initSaveInterface();
        initShop();
        updateGameNames();
        if (Strat7.economy.ifOnlineAvailable()) {
            buttons.get(InterfaceStartMenu.ONLINE_BUTTON).setBlocked(false);
        } else {
            buttons.get(InterfaceStartMenu.ONLINE_BUTTON).setBlocked(true);
        }
//        Strat7.economy.showBanner(true);

        //plainText = new PlainText(2*Strat7.deltaW, 39*Strat7.deltaH, 14*Strat7.deltaW, 5*Strat7.deltaH, "Test text just testing another one");

        common = new InterfaceCommon(this, Interface.FULL_SCREEN_INTERFACE);

        stack.activateInterface(buttons , true, new AppearanceParameters(buttons));

    }

    protected void initStartMenuInterface() {
        buttons = new InterfaceStartMenu(this);
        buttons.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        playButtonClicked();
                    }
                },
                InterfaceStartMenu.PLAY_BUTTON
        );
        buttons.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        continueButtonClicked();
                    }
                },
                InterfaceStartMenu.CONTINUE_BUTTON
        );
        buttons.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        loadButtonClicked();
                    }
                },
                InterfaceStartMenu.LOAD_BUTTON
        );
        buttons.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        playOnlineButtonClicked();
                    }
                },
                InterfaceStartMenu.ONLINE_BUTTON
        );
        buttons.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        shopButtonClicked();
                    }
                },
                InterfaceStartMenu.SHOP_BUTTON
        );
        buttons.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        tutorialButtonClicked();
                    }
                },
                InterfaceStartMenu.TUTORIAL_BUTTON
        );
        buttons.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        changeLanguage();
                    }
                },
                InterfaceStartMenu.LANGUAGE_CHANGE_BUTTON
        );
        buttons.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        backButtonClicked();
                    }
                },
                InterfaceStartMenu.BACK_BUTTON
        );
        initPlayButton();
    }

    protected void initSaveInterface() {
        saves = new SaveInterface(this);
        for(int i = 0; i < SaveInterface.MAX_SAVES_AMOUNT; i ++) {
            final int j = SaveInterface.FIRST_SAVE_BUTTON + i;

            saves.setRuleForButton(
                    new Runnable() {
                        @Override
                        public void run() {
                            changeSaveButtonPressed(j - SaveInterface.FIRST_SAVE_BUTTON);
                        }
                    },
                    j
            );
        }
        for(int i = 0; i < SaveInterface.MAX_SAVES_AMOUNT; i ++) {
            final int j = SaveInterface.FIRST_DELETE_SAVE_BUTTON + i;
            saves.setRuleForButton(
                    new Runnable() {
                        @Override
                        public void run() {
                            deleteSave(j - SaveInterface.FIRST_DELETE_SAVE_BUTTON);
                        }
                    },
                    j
            );
        }
        saves.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        backButtonClicked();
                    }
                },
                SaveInterface.BACK_BUTTON
        );
    }

    protected void initShop() {
        shop = new ShopInterface(this);

        shop.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        showVideo();
                    }
                },
                ShopInterface.WATCH_VIDEO
        );
        shop.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        removeAds();
                    }
                },
                ShopInterface.NO_ADS
        );
        shop.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        backButtonClicked();
                    }
                },
                ShopInterface.BACK_BUTTON
        );
    }
    @Override
    public void backButtonClicked() {
        if(stack.isActivated(saves) ) {
            stack.deactivateInterface(saves);
            initPlayButton();
            return;
        }
        if(stack.isActivated(shop) ) {
            stack.deactivateInterface(shop);
            initPlayButton();
            return;
        }
        // Gdx.app.exit();
    }

    private void removeAds() {
        if (Strat7.economy.getMoney() >= 90) {
            Strat7.economy.changeMoney(-90);
            Strat7.economy.removeAds();
            shop.get(ShopInterface.NO_ADS).setBlocked(true);
        } else {
            Strat7.economy.notEnoughMoney();
        }
    }

    private void showVideo() {
        Strat7.economy.adHandler.showVideo();
    }

    private void deleteSave(int saveNum) {
        saveGameNames();
        GameSaver.makeSaveFirst(GameSaver.savesAmount() - saveNum - 1);
        saves.get(GameSaver.savesAmount() - 1 + SaveInterface.FIRST_SAVE_BUTTON).setVisible(false);
        saves.get(GameSaver.savesAmount() - 1 + SaveInterface.FIRST_SAVE_BUTTON).setBlocked(true);
        saves.get(GameSaver.savesAmount() - 1 + SaveInterface.FIRST_DELETE_SAVE_BUTTON).setVisible(false);
        saves.get(GameSaver.savesAmount() - 1 + SaveInterface.FIRST_DELETE_SAVE_BUTTON).setBlocked(true);
        saves.getTextField(GameSaver.savesAmount() - 1).setVisible(false);
        saves.getTextField(GameSaver.savesAmount() - 1).setBlocked(true);
        GameSaver.deleteFirstSave();
        updateGameNames();
        if (GameSaver.savesAmount() == 0) {
            loadButtonClicked();
        }
    }

    private void changeLanguage() {
        Strat7.changeLanguage();

        int num = stack.interfaceNumber(buttons);
        initStartMenuInterface();
        stack.replaceInterface(num, buttons);

        num = stack.interfaceNumber(shop);
        initShop();
        stack.replaceInterface(num, shop);

        num = stack.interfaceNumber(saves);
        initSaveInterface();
        stack.replaceInterface(num, saves);

        updateGameNames();
        Strat7.preferences.putString("language", Strat7.locale.getLanguage());
        Strat7.preferences.flush();
        if (Strat7.economy.ifOnlineAvailable()) {
            buttons.get(InterfaceStartMenu.ONLINE_BUTTON).setBlocked(false);
        } else {
            buttons.get(InterfaceStartMenu.ONLINE_BUTTON).setBlocked(true);
        }
    }

    private void tutorialButtonClicked() {
        dispose();
        Strat7.context.setScreen(new TutorialMainMenuScreen());
    }

    private void shopButtonClicked() {
        if(stack.getInterfaceNum(shop) >= 0) {
            stack.deactivateInterface(shop);
        } else {
            stack.activateInterface(shop,false,new AppearanceParameters(shop, AppearanceParameters.FAST, AppearanceParameters.FROM_RIGHT));
            if(stack.isActivated(saves)) {
                loadButtonClicked();
            }
        }
    }

    private void loadButtonClicked() {
        if(stack.getInterfaceNum(saves) >= 0) {
            deactivateInterface(saves, false);
            if (saves.getTextField(0).getText().getText().equals("200 WT give")) {
                Strat7.economy.changeMoney(200);
            } else if (saves.getTextField(0).getText().getText().equals("0 WT online")) {
                Strat7.economy.availableOnline();
            }
        }
        else {
            if(stack.isActivated(shop))
                shopButtonClicked();
            buttons.get(0).setText(Strat7.myBundle.get("play"));
            buttons.get(1).setBlocked(true);
            stack.activateInterface(saves,false,new AppearanceParameters(saves, AppearanceParameters.FAST, AppearanceParameters.FROM_RIGHT));
        }
        initPlayButton();
    }

    private void initPlayButton() {
        if(stack.isActivated(saves)) {
            buttons.get(InterfaceStartMenu.PLAY_BUTTON).setText(Strat7.myBundle.get("play"));
            buttons.get(InterfaceStartMenu.CONTINUE_BUTTON).setBlocked(true);
        }
        else {
            buttons.get(InterfaceStartMenu.PLAY_BUTTON).setText(Strat7.myBundle.get("single"));
            if (GameSaver.savesAmount() == 0) {
                buttons.get(1).setBlocked(true);
                buttons.get(2).setBlocked(true);
            } else {
                buttons.get(1).setBlocked(false);
                buttons.get(2).setBlocked(false);
            }
        }
    }

    private void changeSaveButtonPressed(int saveNum) {
        if (Strat7.preferences.contains("Save" + Integer.toString(saveNum))) {
            for (int i = 0; i < 8; i++) {
                saves.get(i + SaveInterface.FIRST_SAVE_BUTTON).setVisible(false);
            }
            saves.get(saveNum + SaveInterface.FIRST_SAVE_BUTTON).setVisible(true);
        }
    }

    public void playButtonClicked(){
        if(!stack.isInStack(saves) || stack.getParameter(saves).getTimePassed() > 0) {
            this.dispose();
            Strat7.context.setScreen(new MainMenuScreen());
        }
        else {
            for (int i = 0; i < 8; i ++){
                if (saves.get(i + SaveInterface.FIRST_SAVE_BUTTON).isVisible()) {
                    saveGameNames();
                    GameSaver.makeSaveFirst(GameSaver.savesAmount()-1 - i);
                    GameScreen offlineGame = GameSaver.loadGame(Strat7.preferences.getString("Save" + Integer.toString(GameSaver.savesAmount()-1)));
                    updateGameNames();
                    if (offlineGame != null) {
                        this.dispose();
                        Strat7.context.setScreen(offlineGame);
                    }
                }
            }
        }
    }

    public void playOnlineButtonClicked(){
        // this.dispose();
        // Strat7.setScreen(new OnlineMainMenuScreen(Strat7));
    }

    public void continueButtonClicked() {
        GameScreen offlineGame = GameSaver.loadGame(Strat7.preferences.getString("Save" + Integer.toString(GameSaver.savesAmount()-1)));
        if (offlineGame != null) {
            this.dispose();
            Strat7.context.setScreen(offlineGame);
        }
    }

    public void saveGameNames() {
        for (int i = 0; i < GameSaver.savesAmount(); i++) {
            GameSaver.changeSaveName(GameSaver.savesAmount() - i - 1, saves.getTextField(i).getText().getText());
        }
    }

    public void updateGameNames() {
        for (int i = 0; i < GameSaver.savesAmount(); i++) {
            saves.getTextField(i).setText(Strat7.preferences.getString(Strat7.preferences.getString("Save" + Integer.toString(GameSaver.savesAmount() - i - 1))));
        }
    }

    @Override
    public void show() { //Called when this screen becomes the current screen for a Strat7.
    }

    @Override
    public void render(float delta) {

        Strat7.timePassed = Gdx.graphics.getDeltaTime();

        Strat7.getCurrentBatch().setColor(Strat7.backColor);
        background.draw(false);
        common.drawLogo();

        drawInterfaces(delta);

        common.drawMoney();
        if(stack.isActivated(saves))
            common.drawTriangle(buttons.get(InterfaceStartMenu.LOAD_BUTTON));
        if(stack.isActivated(shop))
            common.drawTriangle(buttons.get(InterfaceStartMenu.SHOP_BUTTON));


    }

    protected void drawInterfaces(float delta) {
        for(int i = 0; i < stack.size(); i ++)
            stack.draw(i,delta);
        stack.clean();
    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        saveGameNames();
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        //Strat7.economy.showBanner(false);
        //buttons.dispose();
        saveGameNames();
    }

    @Override
    public void deactivate() {
        AppearParameter appearParameter = stack.getParameter(saves);
        if(appearParameter != null)
            appearParameter.pause();
    }
}
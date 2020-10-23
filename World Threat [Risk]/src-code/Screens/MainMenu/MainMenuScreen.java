package com.strat7.game.Screens.MainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.strat7.game.Interfaces.Basics.Interface;
import com.strat7.game.Interfaces.InterfaceCommon;
import com.strat7.game.Interfaces.InterfaceStacks.AppearanceParameters;
import com.strat7.game.GameInfo.PlayersList;
import com.strat7.game.Screens.Game.GameScreen;
import com.strat7.game.Screens.ListenableScreen;
import com.strat7.game.Screens.Manager.AppScreen;
import com.strat7.game.Screens.Manager.BasicScreenAppear;
import com.strat7.game.Screens.StartMenu.StartMenuScreen;
import com.strat7.game.Strat7;

/**
 * Created by Юра on 13.06.2017.
 */

public class MainMenuScreen extends ListenableScreen {
    protected InterfaceMainMenu buttons;
    protected MapSelectInterface maps;
    protected InterfaceCommon common;
    protected PlayerSettingsInterface playerSettings;
    protected SettingsInterface settingsInterface;

    protected PlayersList playersIdList;


    public MainMenuScreen() {
        super();
        if((playersIdList = Strat7.json.fromJson(PlayersList.class, Strat7.preferences.getString("playersIdList"))) == null) {
            playersIdList = new PlayersList();
            playersIdList.makePlayerActive(0);
            playersIdList.getActivePlayer(0).setArtificial(false);
            playersIdList.makePlayerActive(1);
        }
        if (!Strat7.preferences.contains("Map") || !Strat7.economy.isMapAvailable(Strat7.preferences.getString("Map"))) {
            Strat7.preferences.putString("Map", "EarthSmall");
            Strat7.preferences.flush();
        }

        Gdx.gl.glClearColor(Strat7.backColor.r, Strat7.backColor.g, Strat7.backColor.b,Strat7.backColor.a);

        initInterfaceMainMenu();
        initMapSelectScreen();
        initPlayerSettings();
        initSettings();
        common = new InterfaceCommon(this, Interface.FULL_SCREEN_INTERFACE);
        stack.activateInterface(buttons,true,new AppearanceParameters(buttons));
    }

    protected void initSettings() {
        settingsInterface = new SettingsInterface(this, playersIdList);
        settingsInterface.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        MainMenuScreen.this.gameSpeedPressed();
                    }
                },
                SettingsInterface.GAME_SPEED_BUTTON
        );
        settingsInterface.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        MainMenuScreen.this.gameDifficultyPressed();
                    }
                },
                SettingsInterface.GAME_DIFFICULTY_BUTTON
        );
        settingsInterface.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        MainMenuScreen.this.fogOfWarButtonPressed();
                    }
                },
                SettingsInterface.FOG_OF_WAR_BUTTON
        );
        settingsInterface.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        MainMenuScreen.this.gameClickSpeedPressed();
                    }
                },
                SettingsInterface.GAME_CLICK_SPEED_BUTTON
        );
        settingsInterface.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        MainMenuScreen.this.backButtonClicked();
                    }
                },
                SettingsInterface.BACK_TO_PREVIOUS_SCREEN_BUTTON
        );
    }

    protected void initPlayerSettings() {
        playerSettings = new PlayerSettingsInterface(this, playersIdList);
        for (int i = 0; i <= PlayersList.MAX_PLAYER_AMOUNT; i++) {
            final int j = i;
            playerSettings.setRuleForButton(
                    new Runnable() {
                        @Override
                        public void run() {MainMenuScreen.this.changeColorPressed(j);
                        }
                    },
                    PlayerSettingsInterface.FIRST_COLOR_BUTTON + j
            );
        }
        playerSettings.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        MainMenuScreen.this.makePlayerPressed();
                    }
                },
                PlayerSettingsInterface.MAKE_PLAYER_BUTTON
        );
        playerSettings.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        MainMenuScreen.this.makeAIPressed();
                    }
                },
                PlayerSettingsInterface.MAKE_AI_BUTTON
        );
        for (int i = 0; i <= 4; i++){
            final int j = i;
            playerSettings.setRuleForButton(
                    new Runnable() {
                    @Override
                    public void run() {
                        MainMenuScreen.this.AIDifficultyPressed(j);
                    }
                },
                    PlayerSettingsInterface.DUMB_DIFFICULTY_BUTTON + j
            );
        }
        playerSettings.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        MainMenuScreen.this.backButtonClicked();
                    }
                },
                PlayerSettingsInterface.BACK_TO_PREVIOUS_SCREEN_BUTTON
        );
    }

    protected void initMapSelectScreen() {
        maps = new MapSelectInterface(this);
        maps.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        MainMenuScreen.this.previousMap();
                    }
                },
                MapSelectInterface.PREVIOUS_BUTTON
        );
        maps.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                          MainMenuScreen.this.nextMap();
                    }
                },
                MapSelectInterface.NEXT_BUTTON
        );
        for(int i = 0; i < MapSelectInterface.BUY_BUTTON_AMOUNT; i ++ ) {
            final int j = i;
            maps.setRuleForButton(
                    new Runnable() {
                        @Override
                        public void run() {
                            MainMenuScreen.this.buyMap(j);
                        }
                    },
                    MapSelectInterface.FIRST_BUY_BUTTON + j
            );
        }
        maps.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        MainMenuScreen.this.backButtonClicked();
                    }
                },
                MapSelectInterface.BACK_TO_PREVIOUS_SCREEN_BUTTON
        );
    }

    protected void initInterfaceMainMenu() {
        buttons = new InterfaceMainMenu( this, playersIdList);
        buttons.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        MainMenuScreen.this.playButtonPressed();
                    }
                },
                InterfaceMainMenu.GAME_BUTTON
        );
        buttons.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        MainMenuScreen.this.mapButtonPressed();
                    }
                },
                InterfaceMainMenu.MAP_BUTTON
        );
        buttons.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        MainMenuScreen.this.settingsPressed();
                    }
                },
                InterfaceMainMenu.SETTINGS_BUTTON
        );
        buttons.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        MainMenuScreen.this.backButtonClicked();
                    }
                },
                InterfaceMainMenu.BACK_BUTTON
        );
        buttons.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        MainMenuScreen.this.addPlayerButtonPressed();
                    }
                },
                InterfaceMainMenu.ADD_PLAYER_BUTTON
        );
        for(int i = 0; i < PlayersList.MAX_PLAYER_AMOUNT; i ++) {
            final int j = i;
            buttons.setRuleForButton(
                    new Runnable() {
                        @Override
                        public void run() {MainMenuScreen.this.playerSettingsPressed(j);
                        }
                    },
                    InterfaceMainMenu.FIRST_PLAYER_SETTINGS_BUTTON + j
            );
        }
        for(int i = 0; i < PlayersList.MAX_PLAYER_AMOUNT; i ++) {
            final int j = i;
            buttons.setRuleForButton(
                    new Runnable() {
                        @Override
                        public void run() {MainMenuScreen.this.deletePlayerButtonPressed(j);
                        }
                    },
                    InterfaceMainMenu.FIRST_PLAYER_DELETE_BUTTON + j
            );
        }
        buttons.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        //MainMenuScreen.this.backButtonClicked();
                    }
                },
                InterfaceMainMenu.BACK_TO_PREVIOUS_SCREEN_BUTTON
        );

    }

    protected void previousMap() {
        maps.previousMap();
        /*
        Strat7.previousMapName();
        if (Strat7.economy.currentMapAvailable()) {
            maps.setDontCheckBorders(false);
            maps.get(MapSelectInterface.BUY_BUTTON).setVisible(false);
            maps.get(MapSelectInterface.BUY_BUTTON).setBlocked(true);
        } else {
            maps.setDontCheckBorders(true);
            maps.get(MapSelectInterface.BUY_BUTTON).setVisible(true);
            maps.get(MapSelectInterface.BUY_BUTTON).setBlocked(false);
        }
        */
    }
    protected void nextMap() {
        maps.nextMap();
        /*
        Strat7.nextMapName();
        if (Strat7.economy.currentMapAvailable()) {
            maps.setDontCheckBorders(false);
            maps.get(MapSelectInterface.BUY_BUTTON).setVisible(false);
            maps.get(MapSelectInterface.BUY_BUTTON).setBlocked(true);
        } else {
            maps.setDontCheckBorders(true);
            maps.get(MapSelectInterface.BUY_BUTTON).setVisible(true);
            maps.get(MapSelectInterface.BUY_BUTTON).setBlocked(false);
        }
        */
    }

    private void buyMap(int mapNum) {
        if (Strat7.economy.getMoney() >= 50) {
            Strat7.economy.changeMoney(-50);
            Strat7.economy.availableCurrentMap();
            maps.setDontCheckBorders(false);
            maps.get(MapSelectInterface.FIRST_BUY_BUTTON + mapNum).hide();
            maps.get(MapSelectInterface.FIRST_BUY_BUTTON + mapNum).setBlocked(true);
        } else {
            Strat7.economy.notEnoughMoney();
        }
    }

    protected void gameClickSpeedPressed() {
        Strat7.changeClickSpeed(Strat7.preferences.getInteger("ClickSpeed"));
        settingsInterface.setClickSpeedButtonText();
    }

    protected void fogOfWarButtonPressed() {
        Strat7.changeFogOfWar();
        settingsInterface.setFogOfWarButtonText();
    }

    protected void gameDifficultyPressed() {
        Strat7.changeDifficulty(Strat7.preferences.getInteger("Difficulty"));
        for (int i = 0; i < PlayersList.MAX_PLAYER_AMOUNT; i++) {
            playersIdList.getInactivePlayer(i).setArtificialType(Strat7.preferences.getInteger("Difficulty"));
        }
    }

    protected void gameSpeedPressed() {
        Strat7.changeAISpeed(Strat7.preferences.getInteger("AISpeed"));
        settingsInterface.setGameSpeedButtonText();
    }

    protected void settingsPressed() {
        stack.activateInterface(settingsInterface,true,new AppearanceParameters(settingsInterface, AppearanceParameters.FAST , AppearanceParameters.FROM_TOP));
    }

    protected void mapButtonPressed() {
        // changeCurrentState(2);
        stack.activateInterface(maps,true,new AppearanceParameters(maps, AppearanceParameters.FAST, AppearanceParameters.FROM_TOP));
    }

    protected void addPlayerButtonPressed() {
        buttons.addPlayerPressed();
    }
    protected void deletePlayerButtonPressed(int playerNum) {
        buttons.deletePlayer(playerNum);
    }

    protected void playerSettingsPressed(int playerNum) {
        playerSettings.setPlayer(playerNum);
        stack.activateInterface(playerSettings,true,new AppearanceParameters(playerSettings, AppearanceParameters.FAST, AppearanceParameters.FROM_TOP));

    }

    protected void changeColorPressed(int colorNum) {
        playersIdList.change2Colors(playerSettings.getPlayer(), colorNum);
    }

    protected void makeAIPressed() {
        playersIdList.getInactivePlayer(playerSettings.getPlayer()).setArtificial(true);
        playerSettings.configPlayersAI();
    }

    protected void makePlayerPressed() {
        playersIdList.getInactivePlayer(playerSettings.getPlayer()).setArtificial(false);
        playerSettings.configPlayersAI();
    }

    protected void AIDifficultyPressed(int difficulty) {
        playerSettings.get(PlayerSettingsInterface.DUMB_DIFFICULTY_BUTTON + playersIdList.getInactivePlayer(playerSettings.getPlayer()).getArtificialType()).setVisible(false);
        playerSettings.get(difficulty + PlayerSettingsInterface.DUMB_DIFFICULTY_BUTTON).setVisible(true);
        playersIdList.getInactivePlayer(playerSettings.getPlayer()).setArtificialType(difficulty);
    }


    protected void playButtonPressed(){
        if (playersIdList.getActivePlayersAmount() > 1) {
            savePreferences();
            Strat7.context.setScreen(new GameScreen( playersIdList));
        } else {
            Strat7.economy.adHandler.atLeast2();
        }
    }

    public void backButtonClicked() {
        if(stack.getActiveInterfacesAmount() == 1) {
            savePreferences();
            StartMenuScreen screen = new StartMenuScreen();
            Strat7.context.setScreen(screen, AppScreen.BACK_BUTTON);
            return;
        }
        stack.deactivateLastActiveInterface();

    }


    protected void savePreferences() {
        String settingsJson = Strat7.json.toJson(playersIdList, PlayersList.class);
        Strat7.preferences.putString("playersIdList", settingsJson);
        Strat7.preferences.flush();
    }

    @Override
    public void render(float delta) {
        //delta = 1 / 60f;
        Strat7.getCurrentBatch().setColor(Strat7.backColor);

        background.draw(false);
        drawInterfaces(delta);

        Strat7.getCurrentBatch().setColor(Color.WHITE);
    }

    public void drawInterfaces(float delta) {
        for(int i = 0; i < stack.size(); i ++) {
            stack.draw(i,delta);
        }
        stack.clean();
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        savePreferences();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        savePreferences();
    }

    @Override
    public void show() { //Called when this screen becomes the current screen for a Strat7.
    }
}

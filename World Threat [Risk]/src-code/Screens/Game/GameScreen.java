
package com.strat7.game.Screens.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.strat7.game.AI.AIPlayer;
import com.strat7.game.GameInfo.GameSaver;
import com.strat7.game.GameInfo.GameState;
import com.strat7.game.GameInfo.PlayersList;
import com.strat7.game.GameInfo.Province;
import com.strat7.game.Interfaces.InfoWindowInterface;
import com.strat7.game.Interfaces.Basics.Interface;
import com.strat7.game.Interfaces.InterfaceStacks.AppearanceParameters;
import com.strat7.game.Screens.MainMenu.SettingsInterface;
import com.strat7.game.Screens.ListenableScreen;
import com.strat7.game.Screens.Manager.AppScreen;
import com.strat7.game.Screens.StartMenu.StartMenuScreen;
import com.strat7.game.Strat7;

/**
 * Created by Юра on 13.06.2017.
 */

public class GameScreen extends ListenableScreen {
    protected GameState state; // here we will make players moves and take turns
    public GameState getState() { return state;}


    protected String gameName = null;
    protected String mapName = null;

    //protected Arrows arrows;
    //protected TextSubstrate textSubstrate;

    protected static AIPlayer computerPlayer;

    protected int it = 0;

    protected InterfaceGame buttons;
    protected PauseMenuInterface pauseMenu;
    protected SettingsInterface settings;
    protected InfoWindowInterface ending;
    protected GameProcessInterface gameProcessInterface;
    protected PassTurnInterface infoWindow;
    protected com.strat7.game.Screens.Game.Stats.StatsInterface statsInterface;



    public GameScreen( PlayersList playersList) {
        super();

        Strat7.font.setColor(Color.BLACK);
        if (playersList != null) {
            createGameState(playersList);
        }

        //Gdx.gl.glClearColor(Strat7.backColor.r, Strat7.backColor.g, Strat7.backColor.b, 1);
        Gdx.graphics.setContinuousRendering(true);
    }

    public void createGameState(PlayersList players) {
        if (mapName == null) {
            mapName = Strat7.preferences.getString("Map");
            state = new GameState(players, mapName);
            initGameProcessInterface();
            initButtons();
            state.setButtons(buttons);
            state.changeCurrentState(0);
            initStatsInterface();
            loadSheluhu();
        } else {
            state = new GameState(null, mapName);
            state.setPlayersIdList(players);
            initGameProcessInterface();
            initButtons();
            initStatsInterface();
            state.setButtons(buttons);
        }
    }

    private void initStatsInterface() {
        statsInterface = new com.strat7.game.Screens.Game.Stats.StatsInterface(this,state.getStatTracker());
        statsInterface.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        GameScreen.this.backButtonClicked();
                    }
                },
                com.strat7.game.Screens.Game.Stats.StatsInterface.BACK_BUTTON
        );
    }

    public void loadSheluhu() {
        // here could be some settings
        computerPlayer = new AIPlayer(state);

        //arrows = new Arrows(Strat7,state);
        initInfoWindow();
        initPauseMenu();
        initSettings();
        initEnding();
        stack.activateInterface(gameProcessInterface, true,new AppearanceParameters(gameProcessInterface));
        stack.activateInterface(buttons,false,new AppearanceParameters(buttons));
        prepareToPassTurn();
    }

    protected void initGameProcessInterface() {
        gameProcessInterface = new GameProcessInterface(this, state);

        final int shift = gameProcessInterface.FIRST_CIRCLE;
        for(int i = 0; i < gameProcessInterface.CIRCLES_AMOUNT; i ++) {
            final int j = i + shift;
            gameProcessInterface.setRuleForButton(
                    new Runnable() {
                        @Override
                        public void run() {
                            GameScreen.this.circlePressed(j - shift);
                        }
                    },
                    j
            );
        }
        final int shift1 = gameProcessInterface.FIRST_PROVINCE;
        for(int i = 0; i < gameProcessInterface.PROVINCE_AMOUNT; i ++) {
            final int j = i + shift1;

            gameProcessInterface.setRuleForButton(
                    new Runnable() {
                        @Override
                        public void run() {
                            GameScreen.this.provincePressed(j - shift1);
                        }
                    },
                    j
            );
        }
        gameProcessInterface.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        GameScreen.this.provincePressed(-1);
                    }
                },
                gameProcessInterface.OCEAN
        );
    }

    protected void provincePressed(int provinceNum) {
        if (isBlocked())
            return;

        if(provinceNum == -1) {
            state.playerActions(null);
            return;
        }

        if(!state.getProvincesArray().get(provinceNum).isVisible()) {
            circlePressed(state.getProvincesArray().getContinentNumber(provinceNum));
            return;
        }
        state.playerActions(state.getProvincesArray().get(provinceNum));
        // todo state.setAttackAll(true);
    }

    protected void initInfoWindow() {
        infoWindow = new PassTurnInterface(this,Interface.SMALL_INTERFACE_CENTER,"",null, "next step",state);
        infoWindow.setTextScale(0.7);

        infoWindow.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        GameScreen.this.passTurn();
                    }
                },
                InfoWindowInterface.INFO_BUTTON_2
        );
        infoWindow.setDontCheckBorders(true);
    }


    protected void initSettings() {
        settings = new SettingsInterface(this,state.getPlayersIdList());
        settings.get(SettingsInterface.GAME_DIFFICULTY_BUTTON).setBlocked(true);
        settings.get(SettingsInterface.FOG_OF_WAR_BUTTON).setBlocked(true);

        settings.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        GameScreen.this.gameSpeedPressed();
                    }
                },
                SettingsInterface.GAME_SPEED_BUTTON
        );
        settings.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        GameScreen.this.gameDifficultyPressed();
                    }
                },
                SettingsInterface.GAME_DIFFICULTY_BUTTON
        );
        settings.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        GameScreen.this.gameClickSpeedPressed();
                    }
                },
                SettingsInterface.GAME_CLICK_SPEED_BUTTON
        );
        settings.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        GameScreen.this.gameClickSpeedPressed();
                    }
                },
                SettingsInterface.GAME_CLICK_SPEED_BUTTON
        );
        settings.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        GameScreen.this.backButtonClicked();
                    }
                },
                SettingsInterface.BACK_TO_PREVIOUS_SCREEN_BUTTON
        );
    }

    protected void gameSpeedPressed() {
        Strat7.changeAISpeed(Strat7.preferences.getInteger("AISpeed"));
        computerPlayer.setAISpeed(Strat7.preferences.getInteger("AISpeed"));
        settings.setGameSpeedButtonText();
    }

    protected void gameDifficultyPressed() {
        Strat7.changeDifficulty(Strat7.preferences.getInteger("Difficulty"));
        for (int i = 0; i < PlayersList.MAX_PLAYER_AMOUNT; i++) {
            state.getPlayersIdList().getInactivePlayer(i).setArtificialType(Strat7.preferences.getInteger("Difficulty"));
        }
    }

    protected void fogOfWarButtonPressed() {
        Strat7.changeFogOfWar();
        settings.setFogOfWarButtonText();
    }

    protected void gameClickSpeedPressed() {
        Strat7.changeClickSpeed(Strat7.preferences.getInteger("ClickSpeed"));
        state.setInitialAttackAllDefaultTimePerTroop(Strat7.preferences.getInteger("ClickSpeed"));
        settings.setClickSpeedButtonText();
    }


    protected void initPauseMenu() {
        pauseMenu = new PauseMenuInterface(this);

        pauseMenu.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        GameScreen.this.resumePressed();
                    }
                },
                PauseMenuInterface.RESUME_BUTTON
        );
        pauseMenu.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        GameScreen.this.saveCurrentGame();
                    }
                },
                PauseMenuInterface.SAVE_GAME_BUTTON
        );
        pauseMenu.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        GameScreen.this.openSettings();
                    }
                },
                PauseMenuInterface.SETTINGS_BUTTON
        );
        pauseMenu.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        GameScreen.this.openStats();
                    }
                },
                PauseMenuInterface.STATS
        );
        pauseMenu.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        GameScreen.this.exitToMainMenuPressed();
                    }
                },
                PauseMenuInterface.EXIT_TO_MAIN_MENU_BUTTON
        );
        pauseMenu.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        GameScreen.this.exitPressed();
                    }
                },
                PauseMenuInterface.EXIT_BUTTON
        );
        pauseMenu.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        GameScreen.this.backButtonClicked();
                    }
                },
                PauseMenuInterface.BACK_TO_PREVIOUS_SCREEN_BUTTON
        );
    }

    protected void resumePressed() {
        while (stack.getActiveInterfacesAmount() > 1 ) {
            backButtonClicked();
        }
    }

    protected void saveCurrentGame() {
        if (it == -300) {
            GameSaver.deleteFirstSave();
        } else {
            GameSaver.saveGame(this);
        }
    }

    protected void openSettings() {
        stack.activateInterface(settings,true,new AppearanceParameters(settings,AppearanceParameters.FAST, AppearanceParameters.FROM_TOP));
    }
    protected void openStats() {
        stack.activateInterface(statsInterface,true,new AppearanceParameters(statsInterface,AppearanceParameters.FAST, AppearanceParameters.FROM_TOP));
    }

    protected void exitToMainMenuPressed() {
        saveCurrentGame();
        StartMenuScreen screen = new StartMenuScreen();
        Strat7.context.setScreen(screen, AppScreen.BACK_BUTTON);
    }

    protected void exitPressed() {
        Gdx.app.exit();
    }


    protected void initButtons() {
        buttons = new InterfaceGame(this, state);
        buttons.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        GameScreen.this.mainButtonPressed();
                    }
                },
                InterfaceGame.MAIN_BUTTON
        );
        buttons.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        GameScreen.this.pauseButtonPressed();
                    }
                },
                InterfaceGame.PAUSE_BUTTON
        );
        buttons.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        GameScreen.this.showContinents();
                    }
                },
                InterfaceGame.SHOW_CONTINENTS_BUTTON
        );
        buttons.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        GameScreen.this.showCircles();
                    }
                },
                InterfaceGame.SHOW_CIRCLES_BUTTON
        );
    }

    protected void showCircles() {
        gameProcessInterface.showCircles();
    }

    protected void circlePressed(int circleNum) {
        int shift = gameProcessInterface.FIRST_PROVINCE;
        for(int i = circleNum == 0 ? 0 : state.getProvincesArray().getContinentArray()[circleNum - 1]; i < state.getProvincesArray().getContinentArray()[circleNum]; i++)
            gameProcessInterface.get(i + shift).setVisible(
                    !gameProcessInterface.get(i + shift).isVisible()
            );
    }

    protected void showContinents() {
        final int shift = gameProcessInterface.FIRST_PROVINCE;
        boolean drawBack = false;
        for(int i = 0; i < gameProcessInterface.PROVINCE_AMOUNT; i ++) {
            if(!gameProcessInterface.get(i + shift).isVisible()) {
                drawBack = true;
            }
        }
        for(int i = 0; i < gameProcessInterface.PROVINCE_AMOUNT; i ++) {
            gameProcessInterface.get(i + shift).setVisible(drawBack);
        }
    }

    protected void mainButtonPressed() {
        Province province = state.ifButtonUnpressed();
        prepareToPassTurn();
        if (province != null &&
                province.getOwnerID() == null) {
            it = -300;
            gameOver();
        }
    }

    protected void pauseButtonPressed() {
        stack.activateInterface(pauseMenu,true,new AppearanceParameters(pauseMenu, AppearanceParameters.FAST, AppearanceParameters.FROM_TOP));
//        Strat7.economy.showBanner(true);
    }

    protected void initEnding() {
        ending = new InfoWindowInterface(this, Interface.SMALL_INTERFACE_CENTER,"",null,null);
        ending.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        GameScreen.this.exitToMainMenuPressed();
                        ending.setDontCheckBorders(false);
                    }
                },
                InfoWindowInterface.INFO_BUTTON_2
        );
        ending.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        GameScreen.this.openStats();
                        ending.setDontCheckBorders(false);
                    }
                },
                InfoWindowInterface.INFO_BUTTON_1
        );
    }

    public void backButtonClicked() {
        if(stack.getActiveInterfacesAmount() == 1) {
            pauseButtonPressed();
            return;
        }
        if(stack.getActiveInterfacesAmount() == 2) {
//            Strat7.economy.showBanner(false);
        }
        stack.deactivateLastActiveInterface();
    }

    @Override
    public void deactivateInterface(int num, boolean type) {
        super.deactivateInterface(num,type);

    }


    protected void gameOver() {
        stack.activateInterface(ending,true, new AppearanceParameters(ending));
        ending.setDontCheckBorders(true);
        String string;
        if (state.getPlayersIdList().getCurrentPlayer().isArtificial()) {
            string = Strat7.myBundle.get("you_lose");
        } else {
            string = Strat7.myBundle.get("you_win") + " " + state.getPlayersIdList().getAIAmount() + " "+ Strat7.myBundle.get("AI") + "    " + Strat7.myBundle.get("coins") + ": +" + Integer.toString(state.getPlayersIdList().getAIAmount()+1);
            Strat7.economy.changeMoney(state.getPlayersIdList().getAIAmount()+1);
        }
        ending.setInfo(string,"Stats", "Ok" );
    }

    public boolean isPause() {
        return stack.getActiveInterfacesAmount() > 1;
    }

    @Override
    public void render(float delta) {
        Strat7.timePassed = Gdx.graphics.getDeltaTime();

        // TODO Print based on condition (our int var)

        if (!stack.getParameter(gameProcessInterface).isHiding())
            background.setColor(new Color(0.9f, 1, 1, 1));
        else
            background.setColor(new Color(0, 0, 0, 1));
        background.draw(false);

        waitForEvents();

        drawInterfaces(delta);

        ifAIMakeTurn();

        if(!isBlocked()) {
            state.setAttackAll(gameProcessInterface.pressedButton - gameProcessInterface.FIRST_PROVINCE>= 0);
            state.attackAll(gameProcessInterface.pressedButton - gameProcessInterface.FIRST_PROVINCE);
        }

    }

    protected void drawInterfaces(float delta) {
        for(int i = 0; i < stack.size(); i ++)
            stack.draw(i,delta);
        stack.clean();
    }

    protected boolean isBlocked() {
        return state.getPlayersIdList().getCurrentPlayer().isArtificial();
    }
    protected boolean isAiBlocked() {
        return isPause();
    }


    protected void waitForEvents() {
    }

    protected void ifAIMakeTurn() {
        if(state.getPlayersIdList().getCurrentPlayer().isArtificial()
                && !isAiBlocked()) {
            state.getButtons().get(0).setText(Strat7.myBundle.get("skip_ai_turn"));
            state.getButtons().get(0).setVisible(true);
            if (computerPlayer.getAISpeed() > 0 && !state.isSkipAITurn()) {
                if ((it = computerPlayer.makeTurn()) == -300) {
                    gameOver();
                }
            } else {
                computerPlayer.setAISpeed(0);
                do {
                    if ((it = computerPlayer.makeTurn()) == 1) {
                        prepareToPassTurn();
                        break;
                    }
                    if (it == -300) {
                        gameOver();
                        return;
                    }
                } while (true);
                computerPlayer.setAISpeed(Strat7.preferences.getInteger("AISpeed"));
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
    }

    @Override
    public void pause() {
        saveCurrentGame();
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        Strat7.economy.showBanner(true);

        //state.dispose();

        //Strat7.graphics.disposePath("Maps");
        //buttons.dispose();
    }

    public static void setComputerPlayer(AIPlayer computerPlayer) {
        GameScreen.computerPlayer = computerPlayer;
    }
    public static AIPlayer getComputerPlayer () {
        return computerPlayer;
    }
    public String getMapName() {
        return mapName;
    }
    public void setMapName(String mapName) {
        this.mapName = mapName;
    }
    public String getGameName() {
        return gameName;
    }
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }


    protected void prepareToPassTurn() {
        if (state.isHidden()) {
            infoWindow.setInfo(Strat7.myBundle.get("pass"), null,Strat7.myBundle.get("ready"));
            for (int i = 0; i < stack.size(); i++) {
                stack.hide(i);
            }
            stack.activateInterface(infoWindow,true,new AppearanceParameters(infoWindow));
        }
    }

    protected void passTurn() {
        state.setHidden(false);
        stack.deactivateInterface(stack.getInterfaceNum(infoWindow));
        for (int i = 0; i < stack.size(); i++) {
            stack.show(i);
        }
    }
}
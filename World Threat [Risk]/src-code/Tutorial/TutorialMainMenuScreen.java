package com.strat7.game.Tutorial;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.strat7.game.GameInfo.PlayersList;
import com.strat7.game.Interfaces.Basics.BoundBasics.BoundWithFrame;
import com.strat7.game.Interfaces.Basics.BoundBasics.Carcass;
import com.strat7.game.Interfaces.Basics.Interface;
import com.strat7.game.Interfaces.InfoWindowInterface;
import com.strat7.game.Screens.MainMenu.InterfaceMainMenu;
import com.strat7.game.Interfaces.InterfaceStacks.AppearanceParameters;
import com.strat7.game.Screens.MainMenu.PlayerSettingsInterface;
import com.strat7.game.Screens.MainMenu.MainMenuScreen;
import com.strat7.game.Screens.Manager.AppScreen;
import com.strat7.game.Screens.Manager.BasicScreenAppear;
import com.strat7.game.Screens.StartMenu.StartMenuScreen;
import com.strat7.game.Strat7;

import java.util.ArrayList;

/**
 * Created by Евгений on 01.08.2017.
 */

public class TutorialMainMenuScreen extends MainMenuScreen {
    private static final int START_STEP = 0;
    private static final int ADD_PLAYER_STEP = 1;
    private static final int PLAYER_SETTINGS_BUTTON_STEP = 2;
    private static final int COLOR_STEP = 3;
    private static final int MAKE_AI_STEP = 4;
    private static final int DIFFICULTY_STEP = 5;
    private static final int MAKE_PLAYER_STEP = 6;
    private static final int PLAYER_SETTINGS_STEP = 7;
    private static final int DELETE_STEP = 8;
    private static final int GAME_SETTINGS_BUTTON_STEP = 9;
    private static final int GAME_SETTINGS_STEP = 10;
    private static final int MAP_STEP = 11;
    private static final int MAP_CHOOSE_STEP = 12;
    private static final int PREPARE_FOR_GAME_STEP = 13;


    private int tutorialState = -1;

    private String message = "HelloMfck";
    private SelectedFrame selectedFrame;

    private InfoWindowInterface infoWindow;
    private final Runnable nextTip = new Runnable() {
        @Override
        public void run() {
            nextStep();
        }
    };



    public TutorialMainMenuScreen () {
        super() ;
        selectedFrame = new SelectedFrame();
        infoWindow = new InfoWindowInterface(this,Interface.SMALL_INTERFACE_RIGHT_TOP,"",null,"");
        infoWindow.setTextScale(0.7);
        nextStep();
    }

    @Override
    protected void initInterfaceMainMenu() {
        playersIdList.clear();
        playersIdList.makePlayerActive(0);
        playersIdList.makePlayerActive(1);
        super.initInterfaceMainMenu();
    }

    @Override
    protected void initPlayerSettings() {
        super.initPlayerSettings();
    }

    @Override
    protected void initSettings() {
        super.initSettings();
    }

    private void nextStep () {
        tutorialState ++;
        int shift ;
        ArrayList<Carcass> buttonArrayList;
        buttonArrayList = new ArrayList<Carcass>();
        switch (tutorialState) {
            case START_STEP:
                showStartMessage ();
                selectedFrame.setNewFrame(infoWindow.get(InfoWindowInterface.INFO_BUTTON_2) ,infoWindow);
                break;

            case ADD_PLAYER_STEP:
                buttonArrayList.add(buttons.get(InterfaceMainMenu.ADD_PLAYER_BUTTON) );
                infoWindow.setRuleForButton(
                        new Runnable() {
                            @Override
                            public void run() {
                                addPlayerButtonPressed();
                            }
                        },
                        InfoWindowInterface.INFO_BUTTON_2
                );
                selectedFrame.setNewFrame(buttonArrayList,infoWindow);
                break;

            case PLAYER_SETTINGS_BUTTON_STEP:
                buttonArrayList.add(buttons.get(InterfaceMainMenu.FIRST_PLAYER_SETTINGS_BUTTON) );
                infoWindow.setRuleForButton(
                        new Runnable() {
                            @Override
                            public void run() {
                                playerSettingsPressed(0);
                            }
                        },
                        InfoWindowInterface.INFO_BUTTON_2
                );
                selectedFrame.setNewFrame(buttonArrayList,buttons);
                break;

            case COLOR_STEP:
                playerSettings.setDontCheckBorders(true);
                stack.deleteInterface(infoWindow);
                stack.activateInterface(infoWindow,false,new AppearanceParameters(infoWindow));
                stack.moveInterface(
                        infoWindow,
                        new BoundWithFrame(infoWindow.getMainFrame(),Interface.SMALL_INTERFACE_RIGHT_BOTTOM),
                        AppearanceParameters.FAST
                );
                infoWindow.setRuleForButton(
                        new Runnable() {
                            @Override
                            public void run() {
                                changeColorPressed(1);
                            }
                        },
                        InfoWindowInterface.INFO_BUTTON_2
                );

                shift = PlayerSettingsInterface.FIRST_COLOR_BUTTON;
                for(int i = 0; i < PlayersList.MAX_PLAYER_AMOUNT; i++) {
                    buttonArrayList.add(playerSettings.get(i + shift) );
                }
                selectedFrame.setNewFrame(buttonArrayList,playerSettings);
                break;

            case MAKE_AI_STEP:
                buttonArrayList.add(playerSettings.get(PlayerSettingsInterface.MAKE_AI_BUTTON) );
                stack.moveBack(infoWindow);
                infoWindow.setRuleForButton(
                        new Runnable() {
                            @Override
                            public void run() {
                                makeAIPressed();
                            }
                        },
                        InfoWindowInterface.INFO_BUTTON_2
                );
                selectedFrame.setNewFrame(buttonArrayList,playerSettings);
                break;

            case DIFFICULTY_STEP:
                shift = PlayerSettingsInterface.DUMB_DIFFICULTY_BUTTON;
                for(int i = 0; i < 4; i++) {
                    buttonArrayList.add(playerSettings.get(i + shift) );
                }
                infoWindow.setRuleForButton(
                        new Runnable() {
                            @Override
                            public void run() {
                                AIDifficultyPressed(3);
                            }
                        },
                        InfoWindowInterface.INFO_BUTTON_2
                );
                selectedFrame.setNewFrame(buttonArrayList,playerSettings);
                break;

            case MAKE_PLAYER_STEP:
                buttonArrayList.add(playerSettings.get(PlayerSettingsInterface.MAKE_PLAYER_BUTTON) );
                infoWindow.setRuleForButton(
                        new Runnable() {
                            @Override
                            public void run() {
                                makePlayerPressed();
                            }
                        },
                        InfoWindowInterface.INFO_BUTTON_2
                );
                selectedFrame.setNewFrame(buttonArrayList,playerSettings);
                break;

            case PLAYER_SETTINGS_STEP:
                selectedFrame.clear();
                playerSettings.setDontCheckBorders(false);
                infoWindow.setRuleForButton(
                        new Runnable() {
                            @Override
                            public void run() {
                                backButtonClicked();
                            }
                        },
                        InfoWindowInterface.INFO_BUTTON_2
                );
                break;

            case DELETE_STEP:
                buttonArrayList.add(buttons.get(InterfaceMainMenu.THIRD_PLAYER_DELETE_BUTTON) );
                infoWindow.setRuleForButton(
                        new Runnable() {
                            @Override
                            public void run() {
                                deletePlayerButtonPressed(2);
                            }
                        },
                        InfoWindowInterface.INFO_BUTTON_2
                );
                selectedFrame.setNewFrame(buttonArrayList,buttons);
                break;

            case GAME_SETTINGS_BUTTON_STEP:
                buttonArrayList.add(buttons.get(InterfaceMainMenu.SETTINGS_BUTTON) );
                infoWindow.setRuleForButton(
                        new Runnable() {
                            @Override
                            public void run() {
                                settingsPressed();
                            }
                        },
                        InfoWindowInterface.INFO_BUTTON_2
                );
                selectedFrame.setNewFrame(buttonArrayList,buttons);
                break;

            case GAME_SETTINGS_STEP:
                stack.deleteInterface(infoWindow);
                stack.activateInterface(infoWindow,false,new AppearanceParameters(infoWindow));
                stack.moveInterface(
                        infoWindow,
                        new BoundWithFrame(infoWindow.getMainFrame(),Interface.SMALL_INTERFACE_RIGHT_BOTTOM),
                        AppearanceParameters.FAST
                );
                infoWindow.setRuleForButton(
                        new Runnable() {
                            @Override
                            public void run() {
                                backButtonClicked();
                            }
                        },
                        InfoWindowInterface.INFO_BUTTON_2
                );
                selectedFrame.clear();
                break;

            case MAP_STEP:
                stack.moveBack(infoWindow);
                buttonArrayList.add(buttons.get(InterfaceMainMenu.MAP_BUTTON) );
                infoWindow.setRuleForButton(
                        new Runnable() {
                            @Override
                            public void run() {
                                mapButtonPressed();
                            }
                        },
                        InfoWindowInterface.INFO_BUTTON_2
                );
                selectedFrame.setNewFrame(buttonArrayList,buttons);
                break;

            case MAP_CHOOSE_STEP:
                stack.deleteInterface(infoWindow);
                stack.activateInterface(infoWindow,false,new AppearanceParameters(infoWindow));
                infoWindow.setRuleForButton(
                        new Runnable() {
                            @Override
                            public void run() {
                                backButtonClicked();
                            }
                        },
                        InfoWindowInterface.INFO_BUTTON_2
                );
                selectedFrame.clear();
                break;

            case PREPARE_FOR_GAME_STEP:
                buttonArrayList.add(buttons.get(InterfaceMainMenu.GAME_BUTTON) );
                infoWindow.setRuleForButton(
                        new Runnable() {
                            @Override
                            public void run() {
                                playButtonPressed();
                            }
                        },
                        InfoWindowInterface.INFO_BUTTON_2
                );
                selectedFrame.setNewFrame(buttonArrayList,buttons);
                break;
        }
        infoWindow.setInfo(Strat7.tutotialBundle.get("tutorial_menu_" + tutorialState),null, Strat7.tutotialBundle.get("next_step"));
    }

    // todo make all show functions Active
    private void showStartMessage() {
        // here we wait while player press everywhere
        //infoWindow.setInfo("hello",null, "next step");
        infoWindow.setRuleForButton(
                nextTip,
                InfoWindowInterface.INFO_BUTTON_2
        );
        stack.activateInterface(infoWindow,false,new AppearanceParameters(infoWindow, AppearanceParameters.FAST, AppearanceParameters.FROM_RIGHT));
    }

    private void showExitQuestion() {
        InfoWindowInterface exit = new InfoWindowInterface(this,Interface.SMALL_INTERFACE_CENTER,"",null,null);
        exit.setInfo(Strat7.tutotialBundle.get("exit"),Strat7.tutotialBundle.get("yes"), Strat7.tutotialBundle.get("no"));
        exit.setDontCheckBorders(true);
        stack.activateInterface(exit,true,new AppearanceParameters(exit));
        exit.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        exitButtonPressed();
                    }
                },
                InfoWindowInterface.INFO_BUTTON_1
        );
        exit.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        TutorialMainMenuScreen.super.backButtonClicked();
                    }
                },
                InfoWindowInterface.INFO_BUTTON_2
        );
    }

    private void exitButtonPressed() {
        savePreferences();
        dispose();
        StartMenuScreen screen = new StartMenuScreen();
        Strat7.context.setScreen(screen, AppScreen.BACK_BUTTON);
    }

    @Override
    protected void addPlayerButtonPressed() {
        switch (tutorialState) {
            case ADD_PLAYER_STEP:
                super.addPlayerButtonPressed();
                nextStep();
                break;
            case PREPARE_FOR_GAME_STEP:
                super.addPlayerButtonPressed();
                break;
        }
    }

    @Override
    protected void playerSettingsPressed(int playerNum) {
        switch (tutorialState) {
            case PLAYER_SETTINGS_BUTTON_STEP:
                if(playerNum != 0)
                    break;
                super.playerSettingsPressed(playerNum);
                nextStep();
                break;
            case PREPARE_FOR_GAME_STEP:
                super.playerSettingsPressed(playerNum);
                break;
        }
    }

    @Override
    protected void changeColorPressed(int colorNum) {
        switch (tutorialState) {
            case COLOR_STEP:
                super.changeColorPressed(colorNum);
                nextStep();
                break;
            case PREPARE_FOR_GAME_STEP:
            case PLAYER_SETTINGS_STEP:
                super.changeColorPressed(colorNum);
                break;
        }
    }

    @Override
    protected void makeAIPressed() {

        switch (tutorialState) {
            case MAKE_AI_STEP:
                super.makeAIPressed();
                nextStep() ;
                break;
            case PREPARE_FOR_GAME_STEP:
            case PLAYER_SETTINGS_STEP :
                super.makeAIPressed();
                break;
        }
    }

    @Override
    protected void makePlayerPressed() {
        switch (tutorialState) {
            case MAKE_PLAYER_STEP:
                super.makePlayerPressed();
                nextStep();
                break;
            case PREPARE_FOR_GAME_STEP:
            case PLAYER_SETTINGS_STEP:
                super.makePlayerPressed();
                break;

        }
    }

    @Override
    protected void AIDifficultyPressed(int difficulty) {
        switch (tutorialState) {
            case DIFFICULTY_STEP:
                super.AIDifficultyPressed(difficulty);
                nextStep();
                break;
            case PREPARE_FOR_GAME_STEP:
            case PLAYER_SETTINGS_STEP:
                super.AIDifficultyPressed(difficulty);
                break;
        }
    }

    @Override
    protected void deletePlayerButtonPressed(int playerNum) {
        switch (tutorialState) {
            case DELETE_STEP:
                super.deletePlayerButtonPressed(playerNum);
                nextStep();
                break;
            case PREPARE_FOR_GAME_STEP:
                super.deletePlayerButtonPressed(playerNum);
                break;
        }
    }

    @Override
    protected void settingsPressed() {
        switch (tutorialState) {
            case GAME_SETTINGS_BUTTON_STEP:
                super.settingsPressed();
                nextStep();
                break;
            case PREPARE_FOR_GAME_STEP:
                super.settingsPressed();
                break;
        }
    }

    @Override
    protected void mapButtonPressed() {
        switch (tutorialState){
            case MAP_STEP:
                super.mapButtonPressed();
                nextStep();
                break;
            case PREPARE_FOR_GAME_STEP:
                super.mapButtonPressed();
                break;
        }
    }

    @Override
    protected void playButtonPressed(){
        if(tutorialState == PREPARE_FOR_GAME_STEP){
            if (playersIdList.getActivePlayersAmount() > 1) {
                savePreferences();
                this.dispose();
                Strat7.context.setScreen(new TutorialGameScreen(playersIdList));
            } else {
                Strat7.economy.adHandler.atLeast2();
            }
        }
    }

    @Override
    public void backButtonClicked() {
        switch (tutorialState) {
            case PLAYER_SETTINGS_STEP:
            case GAME_SETTINGS_STEP:
            case MAP_CHOOSE_STEP:
                super.backButtonClicked();
                nextStep();
                break;
            default:
                if(stack.getActiveInterfacesAmount() != 1 && tutorialState != PREPARE_FOR_GAME_STEP || stack.getActiveInterfacesAmount() == 1) {
                    showExitQuestion();
                    return;
                }
                super.backButtonClicked();
                break;
        }
    }

    @Override
    public void render (float delta) {
        super.render(delta);
    }

    @Override
    public void drawInterfaces(float delta) {
        for(int i = 0; i < stack.size(); i ++) {
            stack.draw(i,delta);
            selectedFrame.draw(delta,stack.getInterface(i));
        }
        stack.clean();
    }


}


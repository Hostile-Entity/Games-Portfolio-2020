package com.strat7.game.Tutorial;

import com.strat7.game.AI.AIPlayer;
import com.strat7.game.GameInfo.GameState;
import com.strat7.game.GameInfo.Player;
import com.strat7.game.GameInfo.PlayersList;
import com.strat7.game.GameInfo.Province;
import com.strat7.game.Interfaces.Basics.BoundBasics.BoundWithFrame;
import com.strat7.game.Interfaces.Basics.BoundBasics.Frame;
import com.strat7.game.Interfaces.Basics.Interface;
import com.strat7.game.Interfaces.InfoWindowInterface;
import com.strat7.game.Screens.Game.InterfaceGame;
import com.strat7.game.Interfaces.InterfaceStacks.AppearanceParameters;
import com.strat7.game.Screens.Game.GameScreen;
import com.strat7.game.Strat7;

import java.util.ArrayList;

/**
 * Created by Евгений on 04.08.2017.
 */

public class TutorialGameScreen extends GameScreen {
    static final int START_STEP = 0;
    static final int STATE_PANEL_STEP            = START_STEP + 1;
    static final int SHOW_CONTINENTS_BUTTON_STEP = STATE_PANEL_STEP + 1;
    static final int SHOW_CONTINENTS_STEP        = SHOW_CONTINENTS_BUTTON_STEP + 1;
    static final int SHOW_CONTINENTS_BONUSES_STEP= SHOW_CONTINENTS_STEP + 1;
    static final int CONTINENT_CIRCLE_STEP       = SHOW_CONTINENTS_BONUSES_STEP + 1;
    static final int BRING_CIRCLE_BACK           = CONTINENT_CIRCLE_STEP + 1;

    static final int DEPLOY_STEP                 = BRING_CIRCLE_BACK + 1; // 7
    static final int DEPLOY_YOUR_TROOPS_STEP     = DEPLOY_STEP + 1; // 7
    static final int DEPLOY_BUTTON_STEP          = DEPLOY_YOUR_TROOPS_STEP + 1; // 8

    static final int CHOOSE_PROVINCE_STEP        = DEPLOY_BUTTON_STEP + 1;
    static final int ATTACK_STEP                 = CHOOSE_PROVINCE_STEP + 1; // 10

    static final int DISTRIBUTE_STEP             = ATTACK_STEP + 1;
    static final int DISTRIBUTE_BUTTON_STEP      = DISTRIBUTE_STEP + 1; // 12

    static final int END_ATTACK_BUTTON_STEP      = DISTRIBUTE_BUTTON_STEP + 1;
    static final int MOVE_STEP                   = END_ATTACK_BUTTON_STEP + 1;
    static final int END_TURN_BUTTON_STEP        = MOVE_STEP + 1;  // 15

    static final int FAST_DEPLOY_STEP            = END_TURN_BUTTON_STEP + 1;
    static final int FAST_ATTACK_STEP            = FAST_DEPLOY_STEP + 1;
    static final int GAME_STEP                   = FAST_ATTACK_STEP + 1; // 18

    private int tutorialState = -1;
    private int circleChosen = -1;

    private SelectedFrame selectedFrame;
    private SelectionRectangle selectionRectangle;

    private boolean changedPosition = false;

    private TutorialProvince chosenEnemyProvince;
    private boolean removeSelection;

    private Province chosenDeployProvince = null;

    private boolean fastDeploy = false;
    private boolean fastAttack = false;


    private InfoWindowInterface infoWindow;
    private final Runnable nextTip = new Runnable() {
        @Override
        public void run() {
            nextStep();
        }
    };

    TutorialGameScreen ( PlayersList playersList){
        super(playersList);

        selectedFrame = new SelectedFrame();
        selectionRectangle = new SelectionRectangle();
        infoWindow = new InfoWindowInterface(this,Interface.SMALL_INTERFACE_RIGHT_TOP,"",null, "next step");
        infoWindow.setTextScale(0.7);
        nextStep();
    }



    public void createGameState(PlayersList players) {
        if (mapName == null) {
            mapName = Strat7.preferences.getString("Map");
        }
        state = new TutorialGameState(players,this,mapName);
        initGameProcessInterface();
        initButtons();
        state.setButtons(buttons);
        state.changeCurrentState(0);

        state.changeCurrentState(0);
        // here could be some settings
        if (players != null) {
            computerPlayer = new AIPlayer(state);
        }
        initPauseMenu();
        initSettings();
        initEnding();
        stack.activateInterface(gameProcessInterface,true,new AppearanceParameters(gameProcessInterface));
        stack.activateInterface(buttons,false,new AppearanceParameters(buttons));

    }

    protected void initEnding() {
        ending = new InfoWindowInterface(this,Interface.SMALL_INTERFACE_CENTER,"",null,null);
        ending.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        TutorialGameScreen.super.exitToMainMenuPressed();
                        ending.setDontCheckBorders(false);
                    }
                },
                InfoWindowInterface.INFO_BUTTON_2
        );
    }

    void nextStep() {
        tutorialState ++;
        BoundWithFrame frame;
        ArrayList<BoundWithFrame> frames;
        int shift ;
        switch (tutorialState) {
            case START_STEP:
                showStartMessage();
                selectedFrame.clear();
                selectedFrame.setNewFrame(infoWindow.get(InfoWindowInterface.INFO_BUTTON_2) ,infoWindow);
                break;
            case STATE_PANEL_STEP:
                showPanelMessage();
                frame = new BoundWithFrame(
                        buttons,
                        Frame.deltaH,
                        Frame.deltaH,
                        Frame.screenWidth  - 2 * Frame.deltaH,
                        6*Frame.deltaH,
                        1
                );
                selectedFrame.setNewFrame(frame,infoWindow);
                break;

            case SHOW_CONTINENTS_BUTTON_STEP:
                infoWindow.setRuleForButton(
                        new Runnable() {
                            @Override
                            public void run() {
                                showContinents();
                            }
                        },
                        InfoWindowInterface.INFO_BUTTON_2
                );
                selectedFrame.setNewFrame(buttons.get(InterfaceGame.SHOW_CONTINENTS_BUTTON) ,buttons);
                break;

            case SHOW_CONTINENTS_STEP:
                infoWindow.setRuleForButton(
                        new Runnable() {
                            @Override
                            public void run() {
                                showContinents();
                            }
                        },
                        InfoWindowInterface.INFO_BUTTON_2
                );
                selectedFrame.setNewFrame(buttons.get(InterfaceGame.SHOW_CONTINENTS_BUTTON) ,buttons);
                break;

            case SHOW_CONTINENTS_BONUSES_STEP:
                infoWindow.setRuleForButton(
                        new Runnable() {
                            @Override
                            public void run() {
                                showCircles();
                            }
                        },
                        InfoWindowInterface.INFO_BUTTON_2
                );
                selectedFrame.setNewFrame(buttons.get(InterfaceGame.SHOW_CIRCLES_BUTTON) ,buttons);
                break;
            case CONTINENT_CIRCLE_STEP:
                infoWindow.setRuleForButton(
                        new Runnable() {
                            @Override
                            public void run() {
                                circlePressed(0);
                            }
                        },
                        InfoWindowInterface.INFO_BUTTON_2
                );
                selectedFrame.clear();
                frames = new ArrayList<BoundWithFrame>();
                shift = gameProcessInterface.FIRST_CIRCLE;
                for(int i = 0; i < gameProcessInterface.CIRCLES_AMOUNT; i ++) {
                    frames.add(gameProcessInterface.get(i+shift));
                }
                selectionRectangle.setButtons(frames,false);
                break;
            case BRING_CIRCLE_BACK:
                infoWindow.setRuleForButton(
                        new Runnable() {
                            @Override
                            public void run() {
                                circlePressed(circleChosen);
                            }
                        },
                        InfoWindowInterface.INFO_BUTTON_2
                );
                selectionRectangle.clear();
                selectedFrame.setNewFrame(gameProcessInterface.get(gameProcessInterface.FIRST_CIRCLE + circleChosen) ,gameProcessInterface);
                break;
            case DEPLOY_STEP:
                selectedFrame.setNewFrame(infoWindow.get(InfoWindowInterface.INFO_BUTTON_2), infoWindow);
                infoWindow.setRuleForButton(
                        nextTip,
                        InfoWindowInterface.INFO_BUTTON_2
                );
                break;
            case DEPLOY_YOUR_TROOPS_STEP:
                selectedFrame.clear();
                for(Province province : state.getProvincesArray().getProvinceArray()) {
                    if(province.getOwnerID() == state.getPlayersIdList().getCurrentPlayer()) {
                        ((TutorialProvince) province).setChosenOne(true);
                    }
                }
                infoWindow.setRuleForButton(
                        new Runnable() {
                            @Override
                            public void run() {
                                provincePressed(chosenEnemyProvince.getProvinceID());
                            }
                        },
                        InfoWindowInterface.INFO_BUTTON_2
                );
                break;

            case DEPLOY_BUTTON_STEP:
                selectedFrame.setNewFrame(buttons.get(InterfaceGame.MAIN_BUTTON) ,buttons);
                for(Province province : state.getProvincesArray().getProvinceArray()) {
                    if(province.getOwnerID() == state.getPlayersIdList().getCurrentPlayer()) {
                        ((TutorialProvince) province).setChosenOne(false);
                    }
                }
                infoWindow.setRuleForButton(
                        new Runnable() {
                            @Override
                            public void run() {
                                state.ifButtonUnpressed();
                            }
                        },
                        InfoWindowInterface.INFO_BUTTON_2
                );
                break;
            case CHOOSE_PROVINCE_STEP:
                chosenEnemyProvince.setChosenOne(true);
                if(chosenEnemyProvince.isInRightTopCorner()) {
                    stack.moveInterface(
                            infoWindow,
                            new BoundWithFrame(infoWindow.getMainFrame(),Interface.SMALL_INTERFACE_RIGHT_BOTTOM),
                            AppearanceParameters.FAST
                    );
                    changedPosition = true;
                }

                selectedFrame.clear();
                infoWindow.setRuleForButton(
                        new Runnable() {
                            @Override
                            public void run() {
                                state.playerActions(chosenEnemyProvince);
                            }
                        },
                        InfoWindowInterface.INFO_BUTTON_2
                );
                break;
            case ATTACK_STEP:
                chosenEnemyProvince.setChosenOne(false);
                infoWindow.setRuleForButton(
                        new Runnable() {
                            @Override
                            public void run() {
                                Province province = chosenEnemyProvince.getRandomAdjacentEnemyProvince();
                                while (province.getOwnerID() != state.getPlayersIdList().getCurrentPlayer())
                                    state.playerActions(province);
                            }
                        },
                        InfoWindowInterface.INFO_BUTTON_2
                );
                break;
            case DISTRIBUTE_STEP:
                if(changedPosition) {
                    changedPosition = false;
                }
                infoWindow.setRuleForButton(
                        new Runnable() {
                            @Override
                            public void run() {
                                state.playerActions(chosenEnemyProvince);
                            }
                        },
                        InfoWindowInterface.INFO_BUTTON_2
                );
                break;
            case DISTRIBUTE_BUTTON_STEP:
                infoWindow.setRuleForButton(
                        new Runnable() {
                            @Override
                            public void run() {
                                state.ifButtonUnpressed();
                            }
                        },
                        InfoWindowInterface.INFO_BUTTON_2
                );
                selectedFrame.setNewFrame(buttons.get(InterfaceGame.MAIN_BUTTON) ,buttons);
                break;
            case END_ATTACK_BUTTON_STEP:
                stack.moveBack(infoWindow);
                infoWindow.setRuleForButton(
                        new Runnable() {
                            @Override
                            public void run() {
                                state.ifButtonUnpressed();
                            }
                        },
                        InfoWindowInterface.INFO_BUTTON_2
                );
                selectedFrame.setNewFrame(buttons.get(InterfaceGame.MAIN_BUTTON) ,buttons);
                break;
            case MOVE_STEP:
                selectedFrame.clear();
                infoWindow.setRuleForButton(
                        new Runnable() {
                            @Override
                            public void run() {
                                state.playerActions(chosenEnemyProvince);
                                state.playerActions(chosenEnemyProvince.getStrongestPlayerAdjacentProvince(chosenEnemyProvince.getOwnerID()));
                            }
                        },
                        InfoWindowInterface.INFO_BUTTON_2
                );
                break;
            case END_TURN_BUTTON_STEP:
                infoWindow.setRuleForButton(
                        new Runnable() {
                            @Override
                            public void run() {
                                state.ifButtonUnpressed();
                            }
                        },
                        InfoWindowInterface.INFO_BUTTON_2
                );
                selectedFrame.setNewFrame(buttons.get(InterfaceGame.MAIN_BUTTON) ,buttons);
                break;
            case FAST_DEPLOY_STEP:
                infoWindow.setRuleForButton(
                        new Runnable() {
                            @Override
                            public void run() {
                                fastDeploy = true;
                                if(chosenDeployProvince == null)
                                    chosenDeployProvince = chosenEnemyProvince.getRandomAdjacentFriendlyProvince(state.getPlayersIdList().getCurrentPlayer());
                            }
                        },
                        InfoWindowInterface.INFO_BUTTON_2
                );
                selectedFrame.clear();
                for(Province province :chosenEnemyProvince.getPlayerAdjProvinces(state.getPlayersIdList().getCurrentPlayer()))
                    ((TutorialProvince) province).setChosenOne(true);
                if(chosenEnemyProvince.isInRightTopCorner()) {
                    stack.moveInterface(
                            infoWindow,
                            new BoundWithFrame(infoWindow.getMainFrame(),Interface.SMALL_INTERFACE_RIGHT_BOTTOM),
                            AppearanceParameters.FAST
                    );
                    changedPosition = true;
                }
                while (Strat7.preferences.getInteger("ClickSpeed") != 2) {
                    gameClickSpeedPressed();
                }
                break;
            case FAST_ATTACK_STEP:
                chosenEnemyProvince.setChosenOne(true);
                infoWindow.setRuleForButton(
                        new Runnable() {
                            @Override
                            public void run() {
                                fastAttack = true;
                            }
                        },
                        InfoWindowInterface.INFO_BUTTON_2
                );
                break;
            case GAME_STEP:
                infoWindow.setRuleForButton(
                        new Runnable() {
                            @Override
                            public void run() {
                                showEndMessage();
                            }
                        },
                        InfoWindowInterface.INFO_BUTTON_2
                );
                chosenEnemyProvince.setChosenOne(false);
                if(changedPosition) {
                    stack.moveBack(infoWindow);
                    changedPosition = true;
                }
                selectedFrame.setNewFrame(infoWindow.get(InfoWindowInterface.INFO_BUTTON_2) ,infoWindow);
                selectionRectangle.clear();
                break;
        }
        infoWindow.setInfo(Strat7.tutotialBundle.get("tutorial_game_" + tutorialState),null, Strat7.tutotialBundle.get("next_step"));
    }

    private void showEndMessage() {
        InfoWindowInterface info = new InfoWindowInterface(this,Interface.SMALL_INTERFACE_CENTER,"",null,null);
        info.setInfo(Strat7.tutotialBundle.get("continue"),Strat7.tutotialBundle.get("yes"), Strat7.tutotialBundle.get("no"));
        info.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        stack.deactivateLastActiveInterface();
                        continueGamePressed();
                    }
                },
                InfoWindowInterface.INFO_BUTTON_1
        );
        info.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        TutorialGameScreen.super.exitToMainMenuPressed();
                    }
                },
                InfoWindowInterface.INFO_BUTTON_2
        );
        info.setDontCheckBorders(true);
        stack.activateInterface(info,true,new AppearanceParameters(info));
    }

    private void continueGamePressed() {
        Player currentPlayer = state.getPlayersIdList().getCurrentPlayer();
        for(
                Player player = state.getPlayersIdList().getNextPlayer(currentPlayer);
                player != currentPlayer;
                player = state.getPlayersIdList().getNextPlayer(player)
                ) {
            player.setArtificial(true);
            player.setArtificialType(1);
        }
        infoWindow.setInfo(Strat7.tutotialBundle.get("die"),null, Strat7.tutotialBundle.get("next_step"));
        infoWindow.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        stack.deactivateInterface(infoWindow);
                    }
                },
                InfoWindowInterface.INFO_BUTTON_2
        );
        selectedFrame.setNewFrame(infoWindow.get(InfoWindowInterface.INFO_BUTTON_2),infoWindow);
    }

    private void showExitQuestion() {
        InfoWindowInterface exit = new InfoWindowInterface(this,Interface.SMALL_INTERFACE_CENTER,"",null,null);
        exit.setInfo(Strat7.tutotialBundle.get("exit"),Strat7.tutotialBundle.get("yes"), Strat7.tutotialBundle.get("no"));
        exit.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        TutorialGameScreen.super.exitToMainMenuPressed();
                    }
                },
                InfoWindowInterface.INFO_BUTTON_1
        );
        exit.setRuleForButton(
                new Runnable() {
                    @Override
                    public void run() {
                        backButtonClicked();
                    }
                },
                InfoWindowInterface.INFO_BUTTON_2
        );
        exit.setDontCheckBorders(true);
        stack.activateInterface(exit,true,new AppearanceParameters(exit));
    }

    private void showStartMessage() {
        stack.activateInterface(infoWindow,false,new AppearanceParameters(infoWindow, AppearanceParameters.FAST, AppearanceParameters.FROM_RIGHT));
        infoWindow.setRuleForButton(nextTip,InfoWindowInterface.INFO_BUTTON_2);
    }

    private void showPanelMessage(){
        infoWindow.setRuleForButton(nextTip,InfoWindowInterface.INFO_BUTTON_2);
    }

    protected void exitToMainMenuPressed() {
        showExitQuestion();
    }

    protected void showContinents() {
        switch (tutorialState) {
            case SHOW_CONTINENTS_BUTTON_STEP:
            case SHOW_CONTINENTS_STEP:
                super.showContinents();
                nextStep();
                break;
            default:
                if(tutorialState >= DEPLOY_YOUR_TROOPS_STEP)
                    super.showContinents();
                break;
        }
    }

    protected void showCircles() {
        switch (tutorialState) {
            case SHOW_CONTINENTS_BONUSES_STEP:
                super.showCircles();
                nextStep();
                break;
            default:
                if(tutorialState >= DEPLOY_YOUR_TROOPS_STEP)
                    super.showCircles();
                break;
        }
    }

    protected void circlePressed(int circleNum) {

        switch (tutorialState) {
            case CONTINENT_CIRCLE_STEP:
                circleChosen = circleNum;
            case BRING_CIRCLE_BACK:
                if(circleChosen != circleNum)
                    return;
                super.circlePressed(circleNum);
                nextStep();
                break;
            default:
                if(tutorialState >= DEPLOY_YOUR_TROOPS_STEP)
                    super.circlePressed(circleNum);
                break;
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if(fastDeploy)
            if( state.getCurrentState() == GameState.DEPLOY) {
                state.playerActions(chosenDeployProvince);
            }
            else
                fastDeploy = false;
        if(fastAttack)
            if( tutorialState == FAST_ATTACK_STEP) {
                state.playerActions(chosenEnemyProvince);
            }
            else
                fastAttack = false;
        if(removeSelection) {
            for(int i = 0; i < state.getProvincesArray().getProvinceAmount(); i ++) {
                ((TutorialProvince) state.getProvincesArray().get(i)).setChosenOne(false);
            }
            removeSelection = false;
        }

    }

    protected void drawInterfaces(float delta) {
        for(int i = 0; i < stack.size(); i ++) {
            stack.draw(i,delta);
            if(stack.getInterface(i) == gameProcessInterface)
                selectionRectangle.draw();
            if(!(tutorialState == END_ATTACK_BUTTON_STEP && state.getCurrentState() != GameState.ATTACK))
                selectedFrame.draw(delta,stack.getInterface(i));
        }
        stack.clean();
    }

    public int getTutorialState() {
        return tutorialState;
    }

    public void setChosenEnemyProvince(TutorialProvince chosenEnemyProvince) {
        this.chosenEnemyProvince = chosenEnemyProvince;
    }

    public void setRemoveSelection(boolean removeSelection) {
        this.removeSelection = removeSelection;
    }

    public void setChosenDeployProvince(Province chosenDeployProvince) {
        this.chosenDeployProvince = chosenDeployProvince;
    }
}
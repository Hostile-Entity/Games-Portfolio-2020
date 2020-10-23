package com.strat7.game.AI;

import com.strat7.game.GameInfo.GameState;
import com.strat7.game.GameInfo.Province;


public class AIPlayer {
    private long time;
    private long deltaTime;
    private int AISpeed = 0;

    private GameState state;


    private InformationAboutState allInfo ;

    private AttackPair selectedProvinces;
    public AttackPair getSelectedProvinces() {
        return selectedProvinces;
    }
    public void setSelectedProvinces(AttackPair selectedProvinces) {
        this.selectedProvinces = selectedProvinces;
    }

    public AIPlayer (GameState curState) {
        allInfo = new InformationAboutState(curState);
        setAISpeed(curState.getAISpeed());
        setState(curState);
    }

    public void setState (GameState curState) {
        time = System.nanoTime();
        state = curState;
        selectedProvinces = null;
        allInfo.set(curState);
    }

    public int makeTurn() {
        if (System.nanoTime() - time > deltaTime / 2 ) {
            switch (state.getCurrentState()) {
                case 0:
                    state.playerActions(allInfo.getProvinceToDeploy());
                    if (state.getCurrentState() == 1) {
                        allInfo.setAlreadyFoundWay(false);
                        allInfo.updateActiveState();
                    }
                    allInfo.updatePoints();
                    time = System.nanoTime();
                    return 0;
                case 1:
                    if (allInfo.getAvailableTroops() > 0) {
                        if (selectedProvinces == null) {
                            selectedProvinces = allInfo.getAttackPair();

                            if (selectedProvinces == null) {
                                allInfo.setAvailableTroops(0);
                                return 0;
                            }
                            state.playerActions(selectedProvinces.getAttacker());
                        } else {
                            if (System.nanoTime() - time > deltaTime) {
                                state.playerActions(selectedProvinces.getDefender());
                                if (state.getCurrentState() != 2) {
                                    if (selectedProvinces.getDefender().getOwnerID() !=
                                            selectedProvinces.getAttacker().getOwnerID()) {
                                        allInfo.updateOnlyOneActiveProvince(selectedProvinces.getAttacker());
                                    }
                                    else {
                                        allInfo.updateActiveState(selectedProvinces.getDefender());
                                    }
                                    selectedProvinces = null;
                                }
                                time = System.nanoTime();
                            }
                        }
                    } else {
                        state.ifButtonUnpressed();
                    }
                    return 0;
                case 2:
                    int slider = allInfo.distribution(
                            selectedProvinces.getDefender(),
                            selectedProvinces.getAttacker(),
                            state.getFreeTroops());
                    for (; slider > 0;slider -- )
                        state.playerActions(selectedProvinces.getAttacker());
                    for (; state.getFreeTroops() > 0; )
                        state.playerActions(selectedProvinces.getDefender());
                    allInfo.updateActiveState(selectedProvinces.getDefender());
                    selectedProvinces = null;
                    time = System.nanoTime();
                    break;
                case 3:
                    selectedProvinces = allInfo.transport();
                    if(selectedProvinces != null) {
                        state.clearLightenedProvinces();
                        state.playerActions( selectedProvinces.getAttacker());
                        while (selectedProvinces.getAttacker().getNumOfTroops() > 1)
                            state.playerActions(selectedProvinces.getDefender());
                    }
                    Province province = state.ifButtonUnpressed();
                    if(province != null && province.getOwnerID() == null)
                        return -300;
                    else
                        return 1;
                default:
                    break;
            }
        }
        return 0;
    }

    public int getAISpeed() {
        return AISpeed;
    }

    public void setAISpeed(int AISpeed) {
        this.AISpeed = AISpeed;
        switch (AISpeed) {
            case 0:
            case 1:
                deltaTime = 0;
                break;
            case 2:
                deltaTime = 250000000L  ;
                break;
            case 3:
                deltaTime = 250000000L  * 2;
            case 4:
                deltaTime = 250000000L  * 4;
                break;
        }
    }
}
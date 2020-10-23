package com.strat7.game.Tutorial;

import com.strat7.game.GameInfo.GameState;
import com.strat7.game.GameInfo.PlayersList;
import com.strat7.game.GameInfo.Province;
import com.strat7.game.Screens.Game.InterfaceGame;
import com.strat7.game.Screens.Game.GameScreen;
import com.strat7.game.Strat7;

import java.util.ArrayList;

/**
 * Created by Евгений on 08.08.2017.
 */

public class TutorialGameState extends GameState {

    private TutorialGameScreen gameScreen;

    TutorialProvince chosenEnemyProvince;
    Province rememberedProvince = null;

    private boolean attacked = false;

    public TutorialGameState( PlayersList playersList, TutorialGameScreen gameScreen, String mapName) {
        super(playersList,mapName);
        this.gameScreen = gameScreen;
        playersList.getCurrentPlayer().setArtificial(false);
        playersList.getActivePlayer(1).setArtificial(false);

        Province province = provincesArray.getRandomProvinceWithEnemies(playersList.getCurrentPlayer());

        int destroyedFriendlyTroops = 0;
        int[] destroyedEnemyTroops = new int[playersList.getActivePlayersAmount()];

        destroyedFriendlyTroops += province.getNumOfTroops() - 8;
        province.setNumOfTroops(8);
        for(Province enemyProvince : province.getAdjacentEnemyProvinces()) {
            destroyedEnemyTroops[enemyProvince.getOwnerID().getPID()] += enemyProvince.getNumOfTroops() - 1;
            enemyProvince.setNumOfTroops(1);
        }

        chosenEnemyProvince = ((TutorialProvince) province);


        for(int player = 0; player < destroyedEnemyTroops.length; player++) {
            ArrayList<Province> enemyProvinces = new ArrayList<Province>();
            for(Province enemyProvince :provincesArray.getPlayersProvinces(player)) {
                if((destroyedEnemyTroops[player] > 0 || destroyedEnemyTroops[player] < 0 && enemyProvince.getNumOfTroops() > 1) &&
                        !enemyProvince.isAdjacent(province))
                    enemyProvinces.add(enemyProvince);
            }
            Province enemyProvince;
            while (destroyedEnemyTroops[player] != 0) {
                enemyProvince = enemyProvinces.get((int)(enemyProvinces.size() * Math.random()));
                    if(destroyedEnemyTroops[player] > 0) {
                        destroyedEnemyTroops[player] --;
                        enemyProvince.incNumOfTroops();
                    }
                    else {
                        destroyedEnemyTroops[player] ++;
                        enemyProvince.decNumOfTroops();
                        if(enemyProvince.getNumOfTroops() == 1)
                            enemyProvinces.remove(enemyProvince);
                    }
            }
        }

        if(destroyedFriendlyTroops != 0) {
            ArrayList<Province> friendlyProvinces = new ArrayList<Province>();
            for (int i = 0; i < provincesArray.getProvinceAmount(); i++) {
                if (provincesArray.get(i).getOwnerID() == playersList.getCurrentPlayer() &&
                        (destroyedFriendlyTroops > 0 || destroyedFriendlyTroops < 0 && provincesArray.get(i).getNumOfTroops() != 1) &&
                        provincesArray.get(i) != province)
                    friendlyProvinces.add(provincesArray.get(i));
            }
            while (destroyedFriendlyTroops != 0) {
                Province friendlyProvince = friendlyProvinces.get((int)(friendlyProvinces.size() * Math.random()));
                if(destroyedFriendlyTroops > 0) {
                    destroyedFriendlyTroops --;
                    friendlyProvince.incNumOfTroops();
                }
                else {
                    destroyedFriendlyTroops ++;
                    friendlyProvince.decNumOfTroops();
                    if(friendlyProvince.getNumOfTroops() == 1)
                        friendlyProvinces.remove(friendlyProvince);
                }
            }
        }
        gameScreen.setChosenEnemyProvince(chosenEnemyProvince);
    }

    @Override
    protected void loadProvinces(String mapName) {
        provincesArray = new TutorialProvinceList(mapName,playersIdList);
    }

    public void changeCurrentState(int state){
        if (playersIdList == null) {
            return;
        }
        currentState = state;
        if (!playersIdList.getCurrentPlayer().isArtificial()) {
            switch (state) {
                case DEPLOY:
                    buttons.get(InterfaceGame.MAIN_BUTTON).setText(Strat7.myBundle.get("deploy_all"));
                    buttons.get(InterfaceGame.MAIN_BUTTON).setVisible(false);
                    break;

                case ATTACK:
                    buttons.get(InterfaceGame.MAIN_BUTTON).setVisible(true);
                    buttons.get(InterfaceGame.MAIN_BUTTON).setText(Strat7.myBundle.get("end_attack"));
                    if(gameScreen.getTutorialState() == TutorialGameScreen.DEPLOY_BUTTON_STEP ||
                            gameScreen.getTutorialState() == TutorialGameScreen.DISTRIBUTE_BUTTON_STEP ||
                            gameScreen.getTutorialState() == TutorialGameScreen.FAST_DEPLOY_STEP
                            ) {
                        gameScreen.setChosenEnemyProvince(chosenEnemyProvince);
                        gameScreen.nextStep();
                    }

                    break;
                case DISTRIBUTE:
                    buttons.get(InterfaceGame.MAIN_BUTTON).setVisible(true);
                    buttons.get(InterfaceGame.MAIN_BUTTON).setText(Strat7.myBundle.get("move_all"));

                    if(gameScreen.getTutorialState() == TutorialGameScreen.ATTACK_STEP)
                        gameScreen.nextStep();
                    break;
                case MOVE:
                    buttons.get(InterfaceGame.MAIN_BUTTON).setVisible(true);
                    buttons.get(InterfaceGame.MAIN_BUTTON).setText(Strat7.myBundle.get("end_turn"));
                    if(gameScreen.getTutorialState() == TutorialGameScreen.END_ATTACK_BUTTON_STEP)
                        gameScreen.nextStep();

                    break;
                case -1:
                    break;
            }
        }
        switch (state) {
            case DEPLOY:
                freeTroops = playersIdList.getCurrentPlayer().countAvailableResources(provincesArray, playersIdList.getCurrentPlayerID());
                stateName = Strat7.myBundle.get("deploy") + " " + Integer.toString(freeTroops);

                if(gameScreen.getTutorialState() == TutorialGameScreen.END_TURN_BUTTON_STEP) {
                    freeTroops = 27;
                    stateName = Strat7.myBundle.get("deploy") + " " + Integer.toString(freeTroops);
                    Province province = provincesArray.getRandomProvinceWithEnemies(playersIdList.getCurrentPlayer());
                    chosenEnemyProvince = ((TutorialProvince) province.getRandomAdjacentEnemyProvince());
                    gameScreen.setChosenEnemyProvince(chosenEnemyProvince);
                    chosenEnemyProvince.setNumOfTroops(20);
                }
                //statTracker.deployed(freeTroops,playersIdList.getCurrentPlayerID());
                break;
            case ATTACK:
                stateName = Strat7.myBundle.get("attack");
                break;
            case DISTRIBUTE:
                stateName = Strat7.myBundle.get("move") + " " + Integer.toString(freeTroops);
                break;
            case MOVE:
                provinceMove = null;
                stateName = Strat7.myBundle.get("move");
                break;
            case -1:
                break;
        }
    }

    public Province playerActions(Province pressedProvinceFirst){

        Province chosenProvince = getCurrentProvince();
        Province tempProvince = null;
        switch(currentState){
            //Deploy
            case DEPLOY:
                if(gameScreen.getTutorialState() != TutorialGameScreen.DEPLOY_YOUR_TROOPS_STEP &&
                        gameScreen.getTutorialState() != TutorialGameScreen.FAST_DEPLOY_STEP &&
                        gameScreen.getTutorialState() != TutorialGameScreen.GAME_STEP ) {
                    return null;
                }
                if( pressedProvinceFirst == null ||
                        pressedProvinceFirst.getOwnerID() !=
                                getPlayersIdList().getCurrentPlayer()) {
                    if(gameScreen.getTutorialState() == TutorialGameScreen.FAST_DEPLOY_STEP)
                        return null;
                    buttons.get(InterfaceGame.MAIN_BUTTON).setVisible(false);
                    clearLightenedProvinces();
                    return null;
                }
                if(gameScreen.getTutorialState() == TutorialGameScreen.FAST_DEPLOY_STEP &&
                        (rememberedProvince != null && pressedProvinceFirst != rememberedProvince ||
                        rememberedProvince ==null && !pressedProvinceFirst.isAdjacent(chosenEnemyProvince)))
                    return null;

                if(gameScreen.getTutorialState() == TutorialGameScreen.FAST_DEPLOY_STEP &&
                        rememberedProvince == null) {
                    rememberedProvince = pressedProvinceFirst;
                    gameScreen.setRemoveSelection(true);
                    gameScreen.setChosenDeployProvince(rememberedProvince);
                }
                if(pressedProvinceFirst != chosenProvince) {
                    clearLightenedProvinces();
                    lightProvince(pressedProvinceFirst);
                }
                pressedProvinceFirst.incNumOfTroops();
                if (-- freeTroops != 0) {
                    buttons.get(InterfaceGame.MAIN_BUTTON).setVisible(true);
                    stateName = Strat7.myBundle.get("deploy") + " " + freeTroops;
                } else {
                    changeCurrentState(1);
                    for (Province province : pressedProvinceFirst.getAdjacentEnemyProvinces()) {
                        lightProvince(province);
                    }
                }
                if(gameScreen.getTutorialState() == TutorialGameScreen.DEPLOY_YOUR_TROOPS_STEP)
                    gameScreen.nextStep();
                return pressedProvinceFirst;

            case ATTACK:
                if( pressedProvinceFirst == null ) {
                    if(gameScreen.getTutorialState() != TutorialGameScreen.ATTACK_STEP &&
                            gameScreen.getTutorialState() != TutorialGameScreen.FAST_ATTACK_STEP)
                        clearLightenedProvinces();
                    return null;
                }
                if(pressedProvinceFirst.getOwnerID() !=
                        getPlayersIdList().getCurrentPlayer()){
                    if (chosenProvince == null) {
                        return null;
                    }
                    else {
                        if (pressedProvinceFirst.isAdjacent(chosenProvince) &&
                                (gameScreen.getTutorialState() == TutorialGameScreen.ATTACK_STEP && pressedProvinceFirst.isAdjacent(chosenEnemyProvince) ||
                                        gameScreen.getTutorialState() == TutorialGameScreen.FAST_ATTACK_STEP && pressedProvinceFirst == chosenEnemyProvince ||
                                        gameScreen.getTutorialState() != TutorialGameScreen.FAST_ATTACK_STEP && gameScreen.getTutorialState() != TutorialGameScreen.ATTACK_STEP)
                                ) {//pressed adjacent enemy province
                            //main attack!!!
                            if (attack(chosenProvince, pressedProvinceFirst)) {
                                clearLightenedProvinces();
                                if(gameScreen.getTutorialState() == TutorialGameScreen.FAST_ATTACK_STEP)
                                    gameScreen.nextStep();
                                if (chosenProvince.getNumOfTroops() > 1) {
                                    freeTroops = chosenProvince.getNumOfTroops() - 1;
                                    chosenProvince.setNumOfTroops(1);
                                    changeCurrentState(DISTRIBUTE);
                                    lightProvince(pressedProvinceFirst);
                                    lightProvince(chosenProvince);
                                    return chosenProvince;
                                }

                                lightProvince(pressedProvinceFirst);
                                for (Province province : pressedProvinceFirst.getAdjacentEnemyProvinces()) {
                                    lightProvince(province);
                                }
                                return pressedProvinceFirst;
                            } else {
                                if(gameScreen.getTutorialState() == TutorialGameScreen.FAST_ATTACK_STEP &&
                                        chosenProvince.getNumOfTroops() <= 3)
                                    gameScreen.nextStep();
                                return chosenProvince;
                            }
                        } else {
                            if(gameScreen.getTutorialState() != TutorialGameScreen.ATTACK_STEP &&
                                    gameScreen.getTutorialState() != TutorialGameScreen.FAST_ATTACK_STEP)
                                clearLightenedProvinces();
                            return null;
                        }
                    }
                }
                else {
                    //pressed new friendly province
                    if(gameScreen.getTutorialState() == TutorialGameScreen.ATTACK_STEP ||
                            gameScreen.getTutorialState() == TutorialGameScreen.FAST_ATTACK_STEP)
                        return null;
                    clearLightenedProvinces();
                    if(gameScreen.getTutorialState() == TutorialGameScreen.CHOOSE_PROVINCE_STEP) {
                        if (pressedProvinceFirst == chosenEnemyProvince )
                            gameScreen.nextStep();
                        else
                            return null;
                    }

                    clearLightenedProvinces();
                    lightProvince(pressedProvinceFirst);
                    for (Province province : pressedProvinceFirst.getAdjacentEnemyProvinces()) {
                        lightProvince(province);
                    }
                    return pressedProvinceFirst;
                }
                // Move after Attack
                // changed by me (error when we press ocean)
            case DISTRIBUTE:
                if(pressedProvinceFirst == null) {
                    return chosenProvince;
                }

                if(gameScreen.getTutorialState() == TutorialGameScreen.DISTRIBUTE_BUTTON_STEP)
                    return null;
                if (pressedProvinceFirst != lightenedProvinces.get(1)) {
                    if (pressedProvinceFirst != chosenProvince) {
                        return chosenProvince;
                    }
                }
                else {
                    clearLightenedProvinces();
                    lightProvince(pressedProvinceFirst);
                    lightProvince(chosenProvince);
                }

                if (freeTroops > 0) {
                    stateName = Strat7.myBundle.get("move") + " " + --freeTroops;
                    pressedProvinceFirst.incNumOfTroops();
                    if(gameScreen.getTutorialState() == TutorialGameScreen.DISTRIBUTE_STEP)
                        gameScreen.nextStep();
                }
                if (freeTroops == 0) {
                    changeCurrentState(1);
                    clearLightenedProvinces();
                    lightProvince(pressedProvinceFirst);
                    for (Province province : pressedProvinceFirst.getAdjacentEnemyProvinces()) {
                        lightProvince(province);
                    }
                    return pressedProvinceFirst;
                }
                return chosenProvince;

            //Move
            case MOVE:
                // Very strange if
                if( pressedProvinceFirst == null) {
                    if (chosenProvince != null && provinceMove != null) {
                        return chosenProvince;
                    }
                    clearLightenedProvinces();
                    provinceMove = null;
                    return null;
                }
                if(pressedProvinceFirst.getOwnerID() == playersIdList.getCurrentPlayer()){
                    if (chosenProvince == null) {
                        lightProvince(pressedProvinceFirst);
                        for(Province province: pressedProvinceFirst.getPlayerAdjProvinces(pressedProvinceFirst.getOwnerID())) {
                            lightProvince(province);
                        }
                        return pressedProvinceFirst;
                    }
                    if(provinceMove == null) {
                        if (pressedProvinceFirst.isAdjacent(chosenProvince)) {//pressed adjacent friendly province
                            //move
                            if(gameScreen.getTutorialState() == TutorialGameScreen.MOVE_STEP)
                                gameScreen.nextStep();
                            provinceMove = chosenProvince;
                            clearLightenedProvinces();
                            move(chosenProvince, pressedProvinceFirst);
                            lightProvince(pressedProvinceFirst);
                            lightProvince(chosenProvince);
                            return pressedProvinceFirst;
                        }
                        if (pressedProvinceFirst == chosenProvince) {
                            return chosenProvince;
                        }
                        clearLightenedProvinces();
                        lightProvince(pressedProvinceFirst);
                        for(Province province: pressedProvinceFirst.getPlayerAdjProvinces(pressedProvinceFirst.getOwnerID())) {
                            lightProvince(province);
                        }
                        return pressedProvinceFirst;
                    }
                    else {
                        if (pressedProvinceFirst == chosenProvince) {
                            move(provinceMove, chosenProvince);
                            return pressedProvinceFirst;
                        }
                        if (pressedProvinceFirst == provinceMove) {
                            move(chosenProvince, provinceMove);
                            clearLightenedProvinces();
                            lightProvince(pressedProvinceFirst);
                            lightProvince(chosenProvince);
                            provinceMove = chosenProvince;
                            return pressedProvinceFirst;
                        }
                    }
                    return chosenProvince;
                }

                if (chosenProvince != null && provinceMove != null) {
                    return chosenProvince;
                }
                return getCurrentProvince();

        }

        return pressedProvinceFirst;

    }

    @Override
    public Province ifButtonUnpressed(){
        if (!playersIdList.getCurrentPlayer().isArtificial() || skipAITurn) {
            buttons.get(0).setPressed(false);
            switch (currentState) {
                case DEPLOY:
                    if(lightenedProvinces.size() > 0)
                        if (buttons.get(0).isVisible() && (
                                        gameScreen.getTutorialState() == TutorialGameScreen.DEPLOY_BUTTON_STEP ||
                                        gameScreen.getTutorialState() == TutorialGameScreen.GAME_STEP )
                                ) {
                            changeCurrentState(1);
                            lightenedProvinces.get(0).setNumOfTroops(lightenedProvinces.get(0).getNumOfTroops() + playersIdList.getCurrentPlayer().getAvailableResources());
                            playersIdList.getCurrentPlayer().setAvailableResources(0);
                            if(gameScreen.getTutorialState() == TutorialGameScreen.CHOOSE_PROVINCE_STEP) {
                                clearLightenedProvinces();
                                return null;
                            }
                            for (Province province: getCurrentProvince().getAdjacentEnemyProvinces()) {
                                lightProvince(province);
                            }
                        }
                    return getCurrentProvince();
                case ATTACK:
                    if(gameScreen.getTutorialState() != TutorialGameScreen.END_ATTACK_BUTTON_STEP &&
                            gameScreen.getTutorialState() != TutorialGameScreen.GAME_STEP)
                        return null;
                    changeCurrentState(3);
                    clearLightenedProvinces();
                    return null;
                case DISTRIBUTE:
                    if(gameScreen.getTutorialState() != TutorialGameScreen.DISTRIBUTE_BUTTON_STEP &&
                            gameScreen.getTutorialState() != TutorialGameScreen.END_ATTACK_BUTTON_STEP &&
                            gameScreen.getTutorialState() != TutorialGameScreen.GAME_STEP)
                        return null;
                    getCurrentProvince().setNumOfTroops(getCurrentProvince().getNumOfTroops() + freeTroops);
                    freeTroops = 0;
                    Province province = getCurrentProvince();
                    changeCurrentState(ATTACK);
                    clearLightenedProvinces();
                    lightProvince(province);
                    for (Province province1 : province.getAdjacentEnemyProvinces()) {
                        lightProvince(province1);
                    }
                    return province;
                case MOVE:
                    if(gameScreen.getTutorialState() != TutorialGameScreen.END_TURN_BUTTON_STEP &&
                            gameScreen.getTutorialState() != TutorialGameScreen.GAME_STEP)
                        return null;
                    buttons.get(0).setVisible(false);
                    if (playersIdList.nextPlayerTurn(provincesArray) == -1) {
                        return provincesArray.get(provincesArray.getProvinceAmount());
                    }
                    skipAITurn = false;
                    changeCurrentState(0);clearLightenedProvinces();
                    if (playersIdList.getCurrentPlayer().isArtificial())
                        GameScreen.getComputerPlayer().setState(this);
                    if(gameScreen.getTutorialState() == TutorialGameScreen.END_TURN_BUTTON_STEP)
                        gameScreen.nextStep();
                    return null;
            }
        } else {
            skipAITurn = true;
        }
        return getCurrentProvince();
    }

    public void attackAll(Province pressedProvinceFirst){
        if(gameScreen.getTutorialState() != TutorialGameScreen.FAST_DEPLOY_STEP &&
                gameScreen.getTutorialState() != TutorialGameScreen.FAST_ATTACK_STEP &&
                gameScreen.getTutorialState() != TutorialGameScreen.GAME_STEP
                )
            return;
        int oldCurrentState = currentState;
        if (isAttackAll()) {
            attackAllTime += Strat7.timePassed;
            if (attackAllTime > attackAllDefaultTimePerTroop) {
                attackAllTime -= attackAllDefaultTimePerTroop;

                attackAllDefaultTimePerTroop = timePerTroop / attackModifier;
                attackModifier *= ATTACK_MODIFIER;
                if (pressedProvinceFirst != null) {
                    playerActions(pressedProvinceFirst);
                    if(gameScreen.getTutorialState() != TutorialGameScreen.FAST_ATTACK_STEP &&
                            pressedProvinceFirst == chosenEnemyProvince)
                        attacked = true;
                    if (currentState != oldCurrentState) {
                        attackAllDefaultTimePerTroop = initialAttackAllDefaultTimePerTroop;
                        attackAllTime = 0;
                        attackModifier = ATTACK_MODIFIER;
                    }
                }
            }
        } else {
            attackAllDefaultTimePerTroop = initialAttackAllDefaultTimePerTroop;
            attackAllTime = 0;
            attackModifier = ATTACK_MODIFIER;
        }
    }

    @Override
    public void setAttackAll(boolean attackAll) {
        if(attacked && gameScreen.getTutorialState() == TutorialGameScreen.FAST_ATTACK_STEP)
            gameScreen.nextStep();
        super.setAttackAll(attackAll);
    }


}

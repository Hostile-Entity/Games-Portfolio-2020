package com.strat7.game.GameInfo;

import com.badlogic.gdx.utils.Disposable;
import com.strat7.game.Screens.Game.InterfaceGame;
import com.strat7.game.Screens.Game.GameScreen;
import com.strat7.game.Strat7;

import java.util.ArrayList;

public class GameState implements Disposable {
    public final static int DEPLOY = 0;
    public final static int ATTACK = 1;
    public final static int DISTRIBUTE = 2;
    public final static int MOVE = 3;

    //protected StatTracker statTracker;

    protected  InterfaceGame buttons;
    private StatTracker statTracker;

    public InterfaceGame getButtons() {
        return buttons;
    }

    public String getStateName(){return stateName;}
    protected int currentState; // 0 - deploy , 1 - attack, 2 - move after attack, 3 - move
    protected String stateName;
    protected ArrayList<Province> lightenedProvinces;
    protected  boolean skipAITurn = false;
    protected  boolean fog = false;
    protected boolean hideMap = false;

    protected  ProvincesList provincesArray;  // array of Provinces

    protected  PlayersList playersIdList;  // LIST of player`s id

    protected  int freeTroops;

    protected float attackAllTime = 0;
    protected float initialAttackAllDefaultTimePerTroop = 0f;
    protected float timePerTroop = 0f;
    protected float attackAllDefaultTimePerTroop = initialAttackAllDefaultTimePerTroop;
    protected final float ATTACK_MODIFIER = 1.2f;
    protected float attackModifier = 1;

    protected boolean attackAll = false;
    protected Province provinceMove = null;



    public GameState (PlayersList playersList, String mapName) {
        this.playersIdList = playersList;
        initStatistics(playersList);
        setInitialAttackAllDefaultTimePerTroop(Strat7.preferences.getInteger("ClickSpeed", 2));
        fog = Strat7.preferences.getBoolean("Fog", false);
        lightenedProvinces = new ArrayList<Province>();

        statTracker = new StatTracker();
        loadProvinces(mapName);
    }

    void initStatistics(PlayersList playersList) {
        //if(playersList != null)
        //statTracker = new StatTracker(playersList.getActivePlayersAmount());
    }

    public void setButtons(InterfaceGame buttons) {
        this.buttons = buttons;
    }

    protected void loadProvinces(String mapName) {
        provincesArray = new ProvincesList(mapName, playersIdList);
    }

    public ProvincesList getProvincesArray() {
        return provincesArray;
    }
    public void setProvincesArray(ProvincesList provincesArray) {
        this.provincesArray = provincesArray;
    }

    public PlayersList   getPlayersIdList() {
        return playersIdList;
    }
    public void setPlayersIdList(PlayersList playersIdList) {
        this.playersIdList = playersIdList;
    }

    public int           getCurrentState() {
        return currentState;
    }
    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    public int getFreeTroops() {
        return freeTroops;
    }
    public void setFreeTroops(int freeTroops) {
        this.freeTroops = freeTroops;
    }

    public ArrayList<Province> getLightenedProvinces() {
        return lightenedProvinces;
    }
    public int           getAISpeed() {
        return Strat7.preferences.getInteger("AISpeed");
    }
    public Province      getCurrentProvince() {
        return lightenedProvinces.size() == 0 ? null : lightenedProvinces.get(0);
    }
    public boolean       isSkipAITurn() {return skipAITurn;}
    public float getAttackAllTime() {
        return attackAllTime;
    }
    public void setAttackAllTime(float attackAllTime) {
        this.attackAllTime = attackAllTime;
    }

    public boolean isAttackAll() {
        return attackAll;
    }
    public void setAttackAll(boolean attackAll) {
        this.attackAll = attackAll;
    }

    public boolean getFog() {
        return fog;
    }
    public void setFog(boolean fog) {
        this.fog = fog;
    }

    public boolean isHidden() {
        return hideMap;
    }
    public void setHidden(boolean hideMap) {
        this.hideMap = hideMap;
    }

    public void setInitialAttackAllDefaultTimePerTroop(int ClickSpeed) {
        switch(ClickSpeed) {
            case 0:
                initialAttackAllDefaultTimePerTroop = 0f;
                timePerTroop = 0;
                break;
            case 1:
                initialAttackAllDefaultTimePerTroop = 0.1f;
                timePerTroop = 0.05f;
                break;
            case 2:
                initialAttackAllDefaultTimePerTroop = 0.3f;
                timePerTroop = 0.1f;
                break;
            case 3:
                initialAttackAllDefaultTimePerTroop = 0.5f;
                timePerTroop = 0.3f;
                break;
        }
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
                    break;
                case DISTRIBUTE:
                    buttons.get(InterfaceGame.MAIN_BUTTON).setVisible(true);
                    buttons.get(InterfaceGame.MAIN_BUTTON).setText(Strat7.myBundle.get("move_all"));
                    break;
                case MOVE:
                    buttons.get(InterfaceGame.MAIN_BUTTON).setVisible(true);
                    buttons.get(InterfaceGame.MAIN_BUTTON).setText(Strat7.myBundle.get("end_turn"));
                    break;
                case -1:
                    break;
            }
        }
        switch (state) {
            case 0:
                manageFog();
                if (freeTroops <= 0) {
                    freeTroops = playersIdList.getCurrentPlayer().countAvailableResources(provincesArray, playersIdList.getCurrentPlayerID());
                    //statTracker.deployed(freeTroops,playersIdList.getCurrentPlayerID());
                }
                stateName = Strat7.myBundle.get("deploy") + " " + freeTroops;
                break;
            case 1:
                stateName = Strat7.myBundle.get("attack");
                break;
            case 2:
                stateName = Strat7.myBundle.get("move") + " " + Integer.toString(freeTroops);
                break;
            case 3:
                stateName = Strat7.myBundle.get("move");
                provinceMove = null;
                break;
            case -1:
                break;
        }
    }

    public Province ifButtonUnpressed(){
        Province chosenProvince = getCurrentProvince();
        if (!playersIdList.getCurrentPlayer().isArtificial() || skipAITurn) {
            buttons.get(0).setPressed(false);
            switch (currentState) {
                case 0:
                    if(lightenedProvinces.size() > 0)
                        if (buttons.get(0).isVisible()) {
                            changeCurrentState(1);
                            lightenedProvinces.get(0).setNumOfTroops(lightenedProvinces.get(0).getNumOfTroops() + freeTroops);
                            for (Province province :chosenProvince.getAdjacentEnemyProvinces()) {
                                lightProvince(province);
                            }
                            freeTroops = 0;
                        }
                    return getCurrentProvince();
                case 1:
                    changeCurrentState(3);
                    clearLightenedProvinces();
                    return null;
                case 2:
                    getCurrentProvince().setNumOfTroops(getCurrentProvince().getNumOfTroops() + freeTroops);
                    freeTroops = 0;
                    clearLightenedProvinces();
                    lightProvince(chosenProvince);
                    changeCurrentState(1);
                    for (Province province: chosenProvince.getAdjacentEnemyProvinces()) {
                        lightProvince(province);
                    }
                    return chosenProvince;
                case 3:
                    buttons.get(0).setVisible(false);
                    statTracker.addTurnInfo(this);
                    statTracker.cout();
                    if (playersIdList.nextPlayerTurn(provincesArray) == -1) {
                        return provincesArray.get(provincesArray.getProvinceAmount());
                    }
                    skipAITurn = false;
                    changeCurrentState(0);
                    clearLightenedProvinces();
                    if (playersIdList.getCurrentPlayer().isArtificial())
                        GameScreen.getComputerPlayer().setState(this);
                    return null;
            }
        } else {
            skipAITurn = true;
        }
        return getCurrentProvince();
    }

    //main attack function
    protected boolean attack(Province from, Province to) {
        int attackerTroops = from.getNumOfTroops();
        int defenderTroops = from.getNumOfTroops();
        if (from.getNumOfTroops() == 1) {
            return false;
        }
        else{
            int Rnd=(int)(Math.random()*101); // from 0 to 100

            if (playersIdList.getCurrentPlayer().getLuck() < -2) {
                Rnd = (int)(Math.random()*26);
            }
            if (from.getNumOfTroops()-1>2){
                if(to.getNumOfTroops()>1){ //Атакующий 3, Защитник 2
                    if(Rnd>37){
                        if(Rnd>71){
                            from.decNumOfTroops();
                            from.decNumOfTroops();
                            playersIdList.getCurrentPlayer().changeLuck(-2);
                        }
                        else{
                            from.decNumOfTroops();
                            to.decNumOfTroops()  ;
                        }
                    }
                    else{
                        to.decNumOfTroops();
                        to.decNumOfTroops();
                        if (playersIdList.getCurrentPlayer().getLuck() < 0) {
                            playersIdList.getCurrentPlayer().changeLuck(2);
                        }
                    }
                }
                else{ //Атакующий 3, Защитник 1
                    if(Rnd>66){
                        from.decNumOfTroops();
                        playersIdList.getCurrentPlayer().changeLuck(-1);
                    }
                    else{
                        to.decNumOfTroops();
                        if (playersIdList.getCurrentPlayer().getLuck() < 0) {
                            playersIdList.getCurrentPlayer().changeLuck(1);
                        }
                    }
                }
            }
            else{
                if (from.getNumOfTroops()-1>1){
                    if(to.getNumOfTroops()>1){  //Атакующий 2, Защитник 2
                        if(Rnd>23){
                            if(Rnd>56){
                                from.decNumOfTroops();
                                from.decNumOfTroops();
                                playersIdList.getCurrentPlayer().changeLuck(-2);
                            }
                            else{
                                from.decNumOfTroops();
                                to.decNumOfTroops();
                            }
                        }
                        else{
                            to.decNumOfTroops();
                            to.decNumOfTroops();
                            if (playersIdList.getCurrentPlayer().getLuck() < 0) {
                                playersIdList.getCurrentPlayer().changeLuck(2);
                            }
                        }
                    }
                    else{   //Атакующий 2, Защитник 1
                        if(Rnd>58){
                            from.decNumOfTroops();
                            playersIdList.getCurrentPlayer().changeLuck(-1);
                        }
                        else{
                            to.decNumOfTroops();
                            if (playersIdList.getCurrentPlayer().getLuck() < 0) {
                                playersIdList.getCurrentPlayer().changeLuck(1);
                            }
                        }
                    }
                }
                else{
                    if(to.getNumOfTroops()>1){ //Атакующий 1, Защитник 2
                        if(Rnd>26){
                            from.decNumOfTroops();
                            playersIdList.getCurrentPlayer().changeLuck(-1);
                        }
                        else{
                            to.decNumOfTroops();
                            if (playersIdList.getCurrentPlayer().getLuck() < 0) {
                                playersIdList.getCurrentPlayer().changeLuck(1);
                            }
                        }
                    }
                    else{ //Атакующий 1, Защитник 1
                        if(Rnd>42){
                            from.decNumOfTroops();
                            playersIdList.getCurrentPlayer().changeLuck(-1);
                        }
                        else{
                            to.decNumOfTroops();
                            if (playersIdList.getCurrentPlayer().getLuck() < 0) {
                                playersIdList.getCurrentPlayer().changeLuck(1);
                            }
                        }
                    }
                }
            }
            //System.out.print(playersIdList.getCurrentPlayer().getLuck() + " ");
            if (to.getNumOfTroops() == 0){
                //statTracker.attacked(from.getNumOfTroops() - attackerTroops, defenderTroops,from.getOwnerID(),to.getOwnerID());
                from.decNumOfTroops();
                from.getOwnerID().incAvailableProvinces();
                to.getOwnerID().decAvailableProvinces();
                to.setOwnerID(getPlayersIdList().getCurrentPlayer());
                to.incNumOfTroops();
                return true;
            }
        }
        //statTracker.attacked(attackerTroops - from.getNumOfTroops(), defenderTroops - to.getNumOfTroops(),from.getOwnerID(),to.getOwnerID());
        return false;
    }

    protected void move(Province from, Province to){
        if (from.getNumOfTroops() != 1) {
            from.decNumOfTroops();
            to.incNumOfTroops();
        }
    }

    public Province playerActions(Province pressedProvinceFirst){
        Province chosenProvince = getCurrentProvince();
        switch(currentState){
            //Deploy
            case DEPLOY:
                if( pressedProvinceFirst == null ||
                        pressedProvinceFirst.getOwnerID() !=
                                getPlayersIdList().getCurrentPlayer()) {
                    clearLightenedProvinces();
                    buttons.get(InterfaceGame.MAIN_BUTTON).setVisible(false);
                    return null;
                }

                if(pressedProvinceFirst != chosenProvince) {
                    clearLightenedProvinces();
                    lightProvince(pressedProvinceFirst );
                }

                pressedProvinceFirst.incNumOfTroops();
                freeTroops--;
                if (freeTroops != 0) {
                    buttons.get(InterfaceGame.MAIN_BUTTON).setVisible(true);
                    stateName = Strat7.myBundle.get("deploy") + " " + Integer.toString(freeTroops);
                } else {
                    changeCurrentState(1);
                    for (Province province :pressedProvinceFirst.getAdjacentEnemyProvinces()) {
                        lightProvince(province);
                    }
                }
                return getCurrentProvince();
            //Attack
            case ATTACK:
                if( pressedProvinceFirst == null ) {
                    clearLightenedProvinces();
                    return null;
                }
                if(pressedProvinceFirst.getOwnerID() !=
                        getPlayersIdList().getCurrentPlayer()){
                    if (chosenProvince == null) {
                        return null;
                    } else
                    if (pressedProvinceFirst.isAdjacent(chosenProvince)){//pressed adjacent enemy province
                        //main attack!!!
                        if (attack(chosenProvince, pressedProvinceFirst)){
                            if (fog) {
                                for (Province province : pressedProvinceFirst.getAdjacentEnemyProvinces()) {
                                    province.setFogged(false);
                                }
                            }
                            if (chosenProvince.getNumOfTroops() > 1) {
                                freeTroops = chosenProvince.getNumOfTroops() - 1;
                                chosenProvince.setNumOfTroops(1);
                                changeCurrentState(2);
                                clearLightenedProvinces();
                                lightProvince(pressedProvinceFirst);
                                lightProvince(chosenProvince);
                                return chosenProvince;
                            }
                            clearLightenedProvinces();
                            lightProvince(pressedProvinceFirst);
                            for (Province province :pressedProvinceFirst.getAdjacentEnemyProvinces()) {
                                lightProvince(province);
                            }
                            return chosenProvince;
                        }
                        else{
                            return chosenProvince;
                        }
                    } else {
                        clearLightenedProvinces();
                        return null;
                    }
                }
                else{
                    //pressed new friendly province
                    if(pressedProvinceFirst == chosenProvince)
                        return chosenProvince;
                    clearLightenedProvinces();
                    lightProvince(pressedProvinceFirst);
                    for (Province province :pressedProvinceFirst.getAdjacentEnemyProvinces()) {
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

                if (pressedProvinceFirst.getProvinceID() != lightenedProvinces.get(1).getProvinceID()) {
                    if (pressedProvinceFirst.getProvinceID() != chosenProvince.getProvinceID()) {
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
                }
                if (freeTroops == 0) {
                    changeCurrentState(1);
                    clearLightenedProvinces();
                    lightProvince(pressedProvinceFirst);
                    for (Province province :pressedProvinceFirst.getAdjacentEnemyProvinces()) {
                        lightProvince(province);
                    }
                    return pressedProvinceFirst;
                }
                return chosenProvince;
            //Move
            // changed by me (mistake in selection lightened province)
            case MOVE:
                // Very strange if
                if( pressedProvinceFirst == null) {
                    if (lightenedProvinces.size() == 2) {
                        return chosenProvince;
                    }
                    clearLightenedProvinces();
                    provinceMove = null;
                    return null;
                }
                if(pressedProvinceFirst.getOwnerID() == playersIdList.getCurrentPlayer()){
                    if (chosenProvince == null) {
                        clearLightenedProvinces();
                        lightProvince(pressedProvinceFirst);
                        for(Province province: pressedProvinceFirst.getPlayerAdjProvinces(pressedProvinceFirst.getOwnerID())) {
                            lightProvince(province);
                        }
                        return pressedProvinceFirst;
                    }
                    if(provinceMove == null) {
                        if (pressedProvinceFirst.isAdjacent(chosenProvince)) {//pressed adjacent friendly province
                            //move
                            move(chosenProvince, pressedProvinceFirst);
                            clearLightenedProvinces();
                            lightProvince(pressedProvinceFirst);
                            lightProvince(chosenProvince);
                            provinceMove = chosenProvince;
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
                            move(lightenedProvinces.get(1), chosenProvince);
                            return pressedProvinceFirst;
                        }
                        if (pressedProvinceFirst == provinceMove) {
                            move(chosenProvince, pressedProvinceFirst);
                            clearLightenedProvinces();
                            lightProvince(pressedProvinceFirst);
                            lightProvince(chosenProvince);
                            provinceMove = chosenProvince;
                            return pressedProvinceFirst;
                        }
                    }
                    return chosenProvince;
                }

                if (provinceMove != null && chosenProvince != null) {
                    return chosenProvince;
                }
                clearLightenedProvinces();
                return getCurrentProvince();

        }
        return pressedProvinceFirst;
    }

    public void clearLightenedProvinces() {
        for(Province province : lightenedProvinces) {
            province.setChosen(false);
            province.setLightened(false);
        }
        lightenedProvinces.clear();
    }

    public void lightProvince(Province province) {
        if(lightenedProvinces.size() == 0)
            province.setChosen(true);
        else
            province.setLightened(true);
        lightenedProvinces.add(province);
    }

    // 29072017 deleted attackAll

    public void attackAll(int province){
        Province pressedProvinceFirst = provincesArray.get(province);

        int oldCurrentState = currentState;
        if (isAttackAll()) {
            attackAllTime += Strat7.timePassed;
            if (attackAllTime > attackAllDefaultTimePerTroop) {
                attackAllTime -= attackAllDefaultTimePerTroop;

                attackAllDefaultTimePerTroop = timePerTroop / attackModifier;
                attackModifier *= ATTACK_MODIFIER;
                if (pressedProvinceFirst != null) {
                    playerActions(pressedProvinceFirst);
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

    public void setAiSpeed(int aiSpeed) {
    }

    public boolean isStatisticsLoaded() {
        return true;
    }

    @Override
    public void dispose() {
        lightenedProvinces.clear();
        provincesArray.dispose();
    }

    //public StatTracker getStatTracker() {
    //return statTracker;
    //}

    public boolean getAttackAll() {
        return attackAll;
    }

    public void manageFog() {
        if (fog) {
            if (playersIdList.getRealPlayingPlayersAmount() == 0) {
                skipAITurn = false;
                fog = false;
                for (int i = 0; i < provincesArray.getProvinceAmount(); i++) {
                    provincesArray.get(i).setFogged(false);
                }
                return;
            }
            skipAITurn = true;
            if (playersIdList.getRealPlayingPlayersAmount() > 1 && !playersIdList.getCurrentPlayer().isArtificial()) {
                hideMap = true;
            }
            fogEnemyProvinces();
        }
    }

    public void fogEnemyProvinces() {
        for (int i = 0; i < provincesArray.getProvinceAmount(); i++) {
            if (playersIdList.getCurrentPlayer().isArtificial() || (provincesArray.get(i).getOwnerID().getPID() != playersIdList.getCurrentPlayer().getPID() && provincesArray.get(i).getPlayerAdjProvinces(playersIdList.getCurrentPlayer()).size() == 0)) {
                provincesArray.get(i).setFogged(true);
            } else {
                provincesArray.get(i).setFogged(false);
            }
        }
    }

    public StatTracker getStatTracker() {
        return statTracker;
    }
}
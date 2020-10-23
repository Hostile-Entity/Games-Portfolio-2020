package com.strat7.game.GameInfo;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Евгений on 09.06.2017.
 */

public class Player {
    private static int playerNumber = 0;    // MAX players amount
    public static int getPlayerAmounts() {
        return playerNumber;
    }

    // TODO set color
    // player id
    private int playerID = 0;
    private int availableProvincesAmount = 0;
    private int availableResources = 0;
    private int luck = 0;
    // player color
    private Color color;
    private int artificialType = 0;
    private boolean artificial = true;
    private boolean external = true;

    public Player () {
        playerID = playerNumber ++;
        this.color = Color.WHITE;
    }

    public Player (Color color) {
        this.color = color;
        playerID = playerNumber ++;
    }

    public int getLuck() {
        return luck;
    }
    public void changeLuck(int luck) {
        this.luck += luck;
    }


    public void setColor (Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
    public int getPID () {
        return playerID;
    }


    public int setAvailableProvinces(int Num) {
        return availableProvincesAmount = Num;
    }
    public int getAvailableProvinces() {
        return availableProvincesAmount;
    }
    public int incAvailableProvinces() {
        return ++availableProvincesAmount;
    }
    public int decAvailableProvinces() {
        return --availableProvincesAmount;
    }


    public int countAvailableResources(ProvincesList allProvinces, int playerID) {
        int availableT= 3;
        int i = 0, j = 0;
        for (i = 0; i < allProvinces.getContinentAmount(); i++) {
            availableT += allProvinces.getContinentBonus()[i];
        }
        for (i = 0; i < allProvinces.getContinentAmount(); i++) {
            for (; j < allProvinces.getContinentArray()[i];j++) {
                if(allProvinces.get(j).getOwnerID().getPID() != playerID){
                    availableT -= allProvinces.getContinentBonus()[i];
                    j = allProvinces.getContinentArray()[i];
                    break;
                }
            }
        }
        return availableResources = availableT;
    }
    public int decAvailableResources() {
        if(availableResources != 0)
            return -- availableResources;
        return 0 ;
    }
    public int getAvailableResources() {
        return availableResources;
    }
    public void setAvailableResources(int num) {
        availableResources = num;
    }

    public boolean isArtificial() {
        return artificial;
    }
    public int getArtificialType() {
        return artificialType;
    }

    public boolean setArtificial(boolean artificial) {
        this.artificial = artificial;
        return artificial;
    }
    public void setArtificialType(int artificialType) { this.artificialType = artificialType;}

    public float getMediumColor () {
        return color.r + color.g + color.b;
    }

    public boolean isExternal() {
        return external;
    }
    public void setExternal(boolean external) {
        this.external = external;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }
}
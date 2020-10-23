package com.strat7.game.GameInfo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Disposable;
import com.strat7.game.Interfaces.Basics.BoundTexture;
import com.strat7.game.Interfaces.Basics.Text.BoundText;
import com.strat7.game.Interfaces.Basics.Button;
import com.strat7.game.Interfaces.Basics.BoundBasics.Carcass;

import java.util.ArrayList;
import com.strat7.game.GameInfo.PictureChanges.TextSubstrate;
import com.strat7.game.Strat7;

/**
 * Created by Евгений on 09.06.2017.
 */

public class Province extends Button implements Disposable{

    private boolean fogged = false;
    //private int topOffset, width;

    private Player ownerID = null;      // player owner id            DONE
    private int provinceID = -1;        // player owner id            DONE
    private int provinceIDColor = -1;   // color that identify province DONE
    private int numOfTroops = 1;        // number of troops   DONE

    private ArrayList<Province> adjacentProvinces; // array of adjacent provinces (array of int) DONE

    private Pixmap pressMap;
    private double fontDiv;

    private TextSubstrate textSubstrate;

    private boolean lightened = false;
    protected double lightenedTime = 0;

    private boolean chosen = false;
    protected double chosenTime = 0;

    public Province (Carcass carcass, int xPos, int yPos, Player pID, String  texture, ProvincesList provincesList) {
        super(
                carcass,
                xPos,yPos,
                texture == null ? 0 : Strat7.graphics.getTextureByName(texture).getWidth(),
                texture == null ? 0 : Strat7.graphics.getTextureByName(texture).getHeight(),
                texture,
                Color.BLACK,Color.BLACK
        );
        pressMap = provincesList.getPixmap();
        fontDiv = provincesList.getFontDiv();
        ownerID = pID;
        if(ownerID == null)
            color = colorPressed = Color.GRAY;
        else
            color = colorPressed = ownerID.getColor();
        adjacentProvinces = new ArrayList<Province>();
        text.setText(numOfTroops <= 0 ? "" : Integer.toString(numOfTroops));
    }

    @Override
    protected void initText() {
        text = new BoundText(this,"",Color.BLACK,  0,0,-1,-1,Strat7.normalFontSize);
    }

    @Override
    protected void commonDraw() {
        if(!visible)
            return;
        if(pressed || true) {
            setPosY(getLocalPosY() - 1);
        }
        if (fogged) {
            Strat7.getCurrentBatch().setColor(Color.GRAY);
            background.draw(false);
            setPosY(getLocalPosY() + 1);
            return;
        }
        Strat7.getCurrentBatch().setColor(color);
        background.draw(false);
        if(chosen) {
            Strat7.getCurrentBatch().setColor(1, 1, 1, 0.75f);
            background.draw(false);
            chosenTime += Gdx.graphics.getDeltaTime();
            if(chosenTime >= 1)
                chosenTime = -1;
        }
        if(lightened && ! chosen) {
            Strat7.getCurrentBatch().setColor(1, 1, 1, Math.abs((float)lightenedTime) / 2);
            background.draw(false);
            lightenedTime += Gdx.graphics.getDeltaTime();
            if(lightenedTime >= 1)
                lightenedTime = -1;
        }
        Strat7.getCurrentBatch().setColor(Color.BLACK);
        //(new BoundTexture(this,Strat7.graphics.getTextureByName("Interface/BackForText.png"),getLocalCenterX(),getLocalCenterY(), 1,1,1)).draw(false);
        if(pressed || true) {
            setPosY(getLocalPosY() + 1);
        }

    }

    @Override
    protected void commonDrawText() {
        if(!visible || fogged)
            return;
        setPosY(getLocalPosY() - 1);
        if(pressed) {
            text.drawCenterIn(getLocalCenterX(),getLocalCenterY(),false);
        }
        else {
            text.drawCenterIn(getLocalCenterX(),getLocalCenterY(),false);
        }
        setPosY(getLocalPosY() + 1);
    }

    @Override
    public void boundWithNewCarcass(Carcass frame, double l, double b, double scale) {
        super.boundWithNewCarcass(frame, l, b, scale);
        text.setScale(pressMap.getHeight() / fontDiv / 625);
        // text.setScale(1);
    }

    // todo
    @Override
    public boolean isPressed(double X, double Y) {
        if(super.isPressed(X, Y) ) {
            if(pressMap.getPixel((int)getLocalOffsetX(X),pressMap.getHeight() - (int)getLocalOffsetY(Y) - 1) == provinceIDColor)
                return true;
        }
        return false;
    }

    // choose and light province


    public boolean isChosen() {
        return chosen;
    }
    public void setChosen(boolean chosen) {
        if(chosen != this.chosen)
            chosenTime = 0;
        this.chosen = chosen;
    }

    public boolean isLightened() {
        return lightened;
    }
    public void setLightened(boolean lightened) {
        if(lightened != this.lightened)
            lightenedTime = 0;
        this.lightened = lightened;
    }

    public void setFogged(boolean fogged) {
        this.fogged = fogged;
    }
    public boolean isFogged() {
        return fogged;
    }

    // owner
    public Player getOwnerID() {
        return ownerID;
    }
    public void setOwnerID(Player player) {
        ownerID = player;
        if(ownerID == null)
            color = colorPressed = Color.GRAY;
        else
            color = colorPressed = ownerID.getColor();
    }

    // province id and color
    public int getProvinceIDColor() {
        return provinceIDColor;
    }
    public void setProvinceIDColor(int color) {
        provinceIDColor = color;
    }

    public void setProvinceID(int provinceID) {
        this.provinceID = provinceID;
    }
    public int getProvinceID() {
        return provinceID;
    }

    // Troops
    public int getNumOfTroops() {
        return numOfTroops;
    }
    public int setNumOfTroops(int num) {
        numOfTroops = num;
        text.setText(Integer.toString(num));
        return num;
    }
    public int incNumOfTroops() {
        return setNumOfTroops(++numOfTroops);
    }
    public int decNumOfTroops() {
        return setNumOfTroops(--numOfTroops);
    }

    // Adjacent
    public void addAdjacentProvince(Province province) {
        adjacentProvinces.add(province);
    }
    public void removeAdjacentProvince(Province province) {
        adjacentProvinces.remove(province);
    }
    public ArrayList<Province> getAdjacentProvinces() {
        return adjacentProvinces;
    }
    public boolean isAdjacent(Province province){
        for (int i = 0; i < adjacentProvinces.size(); i++) {
            if (adjacentProvinces.get(i).getProvinceID() == province.getProvinceID()){
                return true;
            }
        }
        return false;
    }


    // Additional helpful functions

    // random adjacent provinces
    public Province getRandomAdjacentEnemyProvince() {
        int adjProvAmount = 0;
        for(int i = adjacentProvinces.size() - 1;
            i >= 0; i -- ) {
            if( adjacentProvinces.get(i).getOwnerID() != ownerID) {
                adjProvAmount ++;
            }
        }
        adjProvAmount = (int) (Math.random() * adjProvAmount);
        for (int i = adjacentProvinces.size() - 1;
             i >= 0; i--) {
            if (adjacentProvinces.get(i).getOwnerID() != ownerID) {
                if (adjProvAmount == 0) {
                    return adjacentProvinces.get(i);
                }
                adjProvAmount--;
            }

        }
        return null;
    }
    public Province getRandomAdjacentFriendlyProvince(Player playerID) {
        int adjProvAmount = 0;
        for(int i = adjacentProvinces.size() - 1;
            i >= 0; i -- ) {
            if( adjacentProvinces.get(i).getOwnerID() == playerID) {
                adjProvAmount ++;
            }
        }
        adjProvAmount = (int) (Math.random() * adjProvAmount);
        for (int i = adjacentProvinces.size() - 1;
             i >= 0; i--) {
            if (adjacentProvinces.get(i).getOwnerID() == playerID) {
                if (adjProvAmount == 0) {
                    return adjacentProvinces.get(i);
                }
                adjProvAmount--;
            }

        }
        return null;
    }

    // troops arround
    public int getPlayerTroopsAmountAround(Player playerID) {
        int adjTroopsAmount = 0;
        for(Province province: adjacentProvinces) {
            if(province.getOwnerID() == playerID)
                adjTroopsAmount += province.getNumOfTroops();
        }
        return adjTroopsAmount;
    }
    public int getEnemyTroopsAround() {
        int army = 0;
        for(Province province: adjacentProvinces) {
            if(province.getOwnerID() != ownerID)
                army += province.getNumOfTroops();
        }
        return army;
    }
    public int getEnemyProvincesAmountAround() {
        int army = 0;
        for(Province province: adjacentProvinces) {
            if(province.getOwnerID() != ownerID)
                army ++;
        }
        return army;
    }
    public int getPlayerProvincesAmountAround(Player playerID) {
        int army = 0;
        for(Province province: adjacentProvinces) {
            if(province.getOwnerID() == playerID )
                army ++;
        }
        return army;
    }
    public int getMaxTroopsAround() {
        int max = 0;
        for(Province province: adjacentProvinces) {
            if(province.getOwnerID() != ownerID)
                if(max < province.getNumOfTroops())
                    max = province.getNumOfTroops();
        }
        return max;
    }
    public int getPlayerActiveTroopsAround(Player playerID) {
        return getPlayerTroopsAmountAround(playerID) - getPlayerProvincesAmountAround(playerID);
    }


    // more functions with adjacent provinces
    public ArrayList<Province> getPlayerAdjProvinces(Player playerID){
        ArrayList<Province> provinceArrayList = new ArrayList<Province>();
        for(Province province: adjacentProvinces) {
            if(province.getOwnerID() == playerID)
                provinceArrayList.add(province);
        }
        return provinceArrayList;
    }
    public ArrayList<Province> getAdjacentEnemyProvinces() {
        ArrayList<Province>  provinceArrayList = new ArrayList<Province>();
        for(Province province: adjacentProvinces) {
            if(province.getOwnerID() != ownerID) {
                provinceArrayList.add(province);
            }
        }
        return provinceArrayList;
    }
    public Province getStrongestAdjacentEnemyProvince() {
        Province max = null;
        for(Province province: adjacentProvinces) {
            if(province.getOwnerID() != ownerID) {
                if(max == null)
                    max = province;
                else
                if (max.getNumOfTroops() < province.getNumOfTroops())
                    max = province;
            }
        }
        return max;
    }
    public Province getStrongestPlayerAdjacentProvince(Player playerID) {
        Province strongestFriendlyProvince = null;
        for(Province province : getAdjacentProvinces()) {
            if(province.getOwnerID() == playerID) {
                if (strongestFriendlyProvince == null)
                    strongestFriendlyProvince = province;
                else
                if (strongestFriendlyProvince.getNumOfTroops() < province.getNumOfTroops())
                    strongestFriendlyProvince = province;
            }

        }
        return strongestFriendlyProvince;
    }


    @Override
    public void dispose() {
        super.dispose();
    }

}

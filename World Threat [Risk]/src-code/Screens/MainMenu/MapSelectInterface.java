package com.strat7.game.Screens.MainMenu;

import com.badlogic.gdx.graphics.Color;
import com.strat7.game.GameInfo.PictureChanges.CameraMovement;
import com.strat7.game.Interfaces.Basics.*;
import com.strat7.game.Interfaces.Basics.BoundBasics.BoundWithFrame;
import com.strat7.game.Interfaces.Basics.BoundBasics.Carcass;
import com.strat7.game.Interfaces.Basics.Text.BoundText;
import com.strat7.game.Interfaces.ButtonWithCoin;
import com.strat7.game.Interfaces.SlidingList;
import com.strat7.game.Interfaces.SlidingMaps;
import com.strat7.game.Strat7;
import com.strat7.game.Tutorial.SelectedFrame;

import java.util.ArrayList;


public class MapSelectInterface extends Interface {
    public static final int PREVIOUS_BUTTON = 0;
    public static final int NEXT_BUTTON = 1;

    public static final int FIRST_BUY_BUTTON = 2;
    public static final int BUY_BUTTON_AMOUNT = Strat7.MAP_AMOUNT ;
    public static final int BACK_TO_PREVIOUS_SCREEN_BUTTON = 2;

    public static final int MAP_SLIDE = Strat7.MAP_AMOUNT;

    private BoundTexture background;
    private int currentMapNum;

    private ArrayList<BoundTexture > mapBackgrounds;
    private ArrayList<BoundTexture > maps;
    private ArrayList<BoundTexture > lockBacks;
    private ArrayList<BoundTexture > locks;
    private ArrayList<BoundText>     mapNames; // new BoundText(this,"",Color.WHITE,4*deltaW,28.5*(deltaH),20*deltaW,2.5*deltaH,1 );
    private ArrayList<SelectedFrame> selectedFrames;

    private SelectedFrame[] beautifulBorders = new SelectedFrame[2];

    private DrawableList mapsAndEverything;
    private SlidingMaps slidingList;

    public MapSelectInterface(Carcass screen) {
        super(screen, BIG_INTERFACE);
        scale = 1;
        selectedFrames = new ArrayList<SelectedFrame>();
        mapBackgrounds = new ArrayList<BoundTexture>();
        maps =     new ArrayList<BoundTexture>();
        lockBacks = new ArrayList<BoundTexture>();
        locks =     new ArrayList<BoundTexture>();
        mapNames = new ArrayList<BoundText>();

        buttonAmount = 2 + BUY_BUTTON_AMOUNT /* buy buttons */;
        buttonArray = new Button[buttonAmount];
        rulesForButtons = new Runnable[buttonAmount + 1];

        movableRegionAmount = Strat7.MAP_AMOUNT + 1;
        movableRegions = new MovableRegion[movableRegionAmount];
        // movableRegionAmount = 1;

        for (int i = 0; i < 2; i++) {
            buttonArray[ i ] = new Button(this,1.25*deltaW + i * (3.5 + 0.5 + 30)*deltaW, (BIG_INTERFACE.getLocalHeight() - 12 * deltaH) / 2, 3.5 * deltaW, 12 * deltaH, null);
            buttonArray[ i ].setVisible(true);
            //buttonArray[ i ].hide();
            //buttonArray[ i ].setBlocked(true);
        }
        background = new BoundTexture(this, Strat7.graphics.getTextureByName("Interface/SmallMenu.png"), 0,0,width,height,1,Strat7.backColor);

        initSlidingMaps();
        initDrawables();
    }


    private void initSlidingMaps() {
        mapsAndEverything = new DrawableList(this,5 * deltaW, 2 * deltaH ,30 * deltaW, 28 * deltaH, 1);
        DrawableList mapAndLocks ;
        ArrayList<BoundWithFrame> bounds = new ArrayList<BoundWithFrame>();
        int mapNumber = 0;
        for(String mapName :Strat7.mapNames) {
            BoundWithFrame oneSlide = new BoundWithFrame(null,0,0,mapsAndEverything.getLocalWidth(), mapsAndEverything.getHeight(), 1);
            bounds.add(oneSlide);
            movableRegions[mapNumber] = new MovableRegion(oneSlide,1 * deltaW, 2 * deltaH,28 * deltaW, 22 * deltaH);
            movableRegions[mapNumber].getCameraMovement().setResolutions(true,true,true,true);
            selectedFrames.add(new SelectedFrame("Interface/MapBorders/Corner.png","Interface/MapBorders/Edge.png", 0, 0,0.5* deltaH));
            selectedFrames.get(mapNumber).setMainFrame(mapsAndEverything);
            selectedFrames.get(mapNumber).setNewFrame(movableRegions[mapNumber],this);

            mapBackgrounds.add(new BoundTexture(movableRegions[mapNumber], Strat7.graphics.getTextureByName("Interface/BackForText.png"),0,0,movableRegions[mapNumber].getLocalWidth(),movableRegions[mapNumber].getLocalHeight(),1, new Color(0.9f,1,1,1)));

            BoundWithFrame bound = movableRegions[mapNumber].getCameraMovement();
            maps.add(new BoundTexture(
                    bound,
                    Strat7.graphics.getTextureByName("Maps/" + mapName + "/Game_map_main.png"),
                    0, 0, 1, Color.WHITE
            ));
            movableRegions[mapNumber].setObjectParameters(maps.get(mapNumber), CameraMovement.AUTO_POSITION);
            lockBacks.add( new BoundTexture(
                    bound,
                    Strat7.graphics.getTextureByName("Interface/Circle.png"),
                    bound.getLocalWidth()/2 - 3 * deltaH / bound.getLocalScale(), bound.getLocalHeight()/2 - 3 * deltaH / bound.getLocalScale(),
                    6 * deltaH/ bound.getLocalScale(), 6 * deltaH / bound.getLocalScale(),1, Color.WHITE
            ));
            locks.add( new BoundTexture(
                    bound,
                    Strat7.graphics.getTextureByName("Icons/Lock.png"),
                    bound.getLocalWidth()/2 - 2 * deltaH / bound.getLocalScale(),bound.getLocalHeight()/2 - 2 * deltaH / bound.getLocalScale(),
                    4 * deltaH / bound.getLocalScale(), 4 * deltaH / bound.getLocalScale(),1, Color.BLACK
            ));
            movableRegions[mapNumber].setObjectParameters(maps.get(mapNumber), CameraMovement.AUTO_POSITION);
            mapNames.add(new BoundText(
                    oneSlide,mapName,Color.WHITE, 2 * deltaW, 25 * (deltaH),20*deltaW,2.5*deltaH,1
            ));

            int shift = FIRST_BUY_BUTTON;
            buttonArray[ shift + mapNumber ] = new ButtonWithCoin(this, this.getLocalWidth() / 2 - 3.5*deltaW, (3 + 4 + 2 )*deltaH , 7 * deltaW, 4 * deltaH, "Interface/Button1.png", Strat7.buttonColor, "Interface/Button2.png");
            buttonArray[ shift + mapNumber ].setText("-50");
            if(Strat7.economy.isMapAvailable(mapName)) {
                buttonArray[ shift + mapNumber ].setBlocked(true);
                buttonArray[ shift + mapNumber ].hide();
                lockBacks.get(mapNumber).hide();
                locks    .get(mapNumber).hide();
            }


            mapAndLocks = new DrawableList(movableRegions[mapNumber]);
            mapAndLocks.addDrawable(maps.get(mapNumber));
            mapAndLocks.addDrawable(lockBacks.get(mapNumber));
            mapAndLocks.addDrawable(locks.get(mapNumber));

            mapsAndEverything.addDrawable(mapBackgrounds.get(mapNumber));
            mapsAndEverything.addDrawable(mapAndLocks, true);
            mapsAndEverything.addDrawable(selectedFrames.get(mapNumber));
            mapsAndEverything.addDrawable(mapNames.get(mapNumber));
            mapNumber++;
        }
        slidingList = new SlidingMaps(mapsAndEverything,0,0,mapsAndEverything.getLocalWidth(),mapsAndEverything.getLocalHeight(), bounds,SlidingList.X_COORDINATE);
        movableRegions[MAP_SLIDE] = slidingList;



        BoundWithFrame bound = new BoundWithFrame(mapsAndEverything, deltaH, deltaH, mapsAndEverything.getLocalWidth() - 2 * deltaH, mapsAndEverything.getLocalHeight() - 2 * deltaH, 1);
        beautifulBorders[0] = new SelectedFrame("Interface/MapScreen/Corner1.png","Interface/MapScreen/Edge1.png", 0, 0,1 * deltaH);
        beautifulBorders[0].setMainFrame(mapsAndEverything);
        beautifulBorders[0].setNewFrame(bound,this);
        beautifulBorders[0].setColor(Strat7.backColor);
        beautifulBorders[1] = new SelectedFrame("Interface/MapScreen/Corner2.png","Interface/MapScreen/Edge2.png", 0, 0,1 * deltaH);
        beautifulBorders[1].setMainFrame(mapsAndEverything);
        beautifulBorders[1].setNewFrame(bound,this);
        beautifulBorders[1].setColor(Color.DARK_GRAY);

    }

    private void initDrawables() {
        addDrawable(background,false);
        addDrawable(mapsAndEverything,true);
        addDrawable(beautifulBorders[1],false);
        addDrawable(beautifulBorders[0],false);
        addDrawable(buttonArray[PREVIOUS_BUTTON],false);
        addDrawable(buttonArray[NEXT_BUTTON],false);
    }

    @Override
    public void activate() {
        for(MovableRegion movable : movableRegions) {
            movable.getCameraMovement().refresh();
        }
        currentMapNum = 0;
        if(Strat7.preferences.contains("Map")) {
            String name = Strat7.preferences.getString("Map");
            for (int i = 0; i < Strat7.mapNames.length; i++) {
                if (name.equals(Strat7.mapNames[i])) {
                    currentMapNum = i; break;
                }
            }
        }
        slidingList.setUpFieldAndCenterPos(currentMapNum);
    }

    public String getCurrentMapName() {
        return Strat7.mapNames[slidingList.getCurrentField()];
    }

    public void previousMap() {
        slidingList.moveToPrevious();
    }

    public void nextMap() {
        slidingList.moveToNext();
    }

    public void setRuleForButton(Runnable ruleForButton, int num) {
        rulesForButtons[num] = ruleForButton;
    }

    @Override
    public boolean outOfBorders(int X, int Y) {
        return (X < getPosX() || X > getPosX() + getWidth()) || (Y < getPosY() || Y > getPosY() + getHeight());
    }

    @Override
    protected boolean callBackButton() {
        if (dontCheckBorders || !Strat7.economy.isMapAvailable(getCurrentMapName())) {
            return false;
        }
        rulesForButtons[BACK_TO_PREVIOUS_SCREEN_BUTTON].run();
        return true;
    }

    @Override
    protected void commonDraw () {
        super.commonDraw();

        currentMapNum = slidingList.getCurrentField();
    }

    @Override
    public void dispose() {
        Strat7.preferences.putString("Map", Strat7.mapNames[currentMapNum]);
        Strat7.preferences.flush();
    }
}
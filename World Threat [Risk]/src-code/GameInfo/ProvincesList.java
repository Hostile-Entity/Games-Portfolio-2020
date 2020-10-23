package com.strat7.game.GameInfo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.strat7.game.Interfaces.Basics.Interface;

import  java.lang.Math;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class  ProvincesList  implements Disposable{
    protected   int provinceAmount = 0;     // CHANGEABLE if we will do several locations

    protected   int continentAmount = 0;


    private Texture  gameMap, gameMapMain;               // used
    private Pixmap pixmap, gameMapMainPixmap;
    protected   Province[] provinceArray; // OUR provinces


    private int[] continentArray;
    private int[] continentBonus;
    private boolean[] continentShow;
    private int[] circleXArray;
    private int[] circleYArray;

    private int fontDiv = 0;
    public int getFontDiv() {
        return fontDiv;
    }

    protected String defaultDirectory;
    protected String text;
    protected String[] strings;
    protected String[] words;

    private Texture provincesMap;
    private Map <Integer,  Province> provinceAccessList; // map <color, province id> - list of provinces with their ID colors

    protected  FileHandle info, positions, centers, connections, continents;

    public Texture getGameMap() {
        return gameMap;
    }
    public Texture getGameMapMain() {
        return gameMapMain;
    }

    // later i think we will make some parameters;
    public ProvincesList(String mapName, PlayersList players) {
        defaultDirectory = "Maps/" + mapName + "/";
        switch (Gdx.app.getType()) {
            case Android:
                info = Gdx.files.internal(defaultDirectory + "Map_info.txt");
                positions = Gdx.files.internal(defaultDirectory + "Positions.txt");
                centers = Gdx.files.internal(defaultDirectory + "Centers.txt");
                connections = Gdx.files.internal(defaultDirectory + "Connections.txt");
                continents = Gdx.files.internal(defaultDirectory + "Continents.txt");

                //images
                pixmap = new Pixmap(Gdx.files.internal(defaultDirectory + "touchMap.png"));
                gameMapMainPixmap = new Pixmap(Gdx.files.internal(defaultDirectory + "Game_map_main.png"));
                break;

            case Desktop:
                info = Gdx.files.local(defaultDirectory + "Map_info.txt");
                positions = Gdx.files.local(defaultDirectory + "Positions.txt");
                centers = Gdx.files.local(defaultDirectory + "Centers.txt");
                connections = Gdx.files.local(defaultDirectory + "Connections.txt");
                continents = Gdx.files.local(defaultDirectory + "Continents.txt");

                //images
                pixmap = new Pixmap(Gdx.files.local(defaultDirectory + "touchMap.png"));
                gameMapMainPixmap = new Pixmap(Gdx.files.local(defaultDirectory + "Game_map_main.png"));
                break;
        }

        provincesMap = new Texture(pixmap);
        gameMap = new Texture(defaultDirectory + "gameMap.png");
        gameMapMain = new Texture(gameMapMainPixmap);

        //info
        text = info.readString();
        strings = text.split("\\r?\\n");
        provinceAmount = Integer.parseInt(strings[0]);
        fontDiv = Integer.parseInt(strings[1]);
        provinceArray = new  Province[provinceAmount + 1];

        createProvinces();

        distributePlayersRandomly(players);

        loadCenters();
        loadAdjacent();
        loadContinents();
        distributeTroopsRandomly(players);

        provinceArray[provinceAmount] = new  Province(Interface.MAIN_FRAME,0, 0, null, null,this);

        syncProvincesWithTouchMap();
    }

    protected void createProvinces() {
        text = positions.readString();
        strings = text.split("\\r?\\n");
        for(int i = 0 ; i < provinceAmount; i ++) {
            words = strings[i].split(",");
            provinceArray[i] = new Province(Interface.MAIN_FRAME,Integer.parseInt(words[0]), Integer.parseInt(words[1]), null, defaultDirectory + "Provinces/Pr" + String.valueOf(i) + ".png",this);
            provinceArray[i].setVisible(true);
        }
    }

    public ProvincesList( String mapName) {
        defaultDirectory = "Maps/" + mapName + "/";

        switch (Gdx.app.getType()) {
            case Android:
                info = Gdx.files.internal(defaultDirectory + "Map_info.txt");
                positions = Gdx.files.internal(defaultDirectory + "Positions.txt");
                centers = Gdx.files.internal(defaultDirectory + "Centers.txt");
                connections = Gdx.files.internal(defaultDirectory + "Connections.txt");
                continents = Gdx.files.internal(defaultDirectory + "Continents.txt");

                //images
                pixmap = new Pixmap(Gdx.files.internal(defaultDirectory + "touchMap.png"));
                gameMapMainPixmap = new Pixmap(Gdx.files.internal(defaultDirectory + "Game_map_main.png"));
                break;

            case Desktop:
                info = Gdx.files.local(defaultDirectory + "Map_info.txt");
                positions = Gdx.files.local(defaultDirectory + "Positions.txt");
                centers = Gdx.files.local(defaultDirectory + "Centers.txt");
                connections = Gdx.files.local(defaultDirectory + "Connections.txt");
                continents = Gdx.files.local(defaultDirectory + "Continents.txt");

                //images
                pixmap = new Pixmap(Gdx.files.local(defaultDirectory + "touchMap.png"));
                gameMapMainPixmap = new Pixmap(Gdx.files.local(defaultDirectory + "Game_map_main.png"));
                break;
        }

        provincesMap = new Texture(pixmap);
        gameMap = new Texture(defaultDirectory + "gameMap.png");
        gameMapMain = new Texture(gameMapMainPixmap);

        //info
        text = info.readString();
        strings = text.split("\\r?\\n");
        provinceAmount = Integer.parseInt(strings[0]);
        fontDiv = Integer.parseInt(strings[1]);
        provinceArray = new  Province[provinceAmount + 1];


        text = positions.readString();
        strings = text.split("\\r?\\n");
        for(int i = 0 ; i < provinceAmount; i ++) {
            words = strings[i].split(",");
            provinceArray[i] = new Province(Interface.MAIN_FRAME,Integer.parseInt(words[0]), Integer.parseInt(words[1]), null, defaultDirectory + "Provinces/Pr" + String.valueOf(i) + ".png",this);
            provinceArray[i].setVisible(true);
        }


        loadCenters()   ;
        loadAdjacent()  ;
        loadContinents();
        provinceArray[provinceAmount] = new  Province(Interface.MAIN_FRAME,0, 0, null, null,this);


        syncProvincesWithTouchMap();

        // TODO make program to find neighbors
    }

    public ProvincesList(String mapName, int minimum) {
        defaultDirectory = "Maps/" + mapName + "/";

        gameMapMainPixmap = new Pixmap(Gdx.files.internal(defaultDirectory + "Game_map_main.png"));
        gameMapMain = new Texture(gameMapMainPixmap);
    }

    public void setPlayers (PlayersList players,boolean generate) {
        if(generate) {
            distributePlayersRandomly(players);
            distributeTroopsRandomly(players);
        }
    }

    public int[] getContinentArray() {
        return continentArray;
    }
    public int[] getContinentBonus() {
        return continentBonus;
    }
    public int[] getCircleXArray() {
        return circleXArray;
    }
    public int[] getCircleYArray() {
        return circleYArray;
    }
    public  int getContinentAmount() {
        return continentAmount;
    }
    public  int getProvinceAmount() {
        return provinceAmount;
    }
    public boolean[] getContinentShow() { return continentShow;}

    public Pixmap getPixmap() {
        return pixmap;
    }

    public Texture getProvincesMap() {
        return provincesMap;
    }

    /**
     * We get i-th province
     */
    public Province get(int num) {
        if (num > provinceAmount || num < 0)
            return null;
        return provinceArray[num];
    }

    /**
     * here we get i-th province owned by playerID
     *
     * @param playerID Player whose province we wanna get
     * @param number   province number from provinces that this player own
     * @return return global province number
     */
    public Province getProvinceById(Player playerID, int number) {
        int curNumber = 0;
        for (int j = 0; j < provinceAmount; j++)
            if (provinceArray[j].getOwnerID() == playerID) {
                if (curNumber == number)
                    return get(j);
                curNumber++;
            }
        return null;
    }

    public ArrayList< Province> getNewArrayOfBorderProvinces(Player playerID) {
        ArrayList< Province> curArray = new ArrayList< Province>();
        for ( Province province : provinceArray)
            if (province.getOwnerID() == playerID) {
                for ( Province adjProvNum : province.getAdjacentProvinces()) {
                    if (adjProvNum.getOwnerID() != playerID) {
                        curArray.add(province);
                        break;
                    }
                }
            }
        return curArray;
    }

    // get X and Y, then read Color from province map and get from Map province number
    public  Province getProvince(int X, int Y) {
        if (X < 0 || X > provincesMap.getWidth() || Y < 0 || Y > provincesMap.getHeight())
            return null;
        return provinceAccessList.get(pixmap.getPixel(X, Y));
    }

    public  Province getProvinceByColor(int Color) {
        return provinceAccessList.get(Color);
    }

    public void distributePlayersRandomly( PlayersList playerAmount) {
        if (playerAmount == null) {
            return;
        }
        playerAmount.prepareForGame();

        // i use it for generating players provinces
        int[] playersPoints = new int[provinceAmount];
        int slider;
        int playerPoint;
        int remainder = provinceAmount % playerAmount.getActivePlayersAmount();
        int normalAmount = provinceAmount - remainder;

        // array consists of 111 222 333 444 4 for example
        for (slider = 0; slider < normalAmount; slider++)
            playersPoints[slider] = slider / (normalAmount / playerAmount.getActivePlayersAmount());
        for (slider = 0; slider < remainder; slider++)
            playersPoints[normalAmount + slider] = playerAmount.getActivePlayersAmount() - (slider + 1);

        // here we get random element from it and it will be playerID   ( 5 : 111 2(2)2 333 444 4)
        // then we change last element with that what was chosen        ( 5 : 111 2(4)2 333 444 2)
        // after it we increase increments                              (here we think that array is 111 242 333 444)
        normalAmount += remainder;
        for (slider = 0; slider < normalAmount; ) {
            playerPoint = (int) (Math.random() * (normalAmount - slider));
            // here we will change x y to correct one
            provinceArray[slider].setOwnerID(playerAmount.getActivePlayer(playersPoints[playerPoint]));

            playerAmount.getActivePlayer(playersPoints[playerPoint]).incAvailableProvinces();
            slider++;
            playersPoints[playerPoint] = playersPoints[normalAmount - slider];
        }
    }
    public void distributePlayersByPlayers( PlayersList playerAmount) {}

    public void loadCenters() {
        if (!centers.exists()) {
            return;
        }
        text = centers.readString();
        strings = text.split("\\r?\\n");
        for (int i = 0; i < provinceAmount; i++) {
            words = strings[i].split(",");
            provinceArray[i].setUpCenter(Integer.parseInt(words[0]) - provinceArray[i].getLocalPosX(), Integer.parseInt(words[1]) - provinceArray[i].getLocalPosY());
        }
    }



    public void loadAdjacent() {
        if (!connections.exists()) {
            return;
        }
        text = connections.readString();
        strings = text.split("\\r?\\n");
        for (int i = 0; i < provinceAmount; i++) {
            if (strings[i].substring(0,2).equals("00")) {
                provinceArray[i].addAdjacentProvince(provinceArray[0]);
            }
            for (long j = Long.parseLong(strings[i]); j > 0; j /= 100)
                provinceArray[i].addAdjacentProvince(provinceArray[(int) (j % 100)]);
        }
    }

    public void loadContinents() {
        text = continents.readString();
        strings = text.split("\\r?\\n");
        continentAmount = Integer.parseInt(strings[0]);
        continentArray = new int[continentAmount];
        continentBonus = new int[continentAmount];
        continentShow = new boolean[continentAmount];
        circleXArray = new int[continentAmount];
        circleYArray = new int[continentAmount];
        for (int i = 0; i < continentAmount; i++) {
            continentArray[i] = Integer.parseInt(strings[i + 1]);
            continentBonus[i] = Integer.parseInt(strings[i + 1 + continentAmount]);
            continentShow[i] = false;
            words = strings[1 + i+ 2*continentAmount].split(",");
            circleXArray[i] = Integer.parseInt(words[0]);
            circleYArray[i] = Integer.parseInt(words[1]);
        }
    }

    public void syncProvincesWithTouchMap() {
        provinceAccessList = new HashMap<Integer,  Province>();
        int i = 0;
        for ( Province province : provinceArray) {
            province.setProvinceIDColor(
                    pixmap.getPixel(
                            (int)(province.getLocalCenterX() + province.getLocalPosX()),
                            provincesMap.getHeight() - (int)(province.getLocalCenterY() + province.getLocalPosY())));
            provinceAccessList.put(province.getProvinceIDColor(), province);
            province.setProvinceID(i++);
        }
        provinceAccessList.put(255, null);    // black color
        provinceAccessList.put(-1, null) ;     // white color
    }

    public ArrayList<Province> getPlayersProvinces(int playerID) {
        ArrayList<Province> provinces = new ArrayList<Province>();
        for(int i = 0; i < provinceAmount; i ++) {
            if(provinceArray[i].getOwnerID().getPID() == playerID)
                provinces.add(provinceArray[i]);
        }
        return provinces;
    }
    public ArrayList<Province> getPlayersProvinces(Player playerID) {
        return getPlayersProvinces(playerID.getPID());
    }

    public Color getContinentColor(int continent) {
        Color color = new Color(0,0,0,1);
        int provinceNumber = continentArray[continent] - 1;
        Color.rgba8888ToColor(color, gameMapMainPixmap.getPixel((int)(provinceArray[provinceNumber].getLocalCenterX() + provinceArray[provinceNumber].getLocalPosX()),provincesMap.getHeight() -  (int)(provinceArray[provinceNumber].getLocalCenterY() + provinceArray[provinceNumber].getLocalPosY())));
        return color;
    }

    public void distributeTroopsRandomly( PlayersList playerAmount) {
        if (playerAmount == null) {
            return;
        }
        int activePoints;
        int chosenProvinceNumber;

        for (int slider = 0; slider < playerAmount.getActivePlayersAmount(); slider++) {
            activePoints = 2 * playerAmount.getActivePlayer(slider).getAvailableProvinces();

            for (int i = 0; i < activePoints; i++) {
                chosenProvinceNumber = (int) (Math.random() * (activePoints / 2));
                Province province = getProvinceById(playerAmount.getActivePlayer(slider), chosenProvinceNumber);
                if(province!= null)
                    province.incNumOfTroops();
            }
        }
    }

    public int getArmy(Player playerID) {
        int sum = 0;
        for(Province province: provinceArray) {
            if(province.getOwnerID() == playerID)
                sum += province.getNumOfTroops();
        }
        return sum;
    }

    public int getBonusArmy(Player playerID) {
        int sum = 3;
        int i = 0;
        continentLoop:
        for(int slider = 0; slider < continentArray.length; slider ++) {
            for(; i < continentArray[slider]; i ++) {
                if(provinceArray[i].getOwnerID() != playerID)
                    continue continentLoop;
            }
            sum += continentBonus[slider];
        }
        return sum;
    }

    public int getPotentialArmy(Player playerID) {
        return getArmy(playerID) + getBonusArmy(playerID);
    }


    // added 3007
    public boolean belongToCapturedContinent(Province province) {

        int continentNumber = getContinentNumber(province);
        for(int i = continentNumber == 0 ? 0: continentArray[continentNumber - 1]; i < continentArray[continentNumber]; i++) {
            if(province.getOwnerID() != provinceArray[i].getOwnerID()) {
                return false;
            }
        }
        return true;
    }

    public int getContinentNumber(Province province) {
        if(continentArray == null || province.getProvinceID() < 0)
            return -1;
        for(int i = 0; i < continentArray.length; i ++) {
            if(province.getProvinceID() < continentArray[i])
                return i;
        }
        return -1;
    }
    public int getContinentNumber(int province) {
        return getContinentNumber(provinceArray[province]);
    }

    public Province getRandomProvinceWithEnemies(Player playerID) {
        ArrayList<Province> provinces = new ArrayList<Province>();

        for(Province province : provinceArray) {
            if(province.getOwnerID() == playerID && province.getEnemyTroopsAround() > 0)
                provinces.add(province);
        }

        return provinces.size() == 0 ? null : provinces.get((int) (Math.random() * provinces.size()));
    }

    @Override
    public void dispose() {
        if (provincesMap != null) provincesMap.dispose();
        if (gameMap != null) gameMap.dispose();
        if (gameMapMain != null) gameMapMain.dispose();
        if (pixmap != null) pixmap.dispose();
        if (gameMapMainPixmap != null) gameMapMainPixmap.dispose();
        for (int i = 0; i < provinceAmount; i++) {
            provinceArray[i].dispose();
        }
    }

    public void saveCenters() {
        centers.delete();
        for (int i = 0; i < provinceAmount; i++) {
            text = Integer.toString((int)provinceArray[i].getLocalCenterX()) + "," + Integer.toString((int)provinceArray[i].getLocalCenterY());
            centers.writeString(text + "\r\n", true);
        }
    }

    public void saveContinents() {
        continents.delete();
        continents.writeString(Integer.toString(continentAmount) + "\r\n",true);
        for (int i = 0; i < continentAmount; i++) {
            continents.writeString(Integer.toString(continentArray[i]) + "\r\n",true);
        }
        for (int i = 0; i < continentAmount; i++) {
            continents.writeString(Integer.toString(continentBonus[i]) + "\r\n",true);
        }
    }

    public void saveAdjacent() {
        connections.delete();

        for (int i = 0; i < provinceAmount; i++) {
            for (int j = 0; j < provinceArray[i].getAdjacentProvinces().size(); j++) {
                int provinceID = provinceArray[i].getAdjacentProvinces().get(j).getProvinceID();
                if (provinceID < 10) {
                    connections.writeString("0" + Integer.toString(provinceID), true);
                } else {
                    connections.writeString(Integer.toString(provinceID), true);
                }
            }
            connections.writeString("\r\n",true);
        }
    }

    public Province[] getProvinceArray() {
        return provinceArray;
    }

    public Player getContinentOwner(int num) {
        Player owner = null;
        for(int i = num == 0 ? 0: continentArray[num - 1]; i < continentArray[num]; i++) {
            if(owner == null)
                owner = provinceArray[i].getOwnerID();
            else {
                if (owner != provinceArray[i].getOwnerID())
                    return null;
            }
        }
        return owner;
    }
}
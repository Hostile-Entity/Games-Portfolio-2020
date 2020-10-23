package com.strat7.game.GameInfo;

import com.strat7.game.AI.AIPlayer;
import com.strat7.game.AI.AttackPair;
import com.strat7.game.Screens.Game.GameScreen;
import com.strat7.game.Strat7;

/**
 * Created by Юра on 16.08.2017.
 */

public class GameSaver {

    private static void saveProvince(String gameName, Province province, int number) {
        String path = gameName + "/Province" + Integer.toString(number);
        Strat7.preferences.putInteger(path + "playerID", province.getOwnerID().getPID());
        Strat7.preferences.putInteger(path + "provinceIDColor", province.getProvinceIDColor());
        Strat7.preferences.putInteger(path + "numOfTroops", province.getNumOfTroops());
    }
    private static void loadProvince(String gameName,PlayersList playersList, Province province, int number) {
        String path = gameName + "/Province" + Integer.toString(number);
        province.setOwnerID(playersList.getActivePlayer(Strat7.preferences.getInteger(path + "playerID")));
        province.setProvinceIDColor(Strat7.preferences.getInteger(path + "provinceIDColor"));
        province.setNumOfTroops(Strat7.preferences.getInteger(path + "numOfTroops"));
    }
    private static void removeProvinceSave(String gameName, int number){
        String path = gameName + "/Province" + Integer.toString(number);
        Strat7.preferences.remove(path + "playerID");
        Strat7.preferences.remove(path + "provinceIDColor");
        Strat7.preferences.remove(path + "numOfTroops");
    }
    private void moveProvinceDown(String gameNameTo, String gameNameFrom, int number) {
        String pathTo = gameNameTo + "/Province" + Integer.toString(number);
        String pathFrom = gameNameFrom + "/Province" + Integer.toString(number);
        Strat7.preferences.putInteger(pathTo + "playerID", Strat7.preferences.getInteger(pathFrom + "playerID"));
        Strat7.preferences.putInteger(pathTo + "provinceIDColor", Strat7.preferences.getInteger(pathFrom + "provinceIDColor"));
        Strat7.preferences.putInteger(pathTo + "numOfTroops", Strat7.preferences.getInteger(pathFrom + "numOfTroops"));
    }


    public static void saveGame(GameScreen offlineGame) {
        String gameName = offlineGame.getGameName();
        String saveNumber = null;
        if (gameName == null) {
            for (int i = 0; i < 8; i++) {
                if (!Strat7.preferences.contains("Game" + Integer.toString(i))) {
                    gameName = "Game" + Integer.toString(i);
                    break;
                }
            }
            for (int i = 0; i < 8; i++) {
                if (!Strat7.preferences.contains("Save" + Integer.toString(i))) {
                    saveNumber = "Save" + Integer.toString(i);
                    break;
                }
            }
            if (saveNumber == null) {
                saveNumber = "Save7"; //change others
                makeSaveFirst(0);
                gameName = Strat7.preferences.getString("Save7");
                deleteGameSave(Strat7.preferences.getString("Save7"));
            }
            if (gameName == null) {
                deleteGameSave(Strat7.preferences.getString(saveNumber));
                gameName = Strat7.preferences.getString(saveNumber);
            }
            Strat7.preferences.putString(saveNumber, gameName);
            Strat7.preferences.putString(gameName, gameName);
        }
        Strat7.preferences.putString(gameName + "/Map", offlineGame.getMapName());
        Strat7.preferences.putBoolean(gameName + "/Fog", offlineGame.getState().getFog());
        String gamePlayers = Strat7.json.toJson(offlineGame.getState().getPlayersIdList(), PlayersList.class);
        Strat7.preferences.putString(gameName + "/Players", gamePlayers);

        for(int i = 0; i < offlineGame.getState().getProvincesArray().getProvinceAmount(); i++) {
            saveProvince(gameName, offlineGame.getState().getProvincesArray().get(i), i);
        }
        Strat7.preferences.putInteger(gameName + "/freeTroops", offlineGame.getState().getFreeTroops());
        Strat7.preferences.putInteger(gameName + "/currentState",offlineGame.getState().getCurrentState());
        for(int i = 0; i < 10; i++) {
            if (i < offlineGame.getState().getLightenedProvinces().size()) {
                Strat7.preferences.putInteger(gameName + "/LightenedProvince" + Integer.toString(i), offlineGame.getState().getLightenedProvinces().get(i).getProvinceID());
            } else {
                Strat7.preferences.remove(gameName + "/LightenedProvince" + Integer.toString(i));
            }
        }

        if (GameScreen.getComputerPlayer().getSelectedProvinces() != null) {
            Strat7.preferences.putInteger(gameName + "/selectedProvinces" + "/Attacker", GameScreen.getComputerPlayer().getSelectedProvinces().getAttacker().getProvinceID());
            Strat7.preferences.putInteger(gameName + "/selectedProvinces" + "/Defender", GameScreen.getComputerPlayer().getSelectedProvinces().getDefender().getProvinceID());
        } else {
            Strat7.preferences.remove(gameName + "/selectedProvinces" + "/Attacker");
            Strat7.preferences.remove(gameName + "/selectedProvinces" + "/Defender");
        }

        Strat7.preferences.flush();
    }
    public static GameScreen loadGame(String gameName) {
        if (!Strat7.preferences.contains(gameName)) {
            return null;
        }
        PlayersList loadedPlayers = Strat7.json.fromJson(PlayersList.class, Strat7.preferences.getString(gameName + "/Players"));
        GameScreen offlineGame = new GameScreen(null);
        offlineGame.setMapName(Strat7.preferences.getString(gameName + "/Map"));
        offlineGame.createGameState(loadedPlayers);
        for(int i = 0; i < offlineGame.getState().getProvincesArray().getProvinceAmount(); i++) {
            loadProvince(gameName,offlineGame.getState().getPlayersIdList(), offlineGame.getState().getProvincesArray().get(i), i);
        }
        offlineGame.getState().setFog(Strat7.preferences.getBoolean(gameName + "/Fog"));
        offlineGame.getState().setFreeTroops(Strat7.preferences.getInteger(gameName + "/freeTroops"));
        offlineGame.getState().changeCurrentState(Strat7.preferences.getInteger(gameName + "/currentState"));
        for(int i = 0; i < 12; i++) {
            if (Strat7.preferences.contains(gameName + "/LightenedProvince" + Integer.toString(i))) {
                offlineGame.getState().lightProvince(offlineGame.getState().getProvincesArray().get(Strat7.preferences.getInteger(gameName + "/LightenedProvince" + Integer.toString(i))));
            } else {
                break;
            }
        }
        offlineGame.loadSheluhu();
        GameScreen.setComputerPlayer(new AIPlayer(offlineGame.getState()));
        if (Strat7.preferences.contains(gameName + "/selectedProvinces" + "/Attacker") && Strat7.preferences.contains(gameName + "/selectedProvinces" + "/Defender")) {
            GameScreen.getComputerPlayer().setSelectedProvinces(
                    new AttackPair(
                            offlineGame.getState().getProvincesArray().get(Strat7.preferences.getInteger(gameName + "/selectedProvinces" + "/Attacker")),
                            offlineGame.getState().getProvincesArray().get(Strat7.preferences.getInteger(gameName + "/selectedProvinces" + "/Defender"))
                    )
            );
        }
        offlineGame.setGameName(gameName);
        return offlineGame;
    }

    public static void deleteGameSave(String gameName) {
        if (!Strat7.preferences.contains(gameName)) {
            return;
        }
        Strat7.preferences.remove(gameName + "/Players");
        ProvincesList provincesList = new ProvincesList(Strat7.preferences.getString(gameName + "/Map"));
        for (int i = 0; i < provincesList.getProvinceAmount(); i++) {
            removeProvinceSave(gameName, i);
        }
        Strat7.preferences.remove(gameName + "/Map");
        Strat7.preferences.remove(gameName + "/freeTroops");
        Strat7.preferences.remove(gameName + "/currentState");
        Strat7.preferences.remove(gameName + "/pressedProvinceMove");
        for(int i = 0; i < 10; i++) {
            if (Strat7.preferences.contains(gameName + "/LightenedProvince" + Integer.toString(i))) {
                Strat7.preferences.remove(gameName + "/LightenedProvince" + Integer.toString(i));
            } else {
                break;
            }
        }
        Strat7.preferences.remove(gameName + "/selectedProvinces" + "/Attacker");
        Strat7.preferences.remove(gameName + "/selectedProvinces" + "/Defender");
        Strat7.preferences.remove(gameName);
        Strat7.preferences.flush();
    }
    public static void deleteFirstSave() {
        deleteGameSave(Strat7.preferences.getString("Save" + Integer.toString(savesAmount() - 1)));
        Strat7.preferences.remove("Save" + Integer.toString(savesAmount() - 1));
        Strat7.preferences.flush();
    }
    public static void moveSaveFromTo(int from, int to) {
        String saveNumberFrom = "Save" + Integer.toString(from);
        String saveNumberTo = "Save" + Integer.toString(to);
        Strat7.preferences.putString(saveNumberTo, Strat7.preferences.getString(saveNumberFrom));
        Strat7.preferences.remove(saveNumberFrom);
        Strat7.preferences.flush();
    }
    public static int savesAmount() {
        int i;
        for (i = 0; i < 8; i++) {
            if (!Strat7.preferences.contains("Save" + Integer.toString(i))) {
                break;
            }
        }
        return i;
    }
    public static void makeSaveFirst(int number) {
        int save_amount = savesAmount();
        if (number != save_amount - 1) {
            moveSaveFromTo(number, save_amount);
            for (int i = number + 1; i < save_amount; i++) {
                moveSaveFromTo(i, i - 1);
            }
            moveSaveFromTo(save_amount, save_amount - 1);
        }
    }
    public static void changeSaveName(int number, String newName) {
        Strat7.preferences.remove(Strat7.preferences.getString("Save" + Integer.toString(number)));
        Strat7.preferences.putString(Strat7.preferences.getString("Save" + Integer.toString(number)), newName);
        Strat7.preferences.flush();
    }
}

package com.strat7.game.GameInfo;

import com.badlogic.gdx.graphics.Color;
import com.strat7.game.GameInfo.Player;
import java.util.ArrayList;

/**
 * Created by Евгений on 10.06.2017.
 */

public class PlayersList {
    // MAKE another class with acceptable colors
    public static final int MAX_PLAYER_AMOUNT = 8;
    private ArrayList<Player> playersIdList;  // LIST of available player`s id
    private ArrayList<Integer> activePlayersIdList;  // LIST of active player`s id

    private int currentPlayer = 0; // number of current playing player

    public PlayersList () {
        playersIdList = new ArrayList<Player>(10);
        activePlayersIdList = new ArrayList<Integer>(10);

        playersIdList.add(new Player(new Color((float)0.70,(float)0.12,(float)0.10, 1f))); // red player
        playersIdList.add(new Player(new Color((float)0.20,(float)0.60,(float)0.17, 1f))); // green player
        playersIdList.add(new Player(new Color((float)0.02,(float)0.22,(float)0.80, 1f))); // blue player
        playersIdList.add(new Player(new Color((float)0.95,(float)0.78,(float)0 , 1f))); // yellow player
        playersIdList.add(new Player(new Color((float)0.44,(float)0.16,(float)0.39, 1f))); // purple player 34 8 43
        playersIdList.add(new Player(new Color((float)0.45,(float)0.2 ,(float)0.07, 1f))); // brown player
        playersIdList.add(new Player(new Color((float)0.03,(float)0.91,(float)0.86, 1f))); // aqua player
        playersIdList.add(new Player(new Color((float)1   ,(float)0.55,(float)0  , 1f))); // orange player
    }

    public Color getPlayerColor (int playerNum ) {
        return playersIdList.get(activePlayersIdList.get(playerNum)).getColor();
    }

    public Player getActivePlayer(int Num) {
        return playersIdList.get(activePlayersIdList.get(Num));
    }
    public Player getInactivePlayer(int Num) {
        return playersIdList.get(Num);
    }

    public int getActivePlayersAmount() {
        return activePlayersIdList.size();
    }
    public int getInactivePlayersAmount() {
        return playersIdList.size();
    }

    public Player makePlayerActive(int playerNum) {
        activePlayersIdList.add(playerNum);
        return playersIdList.get(activePlayersIdList.get(activePlayersIdList.size() - 1));
        //playersIdList.remove(playerNum);
    }
    public void makePlayerInactive(int playerNum) {
        /*
        int i = 0;
        for(; i < activePlayersIdList.size(); i ++)
            if(playersIdList.get(playerNum).getPID() == playersIdList.get(activePlayersIdList.get(i)).getPID())
                break;
        */
        //activePlayersIdList.add(i,activePlayersIdList.get(playerNum));
        activePlayersIdList.remove(playerNum);
        // shiftAllPlayersFrom(playerNum);
    }

    public Player getNextPlayer(Player player) {
        int num = playersIdList.indexOf(player);
        while (true) {
            num = (++num) % getActivePlayersAmount();
            if(playersIdList.get(num).getAvailableProvinces() != 0)
                return playersIdList.get(num);
        }
    }

    public int nextPlayerTurn (ProvincesList allProvinces) {
        for (int slider = 0; slider < activePlayersIdList.size() - 1; slider++) {
            currentPlayer = (++currentPlayer) % getActivePlayersAmount();
            if (playersIdList.get(activePlayersIdList.get(currentPlayer)).getAvailableProvinces() != 0) //Yuri changed it 12.06.17
                return currentPlayer;
        }
        currentPlayer = (++currentPlayer) % getActivePlayersAmount();
        return -1; // this mean that game was ended
    }
    public Player getCurrentPlayer() {
        return playersIdList.get(activePlayersIdList.get(currentPlayer));
    }
    public int getCurrentPlayerID() {
        return currentPlayer;
    }

    public void change2Colors(int first, int second) {
        if (first == second){
            return;
        }
        Color color = playersIdList.get(first).getColor();

        playersIdList.get(first).setColor(playersIdList.get(second).getColor());
        playersIdList.get(second).setColor(color);
    }

    public void shiftAllColorsFrom(int player){
        for (int i = player; i < MAX_PLAYER_AMOUNT - 1; i++) {
            change2Colors(i,i+1);
        }
    }

    public void shiftAllPlayersFrom(int player){
        playersIdList.add(playersIdList.get(player));
        playersIdList.remove(player);
        int  i = 0;
        for(Player player1: playersIdList) {
            player1.setPlayerID(i);
            i++;
        }
    }

    public void prepareForGame () {
        for(Player player : playersIdList) {
            player.setAvailableProvinces(0);
            player.setAvailableResources(0);
        }
    }
    public void clear() {
        activePlayersIdList.clear();
        for(Player player: playersIdList) {
            player.setArtificial(false);
            player.setArtificialType(2);
        }
    }

    public int getAIAmount() {
        int amount = 0;
        for (int i = 0; i < getActivePlayersAmount(); i++) {
            if (getActivePlayer(i).isArtificial()) {
                amount++;
            }
        }
        return amount;
    }

    public int getRealPlayingPlayersAmount() {
        int amount = 0;
        for (int i = 0; i < getActivePlayersAmount(); i++) {
            if (!getActivePlayer(i).isArtificial() && getActivePlayer(i).getAvailableProvinces() > 0) {
                amount++;
            }
        }
        return amount;
    }
    public int getPlayingPlayersAmount() {
        int amount = 0;
        for (int i = 0; i < getActivePlayersAmount(); i++) {
            if (getActivePlayer(i).getAvailableProvinces() > 0) {
                amount++;
            }
        }
        return amount;
    }

}
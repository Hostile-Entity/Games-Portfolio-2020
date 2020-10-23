package com.strat7.game.GameInfo;

import java.util.ArrayList;

/**
 * Created by Евгений on 13.08.2017.
 */

public class StatTracker {

    public static class AllPlayersStats {
        public static class OnePlayerStats {
            private int turnArmy;
            private int activeArmy;
            private Player player;
            private ArrayList<Integer> continents;

            OnePlayerStats() {
                turnArmy = 0;
                continents = new ArrayList<Integer>();
            }

            public ArrayList<Integer> getContinents() {
                return continents;
            }

            public int getTurnArmy() {
                return turnArmy;
            }
            public int addToArmy(int amount) {
                turnArmy += amount;
                return turnArmy;
            }

            public int addToActiveArmy(int amount) {
                activeArmy += amount;
                return activeArmy ;
            }
            public int getActiveArmy() {
                return activeArmy;
            }

            public void setPlayer(Player player) {
                this.player = player;
            }
            public Player getPlayer() {
                return player;
            }

            public void addContinent(int num) {
                continents.add(num);
            }


        }

        private ArrayList<OnePlayerStats> curTurnStats;
        private Player currentPlayer;

        AllPlayersStats(int amount) {
            curTurnStats = new ArrayList<OnePlayerStats>(amount);
        }

        public void setCurrentPlayer(Player currentPlayer) {
            this.currentPlayer = currentPlayer;
        }
        public Player getCurrentPlayer() {
            return currentPlayer;
        }

        void addPlayerStats(int num, OnePlayerStats stat) {
            curTurnStats.add(num, stat);
        }
        void addPlayerStats(OnePlayerStats stat) {
            curTurnStats.add(stat);
        }

        public ArrayList<OnePlayerStats> getCurTurnStats() {
            return curTurnStats;
        }

    }

    private ArrayList<AllPlayersStats> stats;
    private int cycleNum = 0;       // one cycle - all players made their turns
    private int playerNum = 0;      // player num in cycle

    public StatTracker() {
        stats = new ArrayList<AllPlayersStats>();
    }

    /**
     * Call this function when turn ends, but before player changed
     * It Accumulate info about players` turns
     * @param state - from this param i take info about turn
     */
    public void addTurnInfo(GameState state) {
        PlayersList players = state.getPlayersIdList();
        ProvincesList provinces = state.getProvincesArray();
        AllPlayersStats turnStats = new AllPlayersStats(players.getActivePlayersAmount());
        turnStats.setCurrentPlayer(players.getCurrentPlayer());

        for(int playerNum = 0; playerNum < players.getActivePlayersAmount(); playerNum ++) {
            AllPlayersStats.OnePlayerStats playerStats = new AllPlayersStats.OnePlayerStats();
            playerStats.setPlayer(players.getActivePlayer(playerNum));
            for(Province province :provinces.getProvinceArray()) {
                if(province.getOwnerID() == null || province.getOwnerID().getPID() != playerNum) {
                    continue;
                }
                playerStats.addToArmy(province.getNumOfTroops());
                if(province.getEnemyTroopsAround() != 0) {
                    playerStats.addToActiveArmy(province.getNumOfTroops() -1);
                }
            }
            for(int continentNum = 0; continentNum < provinces.getContinentAmount(); continentNum++) {
                Player owner = provinces.getContinentOwner(continentNum);
                if(owner == null || owner.getPID() != playerNum) {
                    continue;
                }
                playerStats.addContinent(continentNum);
            }
            turnStats.addPlayerStats(playerStats);
        }
        playerNum ++;
        if(playerNum >= players.getPlayingPlayersAmount()) {
            cycleNum ++;
            playerNum = 0;
        }
        stats.add(turnStats);
    }

    public void cout () {
        AllPlayersStats allPlayersStats = stats.get(stats.size() - 1);
        for(int i = 0; i < allPlayersStats.getCurTurnStats().size(); i ++) {
            System.out.println("Player " + i + ": troops " + allPlayersStats.getCurTurnStats().get(i).getTurnArmy() + "; continents " + allPlayersStats.getCurTurnStats().get(i).getContinents() + "; active troops: " + allPlayersStats.getCurTurnStats().get(i).getActiveArmy());
        }
        System.out.println();
    }

    public ArrayList<AllPlayersStats> getStats() {
        return stats;
    }
}

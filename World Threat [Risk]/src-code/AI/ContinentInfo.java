package com.strat7.game.AI;

import com.strat7.game.GameInfo.Player;
import com.strat7.game.GameInfo.PlayersList;
import com.strat7.game.GameInfo.Province;
import com.strat7.game.GameInfo.ProvincesList;

import java.util.ArrayList;

/**
 * Created by Евгений on 28.06.2017.
 */

public class ContinentInfo {
    private static int [] array ;
    public static int provinceContinentNumber (int provinceNumber) {
        if(array == null || provinceNumber < 0)
            return -1;
        for(int i = 0; i < array.length; i ++) {
            if(provinceNumber < array[i])
                return i;
        }
        return -1;
    }
    public static void setContinentNumbersArray(ProvincesList provincesList) {
        array = provincesList.getContinentArray();
    }

    private double points;
    private int continentNumber;
    private Player currentPlayer;
    private int curPlayerDifficulty;

    private int friendlyProvinces = 0;
    private int provincesAmount = 0;

    // this will be used by easy lvl
    private ArrayList<Province> inactiveProvinces;
    private ArrayList<Province> activeProvinces;
    private ArrayList<Province> enemyProvinces;

    // this will be used by middle lvl
    private ArrayList<Province> activeAdjacentProvinces;

    // this will be used by hard lvl
    private ArrayList<Province> enemyAdjacentProvinces;

    private ArrayList<Province> undeployableProvince;

    private ProvincesList provincesList;

    ContinentInfo() {
        points = 0;
        activeProvinces = new ArrayList <Province> ();
        inactiveProvinces = new ArrayList <Province> ();
        enemyProvinces = new ArrayList <Province> ();
        activeAdjacentProvinces = new ArrayList <Province> ();
        enemyAdjacentProvinces = new ArrayList <Province> ();
        undeployableProvince = new ArrayList <Province> ();
    }

    ContinentInfo(int continentNum, Player curPlayerNum, int playerDifficulty, ProvincesList provincesList) {
        points = 0;
        activeProvinces = new ArrayList <Province> ();
        inactiveProvinces = new ArrayList <Province> ();
        enemyProvinces = new ArrayList <Province> ();
        activeAdjacentProvinces = new ArrayList <Province> ();
        enemyAdjacentProvinces = new ArrayList <Province> ();
        undeployableProvince = new ArrayList <Province> ();
        this.provincesList = provincesList;
        set(continentNum, curPlayerNum, playerDifficulty, provincesList);
    }

    void set (int continentNum, Player curPlayerNum, int playerDifficulty, ProvincesList provincesList) {
        continentNumber = continentNum;
        currentPlayer = curPlayerNum;
        friendlyProvinces = 0;
        provincesAmount = 0;
        curPlayerDifficulty = playerDifficulty;
        activeProvinces.clear();
        inactiveProvinces.clear();
        enemyProvinces.clear();
        activeAdjacentProvinces.clear();
        enemyAdjacentProvinces.clear();
        undeployableProvince.clear();
        this.provincesList = provincesList;
    }

    void updateInactiveProvinces() {
        for(int i = inactiveProvinces.size() - 1; i >= 0; i --){
            if(inactiveProvinces.get(i).getNumOfTroops() == 1) {
                inactiveProvinces.remove(i);
            }
        }
    }
    void updateActiveProvinces() {
        activeCycle:
        for(int i = activeProvinces.size() - 1; i >= 0; i --){
            if(activeProvinces.get(i).getNumOfTroops() == 1) {
                activeProvinces.remove(i);
            }
            else {
                for (Province adjProvince : activeProvinces.get(i).getAdjacentProvinces()) {
                    if (adjProvince.getOwnerID() != currentPlayer)
                        continue activeCycle;
                }
                inactiveProvinces.add(activeProvinces.get(i));
                activeProvinces.remove(i);
            }
        }
    }
    void updateEnemyProvinces() {
        for(int i = enemyProvinces.size() - 1; i >= 0; i --){
            if(enemyProvinces.get(i).getOwnerID()  == currentPlayer) {
                addProvince(enemyProvinces.get(i));
                provincesAmount--;
                enemyProvinces.remove(i);
            }
        }
    }
    void updateActiveAdjacentProvinces() {
        adjCycle:
        for(int i = activeAdjacentProvinces.size() - 1; i >= 0; i --) {
            if(activeAdjacentProvinces.get(i).getNumOfTroops() != 1)
                for(Province province : activeAdjacentProvinces.get(i).getAdjacentProvinces()) {
                    if(provinceContinentNumber(province.getProvinceID()) == continentNumber)
                        if(province.getOwnerID()  != currentPlayer)
                            continue adjCycle;
                }
            activeAdjacentProvinces.remove(i);
        }
    }
    void updateEnemyAdjacentProvinces() {
        for(int i = enemyAdjacentProvinces.size() - 1; i >= 0; i --){
            if(enemyAdjacentProvinces.get(i).getOwnerID()  == currentPlayer){
                addProvince(enemyAdjacentProvinces.get(i));
                enemyAdjacentProvinces.remove(i);
            }
        }
    }
    void updateOnlyOneActiveProvince(Province yourProvince) {
        if(yourProvince.getNumOfTroops() == 1) {
            activeProvinces.remove(yourProvince);
            if (curPlayerDifficulty > 1) {
                activeAdjacentProvinces.remove(yourProvince);
            }
        }

    }

    int addProvince (Province province) {
        int activePoints = 0;
        continentCheck:
        if (continentNumber == provinceContinentNumber(province.getProvinceID())) {
            provincesAmount++;
            if(curPlayerDifficulty > 1)
                for(Province adjProvince: province.getAdjacentProvinces()) {
                    if(provinceContinentNumber(adjProvince.getProvinceID()) != continentNumber) {
                        activePoints += addProvince(adjProvince);
                    }
                }
            ownerCheck:
            if (province.getOwnerID()  == currentPlayer) {
                friendlyProvinces++;
                for (Province adjProvince : province.getAdjacentProvinces())
                    if (adjProvince.getOwnerID()  != currentPlayer) {
                        activeProvinces.add(province);
                        activePoints = province.getNumOfTroops() - 1;
                        break ownerCheck;
                    }
                inactiveProvinces.add(province);
            }
            else
                enemyProvinces.add(province);

        }
        else
            if(curPlayerDifficulty > 1)
                for(Province adjProvince : province.getAdjacentProvinces())
                    if(continentNumber == provinceContinentNumber(adjProvince.getProvinceID())) {
                        if(province.getOwnerID()  == currentPlayer) {
                            if(adjProvince.getOwnerID()  != currentPlayer) {
                                for(Province activeAdjacentProvince: activeAdjacentProvinces)
                                    if(activeAdjacentProvince.getProvinceID() == province.getProvinceID())
                                        break continentCheck;
                                activeAdjacentProvinces.add(province);
                                break continentCheck;
                            }
                        } else {
                            for(Province enemyAdjacentProvince: enemyAdjacentProvinces)
                                if(enemyAdjacentProvince.getProvinceID() == province.getProvinceID())
                                    break continentCheck;
                            enemyAdjacentProvinces.add(province);
                            break continentCheck;
                        }
                    }
        return activePoints;
    }

    private double setPoints () {
        double enemyTroops = getEnemyTroops();
        switch (curPlayerDifficulty) {
            case 1:
                points += getActiveTroops();
                points /= enemyTroops;
                break;
            case 2:
                points += getActiveTroops() + getActiveAdjacentTroops() / 2;
                points /= enemyTroops;
                break;
            case 3:
                points += getActiveTroops() + getActiveAdjacentTroops() / 2;
                inactiveLoop:
                for(Province province: inactiveProvinces)
                    for(Province adjProvince: province.getAdjacentProvinces())
                        if(activeProvinces.indexOf(adjProvince) != -1) {
                            points += (province.getNumOfTroops() - 1) / 2;
                            continue inactiveLoop;
                        }
                points /= enemyTroops;
            case 4:
        }
        return points;
    }

    double setPoints (int freeMinions) {
        if(enemyProvinces.size() == 0) return points = Double.POSITIVE_INFINITY;
        points = activeProvinces.size() == 0 ? 0 : freeMinions;
        return setPoints();
    }

    Province getProvinceToDeploy (int freePoints) {
        int enemyTroops = getEnemyTroops();
        int enemyAdjacentTroops = getEnemyAdjacentTroops();
        int activeTroops = getActiveTroops();
        ArrayList<Province> deletedProvinces = new ArrayList<Province>();
        switch (curPlayerDifficulty) {
            case 1:
                while (enemyProvinces.size() != 0) {
                    Province strongestEnemyProvince = getStrongestEnemyProvince();
                    if(strongestEnemyProvince == null)
                        return null;
                    Province strongestFriendlyProvince = null;
                    for(Province province : strongestEnemyProvince.getAdjacentProvinces()) {
                        if(province.getOwnerID()  == currentPlayer) {
                            if(strongestFriendlyProvince == null)
                                strongestFriendlyProvince = province;
                            else
                                if(strongestFriendlyProvince.getNumOfTroops() < province.getNumOfTroops())
                                    strongestFriendlyProvince = province;
                        }
                    }
                    if(strongestFriendlyProvince == null) {
                        enemyProvinces.remove(strongestEnemyProvince);
                        deletedProvinces.add(strongestEnemyProvince);
                    }
                    else {
                        for(int i = deletedProvinces.size() - 1; i >= 0; i --)
                            enemyProvinces.add(deletedProvinces.get(i));
                        return strongestFriendlyProvince;
                    }
                }
                for(int i = deletedProvinces.size() - 1; i >= 0; i --)
                    enemyProvinces.add(deletedProvinces.get(i));
                return null;


            case 2:
                if(enemyProvinces.size() == 0) {
                    if(enemyAdjacentProvinces.size() != 0) {
                        Province weakest = null;
                        double weakestPoints = 0;
                        double seconaryPoints = 0;
                        // добавить проверку на свободные войска
                        for(Province activeProvince : activeProvinces) {
                            Province strongestAdjacentProvince = activeProvince.getStrongestAdjacentEnemyProvince();
                            if(weakest == null) {
                                weakest = activeProvince;
                                weakestPoints = activeProvince.getNumOfTroops() / (
                                        strongestAdjacentProvince.getNumOfTroops() +
                                                Math.floor(Math.sqrt(provincesList.getPotentialArmy(strongestAdjacentProvince.getOwnerID() )))
                                );
                                seconaryPoints = activeProvince.getNumOfTroops() / (activeProvince.getEnemyTroopsAround() * 2 /  (double) activeProvince.getEnemyProvincesAmountAround());
                                continue;
                            }
                            double pointsBuffer = activeProvince.getNumOfTroops() / (
                                    strongestAdjacentProvince.getNumOfTroops() +
                                            Math.floor(Math.sqrt(provincesList.getPotentialArmy(strongestAdjacentProvince.getOwnerID() )))
                            );
                            double secondaryBuffer = activeProvince.getNumOfTroops() / (activeProvince.getEnemyTroopsAround() * 2 /  (double) activeProvince.getEnemyProvincesAmountAround());
                            if(Math.abs(weakestPoints - pointsBuffer) < 0.1) {
                                if(secondaryBuffer > seconaryPoints)
                                    continue;
                            }
                            else
                            if(pointsBuffer > weakestPoints) {
                                continue;
                            }
                            weakest = activeProvince;
                            weakestPoints = pointsBuffer;
                            seconaryPoints = secondaryBuffer;
                        }
                        if(freePoints < 0) {
                            return weakest;
                        }
                        if(weakest == null)
                            return null;
                        if(weakest.getNumOfTroops() == 1)
                            return weakest;
                        if(weakestPoints < 0.8)
                            return weakest;
                        else
                        if(seconaryPoints < 1)
                            return weakest;
                        return null;
                    }
                    return null;
                }
                for(Province activeProvince: activeProvinces) {
                    if(activeProvince.getEnemyProvincesAmountAround() == 1) {
                        Province enemyPr = activeProvince.getStrongestAdjacentEnemyProvince();
                        if(provinceContinentNumber(enemyPr.getProvinceID()) != continentNumber)
                            continue;
                        if(activeProvince.getNumOfTroops() < Math.ceil(enemyPr.getNumOfTroops() * 1.6) + 1)
                            return activeProvince;
                    }
                    if(activeProvince.getNumOfTroops() == 1) {
                        for(Province province: activeProvince.getAdjacentEnemyProvinces()) {
                            if(province.getNumOfTroops() * 2 > province.getPlayerTroopsAmountAround(currentPlayer))
                                return activeProvince;
                        }
                    }
                }
                while (enemyProvinces.size() != 0) {
                    Province strongestEnemyProvince = getStrongestEnemyProvince();
                    if(strongestEnemyProvince == null) {
                        for(int i = deletedProvinces.size() - 1; i >= 0; i --)
                            enemyProvinces.add(deletedProvinces.get(i));
                        return null;
                    }
                    Province strongestFriendlyProvince = strongestEnemyProvince.getStrongestPlayerAdjacentProvince(currentPlayer);
                    if(strongestEnemyProvince.getEnemyTroopsAround() - strongestEnemyProvince.getPlayerTroopsAmountAround(currentPlayer) != 0) {
                        int sum;
                        if(strongestEnemyProvince.getPlayerActiveTroopsAround(currentPlayer) < strongestEnemyProvince.getNumOfTroops() * 3) {
                            for (int i = deletedProvinces.size() - 1; i >= 0; i--)
                                enemyProvinces.add(deletedProvinces.get(i));
                            return strongestFriendlyProvince;
                        }
                    }
                    else {
                        if(strongestEnemyProvince.getPlayerActiveTroopsAround(currentPlayer)<
                                strongestEnemyProvince.getNumOfTroops() * 2 ) {
                            for (int i = deletedProvinces.size() - 1; i >= 0; i--)
                                enemyProvinces.add(deletedProvinces.get(i));
                            return strongestEnemyProvince.getStrongestAdjacentEnemyProvince();
                        }
                        enemyProvinces.remove(strongestEnemyProvince);
                        deletedProvinces.add(strongestEnemyProvince);
                        continue;
                    }
                    if(strongestFriendlyProvince != null ) {
                        if(freePoints < 0) {
                            for(int i = deletedProvinces.size() - 1; i >= 0; i --)
                                enemyProvinces.add(deletedProvinces.get(i));
                            return strongestFriendlyProvince;
                        }
                        if((activeTroops - activeProvinces.size()) / enemyTroops > 3) {
                            for(int i = deletedProvinces.size() - 1; i >= 0; i --)
                                enemyProvinces.add(deletedProvinces.get(i));
                            return null;
                        }
                        if(strongestFriendlyProvince.getNumOfTroops() - 1 + freePoints > Math.ceil(strongestEnemyProvince.getNumOfTroops() * 1.5) + 1
                                && strongestFriendlyProvince.getNumOfTroops() - 1 < Math.ceil(strongestEnemyProvince.getNumOfTroops() * 1.5) + 1) {
                            for (int i = deletedProvinces.size() - 1; i >= 0; i--)
                                enemyProvinces.add(deletedProvinces.get(i));
                            return strongestFriendlyProvince;
                        }
                    }
                    enemyProvinces.remove(strongestEnemyProvince);
                    deletedProvinces.add(strongestEnemyProvince);
                }
                for(int i = deletedProvinces.size() - 1; i >= 0; i --)
                    enemyProvinces.add(deletedProvinces.get(i));
                return null;

            // если что поменять условия на более лояльные
            // когда мы еще не захватили континент проверять на то какими войсками лучше всего захватывать
            case 3:
                if(enemyProvinces.size() == 0) {
                    if(enemyAdjacentProvinces.size() != 0) {
                        // добавить проверку на свободные войска
                        if(freePoints < 0) {
                            for(Province province : activeProvinces) {
                                if(province.getEnemyProvincesAmountAround() == 1) {
                                    return province;
                                }
                            }
                            Province strongest = null;
                            for(Province province : activeProvinces) {
                                if(strongest == null)
                                    strongest = province;
                                else
                                    if(strongest.getNumOfTroops() < province.getNumOfTroops())
                                        strongest = province;
                            }
                            return strongest;
                        }
                        if((activeTroops + Math.abs(freePoints) ) / (double)enemyAdjacentTroops >= 2.3) {
                            for(Province province : activeProvinces ) {
                                for(Province adjProvince : province.getAdjacentProvinces()) {
                                    if(adjProvince.getOwnerID() != currentPlayer &&
                                            province.getNumOfTroops() - 1 < adjProvince.getNumOfTroops() * 2)
                                        return province;
                                }
                            }
                            return null;
                        }
                        for(Province province: enemyAdjacentProvinces) {
                            for(Province adjProvince: province.getAdjacentProvinces()) {
                                if(provinceContinentNumber(adjProvince.getProvinceID()) == continentNumber &&
                                        adjProvince.getNumOfTroops() <= province.getNumOfTroops() )
                                    return adjProvince;
                            }
                        }
                        for(Province actProvince: activeProvinces) {
                            if(actProvince.getNumOfTroops() * 3 < (actProvince.getEnemyTroopsAround() - actProvince.getEnemyProvincesAmountAround()) * 2)
                                return actProvince;
                        }
                        return null;
                    }
                }
                while (enemyProvinces.size() != 0) {
                    Province strongestEnemyProvince = getStrongestEnemyProvince();
                    if(strongestEnemyProvince == null)
                        return null;
                    Province strongestFriendlyProvince = null;
                    for(Province province : strongestEnemyProvince.getAdjacentProvinces()) {
                        if(province.getOwnerID() == currentPlayer) {
                            if(freePoints < 0 || undeployableProvince.indexOf(province) == -1) {
                                if (strongestFriendlyProvince == null)
                                    strongestFriendlyProvince = province;
                                else
                                    if (strongestFriendlyProvince.getNumOfTroops() < province.getNumOfTroops())
                                        strongestFriendlyProvince = province;
                            }
                        }
                    }
                    if(strongestFriendlyProvince == null ) {
                        enemyProvinces.remove(strongestEnemyProvince);
                        deletedProvinces.add(strongestEnemyProvince);
                    }
                    else {
                        for(int i = deletedProvinces.size() - 1; i >= 0; i --)
                            enemyProvinces.add(deletedProvinces.get(i));
                        if(freePoints < 0)
                            return strongestFriendlyProvince;
                        if((activeTroops - activeProvinces.size()) / enemyTroops > 3)
                            return null;
                        if(strongestFriendlyProvince.getEnemyTroopsAround() * 2 < strongestFriendlyProvince.getNumOfTroops())
                            undeployableProvince.add(strongestFriendlyProvince);
                        return strongestFriendlyProvince;
                    }
                }
                for(int i = deletedProvinces.size() - 1; i >= 0; i --)
                    enemyProvinces.add(deletedProvinces.get(i));
                return null;


            case 4:
                break;
        }
        return null;
    }

    AttackPair getAttackPair() {
        ArrayList<Province> deletedProvinces = new ArrayList<Province>();
        switch (curPlayerDifficulty) {
            case 1:
                while (enemyProvinces.size() != 0) {
                    Province strongestEnemyProvince = getStrongestEnemyProvince();
                    if(strongestEnemyProvince == null)
                        return null;
                    Province strongestFriendlyProvince = null;
                    for(Province province : strongestEnemyProvince.getAdjacentProvinces()) {
                        if(province.getOwnerID() ==
                                currentPlayer && province.getNumOfTroops() > 1) {

                            if(strongestFriendlyProvince == null)
                                strongestFriendlyProvince = province;
                            else
                            if(strongestFriendlyProvince.getNumOfTroops() < province.getNumOfTroops())
                                strongestFriendlyProvince = province;
                        }
                    }
                    if(strongestFriendlyProvince == null) {
                        enemyProvinces.remove(strongestEnemyProvince);
                        deletedProvinces.add(strongestEnemyProvince);
                    }
                    else {
                        for(int i = deletedProvinces.size() - 1; i >= 0; i --)
                            enemyProvinces.add(deletedProvinces.get(i));
                        return new AttackPair(strongestFriendlyProvince,strongestEnemyProvince);
                    }
                }
                for(int i = deletedProvinces.size() - 1; i >= 0; i --)
                    enemyProvinces.add(deletedProvinces.get(i));
                return null;


            case 2:
                if(enemyProvinces.size() == 0) {
                    if(enemyAdjacentProvinces.size() != 0) {
                        for(Province activeProvince: activeProvinces) {
                            if(activeProvince.getEnemyProvincesAmountAround()  == 1 &&
                                    (activeProvince.getNumOfTroops() - 1) * 3 > activeProvince.getEnemyTroopsAround() * 5) {
                                return new AttackPair(activeProvince, activeProvince.getRandomAdjacentEnemyProvince());
                            }
                            if( activeProvince.getNumOfTroops() * 3 > activeProvince.getEnemyTroopsAround() * 2 )
                                if(activeProvince.getNumOfTroops() > activeProvince.getMaxTroopsAround()) {
                                    // найти у нее слабейшую рядом лежащую
                                    Province min = null;
                                    for (Province adjProvince : activeProvince.getAdjacentProvinces()) {
                                        if(adjProvince.getOwnerID() != currentPlayer)
                                            if(min == null)
                                                min = adjProvince;
                                            else {
                                                if(min.getNumOfTroops() > adjProvince.getNumOfTroops())
                                                    min = adjProvince;
                                            }
                                    }
                                    if(min == null)
                                        continue;
                                    if(activeProvince.getNumOfTroops() > 3 || min.getNumOfTroops() == 1)
                                        return new AttackPair(activeProvince, min);
                                }
                        }
                        for(Province enemyProvince: enemyAdjacentProvinces) {
                            if((enemyProvince.getPlayerTroopsAmountAround(currentPlayer)
                                    - enemyProvince.getPlayerProvincesAmountAround(currentPlayer)) >  Math.round( enemyProvince.getNumOfTroops() * 1.6) ) {
                                for(int i = 3; i > 0; i --) {
                                    if(i == 1 && enemyProvince.getNumOfTroops() == i) {
                                        int points = 0;
                                        for (Province adjProvince : enemyProvince.getAdjacentProvinces()) {
                                            if (adjProvince.getOwnerID() == currentPlayer) {
                                                if(!provincesList.belongToCapturedContinent(adjProvince)) {
                                                    if (adjProvince.getNumOfTroops() > i) {
                                                        points += adjProvince.getNumOfTroops() - 1;
                                                    }
                                                    continue;
                                                }
                                                if (adjProvince.getEnemyProvincesAmountAround() == 1 ) {
                                                    if(adjProvince.getNumOfTroops() > i)
                                                        points += adjProvince.getNumOfTroops() - 1;
                                                    continue;
                                                }
                                                if(adjProvince.getNumOfTroops() > adjProvince.getMaxTroopsAround())
                                                    points += adjProvince.getNumOfTroops() - adjProvince.getMaxTroopsAround();
                                            }
                                        }
                                        if(points <= 2)
                                            break;
                                    }
                                    for (Province adjProvince : enemyProvince.getAdjacentProvinces()) {
                                        if (adjProvince.getOwnerID() == currentPlayer) {
                                            if(!provincesList.belongToCapturedContinent(adjProvince)) {
                                                if (adjProvince.getNumOfTroops() > i) {
                                                    return new AttackPair(adjProvince, enemyProvince);
                                                }
                                                continue;
                                            }
                                            if (adjProvince.getEnemyProvincesAmountAround() == 1 ) {
                                                if(adjProvince.getNumOfTroops() > i)
                                                    return new AttackPair(adjProvince, enemyProvince);
                                                continue;
                                            }
                                            if(adjProvince.getNumOfTroops() > adjProvince.getMaxTroopsAround())
                                                return new AttackPair(adjProvince, enemyProvince);
                                        }
                                    }
                                    if(enemyProvince.getNumOfTroops() >= i)
                                        break;
                                }
                            }
                        }
                        return null;
                    }
                    return null;
                }

                // захватывая внутреннюю мы должны проверить есть ли у нее провинция в соседях которая имеет наименьшее число соседей \/
                // плюс еще надо учитывать active adj prov                  \/ (автоматически учитывается)
                // это вроде должно обыграть ситуацию когда мы захватываем провинцию и преграждаем путь другой     \/
                // плюс, если это возможно, сбиваем бонус на соседнем континенте
                // плюс продумать вариант когда лучше не нападать а подождать       \/ ( пока что тут коэффициент 1/3)

                ArrayList<Province> deletedActiveProvinces = new ArrayList<Province>();

                while (activeProvinces.size() > 0) {
                    Province activeProvince = activeProvinces.get(activeProvinces.size() - 1);
                    Province enemyProvince;
                    if(activeProvince.getEnemyProvincesAmountAround() == 1) {
                        enemyProvince = activeProvince.getStrongestAdjacentEnemyProvince();
                        if(activeProvince.getNumOfTroops() - 1 > Math.max(2,enemyProvince.getNumOfTroops()) ) {
                            for(Province deletedProvince: deletedActiveProvinces)
                                activeProvinces.add(deletedProvince);
                            return new AttackPair(activeProvince, enemyProvince);
                        }
                        deletedActiveProvinces.add(activeProvince);
                        activeProvinces.remove(activeProvinces.size() - 1);
                        continue;
                    }
                    enemyProvince = null;
                    Player strongestPlayer = getStrongestEnemy();
                    for(Province adjProvince: activeProvince.getAdjacentProvinces()) {
                        if(adjProvince.getOwnerID() != currentPlayer) {
                            if(adjProvince.getNumOfTroops() == 1 && activeProvince.getNumOfTroops() > 3) {
                                for(Province deletedProvince: deletedActiveProvinces)
                                    activeProvinces.add(deletedProvince);
                                return new AttackPair(activeProvince, adjProvince);
                            }
                            if(adjProvince.getOwnerID() == strongestPlayer) {
                                if(enemyProvince == null)
                                    enemyProvince = adjProvince;
                                else
                                    if(adjProvince.getNumOfTroops() < enemyProvince.getNumOfTroops() )
                                        enemyProvince = adjProvince;
                            }
                        }
                    }
                    if(enemyProvince != null && activeProvince.getNumOfTroops() > 3) {
                        for(Province deletedProvince: deletedActiveProvinces)
                            activeProvinces.add(deletedProvince);
                        return new AttackPair(activeProvince,enemyProvince);
                    }

                    deletedActiveProvinces.add(activeProvince);
                    activeProvinces.remove(activeProvinces.size() - 1);
                }
                for(Province deletedProvince: deletedActiveProvinces)
                    activeProvinces.add(deletedProvince);


                while (enemyProvinces.size() != 0) {
                    Province strongestEnemyProvince = getStrongestEnemyProvince();
                    if(strongestEnemyProvince == null || strongestEnemyProvince.getNumOfTroops() < getAverageEnemyTroops()) {
                        for(int i = deletedProvinces.size() - 1; i >= 0; i --)
                            enemyProvinces.add(deletedProvinces.get(i));
                        return null;
                    }
                    Province strongestFriendlyProvince = null;
                    for(Province province : strongestEnemyProvince.getAdjacentProvinces()) {
                        if(province.getOwnerID() == currentPlayer && province.getNumOfTroops() > 1) {
                            if(provinceContinentNumber(province.getProvinceID()) != continentNumber) {
                                if(provincesList.belongToCapturedContinent(province))
                                    continue;
                                if(province.getNumOfTroops() < Math.max(4,strongestEnemyProvince.getNumOfTroops()))
                                    continue;
                                for(int i = deletedProvinces.size() - 1; i >= 0; i --)
                                    enemyProvinces.add(deletedProvinces.get(i));
                                return new AttackPair(province,strongestEnemyProvince);
                            }
                            if((province.getNumOfTroops() - 1)  > strongestEnemyProvince.getNumOfTroops() &&
                                    (province.getNumOfTroops() - 1)  > province.getEnemyTroopsAround() / province.getEnemyProvincesAmountAround() ) {
                                if (strongestFriendlyProvince == null ) {
                                    strongestFriendlyProvince = province;
                                    continue;
                                }
                                if (strongestFriendlyProvince.getEnemyProvincesAmountAround() > province.getEnemyProvincesAmountAround()) {
                                    strongestFriendlyProvince = province;
                                    continue;
                                }
                                if (strongestFriendlyProvince.getEnemyProvincesAmountAround() == province.getEnemyProvincesAmountAround()) {
                                    if (strongestFriendlyProvince.getEnemyTroopsAround() > province.getEnemyTroopsAround())
                                        strongestFriendlyProvince = province;
                                    else {
                                        if (strongestFriendlyProvince.getEnemyTroopsAround() == province.getEnemyTroopsAround() &&
                                                strongestFriendlyProvince.getNumOfTroops() < province.getNumOfTroops())
                                            strongestFriendlyProvince = province;
                                    }
                                }
                            }
                        }
                    }
                    if(strongestFriendlyProvince == null || strongestFriendlyProvince.getMaxTroopsAround() > strongestFriendlyProvince.getNumOfTroops()) {
                        enemyProvinces.remove(strongestEnemyProvince);
                        deletedProvinces.add(strongestEnemyProvince);
                    }
                    else {
                        for(int i = deletedProvinces.size() - 1; i >= 0; i --)
                            enemyProvinces.add(deletedProvinces.get(i));
                        return new AttackPair(strongestFriendlyProvince,strongestEnemyProvince);
                    }
                }
                for(int i = deletedProvinces.size() - 1; i >= 0; i --)
                    enemyProvinces.add(deletedProvinces.get(i));
                if(enemyProvinces.size() == 1) {
                    if(enemyProvinces.get(0).getPlayerTroopsAmountAround(currentPlayer) -enemyProvinces.get(0).getPlayerProvincesAmountAround(currentPlayer) > enemyProvinces.get(0).getNumOfTroops() * 1.7) {
                        for(Province frProvince: enemyProvinces.get(0).getPlayerAdjProvinces(currentPlayer)) {
                            if(frProvince.getNumOfTroops() > 2 || enemyProvinces.get(0).getNumOfTroops() == 1 && frProvince.getNumOfTroops() == 2)
                                return new AttackPair(frProvince, enemyProvinces.get(0));
                        }
                    }else {
                        return null;
                    }

                }
                return null;

            case 3:
                if(enemyProvinces.size() == 0) {
                    if(enemyAdjacentProvinces.size() != 0) {
                        for(Province activeProvince: activeProvinces) {
                            if(activeProvince.getEnemyProvincesAmountAround()  == 1) {
                                Province enemyProvince = activeProvince.getStrongestAdjacentEnemyProvince();
                                if(activeProvince.getNumOfTroops() - 1 > enemyProvince.getNumOfTroops() * 2)
                                    return new AttackPair(activeProvince, activeProvince.getRandomAdjacentEnemyProvince());
                                else
                                if(activeProvince.getNumOfTroops() <= enemyProvince.getNumOfTroops() ) {
                                    if((enemyProvince.getPlayerTroopsAmountAround(currentPlayer)
                                            - enemyProvince.getPlayerProvincesAmountAround(currentPlayer)) > 2 * enemyProvince.getNumOfTroops() ) {
                                        if(activeProvince.getNumOfTroops() != 1) {
                                            return new AttackPair(activeProvince,enemyProvince);
                                        }
                                        else {
                                            return new AttackPair(enemyProvince.getRandomAdjacentFriendlyProvince(currentPlayer),enemyProvince);
                                        }
                                    }
                                }
                                else
                                    continue;
                            }
                            if(activeProvince.getNumOfTroops() * 3 > (activeProvince.getEnemyTroopsAround() - activeProvince.getEnemyProvincesAmountAround()) * 2)
                                if(activeProvince.getNumOfTroops() > activeProvince.getMaxTroopsAround()) {
                                    // найти у нее слабейшую рядом лежащую
                                    Province min = null;
                                    for (Province adjProvince : activeProvince.getAdjacentProvinces()) {
                                        if(adjProvince.getOwnerID() != currentPlayer)
                                            if(min == null)
                                                min = adjProvince;
                                            else {
                                                if(min.getNumOfTroops() > adjProvince.getNumOfTroops())
                                                    min = adjProvince;
                                            }
                                    }
                                    if(min == null)
                                        break;
                                    return new AttackPair(activeProvince, min);
                                }
                        }
                        for(Province province: enemyAdjacentProvinces) {
                            for(Province adjProvince: province.getAdjacentProvinces()) {
                                if(provinceContinentNumber(adjProvince.getProvinceID()) == continentNumber ) {
                                    if(adjProvince.getNumOfTroops() * 3 > (adjProvince.getEnemyTroopsAround() - adjProvince.getEnemyProvincesAmountAround()) * 2)
                                        if(adjProvince.getNumOfTroops() > adjProvince.getMaxTroopsAround())
                                            // найти у нее слабейшую рядом лежащую
                                            return new AttackPair(adjProvince,province);
                                }
                                else {
                                    int num = 0;
                                    if ((num = activeAdjacentProvinces.indexOf(adjProvince)) != -1 &&
                                            activeAdjacentProvinces.get(num).getNumOfTroops() < activeAdjacentProvinces.get(num).getMaxTroopsAround()) {
                                        return new AttackPair(adjProvince, province);
                                    }
                                }
                            }
                            // здесь надо вставить проверку на то можем ли мы попробовать захватить эту провинйию без ущерба в обороне других
                            // если же нет то мы пропускаем ее (сложность в этом самом "без ущерба")
                            // прописал без ущерба - значит что количество воинов в провинции не  меньше максимального числа провинций вокруг
                        }
                        return null;
                    }
                    return null;
                }

                // захватывая внутреннюю мы должны проверить есть ли у нее провинция в соседях которая имеет наименьшее число соседей \/
                // плюс еще надо учитывать active adj prov                  \/ (автоматически учитывается)
                // это вроде должно обыграть ситуацию когда мы захватываем провинцию и преграждаем путь другой     \/
                // плюс, если это возможно, сбиваем бонус на соседнем континенте
                // плюс продумать вариант когда лучше не нападать а подождать       \/ ( пока что тут коэффициент 1/3)
                while (enemyProvinces.size() != 0) {
                    Province strongestEnemyProvince = getStrongestEnemyProvince();
                    if(strongestEnemyProvince == null) {
                        for(int i = deletedProvinces.size() - 1; i >= 0; i --)
                            enemyProvinces.add(deletedProvinces.get(i));
                        return null;
                    }
                    Province strongestFriendlyProvince = null;
                    for(Province province : strongestEnemyProvince.getAdjacentProvinces()) {
                        if(province.getOwnerID() == currentPlayer && province.getNumOfTroops() > 1) {
                            if(provinceContinentNumber(province.getProvinceID()) != continentNumber) {
                                int enemiesAround = 0;
                                for(Province province1:province.getAdjacentProvinces()) {
                                    if(province1.getOwnerID() != currentPlayer &&
                                            province1 != strongestEnemyProvince)
                                        enemiesAround += province1.getNumOfTroops();
                                }
                                if(province.getNumOfTroops() < enemiesAround  ||
                                        province.getNumOfTroops() < strongestEnemyProvince.getNumOfTroops())
                                    continue;
                                for(int i = deletedProvinces.size() - 1; i >= 0; i --)
                                    enemyProvinces.add(deletedProvinces.get(i));
                                return new AttackPair(province,strongestEnemyProvince);
                            }
                            if((province.getNumOfTroops() - 1)  > strongestEnemyProvince.getNumOfTroops() &&
                                    (province.getNumOfTroops() - 1)  > province.getEnemyTroopsAround() / province.getEnemyProvincesAmountAround() ) {
                                if (strongestFriendlyProvince == null ) {
                                    strongestFriendlyProvince = province;
                                    continue;
                                }
                                if (strongestFriendlyProvince.getEnemyProvincesAmountAround() > province.getEnemyProvincesAmountAround()) {
                                    strongestFriendlyProvince = province;
                                    continue;
                                }
                                if (strongestFriendlyProvince.getEnemyProvincesAmountAround() == province.getEnemyProvincesAmountAround()) {
                                    if (strongestFriendlyProvince.getEnemyTroopsAround() > province.getEnemyTroopsAround())
                                        strongestFriendlyProvince = province;
                                    else {
                                        if (strongestFriendlyProvince.getEnemyTroopsAround() == province.getEnemyTroopsAround() &&
                                                strongestFriendlyProvince.getNumOfTroops() < province.getNumOfTroops())
                                            strongestFriendlyProvince = province;
                                    }
                                }
                            }
                        }
                    }
                    if(strongestFriendlyProvince == null || strongestFriendlyProvince.getMaxTroopsAround() > strongestFriendlyProvince.getNumOfTroops()) {
                        enemyProvinces.remove(strongestEnemyProvince);
                        deletedProvinces.add(strongestEnemyProvince);
                    }
                    else {
                        for(int i = deletedProvinces.size() - 1; i >= 0; i --)
                            enemyProvinces.add(deletedProvinces.get(i));
                        return new AttackPair(strongestFriendlyProvince,strongestEnemyProvince);
                    }
                }
                for(int i = deletedProvinces.size() - 1; i >= 0; i --)
                    enemyProvinces.add(deletedProvinces.get(i));
                return null;

            case 4:

                break;
        }
        return null;
    }

    public double getPoints() {
        return points;
    }

    public int getFriendlyProvinces() {
        return friendlyProvinces;
    }

    public int getProvincesAmount() {
        return provincesAmount;
    }
    public Province getStrongestEnemyProvince() {
        if(enemyProvinces.size() == 0)
            return null;
        Province strongestEnemyProvince = enemyProvinces.get(0);

        for (int i = enemyProvinces.size() - 1; i > 0; i--) {
            if (strongestEnemyProvince.getNumOfTroops() < enemyProvinces.get(i).getNumOfTroops()) {
                strongestEnemyProvince = enemyProvinces.get(i);
            }
        }
        return strongestEnemyProvince;
    }
    public Province getStrongestEnemyAdjacentProvince() {
        if(enemyAdjacentProvinces.size() == 0)
            return null;
        Province strongestEnemyProvince = enemyAdjacentProvinces.get(0);

        for (int i = enemyAdjacentProvinces.size() - 1; i > 0; i--) {
            if (strongestEnemyProvince.getNumOfTroops() < enemyAdjacentProvinces.get(i).getNumOfTroops()) {
                strongestEnemyProvince = enemyAdjacentProvinces.get(i);
            }
        }
        return strongestEnemyProvince;
    }
    public Province getStrongestInactiveProvince() {
        if(inactiveProvinces.size() == 0)
            return null;
        Province strongestInactiveProvince = inactiveProvinces.get(0);

        for (int i = inactiveProvinces.size() - 1; i > 0; i--) {
            if (strongestInactiveProvince.getNumOfTroops() < inactiveProvinces.get(i).getNumOfTroops()) {
                strongestInactiveProvince = inactiveProvinces.get(i);
            }
        }
        return strongestInactiveProvince;
    }

    public ArrayList<Province> getEnemyProvinces() {
        return enemyProvinces;
    }

    Player getOwnerID() {
        if(enemyProvinces.size() == 0)
            return currentPlayer;
        if(friendlyProvinces == 0) {
            Player firstOwner = enemyProvinces.get(0).getOwnerID();
            for(Province province: enemyProvinces) {
                if(province.getOwnerID() != firstOwner)
                    return null;
            }
            return firstOwner;
        }
        return null;
    }

    public int getContinentNumber() {
        return continentNumber;
    }

    public ArrayList<Province> getInactiveProvinces() {
        return inactiveProvinces;
    }

    public int getActiveAdjacentTroops () {
        int activeAdjacentTroops = 0;
        for(int i = activeAdjacentProvinces.size() - 1; i >= 0; i-- ) {
            activeAdjacentTroops += activeAdjacentProvinces.get(i).getNumOfTroops() ;
        }
        return activeAdjacentTroops ;
    }
    public int getActiveTroops () {
        int activeTroops = 0;
        for(int i = activeProvinces.size() - 1; i >= 0; i-- ) {
            activeTroops += activeProvinces.get(i).getNumOfTroops() ;
        }
        return activeTroops ;
    }
    public int getEnemyTroops () {
        int enemyTroops = 0;
        for(int i = enemyProvinces.size() - 1; i >= 0; i-- ) {
            enemyTroops += enemyProvinces.get(i).getNumOfTroops() ;
        }
        return enemyTroops;
    }
    public int getEnemyAdjacentTroops () {
        int enemyAdjacentTroops = 0;
        for(int i = enemyAdjacentProvinces.size() - 1; i >= 0; i-- ) {
            enemyAdjacentTroops += enemyAdjacentProvinces.get(i).getNumOfTroops() ;
        }
        return enemyAdjacentTroops;
    }

    public Player getStrongestEnemy() {
        ArrayList<Integer> armies = new ArrayList<Integer>();
        for(int i = 0; i < PlayersList.MAX_PLAYER_AMOUNT; i++) {
            armies.add(0);
        }
        Player player = null;
        int  points = 0;
        for(Province enemyProvince: enemyProvinces) {
            points = armies.get(enemyProvince.getOwnerID().getPID());
            armies.remove(enemyProvince.getOwnerID().getPID());
            armies.add(enemyProvince.getOwnerID().getPID(), points + enemyProvince.getNumOfTroops());
            if(player == null)
                player = enemyProvince.getOwnerID();
            else  {
                if(armies.get(player.getPID()) < armies.get(enemyProvince.getOwnerID().getPID()))
                    player = enemyProvince.getOwnerID();
            }
        }
        return player;
    }

    public int getAverageEnemyTroops() {
        int sum = 0;
        for(Province province: enemyProvinces)
            sum += province.getNumOfTroops();

        return sum / enemyProvinces.size();
    }
}

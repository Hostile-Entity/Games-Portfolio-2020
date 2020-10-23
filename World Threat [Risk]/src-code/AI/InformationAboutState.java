package com.strat7.game.AI;

import com.strat7.game.GameInfo.GameState;
import com.strat7.game.GameInfo.Player;
import com.strat7.game.GameInfo.PlayersList;
import com.strat7.game.GameInfo.Province;
import com.strat7.game.GameInfo.ProvincesList;

import java.util.ArrayList;

class InformationAboutState {

    // можно сделать ожерельем : двойной массив в каждой строке будет провинция из которой мы сможем
    // атаковать (она будет стоять на нулевом) а в строках будут те провинции на которые мы сможем
    // напасть
    // этот способ быстр по времени доступа , но долго придется это ожерелье обновлять
    // плюс вроде как по памяти медленнее
    private PlayersList playersList;
    private ProvincesList provincesList;
    // i call province active if it lie on the border and could be used by AI on the current step
    private ArrayList<Province> activeProvinces;

    private ArrayList<ContinentInfo> continentArray;
    private WayToEnemyContinent wayToEnemyContinent;
    private boolean alreadyFoundWay = false;
    private Player strongestPlayer;
    private int strongestArmy;


    private int availableTroops = 0;
    private int curPlayerDifficulty;
    private boolean needForceDeploy = false;
    private boolean tryingToBreakBonus = true;

    InformationAboutState(GameState state) {
        wayToEnemyContinent = null;
        continentArray = new ArrayList<ContinentInfo>();
        ContinentInfo.setContinentNumbersArray(state.getProvincesArray());
        for(int i = 0; i < state.getProvincesArray().getContinentAmount(); i ++)
            continentArray.add(new ContinentInfo());
        set(state);
    }

    void set (GameState state) {
        availableTroops = 0;
        alreadyFoundWay = false;
        needForceDeploy = false;
        tryingToBreakBonus = true;
        wayToEnemyContinent = null;
        playersList = state.getPlayersIdList();
        provincesList = state.getProvincesArray();
        curPlayerDifficulty = playersList.getCurrentPlayer().getArtificialType();
        setEnemyStrongestPlayer();
        for(int i = 0; i < continentArray.size(); i ++)
            continentArray.get(i).set(i,playersList.getCurrentPlayer(), curPlayerDifficulty, provincesList);
        switch (curPlayerDifficulty) {
            case 0:
                activeProvinces = state.getProvincesArray().getNewArrayOfBorderProvinces(playersList.getCurrentPlayer());
                for(Province province : activeProvinces)
                    availableTroops += province.getNumOfTroops();
                availableTroops += playersList.getCurrentPlayer().getAvailableResources() - activeProvinces.size();
                availableTroops = (2 * availableTroops / 3);
                return;
            case 1:
            case 2:
            case 3:
            case 4:
                int i ;
                availableTroops += playersList.getCurrentPlayer().getAvailableResources();

                for(i=0; ContinentInfo.provinceContinentNumber(i) >= 0;i++) { //Европа
                    availableTroops += continentArray.get(ContinentInfo.provinceContinentNumber(i)).addProvince(provincesList.get(i));
                }
                for(ContinentInfo continentInfo : continentArray)
                    continentInfo.setPoints(playersList.getCurrentPlayer().getAvailableResources());
                if(curPlayerDifficulty == 1)
                    availableTroops = availableTroops / 2;
                else
                    availableTroops = 100000000;
                break;
            default:
                break;
        }

    }

    void updateActiveState() {
        switch (curPlayerDifficulty) {
            case 0:
                int provNum = activeProvinces.size() - 1;
                cycle1:
                for(;provNum >= 0 ; provNum -- ) {
                    if(activeProvinces.get(provNum).getNumOfTroops() == 1)
                        activeProvinces.remove(provNum);
                    else {
                        for(Province province: activeProvinces.get(provNum).getAdjacentProvinces())
                            if(province.getOwnerID() != playersList.getCurrentPlayer())
                                continue cycle1;
                        activeProvinces.remove(provNum);
                    }
                }
                break;

            case 1:
            case 2:
            case 3:
                for(ContinentInfo continentInfo: continentArray) {
                    continentInfo.updateEnemyAdjacentProvinces();
                    continentInfo.updateEnemyProvinces();
                    continentInfo.updateActiveAdjacentProvinces();
                    continentInfo.updateActiveProvinces();
                    continentInfo.updateInactiveProvinces();
                    continentInfo.setPoints(0);
                }
        }
    }

    // remake
    void updateActiveState(Province enemyProvince) {
        ArrayList<Integer> array = new ArrayList<Integer>();
        array.add(ContinentInfo.provinceContinentNumber(enemyProvince.getProvinceID()));
        adjCycle:
        for(Province province : enemyProvince.getAdjacentProvinces()) {
            for(Integer integer : array) {
                if(integer.equals(ContinentInfo.provinceContinentNumber(province.getProvinceID())))
                    continue adjCycle;
            }
            array.add(ContinentInfo.provinceContinentNumber(province.getProvinceID()));
        }
        switch (curPlayerDifficulty) {
            case 0:
                if(enemyProvince.getOwnerID() == playersList.getCurrentPlayer())
                    activeProvinces.add(enemyProvince);
                updateActiveState();
                return;

            case 1:
            case 2:
            case 3:
                for (Integer i: array) {
                    continentArray.get(i).updateEnemyAdjacentProvinces();
                    continentArray.get(i).updateEnemyProvinces();
                    continentArray.get(i).updateActiveAdjacentProvinces();
                    continentArray.get(i).updateActiveProvinces();
                    continentArray.get(i).updateInactiveProvinces();
                    continentArray.get(i).setPoints(0);
                }
                break;
        }
    }

    void updateOnlyOneActiveProvince(Province yourProvince) {
        switch (curPlayerDifficulty) {
            case 0:
                if(yourProvince.getNumOfTroops() == 1)
                    activeProvinces.remove(yourProvince);
                break;

            case 1:
            case 2:
            case 3:
                for (ContinentInfo continentInfo : continentArray) {
                    continentInfo.updateOnlyOneActiveProvince(yourProvince);
                    continentInfo.setPoints(0);
                }
                break;
        }
    }

    void updatePoints () {
        for(ContinentInfo continentInfo: continentArray) {
            continentInfo.setPoints(playersList.getCurrentPlayer().getAvailableResources());
        }
    }

    // добавить случай когда во всех провинциях все хорошо
    Province getProvinceToDeploy() {
        switch (curPlayerDifficulty) {
            case 0:
                return getRandomActiveProvince();

            case 3:
            case 2:
                do {
                    if (alreadyFoundWay) {
                        if(wayToEnemyContinent == null)
                            break;
                        if(wayToEnemyContinent.needTroopsForPractical() != 0) {
                            return wayToEnemyContinent.getDeployProvince();
                        }
                        else{
                            if(tryingToBreakBonus) {
                                ArrayList<ContinentInfo> deletedContinents = new ArrayList<ContinentInfo>();
                                do {
                                    int continentNum = ContinentInfo.provinceContinentNumber(wayToEnemyContinent.getTarget().getProvinceID());
                                    ContinentInfo cont = null;
                                    for(ContinentInfo continent: continentArray)
                                        if(continent.getContinentNumber() == continentNum) {
                                            cont = continent;
                                            break;
                                        }
                                    if(cont != null) {
                                        deletedContinents.add(cont);
                                        continentArray.remove(cont);
                                    }
                                    wayToEnemyContinent = getWay(playersList.getCurrentPlayer().getAvailableResources());
                                    if(wayToEnemyContinent == null) {
                                        tryingToBreakBonus = false;
                                        insertDeletedContinents(deletedContinents);
                                        break;
                                    }
                                    if(wayToEnemyContinent.isPractical(0))
                                        continue;
                                    if(wayToEnemyContinent.needTroopsForPractical() < playersList.getCurrentPlayer().getAvailableResources() - 3) {
                                        insertDeletedContinents(deletedContinents);
                                        break;
                                    }
                                } while (true);
                            }
                        }
                    } else {
                        wayToEnemyContinent = getWay(playersList.getCurrentPlayer().getAvailableResources());
                        alreadyFoundWay = true;
                    }
                } while (true);
            case 1:

                ContinentInfo maxContinent;
                ArrayList<ContinentInfo> deletedContinents = new ArrayList<ContinentInfo>();
                while ( true ) {
                    maxContinent = getMaxValuebleContinent();
                    if (maxContinent == null)
                        needForceDeploy = true;
                    if(needForceDeploy)  {
                        Province province = null;
                        for(int i = 0; i < provincesList.getProvinceAmount(); i ++) {
                            if(provincesList.get(i).getOwnerID() == playersList.getCurrentPlayer() && provincesList.get(i).getEnemyTroopsAround() != 0) {
                                province = provincesList.get(i);
                                break;
                            }
                        }
                        insertDeletedContinents(deletedContinents);
                        for(ContinentInfo continentInfo: continentArray) {
                            Province province1;
                            if((province1 = continentInfo.getProvinceToDeploy(-playersList.getCurrentPlayer().getAvailableResources())) != null)
                                return province1;
                        }
                        return province;
                    }
                    else {
                        Province province = maxContinent.getProvinceToDeploy(playersList.getCurrentPlayer().getAvailableResources());
                        if(province != null) {
                            insertDeletedContinents(deletedContinents);
                            return province;
                        }
                        else {
                            deletedContinents.add(maxContinent);
                            continentArray.remove(maxContinent);
                        }
                    }
                }
        }
        return null;
    }

    AttackPair getAttackPair() {
        switch (curPlayerDifficulty) {
            case 0:
                if(availableTroops != 0) {
                    Province province = getRandomActiveProvince();
                    if(province == null) {
                        setAvailableTroops(0);
                        return null;
                    }
                    decAvailableTroops();
                    return new AttackPair(province, province.getRandomAdjacentEnemyProvince());
                }
                else {
                    setAvailableTroops(0);
                    return null;
                }

            case 2:
            case 3:

                do {
                    if (!alreadyFoundWay) {
                        wayToEnemyContinent = getWay(0);
                    }
                    if (wayToEnemyContinent != null) {
                        alreadyFoundWay = true;
                        if (!wayToEnemyContinent.isPassed()) {
                            AttackPair attack = wayToEnemyContinent.getAttackPair();
                            if (attack == null) {
                                wayToEnemyContinent = new WayToEnemyContinent(
                                        continentArray.get(ContinentInfo.provinceContinentNumber(wayToEnemyContinent.getTarget().getProvinceID())),
                                        provincesList,
                                        curPlayerDifficulty,
                                        playersList.getCurrentPlayer(),
                                        0
                                );
                                if(wayToEnemyContinent.isPractical(0)) {
                                    continue;
                                }
                                wayToEnemyContinent = null;
                                setEnemyStrongestPlayer();
                                alreadyFoundWay = false;
                                continue;
                            }
                            return attack;
                        } else {
                            setEnemyStrongestPlayer();
                            wayToEnemyContinent = null;
                            alreadyFoundWay = false;
                        }
                    } else {
                        break;
                    }
                } while (true);
            case 1:
                // добавить сюда препятствие в получении бонуса вражеского континента
                // уху, это будет весело
                ArrayList<ContinentInfo> deletedContinents = new ArrayList<ContinentInfo>();
                while ( continentArray.size() > 0) {
                    ContinentInfo maxContinent = getMaxValuebleContinent();
                    if (maxContinent == null) {
                        insertDeletedContinents(deletedContinents);
                        setAvailableTroops(0);
                        return null;
                    }
                    else {
                        AttackPair attackPair = maxContinent.getAttackPair();
                        if( attackPair != null ) {
                            insertDeletedContinents(deletedContinents);
                            decAvailableTroops();
                            return attackPair;
                        }
                        else {
                            deletedContinents.add(maxContinent);
                            continentArray.remove(maxContinent);
                        }
                    }
                }
                insertDeletedContinents(deletedContinents);
                setAvailableTroops(0);
                return null;
        }
        return null;
    }

    // написать функцию по распределению в последнем этапе, которая будет распределять очки из внутренних
    // провинций к активным или если таковых нет рядом к граничным у континентов
    AttackPair transport () {
        switch(curPlayerDifficulty) {
            case 1:
            case 2:
            case 3:
                do {
                    Province strongestInactiveProvince = null;
                    for (ContinentInfo continentInfo : continentArray) {
                        Province contProvince = continentInfo.getStrongestInactiveProvince();
                        if (strongestInactiveProvince == null && contProvince != null)
                            strongestInactiveProvince = contProvince;
                        else {
                            if (contProvince != null && strongestInactiveProvince.getNumOfTroops() < contProvince.getNumOfTroops())
                                strongestInactiveProvince = contProvince;
                        }
                    }
                    if (strongestInactiveProvince == null)
                        return null;
                    ArrayList<Province> surroundingActiveProvinces = new ArrayList<Province>();
                    ArrayList<Province> surroundingInactiveProvinces = new ArrayList<Province>();
                    Province maxSurroundingBorderProvince = null;
                    int maxSurroundingTroops = 0;
                    for (Province province : strongestInactiveProvince.getAdjacentProvinces()) {
                        if (province.getEnemyTroopsAround() != 0)
                            surroundingActiveProvinces.add(province);
                        else
                            surroundingInactiveProvinces.add(province);
                    }
                    if (surroundingActiveProvinces.size() != 0) {
                        Province province = surroundingActiveProvinces.get(0);
                        for (Province actProvince :surroundingActiveProvinces) {
                            if(isLyingOnTheBorder(province) == isLyingOnTheBorder(actProvince) || curPlayerDifficulty != 3) {
                                if ((province.getEnemyTroopsAround() / (double) province.getNumOfTroops()) <
                                        (actProvince.getEnemyTroopsAround() / (double) actProvince.getNumOfTroops()))
                                    province = actProvince;
                            }
                            else {
                                if(isLyingOnTheBorder(actProvince))
                                    province = actProvince;
                            }
                        }
                        return new AttackPair(strongestInactiveProvince, province);
                    }
                    for (Province province : strongestInactiveProvince.getAdjacentProvinces()) {
                        boolean borderProvince = false;
                        boolean gotActiveProvince = false;
                        int surroundingTroops = 0;
                        for (Province adjProvince : province.getAdjacentProvinces()) {
                            if (adjProvince.getEnemyTroopsAround() != 0) {
                                surroundingTroops += adjProvince.getEnemyTroopsAround();
                                gotActiveProvince = true;
                            }
                            if (!borderProvince &&
                                    ContinentInfo.provinceContinentNumber(adjProvince.getProvinceID()) !=
                                            ContinentInfo.provinceContinentNumber(province.getProvinceID())) {
                                borderProvince = true;
                            }
                        }
                        if (gotActiveProvince && borderProvince) {
                            if (maxSurroundingTroops < surroundingTroops) {
                                maxSurroundingBorderProvince = province;
                                maxSurroundingTroops = surroundingTroops;
                            }
                        }
                    }
                    if (maxSurroundingBorderProvince != null) {
                        return new AttackPair(strongestInactiveProvince, maxSurroundingBorderProvince);
                    }
                    if (surroundingInactiveProvinces.size() != 0) {
                        for (Province province : surroundingInactiveProvinces) {
                            int surroundingTroops = 0;
                            for (Province adjProvince : province.getAdjacentProvinces()) {
                                surroundingTroops += adjProvince.getEnemyTroopsAround();
                            }
                            if (maxSurroundingTroops < surroundingTroops) {
                                maxSurroundingBorderProvince = province;
                                maxSurroundingTroops = surroundingTroops;
                            }
                        }
                    }
                    if (maxSurroundingBorderProvince != null) {
                        return new AttackPair(strongestInactiveProvince, maxSurroundingBorderProvince);
                    }
                    continentArray.get(
                            ContinentInfo.provinceContinentNumber(
                                    strongestInactiveProvince.getProvinceID()
                            )
                    ).getInactiveProvinces().remove(strongestInactiveProvince);
                } while (true);

            case 4:
        }
        return null;
    }

    // распределять по колличеству вражеских провинций в округе
    // а в более поздних по принципу, нужны ли тем провинциям войска для захвата
    int distribution (Province defender, Province attacker, int freeTroops) {
        double attackerNewSum = 0;
        double defenderNewSum = 0;
        int surroundingDefenderProvinceEnemyTroops = defender.getEnemyTroopsAround();
        int surroundingAttackerProvinceEnemyTroops = attacker.getEnemyTroopsAround();
        switch (curPlayerDifficulty) {
            case 0:
                return (int)((freeTroops + 1) * Math.random());
            case 1:
                if(surroundingAttackerProvinceEnemyTroops == 0)
                    return 0;
                if(surroundingDefenderProvinceEnemyTroops == 0)
                    return freeTroops;
                return (int)(surroundingAttackerProvinceEnemyTroops /
                        (double)(surroundingAttackerProvinceEnemyTroops + surroundingDefenderProvinceEnemyTroops) *  freeTroops) ;

            case 2:
                if(surroundingAttackerProvinceEnemyTroops == 0) {
                    if(surroundingDefenderProvinceEnemyTroops != 0)
                        return 0;
                    int surroundingAttackerActiveProvinces = 0;
                    int surroundingDefenderActiveProvinces = 0;
                    boolean isAttackerBordered = false;
                    boolean isDefenderBordered = false;
                    attackerCycle:
                    for(Province province: attacker.getAdjacentProvinces()) {
                        if(!isAttackerBordered && ContinentInfo.provinceContinentNumber(province.getProvinceID()) !=
                                ContinentInfo.provinceContinentNumber(attacker.getProvinceID()))
                            isAttackerBordered = true;
                        for(Province adjProvince: province.getAdjacentProvinces())
                            if(adjProvince.getOwnerID() != province.getOwnerID()) {
                                surroundingAttackerActiveProvinces++;
                                continue attackerCycle;
                            }
                    }
                    defenderCycle:
                    for(Province province: defender.getAdjacentProvinces()) {
                        if(!isDefenderBordered && ContinentInfo.provinceContinentNumber(province.getProvinceID()) !=
                                ContinentInfo.provinceContinentNumber(defender.getProvinceID()))
                            isDefenderBordered = true;
                        for(Province adjProvince: province.getAdjacentProvinces())
                            if(adjProvince.getOwnerID() != province.getOwnerID()) {
                                surroundingDefenderActiveProvinces ++;
                                continue defenderCycle;
                            }
                    }
                    if(surroundingAttackerActiveProvinces == 0)
                        if(surroundingDefenderActiveProvinces != 0)
                            return 0;
                    if(surroundingDefenderActiveProvinces == 0)
                        if(surroundingAttackerActiveProvinces != 0)
                            return freeTroops;
                    if(isAttackerBordered && !isDefenderBordered)
                        return freeTroops;
                    if(!isAttackerBordered && isDefenderBordered)
                        return 0;
                    boolean isAttackerContinentCaptured = playersList.getCurrentPlayer() ==
                            continentArray.get(ContinentInfo.provinceContinentNumber(attacker.getProvinceID())).getOwnerID();
                    boolean isDefenderContinentCaptured = playersList.getCurrentPlayer() ==
                            continentArray.get(ContinentInfo.provinceContinentNumber(defender.getProvinceID())).getOwnerID();
                    if(isAttackerContinentCaptured && !isDefenderContinentCaptured)
                        return 0;
                    if(!isAttackerContinentCaptured && isDefenderContinentCaptured)
                        return freeTroops;

                    if(surroundingAttackerActiveProvinces > surroundingDefenderActiveProvinces)
                        return freeTroops;
                    if(surroundingAttackerActiveProvinces < surroundingDefenderActiveProvinces)
                        return 0;
                    return freeTroops / 2;
                }
                if(surroundingDefenderProvinceEnemyTroops == 0)
                    return freeTroops;
                for(Province province: attacker.getAdjacentProvinces()) {
                    double sum = 0;
                    if(province.getOwnerID() != playersList.getCurrentPlayer()) {
                        for (Province enemyAdjProvince:province.getAdjacentProvinces() ) {
                            if(enemyAdjProvince.getOwnerID() == attacker.getOwnerID())
                                sum += enemyAdjProvince.getNumOfTroops() - 1;
                        }
                        attackerNewSum += province.getNumOfTroops() / sum;
                    }
                }
                for(Province province: defender.getAdjacentProvinces()) {
                    double sum = 0;
                    if(province.getOwnerID() != playersList.getCurrentPlayer()) {
                        for (Province enemyAdjProvince:province.getAdjacentProvinces() ) {
                            if(enemyAdjProvince.getOwnerID() == defender.getOwnerID())
                                sum += enemyAdjProvince.getNumOfTroops() - 1;
                        }
                        defenderNewSum += province.getNumOfTroops() / sum;
                    }
                }
                if (Double.valueOf(attackerNewSum).isInfinite() && Double.valueOf(defenderNewSum).isInfinite())
                    return (int)(surroundingAttackerProvinceEnemyTroops /
                            (double)(surroundingAttackerProvinceEnemyTroops + surroundingDefenderProvinceEnemyTroops) *  freeTroops) ;
                if (Double.valueOf(attackerNewSum).isInfinite())
                    return freeTroops;
                if (Double.valueOf(defenderNewSum).isInfinite())
                    return 0;
                return (int)(attackerNewSum / (attackerNewSum + defenderNewSum) * freeTroops);


            case 3:
                if( alreadyFoundWay && wayToEnemyContinent != null )
                    return wayToEnemyContinent.getDistribution(attacker,defender,freeTroops);
                if(surroundingAttackerProvinceEnemyTroops == 0) {
                    if(surroundingDefenderProvinceEnemyTroops != 0)
                        return 0;
                    int surroundingAttackerActiveProvinces = 0;
                    int surroundingDefenderActiveProvinces = 0;
                    boolean isAttackerBordered = false;
                    boolean isDefenderBordered = false;
                    attackerCycle:
                    for(Province province: attacker.getAdjacentProvinces()) {
                        if(!isAttackerBordered && ContinentInfo.provinceContinentNumber(province.getProvinceID()) !=
                                ContinentInfo.provinceContinentNumber(attacker.getProvinceID()))
                            isAttackerBordered = true;
                        for(Province adjProvince: province.getAdjacentProvinces())
                            if(adjProvince.getOwnerID() != province.getOwnerID()) {
                                surroundingAttackerActiveProvinces++;
                                continue attackerCycle;
                            }
                    }
                    defenderCycle:
                    for(Province province: defender.getAdjacentProvinces()) {
                        if(!isDefenderBordered && ContinentInfo.provinceContinentNumber(province.getProvinceID()) !=
                                ContinentInfo.provinceContinentNumber(defender.getProvinceID()))
                            isDefenderBordered = true;
                        for(Province adjProvince: province.getAdjacentProvinces())
                            if(adjProvince.getOwnerID() != province.getOwnerID()) {
                                surroundingDefenderActiveProvinces ++;
                                continue defenderCycle;
                        }
                    }
                    if(surroundingAttackerActiveProvinces == 0)
                        if(surroundingDefenderActiveProvinces != 0)
                            return 0;
                    if( surroundingDefenderActiveProvinces == 0 )
                        if(surroundingAttackerActiveProvinces != 0)
                            return freeTroops;
                    if(isAttackerBordered && !isDefenderBordered)
                        return freeTroops;
                    if(!isAttackerBordered && isDefenderBordered)
                        return 0;
                    boolean isAttackerContinentCaptured = playersList.getCurrentPlayer() ==
                            continentArray.get(ContinentInfo.provinceContinentNumber(attacker.getProvinceID())).getOwnerID();
                    boolean isDefenderContinentCaptured = playersList.getCurrentPlayer() ==
                            continentArray.get(ContinentInfo.provinceContinentNumber(defender.getProvinceID())).getOwnerID();
                    if(isAttackerContinentCaptured && !isDefenderContinentCaptured)
                        return 0;
                    if(!isAttackerContinentCaptured && isDefenderContinentCaptured)
                        return freeTroops;

                    if(surroundingAttackerActiveProvinces > surroundingDefenderActiveProvinces)
                        return freeTroops;
                    if(surroundingAttackerActiveProvinces < surroundingDefenderActiveProvinces)
                        return 0;
                    return freeTroops / 2;
                }
                if ( surroundingDefenderProvinceEnemyTroops == 0 )
                    return freeTroops;
                if(provincesList.belongToCapturedContinent(attacker) != provincesList.belongToCapturedContinent(defender)  )
                    return provincesList.belongToCapturedContinent(attacker) ? freeTroops : 0;
                if(ContinentInfo.provinceContinentNumber(attacker.getProvinceID()) !=
                        ContinentInfo.provinceContinentNumber(defender.getProvinceID()))
                    return 0;

                for(Province enemyProvince: attacker.getAdjacentProvinces()) {
                    double friendlyTroops = 0;
                    ArrayList<Province> friendlyProvinces = new ArrayList<Province>();
                    if(enemyProvince.getOwnerID() != playersList.getCurrentPlayer())
                        for (Province adjProvince: enemyProvince.getAdjacentProvinces() )
                            if(adjProvince.getOwnerID() == attacker.getOwnerID())
                                if(friendlyProvinces.indexOf(adjProvince) == -1) {
                                    friendlyTroops += adjProvince.getNumOfTroops() - 1;
                                    friendlyProvinces.add(adjProvince);
                                }
                    attackerNewSum = surroundingAttackerProvinceEnemyTroops / friendlyTroops;
                }
                for(Province enemyProvince: defender.getAdjacentProvinces()) {
                    double friendlyTroops = 0;
                    ArrayList<Province> friendlyProvinces = new ArrayList<Province>();
                    if(enemyProvince.getOwnerID() != playersList.getCurrentPlayer())
                        for (Province adjProvince: enemyProvince.getAdjacentProvinces() )
                            if(adjProvince.getOwnerID() == attacker.getOwnerID() &&
                                    friendlyProvinces.indexOf(adjProvince) == -1) {
                                friendlyTroops += adjProvince.getNumOfTroops() - 1;
                                friendlyProvinces.add(adjProvince);
                            }
                    defenderNewSum = surroundingDefenderProvinceEnemyTroops / friendlyTroops;
                }
                if (Double.valueOf(attackerNewSum).isInfinite() && Double.valueOf(defenderNewSum).isInfinite())
                    return (int)(surroundingAttackerProvinceEnemyTroops /
                            (double)(surroundingAttackerProvinceEnemyTroops + surroundingDefenderProvinceEnemyTroops) *  freeTroops) ;
                if (Double.valueOf(attackerNewSum).isInfinite())
                    return freeTroops;
                if (Double.valueOf(defenderNewSum).isInfinite())
                    return 0;
                return (int)Math.round(attackerNewSum / (attackerNewSum + defenderNewSum) * freeTroops);

        }
        return 0;
    }

    private void insertDeletedContinents (ArrayList<ContinentInfo> deletedContinents) {
        cycle1:
        for(ContinentInfo continentInfo: deletedContinents) {
            for (int j = 0; j < continentArray.size(); j++)
                if (continentInfo.getContinentNumber() < continentArray.get(j).getContinentNumber()) {
                    continentArray.add(j, continentInfo);
                    continue cycle1;
                }
            continentArray.add(continentInfo);

        }
        deletedContinents.clear();
    }
    private Province getRandomActiveProvince() {
        if(activeProvinces.size() == 0 )
            return null;
        else
            return activeProvinces.get((int) (activeProvinces.size() * Math.random()));
    }

    // изменить
    public ContinentInfo getMaxValuebleContinent () {
        ContinentInfo maxContinent = null;
        for(ContinentInfo continentInfo : continentArray) {
            if(!Double.valueOf(continentInfo.getPoints()).isInfinite()) {
                if(maxContinent == null)
                    maxContinent = continentInfo;
                else
                if(maxContinent.getPoints() < continentInfo.getPoints())
                    maxContinent = continentInfo;
            }
            else {
                if(curPlayerDifficulty > 1 && continentInfo.getEnemyAdjacentTroops() != 0) {
                    if(maxContinent == null) {
                        maxContinent = continentInfo;
                    }
                    else
                        if(maxContinent.getEnemyAdjacentTroops() > continentInfo.getEnemyAdjacentTroops() &&
                                continentInfo.getEnemyAdjacentTroops() != 0)
                            maxContinent = continentInfo;
                }
            }
        }
        return maxContinent;
    }
    int getAvailableTroops() {
        return availableTroops;
    }
    void setAvailableTroops(int availableTroops) {
        this.availableTroops = availableTroops;
    }

    int decAvailableTroops() {
        return --availableTroops;
    }
    int incAvailableTroops() {
        return ++availableTroops;
    }

    PlayersList getPlayersList() {
        return playersList;
    }

    ProvincesList getProvincesList() {
        return provincesList;
    }

    public ArrayList<ContinentInfo> getContinentArray() {
        return continentArray;
    }

    private ContinentInfo getStrongestContinent() {
        Player resultContinentOwner = null;
        ContinentInfo strongestContinent = null;
        for(ContinentInfo continent: continentArray) {
            Player curContinentOwner ;
            if((curContinentOwner = continent.getOwnerID()) != null &&
                    curContinentOwner != playersList.getCurrentPlayer()) {
                if(resultContinentOwner == null) {
                    resultContinentOwner = curContinentOwner;
                    strongestContinent = continent;
                }
                else {
                    if (provincesList.getArmy(curContinentOwner) > provincesList.getArmy(resultContinentOwner) ||
                            provincesList.getArmy(curContinentOwner) == provincesList.getArmy(resultContinentOwner) &&
                                    provincesList.getContinentBonus()[continent.getContinentNumber()] >
                                            provincesList.getContinentBonus()[strongestContinent.getContinentNumber()]) {
                        resultContinentOwner = curContinentOwner;
                        strongestContinent = continent;
                    }
                }

            }
        }
        if(resultContinentOwner == null ||
                resultContinentOwner != strongestPlayer)
            return null;
        return strongestContinent;
    }

    private WayToEnemyContinent getWay(int deployTroops) {
        ArrayList <ContinentInfo> deletedContinents = new ArrayList<ContinentInfo>();
        for(int i = 0; i < continentArray.size(); i++ ) {
            ContinentInfo continent = getStrongestContinent();
            if(continent == null) {
                insertDeletedContinents(deletedContinents);
                return null;
            }
            wayToEnemyContinent = new WayToEnemyContinent(continent, provincesList, curPlayerDifficulty, playersList.getCurrentPlayer(),deployTroops);
            // если путь нежелателен, то ничего не поделать, ищем путь на другом континенте
            if(wayToEnemyContinent.isPractical(deployTroops)) {
                insertDeletedContinents(deletedContinents);
                return wayToEnemyContinent;
            }
            continentArray.remove(continent);
            deletedContinents.add(continent);
        }
        insertDeletedContinents(deletedContinents);
        return null;
    }

    private Player getEnemyStrongestPlayer () {
        Player strongestPlayerInfo = null;
        Player player = playersList.getCurrentPlayer();
        int army1 = -1;

        while ((player = playersList.getNextPlayer(player)) != playersList.getCurrentPlayer()) {
            if(army1 == -1) {
                army1 = provincesList.getPotentialArmy(player);
                strongestPlayerInfo = player;
            }
            else {
                int army =  provincesList.getPotentialArmy(player);
                if( army > army1) {
                    army1 = army;
                    strongestPlayerInfo = player;
                }
            }
        }
        return strongestPlayerInfo;
    }

    private void setEnemyStrongestPlayer() {
        strongestPlayer = getEnemyStrongestPlayer();

    }

    public void setAlreadyFoundWay(boolean alreadyFoundWay) {
        this.alreadyFoundWay = alreadyFoundWay;
    }

    public boolean isLyingOnTheBorder(Province province) {
        for(Province adjacentProvince: province.getAdjacentProvinces()) {
            if(provincesList.getContinentNumber(adjacentProvince) !=
                    provincesList.getContinentNumber(province))
                return true;
        }
        return false;
    }

}

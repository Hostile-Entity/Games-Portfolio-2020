package com.strat7.game.AI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.strat7.game.GameInfo.Player;
import com.strat7.game.GameInfo.Province;
import com.strat7.game.GameInfo.ProvincesList;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Евгений on 25.07.2017.
 */

public class WayToEnemyContinent {
    private ArrayList <Province> wayToContinent;
    private ArrayList <Province> savedWayToContinent;
    private Province target;
    private Province attacker;
    private Player player;
    private int difficulty;
    private ProvincesList provincesList;
    private ContinentInfo continent;



    public WayToEnemyContinent(ContinentInfo continent, ProvincesList provincesList, int difficulty, Player player, int deployTroops) {
        wayToContinent = null;
        savedWayToContinent = null;
        this.player = player;
        this.difficulty = difficulty;
        this.continent = continent;
        this.provincesList = provincesList;

        if(difficulty < 3) {
            makeShortWay(deployTroops);
        }
        else {
            makeFullWay(deployTroops);
            if(wayToContinent != null){
                target = wayToContinent.get(0);
                if(wayToContinent.size() > 1) {
                    attacker = wayToContinent.get(wayToContinent.size() - 1);
                }
            }
        }
    }

    private void makeShortWay( int deployTroops) {
        Province mainTarget = null;
        Province mainAttacker = null;
        for(Province province: continent.getEnemyProvinces()) {
            if(mainTarget == null)
                mainTarget = province;
            for(Province adjProvince: province.getAdjacentProvinces()) {
                if(adjProvince.getOwnerID() == player) {
                    if(mainAttacker == null) {
                        mainAttacker = adjProvince;
                    }
                    else {
                        double mainCoef = (mainAttacker.getNumOfTroops() - 1 + deployTroops) / (double) mainTarget.getNumOfTroops();
                        double secondaryCoef = (adjProvince.getNumOfTroops() - 1 + deployTroops) / (double) province.getNumOfTroops();
                        if (mainCoef < secondaryCoef) {
                            mainAttacker = adjProvince;
                            mainTarget = province;
                        }
                    }
                }
            }
            if(mainAttacker == null)
                mainTarget = null;
        }
        target = mainTarget;
        attacker = mainAttacker;

    }

    private void makeFullWay( int deployTroops) {

        for(Province province: continent.getEnemyProvinces()) {
            savedWayToContinent = new ArrayList<Province>();
            savedWayToContinent.add(province);
            savedWayToContinent = makeOneStep(savedWayToContinent);
            if(savedWayToContinent.size() == 1)
                continue;
            if(wayToContinent == null)
                wayToContinent = savedWayToContinent;
            else
            if(wayPoints(savedWayToContinent,deployTroops) > wayPoints(wayToContinent,deployTroops))
                wayToContinent = savedWayToContinent;
        }
        if(wayToContinent == null) {
            attacker = null;
            target = null;
        }
    }


    private ArrayList<Province> sort(ArrayList<Province> arrayList) {
        for(int i = 0; i < arrayList.size(); i ++) {
            for(int j = i + 1; j < arrayList.size(); j ++) {
                if(arrayList.get(j).getNumOfTroops() < arrayList.get(j-1).getNumOfTroops()) {
                    Province province = arrayList.get(j-1);
                    arrayList.remove(j-1);
                    arrayList.add( j, province );
                }
            }
        }
        return arrayList;
    }
    private ArrayList<Province> makeOneStep(ArrayList<Province> way) {
        ArrayList<Province> buffer = new ArrayList<Province>();
        ArrayList<Province> wayBuffer = new ArrayList<Province>();
        wayBuffer.addAll(way.subList(0,way.size()));
        for(Province province: way.get(0).getAdjacentProvinces()) {
            if(province.getOwnerID() == player && way.indexOf(province) == -1) {
                buffer.add(province);
            }
        }
        if(buffer.size() > 0) {
            buffer = sort(buffer);
            wayBuffer.addAll(buffer.subList(0,buffer.size()));
        }
        return wayBuffer;
    }

    AttackPair getAttackPair() {
        if(target == null || attacker == null)
            return null;
        if(difficulty < 3) {
            if((attacker.getNumOfTroops() - 1) / (double) target.getNumOfTroops() > 1.6)
                return new AttackPair(attacker,target);
        }
        else {
            for(Province province: wayToContinent) {
                if(province.getOwnerID() != player)
                    continue;
                if(province.getNumOfTroops() > 3)
                    return new AttackPair(province,wayToContinent.get(0));
            }
            for(Province province: wayToContinent) {
                if(province.getOwnerID() != player || province.getNumOfTroops() == 1)
                    continue;
                if(province.getNumOfTroops() - 1 > target.getNumOfTroops() * 1.4 || isPractical(0))
                    return new AttackPair(province,wayToContinent.get(0));
            }
        }
        return null;
    }

    public Province getTarget() {
        return target;
    }

    boolean isPassed() {
        if(attacker == null || target == null)
            return false;
        if(difficulty < 3)
            return target.getOwnerID() == attacker.getOwnerID();
        else {
            if(wayToContinent != null && wayToContinent.size() > 1)
                return wayToContinent.get(0).getOwnerID() == wayToContinent.get(1).getOwnerID();
        }
        return false;
    }
    public boolean isPractical(int deployTroops) {
        if(difficulty < 3) {
            if(attacker == null || target == null)
                return false;
            return ((attacker.getNumOfTroops() - 1 + deployTroops) / (double) target.getNumOfTroops() > 2.5);
        }
        else {
            if (wayToContinent == null)
                return false;
            return wayPoints(wayToContinent, deployTroops) > 2.6;
        }
    }

    public Province getDeployProvince() {
        if(difficulty < 3)
            return attacker;
        else
            return wayToContinent.get(wayToContinent.size() - 1);
    }

    public int needTroopsForPractical() {
        if(difficulty < 3) {
            if ((attacker.getNumOfTroops() - 1) / (double) target.getNumOfTroops() > 2.5)
                return 0;
            return (int) (target.getNumOfTroops() * 2.5 - attacker.getNumOfTroops()) + 2;
        }
        else {
            if (wayPoints(wayToContinent, 0) > 2.6)
                return 0;
            for(int i = 0;; i ++) {
                if(wayPoints(wayToContinent, i) > 2.6)
                    return i;
            }
        }
    }

    public int getDistribution(Province attacker, Province defender, int freeTroops) {
        if(wayToContinent.get(0).getOwnerID()  == player)
            return freeTroops;
        return 0;
    }
    private double wayPoints(ArrayList<Province> way, int deployTroops) {
        //System.out.println( (way.get(0).getPlayerTroopsAmountAround(player) - way.get(0).getPlayerProvincesAmountAround(player) + deployTroops) /
        //        (double)way.get(0).getNumOfTroops());
        double points = 0;
        for(Province province: way) {
            if(province.getOwnerID() == way.get(way.size() - 1).getOwnerID()) {
                int provinceArmy = province == way.get(way.size() - 1) ? way.get(way.size() - 1).getNumOfTroops() + deployTroops : province.getNumOfTroops();
                if(way.get(0).getNumOfTroops() == 1) {
                    points += (provinceArmy - 1) == 1 ? 0.6 : 1.2 * (provinceArmy - 1) ;
                }
                else {
                    points += (provinceArmy - 1) == 1 ? 0.3 :
                            (provinceArmy - 1) == 2 ? 1.4 : provinceArmy - 1;
                }
            }
        }
        return points / way.get(0).getNumOfTroops();
    }
}

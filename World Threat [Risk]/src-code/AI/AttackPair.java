package com.strat7.game.AI;

import com.strat7.game.GameInfo.Province;

/**
 * Created by Евгений on 01.07.2017.
 */

public class AttackPair {
    private Province attacker;
    private Province defender;

    public AttackPair(Province attacker, Province defender) {
        this.attacker = attacker;
        this.defender = defender;
    }

    void set (Province attacker, Province defender) {
        this.attacker = attacker;
        this.defender = defender;
    }


    public Province getAttacker() {
        return attacker;
    }

    public Province getDefender() {
        return defender;
    }
}

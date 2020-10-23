package com.strat7.game.Tutorial;

import com.badlogic.gdx.graphics.Texture;
import com.strat7.game.GameInfo.PlayersList;
import com.strat7.game.GameInfo.Province;
import com.strat7.game.GameInfo.ProvincesList;
import com.strat7.game.Interfaces.Basics.Interface;

/**
 * Created by Евгений on 17.08.2017.
 */

public class TutorialProvinceList extends ProvincesList {

    TutorialProvinceList(String mapName, PlayersList players) {
        super(mapName,players);
    }

    @Override
    protected void createProvinces() {
        text = positions.readString();
        strings = text.split("\\r?\\n");
        for(int i = 0 ; i < provinceAmount; i ++) {
            words = strings[i].split(",");
            provinceArray[i] = new TutorialProvince(Interface.MAIN_FRAME,Integer.parseInt(words[0]), Integer.parseInt(words[1]), null, defaultDirectory + "Provinces/Pr" + String.valueOf(i) + ".png",this);
            provinceArray[i].setVisible(true);
        }
    }

}

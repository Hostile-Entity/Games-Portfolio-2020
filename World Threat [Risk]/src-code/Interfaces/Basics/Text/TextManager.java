package com.strat7.game.Interfaces.Basics.Text;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

/**
 * Created by Евгений on 20.08.2017.
 */

public class TextManager {
    String [] strings;
    double height;
    double [] widths;
    ArrayList<ArrayList<String>> words;

    TextManager (GlyphLayout layout) {
        Array<GlyphLayout.GlyphRun> runs = layout.runs;
        strings = new String[runs.size];
        height  = 0;
        widths  = new double[runs.size];
        words   = new ArrayList<ArrayList<String>>();
        for(int i = 0; i < runs.size; i++)
            words.add(new ArrayList<String>());

        int i = 0;
        for(GlyphLayout.GlyphRun run: runs) {
            String str = "";
            String word = "";
            for(BitmapFont.Glyph glyph :run.glyphs) {
                str += ((char) glyph.id);
                if(((char) glyph.id) == ' ') {
                    words.get(i).add(word);
                    word = "";
                }
            }
            strings[i] = str;
            height = 0;
            widths [i] = run.width;
            i++;
        }
    }
}

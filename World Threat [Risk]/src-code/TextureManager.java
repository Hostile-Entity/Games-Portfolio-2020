package com.strat7.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Юра on 31.07.2017.
 */

public class TextureManager {
    private HashMap<String,Texture> allTextures;


    TextureManager () {
        allTextures = new HashMap<String, Texture>();
    }



    private void loadFrom(String internalPath) {
        for(FileHandle fileHandle: Gdx.files.internal(internalPath).list()) {
            if(fileHandle.isDirectory())
                loadFrom(fileHandle.path());
            if(fileHandle.extension().equals("png")) {
                allTextures.put(fileHandle.path(),new Texture(fileHandle));
            }
        }
    }

    public void loadAllTextures(String[] directories) {
        clear();
        for (String string: directories)
            loadFrom(string);
    }

    public void clear() {
        for(HashMap.Entry<String, Texture> entry : allTextures.entrySet()) {
            entry.getValue().dispose();
        }
        allTextures.clear();
    }


    public Texture getTextureByName(String name) {
        FileHandle fileHandle = Gdx.files.internal(name);
        if(allTextures.get(fileHandle.path()) == null) {
            allTextures.put(fileHandle.path(), new Texture(fileHandle));
        }
        return allTextures.get(Gdx.files.internal(name).path());
    }

    public void disposePath(String internalPath) {
        int size = 0;
        LinkedList<String> mustBeDeleted = new LinkedList<String>();
        for(HashMap.Entry<String, Texture> entry : allTextures.entrySet()) {
            if(entry.getKey().startsWith(internalPath)) {
                entry.getValue().dispose();
                mustBeDeleted.add(entry.getKey());
                size ++;
            }
        }
        for(int i = 0; i < size; i ++) {
            allTextures.remove(mustBeDeleted.getFirst());
            mustBeDeleted.removeFirst();
        }

    }
}

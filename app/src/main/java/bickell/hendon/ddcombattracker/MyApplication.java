package bickell.hendon.ddcombattracker;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by fredi on 8/9/2017.
 */

public class MyApplication extends Application {
    private ArrayList<Character> playerCharacters;

    public ArrayList<Character> getPlayerCharacters() {
        return playerCharacters;
    }
    public void setPlayerCharacters(ArrayList<Character> playerCharacters) {
        this.playerCharacters = playerCharacters;
    }
}

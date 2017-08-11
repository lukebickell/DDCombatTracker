package bickell.hendon.ddcombattracker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }
    public ArrayList<Character> deserializeCharacter(JSONArray jsonArray){
        ArrayList<Character> characters = new ArrayList<Character>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject charObj = jsonArray.getJSONObject(i);
                Character character = new Character();
                character.setName(charObj.getString("name"));
                character.setArmor(charObj.getInt("armor"));
                character.setHpMax(charObj.getInt("hpMax"));
                character.setHpCurrent(charObj.getInt("hpCurrent"));
                character.setInitiativeModifier(charObj.getInt("initiativeModifier"));
                character.setHasInitativeAdvantage(charObj.getBoolean("hasInitiativeAdvantage"));
                characters.add(character);
            }
        }
        catch (JSONException ex){
            ex.printStackTrace();
        }
        return characters;
    }

    /** Called when the user taps the Manage Characters button */
        public void sendMessage(View view) {
            Intent intent = new Intent(this, CharacterManagerActivity.class);
            startActivity(intent);
        }

    // FOR CLOSING
    public JSONArray serializeCharacters()
    {   JSONArray jsonArr = new JSONArray();
        try {
            for (Character character : ((MyApplication) this.getApplication()).getPlayerCharacters()) {
                JSONObject characterObj = new JSONObject();
                characterObj.put("name", character.getName());
                characterObj.put("armor", character.getArmor());
                characterObj.put("hpMax", character.getHpMax());
                characterObj.put("hpCurrent", character.getHpCurrent());
                characterObj.put("initiativeModifier", character.getInitiativeModifier());
                characterObj.put("hasInitiativeAdvantage", character.hasInitiativeAdvantage());
                jsonArr.put(characterObj);
            }
        }
        catch(JSONException ex){
            ex.printStackTrace();
        }
        return jsonArr;
    }
    public void savePlayerCharacters(View view){
        String filename = "playerCharacters.json";
        String string = serializeCharacters().toString();
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }



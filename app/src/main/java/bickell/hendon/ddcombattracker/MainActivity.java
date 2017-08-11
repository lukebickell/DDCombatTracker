package bickell.hendon.ddcombattracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private AlertDialog joinEncounterDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        // load player characters
        loadPlayerCharacters();

        // construct the join encounter alert dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title
        alertDialogBuilder.setTitle("Join Encounter");

        // create the edit text
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        // set dialog message
        alertDialogBuilder
                .setView(input)
                .setPositiveButton("Join", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Validate entry //TODO actually validate input
                        String ipAddress = input.getText().toString();

                        // Attempt to connect to host
                        MyClientTask myClientTask = new MyClientTask(
                                ipAddress);
                        myClientTask.execute();
                        // Open up client View
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        joinEncounterDialog = alertDialogBuilder.create();
    }

    /**
     * Called when the user taps the Send button
     */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, CharacterManagerActivity.class);
        startActivity(intent);
    }

    public void hostEncounter(View view) {
        Intent intent = new Intent(this, HostEncounterActivity.class);
        startActivity(intent);
    }

    public void joinEncounter(View view) {

        joinEncounterDialog.show();
    }


    public void loadPlayerCharacters()
    {
        // Get the JSON string from shared preferences
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.Preferences), Context.MODE_PRIVATE);
        String defaultValue = "{\"characters\": []}";
        String charactersJSON = sharedPref.getString("characters", defaultValue);
        Log.d("savePlayActivityLoad",charactersJSON);

        // Turn the string into a JSONArray
        JSONArray charactersJSONArray = null;
        try {
            JSONObject characterArray = new JSONObject(charactersJSON);
            charactersJSONArray = (JSONArray) characterArray.get(getString(R.string.charactersJSONObjectName));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        // Deserialize JSONArray and set characters to correct values
        ArrayList<Character> charactersArray = deserializeCharacters(charactersJSONArray);
        ((MyApplication) this.getApplication()).setPlayerCharacters(charactersArray);
    }

    public ArrayList<Character> deserializeCharacters(JSONArray jsonArray) {
        ArrayList<Character> characters = new ArrayList<>();
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
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return characters;
    }
}




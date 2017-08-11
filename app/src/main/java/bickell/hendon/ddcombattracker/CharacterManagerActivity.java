package bickell.hendon.ddcombattracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CharacterManagerActivity extends AppCompatActivity {
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_manager);

        // This block allows clicking on list entries
        lv = (ListView) findViewById(R.id.ListView);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView < ? > adapter, View view,int position, long id){
                // use position to find your values
                // to go to ShowDetailsActivity, you have to use Intent
                Intent detailScreen = new Intent(getApplicationContext(), CharacterCreationActivity.class);
                detailScreen.putExtra("position", position); // pass value if needed
                startActivity(detailScreen);
            }
        });

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // Create array list to add characters names to for ListView
        List<String> your_array_list = new ArrayList<>();
        for (Character character:((MyApplication) this.getApplication()).getPlayerCharacters()){
            your_array_list.add(character.getName());
        }

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list);

        lv.setAdapter(arrayAdapter);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        savePlayerCharacters();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_name) {
            Intent intent = new Intent(this, CharacterCreationActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    // FOR CLOSING
    public JSONArray serializeCharacters() {
        JSONArray jsonArr = new JSONArray();
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
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return jsonArr;
    }

    public void savePlayerCharacters() {
        JSONObject characterArray = new JSONObject();
        JSONArray charactersJSONArray = serializeCharacters();
        try {
            characterArray.put(getString(R.string.charactersJSONObjectName), charactersJSONArray);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        Log.d("savePlayActivity", characterArray.toString());

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.Preferences),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("characters", characterArray.toString());
        editor.apply();
    }

}

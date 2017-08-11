package bickell.hendon.ddcombattracker;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private AlertDialog joinEncounterDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

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

    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;
        String response = "";

        MyClientTask(String address) {
            dstAddress = address;
            dstPort = 8080; //TODO Extract port as int resource
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;

            try {
                socket = new Socket(dstAddress, dstPort);

                ByteArrayOutputStream byteArrayOutputStream =
                        new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];

                int bytesRead;
                InputStream inputStream = socket.getInputStream();

                /*
                 * notice:
                 * inputStream.read() will block if no data return
                 */
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    response += byteArrayOutputStream.toString("UTF-8");
                }

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // DO SOMETHING WITH RESULT
            super.onPostExecute(result);
        }
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




package bickell.hendon.ddcombattracker;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.support.v7.widget.AppCompatCheckBox;


import java.util.ArrayList;

public class CharacterCreationActivity extends AppCompatActivity {
    ArrayList<TextView> textViews;
    TextView nameText;
    TextView armorText;
    TextView hpText;
    TextView initiativeText ;
    AppCompatCheckBox initiativeAdvantage;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character__creation);
        nameText = (TextView) findViewById(R.id.nameText);
        armorText = (TextView) findViewById(R.id.armorText);
        hpText = (TextView) findViewById(R.id.hpText);
        initiativeText = (TextView) findViewById(R.id.initText);
        initiativeAdvantage = (AppCompatCheckBox) findViewById(R.id.checkBox);
        textViews = new ArrayList<>();
        textViews.add(nameText);
        textViews.add(hpText);
        textViews.add(armorText);
        textViews.add(initiativeText);
        textViews.add(initiativeAdvantage);


        if(getIntent().getExtras() != null)
        {
            // do your functionality with extras
            Bundle bundle = getIntent().getExtras();
            index = bundle.getInt("position");
            Character currentCharacter = ((MyApplication) this.getApplication()).getPlayerCharacters().get(index);
            nameText.setText(currentCharacter.getName());
            armorText.setText(Integer.toString(currentCharacter.getArmor()));
            hpText.setText(Integer.toString(currentCharacter.getHpMax()));
            initiativeText.setText(Integer.toString(currentCharacter.getInitiativeModifier()));
            initiativeAdvantage.setChecked(currentCharacter.hasInitiativeAdvantage());

        }

        else
        {
            index = -1;
        }
    }

    public void saveCharacter(View view) {
        boolean allFilled = true;
       for (TextView textView: textViews){
           if (textView.getText().toString().matches("")){
               textView.setBackgroundColor(Color.RED);
               textView.getBackground().setAlpha(51);
               allFilled = false;
           }
           else{
               textView.setBackgroundColor(Color.TRANSPARENT);
           }
       }
       if (!allFilled){
           return;
       }
       ArrayList<Character> characters = ((MyApplication) this.getApplication()).getPlayerCharacters();
        Character currentCharacter = new Character();

        currentCharacter.setName(nameText.getText().toString());
        currentCharacter.setArmor(Integer.parseInt(armorText.getText().toString()));
        currentCharacter.setHpMax(Integer.parseInt(hpText.getText().toString()));
        currentCharacter.setInitiativeModifier(Integer.parseInt(initiativeText.getText().toString()));
        currentCharacter.setHasInitativeAdvantage(initiativeAdvantage.isChecked());
        if (index >= 0) {
            characters.set(index, currentCharacter);
        } else
        {
            characters.add(currentCharacter);
        }

        Intent intent = new Intent(this, CharacterManagerActivity.class);
        startActivity(intent);
    }

}

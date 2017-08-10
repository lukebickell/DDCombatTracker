package bickell.hendon.ddcombattracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class CharacterCreationActivity extends AppCompatActivity {
    TextView nameText = (TextView) findViewById(R.id.nameText);
    TextView armorText = (TextView) findViewById(R.id.armorText);
    TextView hpText = (TextView) findViewById(R.id.hpText);
    TextView initiativeText = (TextView) findViewById(R.id.initText);
    CheckBox initiativeAdvantage = (CheckBox) findViewById(R.id.checkBox);
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character__creation);
        if(getIntent().getExtras() != null)
        {
            // do your functionality with extras
            Bundle bundle = getIntent().getExtras();
            index = bundle.getInt("position");
            Character currentCharacter = ((MyApplication) this.getApplication()).getPlayerCharacters().get(index);
            nameText.setText(currentCharacter.getName());
            armorText.setText(currentCharacter.getName());
            hpText.setText(currentCharacter.getName());
            initiativeText.setText(currentCharacter.getName());
            initiativeAdvantage.setChecked(currentCharacter.hasInitiativeAdvantage());

        }

        else
        {
            index = -1;
        }
    }

    public void saveCharacter() {
        ArrayList<Character> characters = ((MyApplication) this.getApplication()).getPlayerCharacters();
        Character currentCharacter = new Character();

        currentCharacter.setName(nameText.getText().toString());
        currentCharacter.setArmor(Integer.parseInt(armorText.getText().toString()));
        currentCharacter.setHpMax(Integer.parseInt(hpText.getText().toString()));
        currentCharacter.setInitiativeModifier(Integer.parseInt(initiativeText.getText().toString()));
        currentCharacter.setHasInitativeAdvantage(Boolean.getBoolean(initiativeAdvantage.getText().toString()));
        if (index >= 0) {
            characters.set(index, currentCharacter);
        } else
        {
            characters.add(currentCharacter);
        }
    }

}

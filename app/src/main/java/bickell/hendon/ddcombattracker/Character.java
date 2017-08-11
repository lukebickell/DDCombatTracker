package bickell.hendon.ddcombattracker;

/**
 * Created by fredi on 8/9/2017.
 */

public class Character {
   // Initializes variables for character
    private String name;
    private int armor;
    private int hpMax;
    private int hpCurrent;
    private int initiativeModifier;
    private boolean hasInitativeAdvantage;

    public String getName() {
        return name;
    }
    public int getArmor() {
        return armor;
    }

    public int getHpMax() {
        return hpMax;
    }

    public int getHpCurrent() {
        return hpCurrent;
    }

    public int getInitiativeModifier() {
        return initiativeModifier;
    }
    public boolean hasInitiativeAdvantage(){
        return hasInitativeAdvantage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public void setHpMax(int hpMax) {
        this.hpMax = hpMax;
    }

    public void setHpCurrent(int hpCurrent) {
        this.hpCurrent = hpCurrent;
    }

    public void setInitiativeModifier(int initiativeModifier) {
        this.initiativeModifier = initiativeModifier;
    }

    public void setHasInitativeAdvantage(boolean hasInitativeAdvantage) {
        this.hasInitativeAdvantage = hasInitativeAdvantage;
    }
}

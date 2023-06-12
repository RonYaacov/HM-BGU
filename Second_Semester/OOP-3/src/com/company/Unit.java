package com.company;

public class Unit extends Tile {
    protected String name;
    protected int healthPoll;
    protected int healtAmount;
    protected int attackPoints;
    protected int defencePoints;

    public Unit(char tile, String name, int healthPoll, int attackPoints, int defencePoints) {
        super(tile);
        this.name = name;
        this.healthPoll = healthPoll;
        this.healtAmount = healthPoll;// on creation health amount is equal to health poll
        this.attackPoints = attackPoints;
        this.defencePoints = defencePoints;
    }
    public void accept(Unit unit){
        //TODO: implement me
    }
   


    
}

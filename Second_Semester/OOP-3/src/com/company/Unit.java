package com.company;

public class Unit extends Tile {
    protected String name;
    protected Health health;
    protected int attackPoints;
    protected int defencePoints;

    public Unit(char tile, String name, Health health, int attackPoints, int defencePoints) {
        super(tile);
        this.name = name;
        this.health = health;
        this.attackPoints = attackPoints;
        this.defencePoints = defencePoints;
    }
    public void accept(Unit unit){
        //TODO: implement me
    }
   


    
}

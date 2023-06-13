package com.company;

public class Player extends Unit {
    private int experience;
    private int level;
    protected enum playerTypes {Warrior, Mage, Rogue};



    public Player(char tile, String name,Health health, int attackPoints, int defencePoints){
        super(tile, name, health, attackPoints, defencePoints);
        this.experience = 0;
        this.level = 1;
    }
    public Player(char tile, String name, Health  health, int attackPoints, int defencePoints, int experience, int level){
        super(tile, name, health, attackPoints, defencePoints);
        this.experience = experience;
        this.level = level;
    }


    
}

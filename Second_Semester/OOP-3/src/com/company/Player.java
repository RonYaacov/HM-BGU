package com.company;


public class Player extends Unit {
    protected int experience;
    protected int level;

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
    protected void levelUp(){
        this.level++;
        this.setExperience(this.experience - (50*this.level));
        this.health.increaseHealthPoll(10*this.level);
        this.health.setHealthAmount(this.health.getHealthPool());
        this.setAttackPoints(attackPoints + 4*this.level);
        this.setDefencePoints(defencePoints + this.level);
    }
    
    public void setExperience(int experience){
        this.experience = experience;
    }
    


    
}

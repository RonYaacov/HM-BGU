package com.company;

public class Warrior extends Player{

    private final int abilityCooldown;
    private int remainingCooldown;
    public Warrior(char tile, String name, Health health, int attackPoints, int defencePoints, int abilityCooldown){
        super(tile, name, health, attackPoints, defencePoints);
        this.abilityCooldown = abilityCooldown;
        this.remainingCooldown = 0;
    }
    public Warrior(char tile, String name, Health  health, int attackPoints, int defencePoints, int experience, int level, int abilityCooldown){
        super(tile, name, health, attackPoints, defencePoints, experience, level);
        this.abilityCooldown = abilityCooldown;
        this.remainingCooldown = 0;
    }

    public int getAbilityCooldown() {
        return abilityCooldown;
    }

    public int getRemainingCooldown() {
        return remainingCooldown;
    }

    public void setRemainingCooldown(int remainingCooldown) {
        if (remainingCooldown <= abilityCooldown)
            this.remainingCooldown = remainingCooldown;
    }

    public void castAbility(Unit enemy){
        if(remainingCooldown == 0){
            remainingCooldown = abilityCooldown;
            this.health.increaseHealth(10 * defencePoints);
            //TODO: remove enemy input and search for the closest enemy where d(e) < 3, if null, do nothing, else, attack
            if(enemy != null){
                enemy.receiveDamage(0.1 * this.health.getHealthPool());
            }
        }
        else{
            System.out.println("Ability is not ready yet!");
        }
    }

    public void levelUP(){
        super.levelUp();
        remainingCooldown = 0;
        this.health.increaseHealthPoll(5 * level);
        this.health.setHealthAmount(this.health.getHealthPool());
        attackPoints += (2 * level);
        defencePoints += level;
    }

    public void tick(){
        if(remainingCooldown > 0)
            remainingCooldown--;
    }



}

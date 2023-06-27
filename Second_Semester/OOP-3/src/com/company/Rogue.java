package com.company;

import java.util.*;

public class Rogue extends Player{
    private int cost;
    private int currentEnergy; //max 100

    public Rogue(char tile, String name, Health health, int attackPoints, int defencePoints, int cost){
        super(tile, name, health, attackPoints, defencePoints);
        this.cost = cost;
        this.currentEnergy = 100;
    }

    public Rogue(char tile, String name, Health health, int attackPoints, int defencePoints, int experience, int level, int cost){
        super(tile, name, health, attackPoints, defencePoints, experience, level);
        this.cost = cost;
        this.currentEnergy = 100;
    }

    public void levelUp(){
        super.levelUp();
        currentEnergy = 100;
        attackPoints += (3 * level);
    }

    public void tick(){
        currentEnergy = Math.min(currentEnergy + 10, 100);
    }

    public void castAbility(){
        if(currentEnergy >= cost){
            currentEnergy -= cost;
            List<Enemy> enemies = board.getEnemiesInRange(2);
            while(!enemies.isEmpty()){
                Enemy enemy = enemies.remove(0);
                enemy.receiveDamage(attackPoints);
            }
        }
        else{
            System.out.println("Ability is not ready yet!");
        }
    }
}

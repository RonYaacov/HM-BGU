package com.company;

import java.util.List;
import java.util.Random;

public class Mage extends Player{
    private int manaPool;
    private int currentMana;
    private int manaCost;
    private int spellPower;
    private int maxHits;
    private int abilityRange;


    public Mage(char tile, String name, Health health, int attackPoints, int defencePoints, int manaPool, int manaCost, int spellPower, int hitCount, int abilityRange){
        super(tile, name, health, attackPoints, defencePoints);
        this.manaPool = manaPool;
        this.currentMana = manaPool/4;
        this.manaCost = manaCost;
        this.spellPower = spellPower;
        this.maxHits = hitCount;
        this.abilityRange = abilityRange;
    }
    public Mage(char tile, String name, Health health, int attackPoints, int defencePoints, int experience, int level, int manaPool, int manaCost, int spellPower, int abilityRange){
        super(tile, name, health, attackPoints, defencePoints, experience, level);
        this.manaPool = manaPool;
        this.currentMana = manaPool/4;
        this.manaCost = manaCost;
        this.spellPower = spellPower;
        this.abilityRange = abilityRange;
    }

    public void levelUp(){
        super.levelUp();
        this.manaPool += (25 * level);
        this.currentMana = Math.min(currentMana + (manaPool/4), manaPool);
        this.spellPower += (10 * level);
    }

    public void tick(){
        currentMana = Math.min((currentMana + level), manaPool);
    }

    public void castAbility(){
        if(currentMana >= manaCost){
            currentMana -= manaCost;
            int hits = 0;
            List<Enemy> enemies = board.getEnemiesInRange(abilityRange);
            while(hits < maxHits & !enemies.isEmpty()){
                Enemy enemy = enemies.remove(new Random().nextInt(enemies.size()));
                enemy.receiveDamage(spellPower);
                hits++;
            }
        }
        else{
            System.out.println("Ability is not ready yet!");
        }
    }
}

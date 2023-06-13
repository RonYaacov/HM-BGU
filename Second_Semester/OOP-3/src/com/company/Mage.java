package com.company;

public class Mage extends Player{
    private int manaPool;
    private int currentMana;
    private int manaCost;
    private int spellPower;
    private int hitsCount;
    private int abilityRange;
    public Mage(char tile, String name, Health health, int attackPoints, int defencePoints, int manaPool, int manaCost, int spellPower, int abilityRange){
        super(tile, name, health, attackPoints, defencePoints);
        this.manaPool = manaPool;
        this.currentMana = manaPool/4;
        this.manaCost = manaCost;
        this.spellPower = spellPower;
        this.hitsCount = 0;
        this.abilityRange = abilityRange;
    }
    public Mage(char tile, String name, Health health, int attackPoints, int defencePoints, int experience, int level, int manaPool, int manaCost, int spellPower, int abilityRange){
        super(tile, name, health, attackPoints, defencePoints, experience, level);
        this.manaPool = manaPool;
        this.currentMana = manaPool/4;
        this.manaCost = manaCost;
        this.spellPower = spellPower;
        this.hitsCount = 0;
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

    public void castAbility(Unit enemy){
        if(currentMana >= manaCost){
            currentMana -= manaCost;
            int hits = 0;
            while(hits < hitsCount /*& enemy exists in range*/){
                //select random enemy in range
                enemy.receiveDamage(spellPower);
                hits++;
            }

        }
        else{
            System.out.println("Ability is not ready yet!");
        }
    }
}

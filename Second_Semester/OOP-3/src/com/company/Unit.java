package com.company;
import java.util.Random;
import com.company.Interfaces.Visited;
import com.company.Interfaces.Visitor;

public class Unit extends Tile implements Visitor, Visited {
    protected String name;
    protected Health health;
    protected int attackPoints;
    protected int defencePoints;
    protected Random rand;

    public Unit(char tile, String name, Health health, int attackPoints, int defencePoints) {
        super(tile);
        this.name = name;
        this.health = health;
        this.attackPoints = attackPoints;
        this.defencePoints = defencePoints;
        this.rand = new Random();
    }
    public void accept(Unit unit){
        //TODO: implement me
    }
    public int attackPoints(){
        return attackPoints;
    }
    public int defencePoints(){
        return defencePoints;
    }
    public void setAttackPoints(int attackPoints){
        this.attackPoints = attackPoints;
    }
    public void setDefencePoints(int defencePoints){
        this.defencePoints = defencePoints;
    }
    
    public boolean attack(Unit enemy){
        double currentAttack = this.attackPoints*rand.nextDouble();
        return enemy.receiveDamage(currentAttack) != -1;
    }

    public int receiveDamage(double damage){
        double currentDefence = this.defencePoints*rand.nextDouble();
        if(currentDefence < damage){
            this.health.setHealthAmount((int)(this.health.getHealthAmount() - (damage - currentDefence)));
            if(health.getHealthAmount() < 0){
                onkilled(this);
            }
            
        }
        return -1;
    }
    public int onkilled(Unit unit){


    }
    
}

package com.company;

import com.company.Interfaces.Visitor;

public class Player extends Unit {
    protected int experience;
    protected int level;
    protected boolean isAlive;
    protected Board board;

    public Player(char tile, String name,Health health, int attackPoints, int defencePoints){
        super(tile, name, health, attackPoints, defencePoints);
        this.experience = 0;
        this.level = 1;
        this.isAlive = true;
    }
    public Player(char tile, String name, Health  health, int attackPoints, int defencePoints, int experience, int level){
        super(tile, name, health, attackPoints, defencePoints);
        this.experience = experience;
        this.level = level;
        this.isAlive = true;
    }
    protected void levelUp(){
        this.level++;
        this.setExperience(this.experience - (50*this.level));
        this.health.increaseHealthPoll(10*this.level);
        this.health.setHealthAmount(this.health.getHealthPool());
        this.setAttackPoints(attackPoints + 4*this.level);
        this.setDefencePoints(defencePoints + this.level);
    }

    public void setBoard(Board board){
        this.board = board;
    }
    public void setExperience(int experience){
        this.experience = experience;
    }

    @Override
    public void visitBattle(Player player) {
        throw new UnsupportedOperationException("player should not kill another player");
    }

    @Override
    public void visitBattle(Enemy enemy) {
        setExperience(experience + enemy.getExperienceReword());
    }

    @Override
    public void acceptBattle(Visitor visitor) {
        visitor.visitBattle(this);
    }
    public boolean isAlive(){
        return isAlive;
    }
    public void kill(){
        isAlive = false;
    }
}

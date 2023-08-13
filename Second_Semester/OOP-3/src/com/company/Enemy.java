package com.company;

import com.company.enums.TileType;
import com.company.Interfaces.Visitor;

public abstract class Enemy extends Unit  {
    protected int experienceReword;

    public Enemy(char tile, String name, Health health, int attackPoints, int defencePoints, int experienceReword){
        super(tile, name, health, attackPoints, defencePoints);
        this.experienceReword = experienceReword;
    }
    public int getExperienceReword(){
        return experienceReword;
    }

    @Override
    public void visitBattle(Player player){
        player.kill();
    }
    
    @Override
    public TileType visitPosChanged(Board board){
        return board.visitMove(this);
    }


    @Override
    public void visitBattle(Enemy enemy){
        throw new UnsupportedOperationException("enemy should not kill another enemy");
    }
    public void acceptBattle(Visitor visitor){
        visitor.visitBattle(this);
    }
    protected void moveDown(){
        this.position.setY(this.position.getY() - 1);
    }
    protected void moveUp(){
        this.position.setY(this.position.getY() + 1);
    }
    protected void moveLeft(){
        this.position.setX(this.position.getX() - 1);
    }
    protected void moveRight(){
        this.position.setX(this.position.getX() + 1);
    }
    
}

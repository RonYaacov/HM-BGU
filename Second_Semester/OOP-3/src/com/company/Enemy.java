package com.company;

import com.company.Interfaces.Visitor;

public abstract class Enemy implements Visitor {
    protected int experienceReword;

    public Enemy(int experienceReword){
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
    public void visitBattle(Enemy enemy){
        throw new UnsupportedOperationException("enemy should not kill another enemy");
    }
    
}

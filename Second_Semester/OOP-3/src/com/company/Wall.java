package com.company;

import com.company.Interfaces.Visitor;

public class Wall extends Tile{
    public Wall(){
        super('#');
    }

    @Override
    public void acceptBattle(Visitor visitor) {
        
        return;
    }

    @Override
    public void visitBattle(Player player) {
        return;    
    }

    @Override
    public void visitBattle(Enemy enemy) {
        return;    
    }

}

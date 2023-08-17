package com.company;

import com.company.enums.TileType;
import com.company.Interfaces.Visitor;

public class EmptyTile extends Tile {
    public EmptyTile(){
        super('.');
    }
    @Override
    public TileType visitPosChanged(Board board){
        return board.visitMove(this);
    }
    public void setPosition(Position position){
        this.position = position;
    }
    public Position getPosition(){
        return position;
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

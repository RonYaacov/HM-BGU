package com.company;

public class EmptyTile extends Tile {
    public EmptyTile(){
        super('.');
    }
    @Override
    public String visitPosChanged(Board board){
        return board.visitMove(this);
    }
    public void setPosition(Position position){
        this.position = position;
    }
    public Position getPosition(){
        return position;
    }
}

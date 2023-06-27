package com.company;

public class EmptyTile extends Tile {
    public EmptyTile(){
        super('.');
    }
    public void setPosition(Position position){
        this.position = position;
    }
    public Position getPosition(){
        return position;
    }
}

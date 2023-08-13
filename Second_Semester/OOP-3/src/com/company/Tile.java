package com.company;

import com.company.enums.TileType;
import com.company.Interfaces.Visitor;

public abstract class Tile implements Comparable<Tile> , Visitor {
    protected char tile;
    protected Position position;
    protected Board board;

    protected Tile(char tile){
        this.tile = tile;
    }
    public void setBoard(Board board){
        this.board = board;
    }

    public boolean isPassable(){
        return tile != '#';
    }

    protected void initialize(Position position){
        this.position = position;
    }

    public char getTile() {
        return tile;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    //public abstract void accept(Unit unit);

    @Override
    public int compareTo(Tile tile){
        return this.position.compareTo(tile.position);
    }

    @Override
    public String toString() {
        return String.valueOf(tile);
    }
    public TileType visitPosChanged(Board board){
        return TileType.EmptyTile;
    }
       
    public abstract void acceptBattle(Visitor visitor);

    public abstract void visitBattle(Player player);

    public abstract void visitBattle(Enemy enemy);
  
}

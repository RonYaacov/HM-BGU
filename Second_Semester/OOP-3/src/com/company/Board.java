package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.company.enums.TileType;
import com.company.Interfaces.PosListener;

public class Board implements PosListener {
    private Tile[][] board;
    private String filePath;
    private TileFactory factory;

    public Board(String filePath, Tile player) {
        this.factory = new TileFactory();
        this.filePath = filePath;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            StringBuilder tiles = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                tiles.append(line).append("\n");
            }
            reader.close();
            String[] lines = tiles.toString().split("\n");
            int rows = lines.length;
            int cols = lines[0].length();
            board = new Tile[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    char symbol = lines[i].charAt(j);
                    Tile tile;
                    if(symbol == '@'){
                        tile = player;
                    }
                    else{

                        tile = factory.produceObject(symbol);
                    }
                    tile.setPosition(new Position(i, j));
                    board[i][j] = tile;
                    tile.position.Register(this);
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void setTile(Position position, Tile tile){
        board[position.getX()][position.getY()] = tile;
    }

    public Tile getTile(Position position){ //should be used with try/catch in case position out of bounds
        return board[position.getX()][position.getY()];
    }

    public Tile[][] getBoard() {
        return board;
    }

    public List<Enemy> getEnemiesInRange(int range){
        List<Enemy> enemies = new ArrayList<>();
        for (Tile[] row : board) {
            for (Tile tile : row) {
                if (tile instanceof Enemy) {
                    if (range(tile, board[0][0]) < range) {
                        enemies.add((Enemy) tile);
                    }
                }
            }
        }
        if(enemies.isEmpty()){
            return null;
        }
        return enemies;
    }
    private double range(Tile a, Tile b){
        return Math.sqrt(a.getPosition().getX() - b.getPosition().getX()) + Math.abs(a.getPosition().getY() - b.getPosition().getY());
    }
    @Override
    public String toString(){
        StringBuilder boardString = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                boardString.append(board[i][j].toString());
            }
            boardString.append("\n");
        }
        return boardString.toString();
    }
    private TileType move(Tile wanted){
        return wanted.visitPosChanged(this);
    }
    public TileType visitMove(EmptyTile emptyTile){
        return TileType.EmptyTile;
        
    }
    public TileType visitMove(Player playerTile){
        return TileType.Player;
        
    }
    public TileType visitMove(Enemy enemyTile){
        return TileType.Enemy;
        
    }
   
    @Override
    public void posChanged(Position prevePos, Position newPos){
        Tile newTile = board[prevePos.getX()][prevePos.getY()];
        Tile oldTile = new EmptyTile();
        Tile wanted =  board[newPos.getX()][newPos.getY()];
        TileType result = move(wanted);
        switch (result){
            case EmptyTile:
                board[prevePos.getX()][prevePos.getY()] = oldTile;
                board[newPos.getX()][newPos.getY()] = newTile;
                break;
            case Player:
                wanted.acceptBattle(newTile);
                break;
            case Enemy:
                wanted.acceptBattle(newTile);
                break;
        }
        
        
    }
}

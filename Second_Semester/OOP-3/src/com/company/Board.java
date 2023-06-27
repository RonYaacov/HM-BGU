package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Board {
    private Tile[][] board;
    private int size;

    public Board(int size){
        String filePath = "path/to/your/file.txt";

        try {
            // Read the text file
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();

            // Split the content by newline character
            String[] lines = content.toString().split("\n");

            // Determine the dimensions of the 2D array
            int numRows = lines.length;
            int numCols = lines[0].length();

            // Create the 2D array
            char[][] array2D = new char[numRows][numCols];

            // Fill the 2D array with characters
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    array2D[i][j] = lines[i].charAt(j);
                }
            }

            // Print the 2D array
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    System.out.print(array2D[i][j]);
                }
                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize(){
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                board[i][j] = new EmptyTile('.');
            }
        }
    }

    public void print(){
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }

    public void addTile(Tile tile){
        board[tile.getPosition().getX()][tile.getPosition().getY()] = tile;
    }

    public void removeTile(Tile tile){
        board[tile.getPosition().getX()][tile.getPosition().getY()] = new EmptyTile('.');
    }

    public Tile getTile(Position position){
        return board[position.getX()][position.getY()];
    }

    public int getSize() {
        return size;
    }

    public Tile[][] getBoard() {
        return board;
    }
}

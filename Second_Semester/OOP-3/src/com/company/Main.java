package com.company;

public class Main {

    public static void main(String[] args) {
        UI ui = new UI();
        
        GameManeger gameMangManeger = new GameManeger(System.getProperty("user.dir")+"\\level1.txt");
        ui.Register(gameMangManeger);
        ui.getPlayerType();

    }
}

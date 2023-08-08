package com.company;

public class Main {

    public static void main(String[] args) {
        UI ui = new UI();
        GameManeger gameMangManeger = new GameManeger(System.getProperty("user.dir")+"\\Second_Semester\\OOP-3\\src\\com\\company\\levels_dir\\level1.txt");
        ui.Register(gameMangManeger);
        ui.getPlayerType();
        ui.printBoard(gameMangManeger.getBoard());
        

    }
}

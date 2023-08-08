package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.company.Interfaces.TickListener;
import com.company.Interfaces.UIEvent;
import com.company.Interfaces.UIListener;

public class UI implements UIEvent, TickListener {
    
    private List<UIListener> UIlisteners;
    private Scanner scanner = new Scanner(System.in);

    public UI(){
        
        UIlisteners = new ArrayList<UIListener>();
    }

    public void getPlayerType(){
        System.out.print("Enter Player Name: ");
        String playerName = scanner.nextLine();
        raiseEvent(playerName);
    }

    public void printBoard(Board board){
        System.out.println(board.toString());
        
    }
    @Override
    public void Register(UIListener listener) {
        UIlisteners.add(listener);
    }

    @Override
    public void raiseEvent(String event) {
        for(UIListener listener : UIlisteners)
            listener.onUIEvent(event);
        
    }
    
    public void clearScreen() {  
       
        String os = System.getProperty("os.name").toLowerCase();
        try {
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
                new ProcessBuilder("bash", "-c", "clear").inheritIO().start().waitFor();
            } else {
                System.out.println("Console clearing not supported on this operating system.");
            }
        }
            catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public void onTick() {
        System.out.print("Enter your move: ");
        String move = scanner.nextLine();
        raiseEvent(move);
        
    }

    
}

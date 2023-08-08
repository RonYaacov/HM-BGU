package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.company.Interfaces.TickListener;
import com.company.Interfaces.UIEvent;
import com.company.Interfaces.UIListener;

public class UI implements UIEvent, TickListener {
    
    private List<UIListener> UIlisteners;

    public UI(){
        
        UIlisteners = new ArrayList<UIListener>();
    }

    public void getPlayerType(){
        System.out.print("Enter Player Name: ");
        Scanner scanner = new Scanner(System.in);
        String playerName = scanner.nextLine();
        scanner.close();
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

    @Override
    public void onTick() {
        System.out.println("Enter your move: ");
        Scanner scanner = new Scanner(System.in);
        String move = scanner.nextLine();
        scanner.close();
        raiseEvent(move);
    }

    
}

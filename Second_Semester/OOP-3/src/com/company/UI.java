package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.company.Interfaces.UIEvent;
import com.company.Interfaces.UIListener;

public class UI implements UIEvent {
    private Board board;
    private List<UIListener> UIlisteners;

    public UI(){
        
        UIlisteners = new ArrayList<UIListener>();
    }

    public void setBoard(Board board){
        this.board = board;
    }

    public void getPlayerType(){
        System.out.print("Enter Player Name: ");
        Scanner scanner = new Scanner(System.in);
        String playerName = scanner.nextLine();
        scanner.close();
        raiseEvent(playerName);
    }

    public void printBoard(){
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

    
}

package com.company;
import com.company.Interfaces.TickListener;
import java.util.ArrayList;
import java.util.List;
import com.company.Interfaces.TickEvent;

public class GameManeger implements TickEvent{
    private List<TickListener> Ticklisteners;
    private Board board;

    public GameManeger(String filePath , String PlayerName) {
        this.board = new Board(filePath, PlayerName);
        Ticklisteners = new ArrayList<TickListener>();
    }
        
    @Override
    public void Register(TickListener listener) {
        Ticklisteners.add(listener);
    }

    @Override
    public void tick() {
        for(TickListener listener : Ticklisteners)
            listener.tick();
    }

}
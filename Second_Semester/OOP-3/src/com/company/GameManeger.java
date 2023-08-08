package com.company;
import com.company.Interfaces.TickListener;
import com.company.Interfaces.UIListener;
import java.util.ArrayList;
import java.util.List;
import com.company.Interfaces.TickEvent;

public class GameManeger implements TickEvent, UIListener{
    private List<TickListener> Ticklisteners;
    private String filePath;
    private Board board;
    private Tile player;
    private TileFactory factory;

    public GameManeger(String filePath) {
        this.filePath = filePath;
        Ticklisteners = new ArrayList<TickListener>();
        this.factory = new TileFactory();
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

    @Override
    public void onUIEvent(String event) {
        this.player = factory.producePlayer(event);
        this.board = new Board(filePath, player);
        this.player.setBoard(board);
        
        
    }

}
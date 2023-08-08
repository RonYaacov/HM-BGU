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
    private boolean isRunning;
    private Tile player;
    private TileFactory factory;

    public GameManeger(String filePath) {
        this.filePath = filePath;
        Ticklisteners = new ArrayList<TickListener>();
        this.factory = new TileFactory();
        this.isRunning = false;    }

    public boolean isRunning() {
        return isRunning;
    }
    public void start(){
        isRunning = true;
        while(isRunning){
            tick();
        }
    }
        
    @Override
    public void Register(TickListener listener) {
        Ticklisteners.add(listener);
    }

    @Override
    public void tick() {
        for(TickListener listener : Ticklisteners)
            listener.onTick();
    }
    public Board getBoard(){
        return board;
    }

    @Override
    public void onUIEvent(String event) {
        if (!isRunning){
            this.player = factory.producePlayer(event);
            this.board = new Board(filePath, player);
            this.player.setBoard(board);
        }
        else{
            Position position = player.getPosition();
            Position newPosition = new Position(position.getX(), position.getY());
            switch (event) {
                case "w":
                    newPosition.setX(position.getX() - 1);
                    break;
                case "s":
                    newPosition.setX(position.getX() + 1);
                    break;
                case "a":
                    newPosition.setY(position.getY() - 1);
                    break;
                case "d":
                    newPosition.setY(position.getY() + 1);
                    break;
                default:
                    break;
            }
            Tile tile = board.getTile(newPosition);
            if(tile.isPassable()){
                board.setTile(position, new EmptyTile());
                board.setTile(newPosition, player);
                player.setPosition(newPosition);
            }
            else{
                System.out.println("Invalid move");
            }
        }
            
        
    }

}
import Interfaces.TickListener;
import java.util.ArrayList;
import java.util.List;

import Interfaces.TickEvent;

public class GameManeger implements TickEvent{
    private List<TickListener> Ticklisteners;


    public GameManeger() {
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
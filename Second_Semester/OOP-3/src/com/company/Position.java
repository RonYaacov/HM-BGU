package com.company;

import java.util.ArrayList;
import java.util.List;
import com.company.Interfaces.PosListener;
import com.company.Interfaces.PositionChangeEvent;

public class Position implements Comparable<Position>, PositionChangeEvent{
    private int x;
    private int y;
    private List<PosListener> posListeners;
    private Position prevPos;

    public Position(int x, int y){
        this.prevPos = null;
        this.posListeners = new ArrayList<PosListener>();
        this.x = x;
        this.y = y;
    }
    
    public int compareTo(Position position){
        if(this.y > position.y){
            return 1;
        }
        if(this.y == position.y){
            if(this.x > position.x){
                return 1;
            }
            if(this.x == position.x){
                return 0;
            }
        }
        return -1;
    }
    
    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }  
    public void setX(int x){
        prevPos = new Position(this.x, this.y);
        this.x = x;
        onPosChanged();
    }
    public void setY(int y){
        prevPos = new Position(this.x, this.y);
        this.y = y;
        onPosChanged();
    }

    public double range(Position position){
        return Math.sqrt(Math.pow(this.x - position.x, 2) + Math.pow(this.y - position.y, 2));
    }

    public void Register(PosListener listener){
        posListeners.add(listener);
    }

    @Override
    public void onPosChanged() {
        for(PosListener listener : posListeners){
            listener.posChanged(prevPos, this);
        }
    }

    
}

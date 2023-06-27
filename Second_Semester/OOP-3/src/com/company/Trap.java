package com.company;


import com.company.Interfaces.TickListener;

public class Trap implements TickListener {
    private int visibilityTime;
    private int invisibilityTime;
    private int ticksCount;
    private boolean isVisible;

    public Trap(int visibilityTime, int invisibilityTime){
        this.visibilityTime = visibilityTime;
        this.invisibilityTime = invisibilityTime;
        this.ticksCount = 0;
        this.isVisible = true;
    }

    @Override
    public void tick() {
        ticksCount++;
        if(ticksCount == visibilityTime){
            isVisible = false;
        }
        else if(ticksCount == visibilityTime + invisibilityTime){
            isVisible = true;
            ticksCount = 0;
        }
    }
}

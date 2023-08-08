package com.company;
import com.company.Interfaces.TickListener;

public class Trap extends Enemy implements TickListener {

    private int visibilityTime;
    private int invisibilityTime;
    private int ticksCount;
    private boolean isVisible;

    public Trap(char tile, String name, Health health, int attackPoints, int defencePoints, int experiencePoints, int visibilityTime, int invisibilityTime){
        super(tile, name, health, attackPoints, defencePoints, experiencePoints);
        this.visibilityTime = visibilityTime;
        this.invisibilityTime = invisibilityTime;
    }

    @Override
    public void onTick() {
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

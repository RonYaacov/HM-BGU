package com.company;

import java.beans.Visibility;

public class Trap extends Enemy {
    private int visibilityTime;
    private int invisibilityTime;
    private int ticksCount;
    private boolean isVisible;

    public Trap(char tile, String name, Health health, int attackPoints, int defencePoints, int experiencePoints, int visibilityTime, int invisibilityTime){
        super(tile, name, health, attackPoints, defencePoints, experiencePoints);
        this.visibilityTime = visibilityTime;
        this.invisibilityTime = invisibilityTime;
    }
    
    

}

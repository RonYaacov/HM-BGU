package com.company;

import java.beans.Visibility;

public class Trap {
    private int visibilityTime;
    private int invisibilityTime;
    private int ticksCount;
    private boolean isVisible;

    public Trap(int visibilityTime, int invisibilityTime) {
        this.visibilityTime = visibilityTime;
        this.invisibilityTime = invisibilityTime;
        this.ticksCount = 0;
        this.isVisible = true;
    }
    
    

}

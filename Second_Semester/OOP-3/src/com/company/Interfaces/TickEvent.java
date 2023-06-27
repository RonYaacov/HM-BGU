package com.company.Interfaces;

public interface TickEvent {
    void Register(TickListener listener);
    void tick();
    
}

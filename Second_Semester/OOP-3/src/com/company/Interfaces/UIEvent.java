package com.company.Interfaces;

public interface UIEvent {
    void Register(UIListener listener);
    void raiseEvent(String event);
    
}

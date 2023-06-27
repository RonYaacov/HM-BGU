package com.company.Interfaces;

public interface PositionChangeEvent {
    void onPosChanged();
    void Register(PosListener listener);
}

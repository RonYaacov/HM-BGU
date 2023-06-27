package com.company.Interfaces;

import com.company.Position;

public interface PosListener {
    void posChanged(Position prevPos, Position newPos);
}

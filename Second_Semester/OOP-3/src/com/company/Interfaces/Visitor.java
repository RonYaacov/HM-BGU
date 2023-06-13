package com.company.Interfaces;

import com.company.Player;

public interface Visitor {
    int visitBattle(Player player);
    int visitBattle(Enemy enemy);
    
}

package com.company.Interfaces;

import com.company.Enemy;
import com.company.Player;

public interface Visitor {
    void visitBattle(Player player);
    void visitBattle(Enemy enemy);
    
}

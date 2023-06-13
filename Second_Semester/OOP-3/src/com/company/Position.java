package com.company;

public class Position implements Comparable<Position>{
    private int x;
    private int y;

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    public int compareTo(Position position){
        if(this.y > position.y){
            return 1;
        }
        if(this.y == position.y){
            if(this.x > position.x){
                return 1;
            }
            if(this.x == position.x){
                return 0;
            }
        }
        return -1;
    }
    
    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }  
    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }
    public double range(Position position){
        return Math.sqrt(Math.pow(this.x - position.x, 2) + Math.pow(this.y - position.y, 2));
    }

}

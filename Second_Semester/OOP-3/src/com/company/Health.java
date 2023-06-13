package com.company;

public class Health {
    private int healthPool;
    private int healtAmount;
    
    public Health(int healthPoll, int healtAmount){
        this.healtAmount = healtAmount;
        this.healthPool = healthPoll;
    } 
    public int getHealthPool(){
        return healthPool;
    }

    public int getHealthAmount(){
        return healtAmount;
    }

    public void setHealthPool(int healthPoll){
        this.healthPool = healthPoll;
    }

    public void setHealthAmount(int healtAmount){
        this.healtAmount = healtAmount;
    }

    public void increaseHealthPoll(int healthPoll){
        this.healthPool += healthPoll;
    }
    
}

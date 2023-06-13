package com.company;

public class Health {
    private int healthPoll;
    private int healtAmount;
    
    public Health(int healthPoll, int healtAmount){
        this.healtAmount = healtAmount;
        this.healthPoll = healthPoll;
    } 
    public int getHealthPoll(){
        return healthPoll;
    }
    public int getHealthAmount(){
        return healtAmount;
    }
    public void setHealthPoll(int healthPoll){
        this.healthPoll = healthPoll;
    }
    public void setHealthAmount(int healtAmount){
        this.healtAmount = healtAmount;
    }
    public void increaseHealthPoll(int healthPoll){
        this.healthPoll += healthPoll;
    }
    
}

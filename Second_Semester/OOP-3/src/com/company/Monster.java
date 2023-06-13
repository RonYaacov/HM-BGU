package com.company;

public class Monster extends Enemy {
    private int visionRange;

    public Monster(char tile, String name, Health health, int attackPoints, int defencePoints, int experienceReword, int visionRange) {
        super(tile, name, health, attackPoints, defencePoints, experienceReword);
        this.visionRange = visionRange;
    }
    
    public void move(Player player){
        if(this.position.range(player.position) < visionRange){
            int dx = this.position.getX() - player.position.getX();
            int dy = this.position.getY() - player.position.getY();
            if(Math.abs(dx) > Math.abs(dy)){
                if(dx > 0){
                    moveLeft();
                    return;
                }
                moveRight();
                return;
            }
            if(dy > 0){
                moveUp();
                return;
            }
            moveDown();
            return;
        }        
        double randMove = rand.nextDouble();
        if(randMove < 0.25){
            moveUp();
            return;
        }
        if(randMove < 0.5){
            moveDown();
            return;
        }
        if(randMove < 0.75){
            moveLeft();
            return;
        }
        moveRight();  
    }
}

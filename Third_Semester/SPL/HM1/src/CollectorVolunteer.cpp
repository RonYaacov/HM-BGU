#include "../include/CollectorVolunteer.h" 
#include "../include/Volunteer.h"
#include "../include/Order.h"
#include <iostream>
#include <string>

CollectorVolunteer::CollectorVolunteer(int id, std::string name, int coolDown): Volunteer(id, name){
    this->coolDown = coolDown;
    this->timeLeft = nullptr;
    this->activeOrderId = nullptr; // it should be initialized to NO_ORDER i dont know what is it need to check
    this->completedOrderId = nullptr; // it should be initialized to NO_ORDER i dont know what is it need to check
}

CollectorVolunteer::CollectorVolunteer(const CollectorVolunteer& other){
    this->id = other.id;
    this->name = other.name;
    this->coolDown = other.coolDown;
    this->timeLeft = other.timeLeft;
    this->activeOrderId = other.activeOrderId;
    this->completedOrderId = other.completedOrderId;
}

CollectorVolunteer* CollectorVolunteer::clone() const override{
    return new CollectorVolunteer(*this);
}

bool CollectorVolunteer::hasOrdersLeft() const override{
    return true;
}

bool CollectorVolunteer::canTakeOrder(const Order &order) const override{
    return !(this->isBusy() || order.getStatus() != OrderStatus::PENDING)
}

void CollectorVolunteer::acceptOrder(const Order &order) override{
    if(!this->canTakeOrder(order)){
        return;
    }
    this->activeOrderId = order.getId();
    this->timeLeft = this->coolDown;
    return;
}

void CollectorVolunteer::step() override{
    if(!this->isBusy()){
        return;
    }
    
    if(this->decreaseCoolDown()){
        this->completedOrderId = this->activeOrderId;
        this->activeOrderId = nullptr; //should be NO_ORDER insted of nullptr 
    }
}

bool CollectorVolunteer::decreaseCoolDown(){// can a time left be negative?
    if(this->timeLeft == 0){
        return true;//need to check if this is the right behavior if it allready 0 do i need to do nothing or throw an error?
    }
    this->timeLeft--;
    return this->timeLeft == 0;
}

std::string CollectorVolunteer::toString() const override{
    return "CollectorVolunteer named: "+this->name+" with id: "+this->id;
}

int CollectorVolunteer::getCoolDown() const{
    return this->coolDown;
}

int CollectorVolunteer::getTimeLeft() const{
    return this->timeLeft;
}






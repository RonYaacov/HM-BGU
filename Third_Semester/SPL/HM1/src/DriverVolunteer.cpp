#include "../include/DriverVolunteer.h" 
#include "../include/Volunteer.h"
#include "../include/Order.h"
#include <iostream>
#include <string>

DriverVolunteer::DriverVolunteer(int id, std::string name, int maxDistance, int distancePerStep): Volunteer(id, name){
    this->maxDistance = maxDistance;
    this->distancePerStep = distancePerStep;
    this->distanceLeft = 0;
    this->activeOrderId = nullptr; // it should be initialized to NO_ORDER i dont know what is it need to check
    this->completedOrderId = nullptr; // it should be initialized to NO_ORDER i dont know what is it need to check
}

DriverVolunteer::DriverVolunteer(const DriverVolunteer& other){
    this->id = other.id;
    this->name = other.name;
    this->maxDistance = other.maxDistance;
    this->distanceLeft = other.distanceLeft;
    this->distancePerStep = other.distancePerStep;
    this->activeOrderId = other.activeOrderId;
    this->completedOrderId = other.completedOrderId;
}

DriverVolunteer* DriverVolunteer::clone() const override{
    return new DriverVolunteer(*this);
}

bool DriverVolunteer::hasOrdersLeft() const override{
    return true;
}

bool DriverVolunteer::canTakeOrder(const Order &order) const override{
    return !(this->isBusy() || order.getStatus() != OrderStatus::PENDING || order.getDistance() > this->maxDistance)
}

int DriverVolunteer::getDistanceLeft() const{
    return this->distanceLeft;
}

int DriverVolunteer::getMaxDistance() const{
    return this->maxDistance;
}

int DriverVolunteer::getDistancePerStep() const{
    return this->distancePerStep;
}

bool DriverVolunteer::decreaseDistanceLeft(){
    if(this->distanceLeft == 0){
        return true;//need to check if this is the right behavior if it allready 0 do i need to do nothing or throw an error?
    }
    if(this->distanceLeft < this->distancePerStep){
        this->distanceLeft = 0;
        return true;
    }
    this->distanceLeft -= this->distancePerStep;
    return false;   
}

void DriverVolunteer::acceptOrder(const Order &order) override{
    if(!this->canTakeOrder(order)){
        return;
    }
    this->activeOrderId = order.getId();
    this->distanceLeft = order.getDistance();
}

void DriverVolunteer::step() override{
    if(!this->isBusy()){
        return;
    }
    
    if(this->decreaseDistanceLeft()){
        this->completedOrderId = this->activeOrderId;
        this->activeOrderId = nullptr; //should be NO_ORDER insted of nullptr 
    }
}

std::string DriverVolunteer::toString()const override{ // nned to check what is the right format
    return "DriverVolunteer named: "+ this->name + " with id: " + this->id +
    " with maxDistance: " + this->maxDistance + " with distancePerStep: " + this->distancePerStep +
    " with distanceLeft: " + this->distanceLeft;
}
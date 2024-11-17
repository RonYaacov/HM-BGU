#include "../include/Volunteer.h"
#include "../include/Order.h"
#include <iostream>
#include <string>

Volunteer::Volunteer(int id, std::string name){
    this->id = id;
    this->name = name;
    this->activeOrderId = nullptr; // it should be initialized to NO_ORDER i dont know what is it need to check
    this->completedOrderId = nullptr; // it should be initialized to NO_ORDER i dont know what is it need to check
}

Volunteer::Volunteer(const Volunteer& other){
    this->id = other.id;
    this->name = other.name;
    this->activeOrderId = other.activeOrderId;
    this->completedOrderId = other.completedOrderId;
}

Volunteer* Volunteer::clone() const override{
    return new Volunteer(*this);
}

int Volunteer::getId(){
    return this->id;
}

std::string& Volunteer::getName(){
    return this->name;
}

int Volunteer::getActiveOrderId(){
    return this->activeOrderId;
}

int Volunteer::getCompletedOrderId(){
    return this->completedOrderId;
}

bool Volunteer::isBusy(){
    if(this->activeOrderId == nullptr){ //should be NO_ORDER insted of nullptr
        return false;
    }
    return true;
}

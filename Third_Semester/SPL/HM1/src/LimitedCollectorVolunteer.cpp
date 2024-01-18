#include "../include/LimitedCollectorVolunteer.h" 
#include "../include/CollectorVolunteer.h" 
#include "../include/Volunteer.h"
#include "../include/Order.h"
#include <iostream>
#include <string>

LimitedCollectorVolunteer::LimitedCollectorVolunteer(int id, std::string name, int coolDown ,int maxOrders): CollectorVolunteer(id, name, coolDown){
    this->maxOrders = maxOrders;
    this->ordersLeft = maxOrders;
}

LimitedCollectorVolunteer::LimitedCollectorVolunteer(const LimitedCollectorVolunteer& other): CollectorVolunteer(other){
    this->maxOrders = other.maxOrders;
    this->ordersLeft = other.ordersLeft;
}

LimitedCollectorVolunteer* LimitedCollectorVolunteer::clone() const override{
    return new LimitedCollectorVolunteer(*this);
}

bool LimitedCollectorVolunteer::hasOrdersLeft(){
    return this->ordersLeft > 0;
}

bool LimitedCollectorVolunteer::canTakeOrder(const Order &order){
    if(!CollectorVolunteer::canTakeOrder(order)){
        return false;
    }
    if(!this->hasOrdersLeft()){
        return false;
    }
    return true;
} 

void LimitedCollectorVolunteer::acceptOrder(const Order &order){
    if(!this->canTakeOrder(order)){
        return;
    }
    CollectorVolunteer::acceptOrder(order);
    this->ordersLeft--;
}

int LimitedCollectorVolunteer::getMaxOrders()const{
    return this->maxOrders;
}

int LimitedCollectorVolunteer::getNumOrdersLeft()const{
    return this->ordersLeft;
}

std::string LimitedCollectorVolunteer::toString() const override{
    std::string str = "LimitedCollectorVolunteer: " + this->getName() + " (id: " + std::to_string(this->getId()) + "), ";
    str += "coolDown: " + std::to_string(this->getCoolDown()) + ", ";
    str += "maxOrders: " + std::to_string(this->getMaxOrders()) + ", ";
    str += "ordersLeft: " + std::to_string(this->getNumOrdersLeft());
    return str;
}

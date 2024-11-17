#include "../include/DriverVolunteer.h" 
#include "../include/Volunteer.h"
#include "../include/Order.h"
#include <iostream>
#include <string>

LimitedCollectorVolunteer::LimitedCollectorVolunteer(int id, const std::string &name,
                                                     int maxDistance, int distancePerStep, 
                                                     int maxOrders):DriverVolunteer(id, name, 
                                                                    maxDistance, distancePerStep){
    this->maxOrders = maxOrders;
    this->ordersLeft = maxOrders;
}

LimitedCollectorVolunteer::LimitedCollectorVolunteer(const LimitedCollectorVolunteer& other): DriverVolunteer(other){
    this->maxOrders = other.maxOrders;
    this->ordersLeft = other.ordersLeft;
}

LimitedCollectorVolunteer* LimitedCollectorVolunteer::clone() const{
    return new LimitedCollectorVolunteer(*this);
}

int LimitedCollectorVolunteer::getMaxOrders()const{
    return this->maxOrders;
}

int LimitedCollectorVolunteer::getNumOrdersLeft()const{
    return this->ordersLeft;
}

bool LimitedCollectorVolunteer::hasOrdersLeft() const{
    return this->getNumOrdersLeft() > 0;
}

bool LimitedCollectorVolunteer::canTakeOrder(const Order& order)const{
    return !(!DriverVolunteer::canTakeOrder(order) || !this->hasOrdersLeft())
}

void LimitedCollectorVolunteer::acceptOrder(const Order& order){
    if(!this->canTakeOrder(order)){
        return;
    }
    DriverVolunteer::acceptOrder(order);
    this->ordersLeft--;
}

std::string LimitedCollectorVolunteer::toString() const{// need to check if this is the right format
    std::string str = "LimitedCollectorVolunteer: " + this->getName() + " (id: " + std::to_string(this->getId()) + "), ";
    str += "maxDistance: " + std::to_string(this->getMaxDistance()) + ", ";
    str += "distancePerStep: " + std::to_string(this->getDistancePerStep()) + ", ";
    str += "maxOrders: " + std::to_string(this->getMaxOrders()) + ", ";
    str += "ordersLeft: " + std::to_string(this->getNumOrdersLeft());
    return str;
}
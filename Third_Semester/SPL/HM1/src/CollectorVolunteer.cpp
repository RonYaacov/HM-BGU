#include "../include/CollectorVolunteer.h" 
#include "../include/Volunteer.h"
#include "../include/Order.h"
#include <iostream>
#include <string>

CollectorVolunteer::CollectorVolunteer(int id, std::string name, int coolDown){
    this->id = id;
    this->name = name;
    this->coolDown = coolDown;
    
}
public class Employee {
    int _vacationDaysTaken;
    int _sickLeaveDaysTaken;
    int years;
    int startYear;

    public Employee(int startYear, int _vacationDaysTaken, int _sickLeaveDaysTaken) {
        this.startYear = startYear;
        this.years = getYears(startYear);
    }

    public double computeDueTerminationPayment() {
        double payment = 0.0;
        double salary = 0.0;
        double vacationValue = getVacationValue();
        for (int i = 0; i < years; i++)
        payment += profit(payment, startYear + i);
    
        for(int i = startYear; i < startYear + years; i++)
        salary += getAvgYearPayment(i) * (1.0 - getTaxRate(i));
    
        return salary + payment + vacationValue;
    }

    private double getVacationValue(){
        double bonusDays = 0.0;
        int vacationDays;
        if (years < 5){
            vacationDays = 12 * years - _vacationDaysTaken;
        }
        else{
            vacationDays = 60+(years-5)*(12+(years-5)*2)-_vacationDaysTaken;
        }
    
        if (_sickLeaveDaysTaken < years * 2){
            bonusDays = years * 0.5;
        }
        return (vacationDays + bonusDays) * dayOffPayRate();
    }
}
package classes;

import java.util.Date;

public abstract class staff extends person{
    int salary;
    Date hire_date;

    public void setHire_date(Date hire_date) {
        this.hire_date = hire_date;
    }

    public Date getHire_date() {
        return hire_date;
    }

    public int getSalary() {
        return salary;
    }
}
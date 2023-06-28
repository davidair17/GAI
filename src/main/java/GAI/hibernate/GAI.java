package GAI.hibernate;

import java.util.ArrayList;

public class GAI {
    private ArrayList<Driver> Drivers;
    private ArrayList<Car> Cars;

    public ArrayList<Driver> getDriver(){
        return this.Drivers;
    }
    public ArrayList<Car> getCar(){
        return this.Cars;
    }

    public void setDriver(ArrayList<Driver> Drivers){
        this.Drivers = Drivers;
    }

    public void setCar(ArrayList<Car> Cars) {
        this.Cars = Cars;
    }
}

package GAI.hibernate;

import org.hibernate.annotations.Cascade;

import javax.lang.model.element.Name;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "Car")
public final class Car {
    @Id
    @Column(name = "idCar")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCar;
    @Column(name = "Maintenance")
    private LocalDate Maintenance;
    @Column(name = "Car_plate")
    private String Car_plate;

    @ManyToOne
    @JoinColumn(name = "driverid")
    private Driver driver;

    public int getIdCar() {
        return idCar;
    }

    public void setIdCar(int idCar) {
        this.idCar = idCar;
    }

    public LocalDate getMaintenance() {
        return Maintenance;
    }

    public void setMaintenance(LocalDate maintenance) {
        Maintenance = maintenance;
    }

    public String getCar_plate() {
        return Car_plate;
    }

    public void setCar_plate(String car_plate) {
        Car_plate = car_plate;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    @Override
    public String toString() {
        return String.format("%d %s %s %s",
                this.idCar, this.Maintenance, this.Car_plate, this.driver);
    }
}

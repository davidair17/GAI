package GAI.hibernate;

import net.bytebuddy.asm.Advice;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "Driver")
public final class Driver {
    @Id
    @Column(name = "idDriver")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idDriver;
    @Column(name = "Name")
    private String Name;
    @Column(name = "Dateofbirth")
    private LocalDate Dateofbirth;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private Set<Car> Carlist;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private Set<Violation> Violationlist;

    public Driver(String name) {
    }

    public Driver() {

    }

    public Driver(int id, String name, LocalDate date) {
    }

    public int getIdDriver() {
        return idDriver;
    }

    public void setIdDriver(int idDriver) {
        this.idDriver = idDriver;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public LocalDate getDateofbirth() {
        return Dateofbirth;
    }

    public void setDateofbirth(LocalDate dateofbirth) {
        Dateofbirth = dateofbirth;
    }

    public Set<Car> getCarlist() {
        return Carlist;
    }

    public void setCarlist(Set<Car> carlist) {
        Carlist = carlist;
    }

    public Set<Violation> getViolationlist() {
        return Violationlist;
    }

    public void setViolationlist(Set<Violation> violationlist) {
        Violationlist = violationlist;
    }

    @Override
    public String toString() {
        return this.idDriver + " " + this.Name + " " + this.Dateofbirth;
    }
    public static Driver valueOf(String driverString) {
        String[] parts = driverString.split(" ");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid driver string format");
        }

        String name = parts[1] + " " + parts[2];
        int Id = Integer.parseInt(parts[0]);
        LocalDate date = LocalDate.parse(parts[3]);

        Driver driver = new Driver();
        driver.setIdDriver(Id);
        driver.setName(name);
        driver.setDateofbirth(date);

        System.out.println(name);
        System.out.println(Id);
        System.out.println(date);
        

        return driver;
    }
}

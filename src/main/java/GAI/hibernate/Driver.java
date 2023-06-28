package GAI.hibernate;

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
        return String.format("%d %s %s",
                this.idDriver, this.Name, this.Dateofbirth);
    }
}
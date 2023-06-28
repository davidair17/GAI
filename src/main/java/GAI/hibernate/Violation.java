package GAI.hibernate;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "Violation")
public final class Violation {
    @Id
    @Column(name = "idViolation")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idViolation;
    @Column(name = "Type")
    private String Type;
    @Column(name = "Penalty")
    private String Penalty;
    @Column(name = "Date")
    private LocalDate Date;

    @ManyToOne
    @JoinColumn(name = "driverid")
    private Driver driver;

    public int getIdViolation() {
        return idViolation;
    }

    public void setIdViolation(int idViolation) {
        this.idViolation = idViolation;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getPenalty() {
        return Penalty;
    }

    public void setPenalty(String penalty) {
        Penalty = penalty;
    }

    public LocalDate getDate() {
        return Date;
    }

    public void setDate(LocalDate date) {
        Date = date;
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
                this.idViolation, this.Type, this.Penalty, this.Date);
    }
}
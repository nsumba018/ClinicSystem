package kigali.clinic.rw.domain;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="patient")
@Getter
@Setter
@NoArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="date_of_birth")
    private Date dateOfBirth;

    @Column(name= "ss_number", unique=true)
    private String ssNumber;

    @JsonIgnore
    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments = new ArrayList<>();
}

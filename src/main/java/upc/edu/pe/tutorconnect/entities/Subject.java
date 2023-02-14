package upc.edu.pe.tutorconnect.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="TB_SUBJECT")
@Data
@EqualsAndHashCode
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany(mappedBy = "subjects")
    private List<Tutor> tutors = new ArrayList<>();
}

package upc.edu.pe.tutorconnect.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import upc.edu.pe.tutorconnect.entities.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TutorDTO {

    private Long id;
    private String descripcion;
    private double pricePerHour;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private User userDTO;
}

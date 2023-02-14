package upc.edu.pe.tutorconnect.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import upc.edu.pe.tutorconnect.entities.Tutor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectDTO {
    private Long id;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TutorDTO> tutorsDTO = new ArrayList<>();

}

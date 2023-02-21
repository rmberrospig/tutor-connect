package upc.edu.pe.tutorconnect.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TutorDTO {
    private Long id;
    private String description;
    private double pricePerHour;

    private String productImageUrl;

    private String profileImageUrl;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserDTO userDTO;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<SubjectDTO> subjectsDTO = new ArrayList<>();
}

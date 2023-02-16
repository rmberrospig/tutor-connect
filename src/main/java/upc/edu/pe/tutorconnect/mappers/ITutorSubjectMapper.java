package upc.edu.pe.tutorconnect.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import upc.edu.pe.tutorconnect.dtos.SubjectDTO;
import upc.edu.pe.tutorconnect.dtos.TutorDTO;
import upc.edu.pe.tutorconnect.dtos.UserDTO;
import upc.edu.pe.tutorconnect.entities.Subject;
import upc.edu.pe.tutorconnect.entities.Tutor;
import upc.edu.pe.tutorconnect.entities.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ITutorSubjectMapper {

    List<SubjectDTO> getSubjectsDTO(List<Subject> subjects);

    List<TutorDTO> getTutorsDTO(List<Tutor> tutors);

    @Mapping(source = "user", target = "userDTO")
    TutorDTO toDTO(Tutor target);

    @Mapping(source = "userType", target = "userTypeDTO")
    UserDTO toDTO(User target);


}

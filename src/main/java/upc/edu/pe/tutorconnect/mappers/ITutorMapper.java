package upc.edu.pe.tutorconnect.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import upc.edu.pe.tutorconnect.dtos.TutorDTO;
import upc.edu.pe.tutorconnect.entities.Tutor;


@Mapper(componentModel = "spring")
public interface ITutorMapper extends ITutorSubjectMapper {

    @Mapping(source = "userDTO", target = "user")
    Tutor toEntity(TutorDTO source);

    @Mapping(target = "subjectsDTO", expression = "java(getSubjectsDTO(target.getSubjects()))")
    @Mapping(source = "user", target = "userDTO")
    TutorDTO toDTO(Tutor target);



}

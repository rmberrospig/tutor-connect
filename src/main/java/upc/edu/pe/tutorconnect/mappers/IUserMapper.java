package upc.edu.pe.tutorconnect.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import upc.edu.pe.tutorconnect.dtos.UserDTO;
import upc.edu.pe.tutorconnect.entities.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IUserMapper {

    @Mapping(source = "userTypeDTO", target = "userType")
    @Mapping(source = "tutorDTO", target = "tutor")
    User toEntity(UserDTO source);

    @Mapping(source = "userType", target = "userTypeDTO")
    @Mapping(source = "tutor", target = "tutorDTO")
    UserDTO toDTO(User target);

    List<UserDTO> getUsersDTO(List<User> users);
}

package upc.edu.pe.tutorconnect.mappers;

import org.mapstruct.Mapper;
import upc.edu.pe.tutorconnect.dtos.UserDTO;
import upc.edu.pe.tutorconnect.entities.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IUserMapper {

    User toEntity(UserDTO source);

    UserDTO toDTO(User target);

    List<UserDTO> getUsersDTO(List<User> users);
}

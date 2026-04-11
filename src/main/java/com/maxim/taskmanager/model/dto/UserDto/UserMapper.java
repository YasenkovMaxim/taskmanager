package com.maxim.taskmanager.model.dto.UserDto;


import com.maxim.taskmanager.model.entity.User;

public class UserMapper {


    public static UserResponseDto toResponseDto(User user) {
        if (user == null) {
            return null;
        }
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setAge(user.getAge());
        return dto;
    }

    public static User toEntity(UserCreateDto dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
        user.setPassword(dto.getPassword());
        return user;
    }

    public static void updateEntity(UserUpdateDto dto, User user) {
        if(dto.getFirstName() !=null){
            user.setFirstName(dto.getFirstName());
        }
        if(dto.getLastName() !=null) {
            user.setLastName(dto.getLastName());
        }
       if (dto.getEmail() !=null) {
           user.setEmail(dto.getEmail());
       }

       if (dto.getAge() !=null) {
           user.setAge(dto.getAge());
       }
        if (dto.getPassword() !=null) {
            user.setPassword(dto.getPassword());
        }
    }
}
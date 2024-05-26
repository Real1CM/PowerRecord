package com.main.wave.dto.mapper;

import com.main.wave.dto.request.user.RegisterRequest;
import com.main.wave.dto.request.user.UpdateUserRequest;
import com.main.wave.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "name",target = "name")
    @Mapping(source = "password",target = "password")
    User registerRequestToUser(RegisterRequest request);


    @Mapping(source = "name",target = "name")
    @Mapping(source = "password",target = "password")
    @Mapping(source = "id",target = "id")
    User updateRequestToUser(UpdateUserRequest request);
}

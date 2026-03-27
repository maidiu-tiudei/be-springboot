package studylearn.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import studylearn.demo.dto.request.PermissionRequest;
import studylearn.demo.dto.request.StudentCreationRequest;
import studylearn.demo.dto.request.StudentUpdateRequest;
import studylearn.demo.dto.response.PermissionResponse;
import studylearn.demo.dto.response.StudentResponse;
import studylearn.demo.entity.Permission;
import studylearn.demo.entity.Student;

import java.util.List;

@Mapper(componentModel="spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}

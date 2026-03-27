package studylearn.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import studylearn.demo.dto.request.PermissionRequest;
import studylearn.demo.dto.request.RoleRequest;
import studylearn.demo.dto.response.PermissionResponse;
import studylearn.demo.dto.response.RoleResponse;
import studylearn.demo.entity.Permission;
import studylearn.demo.entity.Role;

@Mapper(componentModel="spring")
public interface RoleMapper {
    @Mapping(target="permissions",ignore=true)
   Role toRole(RoleRequest request);
   RoleResponse toRoleResponse(Role role);
}

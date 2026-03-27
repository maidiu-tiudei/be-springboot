package studylearn.demo.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import studylearn.demo.dto.request.ApiResponse;
import studylearn.demo.dto.request.PermissionRequest;
import studylearn.demo.dto.request.RoleRequest;
import studylearn.demo.dto.response.PermissionResponse;
import studylearn.demo.dto.response.RoleResponse;
import studylearn.demo.service.PermissionService;
import studylearn.demo.service.RoleService;

import java.util.List;


@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal=true)
@Slf4j
public class RoleController {
    RoleService roleService;
    @PostMapping
    ApiResponse<RoleResponse>create(@RequestBody RoleRequest request){
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }
    @GetMapping
    ApiResponse<List<RoleResponse>>getAll(){
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }
    @DeleteMapping("/{roles}")
    ApiResponse<Void> delete(@PathVariable String roles){
        roleService.delete(roles);
        return ApiResponse.<Void>builder().build();
    }

}

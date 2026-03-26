package studylearn.demo.controller;

import jakarta.validation.Valid;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import studylearn.demo.dto.request.ApiResponse;
import studylearn.demo.dto.request.StudentCreationRequest;
import studylearn.demo.dto.request.StudentUpdateRequest;
import studylearn.demo.dto.response.StudentResponse;
import studylearn.demo.entity.Student;
import studylearn.demo.service.StudentService;

import java.util.List;


import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Builder
@Slf4j
@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;
    @PostMapping
    ApiResponse<Student> creatStudent(@RequestBody @Valid StudentCreationRequest request){
        ApiResponse<Student>apiResponse= new ApiResponse<>();
        apiResponse.setResult(studentService.createStudent(request));
        return apiResponse;
    }
    @GetMapping
    ApiResponse<List<StudentResponse>> getStudents(){
        var authentication= SecurityContextHolder.getContext().getAuthentication();
        log.info("Username:{}",authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        return ApiResponse.<List<StudentResponse>>builder()
                .result(studentService.getStudents())
                .build();
    }
    @GetMapping("/{studentId}")
    ApiResponse<StudentResponse> getStudent(@PathVariable("studentId") String studentId){
        return ApiResponse.<StudentResponse>builder()
                .result(studentService.getStudent(studentId))
                .build();
    }
    @GetMapping("/my-info")
    ApiResponse<StudentResponse> getMyInfo(){
        return ApiResponse.<StudentResponse>builder()
                .result(studentService.getMyInfo())
                .build();
    }
    @PutMapping("/{studentId}")
    StudentResponse updateStudent(@PathVariable String studentId,@RequestBody StudentUpdateRequest request ){
        return studentService.updateStudent(studentId,request);
    }
    @DeleteMapping("/{studentId}")
    String deleteStudent(@PathVariable String studentId){
        studentService.deleteStudent(studentId);
        return "User has been deleted";
    }
}

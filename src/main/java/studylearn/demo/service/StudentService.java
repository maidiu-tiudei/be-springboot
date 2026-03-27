package studylearn.demo.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import studylearn.demo.dto.request.StudentCreationRequest;
import studylearn.demo.dto.request.StudentUpdateRequest;
import studylearn.demo.dto.response.StudentResponse;
import studylearn.demo.entity.Student;
import studylearn.demo.enums.Role;
import studylearn.demo.exception.AppException;
import studylearn.demo.exception.ErrorCode;
import studylearn.demo.mapper.StudentMapper;
import studylearn.demo.repository.RoleRepository;
import studylearn.demo.repository.StudentRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level= AccessLevel.PRIVATE,makeFinal=true)
public class StudentService {
    StudentRepository studentRepository;
    RoleRepository roleRepository;
    StudentMapper studentMapper;
    PasswordEncoder passwordEncoder;
    public Student createStudent(StudentCreationRequest request){
        if(studentRepository.existsByUserName(request.getUserName()))
            throw new AppException(ErrorCode.USER_EXISTED);
        Student student= studentMapper.toStudent(request);
       // PasswordEncoder passwordEncoder= new BCryptPasswordEncoder(10);
        student.setPassWord(passwordEncoder.encode(request.getPassWord()));
        HashSet<String> roles=new HashSet<>();
        roles.add(Role.USER.name());
        //student.setRoles(roles);
        return studentRepository.save(student);
    }
    public StudentResponse getMyInfo(){
        var context= SecurityContextHolder.getContext();
        String name= context.getAuthentication().getName();
        Student student =studentRepository.findByUserName(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return studentMapper.toStudentResponse(student);
    }
    public StudentResponse updateStudent(String studentId, StudentUpdateRequest request){
        Student student=studentRepository.findById(studentId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        studentMapper.updateStudent(student,request);
        student.setPassWord(passwordEncoder.encode(request.getPassWord()));
        var roles=roleRepository.findAllById(request.getRoles());
        student.setRoles(new HashSet<>(roles));
        return studentMapper.toStudentResponse(studentRepository.save(student));
    }
    public void deleteStudent(String studentId){
        studentRepository.deleteById(studentId);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<StudentResponse> getStudents(){
        log.info("In method get Students");
        return studentRepository.findAll().stream()
            .map(studentMapper::toStudentResponse).toList();
    }
    @PostAuthorize("returnObject.userName==authentication.name")
    public StudentResponse getStudent(String id){
        log.info("In method get user by Id");
        return studentMapper.toStudentResponse(studentRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }
}

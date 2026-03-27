package studylearn.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import studylearn.demo.dto.request.StudentCreationRequest;
import studylearn.demo.dto.request.StudentUpdateRequest;
import studylearn.demo.dto.response.StudentResponse;
import studylearn.demo.entity.Student;

import java.util.List;

@Mapper(componentModel="spring")
public interface StudentMapper {
    Student toStudent(StudentCreationRequest request);
    StudentResponse toStudentResponse(Student student);
    //List<StudentResponse> toStudentResponseList(List<Student>students);
    @Mapping(target="roles",ignore=true)
    void updateStudent(@MappingTarget Student student, StudentUpdateRequest request);
}

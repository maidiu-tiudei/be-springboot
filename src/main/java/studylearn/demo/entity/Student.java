package studylearn.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;


@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
//@Table(name="students")
public class Student {
    @Id
    String studentId;
    String userName;
    String className ;
    String major;
    String faculty;
    String email;
    String phoneNumber;
    String address;
    String passWord;
    Set<String>roles;
}

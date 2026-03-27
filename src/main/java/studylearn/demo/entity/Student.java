package studylearn.demo.entity;

import jakarta.persistence.*;
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
    @ManyToMany(fetch = FetchType.EAGER)
    Set<Role>roles;
}

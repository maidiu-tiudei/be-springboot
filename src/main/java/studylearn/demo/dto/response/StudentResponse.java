package studylearn.demo.dto.response;

import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentResponse {
    @Id
    String studentId;
    String userName;
    String className ;
    String major;
    String faculty;
    String email;
    String phoneNumber;
    String address;
//    String passWord;
    Set<String> roles;
}

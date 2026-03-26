package studylearn.demo.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Teacher {
    String email;
    String phoneNumber;
    String dob;
    String faculty;
    String email;
}

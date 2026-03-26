package studylearn.demo.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import studylearn.demo.entity.Student;
import studylearn.demo.enums.Role;
import studylearn.demo.repository.StudentRepository;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE,makeFinal=true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;
    @Bean
    ApplicationRunner applicationRunner(StudentRepository studentRepository){
        return args->{
           if(studentRepository.findByUserName("admin").isEmpty()){
               var roles=new HashSet<String>();
               roles.add(Role.ADMIN.name());
               Student student= Student.builder()
                       .userName("admin")
                       .studentId("ADMIN_001")
                       .passWord(passwordEncoder.encode("admin"))
                       .roles(roles)
                       .build();
               studentRepository.save(student);
               log.warn("admin user has been created with default password: admin, please change it");
           }
        };
    }
}

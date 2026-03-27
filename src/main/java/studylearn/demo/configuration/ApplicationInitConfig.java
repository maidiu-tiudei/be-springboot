package studylearn.demo.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import studylearn.demo.entity.Student;
import studylearn.demo.repository.RoleRepository;
import studylearn.demo.repository.StudentRepository;
import studylearn.demo.repository.PermissionRepository;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal=true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(
            StudentRepository studentRepository,
            RoleRepository roleRepository,
            PermissionRepository permissionRepository) { // Thêm 2 repo này
        return args -> {
            if(studentRepository.findByUserName("admin").isEmpty()){

                // 1. Lấy hoặc tạo Role ADMIN thật từ DB
                var adminRole = roleRepository.findById("ADMIN").orElseGet(() ->
                        roleRepository.save(studylearn.demo.entity.Role.builder()
                                .name("ADMIN")
                                .description("Administrator role")
                                .build())
                );

                // 2. Gán Role vào tập hợp
                var roles = new HashSet<studylearn.demo.entity.Role>();
                roles.add(adminRole);

                // 3. Tạo Student admin
                Student student = Student.builder()
                        .userName("admin")
                        .studentId("ADMIN_001")
                        .passWord(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .build();

                studentRepository.save(student);
                log.warn("Admin user created with Role: ADMIN");
            }
        };
    }
}
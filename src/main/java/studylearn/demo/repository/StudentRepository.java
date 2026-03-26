package studylearn.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studylearn.demo.entity.Student;

import java.util.Optional;


@Repository
public interface StudentRepository extends JpaRepository<Student,String> {
    boolean existsByUserName(String username);
    Optional<Student> findByUserName(String userName);
}

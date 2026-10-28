package com.virtuallab.backend.repository;
import com.virtuallab.backend.entity.Submission;import com.virtuallab.backend.entity.User;import org.springframework.data.jpa.repository.JpaRepository;import java.util.List;
public interface SubmissionRepository extends JpaRepository<Submission,Long>{List<Submission> findAllByOrderBySubmittedAtDesc();List<Submission> findByStudentOrderBySubmittedAtDesc(User student);}

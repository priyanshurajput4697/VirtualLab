package com.virtuallab.backend.controller;

import com.virtuallab.backend.entity.Assignment;
import com.virtuallab.backend.entity.User;
import com.virtuallab.backend.repository.AssignmentRepository;
import com.virtuallab.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {
    private final AssignmentRepository assignments;
    private final UserRepository users;

    public AssignmentController(AssignmentRepository assignments, UserRepository users) {
        this.assignments = assignments;
        this.users = users;
    }

    @GetMapping
    public List<Map<String, Object>> listAssignments() {
        return assignments.findAllByOrderByCreatedAtDesc().stream().map(this::toResponse).toList();
    }

    @PostMapping
    public ResponseEntity<?> createAssignment(@RequestBody Map<String, String> body, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Please log in again."));
        }
        User teacher = users.findByEmail(authentication.getName()).orElse(null);
        if (teacher == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "User not found."));
        if (teacher.getRole() != User.Role.TEACHER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Only teachers can create assignments."));
        }
        String title = body.getOrDefault("title", "").trim();
        String description = body.getOrDefault("description", "").trim();
        if (title.isEmpty() || description.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Title and description are required."));
        }
        Assignment assignment = new Assignment();
        assignment.setTitle(title);
        assignment.setDescription(description);
        assignment.setDueDate(body.getOrDefault("dueDate", ""));
        assignment.setCreatedAt(LocalDateTime.now());
        assignment.setTeacher(teacher);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(assignments.save(assignment)));
    }

    private Map<String, Object> toResponse(Assignment assignment) {
        return Map.of(
            "id", assignment.getId(),
            "title", assignment.getTitle(),
            "description", assignment.getDescription(),
            "dueDate", assignment.getDueDate() == null ? "" : assignment.getDueDate(),
            "createdBy", assignment.getTeacher().getName(),
            "createdAt", assignment.getCreatedAt().toString()
        );
    }
}

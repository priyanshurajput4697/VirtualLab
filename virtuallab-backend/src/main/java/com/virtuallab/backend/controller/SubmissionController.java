package com.virtuallab.backend.controller;

import com.virtuallab.backend.entity.*;
import com.virtuallab.backend.repository.*;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {
 private final SubmissionRepository submissions;
 private final UserRepository users;
 private final AssignmentRepository assignments;
 public SubmissionController(SubmissionRepository s, UserRepository u, AssignmentRepository a){submissions=s;users=u;assignments=a;}

 @GetMapping public ResponseEntity<?> list(Authentication auth){
  User current=current(auth); if(current==null)return ResponseEntity.status(401).body(Map.of("message","Please log in again."));
  List<Submission> rows=current.getRole()==User.Role.TEACHER?submissions.findAllByOrderBySubmittedAtDesc():submissions.findByStudentOrderBySubmittedAtDesc(current);
  return ResponseEntity.ok(rows.stream().map(this::map).toList());
 }

 @PostMapping public ResponseEntity<?> create(@RequestBody Map<String,Object> body,Authentication auth){
  User student=current(auth);if(student==null)return ResponseEntity.status(401).body(Map.of("message","Please log in again."));
  if(student.getRole()!=User.Role.STUDENT)return ResponseEntity.status(403).body(Map.of("message","Only student accounts can submit code."));
  String language=str(body,"language"),code=str(body,"sourceCode");
  if(language.isBlank()||code.isBlank())return ResponseEntity.badRequest().body(Map.of("message","Language and source code are required."));
  Submission s=new Submission();s.setStudent(student);s.setLanguage(language);s.setSourceCode(code);s.setStdin(str(body,"stdin"));s.setOutput(str(body,"output"));s.setStatus(str(body,"status"));
  Object aid=body.get("assignmentId");if(aid instanceof Number n)assignments.findById(n.longValue()).ifPresent(s::setAssignment);
  s.setAssignmentTitle(str(body,"assignmentTitle"));return ResponseEntity.status(HttpStatus.CREATED).body(map(submissions.save(s)));
 }

 @PatchMapping("/{id}/grade") public ResponseEntity<?> grade(@PathVariable Long id,@RequestBody Map<String,Object> body,Authentication auth){
  User teacher=current(auth);if(teacher==null)return ResponseEntity.status(401).body(Map.of("message","Please log in again."));
  if(teacher.getRole()!=User.Role.TEACHER)return ResponseEntity.status(403).body(Map.of("message","Only teachers can grade submissions."));
  Optional<Submission> found=submissions.findById(id);if(found.isEmpty())return ResponseEntity.notFound().build();
  Object raw=body.get("marks");if(!(raw instanceof Number))return ResponseEntity.badRequest().body(Map.of("message","Marks must be a number from 0 to 10."));
  int marks=((Number)raw).intValue();if(marks<0||marks>10)return ResponseEntity.badRequest().body(Map.of("message","Marks must be between 0 and 10."));
  Submission s=found.get();s.setMarks(marks);s.setTeacherFeedback(str(body,"teacherFeedback").trim());s.setReviewedAt(LocalDateTime.now());return ResponseEntity.ok(map(submissions.save(s)));
 }

 private User current(Authentication a){if(a==null)return null;return users.findByEmail(a.getName()).orElse(null);}
 private String str(Map<String,Object>b,String k){Object v=b.get(k);return v==null?"":String.valueOf(v);}
 private Map<String,Object> map(Submission s){Map<String,Object>m=new LinkedHashMap<>();m.put("id",s.getId());m.put("assignmentId",s.getAssignment()==null?null:s.getAssignment().getId());m.put("assignmentTitle",s.getAssignmentTitle()==null?"Practice problem":s.getAssignmentTitle());m.put("studentName",s.getStudent().getName());m.put("studentEmail",s.getStudent().getEmail());m.put("language",s.getLanguage());m.put("sourceCode",s.getSourceCode());m.put("stdin",s.getStdin());m.put("output",s.getOutput());m.put("status",s.getStatus());m.put("submittedAt",s.getSubmittedAt().toString());m.put("marks",s.getMarks());m.put("teacherFeedback",s.getTeacherFeedback());m.put("reviewedAt",s.getReviewedAt()==null?null:s.getReviewedAt().toString());return m;}
}

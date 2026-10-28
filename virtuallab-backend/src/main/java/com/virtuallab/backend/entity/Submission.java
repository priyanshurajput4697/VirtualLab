package com.virtuallab.backend.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="submissions")
public class Submission {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="student_id",nullable=false) private User student;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="assignment_id") private Assignment assignment;
 @Column(name="assignment_title",length=180) private String assignmentTitle;
 @Column(nullable=false,length=40) private String language;
 @Column(name="source_code",nullable=false,columnDefinition="LONGTEXT") private String sourceCode;
 @Column(columnDefinition="LONGTEXT") private String stdin;
 @Column(columnDefinition="LONGTEXT") private String output;
 @Column(length=120) private String status;
 @Column(name="submitted_at",nullable=false) private LocalDateTime submittedAt;
 @Column(name="marks_out_of_10") private Integer marks;
 @Column(name="teacher_feedback",columnDefinition="TEXT") private String teacherFeedback;
 @Column(name="reviewed_at") private LocalDateTime reviewedAt;
 @PrePersist void create(){if(submittedAt==null)submittedAt=LocalDateTime.now();}
 public Long getId(){return id;} public User getStudent(){return student;} public void setStudent(User v){student=v;} public Assignment getAssignment(){return assignment;} public void setAssignment(Assignment v){assignment=v;} public String getAssignmentTitle(){return assignmentTitle;} public void setAssignmentTitle(String v){assignmentTitle=v;} public String getLanguage(){return language;} public void setLanguage(String v){language=v;} public String getSourceCode(){return sourceCode;} public void setSourceCode(String v){sourceCode=v;} public String getStdin(){return stdin;} public void setStdin(String v){stdin=v;} public String getOutput(){return output;} public void setOutput(String v){output=v;} public String getStatus(){return status;} public void setStatus(String v){status=v;} public LocalDateTime getSubmittedAt(){return submittedAt;} public Integer getMarks(){return marks;} public void setMarks(Integer v){marks=v;} public String getTeacherFeedback(){return teacherFeedback;} public void setTeacherFeedback(String v){teacherFeedback=v;} public LocalDateTime getReviewedAt(){return reviewedAt;} public void setReviewedAt(LocalDateTime v){reviewedAt=v;}
}

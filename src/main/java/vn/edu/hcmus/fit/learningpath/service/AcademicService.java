package vn.edu.hcmus.fit.learningpath.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmus.fit.learningpath.entity.AcademicGoal;
import vn.edu.hcmus.fit.learningpath.entity.StudentCourse;
import vn.edu.hcmus.fit.learningpath.dto.response.DashboardStatsResponse;
import vn.edu.hcmus.fit.learningpath.repository.AcademicGoalRepository;
import vn.edu.hcmus.fit.learningpath.repository.StudentCourseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AcademicService {

    private final AcademicGoalRepository academicGoalRepository;
    private final StudentCourseRepository studentCourseRepository;

    @Transactional(readOnly = true)
    public DashboardStatsResponse getAcademicProgress(Integer studentId) {
        AcademicGoal goal = academicGoalRepository.findByStudentId(studentId)
                .orElse(new AcademicGoal());
        Integer required = goal.getTotalCreditsRequired() != null ? goal.getTotalCreditsRequired() : 138;

        List<StudentCourse> courses = studentCourseRepository.findByStudentIdAndStatus(studentId, "COMPLETED");

        int earned = courses.stream()
                .mapToInt(sc -> sc.getCourse().getCredits())
                .sum();

        Map<String, Integer> creditsBySemester = courses.stream()
                .collect(Collectors.groupingBy(
                        StudentCourse::getSemester,
                        Collectors.summingInt(sc -> sc.getCourse().getCredits())
                ));

        List<DashboardStatsResponse.SemesterProgressResponse> chartData = new ArrayList<>();
        int cumulative = 0;
        
        List<String> sortedSemesters = creditsBySemester.keySet().stream().sorted().toList();
        
        for (String sem : sortedSemesters) {
            int semCredits = creditsBySemester.get(sem);
            cumulative += semCredits;
            chartData.add(DashboardStatsResponse.SemesterProgressResponse.builder()
                    .semester(sem)
                    .creditsEarned(semCredits)
                    .cumulativeCredits(cumulative)
                    .build());
        }

        return DashboardStatsResponse.builder()
                .totalCreditsEarned(earned)
                .totalCreditsRequired(required)
                .creditsCompleted(earned)
                .graduationProgress(required > 0 ? (double) earned / required * 100 : 0)
                .semesterProgress(chartData)
                .build();
    }
}

package vn.edu.hcmus.fit.learningpath.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmus.fit.learningpath.dto.request.CreateStudentRequest;
import vn.edu.hcmus.fit.learningpath.dto.request.UpdateStudentRequest;
import vn.edu.hcmus.fit.learningpath.dto.response.*;
import vn.edu.hcmus.fit.learningpath.entity.*;
import vn.edu.hcmus.fit.learningpath.exception.AcademicException;
import vn.edu.hcmus.fit.learningpath.exception.ResourceNotFoundException;
import vn.edu.hcmus.fit.learningpath.mapper.StudentMapper;
import vn.edu.hcmus.fit.learningpath.repository.*;
import vn.edu.hcmus.fit.learningpath.service.StorageService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final StorageService storageService;

    @Transactional(readOnly = true)
    public StudentProfileResponse getProfile(Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        return studentMapper.toProfileResponse(student);
    }

    @Transactional(readOnly = true)
    public StudentResponse getStudentById(Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new AcademicException(HttpStatus.NOT_FOUND, "Student not found with id: " + studentId));
        return mapToStudentResponse(student);
    }

    @Transactional
    public StudentResponse updateStudent(Integer studentId, UpdateStudentRequest request) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new AcademicException(HttpStatus.NOT_FOUND, "Student not found with id: " + studentId));

        if (request.getFullName() != null) student.setFullName(request.getFullName());
        if (request.getDob() != null) student.setDob(request.getDob());
        if (request.getPlaceOfBirth() != null) student.setPlaceOfBirth(request.getPlaceOfBirth());
        if (request.getPhone() != null) student.setPhone(request.getPhone());
        if (request.getEmail() != null) student.setEmail(request.getEmail());
        if (request.getIndustry() != null) student.setIndustry(request.getIndustry());
        if (request.getTrainingLevel() != null) student.setTrainingLevel(request.getTrainingLevel());
        if (request.getTypeOfTraining() != null) student.setTypeOfTraining(request.getTypeOfTraining());
        if (request.getCurrentPosition() != null) student.setCurrentPosition(request.getCurrentPosition());
        if (request.getStartDate() != null) student.setStartDate(request.getStartDate());

        // Chỉ dùng avatarUrl từ Request
        if (request.getAvatarUrl() != null) {
            student.setAvatarUrl(request.getAvatarUrl());
        }

        return mapToStudentResponse(studentRepository.save(student));
    }

    @Transactional
    public void deleteStudent(Integer studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new AcademicException(HttpStatus.NOT_FOUND, "Student not found with id: " + studentId);
        }
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public StudentResponse createStudent(CreateStudentRequest request) {
        if (studentRepository.existsByStudentCode(request.getStudentCode())) {
            throw new AcademicException(HttpStatus.BAD_REQUEST, "Student code already exists: " + request.getStudentCode());
        }
        if (studentRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AcademicException(HttpStatus.BAD_REQUEST, "Email already exists: " + request.getEmail());
        }

        Student student = Student.builder()
                .studentCode(request.getStudentCode())
                .fullName(request.getFullName())
                .dob(request.getDob())
                .placeOfBirth(request.getPlaceOfBirth())
                .phone(request.getPhone())
                .email(request.getEmail())
                .industry(request.getIndustry())
                .trainingLevel(request.getTrainingLevel())
                .typeOfTraining(request.getTypeOfTraining())
                .currentPosition(request.getCurrentPosition())
                .startDate(request.getStartDate())
                .avatarUrl(request.getAvatarUrl())
                .build();

        return mapToStudentResponse(studentRepository.save(student));
    }

    private StudentResponse mapToStudentResponse(Student student) {
        String avatar = student.getAvatarUrl();
        if (avatar == null || avatar.isEmpty()) {
            avatar = "https://ui-avatars.com/api/?name=" + URLEncoder.encode(student.getFullName(), StandardCharsets.UTF_8) + "&background=random";
        }

        return StudentResponse.builder()
                .id(student.getId())
                .studentCode(student.getStudentCode())
                .fullName(student.getFullName())
                .dob(student.getDob())
                .placeOfBirth(student.getPlaceOfBirth())
                .phone(student.getPhone())
                .email(student.getEmail())
                .industry(student.getIndustry())
                .trainingLevel(student.getTrainingLevel())
                .typeOfTraining(student.getTypeOfTraining())
                .currentPosition(student.getCurrentPosition())
                .avatarUrl(avatar)
                .startDate(student.getStartDate())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }
}
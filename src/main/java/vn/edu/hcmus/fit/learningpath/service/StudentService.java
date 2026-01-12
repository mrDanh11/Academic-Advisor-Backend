package vn.edu.hcmus.fit.learningpath.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmus.fit.learningpath.entity.*;
import vn.edu.hcmus.fit.learningpath.dto.response.*;
import vn.edu.hcmus.fit.learningpath.exception.ResourceNotFoundException;
import vn.edu.hcmus.fit.learningpath.mapper.StudentMapper;
import vn.edu.hcmus.fit.learningpath.repository.*;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Transactional(readOnly = true)
    public StudentProfileResponse getProfile(Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        return studentMapper.toProfileResponse(student);
    }
}
package vn.edu.hcmus.fit.learningpath.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import vn.edu.hcmus.fit.learningpath.entity.Student;
import vn.edu.hcmus.fit.learningpath.dto.response.StudentProfileResponse;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "course", source = "startDate", qualifiedByName = "calculateCourse")
    StudentProfileResponse toProfileResponse(Student student);

    @Named("calculateCourse")
    default String calculateCourse(LocalDate startDate) {
        if (startDate == null) return "N/A";
        return String.valueOf(startDate.getYear());
    }
}
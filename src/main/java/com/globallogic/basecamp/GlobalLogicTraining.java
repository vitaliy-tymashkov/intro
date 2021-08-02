package com.globallogic.basecamp;

import com.globallogic.basecamp.model.Grade;
import com.globallogic.basecamp.model.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * GlobalLogicTraining is an implementation of the {@link com.globallogic.basecamp.Training Training} interface.
 * Contains the training name and student grades.
 */
public class GlobalLogicTraining implements Training {
    /**
     * TODOx - done: implement methods of the Training interface and add the necessary functionality
     */

    private static final int LOWEST_MARK = 0;
    static final int HIGHEST_MARK = 10;

    private final String name;

    private final HashMap<Student, Grade> grades = new HashMap<>();

    public GlobalLogicTraining(String name) {
        if (null == name || name.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<Student> getStudents() {
        return new ArrayList<>(grades.keySet());
    }

    @Override
    public boolean addStudent(Student student) {
        if (grades.containsKey(student)) {
            return false;
        } else {
            grades.put(student, null);
            return true;
        }
    }

    @Override
    public boolean removeStudent(Student student) {
        if (grades.containsKey(student)) {
            grades.remove(student);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean rateFirstSemester(Student student, int mark) {
        validateMark(mark);
        Grade existingGrade;
        if (grades.containsKey(student)) {
            existingGrade = grades.get(student);
            if (null == existingGrade) {
                Grade firstSemesterGrade = new Grade();
                firstSemesterGrade.setFirstSemester(mark);
                return setGradesWithCheck(student, firstSemesterGrade);
            }
        } else {
            existingGrade = new Grade();
        }
        existingGrade.setFirstSemester(mark);
        return setGradesWithCheck(student, existingGrade);
    }

    @Override
    public boolean rateSecondSemester(Student student, int mark) {
        validateMark(mark);
        Grade existingGrade;
        if (grades.containsKey(student)) {
            existingGrade = grades.get(student);
            if (null == existingGrade) {
                Grade secondSemesterGrade = new Grade();
                secondSemesterGrade.setSecondSemester(mark);
                return setGradesWithCheck(student, secondSemesterGrade);
            }
        } else {
            existingGrade = new Grade();
        }
        existingGrade.setSecondSemester(mark);
        return setGradesWithCheck(student, existingGrade);
    }

    @Override
    public boolean isPresent(Student student) {
        return grades.containsKey(student);
    }

    @Override
    public Optional<Grade> getStudentGrade(Student student) {
        return (grades.containsKey(student)) ? Optional.ofNullable(grades.get(student)) : Optional.empty();
    }

    private void validateMark(int mark) {
        if (mark < LOWEST_MARK || mark > HIGHEST_MARK) {
            throw new IllegalArgumentException();
        }
    }

    private boolean setGradesWithCheck(Student student, Grade semesterGrade) {
        if (grades.containsKey(student)) {
            grades.put(student, semesterGrade);
            return true;
        } else {
            return false;
        }
    }

}

package com.globallogic.basecamp;

import com.globallogic.basecamp.comparator.StudentComparator;
import com.globallogic.basecamp.model.Grade;
import com.globallogic.basecamp.model.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * TrainingUtils contains a set of operations to perform with Stream of trainings.
 * You will need to implement the methods using <strong>Stream API</strong>. Do not use for or while loops
 * for implementation
 *
 * @see <a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/stream/Stream.html">Stream</a>
 * documentation for more details
 */
public class TrainingUtils {

    private TrainingUtils() {

    }

    /**
     * Get emails of the students from provided trainings that satisfy the condition
     *
     * @param trainings stream of trainings
     * @param predicate condition whether we return this student or not
     * @return list of unique students emails
     */
    public static List<String> getStudentEmailsByCondition(Stream<Training> trainings,
                                                           Predicate<Student> predicate) {
        return trainings
                .flatMap(training -> training.getStudents().stream())
                .sorted(new StudentComparator())
                .collect(Collectors.toCollection(LinkedHashSet::new))
                .stream()
                .filter(predicate)
                .map(Student::getEmail)
                .collect(Collectors.toList());
    }

    /**
     * For each student from the provided trainings get a list of trainings that he/she attends
     *
     * @param trainings stream of trainings
     * @return map where keys are student emails and values are List of training names that this student attends
     */
    public static Map<String, List<String>> getTrainingsPerStudent(Stream<Training> trainings) {
        Map<String, List<String>> trainingsPerStudent = new HashMap<>();
        trainings.forEach(training -> training.getStudents()
                .forEach(student -> {
                    List<String> trainingsForThisStudent = trainingsPerStudent.containsKey(student.getEmail())
                            ? trainingsPerStudent.get(student.getEmail())
                            : new ArrayList<>();
                    trainingsForThisStudent.add(training.getName());
                    trainingsPerStudent.put(student.getEmail(), trainingsForThisStudent);
                }));
        return trainingsPerStudent;
    }

    /**
     * For each student from the provided trainings get an average mark. Average mark is calculated as a sum of all
     * marks for both semesters divided by the number of marks
     *
     * @param trainings stream of trainings
     * @return map where keys are student emails and values are student average mark calculating using both semesters
     */
    public static Map<String, Double> getAverageMarkPerStudent(Stream<Training> trainings) {
        Map<String, Double> averageMarkPerStudent = new HashMap<>();
        List<Training> allTrainings = trainings.collect(Collectors.toList());
        allTrainings.stream().flatMap(training -> training.getStudents().stream())
                .forEach(student -> {
                    List<Integer> grades = new ArrayList<>();
                    if (!averageMarkPerStudent.containsKey(student.getEmail())) {
                        averageMarkPerStudent.put(student.getEmail(), null);
                        allTrainings.stream()
                                .filter(trainingsInSecondLoop -> trainingsInSecondLoop.getStudentGrade(student).isPresent())
                                .forEach(trainingsInSecondLoop -> {
                                    grades.add(trainingsInSecondLoop.getStudentGrade(student).get().getFirstSemester());
                                    grades.add(trainingsInSecondLoop.getStudentGrade(student).get().getSecondSemester());
                                });
                        Double avg = grades.stream()
                                .mapToInt(i -> i)
                                .average().orElse(Double.NaN);
                        averageMarkPerStudent.put(student.getEmail(), avg);
                    }
                });
        return averageMarkPerStudent;
    }

    /**
     * Perform an action for all grades in the provided trainings
     *
     * @param trainings stream of trainings
     * @param action    action to perform
     */
    public static void forEachGrade(Stream<Training> trainings, Consumer<Grade> action) {
        trainings.forEach(training -> training.getStudents().stream()
                .filter(student -> training.getStudentGrade(student).isPresent())
                .map(student -> training.getStudentGrade(student).get())
                .forEach(action));
    }

    /**
     * Get students with the highest mark received during any semester
     *
     * @param trainings stream of trainings
     * @return list of students emails
     */
    public static List<String> getStudentsWithMaxMark(Stream<Training> trainings) {
        List<String> studentsWithMaxMarks = new ArrayList<>();
        trainings.forEach(training -> {
            Student maxStudent = null;
            int max = Integer.MIN_VALUE;

            for (Student student : training.getStudents()) {
                if (training.getStudentGrade(student).isPresent()) {
                    int firstSemesterMark = training.getStudentGrade(student).get().getFirstSemester();
                    if (firstSemesterMark > max) {
                        max = firstSemesterMark;
                        maxStudent = student;
                    }
                    int secondSemesterMark = training.getStudentGrade(student).get().getSecondSemester();
                    if (secondSemesterMark > max) {
                        max = secondSemesterMark;
                        maxStudent = student;
                    }
                }
            }
            studentsWithMaxMarks.add(Objects.requireNonNull(maxStudent).getEmail());
        });
        return studentsWithMaxMarks;
    }

    /**
     * Remove students from all provided trainings by the specified condition
     *
     * @param trainings stream of trainings
     * @param predicate condition whether to remove a student
     */
    public static void removeStudentsIf(Stream<Training> trainings, Predicate<Student> predicate) {
        trainings.forEach(training -> training.getStudents().stream()
                .filter(predicate)
                .forEach(training::removeStudent));
    }

    /**
     * Get distinct students' full names from all trainings sorted by the `StudentComparator`
     * <p>
     * For example, full name for the student with the first name "Jerry" and the last name "Ferdy"
     * will be "Jerry Ferdy"
     *
     * @param trainings stream of trainings
     * @return list of sorted
     */
    public static List<String> getStudentsSorted(Stream<Training> trainings) {
        return trainings
                .flatMap(training -> training.getStudents().stream())
                .sorted(new StudentComparator())
                .collect(Collectors.toCollection(LinkedHashSet::new))
                .stream()
                .map(student -> String.join("", student.getFirstName(), " ", student.getLastName()))
                .collect(Collectors.toList());
    }

    /**
     * Get students that received mark lower than provided on any training during any semester. Trainings
     * are also provided as a stream
     *
     * @param trainings stream of trainings
     * @param mark      student mark
     * @return list of students' emails
     */
    public static List<String> getStudentsWithMarkLowerThan(Stream<Training> trainings, int mark) {
        Set<String> studentsWithLowerMarks = new HashSet<>();
        trainings.forEach(training -> training.getStudents().stream()
                .filter(student -> training.getStudentGrade(student).isPresent())
                .forEach(student -> {
                    addStudentIfThereIsLowerMark(studentsWithLowerMarks, mark, student,
                            training.getStudentGrade(student).get().getFirstSemester());
                    addStudentIfThereIsLowerMark(studentsWithLowerMarks, mark, student,
                            training.getStudentGrade(student).get().getSecondSemester());
                }));
        return new ArrayList<>(studentsWithLowerMarks);
    }

    /**
     * Check whether the student attends any of the provided trainings
     * Note that students are equal if and only if their emails are the same
     *
     * @param trainings stream of trainings
     * @param student   student to check presence
     * @return true if the student attends any training, false otherwise
     */
    public static boolean isStudentPresentOnTrainings(Stream<Training> trainings, Student student) {
        return trainings
                .flatMap(training -> training.getStudents().stream())
                .anyMatch(studentOnTraining -> studentOnTraining.getEmail().equals(student.getEmail()));
    }

    /**
     * Get average students mark for each of the provided trainings. Average mark is calculated as a sum of
     * all marks for both semesters divided by the number of marks
     *
     * @param trainings stream of trainings
     * @return map where key is a training name and value is an average mark
     */
    //Stream done
    public static Map<String, Double> getAverageMarkPerTraining(Stream<Training> trainings) {
        Map<String, Double> averageMarkPerTraining = new HashMap<>();
        trainings.forEach(training -> {
            averageMarkPerTraining.put(training.getName(), null);
            List<Integer> grades = new ArrayList<>();
            training.getStudents().stream()
                    .filter(student -> training.getStudentGrade(student).isPresent())
                    .forEach(student -> {
                        grades.add(training.getStudentGrade(student).get().getFirstSemester());
                        grades.add(training.getStudentGrade(student).get().getSecondSemester());
                    });
            Double avg = grades.stream()
                    .mapToInt(i -> i)
                    .average().orElse(Double.NaN);
            averageMarkPerTraining.put(training.getName(), avg);
        });
        return averageMarkPerTraining;
    }

    private static void addStudentIfThereIsLowerMark(Set<String> studentsWithLowerMarks, int mark, Student student, int semesterMark) {
        if (semesterMark < mark) {
            studentsWithLowerMarks.add(student.getEmail());
        }
    }

}

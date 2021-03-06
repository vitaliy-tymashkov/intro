package com.globallogic.basecamp.comparator;

import com.globallogic.basecamp.model.Student;

import java.util.Comparator;

/**
 * Custom comparator for the {@link com.globallogic.basecamp.model.Student Student} class
 */
public class StudentComparator implements Comparator<Student> {

    /**
     * TODOx - done: implement `compare` method of the Comparator interface
     * Compare students in natural order by the last name. If last names are the same,
     * compare in natural order by the first name
     */
    @Override
    public int compare(Student o1, Student o2) {
        return Comparator.comparing(Student::getLastName)
                .thenComparing(Student::getFirstName)
                .compare(o1, o2);
    }

}

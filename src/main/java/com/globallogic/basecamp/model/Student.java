package com.globallogic.basecamp.model;

import java.util.Objects;

/**
 * Student class, represents the student that attends the training
 */
public class Student {

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private Address address;

    public Student(String email) {
        this.email = email;
    }

    /**
     * Allows to get the builder for the Student
     *
     * @param email student's email. Required for each student object construction
     * @return builder
     */
    public static Builder builder(String email) {
        return new Builder(email);
    }


    /**
     * TODOx - done: implement getters and other methods if necessary
     */

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    /**
     * TODOx - done: implement equals() method for this class
     *
     * @param o object to compare the current object to
     * @return true if students have the same emails, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return email.equals(student.email)
                && Objects.equals(firstName, student.firstName)
                && Objects.equals(lastName, student.lastName)
                && Objects.equals(phoneNumber, student.phoneNumber)
                && Objects.equals(address, student.address);
    }

    /**
     * TODOx - done: implement hashCode() method for this class using email field
     *
     * @return object hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(email, firstName, lastName, phoneNumber, address);
    }

    /**
     * Builder class is a part of the builder pattern implementation
     * Needed to ease the Student object construction
     * <p>
     * TODOx - done: implement the builder functionality
     */
    public static class Builder {
        private final Student newStudent;

        public Builder(String email) {
            newStudent = new Student(email);
        }

        public Builder setFirstName(String firstName) {
            newStudent.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            newStudent.lastName = lastName;
            return this;
        }

        public Builder setAddress(Address address) {
            newStudent.address = address;
            return this;
        }

        public Builder setPhoneNumber(String phoneNumber) {
            newStudent.phoneNumber = phoneNumber;
            return this;
        }

        public Student build() {
            return newStudent;
        }
    }

}

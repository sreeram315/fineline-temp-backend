package com.fineline.services;

import com.fineline.models.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

public interface StudentService {

    /**
     * For given ID,
     *  -   Checks if Student with ID exists in cache. If yes, return.
     *  -   Else, get from database. If exists, return.
     *  -   Else, return  I am a teapot exception.
     *
     * @param id id of the student
     * @return Student with id=id
     */
    @GetMapping
    Student getStudent(Integer id) throws InterruptedException;

    /**
     * Adds a Student entry into database.
     * This method also adds the entry into the cache.
     *
     * @param id id of the student
     * @param name name of the student
     * @param contact contact of the student
     * @return student created
     */
    @PostMapping
    Student addStudent(Integer id, String name, String contact) throws InterruptedException;

    /**
     * Return list of all students available in the database.
     * @return list of all students in database
     */
    @GetMapping
    List<Student> getAllStudents() throws InterruptedException;

    /**
     * Endpoint to demonstrate sql predicate
     * Values in Imap here are listed and queried in an SQL fashion.
     *
     * Returns the students who have substring name in their name (cached students are only queried)
     *
     * @param substring substring that is required to be present in student's name
     * @return list of students whose name has letter s
     */
    @GetMapping
    List<Student> getAllStudentsInCacheSql(String substring);

    /**
     * This method prints all the student objects in the cache queried through SQL
     */
    @GetMapping
    String sqlTest();

    /**
     * Return list of students in currently held in cache.
     * @return list of all students in cache
     */
    @GetMapping
    List<Student> getAllStudentsInCache();
}

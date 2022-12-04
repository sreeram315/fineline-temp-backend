package com.fineline.services.impl;

import com.fineline.constants.StudentConstants;
import com.fineline.dao.StudentDAO;
import com.fineline.models.Student;
import com.fineline.services.StudentService;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.impl.predicates.SqlPredicate;
import com.hazelcast.sql.SqlResult;
import com.hazelcast.sql.SqlRow;
import com.hazelcast.sql.SqlService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class StudentServiceImpl implements StudentService {
    private static final Log LOG = LogFactory.getLog(StudentServiceImpl.class);

    @Autowired
    private StudentDAO studentDAO;

    @Autowired
    private HazelcastInstance hazelcastInstance;

    /**
     * {@inheritDoc}
     */
    @GetMapping("/student/get")
    public Student getStudent(Integer id) throws InterruptedException {
        LOG.info("Student data requested for id: " + id);
        Student student = studentDAO.get(id);
        if(student == null)
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, String.format("Student with id:%d - Not Found ", id));
        return student;
    }

    /**
     * {@inheritDoc}
     */
    @PostMapping("/student/add")
    public Student addStudent(Integer id, String name, String contact) throws InterruptedException {
        return studentDAO.insertingStudent(id, name, contact);
    }

    /**
     * {@inheritDoc}
     */
    @GetMapping("/student/all")
    public List<Student> getAllStudents() throws InterruptedException {
        List<Student> students = studentDAO.getAll();
        LOG.info("All students data requested");
        return students;
    }

    /**
     * {@inheritDoc}
     */
    @GetMapping("/student/map/name-contains")
    public List<Student> getAllStudentsInCacheSql(String substring){
        String sqlPredicateString = "name LIKE %" + substring + "%";
        IMap<Integer, Student> hazelcastStudentsMap = studentDAO.getHazelcastStudentsMap();
        @SuppressWarnings("unchecked")
        List<Student> values = (List<Student>) hazelcastStudentsMap.values(new SqlPredicate(sqlPredicateString));
        return values;
    }

    /**
     * {@inheritDoc}
     */
    @GetMapping("student/map/print-all")
    public String sqlTest(){
        SqlService sqlService = hazelcastInstance.getSql();
        try (SqlResult result = sqlService.execute(String.format("SELECT id, name, contact FROM %s ORDER BY id",
                StudentConstants.CACHE_MAP))) {
            for (SqlRow row : result) {
                Integer id = row.getObject("id");
                String name = row.getObject("name");
                String contact = row.getObject("contact");
                LOG.info(String.format("%s %s %s", id.toString(), name, contact));
            }
        }
        return String.format("All objects present in %s are printed on logs.", StudentConstants.CACHE_MAP);
    }

    /**
     * {@inheritDoc}
     */
    @GetMapping("/student/map/all")
    public List<Student> getAllStudentsInCache(){
        IMap<Integer, Student> hazelcastStudentsMap = studentDAO.getHazelcastStudentsMap();
        return (List<Student>) hazelcastStudentsMap.values();
    }
}

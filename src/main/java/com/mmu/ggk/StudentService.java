package com.mmu.ggk;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
@Service
public class StudentService {
 
    @Autowired
    private StudentRepository repo;
    
    
     
    public void setRepo(StudentRepository repo) {
		this.repo = repo;
	}

	public List<Student> listAll() {
        return repo.findAll();
    }
     
    public void save(Student product) {
        repo.saveAndFlush(product);
    }
     
    public Student get(Integer id) {
    	System.out.println("Id id not nulll     "+id);
    	Student stud=repo.getById(id);
    	System.out.println("In service Method....."+stud);
    	return stud;
    }
         
     
    public void delete(Integer id) {
        repo.deleteById(id);
    }
}

package com.example.TechnicalAnalysis.Repositories;

import com.example.TechnicalAnalysis.Nodes.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:3000") //enable cors
public interface Repository extends PagingAndSortingRepository<Person, Long>, CrudRepository<Person, Long> {

}

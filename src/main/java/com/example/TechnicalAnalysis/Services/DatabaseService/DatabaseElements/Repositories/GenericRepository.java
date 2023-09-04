package com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000") //enable cors
public interface GenericRepository <T, E> extends PagingAndSortingRepository<T, E>, CrudRepository<T, E> {
    List<T> findByKey(@Param("key") E key);
}

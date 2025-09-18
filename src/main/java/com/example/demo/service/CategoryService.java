package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.entity.CategoriesEntity;


public interface CategoryService {

	<S extends CategoriesEntity> S save(S entity);
	// Define service methods for category operations

	List<CategoriesEntity> findAll();

	List<CategoriesEntity> findAllById(Iterable<Long> ids);

	Optional<CategoriesEntity> findById(Long categoryId);

	long count();

	void deleteById(Long id);

	void delete(CategoriesEntity entity);

	void deleteAll();

	List<CategoriesEntity> findByNameContaining(String name);

	Page<CategoriesEntity> findByNameContaining(String name, Pageable pageable);

	<S extends CategoriesEntity> Optional<S> findOne(Example<S> example);

	@SuppressWarnings("hiding")
	<CategoriesEntity> Page<CategoriesEntity> findAll(Pageable pageable);
	
}

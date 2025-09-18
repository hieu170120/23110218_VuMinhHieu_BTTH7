package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.entity.CategoriesEntity;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CategoryService;
import org.apache.commons.lang3.StringUtils;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public <S extends CategoriesEntity> S save(S entity) {
        if (entity.getCategoryId() == null) {
            // Nếu là đối tượng mới thì lưu vào cơ sở dữ liệu
            return categoryRepository.save(entity);
        } else {
            // Nếu có categoryId (đã tồn tại trong cơ sở dữ liệu)
            Optional<CategoriesEntity> category = categoryRepository.findById(entity.getCategoryId());
            if (category.isPresent()) {
                // Nếu tên rỗng, giữ nguyên tên cũ
                if (StringUtils.isEmpty(entity.getName())) {
                    entity.setName(category.get().getName());
                }
            } else {
                // Nếu không tìm thấy, có thể lưu tên mới
                // Tuy nhiên điều này không thực sự cần thiết vì nếu không có đối tượng sẽ trả về null
                entity.setName(entity.getName());
            }
        }
        return categoryRepository.save(entity);
    }

    @Override
    public List<CategoriesEntity> findAll() {
        return categoryRepository.findAll();
    }
    @Override
    public List<CategoriesEntity> findAllById(Iterable<Long> ids) {
        return categoryRepository.findAllById(ids);
    }

    @Override
    public Optional<CategoriesEntity> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public <S extends CategoriesEntity> Optional<S> findOne(Example<S> example) {
        return categoryRepository.findOne(example);
    }

    @Override
    public long count() {
        return categoryRepository.count();
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public void delete(CategoriesEntity entity) {
        categoryRepository.delete(entity);
    }

    @Override
    public void deleteAll() {
        categoryRepository.deleteAll();
    }

    @Override
    public List<CategoriesEntity> findByNameContaining(String name) {
        return categoryRepository.findByNameContaining(name);
    }

    @Override
    public Page<CategoriesEntity> findByNameContaining(String name, Pageable pageable) {
        return categoryRepository.findByNameContaining(name, pageable);
    }

	@Override
	public <CategoriesEntity> Page<CategoriesEntity> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}
}

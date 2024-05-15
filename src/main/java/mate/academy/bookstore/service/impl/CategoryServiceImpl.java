package mate.academy.bookstore.service.impl;

import java.util.List;
import java.util.Objects;
import mate.academy.bookstore.dto.category.CategoryDto;
import mate.academy.bookstore.exception.DuplicateEntityException;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.CategoryMapper;
import mate.academy.bookstore.model.Category;
import mate.academy.bookstore.repository.category.CategoryRepository;
import mate.academy.bookstore.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(
            CategoryRepository categoryRepository,
            CategoryMapper categoryMapper
    ) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
                                 .map(categoryMapper::toDto)
                                 .toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Category not found by id " + id));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto save(CategoryDto categoryDto) {
        String name = categoryDto.name();
        if (categoryRepository.findByName(name) != null) {
            throw new DuplicateEntityException("Category with name " + name + " already exists");
        }

        Category category = categoryMapper.toEntity(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public CategoryDto update(Long id, CategoryDto categoryDto) {
        Category existingCategory = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Category not found by id " + id));

        String categoryName = categoryDto.name();
        if (!Objects.equals(existingCategory.getName(), categoryName)
                && categoryRepository.findByName(categoryName) != null) {
            throw new DuplicateEntityException(
                    "Category with name " + categoryName + " already exists");
        }

        Category category = categoryMapper.toEntity(categoryDto);
        category.setId(id);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}

package com.example.filmmonster.service;

import com.example.filmmonster.domain.Category;
import com.example.filmmonster.repository.CategoryRepository;
import com.example.filmmonster.repository.search.CategorySearchRepository;
import com.example.filmmonster.service.dto.CategoryDTO;
import com.example.filmmonster.service.mapper.CategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Category.
 */
@Service
@Transactional
public class CategoryService {

    private final Logger log = LoggerFactory.getLogger(CategoryService.class);
    
    @Inject
    private CategoryRepository categoryRepository;

    @Inject
    private CategoryMapper categoryMapper;

    @Inject
    private CategorySearchRepository categorySearchRepository;

    /**
     * Save a category.
     *
     * @param categoryDTO the entity to save
     * @return the persisted entity
     */
    public CategoryDTO save(CategoryDTO categoryDTO) {
        log.debug("Request to save Category : {}", categoryDTO);
        Category category = categoryMapper.categoryDTOToCategory(categoryDTO);
        category = categoryRepository.save(category);
        CategoryDTO result = categoryMapper.categoryToCategoryDTO(category);
        categorySearchRepository.save(category);
        return result;
    }

    /**
     *  Get all the categories.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<CategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Categories");
        Page<Category> result = categoryRepository.findAll(pageable);
        return result.map(category -> categoryMapper.categoryToCategoryDTO(category));
    }

    /**
     *  Get one category by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public CategoryDTO findOne(Long id) {
        log.debug("Request to get Category : {}", id);
        Category category = categoryRepository.findOne(id);
        CategoryDTO categoryDTO = categoryMapper.categoryToCategoryDTO(category);
        return categoryDTO;
    }

    /**
     *  Delete the  category by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Category : {}", id);
        categoryRepository.delete(id);
        categorySearchRepository.delete(id);
    }

    /**
     * Search for the category corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CategoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Categories for query {}", query);
        Page<Category> result = categorySearchRepository.search(queryStringQuery(query), pageable);
        return result.map(category -> categoryMapper.categoryToCategoryDTO(category));
    }
}

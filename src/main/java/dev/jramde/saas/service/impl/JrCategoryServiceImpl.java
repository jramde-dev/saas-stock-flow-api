package dev.jramde.saas.service.impl;

import dev.jramde.saas.dto.request.JrCategoryRequest;
import dev.jramde.saas.dto.response.JrCategoryResponse;
import dev.jramde.saas.service.IBasicCrudService;
import dev.jramde.saas.service.ICategoryService;
import java.util.List;

public class JrCategoryServiceImpl implements ICategoryService {

    @Override
    public void create(JrCategoryRequest request) {

    }

    @Override
    public void update(String id, JrCategoryRequest request) {

    }

    @Override
    public List<JrCategoryResponse> findAll(int page, int size) {
        return List.of();
    }

    @Override
    public JrCategoryResponse findById(String id) {
        return null;
    }

    @Override
    public void delete(String id) {

    }
}

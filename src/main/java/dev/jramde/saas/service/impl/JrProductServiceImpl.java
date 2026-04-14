package dev.jramde.saas.service.impl;

import dev.jramde.saas.common.JrPageResponse;
import dev.jramde.saas.dto.request.JrProductRequest;
import dev.jramde.saas.dto.response.JrProductResponse;
import dev.jramde.saas.entity.JrCategory;
import dev.jramde.saas.entity.JrProduct;
import dev.jramde.saas.mapper.JrProductMapper;
import dev.jramde.saas.repository.JrCategoryRepository;
import dev.jramde.saas.repository.JrProductRepository;
import dev.jramde.saas.repository.JrStockMvmtRepository;
import dev.jramde.saas.service.IProductService;
import jakarta.persistence.EntityNotFoundException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JrProductServiceImpl implements IProductService {
    private final JrProductRepository productRepository;
    private final JrStockMvmtRepository mvmtRepository;
    private final JrCategoryRepository categoryRepository;
    private final JrProductMapper mapper;


    @Override
    public void create(JrProductRequest request) {
        checkProductAlreadyExist(request);
        productRepository.save(mapper.maps(request));
    }

    @Override
    public void update(String id, JrProductRequest request) {
        productRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("This product does not exist."));
        checkProductAlreadyExist(request);

        JrProduct product = mapper.maps(request);
        product.setId(id);
        productRepository.save(product);
    }

    private void checkProductAlreadyExist(JrProductRequest request) {
        productRepository.findByNameIgnoreCase(request.getName()).ifPresent(product -> {
            throw new RuntimeException("Product with name " + request.getName() + " already exists.");
        });

        productRepository.findByReferenceIgnoreCase(request.getReference()).ifPresent(product -> {
            throw new RuntimeException("Product with reference " + request.getReference() + " already exists.");
        });

        JrCategory category = categoryRepository.findById(request.getCategoryId()).orElseThrow(
                () -> new EntityNotFoundException("Category not found."));

        if (!Objects.equals(request.getCategoryId(), category.getId())) {
            throw new EntityNotFoundException("Category does not match.");
        }
    }

    @Override
    public JrPageResponse<JrProductResponse> findAll(int page, int size) {
        final PageRequest pageRequest = PageRequest.of(page, size);
        final Page<JrProduct> products = productRepository.findAll(pageRequest);
        final Page<JrProductResponse> responses = products.map(mapper::maps);
        return JrPageResponse.of(responses);
    }

    @Override
    public JrProductResponse findById(String id) {
        return productRepository.findById(id)
                .map(mapper::maps)
                .orElseThrow(() -> new EntityNotFoundException("Product does not exist."));
    }

    @Override
    public void delete(String id) {
        productRepository.findById(id).ifPresent(product -> {
            product.getMovements().forEach(movement -> {
                movement.setDeleted(true);
                mvmtRepository.save(movement);
            });
            productRepository.delete(product);
        });
    }
}

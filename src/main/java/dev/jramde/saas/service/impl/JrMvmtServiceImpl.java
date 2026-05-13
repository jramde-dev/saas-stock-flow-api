package dev.jramde.saas.service.impl;

import dev.jramde.saas.common.JrPageResponse;
import dev.jramde.saas.dto.request.JrStockMvmtRequest;
import dev.jramde.saas.dto.response.JrStockMvmtResponse;
import dev.jramde.saas.entity.JrProduct;
import dev.jramde.saas.entity.JrStockMvmt;
import dev.jramde.saas.mapper.JrMvmtMapper;
import dev.jramde.saas.repository.JrProductRepository;
import dev.jramde.saas.repository.JrStockMvmtRepository;
import dev.jramde.saas.service.IStockMvmtService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class JrMvmtServiceImpl implements IStockMvmtService {
    private final JrStockMvmtRepository mvmtRepository;
    private final JrProductRepository productRepository;
    private final JrMvmtMapper mapper;


    @Override
    public void create(JrStockMvmtRequest request) {
        checkProductExist(request);
        request.setDateMvmt(LocalDateTime.now());
        mvmtRepository.save(mapper.maps(request));
    }

    @Override
    public void update(String id, JrStockMvmtRequest request) {
        checkProductExist(request);
        var oldMvmt = mvmtRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("This movement does not exist."));
        request.setDateMvmt(oldMvmt.getDateMvmt());
        JrStockMvmt mvmt = mapper.maps(request);
        mvmt.setId(id);
        mvmtRepository.save(mvmt);
    }

    @Override
    public JrPageResponse<JrStockMvmtResponse> findAll(int page, int size) {
        final PageRequest pageRequest = PageRequest.of(page, size);
        final Page<JrStockMvmt> mvmts = mvmtRepository.findAll(pageRequest);
        final Page<JrStockMvmtResponse> responses = mvmts.map(mapper::maps);
        return JrPageResponse.of(responses);
    }

    @Override
    public JrPageResponse<JrStockMvmtResponse> findAllByProductId(final String productId, int page, int size) {
        final PageRequest pageRequest = PageRequest.of(page, size);
        final Page<JrStockMvmt> mvmts = mvmtRepository.findAllByProductId(productId, pageRequest);
        final Page<JrStockMvmtResponse> responses = mvmts.map(mapper::maps);
        return JrPageResponse.of(responses);
    }

    @Override
    public JrStockMvmtResponse findById(String id) {
        return mvmtRepository.findById(id)
                .map(mapper::maps)
                .orElseThrow(() -> new EntityNotFoundException("Movement does not exist."));
    }

    @Override
    public void delete(String id) {
        mvmtRepository.findById(id).ifPresent(mvmtRepository::delete);
    }

    private void checkProductExist(JrStockMvmtRequest request) {
        JrProduct product = productRepository.findById(request.getProductId()).orElseThrow(
                () -> new EntityNotFoundException("Product not found."));

        if (!Objects.equals(request.getProductId(), product.getId())) {
            throw new EntityNotFoundException("Product does not match.");
        }
    }
}

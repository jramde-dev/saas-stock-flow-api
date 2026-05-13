package dev.jramde.saas.service;

import dev.jramde.saas.common.JrPageResponse;
import dev.jramde.saas.dto.request.JrStockMvmtRequest;
import dev.jramde.saas.dto.response.JrStockMvmtResponse;

public interface IStockMvmtService extends IBasicCrudService<JrStockMvmtRequest, JrStockMvmtResponse> {

    JrPageResponse<JrStockMvmtResponse> findAllByProductId(final String productId, final int page, final int size);
}

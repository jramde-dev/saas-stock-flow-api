package dev.jramde.saas.service;

import dev.jramde.saas.common.JrPageResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface IBasicCrudService<I, O> {

    void create(final I request);

    void update(final String id, final I request);

    JrPageResponse<O> findAll(final int page, final int size);

    O findById(final String id);

    void delete(final String id);
}

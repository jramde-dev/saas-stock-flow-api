package dev.jramde.saas.service;

import java.util.List;

public interface IBasicCrudService<I, O> {

    void create(final I request);

    void update(final String id, final I request);

    List<O> findAll(final int page, final int size);

    O findById(final String id);

    void delete(final String id);
}

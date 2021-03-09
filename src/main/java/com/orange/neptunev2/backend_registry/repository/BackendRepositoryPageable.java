package com.orange.neptunev2.backend_registry.repository;

import com.orange.neptunev2.backend_registry.entity.Backend;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BackendRepositoryPageable extends PagingAndSortingRepository<Backend, UUID> {
}

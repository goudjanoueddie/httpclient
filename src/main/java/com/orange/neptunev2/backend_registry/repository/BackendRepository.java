package com.orange.neptunev2.backend_registry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.orange.neptunev2.backend_registry.entity.Backend;
import java.util.UUID;

@Repository
public interface BackendRepository extends JpaRepository<Backend, UUID> {

    boolean existsByName(String name);

    boolean existsByHost(String host);

}

package com.orange.neptunev2.backend_registry.model;

import java.util.Date;

public interface Datable {

    Date getCreatedAt();

    Date getUpdatedAt();

    void notifyUpdated();
}

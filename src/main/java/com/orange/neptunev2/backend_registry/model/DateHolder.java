package com.orange.neptunev2.backend_registry.model;

import java.util.Date;

public interface DateHolder extends com.orange.neptunev2.backend_registry.model.Datable {

    void setCreatedAt(Date date);

    void setUpdatedAt(Date date);

    @Override
    void notifyUpdated();
}

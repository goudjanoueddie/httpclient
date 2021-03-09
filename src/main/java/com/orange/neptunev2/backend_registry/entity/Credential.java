package com.orange.neptunev2.backend_registry.entity;


import javax.persistence.*;
import java.util.UUID;

@Embeddable
public class Credential {

    @Column(columnDefinition ="enum('basic','digest','bearer')" )
    private String type;

    @Column
    private String value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}

package com.orange.neptunev2.backend_registry.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.orange.neptunev2.backend_registry.annotation.UniqueHost;
import com.orange.neptunev2.backend_registry.annotation.UniqueName;
import com.orange.neptunev2.backend_registry.constraint.Create;
import com.orange.neptunev2.backend_registry.constraint.Update;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import com.orange.neptunev2.backend_registry.serialization.GroupOfSerialization;

@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonFilter("BackendFilter")
@Entity
@Table(name="backend")
public class Backend implements Serializable {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    @GeneratedValue(generator = "custom-uuid")
    @GenericGenerator(
            name = "custom-uuid",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
    )
    @JsonView(GroupOfSerialization.Summary.class)
    private UUID id;

    @UniqueName
    @Basic(optional = true)
    @NotBlank
    @NotEmpty(message = "{backend.name.not_empty}",groups={Create.class, Update.class})
    @Column(name= "name", unique = true,nullable = false)
    @JsonView(GroupOfSerialization.Summary.class)
    private String name;

    @UniqueHost
    @Basic(optional = true)
    @NotBlank
    @NotEmpty(message = "{backend.host.not_empty}",groups={Create.class, Update.class})
    @Column(name= "host", unique = true,nullable = false)
    @JsonView(GroupOfSerialization.Summary.class)
    private String host;

    @Column(name="port" ,columnDefinition="integer default 80")
    @JsonView(GroupOfSerialization.Summary.class)
    private Integer port =80;

    @Column(name="enabled" ,columnDefinition="boolean default true")
    @JsonView(GroupOfSerialization.Summary.class)
    private boolean enabled=true;

    @CreationTimestamp
    @Column(name = "createdAt")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonView(GroupOfSerialization.Summary.class)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updatedAt")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonView(GroupOfSerialization.Summary.class)
    private Date updatedAt;

    @Column(columnDefinition ="enum('http','https')")
    @JsonView(GroupOfSerialization.Summary.class)
    private String scheme="http";

    @AttributeOverrides(value ={
            @AttributeOverride(name="type", column=@Column(name="credential_type")),
            @AttributeOverride(name="value", column=@Column(name= "credential_value"))
    })

    @Embedded
    @JsonView(GroupOfSerialization.Details.class)
    private Credential credential;

    public Backend(String name, String host, Integer port, boolean enabled, String scheme, Credential credential) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.enabled = enabled;
        this.scheme = scheme;
        this.credential = credential;
    }

    public Backend(String name, String host){
        this.name = name;
        this.host = host;
    }

    public Backend(){
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }



    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;

    }
}

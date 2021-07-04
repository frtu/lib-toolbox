package com.github.frtu.generators;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "testBean")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "id", "name" })
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestBean {
    @XmlElement(name = "id", required = false)
    private String id;

    @XmlElement(name = "name", required = true)
    private String name;

    public TestBean() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
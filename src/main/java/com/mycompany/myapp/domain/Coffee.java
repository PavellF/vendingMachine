package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Coffee.
 */
@Entity
@Table(name = "coffee")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Coffee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, 
    	generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "title")
    private String title;

    @OneToMany(mappedBy = "coffee")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CofeeMaterial> cofees = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Coffee title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<CofeeMaterial> getCofees() {
        return cofees;
    }

    public Coffee cofees(Set<CofeeMaterial> cofeeMaterials) {
        this.cofees = cofeeMaterials;
        return this;
    }

    public Coffee addCofee(CofeeMaterial cofeeMaterial) {
        this.cofees.add(cofeeMaterial);
        cofeeMaterial.setCoffee(this);
        return this;
    }

    public Coffee removeCofee(CofeeMaterial cofeeMaterial) {
        this.cofees.remove(cofeeMaterial);
        cofeeMaterial.setCoffee(null);
        return this;
    }

    public void setCofees(Set<CofeeMaterial> cofeeMaterials) {
        this.cofees = cofeeMaterials;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Coffee coffee = (Coffee) o;
        if (coffee.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), coffee.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Coffee{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            "}";
    }
}

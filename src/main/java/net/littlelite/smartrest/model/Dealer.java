/*
 * SmartREST Project - Java Edition
 * Copyright (c) Alessio Saltarin, 2026
 * This software is licensed under ISC License
 * See LICENSE
 */

package net.littlelite.smartrest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "dealers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dealer
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String city;

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "dealer_brands",
        joinColumns = @JoinColumn(name = "dealer_id"),
        inverseJoinColumns = @JoinColumn(name = "brand_id")
    )
    private Set<Brand> brands = new HashSet<>();

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "dealer", cascade = CascadeType.ALL)
    private List<Car> cars = new ArrayList<>();

    @PrePersist
    @PreUpdate
    public void validateBrands()
    {
        if (this.brands == null || this.brands.isEmpty() || this.brands.size() > 2)
        {
            throw new IllegalStateException("A car dealer must have exactly one or two brands. Found: "
                    + (this.brands == null ? 0 : this.brands.size()));
        }
    }
}

package com.capstone.cargo.repository;

import com.capstone.cargo.model.Container;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContainerRepository extends JpaRepository <Container, Long>{
    List<Container> findByOwner(String name);
}

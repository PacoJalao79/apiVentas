package com.example.ventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ventas.model.Ventas;

public interface VentasRepository extends JpaRepository<Ventas, Integer> {
}

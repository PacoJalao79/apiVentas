package com.example.ventas.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.ventas.dto.VentasDTO;
import com.example.ventas.model.Ventas;
import com.example.ventas.repository.VentasRepository;

@Service
public class VentasServices {
    
    // Métodos auxiliares
    private VentasDTO toDTO(Ventas ventas) {
        VentasDTO dto = new VentasDTO();
        dto.setIdVenta(ventas.getIdVenta());
        dto.setId_cliente(ventas.getId_cliente());
        dto.setId_vendedor(ventas.getId_vendedor());
        dto.setFeha_venta(ventas.getFeha_venta());
        return dto;
    }

    private Ventas toEntity(VentasDTO dto) {
        Ventas ventas = new Ventas();
        ventas.setIdVenta(dto.getIdVenta());
        ventas.setId_cliente(dto.getId_cliente());
        ventas.setId_vendedor(dto.getId_vendedor());
        ventas.setFeha_venta(dto.getFeha_venta());
        return ventas;
    }

    @Autowired
    private VentasRepository repository;

    public VentasDTO guardar(VentasDTO dto) {
        Ventas ventas = toEntity(dto);
        Ventas saved = repository.save(ventas);
        return toDTO(saved);
    }

    public List<VentasDTO> listar() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<VentasDTO> obtenerPorId(Integer id) {
        return repository.findById(id)
                .map(this::toDTO);
    }

    public Optional<VentasDTO> actualizar(Integer id, VentasDTO dto) {
        return repository.findById(id).map(ventas -> {
            ventas.setIdVenta(dto.getIdVenta());
            ventas.setId_cliente(dto.getId_cliente());
            ventas.setId_vendedor(dto.getId_vendedor());
            ventas.setFeha_venta(dto.getFeha_venta());
            return toDTO(repository.save(ventas));
        });
    }

    public boolean eliminar(Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

}

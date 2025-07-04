package com.example.ventas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.example.ventas.dto.VentasDTO;
import com.example.ventas.services.VentasServices;

@RestController
@RequestMapping("/api/venta")
public class VentasController {
    
     @Autowired
    private VentasServices service;

    @PostMapping
    public ResponseEntity<VentasDTO> crear(@RequestBody VentasDTO dto) {
        return ResponseEntity.ok(service.guardar(dto));
    }

    @GetMapping
    public ResponseEntity<List<VentasDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentasDTO> obtener(@PathVariable Integer id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<VentasDTO> actualizar(@PathVariable Integer id, @RequestBody VentasDTO dto) {
        return service.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return service.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

            //METODO HATEOAS para buscar por ID
    @GetMapping("/hateoas/{id}")
    public ResponseEntity<VentasDTO> obtenerHATEOAS(@PathVariable Integer id) {
        return service.obtenerPorId(id)
            .map(dto -> {
                // Agregar los links HATEOAS
                dto.add(linkTo(methodOn(VentasController.class).obtenerHATEOAS(id)).withSelfRel());
                dto.add(linkTo(methodOn(VentasController.class).obtenerTodosHATEOAS()).withRel("todos"));
                dto.add(linkTo(methodOn(VentasController.class).eliminar(id)).withRel("eliminar"));

                dto.add(Link.of("http://localhost:8888/api/proxy/productos/" + dto.getIdVenta()).withSelfRel());
                dto.add(Link.of("http://localhost:8888/api/proxy/productos/" + dto.getIdVenta()).withRel("Modificar HATEOAS").withType("PUT"));
                dto.add(Link.of("http://localhost:8888/api/proxy/productos/" + dto.getIdVenta()).withRel("Eliminar HATEOAS").withType("DELETE"));

                return ResponseEntity.ok(dto);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
}

    //METODO HATEOAS para listar todos los productos utilizando HATEOAS
    @GetMapping("/hateoas")
    public List<VentasDTO> obtenerTodosHATEOAS() {
        List<VentasDTO> lista = service.listar();

        for (VentasDTO dto : lista) {
            //link url de la misma API
            dto.add(linkTo(methodOn(VentasController.class).obtenerHATEOAS(dto.getIdVenta())).withSelfRel());

            //link HATEOAS para API Gateway "A mano"
            dto.add(Link.of("http://localhost:8888/api/proxy/productos").withRel("Get todos HATEOAS"));
            dto.add(Link.of("http://localhost:8888/api/proxy/productos/" + dto.getIdVenta()).withRel("Crear HATEOAS").withType("POST"));
        }

        return lista;
    }

}

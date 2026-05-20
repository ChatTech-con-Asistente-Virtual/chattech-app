package com.librotech.controller;

import com.librotech.model.Libro;
import com.librotech.service.LibroService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping
    public ResponseEntity<Page<Libro>> listar(
            @PageableDefault(size = 10, sort = "titulo") Pageable pageable) {
        Page<Libro> libros = libroService.listarPaginado(pageable);
        return ResponseEntity.ok(libros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<Libro> libro = libroService.obtenerPorId(id);

        if (libro.isPresent()) {
            return ResponseEntity.ok(libro.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Libro con ID " + id + " no encontrado.");
    }

    @GetMapping("/autor/{autor}")
    public ResponseEntity<List<Libro>> buscarPorAutor(@PathVariable String autor) {
        List<Libro> libros = libroService.buscarPorAutor(autor);
        return ResponseEntity.ok(libros);
    }

    @PostMapping
    public ResponseEntity<Libro> crear(@RequestBody Libro libro) {
        Libro createdLibro = libroService.guardar(libro);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLibro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Libro libro) {
        Optional<Libro> actualizado = libroService.actualizar(id, libro);

        if (actualizado.isPresent()) {
            return ResponseEntity.ok(actualizado.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No se pudo actualizar. Libro no encontrado.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (libroService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}

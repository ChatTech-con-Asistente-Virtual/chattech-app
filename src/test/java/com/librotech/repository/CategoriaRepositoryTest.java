package com.librotech.repository;

import com.librotech.model.Categoria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CategoriaRepositoryTest {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    @DisplayName("Deberia guardar una categoria y asignarle un ID")
    void guardarCategoriaTest() {
        Categoria categoria = new Categoria(null, "Ficcion");

        Categoria guardada = categoriaRepository.save(categoria);

        assertNotNull(guardada.getId());
        assertEquals("Ficcion", guardada.getNombre());
    }

    @Test
    @DisplayName("Deberia encontrar una categoria por nombre")
    void buscarPorNombreTest() {
        categoriaRepository.save(new Categoria(null, "Historia"));

        Optional<Categoria> resultado = categoriaRepository.findByNombre("Historia");

        assertTrue(resultado.isPresent());
        assertEquals("Historia", resultado.get().getNombre());
    }

    @Test
    @DisplayName("Deberia eliminar una categoria correctamente")
    void eliminarCategoriaTest() {
        Categoria categoria = categoriaRepository.save(new Categoria(null, "Terror"));
        Long id = categoria.getId();

        categoriaRepository.deleteById(id);
        Optional<Categoria> resultado = categoriaRepository.findById(id);

        assertTrue(resultado.isEmpty());
    }
}

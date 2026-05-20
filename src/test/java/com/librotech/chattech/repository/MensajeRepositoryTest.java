package com.librotech.chattech.repository;

import com.librotech.chattech.model.Mensaje;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class MensajeRepositoryTest {

    private final MensajeRepository mensajeRepository;

    @Autowired
    MensajeRepositoryTest(MensajeRepository mensajeRepository) {
        this.mensajeRepository = mensajeRepository;
    }

    @BeforeEach
    void setUp() {
        mensajeRepository.deleteAll();
    }

    @Test
    void shouldSaveMessage() {
        Mensaje mensaje = new Mensaje("Luis", "Hola desde MongoDB");

        Mensaje resultado = mensajeRepository.save(mensaje);

        assertNotNull(resultado.getId());
        assertEquals("Luis", resultado.getRemitente());
        assertEquals("Hola desde MongoDB", resultado.getContenido());
        assertNotNull(resultado.getFechaEnvio());
    }

    @Test
    void shouldFindTop10ByOrderByFechaEnvioDesc() {
        LocalDateTime baseDate = LocalDateTime.of(2026, 5, 20, 10, 0);

        for (int i = 1; i <= 12; i++) {
            Mensaje mensaje = new Mensaje("Usuario " + i, "Mensaje " + i);
            mensaje.setFechaEnvio(baseDate.plusMinutes(i));
            mensajeRepository.save(mensaje);
        }

        List<Mensaje> resultado = mensajeRepository.findTop10ByOrderByFechaEnvioDesc();

        assertEquals(10, resultado.size());
        assertEquals("Mensaje 12", resultado.get(0).getContenido());
        assertEquals("Mensaje 3", resultado.get(9).getContenido());
    }
}
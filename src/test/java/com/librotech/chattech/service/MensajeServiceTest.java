package com.librotech.chattech.service;

import com.librotech.chattech.model.Mensaje;
import com.librotech.chattech.repository.MensajeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MensajeServiceTest {

    @Mock
    private MensajeRepository mensajeRepository;

    @InjectMocks
    private MensajeService mensajeService;

    @Test
    void shouldSaveMessage() {
        Mensaje mensaje = new Mensaje("Luis", "Hola equipo");
        Mensaje mensajeGuardado = new Mensaje("1", "Luis", "Hola equipo", LocalDateTime.now());

        when(mensajeRepository.save(mensaje)).thenReturn(mensajeGuardado);

        Mensaje resultado = mensajeService.guardarMensaje(mensaje);

        assertNotNull(resultado);
        assertEquals("1", resultado.getId());
        assertEquals("Luis", resultado.getRemitente());
        assertEquals("Hola equipo", resultado.getContenido());

        verify(mensajeRepository, times(1)).save(mensaje);
    }

    @Test
    void shouldReturnHistoryOrderedByFechaEnvioAsc() {
        List<Mensaje> historial = List.of(
                new Mensaje("Luis", "Primer mensaje"),
                new Mensaje("LibroBot IA", "Respuesta")
        );

        when(mensajeRepository.findAll(any(Sort.class))).thenReturn(historial);

        List<Mensaje> resultado = mensajeService.obtenerHistorial();

        assertEquals(2, resultado.size());

        ArgumentCaptor<Sort> sortCaptor = ArgumentCaptor.forClass(Sort.class);
        verify(mensajeRepository).findAll(sortCaptor.capture());

        Sort sort = sortCaptor.getValue();
        assertNotNull(sort.getOrderFor("fechaEnvio"));
        assertEquals(Sort.Direction.ASC, sort.getOrderFor("fechaEnvio").getDirection());
    }

    @Test
    void shouldReturnRecentHistoryInChronologicalOrder() {
        LocalDateTime now = LocalDateTime.now();

        Mensaje tercero = new Mensaje("3", "Camilo", "Tercero", now.plusMinutes(3));
        Mensaje segundo = new Mensaje("2", "Alejandro", "Segundo", now.plusMinutes(2));
        Mensaje primero = new Mensaje("1", "Luis", "Primero", now.plusMinutes(1));

        List<Mensaje> repositoryResultDesc = new ArrayList<>(List.of(tercero, segundo, primero));

        when(mensajeRepository.findTop10ByOrderByFechaEnvioDesc()).thenReturn(repositoryResultDesc);

        List<Mensaje> resultado = mensajeService.obtenerHistorialReciente();

        assertEquals(3, resultado.size());
        assertEquals("Primero", resultado.get(0).getContenido());
        assertEquals("Segundo", resultado.get(1).getContenido());
        assertEquals("Tercero", resultado.get(2).getContenido());

        verify(mensajeRepository, times(1)).findTop10ByOrderByFechaEnvioDesc();
    }

    @Test
    void shouldRejectNullMessage() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> mensajeService.guardarMensaje(null)
        );

        assertEquals("El mensaje no puede ser nulo.", exception.getMessage());
        verify(mensajeRepository, never()).save(any());
    }

    @Test
    void shouldRejectBlankSender() {
        Mensaje mensaje = new Mensaje("", "Hola");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> mensajeService.guardarMensaje(mensaje)
        );

        assertEquals("El remitente es obligatorio.", exception.getMessage());
        verify(mensajeRepository, never()).save(any());
    }

    @Test
    void shouldRejectBlankContent() {
        Mensaje mensaje = new Mensaje("Luis", "");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> mensajeService.guardarMensaje(mensaje)
        );

        assertEquals("El contenido del mensaje es obligatorio.", exception.getMessage());
        verify(mensajeRepository, never()).save(any());
    }
}
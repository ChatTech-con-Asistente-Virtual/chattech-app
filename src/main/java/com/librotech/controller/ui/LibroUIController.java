package com.librotech.controller.ui;

import com.librotech.model.Libro;
import com.librotech.service.LibroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/libros")
public class LibroUIController {

    private final LibroService libroService;

    public LibroUIController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping
    public String listarLibrosUI(Model model) {
        List<Libro> libros = libroService.obtenerTodos();

        model.addAttribute("libros", libros);
        model.addAttribute("tituloPantalla", "Catalogo de Libros - Dashboard");

        return "libros/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("libro", new Libro());
        model.addAttribute("tituloPantalla", "Registrar Nuevo Libro");

        return "libros/formulario";
    }

    @PostMapping("/guardar")
    public String guardarLibro(@ModelAttribute("libro") Libro libro, Model model) {
        int anioActual = LocalDate.now().getYear();

        if (libro.getAnioPublicacion() > anioActual) {
            model.addAttribute("errorAnio", "El anio de publicacion no puede ser mayor al anio actual (" + anioActual + ").");
            model.addAttribute("tituloPantalla", "Registrar Nuevo Libro (Correccion)");
            return "libros/formulario";
        }

        libroService.guardar(libro);
        return "redirect:/admin/libros";
    }
}

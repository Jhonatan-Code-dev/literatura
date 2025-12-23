package com.example.literatura.service;

import com.example.literatura.client.GutendexClient;
import com.example.literatura.domain.model.Autor;
import com.example.literatura.domain.model.Libro;
import com.example.literatura.domain.repository.AutorRepository;
import com.example.literatura.domain.repository.LibroRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Scanner;

@Service
public class GutendexService {

    private final GutendexClient client = new GutendexClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    public GutendexService(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void menu() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("""
                    
                    1. Buscar libro por título
                    2. Listar todos los libros
                    3. Listar libros por idioma
                    4. Listar todos los autores
                    5. Listar autores vivos en un año
                    6. Salir
                    """);

            System.out.print("Seleccione una opción: ");

            if (!sc.hasNextInt()) {
                System.out.println("Entrada inválida. Intente nuevamente.");
                sc.nextLine();
                continue;
            }

            int opcion = sc.nextInt();
            sc.nextLine();

            try {
                switch (opcion) {
                    case 1 -> {
                        System.out.print("Ingrese título: ");
                        buscarLibro(sc.nextLine());
                    }
                    case 2 -> listarTodosLibros();
                    case 3 -> {
                        System.out.print("Ingrese idioma: ");
                        listarLibrosPorIdioma(sc.nextLine());
                    }
                    case 4 -> listarTodosAutores();
                    case 5 -> {
                        System.out.print("Ingrese año: ");
                        if (sc.hasNextInt()) {
                            listarAutoresVivos(sc.nextInt());
                            sc.nextLine();
                        } else {
                            System.out.println("Año inválido.");
                            sc.nextLine();
                        }
                    }
                    case 6 -> {
                        System.out.println("Saliendo...");
                        return;
                    }
                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    @Transactional
    public void buscarLibro(String titulo) throws Exception {

        if (libroRepository.findByTituloIgnoreCase(titulo).isPresent()) {
            System.out.println("El libro ya existe en la base de datos.");
            return;
        }

        String json = client.buscarLibroPorTitulo(titulo);
        JsonNode root = mapper.readTree(json);

        if (root.get("results").isEmpty()) {
            System.out.println("No se encontraron resultados.");
            return;
        }

        JsonNode result = root.get("results").get(0);
        JsonNode authorNode = result.get("authors").get(0);

        String nombreAutor = authorNode.get("name").asText();

        Autor autor = autorRepository
                .findByNombre(nombreAutor)
                .orElseGet(() -> {
                    Autor nuevo = new Autor();
                    nuevo.setNombre(nombreAutor);
                    nuevo.setBirthYear(authorNode.get("birth_year").isNull() ? null : authorNode.get("birth_year").asInt());
                    nuevo.setDeathYear(authorNode.get("death_year").isNull() ? null : authorNode.get("death_year").asInt());
                    return autorRepository.save(nuevo);
                });

        Libro libro = new Libro();
        libro.setTitulo(result.get("title").asText());
        libro.setIdioma(result.get("languages").get(0).asText());
        libro.setDownloadCount(result.get("download_count").asInt());
        libro.setAutor(autor);

        libroRepository.save(libro);

        System.out.println("Libro guardado correctamente: " + libro.getTitulo());
    }

    private void listarTodosLibros() {
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros.");
            return;
        }
        libros.forEach(System.out::println);
    }

    private void listarLibrosPorIdioma(String idioma) {
        List<Libro> libros = libroRepository.findByIdioma(idioma);
        if (libros.isEmpty()) {
            System.out.println("No hay libros en ese idioma.");
            return;
        }
        libros.forEach(System.out::println);
    }

    private void listarTodosAutores() {
        autorRepository.findAll().forEach(System.out::println);
    }

    private void listarAutoresVivos(int year) {
        autorRepository.findAll().stream()
                .filter(a -> (a.getBirthYear() == null || a.getBirthYear() <= year) &&
                        (a.getDeathYear() == null || a.getDeathYear() >= year))
                .forEach(System.out::println);
    }
}
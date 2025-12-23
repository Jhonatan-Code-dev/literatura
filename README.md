# Literatura

Aplicación de consola desarrollada con Spring Boot que permite buscar libros desde la API pública Gutendex, almacenar la información en una base de datos PostgreSQL y realizar consultas sobre libros y autores utilizando JPA e Hibernate.

---

## Descripción del proyecto

Este proyecto consume la API de Gutendex para obtener información de libros del dominio público. Los datos obtenidos se procesan y se almacenan en una base de datos relacional, permitiendo realizar búsquedas y listados desde un menú interactivo en consola.

La aplicación sigue una arquitectura en capas y aplica buenas prácticas de desarrollo con Spring Boot y JPA.

---

## Funcionalidades

La aplicación ofrece las siguientes opciones a través de un menú en consola:

1. Buscar un libro por título y guardarlo en la base de datos.
2. Listar todos los libros almacenados.
3. Listar libros filtrados por idioma.
4. Listar todos los autores registrados.
5. Listar autores que estaban vivos en un año específico.
6. Salir de la aplicación.

---

## Arquitectura del proyecto

El proyecto está organizado en las siguientes capas:

### 1. Capa de dominio (`domain`)
Contiene las entidades principales del sistema.

- **Autor**
    - Nombre
    - Año de nacimiento
    - Año de fallecimiento
- **Libro**
    - Título
    - Idioma
    - Cantidad de descargas
    - Relación ManyToOne con Autor

Las entidades están mapeadas con JPA y gestionadas por Hibernate.

---

### 2. Capa de repositorio (`repository`)
Utiliza Spring Data JPA para el acceso a datos.

- `AutorRepository`
- `LibroRepository`

Permite realizar consultas sin necesidad de escribir SQL manual, usando métodos derivados por nombre.

---

### 3. Capa de servicio (`service`)
Contiene la lógica de negocio principal.

- Gestión del menú interactivo en consola.
- Consumo de la API externa.
- Validación de datos antes de persistir.
- Control de duplicados (libros y autores).
- Uso de transacciones para garantizar integridad de datos.

---

### 4. Cliente HTTP (`client`)
Encargado de consumir la API de Gutendex usando `HttpClient`.

- Realiza peticiones HTTP GET.
- Retorna la respuesta en formato JSON.

---

## Persistencia de datos

- Base de datos: **PostgreSQL**
- ORM: **Hibernate**
- Framework de persistencia: **Spring Data JPA**

Las relaciones entre entidades se gestionan correctamente evitando duplicados y errores de estado de entidades.


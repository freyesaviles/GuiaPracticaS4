# Guía Práctica S4 — Servicios Web con Spring Boot

API REST desarrollada con Spring Boot para practicar DTO, validaciones, respuestas JSON y los métodos HTTP `GET`, `POST`, `PUT` y `DELETE`.

El proyecto contiene dos casos:

- El CRUD de empleados solicitado en los puntos 1–7.
- El CRUD de resultados del test de inteligencia de Weill correspondiente al reto del punto 8.

Los datos se almacenan temporalmente en memoria y se pierden al detener la aplicación. No se utiliza una base de datos.

## Ejecutar el proyecto

Se necesita JDK 17 o posterior. En una terminal ubicada en la raíz del proyecto:

```bash
./mvnw spring-boot:run
```

La API estará disponible en `http://localhost:8080`.

Para ejecutar las pruebas automatizadas:

```bash
./mvnw test
```

## Endpoints

### Empleados

| Método | Ruta | Resultado |
| --- | --- | --- |
| GET | `/api/empleados` | Lista todos los empleados |
| GET | `/api/empleados/{id}` | Busca un empleado |
| POST | `/api/empleados` | Registra un empleado |
| PUT | `/api/empleados/{id}` | Actualiza un empleado |
| DELETE | `/api/empleados/{id}` | Elimina un empleado |

Ejemplo para `POST` y `PUT`:

```json
{
  "nombres": "Ana María",
  "apellidos": "López Pérez",
  "cargo": "Analista de sistemas",
  "salario": 18500.00
}
```

### Resultados del test de Weill

| Método | Ruta | Resultado |
| --- | --- | --- |
| GET | `/api/resultados-weill` | Lista todos los resultados |
| GET | `/api/resultados-weill/{id}` | Busca un resultado |
| POST | `/api/resultados-weill` | Registra un resultado |
| PUT | `/api/resultados-weill/{id}` | Actualiza un resultado |
| DELETE | `/api/resultados-weill/{id}` | Elimina un resultado |

Ejemplo para `POST` y `PUT`:

```json
{
  "nombreParticipante": "Carlos Martínez",
  "edad": 21,
  "puntaje": 78,
  "clasificacion": "Superior al promedio",
  "fechaRealizacion": "2024-06-15"
}
```

El modelo posee seis atributos contando el identificador. Valida que el nombre y la clasificación no estén vacíos, que la edad esté entre 6 y 100 años, que el puntaje esté entre 0 y 100 y que la fecha no sea futura.

## Pruebas con Postman

1. Inicie la aplicación.
2. Abra Postman y seleccione **Import**.
3. Importe `postman/Guia-Practica-S4.postman_collection.json`.
4. Ejecute primero la petición de registro de cada carpeta. La colección guarda automáticamente el identificador recibido.
5. Ejecute las demás peticiones en el orden indicado y capture la pestaña de respuesta mostrando el método, la ruta, el código HTTP y el JSON.

Conviene tomar como mínimo estas evidencias:

- `GET` de la lista: `200 OK`.
- `POST` válido: `201 Created`.
- `PUT` válido: `200 OK`.
- `DELETE` válido: `200 OK`.
- `POST` inválido: `400 Bad Request`.
- `GET` con el identificador `999999`: `404 Not Found`.

El JSON inválido de empleados responde con `400` porque los nombres y el cargo incumplen `@NotBlank`, y el salario `0` incumple `@DecimalMin`, que exige un valor mayor que cero. `GlobalExceptionHandler` reúne estas faltas y las devuelve como un objeto JSON con estado, mensaje y errores por campo.

## Preguntas de cierre

1. **¿Qué diferencia existe entre `@Controller` y `@RestController`?**  
   `@Controller` se utiliza normalmente para controladores que devuelven vistas. `@RestController` combina `@Controller` y `@ResponseBody`, por lo que escribe los datos devueltos directamente en el cuerpo de la respuesta HTTP, generalmente como JSON.

2. **¿Qué función cumple `@RequestBody`?**  
   Indica que Spring debe leer el cuerpo JSON de la solicitud y convertirlo en el objeto Java declarado como parámetro.

3. **¿Por qué se utiliza `@Valid` junto al DTO?**  
   Activa las reglas de validación declaradas en el DTO antes de ejecutar el método. Si algún dato no es válido, Spring produce una excepción que el manejador global transforma en una respuesta JSON `400 Bad Request`.

4. **¿Qué código HTTP debe devolver un registro creado correctamente?**  
   `201 Created`.

5. **¿Cómo interviene Jackson en las respuestas de la API?**  
   Jackson serializa los objetos Java que devuelve el controlador a JSON y deserializa el JSON recibido para construir los DTO.

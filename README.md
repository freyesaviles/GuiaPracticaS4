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
4. Compruebe que la variable `baseUrl` tenga el valor `http://localhost:8080`.
5. Ejecute las peticiones de cada carpeta en el orden indicado. El primer `POST` guarda automáticamente el identificador recibido para las siguientes peticiones.
6. En cada evidencia capture el nombre y método de la solicitud, la URL, el código HTTP y el cuerpo JSON de la respuesta.

### Pruebas del ejercicio: empleados

Ejecute estas pruebas en el orden indicado:

| N.º | Método y ruta | Datos o propósito | Código esperado |
| --- | --- | --- | --- |
| 1 | `POST /api/empleados` | Registrar a Ana María con el JSON válido | `201 Created` |
| 2 | `GET /api/empleados` | Comprobar que el registro aparece en la lista | `200 OK` |
| 3 | `GET /api/empleados/{{empleadoId}}` | Consultar el empleado recién creado | `200 OK` |
| 4 | `PUT /api/empleados/{{empleadoId}}` | Cambiar cargo y salario | `200 OK` |
| 5 | `DELETE /api/empleados/{{empleadoId}}` | Eliminar el registro | `200 OK` |
| 6 | `POST /api/empleados` | Enviar nombres y cargo vacíos, y salario `0` | `400 Bad Request` |
| 7 | `GET /api/empleados/999999` | Buscar un identificador inexistente | `404 Not Found` |

JSON de la prueba 1:

```json
{
  "nombres": "Ana María",
  "apellidos": "López Pérez",
  "cargo": "Analista de sistemas",
  "salario": 18500.00
}
```

JSON de la prueba 4:

```json
{
  "nombres": "Ana María",
  "apellidos": "López Pérez",
  "cargo": "Arquitecta de software",
  "salario": 22000.00
}
```

JSON inválido de la prueba 6:

```json
{
  "nombres": "",
  "apellidos": "Pérez",
  "cargo": "",
  "salario": 0
}
```

La última solicitud debe producir una respuesta semejante a esta:

```json
{
  "estado": 400,
  "mensaje": "Los datos enviados no son válidos",
  "errores": {
    "nombres": "Los nombres son obligatorios",
    "cargo": "El cargo es obligatorio",
    "salario": "El salario debe ser mayor que 0"
  }
}
```

Responde con `400` porque los nombres y el cargo incumplen `@NotBlank`, y el salario `0` incumple `@DecimalMin`. El orden de los campos dentro de `errores` puede variar sin afectar el resultado.

### Pruebas de la actividad 8: resultados Weill

Ejecute también estas pruebas en orden:

| N.º | Método y ruta | Datos o propósito | Código esperado |
| --- | --- | --- | --- |
| 1 | `POST /api/resultados-weill` | Registrar un resultado válido | `201 Created` |
| 2 | `GET /api/resultados-weill` | Comprobar que aparece en la lista | `200 OK` |
| 3 | `GET /api/resultados-weill/{{resultadoWeillId}}` | Consultar el resultado creado | `200 OK` |
| 4 | `PUT /api/resultados-weill/{{resultadoWeillId}}` | Actualizar puntaje y clasificación | `200 OK` |
| 5 | `DELETE /api/resultados-weill/{{resultadoWeillId}}` | Eliminar el resultado | `200 OK` |
| 6 | `POST /api/resultados-weill` | Enviar un resultado que incumple las validaciones | `400 Bad Request` |
| 7 | `GET /api/resultados-weill/999999` | Buscar un resultado inexistente | `404 Not Found` |

JSON de la prueba 1:

```json
{
  "nombreParticipante": "Carlos Martínez",
  "edad": 21,
  "puntaje": 78,
  "clasificacion": "Superior al promedio",
  "fechaRealizacion": "2024-06-15"
}
```

JSON de la prueba 4:

```json
{
  "nombreParticipante": "Carlos Martínez",
  "edad": 21,
  "puntaje": 82,
  "clasificacion": "Superior",
  "fechaRealizacion": "2024-06-15"
}
```

JSON inválido de la prueba 6:

```json
{
  "nombreParticipante": "",
  "edad": 4,
  "puntaje": 110,
  "clasificacion": "",
  "fechaRealizacion": "2999-01-01"
}
```

Esta prueba debe responder con `400` y mostrar errores para `nombreParticipante`, `edad`, `puntaje`, `clasificacion` y `fechaRealizacion`. La prueba 7 debe responder:

```json
{
  "mensaje": "Resultado Weill no encontrado"
}
```

Al terminar habrá evidencia de los cinco endpoints de cada caso y de los códigos HTTP `200`, `201`, `400` y `404` solicitados en la guía.

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

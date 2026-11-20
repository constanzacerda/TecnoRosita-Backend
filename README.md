# TecnoRosita — Backend API REST (Spring Boot + JWT)

![Java](https://img.shields.io/badge/Java-17-f97316?style=for-the-badge)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-22c55e?style=for-the-badge)
![MySQL](https://img.shields.io/badge/MySQL-tecnorosita__v2-0ea5e9?style=for-the-badge)
![JWT](https://img.shields.io/badge/Security-JWT%20%2B%20BCrypt-38bdf8?style=for-the-badge)
![Swagger](https://img.shields.io/badge/Docs-Swagger%20OpenAPI%203-85ea2d?style=for-the-badge)

API RESTful desarrollada para la plataforma e-commerce **TecnoRosita** en el marco de la asignatura **Desarrollo Fullstack II (DSY1104 — Duoc UC)** por **Constanza Camila Cerda Bravo** y **Juan Carlos Alvarado Ortega**.

Gestiona la autenticación sin estado con **JWT**, control de acceso basado en roles (`ADMINISTRADOR`, `VENDEDOR`, `CLIENTE`), catálogo de productos y categorías, emisión transaccional de boletas con descuento real de stock en **MySQL** y documentación interactiva con **Swagger UI**.

> [!TIP]
> **Seeder automático incluido (`DatabaseSeeder`):** Al arrancar el backend con la base de datos vacía, el sistema inserta automáticamente **3 usuarios de prueba**, **11 categorías** y **44 productos** (incluyendo ítems con stock normal, stock bajo `≤ 5` y sin stock `= 0` para validar filtros de inventario).

---

## 1. Stack Tecnológico y Dependencias

* **Lenguaje y Framework:** Java 17, Spring Boot `3.3.5` (Maven Wrapper incluido).
* **Seguridad:** Spring Security (`SessionCreationPolicy.STATELESS`), `jjwt 0.11.5` (tokens firmados con HMAC-SHA y vigencia de 10 horas) y encriptación `BCryptPasswordEncoder`.
* **Persistencia:** Spring Data JPA / Hibernate + MySQL Connector (`tecnorosita_v2`).
* **Productividad y Validación:** Lombok `1.18.42`, Spring Boot Starter Validation.
* **Documentación API:** `springdoc-openapi-starter-webmvc-ui 2.5.0` (Swagger UI con esquema `bearerAuth`).
* **Pruebas:** Spring Boot Starter Test + Base de datos en memoria **H2** (`SeguridadIntegracionTest`).

### Estructura del Proyecto (`src/main/java/tecnorosita/backend/`)

```text
src/
├── main/
│   ├── java/tecnorosita/backend/
│   │   ├── config/
│   │   │   ├── CorsConfig.java          # Configuración CORS para frontend local
│   │   │   └── SwaggerConfig.java       # OpenAPI 3 + esquema Bearer JWT
│   │   ├── controller/
│   │   │   ├── AuthController.java      # /api/v1/auth (login y registro)
│   │   │   ├── BoletaController.java    # /api/v1/boletas (checkout transaccional)
│   │   │   ├── CategoriaController.java # /api/v1/categorias
│   │   │   ├── ProductoController.java  # /api/v1/productos (CRUD catálogo)
│   │   │   └── UsuarioController.java   # /api/v1/usuarios (solo ADMINISTRADOR)
│   │   ├── dto/                         # DTOs de entrada/salida (sin exponer contraseñas)
│   │   ├── model/                       # Entidades JPA (Usuario, Producto, Categoria, Boleta, DetalleBoleta)
│   │   ├── repository/                  # Repositorios Spring Data JPA
│   │   ├── security/
│   │   │   ├── JwtRequestFilter.java    # Filtro por petición que extrae rol y usuario del JWT
│   │   │   ├── JwtUtil.java             # Generación y validación de tokens JWT
│   │   │   └── SecurityConfig.java      # Reglas de autorización por ruta y rol
│   │   ├── seeder/
│   │   │   └── DatabaseSeeder.java      # Carga inicial de usuarios, categorías y 44 productos
│   │   └── service/
│   │       └── ProductoService.java     # Lógica de negocio de productos y categorías
│   └── resources/
│       └── application.properties       # Configuración MySQL y puerto 8080
└── test/
    └── java/tecnorosita/backend/
        └── SeguridadIntegracionTest.java
```

---

## 2. Endpoints de la API y Control de Roles

Base URL local: **`http://localhost:8080`**

| Módulo | Método | Endpoint | Acceso / Rol Requerido | Descripción |
| :--- | :--- | :--- | :--- | :--- |
| **Auth** | `POST` | `/api/v1/auth/login` | Público | Valida credenciales y retorna JWT + datos del usuario. |
| **Auth** | `POST` | `/api/v1/auth/registro` | Público | Registra cuenta con rol `CLIENTE` y retorna JWT (`201`). |
| **Productos** | `GET` | `/api/v1/productos` | Público | Lista todos los productos del catálogo. |
| **Productos** | `GET` | `/api/v1/productos/{id}` | Público | Obtiene el detalle de un producto por ID. |
| **Productos** | `POST` | `/api/v1/productos` | `ADMINISTRADOR` | Crea un nuevo producto (stock por defecto: `15`). |
| **Productos** | `PUT` | `/api/v1/productos/{id}` | `ADMINISTRADOR` | Actualiza datos, precio, stock o categoría de un producto. |
| **Productos** | `DELETE` | `/api/v1/productos/{id}` | `ADMINISTRADOR` | Elimina un producto del catálogo. |
| **Categorías** | `GET` | `/api/v1/categorias` | Público | Lista las categorías activas. |
| **Categorías** | `POST` | `/api/v1/categorias` | `ADMINISTRADOR` | Crea una nueva categoría. |
| **Boletas** | `GET` | `/api/v1/boletas` | `ADMINISTRADOR`, `VENDEDOR`, `CLIENTE` | Staff ve todas las boletas; `CLIENTE` solo ve sus propias compras. |
| **Boletas** | `POST` | `/api/v1/boletas` | `ADMINISTRADOR`, `VENDEDOR`, `CLIENTE` | Crea boleta y detalles en transacción (`@Transactional`), validando y descontando stock real en MySQL. |
| **Usuarios** | `GET` | `/api/v1/usuarios` | `ADMINISTRADOR` | Lista usuarios registrados mediante `UsuarioResponseDTO` (sin contraseñas). |

### Documentación Interactiva (Swagger UI)
Con el servidor corriendo, accede a:
* **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`
* **OpenAPI JSON:** `http://localhost:8080/v3/api-docs`

Para probar endpoints protegidos en Swagger, inicia sesión en `POST /api/v1/auth/login`, copia el token devuelto y pégalo en el botón **Authorize** (`bearerAuth`).

---

## 3. Credenciales Iniciales (`DatabaseSeeder`)

| Rol | Nombre | Correo | Contraseña |
| :--- | :--- | :--- | :--- |
| **ADMINISTRADOR** | Juan Carlos | `admin@tecnorosita.cl` | `admin123` |
| **VENDEDOR** | Constanza Cerda | `vendedor@tecnorosita.cl` | `vende123` |
| **CLIENTE** | Cliente Frecuente | `cliente@correo.cl` | `cliente123` |

---

## 4. Guía de Instalación y Ejecución (Clonado en Otro Computador)

### Requisitos Previos
* **Java JDK 17** (o superior compatible)
* **MySQL Server** activo

> [!WARNING]
> **Revisa el puerto de MySQL antes de ejecutar:** En `src/main/resources/application.properties` la conexión apunta por defecto al puerto **`3307`** (`jdbc:mysql://localhost:3307/tecnorosita_v2?createDatabaseIfNotExist=true`) con usuario `root` y contraseña vacía. Si tu MySQL local corre en el puerto estándar **`3306`** o tiene contraseña, ajusta esas líneas en `application.properties` antes de iniciar.

### Paso 1: Clonar el repositorio
```bash
git clone [https://github.com/constanzacerda/TecnoRosita-Backend.git](https://github.com/constanzacerda/TecnoRosita-Backend.git)
cd TecnoRosita-Backend
```

### Paso 2: Levantar el servidor Spring Boot
La base de datos `tecnorosita_v2` y sus tablas se crean automáticamente (`createDatabaseIfNotExist=true` y `ddl-auto=update`).

**En Windows (PowerShell / CMD):**
```powershell
.\mvnw.cmd spring-boot:run
```

**En Linux / macOS:**
```bash
./mvnw spring-boot:run
```

*(También puedes abrir la carpeta directamente en **IntelliJ IDEA** y ejecutar la clase principal).* El servidor quedará escuchando en **`http://localhost:8080`**.

---

## 5. Ejecución de Pruebas de Integración

Las pruebas se ejecutan de forma aislada utilizando **H2** en memoria (no modifican tu base de datos MySQL):

**Windows:**
```powershell
.\mvnw.cmd test
```

**Linux / macOS:**
```bash
./mvnw test
```

---

## 6. Repositorio Frontend Relacionado

* **Frontend React + Vite:** [juancajk/TecnoRosita-Duoc](https://github.com/juancajk/TecnoRosita-Duoc) (Fork: [constanzacerda/TecnoRosita-Duoc](https://github.com/constanzacerda/TecnoRosita-Duoc))
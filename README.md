# Mendel Challenge - Transaction Service

API REST para gestión de transacciones con soporte de jerarquías parent-child y cálculos transitivos.

## 🏗️ Arquitectura

Implementada con **Arquitectura Hexagonal (Ports & Adapters)**:

```
                    ┌──────────────────────────────────────┐
                    │   INFRASTRUCTURE LAYER (Adapters)   │
                    │                                      │
    ┌───────────────┤          Controllers (REST)         ├───────────────┐
    │               │   TransactionController.java         │               │
    │               └──────────────────┬───────────────────┘               │
    │                                  │                                   │
    │                                  ▼                                   │
    │               ┌──────────────────────────────────────┐               │
    │               │      APPLICATION LAYER (Core)        │               │
    │               │                                      │               │
    │               │  ┌────────────────────────────────┐ │               │
    │               │  │   Use Cases (Business Logic)   │ │               │
    │               │  │  - TransactionSaveUseCase      │ │               │
    │               │  │  - GetIdsByTypeUseCase         │ │               │
    │               │  │  - GetSumByParentIdUseCase     │ │               │
    │               │  └────────────────────────────────┘ │               │
    │               │            ▲          ▲             │               │
    │               │            │          │             │               │
    │               │  ┌─────────┴──────────┴──────────┐ │               │
    │               │  │   Ports (Interfaces)          │ │               │
    │               │  │   TransactionPort             │ │               │
    │               │  └───────────────────────────────┘ │               │
    │               │                                     │               │
    │               └──────────────────┬──────────────────┘               │
    │                                  │                                  │
    │                                  ▼                                  │
    │               ┌──────────────────────────────────────┐              │
    │               │       DOMAIN LAYER (Entities)        │              │
    │               │                                      │              │
    │               │  - Transaction (Domain Model)        │              │
    │               │  - TransactionException              │              │
    │               │  - Business Rules & Validations      │              │
    │               │                                      │              │
    │               └──────────────────┬───────────────────┘              │
    │                                  │                                  │
    │                                  ▼                                  │
    │               ┌──────────────────────────────────────┐              │
    │               │   INFRASTRUCTURE LAYER (Adapters)    │              │
    └──────────────▶│                                      │◀─────────────┘
                    │  ┌────────────────────────────────┐ │
                    │  │  TransactionAdapter (Port Impl)│ │
                    │  └────────────────┬───────────────┘ │
                    │                   │                  │
                    │                   ▼                  │
                    │  ┌────────────────────────────────┐ │
                    │  │   TransactionRepository (JPA)  │ │
                    │  └────────────────┬───────────────┘ │
                    │                   │                  │
                    │                   ▼                  │
                    │         [H2 Database (Memory)]       │
                    │                                      │
                    └──────────────────────────────────────┘

                    Flujo de Datos:
                    HTTP Request → Controller → Use Case → Port → Adapter → Repository → Database
                    HTTP Response ← Controller ← Use Case ← Domain Model ← Adapter ← Repository
```

### Capas de la Arquitectura

| Capa | Responsabilidad | Componentes |
|------|-----------------|-------------|
| **Infrastructure** | Adaptadores externos (HTTP, DB) | Controllers, Repositories, Entities |
| **Application** | Lógica de negocio y casos de uso | Use Cases, Ports (interfaces) |
| **Domain** | Modelos y reglas de negocio | Transaction, Exceptions, Validations |

## 🚀 Stack Tecnológico

- **Java 21**
- **Spring Boot 3.4.2**
- **Spring Data JPA**
- **H2 Database** (en memoria)
- **Lombok**
- **JUnit 5 + Mockito**
- **Gradle 9.3.0**

## 📋 Prerequisitos

- JDK 21+
- Gradle 9.3.0+ (o usar `./gradlew`)

## ⚙️ Instalación y Ejecución

### Clonar el repositorio
```bash
git clone https://github.com/LucianoPastocchi/mendel-challenge.git
cd mendel-challenge
```

### Ejecutar la aplicación
```bash
./gradlew bootRun
```

La aplicación estará disponible en: `http://localhost:8080`

### Ejecutar tests
```bash
./gradlew test
```

### Generar JAR
```bash
./gradlew build
java -jar build/libs/challenge-1.0.0.jar
```

## 📡 API Endpoints

### 1. Guardar Transacción
```http
PUT /transactions/{transaction_id}
Content-Type: application/json

{
  "amount": 5000.0,
  "type": "cars",
  "parent_id": 10
}
```

**Response:**
```json
{
  "status": "ok"
}
```

**Validaciones:**
- `amount`: Requerido, debe ser positivo
- `type`: Alfanumérico (a-z, A-Z, 0-9, _, -), máximo 50 caracteres
- `parent_id`: Opcional, debe ser positivo si se proporciona

### 2. Obtener IDs por Tipo
```http
GET /transactions/types/{type}
```

**Ejemplo:**
```bash
curl http://localhost:8080/transactions/types/shopping
```

**Response:**
```json
[11, 12, 15]
```

### 3. Obtener Suma Transitiva
Calcula la suma de una transacción más todas sus transacciones hijas transitivamente.

```http
GET /transactions/sum/{transaction_id}
```

**Ejemplo:**
```bash
curl http://localhost:8080/transactions/sum/10
```

**Response:**
```json
{
  "sum": 20000.0
}
```

**Ejemplo de jerarquía:**
```
Transaction 10: amount=5000  (parent)
  └── Transaction 11: amount=10000  (child)
        └── Transaction 12: amount=5000  (grandchild)

GET /transactions/sum/10 → {"sum": 20000.0}
```

## 🛡️ Seguridad

### Protección contra SQL Injection

La aplicación implementa múltiples capas de defensa:

1. **Validaciones en DTO** - Pattern regex para caracteres permitidos
2. **Validaciones en Use Case** - Sanitización y validación de formato
3. **JPA/Hibernate** - Prepared statements automáticos
4. **Spring Validation** - `@Valid`, `@Pattern`, `@Size`

Caracteres bloqueados: `'`, `"`, `;`, `--`, `UNION`, `DROP`, `DELETE`, etc.

## 🧪 Testing

### Estrategia de Testing

```
Use Cases (Unit)     → 100% cobertura
Adapters (Unit)      → 40% cobertura
Controllers (Mock)   → 30% cobertura
Integration          → Casos críticos
```

### Ejecutar tests específicos
```bash
# Tests unitarios de use cases
./gradlew test --tests "*UseCase*"

# Tests de controller
./gradlew test --tests "*ControllerTest"

# Tests de SQL injection
./gradlew test --tests "*SqlInjectionTest"
```

### Cobertura
```bash
./gradlew test jacocoTestReport
open build/reports/jacoco/test/html/index.html
```

## 📊 Base de Datos H2

Console disponible en: `http://localhost:8080/h2-console`

- **JDBC URL:** `jdbc:h2:mem:testdb`
- **User:** `sa`
- **Password:** _(vacío)_

## 🔍 Swagger/OpenAPI

Documentación interactiva de la API:
```
http://localhost:8080/swagger-ui.html
```

## 📝 Ejemplos de Uso

### Escenario completo
```bash
# 1. Crear transacción padre
curl -X PUT http://localhost:8080/transactions/10 \
  -H "Content-Type: application/json" \
  -d '{"amount": 5000, "type": "cars"}'

# 2. Crear transacción hija
curl -X PUT http://localhost:8080/transactions/11 \
  -H "Content-Type: application/json" \
  -d '{"amount": 10000, "type": "shopping", "parent_id": 10}'

# 3. Crear transacción nieta
curl -X PUT http://localhost:8080/transactions/12 \
  -H "Content-Type: application/json" \
  -d '{"amount": 5000, "type": "shopping", "parent_id": 11}'

# 4. Obtener IDs por tipo
curl http://localhost:8080/transactions/types/shopping
# Response: [11, 12]

# 5. Calcular suma transitiva
curl http://localhost:8080/transactions/sum/10
# Response: {"sum": 20000.0}
```

## ⚠️ Manejo de Errores

### Respuestas de Error
```json
{
  "error": "Validation failed: type: must contain only alphanumeric characters"
}
```

### Códigos HTTP
- `200 OK` - Operación exitosa
- `400 BAD REQUEST` - Validación fallida o datos inválidos
- `404 NOT FOUND` - Recurso no encontrado
- `500 INTERNAL SERVER ERROR` - Error del servidor

## 🏛️ Principios de Diseño

- **SOLID** - Separación de responsabilidades
- **Clean Architecture** - Independencia de frameworks
- **Domain-Driven Design** - Lógica de negocio en el dominio
- **Dependency Inversion** - Interfaces como contratos
- **Defense in Depth** - Múltiples capas de seguridad

## 📂 Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/mendel/challenge/
│   │   ├── application/
│   │   │   ├── ports/
│   │   │   │   └── TransactionPort.java
│   │   │   └── usecase/
│   │   │       ├── impl/
│   │   │       │   ├── TransactionSaveUseCaseImpl.java
│   │   │       │   ├── TransactionGetIdsByTypeUseCaseImpl.java
│   │   │       │   └── TransactionGetSumByParentIdUseCaseImpl.java
│   │   │       └── [interfaces]
│   │   ├── domain/
│   │   │   ├── exception/
│   │   │   │   ├── TransactionException.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   └── model/
│   │   │       ├── Transaction.java
│   │   │       └── TransactionRequest.java
│   │   └── infrastructure/
│   │       ├── adapter/
│   │       │   └── TransactionAdapter.java
│   │       ├── controller/
│   │       │   └── TransactionController.java
│   │       ├── entity/
│   │       │   └── TransactionEntity.java
│   │       └── repository/
│   │           └── TransactionRepository.java
│   └── resources/
│       └── application.yaml
└── test/
    └── java/com/mendel/challenge/
        ├── application/usecase/
        ├── infrastructure/
        │   ├── adapter/
        │   └── controller/
        └── integration/
```

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit cambios (`git commit -am 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crear Pull Request

## 👨‍💻 Autor

Desarrollado para Mendel Challenge - Luciano Pastocchi - 2026

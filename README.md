# BarrioDigital

Plataforma para trámites comunales y atención vecinal. Proyecto desarrollado para **DSY1107 — Desarrollo Cloud Native I** (DuocUC).

**Integrantes:** Camilo Queupil · Francisco Rodríguez
**Evaluación actual:** EP1 — Encargo (16%) y EP2 — Presentación (24%)

---

## 1. ¿Qué hace el sistema?

Un municipio y su red de oficinas comunales necesitan digitalizar el ingreso y seguimiento de trámites vecinales (hoy se hacen en papel o por redes sociales). BarrioDigital permite:

- Ingresar trámites desde la web y hacerles seguimiento por estado.
- Autenticación corporativa con **Azure AD** y autorización por rol (`Admin`, `Funcionario`, `Vecino`, `Auditor`).
- Comunicación segura entre frontend y backend vía **JWT** validado en dos capas (API Gateway + BFF).

El documento de diseño completo (arquitectura, contratos de API de los 6 microservicios del caso, y la arquitectura del frontend) está en [`docs/SDD-BarrioDigital.md`](./docs/SDD-BarrioDigital.md).

## 2. Alcance de esta entrega (EP1 / EP2)

El caso completo contempla 6 microservicios (`requests`, `catalog`, `notify`, `report`, `audit`, `bff`) más RabbitMQ y Kafka. **Esta entrega cubre solo lo que exige la rúbrica de EP1/EP2:**

| Componente | Carpeta | Rol en la rúbrica |
|---|---|---|
| Frontend Angular + MSAL | `barriodigital-front-angular/` | Login OIDC, guards por rol, interceptor con Bearer token (60% del indicador MSAL) |
| BFF | `ms-barriodigital-bff/` | Valida el JWT de Azure AD (issuer, audience, firma) y el rol antes de reenviar la petición (40% del indicador BFF) |
| Microservicio de dominio | `ms-barriodigital-requests/` | CRUD de trámites con MySQL, para que el flujo end-to-end sea real y no un mock |

Los microservicios `catalog`, `notify`, `report` y `audit` (que requieren RabbitMQ/Kafka) quedan fuera de esta entrega; se agregan en evaluaciones posteriores del caso.

## 3. Arquitectura y flujo de seguridad

```
Usuario → Login MSAL (Azure AD) → Angular obtiene JWT
   → Angular llama al BFF con Authorization: Bearer <JWT>
   → BFF valida issuer + audience + firma + rol
   → BFF reenvía a ms-barriodigital-requests (mismo JWT en Authorization)
   → ms-barriodigital-requests valida issuer + audience + firma
```

Ningún microservicio de dominio se expone directo al frontend; todo pasa por el BFF. El microservicio `requests` no debe quedar abierto en el Security Group de EC2 (solo `127.0.0.1:8081`).

Guía de despliegue: [`docs/DESPLIEGUE-EC2.md`](./docs/DESPLIEGUE-EC2.md).

## 4. Requisitos previos

- Java 17
- Maven (o el wrapper `mvnw` incluido en cada backend)
- Node.js 18+ y npm
- MySQL 8 corriendo en `localhost:3306`
- Un App Registration en Azure AD (ver sección 6)

## 5. Cómo levantar el proyecto en local

### 5.0 Configuración (una vez)

1. Copia `.env.example` y exporta las variables (PowerShell: `$env:AZURE_TENANT_ID="..."`).
2. En el frontend, edita `barriodigital-front-angular/src/environments/environment.ts` con tu `clientId`, `tenantId` y URLs (plantilla en `environment.example.ts`).
3. Opcional: `docker compose up -d` levanta MySQL en el puerto 3306.

### 5.1 Base de datos

```bash
docker compose up -d
# La app crea la BD sola (createDatabaseIfNotExist=true) si MySQL está accesible.
```

### 5.2 `ms-barriodigital-requests` (puerto 8081)

```bash
cd ms-barriodigital-requests
export DB_USERNAME=root
export DB_PASSWORD=tu_password
./mvnw spring-boot:run
```

### 5.3 `ms-barriodigital-bff` (puerto 8080)

```bash
cd ms-barriodigital-bff
export AZURE_TENANT_ID=<tu-tenant-id>
export AZURE_API_CLIENT_ID=<tu-api-client-id>
export REQUESTS_SERVICE_URL=http://localhost:8081
./mvnw spring-boot:run
```

### 5.4 `barriodigital-front-angular` (puerto 4200)

```bash
cd barriodigital-front-angular
npm install
ng serve
```

Luego abrir `http://localhost:4200` — debe redirigir al login de Microsoft.

## 6. Configuración de Azure AD (resumen)

1. Crear/usar un tenant en Azure AD (Microsoft Entra ID).
2. Registrar la app **BarrioDigital**, tipo **SPA**, redirect URI `http://localhost:4200`.
3. En "Exponer una API", crear el scope `access_as_user` (queda como `api://<API_CLIENT_ID>`).
4. Crear los **App roles**: `Admin`, `Funcionario`, `Vecino`, `Auditor` (respetar mayúsculas; el BFF y los guards los usan tal cual).
5. En **Token configuration** → **Add optional claim** → token **Access** → claim `roles`, para que el BFF pueda autorizar por rol al validar el Bearer token.
6. Crear usuarios de prueba y asignarles rol en "Usuarios y grupos".

Con el `Directory (tenant) ID` y el `Application (client) ID` completa:

- Variables de entorno de la sección 5 (`AZURE_*`, `DB_*`, `CORS_ALLOWED_ORIGINS`).
- `barriodigital-front-angular/src/environments/environment.ts` (desarrollo).
- `environment.prod.ts` antes del build para EC2/API Gateway.

## 7. Usuarios de prueba

| Email | Rol | Contraseña |
|---|---|---|
| admin@\<tudominio\>.onmicrosoft.com | Admin | *(definida al crear el usuario)* |
| funcionario@\<tudominio\>.onmicrosoft.com | Funcionario | *(definida al crear el usuario)* |
| vecino@\<tudominio\>.onmicrosoft.com | Vecino | *(definida al crear el usuario)* |

## 8. Estructura del repo

```
BarrioDigitalApp/
├── barriodigital-front-angular/   # Angular + MSAL
├── ms-barriodigital-bff/          # Spring Boot — valida JWT y reenvía
├── ms-barriodigital-requests/     # Spring Boot + MySQL — CRUD de trámites
├── docs/
│   └── SDD-BarrioDigital.md       # Diseño completo del sistema
└── README.md
```

## 9. Estado actual

- [x] Estructura del monorepo
- [x] Backend: entidad, repository, service, controller de `requests`
- [x] BFF: validación de JWT (issuer/audience/firma) y reenvío por rol
- [x] Frontend: integración MSAL (login, guards, interceptor)
- [x] Frontend: vistas de trámites conectadas al BFF
- [ ] Prueba end-to-end en local (requiere MySQL + Azure AD configurado)
- [ ] Despliegue en AWS (EC2 + API Gateway) — mencionado en la pauta EP1, típico de EP2

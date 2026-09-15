# Despliegue en EC2 (BarrioDigital)

Guía mínima para levantar backend en una instancia Linux y servir el frontend.

## 1. Instancia EC2

- Amazon Linux 2023 o Ubuntu 22.04.
- Security Group:
  - **8080** (BFF) — acceso desde API Gateway o desde el navegador en pruebas.
  - **8081** (requests) — solo tráfico interno (misma VPC / `127.0.0.1`).
  - **3306** — MySQL solo si la BD corre en otra instancia; preferir RDS.
  - **80/443** — si sirves el Angular compilado con nginx.

## 2. Dependencias en la instancia

```bash
sudo yum install -y java-17-amazon-corretto-headless maven nginx
# Node solo si compilas el front en la instancia; también puedes subir dist/ desde tu PC
```

## 3. Variables de entorno

Copia `.env.example` y exporta las variables antes de arrancar los JAR:

```bash
export AZURE_TENANT_ID=...
export AZURE_API_CLIENT_ID=...
export DB_HOST=...
export DB_PASSWORD=...
export REQUESTS_SERVICE_URL=http://127.0.0.1:8081
export CORS_ALLOWED_ORIGINS=http://localhost:4200,http://TU_IP_PUBLICA
export SERVER_ADDRESS=0.0.0.0
```

## 4. Build y ejecución

```bash
cd ms-barriodigital-requests && ./mvnw -DskipTests package
cd ../ms-barriodigital-bff && ./mvnw -DskipTests package

java -jar ms-barriodigital-requests/target/ms-barriodigital-requests-0.0.1-SNAPSHOT.jar &
java -jar ms-barriodigital-bff/target/ms-barriodigital-bff-0.0.1-SNAPSHOT.jar &
```

Recomendación: usar **systemd** con dos unidades (`barriodigital-requests`, `barriodigital-bff`) y `Restart=always`.

## 5. Frontend

1. Edita `barriodigital-front-angular/src/environments/environment.prod.ts`:
   - `redirectUri` = URL pública del SPA (debe coincidir con Azure AD).
   - `bff.baseUrl` = URL del BFF o del **API Gateway** delante del BFF.
2. Build:

```bash
cd barriodigital-front-angular
npm ci
npm run build -- --configuration production
```

3. Copia `dist/` a `/usr/share/nginx/html` y configura nginx con `try_files` para SPA.

## 6. Azure AD en producción

- Agregar redirect URI de producción en la app SPA.
- Mantener el scope `access_as_user` y el claim **roles** en el access token.
- Si usas API Gateway con JWT authorizer, la audience/issuer deben coincidir con lo configurado en el BFF.

## 7. Comprobación

- `GET http://IP:8080/actuator/health` → `UP`
- `GET http://IP:8081/actuator/health` → `UP` (solo red interna)
- Login en el SPA → dashboard con roles → crear/listar trámites según rol.

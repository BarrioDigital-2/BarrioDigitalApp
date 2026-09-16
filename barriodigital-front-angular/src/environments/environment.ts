/**
 * Desarrollo local. Reemplaza los valores REPLACE_* con tus IDs de Azure AD
 * o copia desde environment.example.ts.
 */
export const environment = {
  production: false,
  azure: {
    clientId: 'REPLACE_AZURE_CLIENT_ID',
    tenantId: 'REPLACE_AZURE_TENANT_ID',
    redirectUri: 'http://localhost:4200',
  },
  bff: {
    baseUrl: 'http://localhost:8080',
    get scope(): string {
      return `api://${environment.azure.clientId}/access_as_user`;
    },
  },
};

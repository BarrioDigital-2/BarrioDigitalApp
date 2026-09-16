/**
 * Desarrollo local. Copia desde environment.example.ts si necesitas
 * apuntar a tus propios valores de Azure AD.
 */
export const environment = {
  production: false,
  azure: {
    clientId: '8b0cc19f-2671-49c7-beff-816087690691',
    tenantId: '92b9ffd8-4da1-4956-b85b-62654b654d25',
    redirectUri: 'http://localhost:4200',
  },
  bff: {
    baseUrl: 'http://localhost:8080/api',
    get scope(): string {
      return `api://${environment.azure.clientId}/access_as_user`;
    },
  },
};

/**
 * Build de producción (EC2 / API Gateway). Ajusta redirectUri y baseUrl
 * a las URLs públicas antes de `ng build --configuration production`.
 */
export const environment = {
  production: true,
  azure: {
    clientId: 'REPLACE_AZURE_CLIENT_ID',
    tenantId: 'REPLACE_AZURE_TENANT_ID',
    redirectUri: 'REPLACE_FRONTEND_PUBLIC_URL',
  },
  bff: {
    baseUrl: 'REPLACE_BFF_PUBLIC_URL',
    get scope(): string {
      return `api://${environment.azure.clientId}/access_as_user`;
    },
  },
};

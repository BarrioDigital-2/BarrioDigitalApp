/**
 * Build de producción (EC2 / API Gateway). Ajusta redirectUri y baseUrl
 * a las URLs públicas antes de `ng build --configuration production`.
 */
export const environment = {
  production: true,
  azure: {
    clientId: '8b0cc19f-2671-49c7-beff-816087690691',
    tenantId: '92b9ffd8-4da1-4956-b85b-62654b654d25',
    redirectUri: 'https://100.61.159.226',
  },
  bff: {
    baseUrl: 'https://o3p4thx9x8.execute-api.us-east-1.amazonaws.com/api',
    get scope(): string {
      return `api://${environment.azure.clientId}/access_as_user`;
    },
  },
};

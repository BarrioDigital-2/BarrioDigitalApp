export const appConfig = {
  azure: {
    clientId: '8b0cc19f-2671-49c7-beff-816087690691',
    tenantId: '92b9ffd8-4da1-4956-b85b-62654b654d25',
    redirectUri: 'http://localhost:4200',
  },
  bff: {
    baseUrl: 'http://localhost:8080',
    scope: 'api://8b0cc19f-2671-49c7-beff-816087690691/access_as_user',
  },
} as const;
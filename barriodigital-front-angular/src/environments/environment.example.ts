export const environment = {
  production: false,
  azure: {
    clientId: '00000000-0000-0000-0000-000000000000',
    tenantId: '00000000-0000-0000-0000-000000000000',
    redirectUri: 'http://localhost:4200',
  },
  bff: {
    baseUrl: 'http://localhost:8080',
    get scope(): string {
      return `api://${environment.azure.clientId}/access_as_user`;
    },
  },
};

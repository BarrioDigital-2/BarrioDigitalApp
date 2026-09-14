import { BrowserCacheLocation, InteractionType, IPublicClientApplication, LogLevel, PublicClientApplication } from '@azure/msal-browser';
import { MsalGuardConfiguration, MsalInterceptorConfiguration } from '@azure/msal-angular';
import { appConfig } from '../config/app.config';

/**
 * Instancia de MSAL. Una unica fuente de verdad para clientId/authority/redirectUri.
 */
export function MSALInstanceFactory(): IPublicClientApplication {
  return new PublicClientApplication({
    auth: {
      clientId: appConfig.azure.clientId,
      authority: `https://login.microsoftonline.com/${appConfig.azure.tenantId}`,
      redirectUri: appConfig.azure.redirectUri,
      postLogoutRedirectUri: appConfig.azure.redirectUri,
    },
    cache: {
      cacheLocation: BrowserCacheLocation.LocalStorage,
    },
    system: {
      loggerOptions: {
        loggerCallback: (level: LogLevel, message: string) => {
          if (level === LogLevel.Error) {
            console.error(message);
          }
        },
        logLevel: LogLevel.Warning,
        piiLoggingEnabled: false,
      },
    },
  });
}

/**
 * Config del MsalGuard: que hacer cuando una ruta protegida se visita sin sesion.
 */
export function MSALGuardConfigFactory(): MsalGuardConfiguration {
  return {
    interactionType: InteractionType.Redirect,
    authRequest: {
      scopes: [appConfig.bff.scope],
    },
  };
}

/**
 * Config del MsalInterceptor: a que URLs adjuntar el Bearer token y con que scope.
 * Solo se le agrega el token a las llamadas al BFF, no a cualquier request HTTP.
 */
export function MSALInterceptorConfigFactory(): MsalInterceptorConfiguration {
  const protectedResourceMap = new Map<string, Array<string>>();
  protectedResourceMap.set(`${appConfig.bff.baseUrl}/*`, [appConfig.bff.scope]);

  return {
    interactionType: InteractionType.Redirect,
    protectedResourceMap,
  };
}
package cl.duoc.ms_barriodigital_bff.config;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Verifica que el JWT haya sido emitido para esta API (api://<API_CLIENT_ID>).
 * Sin este validador, cualquier JWT valido de Azure AD (aunque sea de otra app)
 * seria aceptado por el BFF.
 */
public class AudienceValidator implements OAuth2TokenValidator<Jwt> {

    private final String expectedAudience;

    public AudienceValidator(String apiClientId) {
        this.expectedAudience = "api://" + apiClientId;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        if (jwt.getAudience() != null &&
                (jwt.getAudience().contains(expectedAudience) || jwt.getAudience().contains(expectedAudienceWithoutPrefix()))) {
            return OAuth2TokenValidatorResult.success();
        }
        OAuth2Error error = new OAuth2Error(
                "invalid_token",
                "El token no tiene la audience esperada: " + expectedAudience,
                null
        );
        return OAuth2TokenValidatorResult.failure(error);
    }

    private String expectedAudienceWithoutPrefix() {
        return expectedAudience.replace("api://", "");
    }
}
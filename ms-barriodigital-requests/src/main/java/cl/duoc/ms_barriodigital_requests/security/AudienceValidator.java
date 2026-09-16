package cl.duoc.ms_barriodigital_requests.security;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

public class AudienceValidator implements OAuth2TokenValidator<Jwt> {

    private final String expectedAudience;

    public AudienceValidator(String apiClientId) {
        this.expectedAudience = "api://" + apiClientId;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        if (jwt.getAudience() != null
                && (jwt.getAudience().contains(expectedAudience)
                || jwt.getAudience().contains(expectedAudience.replace("api://", "")))) {
            return OAuth2TokenValidatorResult.success();
        }
        OAuth2Error error = new OAuth2Error(
                "invalid_token",
                "El token no tiene la audience esperada: " + expectedAudience,
                null
        );
        return OAuth2TokenValidatorResult.failure(error);
    }
}

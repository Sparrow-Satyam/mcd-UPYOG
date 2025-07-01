package org.egov.user.security.oauth2.custom;

import lombok.extern.slf4j.Slf4j;
import org.egov.user.domain.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomTokenService extends DefaultTokenServices {

    @Autowired
    private TokenService tokenService;

    public CustomTokenService(TokenStore tokenStore) {
        super.setTokenStore(tokenStore);
    }

    @Override
    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {
        log.info("In MyTokenServices before getAccessToken");
        if (authentication == null) {
            log.error("OAuth2Authentication is null");
        } else {
            log.info("OAuth2Authentication: {}", authentication);
            if (authentication.getOAuth2Request() == null) {
                log.error("OAuth2Request inside OAuth2Authentication is NULL!");
            } else {
                log.info("OAuth2Request clientId: {}", authentication.getOAuth2Request().getClientId());
            }

            if (authentication.getUserAuthentication() == null) {
                log.error("UserAuthentication inside OAuth2Authentication is NULL!");
            } else {
                log.info("UserAuthentication: {}", authentication.getUserAuthentication());
            }
        }

        OAuth2AccessToken existingAccessToken = null;
        try {
            existingAccessToken = this.getAccessToken(authentication);
        } catch (Exception e) {
            log.warn("Could not get existing access token due to exception—ignoring and creating new.", e);
        }
        if (existingAccessToken != null) {
            this.revokeToken(existingAccessToken.getValue());
        }

        // Delete the old auth_to_access key (stale Redis mapping)
        try {
            tokenService.deleteAuthToAccessKey(authentication);
        } catch (Exception e) {
            log.error("Error while deleting old auth_to_access key", e);
        }
        OAuth2AccessToken accessToken;

        try {
            log.info("About to create access token...");
            log.info("Authentication: {}", authentication);
            log.info("Principal: {}", authentication.getPrincipal());
            log.info("Details: {}", authentication.getDetails());
            log.info("Authorities: {}", authentication.getAuthorities());
            accessToken = super.createAccessToken(authentication);
            log.info("Access token created successfully: {}", accessToken);
        } catch (Exception e) {
            log.error("Exception occurred while creating access token", e);
            throw e; // You can rethrow or wrap if you want
        }
        return accessToken;
        //return super.createAccessToken(authentication);
    }


}
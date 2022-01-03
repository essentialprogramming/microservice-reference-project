package com.authentication.response;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * OAuth 2.0 Access Token Response json
 *
 * @author Razvan Prichici
 */

@Schema(name = "AccessToken",
        description = "An AccessToken object is returned for the authenticated user. It contains the JWT Token.")
@Builder
@AllArgsConstructor
public class AccessToken {

    @Schema(name = "The Token", required = true)
    @JsonProperty("accessToken")
    private String accessToken;

    @JsonProperty("tokenType")
    private String tokenType;

    @JsonProperty("expireIn")
    private long expiresIn;

    @JsonProperty("expireDate")
    private Date expireAt;

    @JsonProperty("refreshToken")
    private String refreshToken;

    @JsonProperty("refreshExpiresIn")
    private long refreshExpiresIn;


    @JsonProperty("not-before-policy")
    private int notBeforePolicy;


    @JsonProperty("active")
    private boolean active;


    private final Map<String, Object> otherClaims = new HashMap<>();

    /**
     * The default constructor.
     */
    public AccessToken() {
        super();
    }




    public String getAccessToken() {
        return accessToken;
    }
    public String getTokenType() {
        return tokenType;
    }
    public boolean isActive() {
        return active;
    }

    @JsonAnySetter
    public void addClaim(String name, Object value) {
        otherClaims.put(name, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getOtherClaims() {
        return otherClaims;
    }

}

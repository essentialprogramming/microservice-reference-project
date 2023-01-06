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
 * OAuth 2.0 Access Token Response
 *
 * @author Razvan Prichici
 */

@Schema(name = "AccessToken",
        description = "An AccessToken object is returned for the authenticated user. It contains the JWT Token.")
@Builder
@AllArgsConstructor
public class AccessToken {

    @Schema(name = "accessToken", description = "Access Token Value", required = true, example = "EtleSI6IkhvdVNSbEJ3X2w5MGFzaVExLWNiTSIsImF1ZCI6IkVzc2VudGlhbCBQcm9ncm")
    @JsonProperty("accessToken")
    private String accessToken;

    @JsonProperty("tokenType")
    @Schema(name = "tokenType", description = "Token Type", example = "Bearer")
    private String tokenType;

    @JsonProperty("expireIn")
    @Schema(name = "expiresIn", description = "Expiration Time (ms)", example = "123000")
    private long expiresIn;

    @JsonProperty("expireDate")
    @Schema(name = "expireAt", description = "Expiration Date (timestamp)", example = "1665522000000")
    private Date expireAt;

    @JsonProperty("refreshToken")
    @Schema(name = "refreshToken", description = "Refresh Token Value", required = true, example = "123eSI6Ikh232EJ3X2w5MGFzaVExLWNiTSIsImF1ZCI6IkVzc2VudGlhbCBQc45df")
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
    public long getExpiresIn() { return expiresIn; }

    @JsonAnySetter
    public void addClaim(String name, Object value) {
        otherClaims.put(name, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getOtherClaims() {
        return otherClaims;
    }

}

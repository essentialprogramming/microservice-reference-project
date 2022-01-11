package com.token.validation.jwt;


import java.util.*;

/**
 * JwtClaims represent a set of &quot;claims&quot;
 * <p>
 * This is ultimately a JSON map and any values can be added to it, but JWT standard names are
 * provided as getters and setters for convenience.
 * </p>
 */
public class JwtClaims extends LinkedHashMap<String, Object> {
    private static final long serialVersionUID = -1L;

    public JwtClaims() {
    }

    public JwtClaims(Map<? extends String, ?> claims) {
        super(claims);
    }

    public String getIssuer() {
        return containsKey(Claims.ISS.getValue()) ? (String) get(Claims.ISS.getValue()) : null;
    }


    public String getSubject() {
        return containsKey(Claims.SUB.getValue()) ? (String) get(Claims.SUB.getValue()) : null;
    }


    public String getAudience() {
        return containsKey(Claims.AUD.getValue()) ? (String) get(Claims.AUD.getValue()) : null;
    }


    public long getExpiration() {
        return containsKey(Claims.EXP.getValue()) ? ((Number) get(Claims.EXP.getValue())).longValue() : 0L;
    }


    public long getNotBefore() {
        return containsKey(Claims.NBF.getValue()) ? ((Number) get(Claims.NBF.getValue())).longValue() : 0L;
    }


    public long getIssuedAt() {
        return containsKey(Claims.IAT.getValue()) ? ((Number) get(Claims.IAT.getValue())).longValue() : 0L;
    }


    public String getID() {
        return containsKey(Claims.JTI.getValue()) ? (String) get(Claims.JTI.getValue()) : null;
    }


    public String getDomain() {
        return containsKey(Claims.DOMAIN.getValue()) ? (String) get(Claims.DOMAIN.getValue()) : null;
    }


    public String getRoles() {
        return containsKey(Claims.ROLES.getValue()) ? (String) get(Claims.ROLES.getValue()) : "";
    }

    @SuppressWarnings("unchecked")
    public List<String> getPermissions() {
        return containsKey(Claims.PERMISSIONS.getValue()) ? (List<String>) get(Claims.PERMISSIONS.getValue()) : Collections.emptyList();
    }

    /**
     * Returns a new {@link Builder}.
     * @return the {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private final Map<String, Object> claims = new HashMap<>();

        private Builder() {
        }

        public Builder id(String jti) {
            setValue(Claims.JTI.getValue(), jti);
            return this;
        }

        public Builder issuer(String issuer) {
            claims.put(Claims.ISS.getValue(), issuer);
            return this;
        }

        public Builder issuedAt(long iat) {
            setValue(Claims.IAT.getValue(), iat);
            return this;
        }

        public Builder notBefore(long nbf) {
            setValue(Claims.NBF.getValue(), nbf);
            return this;
        }

        public Builder subject(String sub) {
            setValue(Claims.SUB.getValue(), sub);
            return this;
        }

        public Builder audience(String aud) {
            setValue(Claims.AUD.getValue(), aud);
            return this;
        }

        public Builder expiration(long exp) {
            setValue(Claims.EXP.getValue(), exp);
            return this;
        }

        public Builder domain(String domain) {
            setValue(Claims.DOMAIN.getValue(), domain);
            return this;
        }

        public Builder roles(String roles) {
            setValue(Claims.ROLES.getValue(), roles);
            return this;
        }

        public Builder value(String key, String value) {
            setValue(key, value);
            return this;
        }

        protected void setValue(String name, Object value) {
            if (value == null) {
                claims.remove(name);
            } else {
                claims.put(name, value);
            }
        }

        public JwtClaims build() {
            return new JwtClaims(this.claims);
        }
    }
}

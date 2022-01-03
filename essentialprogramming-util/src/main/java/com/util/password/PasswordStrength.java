package com.util.password;

import java.util.HashMap;
import java.util.Map;

public enum PasswordStrength {

    /**
     * Blank Password (empty and/or space chars only)
     */
    None(0),

    /**
     * Either too short (less than 5 chars), one-case letters only or digits only
     */
    VeryWeak(1),

    /**
     * At least 5 characters, one strong condition met (>= 8 chars with 1 or more
     * UC letters, LC letters, digits & special chars)
     */
    Weak(2),

    /**
     * At least 5 characters, two strong conditions met (>= 8 chars with 1 or more
     * UC letters, LC letters, digits & special chars)
     */
    Medium(3),

    /**
     * At least 8 characters, three strong conditions met (>= 8 chars with 1 or
     * more UC letters, LC letters, digits & special chars)
     */
    Strong(4),

    /**
     * At least 8 characters, all strong conditions met (>= 8 chars with 1 or more
     * UC letters, LC letters, digits & special chars)
     */
    VeryStrong(5);

    private final Integer value;

    private static final Map<Integer, PasswordStrength> lookup = new HashMap<>();

    static {
        for (PasswordStrength d : PasswordStrength.values()) {
            lookup.put(d.getValue(), d);
        }
    }

    PasswordStrength(Integer strength) {
        this.value = strength;
    }

    public Integer getValue() {
        return value;
    }

    public static PasswordStrength get(Integer strength) {
        return lookup.get(strength);
    }

}

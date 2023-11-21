package dev.mccue.microhttp.cookies;

import java.util.Objects;

/**
 * A cookie.
 * @param name The name of the cookie.
 * @param value The value of the cookie.
 */
public record Cookie(String name, String value) {
    public Cookie(String name, String value) {
        this.name = Objects.requireNonNull(name);
        this.value = Objects.requireNonNull(value);
    }
}

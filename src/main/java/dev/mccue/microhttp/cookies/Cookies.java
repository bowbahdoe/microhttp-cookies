package dev.mccue.microhttp.cookies;

import org.jspecify.annotations.Nullable;
import org.microhttp.Request;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Represents a collection of cookies parsed from a request header.
 */
public final class Cookies implements Iterable<Cookie> {
    static final String RE_COOKIE_OCTET = "[!#$%&'()*+\\-./0-9:<=>?@A-Z\\[\\]\\^_`a-z\\{\\|\\}~]";
    static final String RE_COOKIE_VALUE = "\"" + RE_COOKIE_OCTET + "*\"|" + RE_COOKIE_OCTET + "*";
    static final String RE_TOKEN = "[!#$%&'*\\-+.0-9A-Z\\^_`a-z\\|~]+";
    static final Pattern RE_COOKIE = Pattern.compile("\\s*(?<key>" + RE_TOKEN + ")=(?<value>" + RE_COOKIE_VALUE + ")\\s*[;,]?");
    static final Function<String, @Nullable String> URL_DECODE = s -> {
        try {
            return URLDecoder.decode(s, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            return null;
        }
    };

    private final List<Cookie> value;

    public Cookies(List<Cookie> value) {
        this.value = List.copyOf(value);
    }

    /**
     * Parse cookies from a request.
     * @param request The {@link Request} to parse cookies from
     * @return The set of {@link Cookies}.
     */
    public static Cookies parse(Request request) {
        return parse(request, URL_DECODE);
    }

    /**
     * Parse cookies from a request.
     * @param request The {@link Request} to parse cookies from
     * @param decode A function to preprocess the value of a cookie. Should return null on error.
     * @return The set of {@link Cookies}.
     */
    public static Cookies parse(Request request, Function<String, ? extends @Nullable String> decode) {
        var header = request.header("cookie");
        if (header == null) {
            return new Cookies(List.of());
        }
        else {
            return parse(header, decode);
        }
    }

    /**
     * Parse cookies from a request header.
     * @param headerValue The header to parse cookies from
     * @return The set of {@link Cookies}.
     */
    public static Cookies parse(String headerValue) {
        return parse(headerValue, URL_DECODE);
    }

    /**
     * Parse cookies from a request header.
     * @param headerValue The header to parse cookies from
     * @param decode A function to preprocess the value of a cookie. Should return null on error.
     * @return The set of {@link Cookies}.
     */
    public static Cookies parse(String headerValue, Function<String, ? extends @Nullable String> decode) {
        var cookies = new ArrayList<Cookie>();
        var matcher = RE_COOKIE.matcher(headerValue);
        while (matcher.find()) {
            var key = matcher.group("key");
            var value = matcher.group("value");
            var escapedValue = decode.apply(value.replaceAll("^\"|\"$", ""));
            if (escapedValue != null) {
                cookies.add(new Cookie(key, escapedValue));
            }
        }
        return new Cookies(cookies);
    }

    @Override
    public Iterator<Cookie> iterator() {
        return value.iterator();
    }

    public Stream<Cookie> stream() {
        return value.stream();
    }

    public Optional<String> get(String name) {
        for (var cookie : value) {
            if (cookie.name().equals(name)) {
                return Optional.of(cookie.value());
            }
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        return "Cookies[" +
               "value=" + value +
               ']';
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Cookies cookies
                && value.equals(cookies.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

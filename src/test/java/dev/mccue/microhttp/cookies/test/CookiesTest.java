package dev.mccue.microhttp.cookies.test;

import dev.mccue.microhttp.cookies.Cookie;
import dev.mccue.microhttp.cookies.Cookies;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CookiesTest {
    @Test
    public void testBasic() {
        assertEquals(
            new Cookies(List.of(new Cookie("apple", "orange"))),
            Cookies.parse("apple=orange")
        );
    }

    @Test
    public void testQuoted() {
        assertEquals(
                new Cookies(List.of(new Cookie("apple", "orange"))),
                Cookies.parse("apple=\"orange\"")
        );
    }

    @Test
    public void multipleCookies() {
        assertEquals(
                new Cookies(List.of(
                        new Cookie("a", "b"),
                        new Cookie("c", "d"),
                        new Cookie("e", "f")
                )),
                Cookies.parse("a=b; c=d,e=f")
        );
    }

    @Test
    public void readUrlEncoded() {
        assertEquals(
                new Cookies(List.of(
                        new Cookie("a", "hello world")
                )),
                Cookies.parse("a=hello+world")
        );
        assertEquals(
                new Cookies(List.of(
                        new Cookie("a", "hello world")
                )),
                Cookies.parse("a=hello%20world")
        );
    }

    @Test
    public void readNoDecoding() {
        assertEquals(
                new Cookies(List.of(
                        new Cookie("a", "hello+world")
                )),
                Cookies.parse("a=hello+world", Function.identity())
        );
        assertEquals(
                new Cookies(List.of(
                        new Cookie("a", "hello%20world")
                )),
                Cookies.parse("a=hello%20world", Function.identity())
        );
    }

    @Test
    public void readInvalidUrlEncoded() {
        assertEquals(new Cookies(List.of()), Cookies.parse("a=%D"));
    }
}

# microhttp-cookies

Utility for parsing cookie headers from requests.

## Dependency Information

### Maven

```xml
<dependency>
    <groupId>dev.mccue</groupId>
    <artiactId>microhttp-cookies</artiactId>
    <version>0.0.1</version>
</dependency>
```

### Gradle

```groovy
dependencies {
    implementation('dev.mccue:microhttp-cookies:0.0.1')
}
```

## Usage

```java
var cookies = Cookies.parse(request);
var value = cookies.get("name").orElse(null);
```
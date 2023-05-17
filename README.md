# tenpo-challenge

## Estructura
Para este proyecto se esta usando una interpretacion de la arquitectura Hexagonal, tambien conocida como Ports and Adapters. El enfoque que decidi tomar es bastante similar a lo que se plantea en [este articulo](https://reflectoring.io/spring-hexagonal/)

### Ejemplo

![ilustracion de arquitectura hexagonal ejemplo](docs/hexagonal-concepto.png)


## Stack 🛠️

- java 17
- postgresql
- jdbc
- [spring-boot 3](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [lombok](https://projectlombok.org/features/)
- [spock](https://spockframework.org/spock/docs/2.3/index.html) (testing)
- [guava](https://github.com/google/guava/wiki) (cache)
- [resilience4j](https://resilience4j.readme.io/docs/ratelimiter) (rate limiter)

## Swagger 📝
### Swagger json
http://localhost:8083/api-docs

### Swagger UI
http://localhost:8083/swagger-ui/index.html



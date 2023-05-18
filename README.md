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

## Collection
Dentro del directorio `postman` vas a poder encontrar una collection con los endpoints disponibles


## Configuración local    
1. Instalá la Java 17
2. Cloná el repo (`git@github.com:ewyso/tenpo-challenge.git`)
3. Abrí el repo con tu IDE favorito

## Como correr el app
1. Levanta la db posgre con el docker-compose ubicado en ./docker (`docker-compose up -d`)
2. Corre el app desde tu IDE favorito o ejecuta el Dockerfile 

### Como buildear y correr Docker
1. Asegurate estar parado en el mismo folder del Dockerfile y ejecuta el build del mismo (`docker build -t tenpo-app:1.0.0 .`)
2. Ejecutalo especificando network para que apunte a tu local (`sudo docker run --network=host tenpo-app:1.0.0`)
3. Disfruta del app :)
   


# ZeroMQ examples

## Adding the dependency

Considering we're using maven, we need to add the following dependency in our pom.xml:

```xml
<dependency>
	<groupId>org.zeromq</groupId>
	<artifactId>jeromq</artifactId>
</dependency>
```

Unfortunately, it is not possible to add the dependency through the [spring boot initializr](https://start.spring.io).

## Common parts

In this section, we will specifically discuss the common code between the two examples we will see: the first concerns the ROUTER-DEALER pattern, while the second concerns the PUB-SUB pattern.

### Project structure

……..

### Event class

This class implements the data that our endpoints will exchange during communication.

```java
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event<K, T> {

    public enum Type {CREATE, DELETE, UPDATE}

    @JsonProperty("eventType") private Type eventType;

    @JsonProperty("key") private K key;

    @JsonProperty("data") private T data;

    @Builder.Default
    @JsonProperty("eventCreatedAt")
    private ZonedDateTime eventCreatedAt = ZonedDateTime.now();
}
```

Using Lombok annotations (check the [explanation]('Reducing boilerplate code (Lombok).md')) we've be able to implement this class simply through fields declaration. More specifically we have:

* Type&rarr;enum that specifies values fot eventType filed;
* eventType &rarr;store the event type;
* key&rarr;it represents the event's ID (we'll generate a random UUID);
* data&rarr;data to be sent;
* eventCreatedAt&rarr;date and time of event generation.

For all parameters we had to specify @JsonProperty to be able to map the event object in a json serialization. In this way we're able to exchange a string in json format and de-serialize that obtaining back an event object.

### Object mapper

………

```java
@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper getObjectMapper() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;

    }

}
```


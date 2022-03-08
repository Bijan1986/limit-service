# Spring Boot Micro-services from in-28minutes

## 1. Setting up limits microservices

The dependencies expected

1. config client
2. actuator
3. web
4. dev tools

Add the config server properties to the application.properties file

> spring.config.import=optional:configserver:http//localhost:8888

## 2. Creating hard coded limits services

LimitsController

```java
@RestController
public class LimitsController {

    @GetMapping("/limits")
    public Limits retrieveLimits(){
        return new Limits(1,1000);
    }
}

```
Limits bean

```java
public class Limits {
    private int minimum;
    private int maximum;

    public Limits() {
    }

    public Limits(int minimum, int maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public int getMinimum() {
        return minimum;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    public int getMaximum() {
        return maximum;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }
}

```
http://localhost:8080/limits

```json
{
    "minimum":1,
    "maximum":1000
    
}
```

## 3. Enhance Limits service to get configuration from application.properties

in this section we will try to get the values from the application.properties file instead of getting it from the 
code itself.

in the application.properties file kindly add the below

```
limits-service.minimum=2
limits-service.maximum=998
```

now lets create a new class(LimitsServiceConfiguration) and a new package(configuration)

```java
@Component
@ConfigurationProperties("limits-service")
public class LimitsServiceConfiguration {
    private int minimum = 2;
    private int maximum = 998;

    public int getMinimum() {
        return minimum;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    public int getMaximum() {
        return maximum;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }
}


```

now in the limits controller

```java

  @GetMapping("/limits")
    public Limits retrieveLimits() {
        //return new Limits(1,1000);
        return new Limits(configuration.getMinimum(),configuration.getMaximum());
    }
    
```
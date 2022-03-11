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

@Autowired
	private LimitsServiceConfiguration configuration;

  @GetMapping("/limits")
    public Limits retrieveLimits() {
        //return new Limits(1,1000);
        return new Limits(configuration.getMinimum(),configuration.getMaximum());
    }
    
```

## 4. Setting up Spring cloud config server

in the previous section we managed to call the values from the properties file.
in this section we will configure the spring cloud config server.

To achieve this, we will crate a new spring boot project and mention the details as below.

    artifact-id > spring-cloud-config-server

    dependency > 1. spring boot dev tools
                2. spring cloud config server
                
let the config server run on port 8888

in application.properties file

> spring.application.name=spring-cloud-config-server<br>
server.port=8888

so now we have just done the cloud config initial setup 

## 5. Installing GIT and creating local git repository
in this section, we will crate a local git repository 

and try to put the details we have mentioned in limits-service's application.properties file in to it .

```cmd

mkdir C:\DEV\practice\springboot\git-local-config-repo

```

## 6. Connect Spring cloud config server to the local git repository

To make this happen, go to the spring cloud config server's application.properties and add the below mentioned details

```properties
spring.application.name=spring-cloud-config-server
server.port=8888
spring.cloud.config.server.git.uri=file:///C:/DEV/practice/springboot/git-local-config-repo

```

also add the below annotation to the Bootstrap

> @EnableConfigServer

```java
@EnableConfigServer
@SpringBootApplication
public class SpringCloudConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudConfigServerApplication.class, args);
    }

}


```

now start the application and check the below url

> http://localhost:8888/limits-service/default

the output will be as follows

```json

{
"name": "limits-service",
"profiles": [
"default"
],
"label": null,
"version": "6cc6b92704d394c9521770d238ce260e410c03b9",
"state": null,
"propertySources": [
{
"name": "file:///C:/DEV/practice/springboot/git-local-config-repo/file:C:\\DEV\\practice\\springboot\\git-local-config-repo\\limits-service.properties",
"source": {
"spring.config.import": "optional:configserver:http//localhost:8888",
"limits-service.minimum": "4",
"limits-service.maximum": "996"
}
}
]
}

```

## 7.Connect spring cloud config server to the limits-service


in this section we will make the client talk to the config server and get the details from the git local 

in the application.properties file we can include the below

```
spring.application.name=limits-service
spring.cloud.config.enabled=false
spring.cloud.config.import-check.enabled=false
spring.config.import=optional:configserver:http://localhost:8888/


limits-service.minimum=2
limits-service.maximum=998
```

and we can add the below mentioned dependency as well

```xml

<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-bootstrap</artifactId>
</dependency>

```




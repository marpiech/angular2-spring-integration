# SPRING CLOUD MICROSERVICES AND ANGULAR 2 INTEGRATION TRAINING

## LESSON 1: UNSECURED REST SERVICE
In this lesson we will build unsecured REST service with just one class and config.

![alt tag](https://raw.githubusercontent.com/marpiech/angular2-spring-integration/master/docs/lesson1.png)

###### IntelliseqHelloServiceApp.java
```java
@SpringBootApplication
@RestController
public class IntelliseqHelloServiceApp {

	public static void main(String[] args) {
		SpringApplication.run(IntelliseqHelloServiceApp.class, args);
	}
	
	@RequestMapping("/")
    public String hello() {
        return "{\"message\": \"hello\"}";
    }
	
}
```

###### application.yml
```
server:
    port: ${port:8082}
```

#### RUNNING AND TESTING SERVICE
```
git clone https://github.com/marpiech/angular2-spring-integration.git
cd angular2-spring-integration/
git checkout lesson1
gradle build -p hello-service/
mkdir pid
java -jar hello-service/build/libs/hello-service-0.1.jar &
echo $! > pid/hello-service.pid
curl localhost:8082
kill `cat pid/hello-service.pid`
```

## LESSON 2: CONFIGURATION SERVICE
In this lesson we will build unsecured REST service with just one class and config.

![alt tag](https://raw.githubusercontent.com/marpiech/angular2-spring-integration/master/docs/lesson2.png)

###### ConfigServiceApp.java
```java
@SpringBootApplication
@EnableConfigServer
public class IntelliseqHelloServiceApp {

	public static void main(String[] args) {
		SpringApplication.run(IntelliseqHelloServiceApp.class, args);
	}
	
}
```

In config server add dependency
```bash
compile group: 'org.springframework.cloud', name: 'spring-cloud-config-server', version: '1.2.1.RELEASE'
```

In other microservices add dependency
```bash
compile group: 'org.springframework.cloud', name: 'spring-cloud-starter-config', version: '1.2.1.RELEASE'
```

###### bootstrap.yaml
```yaml

spring:
    application.name: config-service
    cloud:
        config:
            server:
                git:
                    uri: ${CONFIGURATION_REPOSITORY:https://github.com/marpiech/angular2-spring-integration-config}
                default-label: ${CONFIGURATION_BRANCH:master}


# EMBEDDED SERVER CONFIGURATION
server:
    port: ${port:8888}
```

In the yaml git repository address was defined. In this repository we can store configurations of our servers using schema: servicename.yml


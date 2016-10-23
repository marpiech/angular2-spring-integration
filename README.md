## LESSON 1: UNSECURED REST SERVICE
In this lesson we will build unsecured REST service with just one class and config.

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
    


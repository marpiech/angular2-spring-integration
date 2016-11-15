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
In this lesson we will move resource service configuration to external configuration service

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

In the yaml git repository address was defined. In this repository we can store configurations of our servers using schema: servicename.yml
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

#### RUNNING AND TESTING SERVICE
```bash
git clone https://github.com/marpiech/angular2-spring-integration.git
cd angular2-spring-integration/
git checkout lesson2
gradle build -p hello-service/
gradle build -p config-service/
mkdir pid
java -jar config-service/build/libs/config-service-0.1.jar &
echo $! > pid/config-service.pid
java -jar hello-service/build/libs/hello-service-0.1.jar &
echo $! > pid/hello-service.pid
curl localhost:8082
kill `cat pid/hello-service.pid`
kill `cat pid/config-service.pid`
```

## LESSON 3: CONFIGURATION SERVICE
In this lesson we will secure our resource service with JWT and OAUth2

![alt tag](https://raw.githubusercontent.com/marpiech/angular2-spring-integration/master/docs/lesson3.png)

[Tutorial on OAuth2 security and JWT](http://stytex.de/blog/2016/02/01/spring-cloud-security-with-oauth2/)

###### ResourcesServerSecurityConfig.java
```java
@Configuration
@EnableResourceServer
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
public class ResourcesServerSecurityConfig implements ResourceServerConfigurer {

    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId("hello-service");
    }

    public void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/**").authenticated()
                    .antMatchers(HttpMethod.GET, "/hello").hasAuthority("USER");
                    
        }



}
```

###### hello-service.yml
```yaml
server:
    port: ${port:8082}
spring:
    profiles:
        active: dev
# Disable spring basic authentication security
security:
    basic:
        enabled: false
    oauth2:
        resource:
            jwt:
                keyValue: |
                        -----BEGIN PUBLIC KEY-----
                        MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnGp/Q5lh0P8nPL21oMMrt2RrkT9AW5jgYwLfSUnJVc9G6uR3cXRRDCjHqWU5WYwivcF180A6CWp/ireQFFBNowgc5XaA0kPpzEtgsA5YsNX7iSnUibB004iBTfU9hZ2Rbsc8cWqynT0RyN4TP1RYVSeVKvMQk4GT1r7JCEC+TNu1ELmbNwMQyzKjsfBXyIOCFU/E94ktvsTZUHF4Oq44DBylCDsS1k7/sfZC2G5EU7Oz0mhG8+Uz6MSEQHtoIi6mc8u64Rwi3Z3tscuWG2ShtsUFuNSAFNkY7LkLn+/hxLCu2bNISMaESa8dG22CIMuIeRLVcAmEWEWH5EEforTg+QIDAQAB
                        -----END PUBLIC KEY-----
            id: openid
            serviceId: ${PREFIX:}resource
```

#### RUNNING AND TESTING SERVICE
```bash
git clone https://github.com/marpiech/angular2-spring-integration.git
cd angular2-spring-integration/
git checkout lesson3
gradle build -p hello-service/
gradle build -p config-service/
gradle build -p auth-service/
mkdir pid
java -jar config-service/build/libs/config-service-0.1.jar & echo $! > pid/config-service.pid
java -jar auth-service/build/libs/auth-service-0.1.jar & echo $! > pid/auth-service.pid
java -jar hello-service/build/libs/hello-service-0.1.jar & echo $! > pid/hello-service.pid
TOKEN=`curl -XPOST "acme:@localhost:8086/oauth/token" -d "grant_type=password&username=admin&password=admin" | cut -d "\"" -f 4`
curl -H "Authorization: Bearer $TOKEN" "localhost:8082/hello"
kill `cat pid/hello-service.pid`
kill `cat pid/auth-service.pid`
kill `cat pid/config-service.pid`
```

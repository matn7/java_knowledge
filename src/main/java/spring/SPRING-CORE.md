# Spring Core

## DEPENDENCIES

```java
@Component
public class HomeController {
    @Autowired
    HomeBusinessService homeBusinessService;
}
```

:arrow_down:

```java
@Component
public class HomeBusinessService {
    @Autowired
    HomeDataService homeDataService;
}
```

:arrow_down:

```java
@Component
public class HomeDataService {
    @Autowired
    JdbcTemplate template;
}
```

## AUTOWIRING

### By name
```java
@Component
public class SortServiceImpl {
    @Autowired
    private SortAlgorithm bubbleSortAlgorithm;
}
```

### By `@Primary` higher priority than by name
```java
@Component
@Primary
public class BubbleSortAlgorithm implements SortAlgorithm {
    // ...
}
```

### `@Qualifier` (highest priority)
```java
@Component
@Qualifier("quick")
public class QuickSortAlgorithm implements SortAlgorithm {
    // ...
}
```

```java
public class SortServiceImpl {
    @Autowired
    @Qualifier("quick")
    private SortAlgorithm sortAlgorithm;
}

```

## SCOPE OF BEANS

Bean Scopes default Singleton.
- Singleton - One instance per Spring Context
- Prototype - New bean whenever requested
- Request - One bean per HTTP request
- Session - One instance per HTTP session

### Singleton

```java
public static void main(String[] args) {
    // ...
	ApplicationContext applicationContext = SpringApplication.run(SpringIn5StepsBasicApplication.class, args);

	BinarySearchImpl binarySearch = applicationContext.getBean(BinarySearchImpl.class);
	BinarySearchImpl binarySearch1 = applicationContext.getBean(BinarySearchImpl.class);

    // The same output
	System.out.println(binarySearch);
	System.out.println(binarySearch1);
}
```

```java
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class BinarySearchImpl {
```

### Prototype

```java
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE
public class BinarySearchImpl {
```

```java
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE,
       proxyMode = ScopedProxyMode.TARGET_CLASS)
public class JdbcConne {
```

## Singleton vs GOF singleton
- GOF singleton - one singleton per JVM
- Sipring singleton - one singleton per Application Context


## COMPONENT SCAN
```java
@SpringBootApplication
@ComponentScan("com.spring.basics.componentscan")
public class ComponentScanApplication {
    // ...
}
```

## THE LIFECYCLE OF THE BEAN
- As soon as bean is created post construct will be called. Initialize content of bean.
`@PostConstruct`
- Called just before bean is removed.
`@PreDestroy`


## CDI
JavaEE Dependency Injection Standard (JRS-330)
- @Inject (`@Autowired`)
- @Named (`@Component & @Qualifier`)
- @Singleton
```java
// @Component
@Named
public class SomeCDIBusiness {
    //@Autowired
    @Inject
    private SomeCdiDao someCdiDao;

    public SomeCdiDao getSomeCdiDao() {
        return someCdiDao;
    }

    public void setSomeCdiDao(SomeCdiDao someCDIDAO) {
        this.someCdiDao = someCDIDAO;
    }
}
```

```java
//@Component
@Named
public class SomeCdiDao {
}
```

## Spring Configuration
*pom.xml*
```xml
<dependency>
    <groupId>org.springframework</groupId>
	<artifactId>spring-core</artifactId>
</dependency>

<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-context</artifactId>
</dependency>
```

```java
//@SpringBootApplication
@Configuration
@ComponentScan("com.panda.spring.basics")
public class BasicApplication {
    // ...
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BasicApplication.class);
    // ...
    applicationContext.close();

    // Or use try with resources to close application context
    /*
    try (AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BasicApplication.class)) {
        // ...
    }
    */
}
```

## Application Context using xml

```xml
<context:component-scan base-package="com.panda.spring.basics"></context:component-scan>

<bean id="xmlJdbcConnection"
    class="com.panda.spring.basics.xml.XmlJdbcConnection">
</bean>

<bean id="xmlPersonDAO" class="com.panda.spring.basics.xml.XmlPersonDAO">
    <property name="xmlJdbcConnection" ref="xmlJdbcConnection"></property>
</bean>
```

```java
// ...
try (ClassPathXmlApplicationContext applicationContext =
					 new ClassPathXmlApplicationContext("applicationContext.xml")) {
    // ...
}
// ...
```

## Wrap up IOC, Application Context and BeanFactory

- IOC Container - Manages beans. Create instance of WelcomeService. Creates beans for WelcomeController.
Autowires WelcomeService bean into the WelcomeController.
Wireing, creation of beans.

- Application Context - implementation of IOC
- Bean Factory - implementation of IOC

- ApplicationContext = Bean Factory enchanced
    - Spring AOP features
    - I18n capabilities
    - WebApplicationContext for web app

| No Spring | With Spring |
|---|---|
| @RestController                       | @Component                        |
| public class WelcomeController {      | public class WelcomeService {}    |
|   private WelcomeService service =    |                                   |
|       new WelcomeService();           | @RestController                   |
|   @RequestMapping("/welcome")         | public class WelcomeController {  |
|   public String welcome() {           |   @Autowired                      |
|       return service.retrieveMsg();   |   private WelcomeService service; |
|   }                                   |                                   |
| }                                     |   @RequestMapping("/welcome")     |
|                                       |   public String welcome() {       |
|                                       |      return service.retrieveMsg();|
|                                       |   }                               |


## Component Annotations
-`@Component` - generic component
-`@Repository` - encapsulating storage, retrieval, typical for relational databases
-`@Service` - Business serice facade
-`@Controller` - Controller in MVC design pattern

Classify components to different categories. Apply different logic for each category.

## Read from properties file
*app.properties*
```properties
external.service.url = http://server.com/service
```

*ExternalService.java*
```java
@Component
public class ExternalService {
    // from property file
    @Value("${external.service.url}")
    private String url;

    public String getUrl() {
        return url;
    }
}
```
*PropertiesApplication.java*
```java
@Configuration
@ComponentScan("com.panda.spring.properties")
@PropertySource("classpath:app.properties")
public class PropertiesApplication {

	private static Logger LOGGER = LoggerFactory.getLogger(PropertiesApplication.class);

	public static void main(String[] args) {

		try (AnnotationConfigApplicationContext applicationContext =
					 new AnnotationConfigApplicationContext(PropertiesApplication.class)) {

			SomeExternalSerice serice = applicationContext.getBean(SomeExternalSerice.class);
			LOGGER.info(" ===> {}", serice.returnServiceURL());
		}
	}
}
```

## Spring Boot Auto Configuration

`@SpringBootApplication`
- Spring Context
- Auto Configuration
- Component Scan

Spring boot looks at a) Frameworks available on CLASSPATH, b) Existing configuration for the application based on these,
Spring boot provides basic configuration needed to configure the application with these framework. This is called
`Auto Configuration`.


## Spring Boot, Spring, Spring MVC

`Spring Framework`
- Most important feature of Spring Framework is Dependency Injection. At the core all Spring Modules is
Dependency Injection or IOC Inversion of Control.
- Reduce duplication, plumbing code.
- Integration with other framework

`Spring MVC`
- Spring MVC framework provides decoupled way of developing web applications. With simple concepts like
Dispatcher Servlet, ModelAndView and iewResolver, it makes it easy to develop web applications.

## Spring boot

*pom.xml*
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-actuator</artifactId> <!-- monitor application health -->
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId> <!-- Restart quickly -->
</dependency>

<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-rest-hal-browser</artifactId>
</dependency>
```

## AOP


































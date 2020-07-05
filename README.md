# SpringCloud 2020

## 一 注册中心 Eureka

### 1 Eureka基本入门

**server端**

① 引入依赖

```xml
<!--SpringCloud 2.x eureka 服务依赖-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

② 修改配置

```yml
server:
  port: 7001
eureka:
  instance:
    hostname: localhost   # eureka实例名字
  client:
    register-with-eureka: false # 只是注册中心,不注册自己
    fetch-registry: false # 自己是注册中心,不拉取任何服务
    service-url:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka  # 设置与eureka server交互的地址查询服务和注册服务都需要依赖这个地址,  也就是表示要把自身注册的地址
```

③ 添加注解

```java
@SpringBootApplication
@EnableEurekaServer  //开启服务注册
public class CloudEurekaServer7001 {
```

**client端**

① 引入依赖

```xml
<!--SpringCloud 2.x eureka 客户端依赖-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    1<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

② 修改配置

```yaml
spring:
  application:
    name: cloud-payment-service
eureka:
  client:
    fetch-registry: true
    register-with-eureka: true
    service-url:
      defaultZone: http://localhost:7001/eureka
```

③ 开启注解

```java
@SpringBootApplication
@EnableEurekaClient
public class CloudProviderPayMent8001 {
```

### 2 Eureka 集群

主要修改配置:

- eureka7001

```yaml
server:
  port: 7001
eureka:
  instance:
    hostname: erueka7001.com
  client:
    register-with-eureka: false # 只是注册中心,不注册自己
    fetch-registry: false # 自己是注册中心,不拉取任何服务
    service-url:
      defaultZone: http://eureka7002.com:7002/eureka  # 自身要注册的地址
```

- eureka7002

```yml
server:
  port: 7002

eureka:
  instance:
    hostname: eureka7002.com
  client:
    register-with-eureka: false
    fetch-registry: false
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka
```

- 将服务注册到进群

只需要修改配置

```yaml
eureka:
  client:
    fetch-registry: true
    register-with-eureka: true
    service-url:
      defaultZone: http://eureka7002.com:7002/eureka,http://eureka7001.com:7001/eureka #只需要改变这个
```



### 3 提供者服务集群搭建和访问

- 提供者服务搭建

略

- 提供者服务的访问

① 不能写死url，而是要针对微服务名

```java
//private static final String PAYMENT_URL="http://127.0.0.1:8001/payment";
private static final String PAYMENT_URL = "http://CLOUD-PAYMENT-SERVICE/payment";
```

② 基于restTemplate开启负载均衡机制

```java
@Bean
@LoadBalanced  //开启负载均衡机制
public RestTemplate restTemplate(){
    return new RestTemplate();
}
```

### 4 微服务完善

```yaml
eureka:
  instance:
    instance-id: payment8001  # 实例id修改
    prefer-ip-address: true  #展示id信息
```



### 5 服务发现 Discovery

- 注入配置，添加方法

```java
@Autowired
private DiscoveryClient discoveryClient;

@GetMapping("/discovery")
public Object discovery(){
    List<String> services = discoveryClient.getServices();
    for(String service:services){
        LOGGER.info("所有的服务:"+service);
    }
    List<ServiceInstance> instances = discoveryClient.getInstances("cloud-order-service");
    for (ServiceInstance instance : instances) {
        LOGGER.info("服务信息:"+instance.getInstanceId()+"\t"+instance.getHost()+"\t"+instance.getPort()+"\t"+instance.getUri());
    }
    return discoveryClient;
}
```

- 开启注解

```java
@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient //开启服务发现
public class CloudConsumerOrder80 {
```



### 6 Eureka自我保护机制

- 注册中心

```yml
eureka:
  server:
    enable-self-preservation: false  #关闭自我保护机制
    eviction-interval-timer-in-ms: 2000
```

- 客户端

```yml
eureka:
  instance:
     lease-renewal-interval-in-seconds:  1
     lease-expiration-duration-in-seconds:  2
```



## 二 注册中心 Zookeeper

### 1 安装Zookeeper

我这里基于ubuntu16.04 安装(我下载的是zookeeper3.5.8)

[zookeeper官网下载](https://zookeeper.apache.org/)

- 解压zookeeper

```shell
tar -xzf apache-zookeeper-3.5.8-bin.tar.gz
```

- 进入conf修改文件名**zoo_sample.cfg**为**zoo.cfg**

```shell
dataDir=/HD/logs/zookeeper #修改存储文件配置
dataLogDir=/HD/logs/zookeeper #修改存储文件log配置
```

- 启动

```shell
./zkServer.sh start  #启动
./zkServer.sh status #状态
./zkServer.sh stop   #停止
```

注：记得开启防火墙端口

### 2 Zookeeper 基本入门

① 引入依赖

```xml
<!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-zookeeper-discovery -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zookeeper-discovery</artifactId>
</dependency>
```

② 修改配置

```yml
server:
  port: 8004
spring:
  application:
    name: cloud-payment-service
  cloud:
    zookeeper:
      connect-string: xxx.xxx.xxx.xxx:2181 #配置zookeeper地址
```

③ 添加注解

```java
@SpringBootApplication
@EnableDiscoveryClient  //开启服务发现
public class CloudProviderPayment8004 {
```

④ 检测zookeeper 是否注册成功

```shell
./zkCli.sh -server localhost:2181 #使用客户端连接zookeeper
ls / #查看根节点
ls /services # 查看所有服务
```

### 3 服务注册进Zookeeper

**依赖，配置，注解**同上面一样，服务间的调用采用微服务调用

```java
private static final String PAYMENT_URL="http://cloud-payment-service/";

@Autowired
private RestTemplate restTemplate;

@GetMapping("/payment")
public String payment(){
    return restTemplate.getForObject(PAYMENT_URL+"payment",String.class);
}
```

## 三 注册中心 Consul

### 1 安装 Consul

[Consul下载安装](https://www.consul.io/downloads.html)

- 解压文件

```shell
unzip consul_1.8.0_linux_amd64.zip 
```

- 将解压文件复制到指定路径下

```shell
mv consul /usr/local/bin
```

- 检测是否安装成功

```shell
consul #查看控制台，出现下面代表安装成功
root@iZ2zeh0hv4ez2i32z40npxZ:/usr/local/bin# consul
Usage: consul [--version] [--help] <command> [<args>]

Available commands are:
    acl            Interact with Consul's ACLs
    agent          Runs a Consul agent
    catalog        Interact with the catalog
    config         Interact with Consul's Centralized Configurations
    connect        Interact with Consul Connect
    debug          Records a debugging archive for operators
...
```

- 启动服务

  **-dev 开发模式**启动的时候，数据是存储在内存中，重启之后数据将丢失

```shell
consul agent -dev -client xxx.xxx.xxx.xxx #该网络ip为服务器的私有ip，该方式以dev启动，数据不持久化
```

-server 生成模式启动的时候，如果是server的话需要指定-server，如果是client的话，需要指定-client，比如

```shell
`consul agent -ui -server -bootstrap-expect 1 -data-``dir` `/tmp/consul` `-node=consul-server -bind=192.168.1.100 -client=192.168.1.100`
```

-bootstrap-expect 1 通知consul server我们现在准备加入的server节点个数，该参数是为了延迟日志复制的启动直到我们指定数量的server节点成功的加入后启动

-data-dir /tmp/consul 数据持久的路径

-node=consul-server 指定节点在集群中的名称

-bind=192.168.1.100 该地址用来在集群内部的通讯，集群内的所有节点到地址都必须是可达的，**默认是0.0.0.0**，这意味着Consulo会使用第一个可用的私有IP地址，Consul可以使用TCP和UDP并且可以使用共同的端口，如果存在防火墙，这两者协议必须是允许的

-client 指定节点为client，指定客户端接口的绑定地址，包括：HTTP、DNS、RPC，**默认是127.0.0.1**，**只允许回环接口访问**，也就是本机访问，如果要想同一局域网内的其他机器访问，需要修改成自己的内网ip

- 访问http://xxx.xxx.xxx.xxx:8500访问(注意关闭防火墙)



### 2 Consul基本入门

① 引入依赖

```xml
<!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-consul-discovery -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-consul-discovery</artifactId>
</dependency>
```

② 修改配置

```yml
server:
  port: 8006

spring:
  application:
    name: cloud-payment-service
  cloud:
    consul:
      host: xxx.xxx.xxx.xxx
      discovery:
        port: 8500
        service-name: ${spring.application.name}  #注入到consul中
```

③ 启动注解

```java
@SpringBootApplication
@EnableDiscoveryClient  //服务发现
public class CloudProviderConsulPayment8006 {
```

### 3 服务注册进Consul

**依赖，配置，注解**同上面一样，服务间的调用采用微服务调用

**调用通过微服务调用**



## 四 Ribbon负载均衡

### 1 Ribbon 基本使用

```xml
<!--其中已经引入ribbon,也可以自行引入
 <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
      <version>2.2.1.RELEASE</version>
      <scope>compile</scope>
</dependency>
-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

```java
@Configuration
public class ApplicationContextConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
```

getForObject方法/getForEntity方法

```java
@GetMapping("/getPayment2/{id}")
public CommonResult<Payment> getPaymentById2(@PathVariable("id") Long id) {
    ResponseEntity<CommonResult> entity = restTemplate.getForEntity(PAYMENT_URL + "/get/" + id, CommonResult.class);
    if(entity.getStatusCode().is2xxSuccessful()){
        return entity.getBody();
    }else {
        return CommonResult.errorOf();
    }
}
```

postForObject/postForEntity方法

略

### 2 更换Ribbon负载均衡策略

负载均衡的父类为**IRule**接口

| Ribbon名称                | Ribbon策略                                                   |
| ------------------------- | ------------------------------------------------------------ |
| RoundRobinRule            | 从服务列表里面循环取                                         |
| WeightedResponseTimeRule  | 根据每个服务的响应时间设置权重，响应时间越长，所占权重越少。 |
| ZoneAvoidanceRule         | 使用CompositePredicate根据区域和可用性过滤服务器的规则       |
| RandomRule                | 随机选择，也就是说Ribbon会随机从服务器列表中选择一个进行访问 |
| RetryRule                 | 鉴于IRule可以级联，此RetryRule类允许向现有规则添加重试逻辑。 |
| BestAvailableRule         | 跳过具有“跳闸”断路器的服务器的规则，并选择具有最低并发请求的服务器。此规则通常应与ServerListSubsetFilter一起使用，后者对规则可见的服务器设置限制。 这确保了它只需要在少量服务器中找到最小的并发请求。 此外，每个客户端将获得一个随机的服务器列表，避免了大量客户端选择一个具有最低并发请求的服务器并立即被淹没的问题。 |
| LoadBalanceCompositeRule  |                                                              |
| AvailabilityFilteringRule | 过滤掉那些因为一直连接失败的被标记为circuit tripped的后端server，并过滤掉那些高并发的的后端server（active connections 超过配置的阈值） |

**注：当自定义Ribbon策略时，不能把Ribbon放到@ComponentScan包下，不然会每个客户端都会使用这个策略**

采用Ribbon随机策略

- 注入随机策略

```java
@Configuration
public class MyIRule {

    @Bean
    public IRule mySelfIRule(){
        return new RandomRule();  //随机策略
    }
}
```

- 在@ComponentScan包下的配置类上加入@RibbonClient注解

```java
@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient //开启服务发现
@RibbonClient(name = "CLOUD-PAYMENT-SERVICE",configuration = {MyIRule.class})//服务名必须和restTemplate中调用的微服务的大小写一致，不然自定义策略不会起作用
public class CloudConsumerOrder80 {
    public static void main(String[] args) {
        SpringApplication.run(CloudConsumerOrder80.class, args);
    }
}
```

### 3 手写算法Ribbon轮询

① 关闭SpringCloud自己的轮询算法

② 自定义接口和实现类

```java
//接口
public interface LoadBalanced {

    ServiceInstance getInstance(List<ServiceInstance> instances);
}
//实现类
@Component
public class MyLoadBalanced implements LoadBalanced{

    private AtomicInteger atomicInteger=new AtomicInteger(0);  //初始值为0

    /*
    * 这里采用CAS自旋锁来代替synchronized等关键字
    * ① 这里采用CAS自旋锁 和 synchronized 关键字一样,是为了防止当有多个线程访问该方法		的时候能保持方法的原子性,不会影响算法的最终结果
    * ② 采用synchronized对CPU功能消耗比较高,这里采用CAS自旋锁更为合适.
    * */
    private final int getServerIndex(){
        for (;;){
            int current=atomicInteger.get();
            int next=current>Integer.MAX_VALUE?0:current+1;
            /*
            * compareAndSet:
            * 当初始值(AtomicInteger atomicInteger=new AtomicInteger(0))与实际				 值(current)一样
            * 就会把next替换为atomicInteger.get()的值
            * */
            if(atomicInteger.compareAndSet(current,next)){
                return next;
            }
        }
    }

    @Override
    public ServiceInstance getInstance(List<ServiceInstance> instances) {
        int index = getServerIndex();
        if(instances.size()==0||instances==null){
            return null;
        }else {
            int i=index%instances.size();
            return instances.get(i);
        }
    }

}
```

③ controller调用

```java
@Autowired
private DiscoveryClient discoveryClient;
@Autowired
private LoadBalanced loadBalanced;

@GetMapping("/lb")
public String lb(){
    ServiceInstance instance =         loadBalanced.getInstance(discoveryClient.getInstances("cloud-payment-service"));
    URI uri = instance.getUri();
    String serverPort = restTemplate.getForObject(uri + "/payment/lb", String.class);
    return serverPort;
}
```

④ Consumer微服务

```java
@Value("${server.port}")
private Integer serverport;
@GetMapping("/lb")
public String lb(){
    return String.valueOf(serverport);
}
```

查看结果



## 五 OpenFeign远程调用

### 1基本使用

① 引入依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

②  编写一个feign接口

```java
@FeignClient("cloud-payment-service") //告诉SpringCloud去哪个服务找
public interface PaymentFeignService {

    @GetMapping("/payment/get/{id}")  //对应的controller地址，要写全路径
    CommonResult<Payment> queryPaymentById(@PathVariable("id") Long id);
}
```

③ 启动注解

```java
@SpringBootApplication
@EnableEurekaClient
<<<<<<< HEAD
@EnableFeignClients(basePackages= {"com.cloneman.com.cloneman.com.cloneman.springcloud.feign"})
=======
@EnableFeignClients(basePackages= {"com.cloneman.com.cloneman.springcloud.feign"})
>>>>>>> 7a6e9c94be2a87ad850c8d0db9110d7bbed07393
public class CloudConsumerFeignOrder80 {
```

**Feign 模式集成了Ribbon，默认是轮询算法**

### 2 Feign超时控制

由于OpenFeign默认1秒的超时时间，所以当调用超时的时候就可以自己设定OpenFegin的超时时间，由于OpenFeign集成了Ribbon,所以超时时间由Ribbon来管理

```yml
ribbon:
  ReadTimeout: 60000
  ConnectTimeout: 60000
```

### 3 OpenFeign 增强日志



| OpenFeign日志级别 | 描述                                          |
| ----------------- | --------------------------------------------- |
| NONE              | 默认，不显示任何日志                          |
| BASIC             | 仅记录请求方法，URL，响应状态码及请求时间     |
| HEADERS           | 除了BASIC信息，还有请求和响应头信息           |
| FULL              | 除了HEADERS信息，还有请求和响应的正文及元数据 |

使用：

- 开启Feign日志级别

```java
//开启Feign日志级别
@Configuration
public class FeignLoggerLevelConfig {

    @Bean
    public Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }
}
```

- 打印日志

```yml
logging:
  level:
<<<<<<< HEAD
    com.cloneman.com.cloneman.com.cloneman.springcloud.feign:
=======
    com.cloneman.com.cloneman.springcloud.feign:
>>>>>>> 7a6e9c94be2a87ad850c8d0db9110d7bbed07393
      debug
```

- 观察控制台



## 六 Hystrix服务降级

### 1 Hystrix基本使用(服务降级)

① 引入依赖(提供者和消费者都可以用,主要用在消费者)

```xml
<!--新增hystrix-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
```

② 在要进行服务熔断的方法上加入@HystrixCommand

**要求FallBack方法和调用方法的返回值类型，参数列表必须一致**

```java
@GetMapping("/hystrix_error/{id}")
@HystrixCommand(
    fallbackMethod = "hystrix_error_handle",  //表示出错了，要调用的方法
    commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
    }
)
public String hystrix_error(@PathVariable("id") Long id) {
    //int i=1/0;出现异常也可以处理
    return paymentFeignService.hystrix_error(id);
}
//要求FallBack方法和调用方法的返回值类型，参数列表必须一致
public String hystrix_error_handle(@PathVariable("id") Long id) {
    return "服务超时或者服务异常，请稍后再试！";
}
```

③ 主启动类加上`@EnableCircuitBreaker` 或者 `@EnableHystrix`注解

```java
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableHystrix  //开启Hystrix功能
public class CloudConsumerFeignHystrixOrder80 {
```

### 2 全局服务异常处理(服务降级)

① 在服务调用类上方加入：

```java
@DefaultProperties(defaultFallback = "fallBack")
```

② 在方法需要返回错误信息的服务调用方法上直接加入注解即可：

```java
@HystrixComman
```

③ 由于此中方式，FallBack方法属于统一错误处理方法，应当适用所有服务调用发放的错误调用，因此此种方式默认调用**空参**FallBack方法，所以改FallBack方法的参数列表为空参即可：

```java
public String fallBack(){        
    return "服务太拥挤了，请稍后再试！"; 
}
```

例：

```java
@RestController
@RequestMapping("/consumer/order")
@DefaultProperties(defaultFallback = "hystrix_global_error_handle")
public class OrderController {

    @Autowired
    private PaymentFeignService paymentFeignService;

    @GetMapping("/hystrix_ok/{id}")
    @HystrixCommand
    public String hystrix_ok(@PathVariable("id") Long id) {
        int a=1/0;
        return paymentFeignService.hystrix_ok(id);
    }

    @GetMapping("/hystrix_error/{id}")
    @HystrixCommand(
        fallbackMethod = "hystrix_error_handle",
        commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
        }
    )
    public String hystrix_error(@PathVariable("id") Long id) {
        int a=1/0;
        return paymentFeignService.hystrix_error(id);
    }

    public String hystrix_error_handle(@PathVariable("id") Long id) {
        return "服务超时或者服务异常，请稍后再试！";
    }

    public String hystrix_global_error_handle() {
        return "全局 服务超时或者服务异常，请稍后再试！";
    }
}
```

当一个方法有了自己的熔断方法处理后，会去调用自己的，如果没有，就去找全局的

### 3 服务熔断

**服务熔断就是指当服务连续一段时间出现错误，会自己停掉服务，等过一段时间后会慢慢恢复**

例：

```java
//=============服务熔断========================//
@HystrixCommand(
    fallbackMethod = "paymentCircuitBreaker_fallback",
    commandProperties = {
        @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),    //是否开启断路器
        @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),     //请求次数
        @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),    //时间范围
        @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),  //失败率达到多少后跳闸
    })
public String paymentCircuitBreaker(Long id) {
    if (id < 0) {
        throw new RuntimeException(+id + ":" + "*****id 不能负数");
    }
    String serialNumber = IdUtil.simpleUUID();

    return Thread.currentThread().getName() + "\t" + "调用成功,流水号：" + serialNumber;
}

public String paymentCircuitBreaker_fallback(Long id) {
    return "id:"+id+" 不能为负数,请稍后才尝试";
}
```

**注意开启Hystrix功能@EnableHystrix**

### 4 Hystrix Dashboard

① 创建应用加入依赖

```xml
<!--新增hystrix dashboard-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
</dependency>
```

**注:本服务和需要检测的服务都必须引入依赖**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

② 开启注解

```java
@SpringBootApplication
@EnableHystrixDashboard
public class CloudConsumerHystrixDashboard9001 {
```

③ 启动访问

```html
http://localhost:9001/hystrix  <!--出现图像化界面表示成功-->
```

④ 新Hystrix需要在要检测的服务上加入

```java
@Bean
public ServletRegistrationBean getServlet(){    HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();    ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
    registrationBean.setLoadOnStartup(1);
    registrationBean.addUrlMappings("/hystrix.stream");
    registrationBean.setName("HystrixMetricsStreamServlet");
    return registrationBean;
}
```

⑤ 启动服务,输入访问地址

```html
http://localhost:xxxx/hystrix.stream
```



## 七 GateWay网关

### 1 GateWay的基本使用

① 新建应用, 引入依赖

```xml
<!--新增gateway-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
<!--gateway也需要将自己注册到注册中心中-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

② yml配置

```yml
server:
  port: 9527
spring:
  application:
    name: cloud-gateway-service
  cloud:
    gateway:
      routes:
       - id: payment_route01
         uri: http://127.0.0.1:8001
         predicates:
           - Path=/payment/get/**
       - id: payment_route02
         uri: http://127.0.0.1:8001
         predicates:
           - Path=/payment/lb/**

eureka:
  client:
    fetch-registry: true
    register-with-eureka: true
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka
  instance:
    hostname: gateway9527
```

### 2 GateWay路由

① 一种yml配置 如上

② 注入容器

```java
@Configuration
public class GateWayConfig {

    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        routes.route("payment_route03", r -> r.path("/guonei").uri("http://news.baidu.com/guonei")).build();
        return routes.build();
    }
}
```

### 3 动态路由

```yml
spring:
  application:
    name: cloud-gateway-service
  cloud:
    gateway:
      routes:
       - id: payment_route01
         uri: lb://cloud-payment-service  #lb为负载均衡
         #uri: http://127.0.0.1:8001
         predicates:
           - Path=/payment/get/**
       - id: payment_route02
         uri: lb://cloud-payment-service
         #uri: http://127.0.0.1:8001
         predicates:
           - Path=/payment/lb/**
```



### 4 Predicates

[**参考官网**](https://cloud.spring.io/spring-cloud-static/Greenwich.SR5/single/spring-cloud.html)

| 路由规则                           | 路由详细                                                     |
| ---------------------------------- | ------------------------------------------------------------ |
| After Route Predicate Factory      | - After=2017-01-20T17:42:47.789-07:00[America/Denver]        |
| Before Route Predicate Factory     | - Before=2017-01-20T17:42:47.789-07:00[America/Denver]       |
| Between Route Predicate Factory    | - Between=2017-01-20T17:42:47.789-07:00[America/Denver], 2017-01-21T17:42:47.789-07:00[America/Denver] |
| Cookie Route Predicate Factory     | - Cookie=chocolate, ch.p                                     |
| Header Route Predicate Factory     | - Header=X-Request-Id, \d+                                   |
| Host Route Predicate Factory       | - Host=**.somehost.org, * *.anotherhost.org                  |
| Method Route Predicate Factory     | - Method=GET,POST                                            |
| Path Route Predicate Factory       | - Path=/foo/{segment},/bar/{segment}                         |
| Query Route Predicate Factory      | - Query=baz                                                  |
| RemoteAddr Route Predicate Factory | - RemoteAddr=192.168.1.1/24                                  |
| Weight Route Predicate Factory     | - Weight=group1, 8                                           |

### 5 Filters

[**官网参考**](https://cloud.spring.io/spring-cloud-static/Greenwich.SR5/single/spring-cloud.html#_gatewayfilter_factories)

自定义过滤器

```java
//自定义全局过滤器,实现GlobalFilter, Ordered接口
@Component
public class MyLogGlobalFilter implements GlobalFilter, Ordered {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyLogGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        LOGGER.info("欢迎进入gateway ");
        String username = exchange.getRequest().getQueryParams().getFirst("username");
        if (null == username) {
            exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);  //如果具有username 就把exchange 传入下一个过滤器
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
```


## 八 Config配置中心

### 1 Config服务端搭建

首页要在自己的github库上配置放一些配置文件

① 新建工程,引入依赖

```xml
<!--配置中心-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-server</artifactId>
</dependency>
<!--需要将配置中心注册到注册中心-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

② 修改配置

```yml
server:
  port: 3344

spring:
  application:
    name: cloud-config-center-service
  cloud:
    config:
      server:
        git:
          uri: https://github.com/2482118722ysj/SpringCloud2020-Config.git  #自己的github路径
          search-paths:
            - SpringCloud2020-Config # github库
      label: master

eureka:
  client:
    fetch-registry: true
    register-with-eureka: true
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka
```

③ 开启注解

```java
@EnableConfigServer
@SpringBootApplication
public class CloudConfigCenter3344 {
```

④ 启动服务访问文件

```html
http://127.0.0.1:3344/master/config-dev.yml <!--访问指定的文件-->
```

### 2 Config客户端搭建

① 新建应用，引入依赖

```xml
<!--引入Config客户端依赖-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
<!--引入eureka客户端依赖-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

**② 建立bootstrap.yml文件(bootstrap.yml比application.yml文件优先级要高)**

```yml
server:
  port: 3355

spring:
  application:
    name: cloud-config-client-service
  cloud:
    config:
      label: master  #分支
      name: config   #配置文件名
      profile: dev   #配置环境
      uri: http://127.0.0.1:3344/
eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka
```

③ 获取配置

```java
@RestController
public class ConfigClientController {

    @Value("${config.info}")
    private String configInfo;

    @GetMapping("/config/info")
    public String configInfo(){
        return this.configInfo;
    }
}
```

④ 访问

```html
http://localhost:3355/config/info
```

### 3 手动刷新服务配置

① Config-Client必须引入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

② 修改配置，暴露端口

```yml
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

③ 加入注解

```java
@RefreshScope  //动态刷新值,必须加载controller上
@RestController
public class ConfigClientController {
```

④ 必须POST刷洗一下服务

```html
curl -X POST "http://localhost:3355/actuator/refresh" <!--此方案不合理,当有多个微服务,难道要一个一个刷新吗？因此要引入消息总线-->
```

然后再次访问看是否动态变化



## 九 BUS总线

### 1  [RabbitMq 安装](https://blog.csdn.net/qq_22638399/article/details/81704372)

- 1 由于rabbitMq需要erlang语言的支持，在安装rabbitMq之前需要安装erlang，执行命令：

```shell
apt-get install erlang-nox     # 安装erlang
erl    # 查看relang语言版本，成功执行则说明relang安装成功
```

- 2 添加公钥

```shell
wget -O- https://www.rabbitmq.com/rabbitmq-release-signing-key.asc | sudo apt-key add -
```

- 3 更新软件包

```shell
apt-get update
```

- 4 安装 RabbitMQ

```shell
apt-get install rabbitmq-server  #安装成功自动启动
```

- 5 查看 RabbitMq状态

```shell
systemctl status rabbitmq-server   #Active: active (running) 说明处于运行状态
# service rabbitmq-server status 用service指令也可以查看，同systemctl指令
```

- 6 启动、停止、重启

```shell
service rabbitmq-server start    # 启动
service rabbitmq-server stop     # 停止
service rabbitmq-server restart  # 重启 
```

执行了上面的步骤，rabbitMq已经安装成功。

- 7 启用 web端可视化操作界面，我们还需要配置Management Plugin插件 

```shell
rabbitmq-plugins enable rabbitmq_management   # 启用插件
service rabbitmq-server restart    # 重启
```

此时，应该可以通过 http://localhost:15672 查看，使用默认账户guest/guest 登录。
注意：RabbitMQ 3.3 及后续版本，guest 只能在服务本机登录。
瞄了一眼官方文档，说的是默认会创建guest用户，但是只能服务器本机登录，建议创建其他新用户，授权，用来做其他操作。

- 8 查看用户

```shell
rabbitmqctl list_users
```

- 9 添加管理用户

```shell
rabbitmqctl add_user admin yourpassword   # 增加普通用户
rabbitmqctl set_user_tags admin administrator    # 给普通用户分配管理员角色 
```

ok，你可以在你的浏览器上输入：[http://服务器Ip:15672/](http://xn--ip-fr5c86lx7z:15672/) 来访问你的rabbitmq监控页面。使用刚刚添加的新用户登录。

### 2 BUS动态刷新全局广播配置

有两种方案:

① 采用刷新一个**Config客户端**，然后广播给其他客户端，实现全部刷新(不太合理)

② 采用刷新**Config服务端**，然后广播给其他客户端，实现全部刷新(采用这种)

实现:

① 给要**Config服务端**和**Config客户端**加入依赖

```xml
<!--BUS总线依赖,支持rabbitmq和kafka-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bus-amqp</artifactId>
</dependency>
```

② Config服务端配置

```yml
spring:
  rabbitmq:
    virtual-host: /
    host: xxx.xxx.xxx.xxx
    port: 5672
<<<<<<< HEAD
    username: xxx # 注意，如果新添加的用户，要去rabiitmq给admin分配virtual-host为"/"的权限
    password: xxx
=======
    username: xxxx # 注意，如果新添加的用户，要去rabiitmq给admin分配virtual-host为"/"的权限
    password: xxxx
>>>>>>> ab737bc90b0082add4cb1ddfdffcc22f7819b8d1
management:
  endpoints:
    web:
      exposure:
        include: "bus-refresh"
```

③ Config客户端配置

```yml
spring:
  rabbitmq:
    virtual-host: /
    host: xxx.xxx.xxx.xxx
    port: 5672
    username: xxx
    password: xxx
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

④ POST刷新Config服务端

```html
curl -X POST "http://localhost:3344/actuator/bus-refresh" <!--只用刷新Config服务端-->
```

⑤ 再次访问

```html
http://localhost:3355/config/info,http://localhost:3366/config/info
```

**观察rabbitmq 的交换机会发现，消息通知本质是以一个交换机名字是springCloudBus的交换机起作用的**

### 3 BUS动态刷新定点通知

```html
<!--curl -X POST "http://配置中心地址/actuator/bus-refresh/微服务名:端口号"-->
例：curl -X POST "http://localhost:3344/actuator/bus-refresh/cloud-config-client-service:3355"
```

## 十  Stream消息驱动

[**官网参考**](https://spring.io/projects/spring-cloud-stream)

目前支持支**RibbtMQ**和**Kafka**

### 1 搭建服务发送者

① 引入依赖

```xml
<!--SprngCloud-Stream-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-stream-rabbit</artifactId>
</dependency>
<!--需要加入注册中心-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

② 添加配置

```yml
server:
  port: 8801
spring:
  application:
    name: cloud-stream-provider-service
  cloud:
    stream:
      binders: # 在此处配置要绑定的rabbitmq的服务信息
        defaultRabbit:  # 表示定义的名称，用于于binding整合
          type: rabbit  # 消息组件类型
          environment:  # 设置rabbitmq的相关的环境配置
            spring:
              rabbitmq:
                host: xxx.xxx.xxx.xxx
                port: 5672
                username: xxx
                password: xxx
      bindings:  # 服务的整合处理
        output:  # 这个名字是一个通道的名称
          destination: studyExchange  # 表示要使用的Exchange名称定义
          content-type: application/json  # 设置消息类型，本次为json，文本则设置“text/plain”
          binder: defaultRabbit # 设置要绑定的消息服务的具体设置

eureka:
  client:
    fetch-registry: true
    register-with-eureka: true
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka
```

③ 编写发送消息业务

```java
//接口
public interface IMessageProvider {
    String send();
}
//实现类
@EnableBinding(Source.class) //定义消息的推送管道
public class IMessageProviderImpl implements IMessageProvider {

    @Resource  
    private MessageChannel input;  //消息发送管道,这里要用input和@Resource,这里IOC找那个注入了三个MessageChannel,这里需要按名注入

    @Override
    public String send() {
        String message = UUID.randomUUID().toString();
        input.send(MessageBuilder.withPayload(message).build());
        System.out.println("...............message:"+message);
        return null;
    }
}
//controller
@RestController
public class StreamRabbitMQProviderController {

    @Autowired
    private IMessageProvider iMessageProvider;

    @GetMapping("/send")
    public String send(){
        return iMessageProvider.send();
    }

}
```

启动Eureka，发送消息，观察RabbitMQ界面

### 2 搭建服务消费者

① 依赖和搭建服务者一样

② 配置

```yml
server:
  port: 8802
spring:
  application:
    name: cloud-stream-consumer-service  #注意变化
  cloud:
    stream:
      binders: 
        defaultRabbit:  
          type: rabbit  
          environment:  
            spring:
              rabbitmq:
                virtual-host: /
                host: xxx.xxx.xxx.xxx
                port: 5672
                username: xxx
                password: xxx
      bindings:  
        input:  # 注意修改
          destination: studyExchange  
          content-type: application/json  
          binder: defaultRabbit 
eureka:
  client:
    fetch-registry: true
    register-with-eureka: true
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka
```

③ 接受业务类

```java
@Component
@EnableBinding(Sink.class)
public class ReceiveMessageListenerController {

    @Value("${server.port}")
    private String serverPort;

    /*
    * 发送时output.send(MessageBuilder.withPayload(message).build());message为String,
    * 所以这里用Message<String> 接受
    * */
    @StreamListener(Sink.INPUT)  //监听消息
    public void receive(Message<String> message){
        System.out.println("接受到的消息是："+message.getPayload()+"    port:"+serverPort);
    }
}
```

### 3 分组消费和持久化

**同一个组的消费者只会有一个消费者消费(同一个组中存在竞争关系)，在两个完全不同的组会重复消费**

(1)分组消费

```yml
server:
  port: 8801
spring:
  application:
    name: cloud-stream-provider-service
  cloud:
    stream:
      binders: 
        defaultRabbit:  
          type: rabbit  
          environment:  
            spring:
              rabbitmq:
                virtual-host: /
                host: xxx.xxx.xxx.xxx
                port: 5672
                username: xxx
                password: xxx
      bindings:  
        output:  
          destination: studyExchange 
          content-type: application/json  
          binder: defaultRabbit 
          group: cloud-stream-01  # 将两个消费者改为同一个，满足了竞争关系，就只有一个会消费
eureka:
  client:
    fetch-registry: true
    register-with-eureka: true
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka
```

(2) 消息持久化

当**两个消费**者都停掉，然后**一个消费者去掉分组**，**另一个消费者不去掉分组**，然后分别重启两个服务,会发现当**去掉分组的会错过消费**，**保持分组的不会错过消费**

## 十一 Sleuth分布式请求链路追踪

### 1 简介

Spring Cloud Sleuth提供了一套完整的服务跟踪的解决方案，在分布式系统中提供追踪解决方案并且兼容支持了zipkin

SpringCloud从F版起已不需要自己构建Zipkin server了，只需要调用jar包即可

[**Zipkin下载地址**](https://dl.bintray.com/openzipkin/maven/io/zipkin/java/zipkin-server/)

zipkin-server-2.12.9.exec.jar

```shell
java -jar xxx 运行
http://localhost:9411/zipkin/  #访问成功，则部署成功
```

### 2 使用

① 要跟踪的服务引入依赖，不用给注册中心引入

```xml
<!--包含了sleuth+zipkin-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>
<dependency> 
    <groupId>org.springframework.cloud</groupId> 
    <artifactId>spring-cloud-sleuth-zipkin</artifactId> 
</dependency> 
```

② 修改要跟踪的服务的配置，不用修改注册中心

```yml
spring:
  zipkin:
    base-url: http://xxx.xxx.xxx.xxx:9411
  sleuth:
    sampler:
    probability: 1
```

③ 启动服务，然后通过consumer调用provider,最后查看http://localhost:9411

<<<<<<< HEAD

# SpringCloud-Alibaba

## 一 Nacos注册中心和配置中心

### 1 Nacos下载安装

[**Nacos下载安装**](https://github.com/alibaba/nacos/releases)

windows就启动下面**startup.cmd**,如果是linux就启动**startup.sh**

[**Nacos参考文档**](https://nacos.io/zh-cn/docs/quick-start.html)

### 2 Nacos注册中心

[**SpringCloud-Alibaba官方文档**](https://github.com/alibaba/spring-cloud-alibaba/blob/master/README-zh.md)

统一说明:在使用SpringCloud-Alibaba之前要在父pom中引入

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-alibaba-dependencies</artifactId>
            <version>2.2.0.RELEASE</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

[**SpringCloud-Nacos-Discovery**](https://github.com/alibaba/spring-cloud-alibaba/blob/master/spring-cloud-alibaba-examples/nacos-example/nacos-discovery-example/readme-zh.md)

① 引入依赖

```xml
 <dependency>
     <groupId>com.alibaba.cloud</groupId>
     <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
 </dependency>
```

② 配置

```yml
server:
  port: 9001
spring:
  application:
    name: cloudalibaba-provider-payment-servcie
  cloud:
    nacos:
      discovery:
        server-addr: 39.97.188.145:8848

management:
  endpoints:
    web:
      exposure:
        include: "*"
```

③ 开启注释

```java
@SpringBootApplication
@EnableDiscoveryClient  //开启服务
public class CloudAlibabaProvierPayment9001 {
```

**Nacos默认继承了Ribbon**



### 3 Nacos配置中心

① 引入依赖

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
```

② 配置

```yaml
# bootstrap.yml
server:
  port: 3377

spring:
  application:
    name: cloudalibaba-config-center
  cloud:
    nacos:
      config:
        server-addr: 39.97.188.145:8848
        group: dev
        file-extension: yaml  # 这里使用yaml,nacos配置中心也必须是yaml为后缀
# application.yml
spring:
  profiles:
    active: dev
```

**SpringCloud在进行配置的时候，会先扫描本地的bootstrap.yaml和application.yml，然后再去寻找配置中心上的配置文件**

### 4 DataID &GROUP & Namespace

**dataID**

在 Nacos Config Starter 中，dataId 的拼接格式如下

```shell
${prefix} - ${spring.profiles.active} . ${file-extension}
```

- `prefix` 默认为 `spring.application.name` 的值，也可以通过配置项 `spring.cloud.nacos.config.prefix`来配置。

- `spring.profiles.active` 即为当前环境对应的 profile，详情可以参考 [Spring Boot文档](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-profiles.html#boot-features-profiles)

  **注意，当 activeprofile 为空时，对应的连接符 - 也将不存在，dataId 的拼接格式变成 ${prefix}.${file-extension}**

- `file-extension` 为配置内容的数据格式，可以通过配置项 `spring.cloud.nacos.config.file-extension`来配置。 目前只支持 `properties` 类型。

**group**

- `group` 默认为 `DEFAULT_GROUP`，可以通过 `spring.cloud.nacos.config.group` 配置。

**namespace**

namespace 可以自由创建，默认**public**

### 5 Nacos集群

#### (1) 搭建Nacos集群

[**Nacos官网集群搭建**](https://nacos.io/zh-cn/docs/cluster-mode-quick-start.html)

**第一种方法:**

① 修改conf/cluster.conf文件，修改配置

```conf
xxx.xxx.xxx.xxx:18848  # xxx.xxx.xxx.xxx为hostname -i 的ip
xxx.xxx.xxx.xxx:28848
xxx.xxx.xxx.xxx:38848
```

② 复制三分nacos，然后对conf/application.properties的端口号进行修改,但是要保证数据库连接同一个

```properties
server.port=18848
#server.port=28848
#server.port=38848
spring.datasource.platform=mysql


db.num=1

db.url.0=jdbc:mysql://xxx.xxx.xxx.xxx:3306/nacos?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
db.user=xxx
db.password=xxx
```

③ 分别启动三个不同的服务

**第二种方法**

使用同一个nacos，根据端口号启动不同的实例

① 需要修改startup.sh

```sh
while getopts ":m:f:s:p:" opt  #修改位置 加入p:
do
    case $opt in
        m)
            MODE=$OPTARG;;
        f)
            FUNCTION_MODE=$OPTARG;;
        s)
            SERVER=$OPTARG;;
        p)                         # 修改位置,注意
            PORT=$OPTARG;;         # 修改位置,注意
        ?)
        echo "Unknown parameter"
        exit 1;;
    esac
done

# 接着修改本文件下的 -Dserver.port=${PORT},注意位置
# start
echo "$JAVA ${JAVA_OPT}" > ${BASE_DIR}/logs/start.out 2>&1 &
nohup $JAVA -Dserver.port=${PORT} ${JAVA_OPT} nacos.nacos >> ${BASE_DIR}/logs/start.out 2>&1 &
echo "nacos is starting，you can check the ${BASE_DIR}/logs/start.out"
```

② 启动

```shell
./startup.sh -p 18848
./startup.sh -p 28848
./startup.sh -p 38848
```

#### (2) 搭建nginx

① nginx安装

```
# 安装gcc g++的依赖库
sudo apt-get install build-essential
sudo apt-get install libtool

#安装pcre依赖库（http://www.pcre.org/）
sudo apt-get update
sudo apt-get install libpcre3 libpcre3-dev

#安装zlib依赖库（http://www.zlib.net）
sudo apt-get install zlib1g-dev

#安装SSL依赖库（16.04默认已经安装了）
sudo apt-get install openssl

#安装Nginx
wget http://nginx.org/download/nginx-1.13.6.tar.gz

# 检查
./configure 

# 编译安装nginx
make && make install

# 进入安装目录
cd /usr/local/nginx/sbin

# 启动nginx
./nginx
访问服务器ip,看是否是nginx页面(nginx默认端口80)
```

② nginx负载均衡配置

```conf
server {
        listen       8888;  # 修改默认端口
        server_name  localhost;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;


        location / {
            root   html;
            proxy_pass  http://nacos-cluster;  # 负载均衡转发点
            index  index.html index.htm;
        }
        
======================================
upstream nacos-cluster{  # 负载均衡有关配置
          server 39.97.188.145:18848; 
          server 39.97.188.145:28848;
          server 39.97.188.145:38848;
}
```

③ 启动nginx，然后访问，看是否能负载均衡成功

#### (3) 提供者和消费者

提供者注册:

```yml
server:
  port: 9002
spring:
  application:
    name: cloudalibaba-provider-payment-service
  cloud:
    nacos:
      discovery:
        server-addr: xxx.xxx.xxx.xxx:8888  #修改为nginx的ip地址
      #server-addr: xxx.xxx.xxx.xxx:8848
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

消费者消费

```yml
server:
  port: 83

spring:
  application:
    name: cloudalibaba-consumer-order-service
  cloud:
    nacos:
      discovery:
       # server-addr: xxx.xxx.xxx.xxx:8848
        server-addr: xxx.xxx.xxx.xxx:8888 #修改为nginx的ip地址

spring-nacos:
  provider-url: http://cloudalibaba-provider-payment-service

management:
  endpoints:
    web:
      exposure:
        include: "*"
```

注:当消费者访问提供者的时候，必须采用负载均衡策略

```java
@Configuration
public class ApplicationContextConfig {

    @Bean
    @LoadBalanced  //比加,不然会出错
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
```

注：当服务注册到nginx的时候，nginx会采用轮询的负载均衡策略把一台**服务**注册到随机的一台nacos服务器上，但是**nacos配置了cluster.conf，nacos构成了集群**，所以**服务**会在每个nacos中同步

## 二 Sentinel 服务降级和服务熔断

### 1 Sentinel的下载安装

[**Sentinel下载安装启动**](https://github.com/alibaba/Sentinel/releases)

```shell
sentinel-dashboard-1.7.2.jar  # 下载
java -jar sentinel-dashboard-1.7.2.jar  #启动
```

### 2 Sentinel基本使用

① 引入依赖

```xml
<!--Nacos注册中心-->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
<!--sentinel持久化-->
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-datasource-nacos</artifactId>
</dependency>
<!--sentinel 依赖jar-->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
</dependency>
```

② 配置

```yml
server:
  port: 8401

spring:
  application:
    name: cloudalibaba-sentinel-service
  cloud:
    nacos:
      discovery:
        server-addr: 39.97.188.145:8848
    sentinel:
      transport:
        dashboard: 127.0.0.1:8080 #使用sentinel检测服务，
        port: 8719  #指定应用与Sentinel控制台交互的端口，应用本地会起一个该端口占用的HttpServer
```

controller

```java
@RestController
public class SentinelController {

    @GetMapping("/testA")
    public String testA(){
        return ">>>>testA";
    }

    @GetMapping("/testB")
    public String testB(){
        return ">>>>testB";
    }
}
```



**sentinel 是懒加载，只有当访问sentinel时，才会在sentinel控制台中进行显示**

注意:如果是Sentinel放在服务器上，但是应用在本地，启动应用可以在sentinel中检测到，但是在无法查看应用的一些具体信息，要保持服务器与本地ntp时间一值

**Q: 客户端和控制台不在一台机器上，客户端成功接入控制台后，控制台无法显示实时的监控数据？但簇点链路页面有实时请求数据（不为 0）？**

**A: 请确保 Sentinel 控制台所在的机器时间与自己应用的机器时间保持一致（通过 NTP 等同步）。Sentinel 控制台是通过控制台所在机器的时间戳拉取监控数据的，因此时间不一致会导致拉不到实时的监控数据。**

[**官网问题参考**]([https://github.com/alibaba/Sentinel/wiki/FAQ#q-sentinel-%E6%8E%A7%E5%88%B6%E5%8F%B0%E6%B2%A1%E6%9C%89%E6%98%BE%E7%A4%BA%E6%88%91%E7%9A%84%E5%BA%94%E7%94%A8%E6%88%96%E8%80%85%E6%B2%A1%E6%9C%89%E7%9B%91%E6%8E%A7%E5%B1%95%E7%A4%BA%E5%A6%82%E4%BD%95%E6%8E%92%E6%9F%A5](https://github.com/alibaba/Sentinel/wiki/FAQ#q-sentinel-控制台没有显示我的应用或者没有监控展示如何排查))

### 3 Sentinel流控规则

#### (1) QPS

QPS的阈值表示当1时，如果一秒内超过一次访问就会报错,主要是在**未进入程序之前**就进行限流

#### (2) 线程数

表示内部服务程序只有一个线程，如果有多个请求访问，一个线程没处理完，就不处理其他线程，主要是在进入**程序内部后**进行限流

#### (3) 关联

当testA关联testB,当testB出了事，testA不能被访问

#### (4) 预热(warm up)

公式：阈值除以coldFactor（默认值为3），经过预热时长后才会达到阈值

默认coldFactor为3，即请求QPS从threshold/3开始，经预热时长逐渐升至设定的QPS阈值。

当阈值为10，预热时长5:  即阈值从10/3开始，经过5秒后，到达阈值10

#### (5) 排队等待

当超过QPS的阈值，就会等待xxx秒

### 4 Sentinel 降级

#### (1) RT

当RT 为200ms时，当时间窗口期1 时，表示当一秒内超过5次请求,并且处理时间超过200ms,就会在接下来的1秒内服务用。

#### (2) 异常比例

```java
@GetMapping("/testC")
public String testC(){
    try {
        Thread.sleep(1000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    return ">>>>testC";
}
```



当异常比例为0.2,时间窗口5 时，表示当一秒内超过5次请求,并且请求次超过阈值的20%，就会服务降级，在未来的5秒内不可用。

#### (3) 异常数

**异常数是按分钟统计的**

```java
@GetMapping("/testD")
public String testD(){
    int a=1/0;
    return ">>>>testD";
}
```



当异常数为5，时间窗口5，就表示在一秒内超过5次请求,并且异常次数超过5次，就会降级，就会在未来的5秒内服务不可用。

### 5 热点key限流

#### (1) 基础使用

```java
@GetMapping("/testHotKey")
//value="testHotKey",表示当热点key配置低的标识为testHotKey,本方法的@SentinelResource就会起作用，blockHandler表示要处理的方法
@SentinelResource(value = "testHotKey",blockHandler = "handle_HotKey")
public String testHotKey(@RequestParam(required = false) String param1,
                         @RequestParam(required = false) String param2){
    return ">>>>testHotKey";
}
//BlockException比传
public String handle_HotKey(String param1, String param2, BlockException ex){
    return "服务异常，稍后再试";
}
```

![](/images/001.png)

资源名和@SentinelResource的value值保持一致,参数索引为0表示，传入的参数的索引值都是0开始，这里表示监视第一个参数,当第一个参数使用是，就会触发限流。

#### (2) 参数例外项

参数例外项就是当参数的值为一个特殊的值的时候，可以自定义其他规则

例如：当param1为5时，就可以自定义自己的规则

![](/images/002.png)

**参数类型和自己自定义的一致，参数值为5时，此时阈值可以达到200**

注：当自己的代码编写中出现异常，@SentinelResource不会起作用，@SentinelResource只会起作用在Sentinel的控制台中的配置，自己代码配置并不会起作用。

### 6 系统规则

定义全局的QPS,当阈值超过设置的值，每隔接口都不能使用。

### 7 @SentinelResource 配置

#### (1) 按资源名称限流+按URL限流

当加入@SentinelResource注解后,可以按资源名称限流

```java
@GetMapping("/byResource") //按url限流
@SentinelResource(value = "byResource",blockHandler = "handle_byResource")
public CommonResult byResource(){ //@SentinelResource按名称限流
    return CommonResult.okOf();
}

public CommonResult handle_byResource(BlockException ex){
    return CommonResult.errorOf();
}

@GetMapping("/rateLimit/byUrl")
@SentinelResource(value = "byUrl")
public CommonResult byUrl()
{
    return new CommonResult(200,"按url限流测试OK",new Payment(2020L,"serial002"));
}
```

#### (2) 客户自定义限流处理逻辑

```java
//自定义全局Sentinel处理类
public class CustomSentinelHandler {
    /*
    static 比加
    */
    public static CommonResult handler1(BlockException ex){
        return CommonResult.errorOf();
    }

    public static CommonResult handler2(BlockException ex){
        return CommonResult.errorOf();
    }
}
//方法中引用
@GetMapping("/customhandler")
@SentinelResource(value = "customhandler",
                  blockHandlerClass = CustomSentinelHandler.class,  //那个类
                  blockHandler = "handler1")  //那个方法
public CommonResult customhandler() {
    return CommonResult.okOf();
}
```

**注意：@SentinelResource不支持private方法**

### 8 服务熔断

#### (1) 服务熔断Ribbon

```java
@RequestMapping("/consumer/fallback/{id}")
//@SentinelResource(value = "fallback") //没有配置
//@SentinelResource(value = "fallback",fallback = "handlerFallback") //fallback只负责业务异常
//@SentinelResource(value = "fallback",blockHandler = "blockHandler") //blockHandler只负责sentinel控制台配置违规
@SentinelResource(value = "fallback",fallback = "handlerFallback", blockHandler = "blockHandler",
                  exceptionsToIgnore = {IllegalArgumentException.class})
public CommonResult fallback(@PathVariable Long id) {
    CommonResult<Payment> result = restTemplate.getForObject(SERVICE_URL + "/paymentSQL/" + id, CommonResult.class, id);

    if (id == 4) {
        throw new IllegalArgumentException("IllegalArgumentException,非法参数异常....");
    } else if (result.getData() == null) {
        throw new NullPointerException("NullPointerException,该ID没有对应记录,空指针异常");
    }

    return result;
}

//fallback
public CommonResult handlerFallback(@PathVariable Long id, Throwable e) {
    Payment payment = new Payment(id, "null");
    return new CommonResult<>(444, "兜底异常handlerFallback,exception内容  " + e.getMessage(), payment);
}

//blockHandler
public CommonResult blockHandler(@PathVariable Long id, BlockException blockException) {
    Payment payment = new Payment(id, "null");
    return new CommonResult<>(445, "blockHandler-sentinel限流,无此流水: blockException  " + blockException.getMessage(), payment);
}

```



- fallback管理java编写的异常
- blockHandler是管理sentinel配置限制
- exceptionsToIgnore当配置了某个异常类，则fallback就不会起作用
- 当配置了fallback和blockHandler时,当触发的问题两个情况都满足的时候，起作用的blockHandler
- 当配置了fallback和blockHandler以及exceptionsToIgnore，当触发的问题三种情况都满足的时候，起作用的blockHandler

#### (2) 服务熔断OpenFeign

① 要开启feign对sentinel的支持

```yml
feign:  # 开启feign 对sentinel的支持
  sentinel:
    enabled: true
```

② 实现feign接口

```java
//接口
@FeignClient(value = "nacos-payment-provider",fallback = PaymentFeignServiceImpl.class)  //fallback指定处理方法
public interface PaymentFeignService {

    @GetMapping(value = "/paymentSQL/{id}")
    public CommonResult<Payment> paymentSQL(@PathVariable("id") Long id);

}
//实现类
@Component
public class PaymentFeignServiceImpl implements PaymentFeignService {

    @Override
    public CommonResult<Payment> paymentSQL(Long id) {
        return CommonResult.errorOf();  
    }
}
```



### 9 Sentinel 持久化规则

① 加入依赖

```xml
<!--sentinel持久化-->
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-datasource-nacos</artifactId>
</dependency>
```

② 配置

```yml
server:
  port: 8401

spring:
  application:
    name: cloudalibaba-sentinel-service
  cloud:
    nacos:
      discovery:
        server-addr: 39.97.188.145:8848
    sentinel:
      transport:
        dashboard: 127.0.0.1:8080 #使用sentinel检测服务
        # dashboard: 39.97.188.145:8080 #使用sentinel检测服务
        port: 8719  #指定应用与Sentinel控制台交互的端口，应用本地会起一个该端口占用的HttpServer
      datasource:  # 将sentinel 持久化到nacos中
        ds1:
          nacos:
            server-addr: ${spring.cloud.nacos.discovery.server-addr}
            dataId: ${spring.application.name} # nacos业务规则配置文件名
            groupId: DEFAULT_GROUP
            data-type: json
            rule-type: flow
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

③ nacos业务规则配置

```json
[
    {
        "resource": "/retaLimit/byUrl",  # 对应url
        "limitApp": "default",           # 对应来源
        "grade":   1,                    # 阈值类型 0:线程数,1:QPS
        "count":   1,                    # 单机阈值                  
        "strategy": 0,                   # 流控模式 0:直接 1:关联 2:链路
        "controlBehavior": 0,            # 流控效果 0:快速失败 1 warm up 2 队列
        "clusterMode": false             # 是否集群
    }
]
```

启动服务，访问接口，发现sentinel配置会显示









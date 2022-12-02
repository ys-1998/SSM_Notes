# Spring



## 前置了解

核心：

<img src="D:\work\TyporaImg\image-20221116170407871.png" alt="image-20221116170407871" style="zoom:30%;" />



七大组成

<img src="D:\work\TyporaImg\image-20221116170628438.png" alt="image-20221116170628438" style="zoom:50%;" />



## 第一步 总体了解

学习之前思考什么是spring框架（DI/IOC 和AOP容器的开源框架）

```
为了解决企业开发的繁杂配置，提出的一种解决方案。
解决了什么问题？用什么方式解决的？
1.管理对象的创建=》联想到Bean	=》联想到存放bean的容器（IOC 控制反转思想）=>联想到 对象的依赖 （DI依赖注入）
2.核心业务与边缘业务的解耦=》AOP
```



Spring原始的写法：在applicationcontex.xml中配置bean的信息，依赖关系

例子：

<img src="D:\work\TyporaImg\image-20221116165727908.png" alt="image-20221116165727908" style="zoom:50%;" />



## 第二步：了解单体结构(纯XML下)

### Bean

#### Bean的scope作用域（了解常用的两个）

```
1.singleton（单例）返回同一个对象，第一次调用会缓存起来方便后续的调用
2.prototype（多例）创建不同的对象
```

不常用的三种方式

<img src="D:\work\TyporaImg\image-20221116203236366.png" alt="image-20221116203236366" style="zoom:33%;" />



#### bean的属性注入方式DI（5种类）

**1.set方法**

```
<bean id="video" class="net.xdclass.sp.domain.Video" scope="singleton">

        <property name="id" value="9"/>
        <property name="title" value="Spring 5.X课程" />

</bean>
```

1.2复杂类型注入

​	**List-Map**

```
<bean id="video" class="net.xdclass.sp.domain.Video" >

        <!--list类型注入-->
        <property name="chapterList">
            <list>
                <value>第一章SpringBoot</value>
                <value>第二章Mybatis</value>
                <value>第三章Spring</value>
            </list>
        </property>

        <property name="videoMap">
            <map>
                <entry key="1" value="SpringCloud课程"></entry>
                <entry key="2" value="面试课程"></entry>
                <entry key="3" value="javaweb课程"></entry>
            </map>
        </property>
</bean>



public class Video {

    private int id;

    private String title;


    private List<String> chapterList;


    private Map<Integer,String> videoMap;

//省略set get方法
}
```



**2.带参构造函数**（index 、type 、name）

```
  <bean id="video" class="net.xdclass.sp.domain.Video" >

        <constructor-arg name="title" value="面试专题课程第一季"></constructor-arg>

    </bean>
```

<img src="D:\work\TyporaImg\image-20221117123637805.png" alt="image-20221117123637805" style="zoom:50%;" />



**3.POJO类型注入(property 没有使用value属性，而是使用了ref属性)**

```
<bean id="video" class="net.xdclass.sp.domain.Video" >

        <constructor-arg name="title" value="面试专题课程第一季"></constructor-arg>

    </bean>


    <bean id="videoOrder" class="net.xdclass.sp.domain.VideoOrder" >
        <property name="id" value="8" />
        <property name="outTradeNo" value="23432fnfwedwefqwef2"/>
        <property name="video" ref="video"/>
    </bean>
```



**4.c命名空间注入**

![image-20221117165245131](D:\work\TyporaImg\image-20221117165245131.png)

**5.p命名空间注入**

注意；添加约束

<img src="D:\work\TyporaImg\image-20221117165116964.png" alt="image-20221117165116964" style="zoom:50%;" />

#### Bean之间的依赖继承

1.A继承B：parent

```
bean id="video" class="net.xdclass.sp.domain.Video" scope="singleton">

        <property name="id" value="9"/>
        <property name="title" value="Spring 5.X课程" />

</bean>


<bean id="video2" class="net.xdclass.sp.domain.Video2" scope="singleton" parent="video">

        <property name="summary" value="这个是summary"></property>

</bean>
```



2.B依赖C：depends-on

```
<bean id="video" class="net.xdclass.sp.domain.Video" scope="singleton">

        <property name="id" value="9"/>
        <property name="title" value="Spring 5.X课程" />

</bean>

<!--设置两个bean的关系，video要先于videoOrder实例化-->

<bean id="videoOrder" class="net.xdclass.sp.domain.VideoOrder" depends-on="video">
        <property name="id" value="8" />
        <property name="outTradeNo" value="23432fnfwedwefqwef2"/>
        <property name="video" ref="video"/>
</bean>
```



#### Bean的生命周期（init destroy）

顺序

```
1.空构造函数
2.init方法
3.业务方法
4.destroy方法
```

![image-20221116212145585](D:\work\TyporaImg\image-20221116212145585.png)

<img src="D:\work\TyporaImg\image-20221116212152892.png" alt="image-20221116212152892" style="zoom:50%;" />



#### Bean的二次加工：后置处理器 BeanPostProcessor（消息中间件设计用）

​	

- 什么是BeanPostProcessor

  - 是Spring IOC容器给我们提供的一个扩展接口
  - 在调用初始化方法前后对 Bean 进行额外加工，ApplicationContext 会自动扫描实现了BeanPostProcessor的 bean，并注册这些 bean 为后置处理器
  - 是Bean的统一前置后置处理而不是基于某一个bean

- 执行顺序

  ```
  Spring IOC容器实例化Bean
  调用BeanPostProcessor的postProcessBeforeInitialization方法
  调用bean实例的初始化方法
  调用BeanPostProcessor的postProcessAfterInitialization方法
  ```

- 注意：接口重写的两个方法不能返回null，如果返回null那么在后续初始化方法将报空指针异常或者通过getBean()方法获取不到bean实例对象

```
public class CustomBeanPostProcessor implements BeanPostProcessor,Ordered {

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        System.out.println("CustomBeanPostProcessor1 postProcessBeforeInitialization beanName="+beanName);

        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("CustomBeanPostProcessor1 postProcessAfterInitialization beanName="+beanName);
        return bean;
    }


    public int getOrder() {
        return 1;
    }
}
```

- 可以注册多个BeanPostProcessor顺序
  - 在Spring机制中可以指定后置处理器调用顺序，通过BeanPostProcessor接口实现类实现Ordered接口getOrder方法，该方法返回整数，默认值为 0优先级最高，值越大优先级越低



#### Bean的自动装配属性:autowire（3种）

```
描述：之前我们需要显示的调用ref指定我们的依赖的对象，现在我们子需要指定autowrie的类型，它回去ioc容器中帮我们查找并注入
```

![image-20221117134912219](D:\work\TyporaImg\image-20221117134912219.png)

1.byname

2.byType

3.constructor

```
<!--<bean id="videoOrder" class="net.xdclass.sp.domain.VideoOrder" autowire="byName">-->
<!--<bean id="videoOrder" class="net.xdclass.sp.domain.VideoOrder" autowire="byType">-->
    <bean id="videoOrder" class="net.xdclass.sp.domain.VideoOrder" autowire="constructor">

        <property name="id" value="8" />
        <property name="outTradeNo" value="23432fnfwedwefqwef2"/>
    </bean>
```



#### 注解实现自动装配：@Autowired	

1.xml配置（导入约束，开启注解支持）

```
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd">


    <context:annotation-config/>
```

<img src="D:\work\TyporaImg\image-20221117152628993.png" alt="image-20221117152628993" style="zoom:50%;" />

<img src="D:\work\TyporaImg\image-20221117154748670.png" alt="image-20221117154748670" style="zoom:50%;" />





2.在类的属性上添加注解@Autowired



注意：@Autowired是通过byType方式去到ioc容器中找对象。

<img src="D:\work\TyporaImg\image-20221117160651527.png" alt="image-20221117160651527" style="zoom: 50%;" />

​			@Autowired可以用在属性上也可以用在set方法上

​			使用@Autowired后可以省略掉set方法



### IOC容器

#### ioc思想

实现不同操作只需要通过修改xml文件，对象由Spring来创建，管理，装配



### AOP

#### 代理模式（设计思想 理解aop前提）



##### **静态代理**

好处

```
1.使得真实角色的操作更加纯粹，不再去关注公共业务
2.公共业务交给代理角色，实现业务的分工
3.公共业务发生拓展的时候，方便集中管理
```

缺点（动态代理解决）

```
一个真实角色就会产生一个代理角色，代码量翻倍，开发效率变低
```



代码实现步骤

```
1.接口
2.真实角色
3.代理角色
4.客户端访问代理角色
```



例子：（静态代理）

```
1.userService 接口 里面有四个方法 add update delete query
2.userServiceImpl 实现接口，并实现四个方法
3.业务需要新增手机日志的功能，为了不改变原有的代码我们新增userServiceProxy代理类
	代理类也实现接口，实现四个方法，只是在方法中调用直接，对象userServiceImpl的方法，并加上自己的搜集日	 志的方法。
4.用户端调用时，将直接对象传入代理对象调用代理对象中的方法。
```



##### **动态代理**

```
动态代理是动态生成的，不是我们直接写好的
分类：基于接口的jdk动态代理，基于类的（Cglib）
```

了解两个类：invocationHandler(调用处理类)、Proxy

```
invocationHandler：调用处理程序并返回结果
proxy:生成动态代理实例，提供了创建动态代理类和实例的静态方法
```

```
invocationHandler内部结构
1.设置要代理的类
2.生成代理类
3.处理被代理类的方法
```



好处：

```
只要是实现的统一个接口，那么我们的动态代理代码就可以复用，不再像静态代理一样，需要为每一个新的被代理类创建代理类。
```



jdk(代理类与目标类实现了相同的接口)

Proxy类的静态方法获得代理类

```
public class UserServiceProxy implements InvocationHandler {

    //1.设置需要代理的类
    private Object target;

    public void setTarget(Object target) {
        this.target = target;
    }

    //2.创建代理类实例
    public Object getProxy(){
    //类加载器：将我们生成的代理类加入到虚拟机中
    //获取接口：保证代理类和被代理类有相同api,保证方法调用的时的准确性
    //InvocationHandler 接口：通过handler 的invoke 方法（我们自己写的）保证代理类准确的调用方法。（代理类是自动实现的，代理类是不知道自己的方法调用的是被代理类的那个方法）
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }

    //3.处理代理类的方法，核心业务
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    //proxy：代理对象
    //method:记录使用者调用的方法，未来可以基于这和方法调用目标方法
    //args:调用方法时传递的参数
        log(method.getName());
        method.invoke(target,args);
        return null;
    }

    //自己的公共业务代码
    public void log(String msg){
        System.out.println("执行了"+msg+"方法");
    }
}

```



Cglib(代理了继承了目标类)

Enhancer获得代理方法

```
public class CglibProxy implements MethodInterceptor {

    private Object target;

    public void setTarget(Object target) {
        this.target = target;
    }

    public Object getproxy(){

        Enhancer enhancer=new Enhancer();
        enhancer.setSuperclass(this.target.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object target, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

        System.out.println("cg方法执行前************");
        methodProxy.invokeSuper(target,args);
        System.out.println("cg方法执行后************");
        return null;
    }
    
}
```





#### Spring实现Aop

##### 方式一：Spring的Api接口实现

```
流程：
1.创建公共业务类Log实现MethodBeforeAdvice（这个只是其中一个接口），编写业务代码
2.XML配置切入点，通知
```

![image-20221117230331525](D:\work\TyporaImg\image-20221117230331525.png)

```
 <!--aop-->
    <bean id="log" class="yspan.work.proxy.Log"/>
    <bean id="userService" class="yspan.work.service.UserServiceImpl"/>


    <aop:config>
        <!--切入点：在什么地方-->
        <!--execution(修饰词 返回值 类名 方法名 参数)-->
        <aop:pointcut id="pointcut" expression="execution(* yspan.work.service.UserServiceImpl.*(..))"/>

        <!--通知：要做什么-->
        <aop:advisor advice-ref="log" pointcut-ref="pointcut"/>
    </aop:config>
```

```
public class Log implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println(target.getClass().getName()+"的"+method.getName()+"被执行了");
    }
}
```



```
切面：模块化的横切关注点的一个类
通知：需要完成的工作。类中的一个方法
```



##### 方式二：自定义类来实现AOP

```
1.创建切面类
2.xml配置切面 切入点 通知
```

```
<!--aop 自定义类-->
    <bean id="diyPointCut" class="yspan.work.proxy.DiyPointCut"/>

    <aop:config>
        <aop:aspect ref="diyPointCut">
            <aop:pointcut id="pointcut" expression="execution(* yspan.work.service.UserServiceImpl.*(..))"/>
            <aop:before method="before" pointcut-ref="pointcut"/>
            <aop:after method="after" pointcut-ref="pointcut"/>
        </aop:aspect>
    </aop:config>
```



##### 方式三：注解实现

默认jdk

```
流程：
1.xml开启注解支持
2.切面类添加注解@aspect
	方法添加注解@before：方法前
			  @after：方法后
			  @around：环绕通知

```

![image-20221118150140574](D:\work\TyporaImg\image-20221118150140574.png)

<img src="D:\work\TyporaImg\image-20221118150521873.png" alt="image-20221118150521873" style="zoom:50%;" />

执行的顺序

```
环绕前
方法执行前
方法执行
环绕后
方法执行后
```







## 第三步：了解注解方式下spring的使用



xml结合注解方式（简化注入）

1.如何加载bean（使用xml情况下）

```
1.xml开启注解扫描
2.需要添加的bean，使用@component或它的细分@service @controller @repository
3.依赖通过@autowired注入
4.获取bean：ClassPathXmlApplicationContext
```



Java的方式配置Spring

2.如何加载bean（不使用xml情况下）

```
1.添加配置类并添加注解@configuration（代表这是一个配置类，和我们之前编写的xml相似）
2.需要添加的bean，使用@component或它的细分@service @controller @repository
3.获取bean：annotationConfigApplicationContext
```

<img src="D:\work\TyporaImg\image-20221117163357210.png" alt="image-20221117163357210" style="zoom:50%;" />

![image-20221117163509349](D:\work\TyporaImg\image-20221117163509349.png)





# Mybatis



## 原生jdbc连接

依赖

<img src="https://ys-typora.oss-cn-chengdu.aliyuncs.com/img/image-20221125143131835.png" alt="image-20221125143131835" style="zoom:50%;" />

例子

![image-20221123143523775](D:\work\TyporaImg\image-20221123143523775.png)



## 什么是ORM框架

对数据库中的表和POJO java对象做映射的框架



## Mybatis核心流程

图解

<img src="D:\work\TyporaImg\image-20221123145516835.png" alt="image-20221123145516835" style="zoom:50%;" />





## 项目搭建引入

1.导入依赖（纯mavn环境）

```
 <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.11</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.47</version>
        </dependency>
```

2.编写mybatis的配置文件

```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/xdclass?useSSL=false&amp;useUnicode=true&amp;characterEncoding=utf8"/>
                <property name="username" value="root"/>
                <property name="password" value="885200"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <mapper resource="mapper/videoMapper.xml"/>
    </mappers>
</configuration>
```

3.编写mybatis的映射文件

```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="work.yspan.mapper.VideoMapper">

    <select id="selectById" resultType="work.yspan.domain.Video">
        select * from video where id=#{video_id};
    </select>

</mapper>
```



4.编写获取sqlsession的配置类（非必要）

```
public class Mybatis_utils {


    private static SqlSessionFactory sqlSessionFactory;

   static  {
        try {
            String resource="config/mybatis_config.xml";
            InputStream in = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static SqlSession getsqlSession(){

       return sqlSessionFactory.openSession();
    }

}
```



5.编写实体类

```

```



6.编写mapper（注意多参数 编写别名映射）

```
public interface VideoMapper {

    Video selectById(@Param("video_id") int videoId);
}

```



## 控制台日志输出

1.添加依赖

```
<dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
            <version>1.7.30</version>
        </dependency>
```

2.添加配置文件

```
log4j.rootLogger=ERROR,stdout
log4j.logger.work.yspan=DEBUG//work.yspan为你的包名可变，DEBUG为日志级别可变为TRACE
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%5p [%t] - %m%n
```



![image-20221124165818812](D:\work\TyporaImg\image-20221124165818812.png)



## 参数类型，字段类型

parameterType 参数类型

- 可以是基本类型

  ```
  parameterType="java.lang.Long"
  parameterType="java.lang.String"
  ```

- 可以是Java集合List或者Map

  ```
  parameterType="java.util.Map"
  parameterType="java.util.List"
  ```

- 可以是Java自定义对象

  ```
  parameterType="net.xdclass.online_class.domain.Video"
  ```

jdbcType  具体某个字段的类型，从java类型映射到数据库类型

- 常见的数据库类型和java列席对比

```
JDBC Type           Java Type 

CHAR                String 
VARCHAR             String 
LONGVARCHAR         String 
NUMERIC             java.math.BigDecimal 
DECIMAL             java.math.BigDecimal 
BIT                 boolean 
BOOLEAN             boolean 
TINYINT             byte 
SMALLINT            short 
INTEGER             INTEGER 
INTEGER       int
BIGINT              long 
REAL                float 
FLOAT               double 
DOUBLE              double 
BINARY              byte[] 
VARBINARY           byte[] 
LONGVARBINARY       byte[] 
DATE                java.sql.Date 
TIME                java.sql.Time 
TIMESTAMP           java.sql.Timestamp 
CLOB                Clob 
BLOB                Blob 
ARRAY               Array 
DISTINCT            mapping of underlying type 
STRUCT              Struct 
REF                 Ref 
DATALINK            java.net.URL
```



图例

![image-20221124183435333](D:\work\TyporaImg\image-20221124183435333.png)



## 增删改查

### 插入

- 插入（并获取自增主键）

开启：useGeneratedKeys="true" keyProperty="id" keyColumn="id"

```
 <insert id="add" parameterType="work.yspan.domain.Video" useGeneratedKeys="true" keyProperty="id" keyColumn="id">
        insert into video (title,summary,cover_img,view_num,price,create_time,online,point)
        values(#{title,jdbcType=VARCHAR},#{summary,jdbcType=VARCHAR},#{coverImg,jdbcType=VARCHAR},
        #{viewNum,jdbcType=INTEGER},#{price,jdbcType=INTEGER},#{createTime,jdbcType=TIMESTAMP},#{online,jdbcType=INTEGER},#{point,jdbcType=DOUBLE})
    </insert>
```

- 批量插入foreach，在values后面环绕语句

```
<insert id="addBatch" parameterType="work.yspan.domain.Video" useGeneratedKeys="true" keyProperty="id" keyColumn="id">
        insert into video (title,summary,cover_img,view_num,price,create_time,online,point)
        values
        <foreach collection="list" item="video" separator=",">
            (#{video.title,jdbcType=VARCHAR},#{video.summary,jdbcType=VARCHAR},#{video.coverImg,jdbcType=VARCHAR},
            #{video.viewNum,jdbcType=INTEGER},#{video.price,jdbcType=INTEGER},#{video.createTime,jdbcType=TIMESTAMP},#{video.online,jdbcType=INTEGER},#{video.point,jdbcType=DOUBLE})
        </foreach>
    </insert>
```

- foreach: 用于循环拼接的内置标签，常用于 批量新增、in查询等常见

  ```
  包含以下属性：
    collection：必填，值为要迭代循环的集合类型，情况有多种
      入参是List类型的时候，collection属性值为list
      入参是Map类型的时候，collection 属性值为map的key值
    
      item：每一个元素进行迭代时的别名
      index：索引的属性名，在集合数组情况下值为当前索引值，当迭代对象是map时，这个值是map的key
      open：整个循环内容的开头字符串
      close：整个循环内容的结尾字符串
      separator: 每次循环的分隔符
  ```



### 更新

指定某个字段更新

trim函数：去除或添加 语句中的某一部分的字符

根据if函数里面条件判断，是否更新

```
<update id="updateSelective" parameterType="work.yspan.domain.Video" >
        update video
        <trim prefix="set" suffixOverrides=",">
            <if test="title != null">title=#{title,jdbcType=VARCHAR},</if>
            <if test="createTime == null">create_time= now() ,</if>
        </trim>
        where id=#{videoId,jdbcType=INTEGER}
    </update>
```



### 删除

特殊字符转义：<![CDATA[ 特殊字符 ]]>

```
 <delete id="deleteByPointAndPrice" parameterType="java.util.Map">
        delete from video where point <![CDATA[ <= ]]> #{point,jdbcType=DOUBLE} and price=#{price,jdbcType=INTEGER}
    </delete>
```



### 查询

#### 别名

在mybatis的配置文件中配置

```
1.一个个配置
2.包扫描

	<typeAliases>
        <typeAlias type="work.yspan.domain.Video" alias="Video"/>
        <package name="work.yspan.domain"/>
    </typeAliases>
```

图例

![image-20221125151914743](https://ys-typora.oss-cn-chengdu.aliyuncs.com/img/image-20221125151914743.png)

#### 自定义sql片段

```
 <sql id="base_video_field">
        id,title,summary,price,point
    </sql>
    <select id="selectById" resultType="Video">
        select <include refid="base_video_field"/> from video where id=#{video_id};
    </select>
```

语句图例

![image-20221125151817184](https://ys-typora.oss-cn-chengdu.aliyuncs.com/img/image-20221125151817184.png)

#### 结果集映射

resultmap

```
 <resultMap id="VideoResultMap" type="Video">
        <id column="id" property="videoId" jdbcType="INTEGER"/>
        <result column="title" property="title" jdbcType="VARCHAR"/>
        <result column="summary" property="summary" jdbcType="VARCHAR"/>
        <result column="price" property="price" jdbcType="DOUBLE"/>
        <result column="cover_img" property="coverImg" jdbcType="VARCHAR"/>
    </resultMap>
    
    
    <select id="selectByIdWithReslutMap" resultMap="VideoResultMap">
        select id,title,summary,price,cover_img from video where id=#{video_id,jdbcType=INTEGER}
    </select>
```

图例

![image-20221125153422913](https://ys-typora.oss-cn-chengdu.aliyuncs.com/img/image-20221125153422913.png)



#### 一对一查询结果映射

association：将结果集映射到POJO的某个复杂类型属性

```
说明：订单order对象里面包含 user对象
查询videoOrder信息,并将关联的用户信息存储到，user属性中。
1.我们的查询语句通过left join 关联两张表，
2.通过resultMap自定义关系映射
3.通过association，将子属性的数据映射到子属性对象中
```

```
//videoOrderDO中含有UserDO user属性
public class VideoOrderDO implements Serializable {
  
    private String outTradeNo;
   
    private Integer state;

    private Date createTime;

    private Date notifyTime;

    private Integer totalFee;

    private UserDO user;
```

数据库中video_order有user_id，能后与user表的id关联起来

![image-20221125165639874](https://ys-typora.oss-cn-chengdu.aliyuncs.com/img/image-20221125165639874.png)



```
<resultMap id="VideoOrderResultMap" type="VideoOrderDO">
        <id column="id" property="id" jdbcType="INTEGER"/>
        <result column="user_id" property="userId"/>
        <result column="out_trade_no" property="outTradeNo"/>
        <result column="create_time" property="createTime"/>
        <result column="state" property="state"/>
        <result column="total_fee" property="totalFee"/>
        <result column="video_id" property="videoId"/>
        <result column="video_title" property="videoTitle"/>
        <result column="video_img" property="videoImg"/>
        
        <association property="user" javaType="UserDO">
            <id property="id"  column="user_id"/>
            <result property="name" column="name"/>
            <result property="headImg" column="head_img"/>
            <result property="createTime" column="create_time"/>
            <result property="phone" column="phone"/>
        </association>

    </resultMap>
    <select id="queryVideoOrderList" resultMap="VideoOrderResultMap">
        select
        o.id id,
        o.user_id ,
        o.out_trade_no,
        o.create_time,
        o.state,
        o.total_fee,
        o.video_id,
        o.video_title,
        o.video_img,
        u.name,
        u.head_img,
        u.create_time,
        u.phone
        from video_order o left join user u on o.user_id=u.id
    </select>
```

查询结果图示

![image-20221125165057525](https://ys-typora.oss-cn-chengdu.aliyuncs.com/img/image-20221125165057525.png)



#### 一对多查询结果映射

collection



## 缓存

### 一级缓存

作用域：sqlsession,同一个SqlSession中执行相同的SQL查询(相同的SQL和参数)，第一次会去查询数据库并写在缓存中，第二次会直接从缓存中取。

开启方式：默认开启

失效：

```
当执行SQL时候两次查询中间发生了增删改的操作，即insert、update、delete等操作commit后会清空该SQLSession缓存; 
比如sqlsession关闭，或者清空等
```



### 二级缓存

作用域：namespace级别，即某一个mapper.class，多个SqlSession去操作同一个namespace下的Mapper的sql语句，多个SqlSession可以共用二级缓存,如果两个mapper的namespace相同，（即使是两个mapper，那么这两个mapper中执行sql查询到的数据也将存在相同的二级缓存区域中，但是最后是每个Mapper单独的名称空间）

开启方式：默认没有开启

```
1.全局开启，去到Mabatis的配置文件的setting中设置
<settings>
<!--这个配置使全局的映射器(二级缓存)启用或禁用缓存，全局总开关，这里关闭，mapper中开启了也没用-->
        <setting name="cacheEnabled" value="true" />
</settings>

2.某个mapper的二级缓存，去到Mybatis的映射文件xxxMapper.xml中
<cache eviction="LRU" flushInterval="100000" readOnly="true" size="1024"/>

参数含义：
		eviction:代表的是缓存回收策略，常见下面两种。
        (1) LRU,最近最少使用的，一处最长时间不用的对象
        (2) FIFO,先进先出，按对象进入缓存的顺序来移除他们

        flushInterval:刷新间隔时间，单位为毫秒，这里配置的是100秒刷新，如果不配置它，当SQL被执行的时候才会去刷新缓存。

        size:引用数目，代表缓存最多可以存储多少个对象，设置过大会导致内存溢出

        readOnly:只读，缓存数据只能读取而不能修改，默认值是false
```

失效：

```
执行同个namespace下的mapepr映射文件中增删改sql，并执行了commit操作,会清空该二级缓存
```

**注意点**

- 实现二级缓存的时候，MyBatis建议返回的POJO是可序列化的， 也就是建议实现Serializable接口

- 缓存淘汰策略：会使用默认的 LRU 算法来收回（最近最少使用的）



图示：

1.全局

![image-20221125184838154](https://ys-typora.oss-cn-chengdu.aliyuncs.com/img/image-20221125184838154.png)

2.局部

![image-20221125184656163](https://ys-typora.oss-cn-chengdu.aliyuncs.com/img/image-20221125184656163.png)



## 懒加载

1.开启mybatis配置文件settings开启懒加载

```
<!--延迟加载总开关-->
    <setting name="lazyLoadingEnabled" value="true"/>
    <!--将aggressiveLazyLoading设置为false表示按需加载，默认为true-->
    <setting name="aggressiveLazyLoading" value="false"/>
```

2.例子展示

注意:当我们的代码涉及到懒加载数据时，就会触发懒加载。

例如：videoOrder中有user，当我们的代码只围绕着videoorder的逻辑（查询的信息不涉及user属性），既不会触发懒加载

### association

```

```

### collection

```

```





## 事务管理

1.mybatis配置文件environment中配置 JDBC或MANAGED（交给其他程序管理，例如Spring、JBOSS) 

```
<environment id="development">
            <!-- mybatis使用jdbc事务管理方式 -->
            <transactionManager type="JDBC"/>
            <transactionManager type="MANAGED"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://127.0.0.1:3306/xdclass?useUnicode=true&amp;characterEncoding=utf-8&amp;useSSL=false"/>
                <property name="username" value="root"/>
                <property name="password" value="xdclass.net"/>
            </dataSource>
</environment>
```



2.当我们设置为MANAGED，不开启自动提交，程序出错，sql语句依旧可以插入成功



# SpringMVC



## MVC架构(各个模块的作用)

```
M：
V：
C:
```



## 第一步回顾基础

### Servlet

创建项目例子演示

1.导入依赖

```

```

<img src="D:\work\TyporaImg\image-20221119003017772.png" alt="image-20221119003017772" style="zoom:50%;" />

2.

<img src="D:\work\TyporaImg\image-20221119003249687.png" alt="image-20221119003249687" style="zoom:50%;" />





3.编写逻辑流程

创建servlet

![image-20221119003719863](D:\work\TyporaImg\image-20221119003719863.png)

2.配置servlet

![image-20221119004019776](D:\work\TyporaImg\image-20221119004019776.png)

3.请求页面编写







![image-20221119004353778](D:\work\TyporaImg\image-20221119004353778.png)





### SpringMVC执行原理

dispatcherServlet

![image-20221119120841757](D:\work\TyporaImg\image-20221119120841757.png)



图解含义：

```
第一块
handlermapping(处理映射器):根据url查找Handler处理器.
HandlerExecution(表示具体的处理器):根据url查找控制器，如上url被查找控制器为：hello。然后将解析后的消息传递给DispatchServlet,如解析控制器映射等。

第二块
HandlerAdapter(处理适配器)：按照特定的规则执行Handler,Handler让具体的Controller去执行
（我的理解是DispatchServlet将信息传递给HandlerAdapter，HandlerAdapter根据信息内容去实现了controller的所有类中查找我们想要的那个controller,找到之后让Handler执行Controller，controller将执行的信息返回给handleradapter（例如ModelAndView），然后handleradapter将相关消息传递回dispatcherservlet（例如视图逻辑名和模型））

第三块
ViewResolver(视图解析器)：DispatchServlet将收到的消息（逻辑视图名），调用视图解析器（ViewResolver）来解析，解析完后传递给HandlerAdapter，HandlerAdapter再调用具体的视图，将视图呈现给用户。
（我的理解：根据代码字面上看更像是定位视图的位置，将传递回来的值拼接得到目标视图路径，HandlerAdapter再将返回的模型中的数据传入到视图中，然后将视图呈现给用户）

```

![image-20221120001230664](D:\work\TyporaImg\image-20221120001230664.png)



博客：流程

https://blog.csdn.net/weixin_53353693/article/details/124057001

```
1. 用户通过浏览器发起 HttpRequest 请求到前端控制器 (DispatcherServlet)。

2. DispatcherServlet 将用户请求发送给处理器映射器 (HandlerMapping)。

3. 处理器映射器 (HandlerMapping)会根据请求，找到负责处理该请求的处理器，并将其封装为处理器执行链 返回 (HandlerExecutionChain) 给 DispatcherServlet

4. DispatcherServlet 会根据 处理器执行链 中的处理器，找到能够执行该处理器的处理器适配器(HandlerAdaptor)    --注，处理器适配器有多个

5. 处理器适配器 (HandlerAdaptoer) 会调用对应的具体的 Controller

6. Controller 将处理结果及要跳转的视图封装到一个对象 ModelAndView 中并将其返回给处理器适配器 (HandlerAdaptor)

7. HandlerAdaptor 直接将 ModelAndView 交给 DispatcherServlet ，至此，业务处理完毕

8. 业务处理完毕后，我们需要将处理结果展示给用户。于是DisptcherServlet 调用 ViewResolver，将 ModelAndView 中的视图名称封装为视图对象

9. ViewResolver 将封装好的视图 (View) 对象返回给 DIspatcherServlet

10. DispatcherServlet 调用视图对象，让其自己 (View) 进行渲染（将模型数据填充至视图中），形成响应对象 (HttpResponse)

11. 前端控制器 (DispatcherServlet) 响应 (HttpResponse) 给浏览器，展示在页面上。

```



### 原生开发SpringMVC



```
1.web.xml配置DispatchServlet，DispatchServlet绑定Spring的配置文件，设置启动级别为1和服务器一起启动
2.创建SPring的配置文件，（处理器映射器，处理器适配器，视图解析器）
3.编写controller返回ModelAndView（业务代码、视图跳转）
```

图解：

第一步

第二步

![image-20221119123339485](D:\work\TyporaImg\image-20221119123339485.png)

第三步

![image-20221119123055597](D:\work\TyporaImg\image-20221119123055597.png)



### 注解开发SpringMVC

流程

```

```

![image-20221120000811783](D:\work\TyporaImg\image-20221120000811783.png)



![image-20221120001014729](D:\work\TyporaImg\image-20221120001014729.png)

1.web.xml

2.springmvc.xml

现在的方式：因为要使用注解就会需要开启组件扫描，保证spring能找到我们编写的控制器

![image-20221120143013064](D:\work\TyporaImg\image-20221120143013064.png)

3.

**原来的方式**：编写controller类实现controller接口，编写完成后去到spring的配置文件中注册请求的bean

**现在的方式**：类添加注解@controller，不用再去spring的配置文件添加bean。方法添加路径映射。

​						（被@controller注解的类，如果返回值是string，并且有具体的页面可以跳转，那么就会被视图解析器解析）

**之前的例子**：

![image-20221120143308851](D:\work\TyporaImg\image-20221120143308851.png)

**现在的例子：**

<img src="D:\work\TyporaImg\image-20221120144401690.png" alt="image-20221120144401690" style="zoom:50%;" />

4.编写jsp页面

<img src="D:\work\TyporaImg\image-20221120143400860.png" alt="image-20221120143400860" style="zoom:50%;" />



## 第二步前端参数处理

### 数据显示到前端

#### 三种方式

**1.ModelAndView**

![image-20221120164603663](D:\work\TyporaImg\image-20221120164603663.png)

**2.Model**

![image-20221120164617445](D:\work\TyporaImg\image-20221120164617445.png)

**3.ModelMap**

![image-20221120164634428](D:\work\TyporaImg\image-20221120164634428.png)



#### **对比**（简单的理解）

![image-20221120164931926](D:\work\TyporaImg\image-20221120164931926.png)



#### 数据乱码解决，（xml配置乱码过滤器）

![image-20221120165729127](D:\work\TyporaImg\image-20221120165729127.png)





### Controller参数接受

1.单个参数：

2.对象参数

<img src="D:\work\TyporaImg\image-20221120154504984.png" alt="image-20221120154504984" style="zoom:50%;" />

注意：前端传递的参数如果与对象名不一致，传递的数据会为null。





# SSM整合

## 整合Mybatis

```
1.创建编写配置文件mybatis-config.xml（别名，mapper）
2.编写数据库配置信息文件database.properties（连接的数据库和账号密码）
3.创建实体类（pojo）
4.创建xxxxmapper接口,里面编写业务的方法，
5.创建xxxxmapper.xml编写具体实现
6.将xxxxmapper接口注册到mybatis-config.xml文件中
```

图解

1.mybatis配置文件：mybatis-config.xml

<img src="D:\work\TyporaImg\image-20221120172035633.png" alt="image-20221120172035633" style="zoom:50%;" />

2.数据源信息配置：database.properties

![image-20221120171333683](D:\work\TyporaImg\image-20221120171333683.png)

3.创建实体类

<img src="D:\work\TyporaImg\image-20221120172154588.png" alt="image-20221120172154588" style="zoom:50%;" />

4.创建xxxxmapper接口,里面编写业务的方法，

5.创建xxxxmapper.xml编写具体实现

<img src="D:\work\TyporaImg\image-20221120171627688.png" alt="image-20221120171627688" style="zoom:50%;" />****

6.将xxxxmapper.xml注册到mybatis-config.xml文件中

<img src="D:\work\TyporaImg\image-20221120172044138.png" alt="image-20221120172044138" style="zoom:50%;" />



## 整合Sping（用于整合dao，service层）

```
dao层
1.关联数据库配置文件
2.配置数据库连接池
3.配置sqlsessionfactory
4.配置dao包自动扫描

service层
1.扫描service下的包
2.将所有的业务类注入到spring（可以用配置也可以用注解实现）
3.声明式事务配置
```



### dao层

1.<img src="D:\work\TyporaImg\image-20221120173253152.png" alt="image-20221120173253152" style="zoom:50%;" />

2.配置数据库连接池（可以自己选择连接池类型）

<img src="D:\work\TyporaImg\image-20221120173321531.png" alt="image-20221120173321531" style="zoom:50%;" />



3.配置sqlsessionfactory![image-20221120173349755](D:\work\TyporaImg\image-20221120173349755.png)



4.配置dao包自动扫描

![image-20221120230903958](D:\work\TyporaImg\image-20221120230903958.png)

### service层

```

```

![image-20221120225811179](D:\work\TyporaImg\image-20221120225811179.png)



## 整合springmvc



# SpringBoot

约定大于配置，默认帮我们进行了很多设置

## 自动装配原理（重点）



### pom.xml

### 启动器



### 主程序

#### 1.@springbootconfiguration

```
点击进去后是@configuration注解，代表这是个配置类
```

#### 2.@componentscan

```
指定扫描那些包
```



#### 3.@EnableAutoConfiguration（重点）：自动导入包

两个重点包

@AutoConfigurationPackage

@Import(AutoConfigurationImportSelector.class)



##### @AutoConfigurationPackage

```
1.点击进去发现
@AutoConfigurationPackage（将指定的一个包下的所有组件导入进来，即MainApplication程序所在的包）
@Import(AutoConfigurationImportSelector.class)//给容器导入一个组件
public @interface EnableAutoConfiguration {

2.点击@AutoConfigurationPackage发现
@Import(AutoConfigurationPackages.Registrar.class)
public @interface AutoConfigurationPackage {

3.点击@Import(AutoConfigurationPackages.Registrar.class)中的register
/**
*存储来自导入配置的基础包
*/
static class Registrar implements ImportBeanDefinitionRegistrar, DeterminableImports {

		@Override
		public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
			register(registry, new PackageImports(metadata).getPackageNames().toArray(new String[0]));//使用Registrar给容器批量注册组件（将元注解信息修饰的类所在包下的所有组件批量注册进去）
			//这里参数AnnotationMetadata metadata所修饰的类为我们的启动类，那我们所取得的包名及为启动类所在包
		}

		@Override
		public Set<Object> determineImports(AnnotationMetadata metadata) {
			return Collections.singleton(new PackageImports(metadata));
		}

	}
```



##### @Import(AutoConfigurationImportSelector.class)

```
1.点击AutoConfigurationImportSelector.class发现selectImports方法，用于选择导入那些包

@Override
	public String[] selectImports(AnnotationMetadata annotationMetadata) {
		if (!isEnabled(annotationMetadata)) {
			return NO_IMPORTS;
		}
		AutoConfigurationEntry autoConfigurationEntry = getAutoConfigurationEntry(annotationMetadata);
		return StringUtils.toStringArray(autoConfigurationEntry.getConfigurations());
	}
	
2.重点getAutoConfigurationEntry(annotationMetadata)方法（获取所有自动配置的集合）：利用这个方法给容器。

protected AutoConfigurationEntry getAutoConfigurationEntry(AnnotationMetadata annotationMetadata) {
		if (!isEnabled(annotationMetadata)) {
			return EMPTY_ENTRY;
		}
		AnnotationAttributes attributes = getAttributes(annotationMetadata);
		List<String> configurations = getCandidateConfigurations(annotationMetadata, attributes);//获取到所有的需要导入到容器的配置类
		//后面的操作都是再进行配置的筛选操作
		configurations = removeDuplicates(configurations);
		Set<String> exclusions = getExclusions(annotationMetadata, attributes);
		checkExcludedClasses(configurations, exclusions);
		configurations.removeAll(exclusions);
		configurations = getConfigurationClassFilter().filter(configurations);
		fireAutoConfigurationImportEvents(configurations, exclusions);
		return new AutoConfigurationEntry(configurations, exclusions);
	}
	
3.点击 getCandidateConfigurations(annotationMetadata, attributes)方法，

protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes) {
		List<String> configurations = SpringFactoriesLoader.loadFactoryNames(getSpringFactoriesLoaderFactoryClass(),
				getBeanClassLoader());//利用spring工厂加载
				
		Assert.notEmpty(configurations, "No auto configuration classes found in META-INF/spring.factories. If you "
				+ "are using a custom packaging, make sure that file is correct.");
		return configurations;
	}

4.点击loadFactoryNames(getSpringFactoriesLoaderFactoryClass(),getBeanClassLoader())方法，

 public static List<String> loadFactoryNames(Class<?> factoryType, @Nullable ClassLoader classLoader) {
        String factoryTypeName = factoryType.getName();
        return (List)loadSpringFactories(classLoader).getOrDefault(factoryTypeName, Collections.emptyList());
    }
5.点击(List)loadSpringFactories(classLoader)跳转到下面方法，（工厂加载在这个方法得到所有组件）
private static Map<String, List<String>> loadSpringFactories(@Nullable ClassLoader classLoader) {
        MultiValueMap<String, String> result = (MultiValueMap)cache.get(classLoader);
        if (result != null) {
            return result;
        } else {
        
   注意这个包下的一个方法 ：从"META-INF/spring.factories"的位置来加载文件
   默认是扫描我们系统里面所有"META-INF/spring.factories"位置的文件
   spring-boot-autoconfigure-2.3.3.RELEASE.jar包中也有META-INF/spring.factories（包含所有127个自动配置类），文件里面写死了spring-boot一启动就要加载的配置类。
   
```

![image-20221121173211438](D:\work\TyporaImg\image-20221121173211438.png)

示例：

![image-20221121173838879](D:\work\TyporaImg\image-20221121173838879.png)



按需开启自动配置项

```
1.虽然我们127个场景的所有自动配置启动的时候默认全部加载
2.按照条件装配原则（@Conditional），最终会按需配置
```



Springboot默认会在底层配好所有的组件，但是如果用户自己配置了以用户的优先

```
@Bean
	@ConditionalOnMissingBean//用户没有配置则使用这个
	public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
		return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
	}
```

#### 总结：

- springboot先加载所有的自动配置类 xxxxAutoConfiguration
- 每个自动配置类按照条件进行生效，默认都会绑定配置文件指定的值。xxxxProperties.class里面拿。xxxxProperties.class又和配置文件进行了绑定
- 生效的配置类就会给容器中装配很多组件
- 只有容器中有这些组件，相当于这些功能就有了
- 只要用户有自己的配置，就以用户的优先
- 定制化配置
  - 用户直接自己@Bean替换底层的组件
  - 用户去看这个组件时获取的配置文件什么值就去修改

xxxxAutoConfiguration --->添加一系列的组件 ------>xxxxProperties 里面拿值  ----->application.properties



![image-20221121233225276](D:\work\TyporaImg\image-20221121233225276.png)





## 拦截器

### 1.配置类

### （实现WebMvcConfigurer）

简单的例子

```
@Configuration
public class requestHandlerConfig implements WebMvcConfigurer {

    //添加拦截路径
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        InterceptorRegistration interceptorRegistration = registry.addInterceptor(new requestIntercepter());

        interceptorRegistration.addPathPatterns("/**");
    }
}
```

### 2.拦截方法

### （实现HandlerInterceptor）

```
public class requestIntercepter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println("执行了拦截器方法");
        return true;
    }
}
```



和Filter过滤器的区别

```
Filter和Interceptor二者都是AOP编程思想的体现，功能基本都可以实现

拦截器功能更强大些，Filter能做的事情它都能做

Filter在只在Servlet前后起作用，而Interceptor够深入到方法前后、异常抛出前后等

filter依赖于Servlet容器即web应用中，而Interceptor不依赖于Servlet容器所以可以运行在多种环境。

在接口调用的生命周期里，Interceptor可以被多次调用，而Filter只能在容器初始化时调用一次。
	
Filter和Interceptor的执行顺序
 	
过滤前->拦截前->action执行->拦截后->过滤后
```



## 过滤器

1.启动类添加@ServletComponentScan注解

2.创建类，实现Filter接口，添加@WebFilter(urlPatterns = "拦截路径",filterName = "过滤器名")注解

3.重写doFilter方法

```
@WebFilter(urlPatterns = "/user/login/*",filterName = "loginfilter")
public class loginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest=(HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse=(HttpServletResponse) servletResponse;
        System.out.println("do===========loginfilter==============");
        String name = httpServletRequest.getParameter("name");

        if (name.equalsIgnoreCase("小周")){
            filterChain.doFilter(servletRequest,servletResponse);
        }else {
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setHeader("Content-Type","text/html;charset=utf-8");
            PrintWriter writer = httpServletResponse.getWriter();
            writer.write(name);
        }

    }
}
```





## 全局异常处理

1.异常处理类

类添加@RestControllerAdvice

方法添加@ExceptionHandler(xxxxException.class)



```
@RestControllerAdvice
public class LoginExceptionHandler {

    @ExceptionHandler(Exception.class)
    ModelAndView loginhandler(Exception e){

//        String response="全局异常:code=-2";
//        System.out.println("全局异常:code"+"-2");

//异常界面跳转
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.addObject("msg",500);
        modelAndView.setViewName("error.html");
        return modelAndView;
    }
}
```



## 异步任务



1.启动类添加注解@EnableAsync

2.创建类添加注解@Component @Async（添加类：类的suo）

例子

```
@Component
@Async
public class AsyncTask {

    public void task1(){
        try {
            Thread.sleep(4000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("task 1");

    }
    public void task2(){
        try {
            Thread.sleep(4000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("task 2");

    }

    public Future<String> task3(){
        try {
            Thread.sleep(4000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("task 3");
        return new AsyncResult<String>("task3");
    }

    public Future<String> task4(){
        try {
            Thread.sleep(4000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("task 4");
        return new AsyncResult<String>("task4");
    }

}

```



## 定时任务



1.启动类添加@EnableScheduling注解

2.创建定时任务类田间@Component注解，方法添加@Scheduled(cron = "*/5  *  *  *  *  *")注解。

​	参数设置任务计划的时间。

​	

参数选项

- cron 定时任务表达式 @Scheduled(cron="*/1 * * * * *") 表示每秒
  - crontab 工具 https://tool.lu/crontab/
- fixedRate: 定时多久执行一次（上一次开始执行时间点后xx秒再次执行；）
- fixedDelay: 上一次执行结束时间点后xx秒再次执行



例子：

```
@Component
public class ScheduleDemo {
    @Scheduled(cron = "*/5  *  *  *  *  *")
    public void gettime(){

        String time1=LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss"));
        System.out.println(LocalDateTime.now().toString());
        System.out.println("当前时间为："+time1);
    }
}
```




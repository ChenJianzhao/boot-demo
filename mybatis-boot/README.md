## Spring Boot 集成 MyBatis

### 添加依赖
```xml
<!-- https://mvnrepository.com/artifact/com.alibaba/druid-spring-boot-starter -->
<dependencies>
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid-spring-boot-starter</artifactId>
        <version>1.1.10</version>
    </dependency>
    
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>1.3.2</version>
    </dependency>
    
    <dependency>
        <groupId>com.github.pagehelper</groupId>
        <artifactId>pagehelper-spring-boot-starter</artifactId>
        <version>1.2.5</version>
    </dependency>
</dependencies>
```



> 1、这里不引入spring-boot-starter-jdbc依赖，是由于mybatis-spring-boot-starter中已经包含了此依赖。
>
> 2、这里引入阿里的 druid 作为数据源，可使用其他数据源替换。



**MyBatis-Spring-Boot-Starter** 依赖将会提供如下：
- 自动检测现有的DataSource
- 将创建并注册SqlSessionFactory的实例，该实例使用SqlSessionFactoryBean将该DataSource作为输入进行传递
- 将创建并注册从SqlSessionFactory中获取的SqlSessionTemplate的实例。
- 自动扫描您的mappers，将它们链接到SqlSessionTemplate并将其注册到Spring上下文，以便将它们注入到您的bean中。

也就是说，使用了该 **Starter** 之后，只需要定义一个 **DataSource **即可（application.properties中可配置），它会自动创建使用该 DataSource 的 SqlSessionFactoryBean 以及 SqlSessionTemplate。会自动扫描你的Mappers，连接到 SqlSessionTemplate，并注册到Spring上下文中。



### 数据源配置

在src/main/resources/application.properties中配置数据源信息

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/spring?useUnicode=true&characterEncoding=utf-8
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
```



### 自定义数据源

Spring Boot默认使用 **tomcat-jdbc** 数据源，如果你想使用其他的数据源，比如这里使用了阿里巴巴的数据池管理,除了在`application.properties`配置数据源之外，你应该额外添加以下依赖：(上面的依赖已经给出)

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>1.1.10</version>
</dependency>
```



修改 MyBatisApplication.java

```java
@SpringBootApplication
// 在mapper接口添加@Mapper注解
// 或者在启动类上添加@MapperScan(“com.example”)注解都行
public class MybatisBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisBootApplication.class, args);
    }

    // 获取 SpringBoot 的配置参数
    @Autowired
    private Environment env;

    //ok这样就算自己配置了一个DataSource，Spring Boot会智能地选择我们配置的这个DataSource实例。
    @Bean
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));//用户名
        dataSource.setPassword(env.getProperty("spring.datasource.password"));//密码
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setInitialSize(2);//初始化时建立物理连接的个数
        dataSource.setMaxActive(20);//最大连接池数量
        dataSource.setMinIdle(0);//最小连接池数量
        dataSource.setMaxWait(60000);//获取连接时最大等待时间，单位毫秒。
        dataSource.setValidationQuery("SELECT 1");//用来检测连接是否有效的sql
        dataSource.setTestOnBorrow(false);//申请连接时执行validationQuery检测连接是否有效
        dataSource.setTestWhileIdle(true);//建议配置为true，不影响性能，并且保证安全性。
        dataSource.setPoolPreparedStatements(false);//是否缓存preparedStatement，也就是PSCache
        return dataSource;
    }
}
```



ok这样就算自己配置了一个DataSource，Spring Boot会智能地选择我们自己配置的这个DataSource实例。

**此外需要注意**，如果自定义了数据源，那么 application.xml 中关于数据源的配置就不会用于 Spirng Boot 的自动配置了。



### 初始化脚本

```sql
CREATE DATABASE /*!32312 IF NOT EXISTS*/`spring` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `spring`;
DROP TABLE IF EXISTS `learn_resource`;

CREATE TABLE `learn_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `author` varchar(20) DEFAULT NULL COMMENT '作者',
  `title` varchar(100) DEFAULT NULL COMMENT '描述',
  `url` varchar(100) DEFAULT NULL COMMENT '地址链接',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1029 DEFAULT CHARSET=utf8;

insert into `learn_resource`(`id`,`author`,`title`,`url`) values (999,'官方SpriongBoot例子','官方SpriongBoot例子','https://github.com/spring-projects/spring-boot/tree/master/spring-boot-samples');
insert into `learn_resource`(`id`,`author`,`title`,`url`) values (1000,'龙果学院','Spring Boot 教程系列学习','http://www.roncoo.com/article/detail/124661');
insert into `learn_resource`(`id`,`author`,`title`,`url`) values (1001,'嘟嘟MD独立博客','Spring Boot干货系列','http://tengj.top/');
insert into `learn_resource`(`id`,`author`,`title`,`url`) values (1002,'后端编程嘟','Spring Boot视频教程','http://www.toutiao.com/m1559096720023553/');
```



### MyBatis 的集成

**注解方式跟XML配置方式**，对于 MyBatis，个人还是觉得基于 XML 的配置对于管理 SQL 比较舒服。



**编写Dao层的代码**
新建LearnMapper接口，无需具体实现类。

```java
package com.dudu.dao;
@Mapper
public interface LearnMapper {
    
    int add(LearnResouce learnResouce);
    
    int update(LearnResouce learnResouce);
    
    int deleteByIds(String[] ids);
    
    LearnResouce queryLearnResouceById(Long id);
    
    public List<LearnResouce> queryLearnResouceList(Map<String, Object> params);
}
```



**修改application.properties 配置文件**

```properties
#指定 entity bean 所在包
mybatis.type-aliases-package=com.example.mybatisboot.entity
#指定映射文件
mybatis.mapperLocations=classpath:mapper/*.xml
```



**添加LearnMapper的映射文件**
在src/main/resources目录下新建一个mapper目录，在mapper目录下新建LearnMapper.xml文件。

通过mapper标签中的namespace属性指定对应的dao映射，这里指向LearnMapper。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.mybatisboot.LearnMapper">
  <resultMap id="baseResultMap" type="com.example.mybatisboot.entity.LearnResouce">
    <id column="id" property="id" jdbcType="BIGINT"  />
    <result column="author" property="author" jdbcType="VARCHAR"/>
    <result column="title" property="title" jdbcType="VARCHAR"/>
    <result column="url" property="url" jdbcType="VARCHAR"/>
  </resultMap>

  <sql id="baseColumnList" >
    id, author, title,url
  </sql>

  <select id="queryLearnResouceList" resultMap="baseResultMap" parameterType="java.util.HashMap">
    select
    <include refid="baseColumnList" />
    from learn_resource
    <where>
      1 = 1
      <if test="author!= null and author !=''">
        AND author like CONCAT(CONCAT('%',#{author,jdbcType=VARCHAR}),'%')
      </if>
      <if test="title != null and title !=''">
        AND title like  CONCAT(CONCAT('%',#{title,jdbcType=VARCHAR}),'%')
      </if>

    </where>
  </select>

  <select id="queryLearnResouceById"  resultMap="baseResultMap" parameterType="java.lang.Long">
    SELECT
    <include refid="baseColumnList" />
    FROM learn_resource
    WHERE id = #{id}
  </select>

  <insert id="add" parameterType="com.dudu.domain.LearnResouce" >
    INSERT INTO learn_resource (author, title,url) VALUES (#{author}, #{title}, #{url})
  </insert>

  <update id="update" parameterType="com.dudu.domain.LearnResouce" >
    UPDATE learn_resource SET author = #{author},title = #{title},url = #{url} WHERE id = #{id}
  </update>

  <delete id="deleteByIds" parameterType="java.lang.String" >
    DELETE FROM learn_resource WHERE id in
    <foreach item="idItem" collection="array" open="(" separator="," close=")">
      #{idItem}
    </foreach>
  </delete>
</mapper>
```



### 分页插件

上面我有使用到物理分页插件 pagehelper，用法还算简单，配置如下
pom.xml中添加依赖

```xml
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper-spring-boot-starter</artifactId>
    <version>1.2.5</version>
</dependency>
```

然后你只需在查询list之前使用 `PageHelper.startPage(int pageNum, int pageSize)` 方法即可。pageNum是第几页，pageSize是每页多少条。

```java
@Override
    public List<LearnResouce> queryLearnResouceList(Map<String,Object> params) {
        PageHelper.startPage(Integer.parseInt(params.get("page").toString()), Integer.parseInt(params.get("rows").toString()));
        return this.learnMapper.queryLearnResouceList(params);
    }
```

分页插件PageHelper项目地址： <https://github.com/pagehelper/Mybatis-PageHelper>

 

原文： [Spring Boot干货系列：(九)数据存储篇-SQL关系型数据库之MyBatis的使用)](http://tengj.top/2017/04/23/springboot9/)
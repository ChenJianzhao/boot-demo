## Spring Boot 集成 MyBatis-Plus

### 添加依赖

```xml
<!-- 不能引入 mybatis，mybatis-plus 已经包含，会引起冲突 -->
<!-- https://mvnrepository.com/artifact/com.alibaba/druid-spring-boot-starter -->
<dependencies>
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid-spring-boot-starter</artifactId>
        <version>1.1.10</version>
    </dependency>

    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>2.2.0</version>
    </dependency>
    <!-- https://mvnrepository.com/artifact/org.apache.velocity/velocity -->
    <dependency>
        <groupId>org.apache.velocity</groupId>
        <artifactId>velocity</artifactId>
        <version>1.7</version>
    </dependency>
</dependencies>
```



### 数据源配置

使用 application.xml 自动装配数据源

```properties
# ###########################################################################
# configuration for DataSource AutoConfiguration
# ###########################################################################
spring.datasource.url=jdbc:mysql://localhost:3306/spring?useUnicode=true&characterEncoding=utf-8
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
```



### 配置 MyBatis-Plus

配置 application.yml  

MyBatis-Plus 在缺少 @Bean(" SqlSessionFactory") 将自动配置

```yaml
# ###########################################################################
# configuration for mybatis-plus AutoConfiguration
# ###########################################################################
#mybatis
mybatis-plus:
  mapper-locations: classpath:/mapper/*Mapper.xml
  #实体扫描，多个package用逗号或者分号分隔
  typeAliasesPackage: com.example.mybatisplusboot.entity
#  typeEnumsPackage: com.baomidou.springboot.entity.enums

  global-config:
    #主键类型  0:"数据库ID自增", 1:"用户输入ID",2:"全局唯一ID (数字类型唯一ID)", 3:"全局唯一ID UUID";
    id-type: 0
    #字段策略 0:"忽略判断",1:"非 NULL 判断"),2:"非空判断"
    field-strategy: 2
    #驼峰下划线转换
    db-column-underline: true
    #刷新mapper 调试神器
    refresh-mapper: true
    #数据库大写下划线转换
    #capital-mode: true
    #序列接口实现类配置,不在推荐使用此方式进行配置,请使用自定义bean注入
#    key-generator: com.baomidou.mybatisplus.incrementer.H2KeyGenerator

    #逻辑删除配置（下面3个配置）
#    logic-delete-value: 0
#    logic-not-delete-value: 1
    #自定义sql注入器,不在推荐使用此方式进行配置,请使用自定义bean注入
#    sql-injector: com.baomidou.mybatisplus.mapper.LogicSqlInjector

    #自定义填充策略接口实现,不在推荐使用此方式进行配置,请使用自定义bean注入
#    meta-object-handler: com.baomidou.springboot.MyMetaObjectHandler

    #自定义SQL注入器
    #sql-injector: com.baomidou.springboot.xxx
    # SQL 解析缓存，开启后多租户 @SqlParser 注解生效
    sql-parser-cache: true
    
  configuration:
    map-underscore-to-camel-case: true
    cache-enabled: false

```



### @Configuration 补充配置 MyBatis-Plus

添加 `MyBatisPlusConifgDev.java` (名字随意)

```java
package com.example.druidboot;

/**
 * @author chenjz
 * 2018/5/23
 *
 * 官网简易配置
 */
@Configuration
public class MybatisPlusConfigDev {

    /**
     * 【性能分析拦截器】，用于输出每条 SQL 语句及其执行时间
     * mybatis-plus SQL执行效率插件【生产环境可以关闭】
     */
    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        performanceInterceptor.setFormat(true);
        performanceInterceptor.setMaxTime(100);
        return performanceInterceptor;
    }
    /**
     * 【SQL 执行分析拦截器】【 目前只支持 MYSQL-5.6.3 以上版本 】
     * 分析 处理 DELETE UPDATE 语句， 防止小白或者恶意 delete update 全表操作！
     */
    @Bean
    public SqlExplainInterceptor sqlExplainInterceptor() {
        SqlExplainInterceptor sqlExplain = new SqlExplainInterceptor();
        sqlExplain.setStopProceed(true);
        return sqlExplain;
    }

    /**
     * 【分页插件】
     * @return
     */
//    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setLocalPage(true);// 开启 PageHelper 的支持

        /*
         *  这里配合 分页拦截器 使用
         * 【测试多租户】 SQL 解析处理拦截器<br>
         * 这里固定写成住户 1 实际情况你可以从cookie读取，因此数据看不到 【 麻花藤 】 这条记录（ 注意观察 SQL ）<br>
         */
        List<ISqlParser> sqlParserList = new ArrayList<>();
        TenantSqlParser tenantSqlParser = new TenantSqlParser();
        tenantSqlParser.setTenantHandler(new TenantHandler() {
            @Override
            public Expression getTenantId() {
                return new LongValue(1L);
            }

            @Override
            public String getTenantIdColumn() {
                return "dbid";
            }

            @Override
            public boolean doTableFilter(String tableName) {
                // 这里可以判断是否过滤表
                if ("learn_tenant".equals(tableName)) {
                    return true;
                }
                return false;
            }
        });
        sqlParserList.add(tenantSqlParser);
        paginationInterceptor.setSqlParserList(sqlParserList);

        paginationInterceptor.setSqlParserFilter(new ISqlParserFilter() {
            @Override
            public boolean doFilter(MetaObject metaObject) {
                MappedStatement ms = PluginUtils.getMappedStatement(metaObject);
                // 过滤自定义查询此时无租户信息约束【 麻花藤 】出现
                /**
                 * 理解：在定义的 mapper 接口方法上注解 @SqlParser(filter = true)
                 * 如果过滤条件返回 true，则不受租户信息约束
                 */
                if ("com.baomidou.springboot.mapper.UserMapper.selectListBySQL".equals(ms.getId())) {
                    return true;
                }
                return false;
            }
        });

        return paginationInterceptor;
    }
}

```


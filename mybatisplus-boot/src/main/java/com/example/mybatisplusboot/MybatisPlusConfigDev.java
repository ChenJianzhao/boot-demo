package com.example.mybatisplusboot;

import com.baomidou.mybatisplus.MybatisConfiguration;
import com.baomidou.mybatisplus.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.enums.DBType;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.incrementer.OracleKeyGenerator;
import com.baomidou.mybatisplus.mapper.LogicSqlInjector;
import com.baomidou.mybatisplus.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.plugins.SqlExplainInterceptor;
import com.baomidou.mybatisplus.plugins.parser.ISqlParser;
import com.baomidou.mybatisplus.plugins.parser.ISqlParserFilter;
import com.baomidou.mybatisplus.plugins.parser.tenant.TenantHandler;
import com.baomidou.mybatisplus.plugins.parser.tenant.TenantSqlParser;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import com.baomidou.mybatisplus.toolkit.PluginUtils;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenjz
 * 2018/5/23
 */

/**
 * 官网简易配置
 */
@Configuration
public class MybatisPlusConfigDev {

    /**
     * 性能分析拦截器，用于输出每条 SQL 语句及其执行时间
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
     * SQL 执行分析拦截器【 目前只支持 MYSQL-5.6.3 以上版本 】
     * 分析 处理 DELETE UPDATE 语句， 防止小白或者恶意 delete update 全表操作！
     */
    @Bean
    public SqlExplainInterceptor sqlExplainInterceptor() {
        SqlExplainInterceptor sqlExplain = new SqlExplainInterceptor();
        sqlExplain.setStopProceed(true);
        return sqlExplain;
    }

    @Autowired
    @Qualifier("mybatisPlusDataSource")
    DataSource dataSource;

    @Autowired
    Interceptor[] interceptors;

    @Bean("mybatisSqlSession")
    public SqlSessionFactory sqlSessionFactory(/*DataSource dataSource,*/ ResourceLoader resourceLoader, GlobalConfiguration globalConfiguration) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        // 以下可以在 application.xml 中配置
        sqlSessionFactory.setTypeAliasesPackage("com.example.mybatisplusboot.entity");

        /*
         * MybatisConfiguration
         */
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        sqlSessionFactory.setConfiguration(configuration);

        /*
         * 插件
         */
        sqlSessionFactory.setPlugins(interceptors);

//        sqlSessionFactory.setPlugins(new Interceptor[]{
//                new PaginationInterceptor(),
//                new PerformanceInterceptor(),
//                new OptimisticLockerInterceptor()
//        });

        sqlSessionFactory.setGlobalConfig(globalConfiguration);
        return sqlSessionFactory.getObject();
    }

    /**
     * 全局配置 Bean
     * @return
     */
    @Bean
    public GlobalConfiguration globalConfiguration() {
        GlobalConfiguration conf = new GlobalConfiguration(new LogicSqlInjector());
        // ID 策略 AUTO->`0`("数据库ID自增") INPUT->`1`(用户输入ID") ID_WORKER->`2`("全局唯一ID") UUID->`3`("全局唯一ID")
//        conf.setIdType(2);
        conf.setIdType(IdType.ID_WORKER.getKey());
        conf.setDbColumnUnderline(true);        // 数据库下划线映射为驼峰
        conf.setDbType(DBType.MYSQL.name());    // 数据库类型（不需要这么配置了，自动获取数据库类型）

//        conf.setLogicNotDeleteValue("-1");    // 逻辑删除
//        conf.setLogicNotDeleteValue("1");
//        conf.setMetaObjectHandler(new H2MetaObjectHandler());
//        conf.setKeyGenerator(new OracleKeyGenerator()); // 主键 Sequence（mysql不需要）
        return conf;
    }



    /**
     * 分页插件
     * @return
     */
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

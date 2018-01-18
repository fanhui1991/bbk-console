package com.bookie.guns.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import com.bookie.guns.config.properties.ConsoleProperties;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Function: TODO
 *
 * @author Viki
 * @date 2017/11/13 9:22
 */
@Configuration
@EnableTransactionManagement(order = 2)//由于引入多数据源，所以让spring事务的aop要在多数据源切换aop的后面
@MapperScan(basePackages = {"com.bookie.guns.dao.console"}, sqlSessionTemplateRef = "consoleSqlSessionTemplate")
public class ConsoleDatasourceConfig {

    @Autowired
    ConsoleProperties consoleProperties;

    @Primary
    @Bean("consoleDataSource")
    public DataSource consoleDataSource(){
        DruidDataSource datasource = new DruidDataSource();
        consoleProperties.config(datasource);
        return datasource;
    }

    @Primary
    @Bean("consoleTransactionManager") public PlatformTransactionManager consoleTransactionManager(@Qualifier("consoleDataSource") DataSource dataSource) throws SQLException {
        return new DataSourceTransactionManager(dataSource);
    }

    @Primary
    @Bean("consoleSqlSessionFactory") public SqlSessionFactory consoleSqlSessionFactory(@Qualifier("consoleDataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mapper/console/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Primary
    @Bean(name = "consoleSqlSessionTemplate")
    public SqlSessionTemplate consoleSqlSessionTemplate(@Qualifier("consoleSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}

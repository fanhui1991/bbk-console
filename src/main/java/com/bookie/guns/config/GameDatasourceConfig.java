package com.bookie.guns.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.bookie.guns.config.properties.GameProperties;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@MapperScan(basePackages = {"com.bookie.guns.dao.game"}, sqlSessionTemplateRef = "gameSqlSessionTemplate")
public class GameDatasourceConfig {

    @Autowired
    GameProperties gameProperties;

    @Bean("gameDataSource")
    public DataSource gameDataSource(){
        DruidDataSource datasource = new DruidDataSource();
        gameProperties.config(datasource);
        return datasource;
    }

    @Bean("gameTransactionManager") public PlatformTransactionManager gameTransactionManager(@Qualifier("gameDataSource") DataSource dataSource) throws SQLException {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean("gameSqlSessionFactory") public SqlSessionFactory gameSqlSessionFactory(@Qualifier("gameDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mapper/game/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "gameSqlSessionTemplate")
    public SqlSessionTemplate gameSqlSessionTemplate(@Qualifier("gameSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}

package com.sleepy.blog.config;

/**
 * orm配置
 *
 * @author gehoubao
 * @create 2020-01-19 10:39
 **/
public class OrmConfiguration {
//    private Environment environment;
//
//    private RelaxedPropertyResolver datasourcePropertyResolver;
//
//    //从application.yml中读取资源
//
//    @Override
//    public void setEnvironment(Environment environment) {
//
//        this.environment = environment;
//
//        this.datasourcePropertyResolver = new RelaxedPropertyResolver(environment, "spring.datasource.");
//
//    }
//
//
//    //datasource
//
//    @Bean(initMethod = "init", destroyMethod = "close")
//    public DataSource dataSource() throws SQLException {
//        DruidDataSource druidDataSource = new DruidDataSource();
//        druidDataSource.setUrl(datasourcePropertyResolver.getProperty("url"));
//        druidDataSource.setUsername(datasourcePropertyResolver
//                .getProperty("username"));
//        druidDataSource.setPassword(datasourcePropertyResolver
//                .getProperty("password"));
//        druidDataSource.setDriverClassName(datasourcePropertyResolver
//                .getProperty("dataSourceClassName"));
//        druidDataSource.setInitialSize(1);
//        druidDataSource.setMinIdle(1);
//        druidDataSource.setMaxActive(20);
//        druidDataSource.setMaxWait(60000);
//        druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
//        druidDataSource.setMinEvictableIdleTimeMillis(300000);
//        druidDataSource.setTestWhileIdle(true);
//        druidDataSource.setTestOnBorrow(false);
//        druidDataSource.setTestOnReturn(false);
//        return druidDataSource;
//    }
//
//    //sessionFactory
//    @Bean
//    public LocalSessionFactoryBean sessionFactory() throws SQLException {
//        LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
//        localSessionFactoryBean.setDataSource(this.dataSource());
//        Properties properties1 = new Properties();
//        properties1.setProperty("hibernate.hbm2ddl.auto", "none");
//        properties1.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
//        properties1.setProperty("hibernate.show_sql", "true");
//        localSessionFactoryBean.setHibernateProperties(properties1);
//        localSessionFactoryBean.setPackagesToScan("com.yun.lab.server.dts.domain.*");
//        return localSessionFactoryBean;
//    }
//
//    @Bean("sessionFactory")
//    @Primary
//    public SessionFactory sessionFactory(LocalSessionFactoryBean localSessionFactoryBean) throws
//            PropertyVetoException, IOException {
//        SessionFactory sessionFactory = localSessionFactoryBean.getObject();
//
//        return sessionFactory;
//
//    }
//
//    //txManager事务开
//    @Bean("transactionManager")
//    public HibernateTransactionManager txManager() throws SQLException {
//        HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager();
//        hibernateTransactionManager.setSessionFactory(sessionFactory().getObject());
//        return hibernateTransactionManager;
//
//    }
}
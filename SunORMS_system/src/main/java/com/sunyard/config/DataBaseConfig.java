package com.sunyard.config;

import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.NoRollbackRuleAttribute;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import com.github.pagehelper.PageInterceptor;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.sunyard.cop.IF.mybatis.datasource.DynamicDataSource;

/**
 * 数据库相关配置
 * 
 * @author zgz19
 *
 */
@Configuration
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableTransactionManagement
@MapperScan({"com.sunyard.aos.**.dao","com.sunyard.ars.**.dao","com.sunyard.cop.IF.mybatis.dao"})
public class DataBaseConfig {

	/**
	 * 数据源
	 * @param dataSourceType 数据源类型， c3p0/jndi
	 * @param dataSourceName jndi数据源名称
	 * @param driverClass
	 * @param url
	 * @param username
	 * @param password
	 * @param maxPoolSize
	 * @param minPoolSize
	 * @param initialPoolSize
	 * @param acquireIncrement
	 * @return
	 * @throws PropertyVetoException
	 */
	@Bean
	public DataSource dataSource(
			@Value("${dataSource.type}") String dataSourceType, @Value("${dataSource.name}") String dataSourceName,
			@Value("${main.driverClassName}") String driverClass, @Value("${main.url}") String url,
			@Value("${main.username}") String username, @Value("${main.password}") String password,
			@Value("${c3p0.pool.size.max}") int maxPoolSize, @Value("${c3p0.pool.size.min}") int minPoolSize,
			@Value("${c3p0.pool.size.ini}") int initialPoolSize,
			@Value("${c3p0.pool.size.increment}") int acquireIncrement) throws PropertyVetoException, IllegalArgumentException, NamingException {
		if(dataSourceType != null && dataSourceType.toLowerCase().equals("jndi")) {
			JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
	        bean.setJndiName(dataSourceName);
	        bean.setResourceRef(true);
	        bean.setProxyInterface(DataSource.class);
	        bean.setLookupOnStartup(false);
	        bean.afterPropertiesSet();
	        return (DataSource) bean.getObject();
		}else {
			DynamicDataSource dataSource = new DynamicDataSource();
			Map<Object, Object> dataSourceMap = new HashMap<>();
			DataSource mainDB = mainDB(driverClass, url, username, password, maxPoolSize, minPoolSize, initialPoolSize,
					acquireIncrement);
			dataSourceMap.put("mainDB", mainDB);
			dataSource.setTargetDataSources(dataSourceMap);
			dataSource.setDefaultTargetDataSource(mainDB);
			return dataSource;
		}
	}

	/**
	 * C3p0数据源配置
	 * 
	 * @param driverClass
	 * @param url
	 * @param username
	 * @param password
	 * @param maxPoolSize
	 * @param minPoolSize
	 * @param initialPoolSize
	 * @param acquireIncrement
	 * @return
	 * @throws PropertyVetoException
	 */
	private DataSource mainDB(String driverClass, String url, String username, String password, int maxPoolSize,
			int minPoolSize, int initialPoolSize, int acquireIncrement) throws PropertyVetoException {
		ComboPooledDataSource mainDB = new ComboPooledDataSource();
		mainDB.setDriverClass(driverClass);
		mainDB.setJdbcUrl(url);
		mainDB.setUser(username);
		mainDB.setPassword(password);
		mainDB.setMaxPoolSize(maxPoolSize);
		mainDB.setMinPoolSize(minPoolSize);
		mainDB.setInitialPoolSize(initialPoolSize);
		mainDB.setAcquireIncrement(acquireIncrement);
		return mainDB;
	}
	
	/**
	 * JNDI配置
	 * @param dataSourceName
	 * @param driverClass
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	@Bean
    public ServletWebServerFactory servletContainer(
    		@Value("${dataSource.name}") String dataSourceName, 
    		@Value("${main.driverClassName}") String driverClass, @Value("${main.url}") String url,
			@Value("${main.username}") String username, @Value("${main.password}") String password) {
        return new TomcatServletWebServerFactory() {
            @Override
            protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {
                tomcat.enableNaming();
                return super.getTomcatWebServer(tomcat);
            }
            @Override
            protected void postProcessContext(Context context) {
                ContextResource resource = new ContextResource();
                resource.setName(dataSourceName);
                resource.setType(DataSource.class.getName());
                resource.setProperty("driverClassName", driverClass);
                resource.setProperty("url", url);
                resource.setProperty("username", username);
                resource.setProperty("password", password);
                context.getNamingResources().addResource(resource);
                super.postProcessContext(context);
            }
        };
    }


	/**
	 * 数据库标识配置 
	 * @return
	 */
	@Bean
	public DatabaseIdProvider databaseIdProvider() {
		VendorDatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
		Properties properties = new Properties();
		properties.put("DB2", "db2");
		properties.put("Oracle", "oracle");
		properties.put("MySQL", "mysql");
		databaseIdProvider.setProperties(properties);
		return databaseIdProvider;
	}

	/**
	 * 添加分页插件
	 * 
	 * @return
	 */
	@Bean
	public PageInterceptor pageInterceptor() {
		PageInterceptor pageInterceptor = new PageInterceptor();
		Properties properties = new Properties();
		properties.put("helperDialect", "oracle");
		properties.put("pageSizeZero", "true");
		properties.put("reasonable", "true");
		properties.put("supportMethodsArguments", "true");
		properties.put("params", "count=countSql");
		properties.put("autoRuntimeDialect", "true");
		pageInterceptor.setProperties(properties);
		return pageInterceptor;
	}

	/**
	 * 配置事务管理器
	 * @param dataSource
	 * @return
	 */
	@Bean
	public DataSourceTransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	/**
	 * 配置事务属性（传播特性）
	 * @param transactionManager
	 * @return
	 */
	@Bean
	public TransactionInterceptor userTxAdvice(DataSourceTransactionManager transactionManager) {
		NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();

		RollbackRuleAttribute exceptionRule = new RollbackRuleAttribute(Exception.class);
		RollbackRuleAttribute runtimeExceptionRule = new RollbackRuleAttribute(RuntimeException.class);
		NoRollbackRuleAttribute runtimeExceptionNoRule = new NoRollbackRuleAttribute(RuntimeException.class);

		List<RollbackRuleAttribute> listFirst = new ArrayList<>();
		listFirst.add(exceptionRule);
		listFirst.add(runtimeExceptionNoRule);
		RuleBasedTransactionAttribute ruleFirst = new RuleBasedTransactionAttribute(
				TransactionDefinition.PROPAGATION_REQUIRED, listFirst);

		RuleBasedTransactionAttribute ruleSecond = new RuleBasedTransactionAttribute(
				TransactionDefinition.PROPAGATION_REQUIRED, Collections.singletonList(runtimeExceptionRule));

		RuleBasedTransactionAttribute ruleThird = new RuleBasedTransactionAttribute(
				TransactionDefinition.PROPAGATION_REQUIRED, Collections.singletonList(exceptionRule));

		RuleBasedTransactionAttribute ruleFourth = new RuleBasedTransactionAttribute();
		ruleFourth.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);

		Map<String, TransactionAttribute> txMap = new HashMap<>();
		txMap.put("delete*", ruleFirst);
		txMap.put("insert*", ruleSecond);
		txMap.put("update*", ruleThird);
		txMap.put("find*", ruleFourth);
		txMap.put("get*", ruleFourth);
		txMap.put("select*", ruleFourth);
		source.setNameMap(txMap);
		return new TransactionInterceptor(transactionManager, source);
	}

	/**
	 *  配置事务切点（参与类、方法）
	 * @param userTxAdvice
	 * @return
	 */
	@Bean
	public DefaultPointcutAdvisor txAop(TransactionInterceptor userTxAdvice) {
		DefaultPointcutAdvisor pointcutAdvisor = new DefaultPointcutAdvisor();
		pointcutAdvisor.setAdvice(userTxAdvice);
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression("execution(public * com.sunyard.cop.IF.modelimpl.*.*(..)) "
				+ "|| execution(public * com.sunyard.aos.*.modelImpl.*.*(..)) "
				+ "|| execution(public * com.sunyard.ars.*.service.*.*(..))");
		pointcutAdvisor.setPointcut(pointcut);
		return pointcutAdvisor;
	}

}

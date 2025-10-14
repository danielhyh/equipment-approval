package cn.iocoder.yudao.module.biz.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class DmDatasourceProxy implements InvocationHandler {

    private final DataSource targetDataSource;

    private static final Logger logger = LoggerFactory.getLogger(DmDatasourceProxy.class);

    // 构造方法，传入实际的 DataSource 对象
    public DmDatasourceProxy(DataSource targetDataSource) {
        this.targetDataSource = targetDataSource;
    }
    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        // 拦截 close() 方法
        if ("close".equals(methodName)) {
            logger.info("DataSource.close() called, but ignored by proxy.");
            // 可以选择：忽略、记录、或真正关闭
            // this.closed = true;
            return null;
        }
        // 拦截 getConnection 方法
        if ("getConnection".equals(methodName)) {

            // 在这里可以添加前置逻辑，比如记录日志、事务管理等
            Connection connection = (Connection) method.invoke(targetDataSource, args);
            // 可以对 Connection 进行包装或添加后置逻辑
            connection = (Connection) Proxy.newProxyInstance(
                    connection.getClass().getClassLoader(),
                    new  Class[]{Connection.class},
                    new ConnectionInvocationHandler(connection)
            );
            return connection;
        }
        // 其他方法直接调用原始 DataSource 的实现
        return method.invoke(targetDataSource, args);
    }

    // 2. Connection处理器
    class ConnectionInvocationHandler implements InvocationHandler {
        private final Connection target;

        public ConnectionInvocationHandler(Connection target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // 拦截prepareStatement和prepareCall方法
            if ("prepareStatement".equals(method.getName()) && args.length > 0) {
                String originalSql = (String) args[0];
                String modifiedSql = SqlAdapter.adaptSql(originalSql);
                args[0] = modifiedSql;

                PreparedStatement ps = (PreparedStatement) method.invoke(target, args);
                return createPreparedStatementProxy(ps, originalSql, modifiedSql);
            }

            if ("createStatement".equals(method.getName())) {
                Statement stmt = (Statement) method.invoke(target, args);
                return createStatementProxy(stmt);
            }

            return method.invoke(target, args);
        }

        private PreparedStatement createPreparedStatementProxy(PreparedStatement ps, String originalSql, String modifiedSql) {
            return (PreparedStatement) Proxy.newProxyInstance(
                    ps.getClass().getClassLoader(),
                    new Class[]{PreparedStatement.class},
                    new DmDatasourceProxy.PreparedStatementHandler(ps, originalSql, modifiedSql)
            );
        }

        private Statement createStatementProxy(Statement stmt) {
            return (Statement) Proxy.newProxyInstance(
                    stmt.getClass().getClassLoader(),
                    new Class[]{Statement.class},
                    new DmDatasourceProxy.StatementHandler(stmt)
            );
        }
    }

    // 3. PreparedStatement处理器
    static class PreparedStatementHandler implements InvocationHandler {
        private final PreparedStatement target;
        private final String originalSql;
        private final String modifiedSql;

        public PreparedStatementHandler(PreparedStatement target, String originalSql, String modifiedSql) {
            this.target = target;
            this.originalSql = originalSql;
            this.modifiedSql = modifiedSql;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // 可以在这里记录SQL执行日志
            if ("execute".equals(method.getName()) || "executeQuery".equals(method.getName()) || "executeUpdate".equals(method.getName())) {
                logger.info("Original SQL: {}", originalSql);
                logger.info("Modified SQL: {}", modifiedSql);
            }
            return method.invoke(target, args);
        }
    }

    // 4. Statement处理器
    record StatementHandler(Statement target) implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // 拦截execute系列方法
            if (method.getName().startsWith("execute") && args != null && args.length > 0 && args[0] instanceof String originalSql) {
                String modifiedSql = SqlAdapter.adaptSql(originalSql);
                args[0] = modifiedSql;

                logger.info("Original SQL: {}", originalSql);
                logger.info("Modified SQL: {}", modifiedSql);
            }
            return method.invoke(target, args);
        }
    }
}

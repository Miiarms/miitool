package com.miiarms.miitool.mybatis;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 处理查询参数带特殊符号 % _
 * @author Miiarms
 * @version 1.0
 * @date 2021/7/3
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class MybatisParameterPlugin implements Interceptor {

    private static final String LIKE_ORIGIN_CONCAT_REG = "\\s*\\w+\\s*like\\s*concat(\\(\\s*'%'\\s*,\\s*(#\\{\\s*(\\w+)\\s*\\})\\s*,\\s*'%'\\))?";
    private static final Pattern LIKEN_ORIGIN_PATTERN = Pattern.compile(LIKE_ORIGIN_CONCAT_REG, Pattern.CASE_INSENSITIVE);

    private static final String  LIKE_BOUND_CONCAT_REG = "\\s*\\w+\\s*LIKE\\s*concat(\\(\\s*'%'\\s*,\\s*\\?\\s*,\\s*'%'\\))?";;
    private static final Pattern LIKEN_BOUND_PATTERN = Pattern.compile(LIKE_BOUND_CONCAT_REG, Pattern.CASE_INSENSITIVE);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        // 分离代理对象链(由于目标类可能被多个插件拦截，从而形成多次代理，通过下面的两次循环
        // 可以分离出最原始的的目标类)
        while (metaStatementHandler.hasGetter("h")) {
            Object object = metaStatementHandler.getValue("h");
            metaStatementHandler = SystemMetaObject.forObject(object);
        }
        // 分离最后一个代理对象的目标类
        while (metaStatementHandler.hasGetter("target")) {
            Object object = metaStatementHandler.getValue("target");
            metaStatementHandler = SystemMetaObject.forObject(object);
        }
        MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");

        MetaObject metaMappedStatement = MetaObject.forObject(mappedStatement, new DefaultObjectFactory(), new DefaultObjectWrapperFactory(), new DefaultReflectorFactory());
        final BoundSql boundSql = statementHandler.getBoundSql();
        SqlNode sqlNode = null;
        try {
            // RawSqlSource 没有 rootSqlNode会报错
            sqlNode = (SqlNode) metaMappedStatement.getValue("sqlSource.rootSqlNode");
        }catch (Exception e){
        }
        // 不是动态sql 的过滤，暂时没实现，有需要的可以自行修改
        if(Objects.isNull(sqlNode)){
            return invocation.proceed();
        }

        DynamicContext context = new DynamicContext(mappedStatement.getConfiguration(), boundSql.getParameterObject());
        sqlNode.apply(context);
        String modifyLikeSql = modifyLikeSql(metaStatementHandler, context, mappedStatement, boundSql);
        metaStatementHandler.setValue("delegate.boundSql.sql", modifyLikeSql);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    public String modifyLikeSql(MetaObject metaStatementHandler, DynamicContext context, MappedStatement mappedStatement, BoundSql boundSql){
        String sql =  context.getSql();
        if(!sql.toLowerCase().contains("like")){
            return boundSql.getSql();
        }
        Matcher matcher = LIKEN_ORIGIN_PATTERN.matcher(sql);
        List<String> parameterNames = new ArrayList<>();
        while(matcher.find()){
            if(matcher.groupCount() >= 3 && Objects.nonNull(matcher.group(3))){
                parameterNames.add(matcher.group(3));
            }
        }

        //修改参数
        Object parameterObject = boundSql.getParameterObject();
        if(parameterObject instanceof HashMap){
            HashMap<String,Object> paramMap=(HashMap)boundSql.getParameterObject();
            for(String parmeterName : parameterNames){
                Object val = paramMap.get(parmeterName);
                if(Objects.nonNull(val) && val instanceof String && (val.toString().contains("%")||val.toString().contains("_"))){
                    val = val.toString().replace("%", "/%").replace("_", "/_");
                    // pageHelper分页插件会导致这个问题，/% 变成 //% ,需要替换回来
                    val = val.toString().replace("//%", "/%").replace("//_", "/_");
                    paramMap.put(parmeterName, val);
                }
            }
        }else if(parameterObject instanceof String){
            parameterObject = parameterObject.toString().replace("%", "/%").replace("_", "/_");
            metaStatementHandler.setValue("delegate.boundSql.parameterObject", parameterObject);
            metaStatementHandler.setValue("delegate.parameterHandler.parameterObject", parameterObject);
        }else {
            for(String parmeterName : parameterNames){
                try {
                    final Field declaredField = parameterObject.getClass().getDeclaredField(parmeterName);
                    ReflectionUtils.makeAccessible(declaredField);
                     Object val = declaredField.get(parameterObject);
                    if(Objects.nonNull(val) && val instanceof String && (val.toString().contains("%")||val.toString().contains("_"))){
                        val = val.toString().replace("%", "/%").replace("_", "/_");
                        ReflectionUtils.setField(declaredField, parameterObject, val);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        String sqlBound = boundSql.getSql();
        Matcher matcherBound = LIKEN_BOUND_PATTERN.matcher(sqlBound);
        List<String> replaceSement = new ArrayList<>();
        while(matcherBound.find()){
            replaceSement.add(matcherBound.group());
        }
        for(String s : replaceSement){
            sqlBound = sqlBound.replace(s, s + " ESCAPE '/' ");
        }

        return sqlBound;
    }


}

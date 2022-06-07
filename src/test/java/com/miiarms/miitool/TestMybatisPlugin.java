package com.miiarms.miitool;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Miiarms
 * @version 1.0
 * @date 2021/7/8
 */
public class TestMybatisPlugin {

    @Test
    public void code(){
        Assertions.assertNotEquals(1, 2);

        String sql =  "select * from t_table  where name like concat('%', #{name}, '%') order by name";
        String reg="\\s*\\w+\\s*LIKE\\s*concat(\\(\\s*'%'\\s*,\\s*(#\\{\\s*(\\w+)\\s*\\})\\s*,\\s*'%'\\))?";
        Pattern pattern = Pattern.compile(reg,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        while(matcher.find()){
            int n = matcher.groupCount();
            for (int i = 0; i <= n; i++){
                String  output = matcher.group(i);
                System.out.println(output);
            }
        }
        System.out.println("-================");
        String boundSql =  "select * from t_table  where name like concat('%', ?, '%') and age like concat('%', ?, '%') order by name";
        String boudReg="\\s*\\w+\\s*LIKE\\s*concat(\\(\\s*'%'\\s*,\\s*\\?\\s*,\\s*'%'\\))";
        Pattern patternBound = Pattern.compile(boudReg,Pattern.CASE_INSENSITIVE);
        Matcher matcherBound = patternBound.matcher(boundSql);
        while(matcherBound.find()){
            int n = matcherBound.groupCount();
            for (int i = 0; i <= n; i++){
                String  output = matcherBound.group(i);
                System.out.println(output);
            }
        }
    }
}

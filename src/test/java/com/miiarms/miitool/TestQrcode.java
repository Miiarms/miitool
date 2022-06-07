package com.miiarms.miitool;

import com.miiarms.miitool.qrcode.QrCodeUtils;
import org.junit.jupiter.api.Test;
import sun.misc.BASE64Encoder;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Miiarms
 * @version 1.0
 * @date 2021/6/3
 */
public class TestQrcode {

    @Test
    public void test() throws Exception {
        String content = "https://baidu.com";
        String path = "C:\\Users\\legend\\Desktop\\test.png";
        //创建二维码
        QrCodeUtils.createQRCode(300, 300, content, "utf-8", path, "png");

        //解析二维码
        String string = QrCodeUtils.decodeQRCode(path);

        BASE64Encoder encoder = new BASE64Encoder();
        encoder.encode(new byte[1]);
        System.out.println(string);
    }

    @Test
    public void code(){

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

        final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        boolean flag = LocalTime.parse("20:11", timeFormatter).isAfter(LocalTime.parse("19:20", timeFormatter));
        System.out.println(flag);


    }
}

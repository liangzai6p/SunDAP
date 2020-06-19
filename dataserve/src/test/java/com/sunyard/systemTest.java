package com.sunyard;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.RandomUtil;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: SunDAP
 * @description: systemTest
 * @author: yey.he
 * @create: 2020-06-12 11:41
 **/
@SpringBootTest
public class systemTest {

    @Test
    public void generateDatabyDate() throws Exception{
        for (long year = 20170000;year <= 20200000;year+=10000) {
            for (long month = 100; month <= 1200; month += 100) {
                for (long day = 1; day <= 27; day++) {
                    generateData(String.valueOf(year + month + day));
                    System.out.println("生成成功："+String.valueOf(year + month + day));
                }
            }
        }
    }



    public static void generateData(String date) throws Exception{
        Connection connGet = null,connSet = null;
        PreparedStatement ps = null;
        ResultSet rs = getRs(connGet,ps);
        List<List<String>> list = new ArrayList<>();
        ResultSetMetaData md = rs.getMetaData();

        int columnCount = md.getColumnCount();

        while (rs.next()){
            List<String> subList = new ArrayList<>();
            for (int i = 1 ; i <= columnCount ; i ++){
                subList.add(rs.getString(i));
            }
            list.add(subList);
        }

        for (List<String> subList : list){
            int busi_count = Integer.parseInt(subList.get(10));
            busi_count = RandomUtil.randomInt(Math.round(busi_count/2),Math.round(busi_count*2));
            subList.set(10,String.valueOf(busi_count));
            int amount_per = RandomUtil.randomInt(0, 2000);
            int amount = amount_per*busi_count;

            int low = busi_count>10?busi_count-10:1;
            int cus_count = busi_count>low?RandomUtil.randomInt(low, busi_count):busi_count;
            subList.add(String.valueOf(amount));
            subList.add(String.valueOf(cus_count));
            subList.add(date);
        }

        insert(connSet,ps,list);

        IoUtil.close(connGet);
        IoUtil.close(connSet);
        IoUtil.close(rs);
        IoUtil.close(ps);
    }






    public static ResultSet getRs(Connection conn , PreparedStatement ps){
        ResultSet resultSet = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@172.1.1.36:1521:orcl","sunaos","sunaos");
            String sql = "select bloc_no,bloc_name,ZONE_NO,ZONE_NAME,BRANCH_NO,BRANCH_NAME,ORGAN_NO,ORGAN_NAME,SYS_NUM,SYS_NAME,count(TRANS_ID) \n" +
                    "from MN_BUSI_DETAIL_V \n" +
                    "GROUP BY bloc_no,bloc_name,ZONE_NO,ZONE_NAME,BRANCH_NO,BRANCH_NAME,ORGAN_NO,ORGAN_NAME,SYS_NUM,SYS_NAME";
            ps = conn.prepareStatement(sql);
            resultSet = ps.executeQuery();
            return resultSet;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }

    public static void insert(Connection conn,PreparedStatement ps,List<List<String>> list) throws Exception{
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@172.1.1.11:1521:orcl","sundap","123456");
            String sql = "insert into DM_BUSI_DAILY_TB values (?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'yyyymmdd'))";

            for (List<String> s : list){
                ps = conn.prepareStatement(sql);
                for (int i = 1 ; i <= s.size() ; i++){
                    ps.setString(i,s.get(i-1));
                }
                ps.execute();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            IoUtil.close(conn);
            IoUtil.close(ps);
        }

    }


}

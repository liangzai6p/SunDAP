package com.sunyard;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.db.Db;
import cn.hutool.db.DbUtil;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
//
//    @Test
//    public void generateDatabyDate() throws Exception{
//        for (long year = 20170000;year <= 20200000;year+=10000) {
//            for (long month = 100; month <= 1200; month += 100) {
//                for (long day = 1; day <= 27; day++) {
//                    generateData(String.valueOf(year + month + day),"yyyymmdd");
//                    System.out.println("生成成功："+String.valueOf(year + month + day));
//                }
//            }
//        }
//    }
//
//    @Test
//    public void generateDatabyTime() throws Exception{
//        LocalDateTime time = LocalDateTime.of(2020,1,1,7,0,0);
//        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
//        while (time.getHour()<=18){
//            time = time.plusSeconds(1);
////            generateData(df.format(time),"yyyymmddhh24miss");
//            generateDetail(df.format(time));
//        }
//    }
//
//
//
//    public static void generateData(String date,String format) throws Exception{
//        Connection connGet = null,connSet = null;
//        PreparedStatement ps = null;
//        ResultSet rs = getRs(connGet,ps);
//        List<List<String>> list = new ArrayList<>();
//        ResultSetMetaData md = rs.getMetaData();
//
//        int columnCount = md.getColumnCount();
//
//        while (rs.next()){
//            List<String> subList = new ArrayList<>();
//            for (int i = 1 ; i <= columnCount ; i ++){
//                subList.add(rs.getString(i));
//            }
//            list.add(subList);
//        }
//
//        for (List<String> subList : list){
//            int busi_count = Integer.parseInt(subList.get(10));
//            busi_count = RandomUtil.randomInt(Math.round(busi_count/2),Math.round(busi_count*2));
//            subList.set(10,String.valueOf(busi_count));
//            int amount_per = RandomUtil.randomInt(0, 2000);
//            int amount = amount_per*busi_count;
//
//            int low = busi_count>10?busi_count-10:1;
//            int cus_count = busi_count>low?RandomUtil.randomInt(low, busi_count):busi_count;
//            subList.add(String.valueOf(amount));
//            subList.add(String.valueOf(cus_count));
//            subList.add(date);
//        }
//
//        insert(connSet,ps,list,format);
//
//        IoUtil.close(connGet);
//        IoUtil.close(connSet);
//        IoUtil.close(rs);
//        IoUtil.close(ps);
//    }
//
//
//
//
//
//
//    public static ResultSet getRs(Connection conn , PreparedStatement ps){
//        ResultSet resultSet = null;
//        try {
//            Class.forName("oracle.jdbc.driver.OracleDriver");
//            conn = DriverManager.getConnection("jdbc:oracle:thin:@172.1.1.36:1521:orcl","sunaos","sunaos");
//            String sql = "select bloc_no,bloc_name,ZONE_NO,ZONE_NAME,BRANCH_NO,BRANCH_NAME,ORGAN_NO,ORGAN_NAME,SYS_NUM,SYS_NAME,count(TRANS_ID) \n" +
//                    "from MN_BUSI_DETAIL_V \n" +
//                    "GROUP BY bloc_no,bloc_name,ZONE_NO,ZONE_NAME,BRANCH_NO,BRANCH_NAME,ORGAN_NO,ORGAN_NAME,SYS_NUM,SYS_NAME";
//            ps = conn.prepareStatement(sql);
//            resultSet = ps.executeQuery();
//            return resultSet;
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//
//
//    }
//
//    public static void insert(Connection conn,PreparedStatement ps,List<List<String>> list,String format) throws Exception{
//        try {
//            Class.forName("oracle.jdbc.driver.OracleDriver");
//            conn = DriverManager.getConnection("jdbc:oracle:thin:@172.1.1.11:1521:orcl","sundap","123456");
//            String sql = "insert into DM_BUSI_RT_TB values (?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, '"+format+"'))";
//
//            for (List<String> s : list){
//                ps = conn.prepareStatement(sql);
//                for (int i = 1 ; i <= s.size() ; i++){
//                    ps.setString(i,s.get(i-1));
//                }
//                ps.execute();
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            IoUtil.close(conn);
//            IoUtil.close(ps);
//        }
//
//    }
//
//
//    @Test
//    public void setDetail(){
//        Connection conn = null;
//        PreparedStatement ps = null;
//        ResultSet resultSet = null;
//        try {
//            Class.forName("oracle.jdbc.driver.OracleDriver");
//            conn = DriverManager.getConnection("jdbc:oracle:thin:@172.1.1.36:1521:orcl", "sunaos", "sunaos");
//            String sql = "SELECT TASK_ID,ZONE_NO,ZONE_NAME,BRANCH_NO,BRANCH_NAME,ORGAN_NO,ORGAN_NAME,SYS_NUM,SYS_NAME,ROLE_NO,ROLE_NAME,OPER_NO,OPER_NAME,trunc(dbms_random.value(0,2)),trunc(dbms_random.value(0,10000)),'',trunc(dbms_random.value(0,20000)),OPER_TIME,TRANS_STATE FROM MN_BUSI_DETAIL_V ORDER BY oper_time";
//            ps = conn.prepareStatement(sql);
//            resultSet = ps.executeQuery();
//            conn = DriverManager.getConnection("jdbc:oracle:thin:@172.1.1.11:1521:orcl", "sundap", "123456");
//            sql = "insert into DM_BUSI_DETAIL_TB values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//            while (resultSet.next()){
//                ps = conn.prepareStatement(sql);
//                for (int i = 1;i <= 19 ; i++){
//                    ps.setObject(i,resultSet.getObject(i));
//                }
//                ps.execute();
//                DbUtil.close(ps);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            DbUtil.close(conn,ps,resultSet);
//        }
//
//    }
//
//    public static Connection getOracleConn(String url,String username,String password){
//        try {
//            Class.forName("oracle.jdbc.driver.OracleDriver");
//            return DriverManager.getConnection(url, username, password);
//        }catch (Exception e){
//            return null;
//        }
//    }
//
//
//    public static List<String[]> getSites(){
//        Connection conn = null;
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        List<String[]> list = new ArrayList<>();
//        String sql = "SELECT ZONE_NO,ZONE_NAME,BRANCH_NO,BRANCH_NAME,ORGAN_NO,ORGAN_NAME FROM MN_BUSI_DETAIL_V GROUP BY ZONE_NO,ZONE_NAME,BRANCH_NO,BRANCH_NAME,ORGAN_NO,ORGAN_NAME ORDER BY ZONE_NO,BRANCH_NO,ORGAN_NO";
//        try {
//            Class.forName("oracle.jdbc.driver.OracleDriver");
//            conn = DriverManager.getConnection("jdbc:oracle:thin:@172.1.1.36:1521:orcl", "sunaos", "sunaos");
//            ps = conn.prepareStatement(sql);
//            rs = ps.executeQuery();
//            while (rs.next()){
//                String[] rString = {rs.getString(1),rs.getString(2),rs.getString(3),
//                                    rs.getString(4),rs.getString(5),rs.getString(6)};
//                list.add(rString);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            DbUtil.close(conn,ps,rs);
//            return list;
//        }
//    }
//
//    public static List<String[]> getTypes(){
//        Connection conn = null;
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        List<String[]> list = new ArrayList<>();
//        String sql = "select BUSI_NO,BUSI_NAME from SM_BUSI_TYPE_TB";
//        try {
//            Class.forName("oracle.jdbc.driver.OracleDriver");
//            conn = DriverManager.getConnection("jdbc:oracle:thin:@172.1.1.11:1521:orcl", "sundap", "123456");
//            ps = conn.prepareStatement(sql);
//            rs = ps.executeQuery();
//            while (rs.next()){
//                String[] rString = {rs.getString(1),rs.getString(2)};
//                list.add(rString);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            DbUtil.close(conn,ps,rs);
//            return list;
//        }
//    }
//
//    public static void generateDetail(String busiDate){
//        List<String[]> sites = getSites();
//        List<String[]> types = getTypes();
//        String sql = "insert into DM_BUSI_DETAIL_TB values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,to_date(?,'yyyymmddhh24miss'),?)";
//        //业务量
//        int busiCount = RandomUtil.randomInt(0, 3);
//        Connection conn = getOracleConn("jdbc:oracle:thin:@172.1.1.11:1521:orcl", "sundap", "123456");
//        PreparedStatement ps = null;
//        try {
//            for (int i = 0; i < busiCount; i++){
//                ps = conn.prepareStatement(sql);
//                //系统流水号
//                ps.setString(1,String.valueOf(System.currentTimeMillis()*10+i));
//                //区域分行网点
//                int siteIndex = RandomUtil.randomInt(0, sites.size() - 1);
//                for (int j = 0 ; j < 6 ; j++){
//                    ps.setString(2+j,sites.get(siteIndex)[j]);
//                }
//                //渠道
//                int chRandom = RandomUtil.randomInt(0, 2);
//                if (chRandom == 0){
//                    ps.setString(8,"CH001");
//                    ps.setString(9,"柜面渠道");
//                }else {
//                    ps.setString(8,"CH002");
//                    ps.setString(9,"电子银行");
//                }
//                //岗位员工客户暂时跳过
//                for (int k = 10; k <= 16; k++){
//                    ps.setString(k,"");
//                }
//                //业务编号
//                int typeIndex = RandomUtil.randomInt(0,types.size()-1);
//                ps.setString(17,types.get(typeIndex)[0]);
//                ps.setString(18,types.get(typeIndex)[1]);
//                //交易金额
//                ps.setString(19,String.valueOf(RandomUtil.randomInt(1,10000)));
//                //创建日期
//                ps.setString(20,busiDate);
//                //业务状态
//                int judge = RandomUtil.randomInt(0, 10000);
//                String status;
//                if (judge>200){
//                    status = "5";
//                }else if (judge >10){
//                    status = "3";
//                }else {
//                    status = "4";
//                }
//                ps.setString(21,status);
//                ps.execute();
//                DbUtil.close(ps);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            DbUtil.close(conn,ps);
//        }
//
//
//    }

}

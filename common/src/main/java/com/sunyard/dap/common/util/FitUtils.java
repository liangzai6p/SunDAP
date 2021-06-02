package com.sunyard.dap.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * @program: SunDAP
 * @description: 预测工具类
 * @author: yey.he
 * @create: 2020-09-11 10:25
 **/public class FitUtils {
    //线性拟合 y=a+bx
    //<param name="X">X数组</param>
    //<param name="Y">Y数组</param>
    //<returns>a,b</returns>
    public static double[] lineFitting(double[] x, double[] y) {
        // X,Y -- X,Y两轴的坐标
        // A -- 结果参数
        double[]A = new double[2];
        int len = x.length;
        if (len <= 1) {
            return null;
        }

        double tmp = 0;
        int i, j, k;
        double Z, D1, D2, C, P, G, Q;
        double[] B = new double[len];
        double[] T = new double[len];
        double[] S = new double[len];

        for (i = 0; i < 2; i++) {
            A[i] = 0;
        }

        Z = 0;
        B[0] = 1;
        D1 = len;
        P = 0;
        C = 0;
        for (i = 0; i < len; i++) {
            P = P + x[i] - Z;
            C = C + y[i];
        }

        // 加除零判断
        if (D1 >= 0.0 && D1 <= 0.0) {
            return null;
        }

        C = C / D1;
        P = P / D1;
        A[0] = C * B[0];

        if (2 > 1) {
            T[1] = 1;
            T[0] = -P;
            D2 = 0;
            C = 0;
            G = 0;
            for (i = 0; i < len; i++) {
                Q = x[i] - Z - P;
                D2 = D2 + Q * Q;
                C = y[i] * Q + C;
                G = (x[i] - Z) * Q * Q + G;
            }

            // 加除零判断
            if (D2 >= 0.0 && D2 <= 0.0) {
                return null;
            }

            C = C / D2;
            P = G / D2;

            // 加除零判断
            if (D1 >= 0.0 && D1 <= 0.0) {
                return null;
            }

            Q = D2 / D1;
            D1 = D2;
            A[1] = C * T[1];
            A[0] = C * T[0] + A[0];

        }
        for (j = 2; j < 2; j++) {
            S[j] = T[j - 1];
            S[j - 1] = -P * T[j - 1] + T[j - 2];
            if (j >= 3) {
                for (k = j - 2; k >= 1; k--) {
                    S[k] = -P * T[k] + T[k - 1] - Q * B[k];
                }
            }
            S[0] = -P * T[0] - Q * B[0];
            D2 = 0;
            C = 0;
            G = 0;
            for (i = 0; i < len; i++) {
                Q = S[j];
                for (k = j - 1; k >= 0; k--) {
                    Q = Q * (x[i] - Z) + S[k];
                }

                D2 = D2 + Q * Q;
                C = y[i] * Q + C;
                G = (x[i] - Z) * Q * Q + G;

            }

            // 加除零判断
            if (D2 >= 0.0 && D2 <= 0.0) {
                return null;
            }

            C = C / D2;
            P = G / D2;

            // 加除零判断
            if (D1 >= 0.0 && D1 <= 0.0) {
                return null;
            }

            Q = D2 / D1;
            D1 = D2;
            A[j] = C * S[j];

            T[j] = S[j];
            for (k = j - 1; k >= 0; k--) {
                A[k] = C * S[k] + A[k];
                B[k] = T[k];
                T[k] = S[k];
            }
        }

        for (int nk = 0; nk < 2; nk++) {
            tmp = A[nk];
        }

        return A;
    }

    //多项式拟合------------------------------------begin
    //y=a+bx+cx*x+.....
    //<param name="d">次数</param>
    //<param name="X">X数组</param>
    //<param name="Y">Y数组</param>
    //<returns>a,b,c,....</returns>
    public static double[] polyFitting(int d, double[] X, double[] Y) {
        double[] C = new double[d+1];
        int order = d + 1;
        int rows = X.length;

        double[] Base = new double[order * rows];
        double[] alpha = new double[order * order];
        double[] alpha2 = new double[order * order];
        double[] beta = new double[order];

        try {
            // calc base
            for (int i = 0; i < order; i++) {
                for (int j = 0; j < rows; j++) {
                    int k = i + j * order;
                    Base[k] = i == 0 ? 1.0 : X[j] * Base[k - 1];
                }
            }

            // calc alpha2
            for (int i = 0; i < order; i++) {
                for (int j = 0; j <= i; j++) {
                    int k2 = 0;
                    int k3 = 0;
                    double sum = 0.0;
                    for (int k = 0; k < rows; k++) {
                        k2 = i + k * order;
                        k3 = j + k * order;
                        sum += Base[k2] * Base[k3];
                    }

                    k2 = i + j * order;

                    alpha2[k2] = sum;

                    if (i != j) {
                        k2 = j + i * order;
                        alpha2[k2] = sum;
                    }
                }
            }

            // calc beta
            for (int j = 0; j < order; j++) {
                double sum = 0;
                int k3 = 0;
                for (int k = 0; k < rows; k++) {
                    k3 = j + k * order;
                    sum += Y[k] * Base[k3];
                }

                beta[j] = sum;
            }

            // get alpha
            for (int j = 0; j < order * order; j++) {
                alpha[j] = alpha2[j];
            }

            // solve for params
            boolean bRes = polySolve(alpha, beta, order);

            for (int j = 0; j < order; j++) {
                C[j] = beta[j];
            }
            return C;
        } catch (Exception e) {
            for (int v = 0; v < C.length; v++) {
                C[v] = 0;
            }
            return null;
        }

    }

    private static boolean polySolve(double[] a, double[] b, int n) {
        for (int i = 0; i < n; i++) {
            // find pivot
            double mag = 0;
            int pivot = -1;

            for (int j = i; j < n; j++) {
                double mag2 = Math.abs(a[i + j * n]);
                if (mag2 > mag) {
                    mag = mag2;
                    pivot = j;
                }
            }

            // no pivot: error
            if (pivot == -1 || mag == 0) {
                return false;
            }

            // move pivot row into position
            if (pivot != i) {
                double temp;
                for (int j = i; j < n; j++) {
                    temp = a[j + i * n];
                    a[j + i * n] = a[j + pivot * n];
                    a[j + pivot * n] = temp;
                }

                temp = b[i];
                b[i] = b[pivot];
                b[pivot] = temp;
            }

            // normalize pivot row
            mag = a[i + i * n];
            for (int j = i; j < n; j++) {
                a[j + i * n] /= mag;
            }
            b[i] /= mag;

            // eliminate pivot row component from other rows
            for (int i2 = 0; i2 < n; i2++) {
                if (i2 == i) {
                    continue;
                }

                double mag2 = a[i + i2 * n];

                for (int j = i; j < n; j++) {
                    a[j + i2 * n] -= mag2 * a[j + i * n];
                }

                b[i2] -= mag2 * b[i];
            }
        }

        return true;
    }

    //多项式拟合------------------------------------end



    //求标准误差
    //<param name="values">values数组</param>
    //<returns>标准误差值</returns>
    public static double standardDeviation(double[] values) {
        if (values.length <= 1) {
            return 0;
        }
        double sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum += values[i];
        }
        double average = sum / values.length;
        double deviation = 0;
        for (int i = 0; i < values.length; i++) {
            deviation += Math.pow((values[i] - average), 2);
        }
        return Math.sqrt(deviation / (values.length - 1));
    }

    /**
     * 周线
     * @description: 预测工具类
     * @Author jie1.zheng
     * @Date 10:01 AM 2020/12/15
     **/
    //周线 y=a+bx
    //
    //<param name="Y">Y数组</param>
    //<returns>a,b</returns>
    public static double[] contourFitting(double[] y) {
        // X,Y -- X,Y两轴的坐标
        double[] result=new double[y.length];
        double count=0;
        double sum=0;
        for(int i=0;i<y.length;i++){
            if(i<7){
                for(int j=0;j<=i;j++){
                    sum+=y[j];
                    count++;
                }
                result[i]=sum/count;
                sum=0;
                count=0;
            }else{
                for(int j=i;j>i-7;j--){
                    sum+=y[j];
                }
                result[i]=sum/7;
                sum=0;
            }
        }
        return result;
    }

    /**
     * 月线
     * @description: 预测工具类
     * @Author jie1.zheng
     * @Date 10:01 AM 2020/12/15
     **/
    //月线
    //<param name="Y">Y数组</param>
    public static double[] onLineFitting(double[] y) {
        // X,Y -- X,Y两轴的坐标
        double[] result=new double[y.length];
        double count=0;
        double sum=0;
        for(int i=0;i<y.length;i++){
            if(i<30){
                for(int j=0;j<=i;j++){
                    sum+=y[j];
                    count++;
                }
                result[i]=sum/count;
                sum=0;
                count=0;
            }else{
                for(int j=i;j>i-30;j--){
                    sum+=y[j];
                }
                result[i]=sum/30;
                sum=0;
            }
        }
        return result;
    }

    /**
     * 年线
     * @description: 预测工具类
     * @Author jie1.zheng
     * @Date 10:01 AM 2020/12/15
     **/
    //年线
    //<param name="Y">Y数组</param>
    public static double[] annualLineFitting(double[] y) {
        // X,Y -- X,Y两轴的坐标
        double[] result=new double[y.length];
        double count=0;
        double sum=0;
        for(int i=0;i<y.length;i++){
            for(int j=0;j<=i;j++){
                sum+=y[j];
                count++;
            }
            result[i]=sum/count;
            sum=0;
            count=0;
        }
        return result;
    }

    /**
     * 日期转星期
     *
     * @param datetime
     * @return
     */
    public static int dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        int[] weekDays = { 7, 1, 2, 3, 4, 5, 6 };
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0){
            w = 0;
        }
        return weekDays[w];
    }

    /**
     * 获取两个日期之间的天数
     * @param startTime
     * @param endTime
     * @return
     */
    public static int getBetweenDays(String startTime , String endTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = null;
        Date end= null;
        try {
            start = sdf.parse(startTime);
            end = sdf.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long betweenDays = (end.getTime() - start.getTime()) / (1000L*3600L*24L);
        return betweenDays.intValue();
    }

    /**
     * 使用一次指数平滑法计算预测值
     *  yt+1'=a*yt+(1-a)*yt'     式中，
     *
     * •    yt+1'--t+1期的预测值，即本期（t期）的平滑值St ；
     *
     * •    yt--t期的实际值；
     *
     * •    yt'--t期的预测值，即上期的平滑值St-1 。
     * •    a-平滑常数，取值范围：(0-1)，可用日循环权值代替。常数越小平滑作用越强，默认为0.5。
     * @param arr 原始数据数组，至少大于三天的数据
     * @return
     */
    public static double[] forecast(double[] arr , double a , String startTime , String workDayCompensate , String endTime) throws Exception{
        if(arr.length <= 5){
             throw new Exception("传入数组长度不足！");
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = null;
            startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);
            if(startDate.compareTo(endDate) >= 0){
                throw new Exception("结束时间必须大于开始时间！");
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            //用于记录yt'即上一期预测平滑值,先计算算法中第一个预测值yt
            double s0 = Math.round((arr[0] + arr[1] + arr[2])/3);
            //用于记录yt+1，即预测平滑值
            double st = 0;
            //用于记录yt，即t期实际值
            double yt = 0;
            //周末总数值
            double weekData = 0;
            //周末天数
            double weekNum = 0;
            //周末平均值
            double weekAvg = 0;
            //计算周末平均值
            if(workDayCompensate.equals("1")) {
                for (int i = 0; i < arr.length; i++) {
                    if (cal.get(Calendar.DAY_OF_WEEK) == 1 || cal.get(Calendar.DAY_OF_WEEK) == 7) {
                        weekData += arr[i];
                        weekNum += 1;
                    }
                    cal.add(Calendar.DATE , 1);
                }
                weekAvg = Math.round(weekData / weekNum);
                //将日历重新设定为开始时间
                cal.setTime(startDate);
            }
            //预测平滑值数组,多预测一组数据，最后一位为预测值
            int betweenDays = FitUtils.getBetweenDays(startTime , endTime) + 1;
            //结果数组
            double[] s = new double[0];
            if((betweenDays - arr.length) > 1){
                s = new double[betweenDays];
            }else{
                s = new double[arr.length + 1];
            }
            if(workDayCompensate.equals("1")) {
                cal.add(Calendar.DATE , arr.length);
                if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
                    if(s.length < (arr.length + 2)) {
                        s = new double[arr.length + 2];
                    }
                    s0 = Math.round((arr[1] + arr[2] + arr[3])/3);
                    //s[arr.length + 1] = weekAvg;
                } else if (cal.get(Calendar.DAY_OF_WEEK) == 7) {
                    if(s.length < (arr.length + 3)) {
                        s = new double[arr.length + 3];
                    }
                    s0 = Math.round((arr[2] + arr[3] + arr[4])/3);
                    //s[arr.length + 2] = weekAvg;
                    //s[arr.length + 1] = weekAvg;
                }
                //将日历重新设定为开始时间
                cal.setTime(startDate);
            }
            s[0] = s0;
            /*//判断平滑常数是否为0或1，如果是则默认0.5计算
            if(a == 0 || a == 1){
                a = 0.5;
            }*/
            //平滑值从第二个开始计算
            for(int i = 1;i<arr.length;i++) {
                cal.add(Calendar.DATE , 1);
                if(workDayCompensate.equals("1")) {
                    if (cal.get(Calendar.DAY_OF_WEEK) == 1 || cal.get(Calendar.DAY_OF_WEEK) == 7) {
                        s[i] = weekAvg;
                    } else {
                        st = a * yt + (1 - a) * s0;
                        s0 = st;
                        yt = arr[i];
                        s[i] = Math.round(st);
                    }
                }else{
                    st = a * yt + (1 - a) * s0;
                    s0 = st;
                    yt = arr[i];
                    s[i] = Math.round(st);
                }
            }
            //已经预测了一个值，应该相应后移一天日期
            cal.add(Calendar.DATE , 1);
            double rate = 0;
            //移动指标，下面的循环需要移动几位
           int index = 0;
            //预测第一个值
            if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
                s[arr.length] = weekAvg;
                s[arr.length + 1] = Math.round(a * yt + (1 - a ) * st);
                rate = s[arr.length + 1] / arr[0];
                s[arr.length + 1] = weekAvg;
                index = 2;
                cal.add(Calendar.DATE , 1);
            } else if (cal.get(Calendar.DAY_OF_WEEK) == 7) {
                s[arr.length] = weekAvg;
                s[arr.length + 1] = weekAvg;
                s[arr.length + 2] = Math.round(a * yt + (1 - a ) * st);
                index = 3;
                rate = s[arr.length + 2] / arr[0];
                cal.add(Calendar.DATE , 2);
            }

            if(s.length > (arr.length + 1)) {
                for (int i = arr.length + index; i < s.length; i++) {
                    cal.add(Calendar.DATE, 1);
                    if (startDate.compareTo(endDate) < 0) {
                        if (workDayCompensate.equals("1")) {
                            if (cal.get(Calendar.DAY_OF_WEEK) == 1 || cal.get(Calendar.DAY_OF_WEEK) == 7) {
                                s[i] = weekAvg;
                            } else {
                                s[i] = Math.round(rate * s[i - arr.length]);
                            }
                        } else {
                            s[i] = Math.round(rate * s[i - arr.length]);
                        }
                    }else{
                        break;
                    }
                }
            }
            return s;
        }
    }


    /**
     * 在一次指数平滑法的基础上，使用二次指数平滑法计算预测值
     *  yt+1'=a*yt+(1-a)*yt'     式中，
     *  St = a*yt+(1-a)*St-1
     *  at = 2*yt - St
     *  bt = a/(1-a)*(yt - St)
     *  Ft = at + bt
     *  Ft即二次指数平滑值，即预测值
     * @param arr 一次指数平滑数组，至少大于三天的数据 ,a 平滑指数,startTime 预测开始时间，endTime 预测结束时间
     * @return
     */
    /*public static double[] forecast2(double[] arr , double a , String startTime , String endTime) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);

        int betweenDays = FitUtils.getBetweenDays(startTime , endTime);
        if(arr.length <= 5){
            throw new Exception("传入数组长度不足！");
        }else{
            double[] Ft = new double[arr.length + betweenDays];
            double bt = 0;
            double at = 0;
            //实际值
            double St2 = 0;
            //前一次计算值
            double St1 = 0;
            if(arr[0] < 0){
                if(arr[1] < 0){
                    St1 = arr[2];
                }else{
                    St1 = arr[1];
                }
            }else{
                St1 = arr[0];
            }
            //数组循环指数
            int index = 0;
            while(cal.getTime().compareTo(endDate) < 0){
                St2 = a*arr[index] +
            }
        }
    }*/

    /**
     * 计算两个时间之间相差天数
     * @param smdate
     * @param bdate
     * @return
     * @throws ParseException
     */
    public static int daysBetween(String smdate,String bdate) throws ParseException{

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance();

        cal.setTime(sdf.parse(smdate));

        long time1 = cal.getTimeInMillis();

        cal.setTime(sdf.parse(bdate));

        long time2 = cal.getTimeInMillis();

        long between_days=(time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));

    }

    /**
     * 周循环预测
     * @param arr 原始数据
     * @param startTime 原始数据开始时间
     * @param endTime 预测结束时间
     * @param workDay 工作日补偿开关 ：0 - 关闭；1 - 开启
     * @return
     * @throws Exception
     */
    public static double[] getWeekForecast(double[] arr , String startTime , String endTime , int workDay) throws Exception{
        if(arr.length < 7){
            throw new Exception("原始数据不足！");
        }else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = null;
                startDate = sdf.parse(startTime);
             Date endDate = sdf.parse(endTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            int betweenDays = FitUtils.getBetweenDays(startTime , endTime);
            double[] newArr = new double[arr.length + betweenDays];
            for (int i = 0; i < newArr.length; i++) {
                if(i < 7){
                    newArr[i] = 0;
                    cal.add(Calendar.DATE , 1);
                }else{
                    double count = 0;
                    if(i < arr.length) {
                        //计算前7天数据总和
                        for (int j = i; j > i - 7; j--) {
                            count += arr[j];
                        }
                    }else{
                        //计算前7天数据总和
                        for (int j = i; j > i - 7; j--) {
                            count += newArr[j];
                        }
                    }
                    //开启工作日补偿
                    if (workDay == 1) {
                        int week = cal.get(Calendar.DAY_OF_WEEK);
                        if(week == 1 || week == 7){
                            newArr[i] = 0;
                        }else{
                            newArr[i] = Math.round(count / 5);
                        }
                        cal.add(Calendar.DATE , 1);
                    } else {
                        newArr[i] = Math.round(count / 7);
                        cal.add(Calendar.DATE , 1);
                    }
                }
            }
            return newArr;
        }
    }

    /**
     * 月循环
     * @param arr 原始数据
     * @param startTime 原始数据开始时间
     * @param endTime 预测数据结束时间
     * @param workDay
     * @return
     * @throws Exception
     */
    public static double[] getMonthForecast(double[] arr , String startTime , String endTime , int workDay) throws Exception{
        if(arr.length < 30){
            throw new Exception("原始数据不足！");
        }else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = null;
            startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            int betweenDays = FitUtils.getBetweenDays(startTime , endTime);
            double[] newArr = new double[arr.length + betweenDays];
            for (int i = 0; i < newArr.length; i++) {
                if(i < 30){
                    newArr[i] = 0;
                    cal.add(Calendar.DATE , 1);
                }else{
                    double count = 0;
                    if(i < arr.length) {
                        //计算前30天数据总和
                        for (int j = i; j > i - 30; j--) {
                            count += arr[j];
                        }
                    }else{
                        int cha = i - arr.length;
                        //计算前30天数据总和
                        for (int j = i; j > i - 30; j--) {
                            count += newArr[j];
                        }
                    }
                    //开启工作日补偿
                    if (workDay == 1) {
                        int week = cal.get(Calendar.DAY_OF_WEEK);
                        if(week == 1 || week == 7){
                            newArr[i] = 0;
                        }else{
                            newArr[i] = Math.round(count / 22);
                        }
                        cal.add(Calendar.DATE , 1);
                    } else {
                        newArr[i] = Math.round(count / 22);
                        cal.add(Calendar.DATE , 1);
                    }
                }
            }
            return newArr;
        }
    }

    //获取工作日数据
    public static double[] getWorkDayArr(double[] arr , String startTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Calendar cal = Calendar.getInstance();
        try {
            startDate = sdf.parse(startTime);
            cal.setTime(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if(dayOfWeek == 0){
            dayOfWeek = 7;
        }
        //用于存储处理过的数据，将周末时的数据变为-1
        double[] newArr = new double[arr.length];

        for(int i = 0;i<arr.length;i++){
            if(dayOfWeek == 6 || dayOfWeek == 7){
                newArr[i] = 0;
            }else{
                newArr[i] = arr[i];
            }
            cal.add(Calendar.DATE,1);
            dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if(dayOfWeek == 0){
                dayOfWeek = 7;
            }
        }
        return newArr;
    }

    public static void main(String[] args){
        double[] arr = new double[4];
        double[] x = new double[arr.length];
        String startTime = "2020-12-21";
        String endTime = "2021-01-25";
        for(int i = 0;i<arr.length;i++){
            arr[i] = Math.round(Math.random()*1000);
            x[i] = i + 1;
        }
        double[] s = new double[arr.length];
        double[] newArr = new double[0];
        try {
            newArr = FitUtils.forecast(arr , 0.5 , startTime , "1" , endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        double[] month = new double[arr.length];
        double[] week = new double[arr.length];

        s = FitUtils.lineFitting(arr , x);

        System.out.println("斜率及截距：【");
        for(int i = 0;i<s.length;i++){
            System.out.println(", " + s[i]);
        }
        System.out.println("】");
        System.out.print("原始数据：[");
        for(int i = 0;i<arr.length;i++){
            System.out.print(arr[i] + ", ");
        }
        System.out.println("]");
        System.out.print("处理后的数据：[");
        for(int i = 0;i<newArr.length;i++){
            System.out.print(newArr[i] + ", ");
        }
        System.out.println("]");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = sdf.parse(startTime);
            endDate = sdf.parse(endTime);
        }catch (Exception e){

        }
        try {
            System.out.println(FitUtils.daysBetween(startTime , endTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    }

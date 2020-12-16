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
     * 月线
     * @description: 预测工具类
     * @Author jie1.zheng
     * @Date 10:01 AM 2020/12/15
     **/
    //月线
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
}

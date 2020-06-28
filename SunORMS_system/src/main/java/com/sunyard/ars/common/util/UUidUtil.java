package com.sunyard.ars.common.util;

import java.util.UUID;

/**
 * Created by bing on 2016/7/20.
 */
public class UUidUtil {

    public static String[] chars = new String[]
            {
                    "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
                    "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                    "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V","W", "X", "Y", "Z"
            };


    public static String getShortUuid()
    {
        StringBuffer stringBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
//        for (int i = 0; i < 8; i++)
//        {
//            String str      = uuid.substring(i * 4, i * 4 + 4);
//            int strInteger  = Integer.parseInt(str, 16);
//            stringBuffer.append(chars[strInteger % 0x3E]);
//        }

//        return stringBuffer.toString();
        return  uuid;
    }


    private static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return UUidBaseUtil.toString(hi | (val & (hi - 1)), UUidBaseUtil.MAX_RADIX)
                .substring(1);
    }

    /**
     * 以62进制（字母加数字）生成19位UUID，最短的UUID
     *
     * @return
     */
    public static String uuid() {
        UUID uuid = UUID.randomUUID();
        StringBuilder sb = new StringBuilder();
        sb.append(digits(uuid.getMostSignificantBits() >> 32, 8));
        sb.append(digits(uuid.getMostSignificantBits() >> 16, 4));
        sb.append(digits(uuid.getMostSignificantBits(), 4));
        sb.append(digits(uuid.getLeastSignificantBits() >> 48, 4));
        sb.append(digits(uuid.getLeastSignificantBits(), 12));
        return sb.toString();
    }




}

package com.sunyard.ars.common.util;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class LogUtil {
    /**
     * Log Forging漏洞校验
     * @param logs
     * @return
     */
    public static String vaildLog(String logs) {
        List<String> list=new ArrayList<String>();
        list.add("%0d");
        list.add("%0a");
        list.add("%0A");
        list.add("%0D");
        list.add("\r");
        list.add("\n");
        String normalize = Normalizer.normalize(logs, Normalizer.Form.NFKC);
        for (String str : list) {
            normalize=normalize.replace(str, "");
        }
        return normalize;
    }


    /**
     * 对输入源进行校验和过滤
     * @param input
     * @return
     */
    public static String filterInput(String input) {
        List<String> list=new ArrayList<String>();
        list.add("<");
        list.add(">");
        list.add("(");
        list.add(")");
        list.add("&");
        list.add("?");
        list.add(";");
        String normalize = Normalizer.normalize(input, Normalizer.Form.NFKC);
        for (String str : list) {
            normalize=normalize.replace(str, "");
        }
        return normalize;
    }
}

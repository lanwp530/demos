package com.lwp.example.plsql;

import java.util.List;

public class ListUtils {
    public static String join(List<String> list, String symbol) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder(128);
        for (String str : list) {
            sb.append(str).append(symbol);
        }
        sb.lastIndexOf(symbol);
        return sb.toString();
    }
}

package com.bittrex;

import java.util.*;

public class BittrexReply {
    boolean success;
    String message;
    List<Map<String, String>> result;

    public BittrexReply() {
        result = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map<String, String> map : result) {
            double balance = -1;
            if (map.containsKey("Balance")) {
                balance = Double.parseDouble(map.get("Balance"));
            }
            if (balance != 0) {
                Set<String> keyList = map.keySet();
                for (String key : keyList) {
                    sb.append("  " + key + ": " + map.get(key) + "\n");
                }
                sb.append("\n");
            }
        }
        return "Success = " + success + "\nMessage = " + message + "\n" + sb;
    }
}

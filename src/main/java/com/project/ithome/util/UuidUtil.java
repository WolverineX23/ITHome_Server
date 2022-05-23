package com.project.ithome.util;

import java.util.UUID;

public class UuidUtil {
    public static String getUuid(){
        StringBuilder sb = new StringBuilder();
        sb.append(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        return sb.toString();
    }
}

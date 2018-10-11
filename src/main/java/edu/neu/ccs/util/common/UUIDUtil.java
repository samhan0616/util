package edu.neu.ccs.util.common;

/**
 * Created by Administrator on 2018/3/2.
 */

public final class UUIDUtil {
    public UUIDUtil() {
    }

    public static String randomUUID10() {
        return RandomUtil.randomString(10);
    }

    public static String randomUUID() {
        return java.util.UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String randomUUID(int length) {
        String uuId = randomUUID();
        if(length > uuId.length()) {
            length = uuId.length();
        }

        return uuId.substring(0, length);
    }

    public static String randomUserId() {
        return RandomUtil.randomNumberString(10);
    }
}

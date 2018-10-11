package edu.neu.ccs.util.common;

/**
 * Created by Administrator on 2018/3/2.
 */
public class PasswordUtil {
    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_INTERATIONS = 1024;
    public static final int SALT_SIZE = 8;

    public PasswordUtil() {
    }

    public static String encryptPassword(String plainPassword) {
        byte[] salt = DigestUtil.generateSalt(8);
        byte[] hashPassword = DigestUtil.sha1(plainPassword.getBytes(), salt, 1024);
        return EncodeUtil.encodeHex(salt) + EncodeUtil.encodeHex(hashPassword);
    }

    public static boolean validatePassword(String plainPassword, String password) {
        byte[] salt = EncodeUtil.decodeHex(password.substring(0, 16));
        byte[] hashPassword = DigestUtil.sha1(plainPassword.getBytes(), salt, 1024);
        return password.equals(EncodeUtil.encodeHex(salt) + EncodeUtil.encodeHex(hashPassword));
    }
}

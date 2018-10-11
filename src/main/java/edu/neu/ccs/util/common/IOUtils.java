package edu.neu.ccs.util.common;

/**
 * Created by Administrator on 2018/3/2.
 */
import java.io.IOException;
import java.io.InputStream;

public class IOUtils extends org.apache.commons.io.IOUtils {
    public IOUtils() {
    }

    public static byte[] readFully(InputStream is) throws IOException {
        byte[] bytes = new byte[is.available()];
        readFully(is, bytes);
        return bytes;
    }
}

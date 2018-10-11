package edu.neu.ccs.util.common;

/**
 * Created by Administrator on 2018/3/2.
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ObjectUtil {
    public ObjectUtil() {
    }

    public static <T extends Serializable> T cloneSerializable(T obj) {
        ByteArrayOutputStream memoryBuffer = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        Serializable dest = null;

        try {
            out = new ObjectOutputStream(memoryBuffer);
            out.writeObject(obj);
            out.flush();
            in = new ObjectInputStream(new ByteArrayInputStream(memoryBuffer.toByteArray()));
            dest = (Serializable)in.readObject();
        } catch (Exception var9) {
            throw new RuntimeException(var9);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }

        return (T) dest;
    }

    public static <T> T clone(T obj) {
        String json = GsonUtil.t2Json(obj);
        Object dest = GsonUtil.fromJson(json, obj.getClass());
        return (T) dest;
    }

    public static <T extends Serializable> List<T> cloneSerializable(Collection<T> objList) {
        ArrayList copyList = new ArrayList();
        Iterator var2 = objList.iterator();

        while(var2.hasNext()) {
            Serializable obj = (Serializable)var2.next();
            copyList.add(cloneSerializable(obj));
        }

        return copyList;
    }
}

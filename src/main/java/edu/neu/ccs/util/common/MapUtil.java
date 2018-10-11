package edu.neu.ccs.util.common;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/2.
 */
public class MapUtil {
    public MapUtil() {
    }

    public static Map java2Map(Object javaBean) {
        HashMap map = new HashMap();

        try {
            BeanInfo e = Introspector.getBeanInfo(javaBean.getClass());
            PropertyDescriptor[] propertyDescriptors = e.getPropertyDescriptors();
            if(propertyDescriptors != null && propertyDescriptors.length > 0) {
                String propertyName = null;
                Object propertyValue = null;
                PropertyDescriptor[] var6 = propertyDescriptors;
                int var7 = propertyDescriptors.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    PropertyDescriptor pd = var6[var8];
                    propertyName = pd.getName();
                    if(!propertyName.equals("class")) {
                        Method readMethod = pd.getReadMethod();
                        propertyValue = readMethod.invoke(javaBean, new Object[0]);
                        map.put(propertyName, propertyValue);
                    }
                }
            }
        } catch (Exception var11) {
            var11.printStackTrace();
        }

        return map;
    }

    public static <T> T map2Java(T javaBean, Map map) {
        try {
            BeanInfo e = Introspector.getBeanInfo(javaBean.getClass());
            Object obj = javaBean.getClass().newInstance();
            PropertyDescriptor[] propertyDescriptors = e.getPropertyDescriptors();
            if(propertyDescriptors != null && propertyDescriptors.length > 0) {
                String propertyName = null;
                Object propertyValue = null;
                PropertyDescriptor[] var7 = propertyDescriptors;
                int var8 = propertyDescriptors.length;

                for(int var9 = 0; var9 < var8; ++var9) {
                    PropertyDescriptor pd = var7[var9];
                    propertyName = pd.getName();
                    if(map.containsKey(propertyName)) {
                        propertyValue = map.get(propertyName);
                        pd.getWriteMethod().invoke(obj, new Object[]{propertyValue});
                    }
                }

                return (T) obj;
            }
        } catch (Exception var11) {
            var11.printStackTrace();
        }

        return null;
    }

    public static <T> List<T> map2Java(T javaBean, List<Map> mapList) {
        if(mapList != null && !mapList.isEmpty()) {
            ArrayList objectList = new ArrayList();
            Object object = null;
            Iterator var4 = mapList.iterator();

            while(var4.hasNext()) {
                Map map = (Map)var4.next();
                if(map != null) {
                    object = map2Java(javaBean, map);
                    objectList.add(object);
                }
            }

            return objectList;
        } else {
            return null;
        }
    }

    public static final <E> E get(Map map, Object key, E defaultValue) {
        Object o = map.get(key);
        return o == null?defaultValue: (E) o;
    }
}

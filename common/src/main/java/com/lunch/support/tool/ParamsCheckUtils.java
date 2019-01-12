package com.lunch.support.tool;

import java.lang.reflect.Field;
import java.util.logging.Logger;

/**
 * 检查对象
 */
public class ParamsCheckUtils {
    static Logger logger = Logger.getLogger(ParamsCheckUtils.class.getSimpleName());

    /**
     * 检查对象里面的字段是否都有值
     *
     * @param object  需要检查的对象
     * @param excepts 除开的字段
     * @return
     */
    public static boolean check(Object object, String... excepts) {
        if (object == null) {
            logger.info("object is null");
            return false;
        }
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            for (String except : excepts) {
                if (field.getName().equals(except)) {
                    continue;
                }
            }

            try {
                Object obj = field.get(object);
                if (obj == null) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                logger.info("get field exception:" + e.getMessage());
                return false;
            }
        }
        return true;
    }
}

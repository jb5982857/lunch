package com.lunch.support.tool;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

/**
 * 日志工具类
 */
public class LogUtils {
    private static final String TAG = "lunch";
    private static Logger logger = Logger.getLogger("");

    public static void info(String msg) {
        info(4, msg);
    }

    private static void info(int index, String msg) {
        logger.info(getTag(index) + ":" + msg);
    }

    public static void printException(String msg, Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        info(4, msg + "\n" + writer.getBuffer().toString());
    }

    private static String getTag(int index) {
//        StringBuffer sb0 = new StringBuffer();
//        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
//            sb0.append(element.toString()).append("|");
//        }
//        logger.info("aaaaaaa:" + sb0.toString());
        StackTraceElement ele = Thread.currentThread().getStackTrace()[index];
        StringBuffer sb = new StringBuffer(TAG);
        control(sb, ele);
        return sb.toString();
    }

    private static void control(StringBuffer sb, StackTraceElement ele) {
        if (ele == null) {
            sb.append("no ele");
            return;
        }
        sb.append(": " + ele.getClassName());
        sb.append("." + ele.getMethodName());
        sb.append("(L:" + ele.getLineNumber() + ")");
    }
}

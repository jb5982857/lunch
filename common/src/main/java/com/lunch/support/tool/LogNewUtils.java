package com.lunch.support.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 基于logback的日志工具
 */
public class LogNewUtils {
    private static Logger logger = LoggerFactory.getLogger("lunch");

    public static void error(String msg) {
        logger.error(getTag(3) + msg);
    }

    public static void warn(String msg) {
        logger.warn(getTag(3) + msg);
    }

    public static void info(String msg) {
        logger.info(getTag(3) + msg);
    }

    public static void debug(String msg) {
        logger.info(getTag(3) + msg);
    }

    public static void trace(String msg) {
        logger.trace(getTag(3) + msg);
    }

    public static void printException(String msg, Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        error(msg + "\n" + writer.getBuffer().toString());
    }


    private static String getTag(int index) {
//        StringBuffer sb0 = new StringBuffer();
//        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
//            sb0.append(element.toString()).append("|");
//        }
//        logger.info("aaaaaaa:" + sb0.toString());
        StackTraceElement ele = Thread.currentThread().getStackTrace()[index];
        StringBuffer sb = new StringBuffer();
        control(sb, ele);
        return sb.append(":").toString();
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

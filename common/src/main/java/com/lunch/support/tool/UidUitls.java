package com.lunch.support.tool;

import java.text.*;
import java.util.Calendar;
import java.util.logging.Logger;

public class UidUitls {

    /**
     * The FieldPosition.
     */
    private static final FieldPosition HELPER_POSITION = new FieldPosition(0);

    /**
     * This Format for format the data to special format.
     */
    private final static Format DATA_FORMAT = new SimpleDateFormat("MMddHHmmssS");

    /**
     * This Format for format the number to special format.
     */
    private final static NumberFormat NUMBER_FORMAT = new DecimalFormat("0000");

    /**
     * This int is the sequence number ,the default value is 0.
     */
    private static int seq = 0;

    private static final int MAX = 9999;

    /**
     * 时间格式生成序列
     *
     * @return String
     */
    public static synchronized String generateSequenceNo(String username) {
        Calendar rightNow = Calendar.getInstance();
        StringBuffer sb = new StringBuffer();
        DATA_FORMAT.format(rightNow.getTime(), sb, HELPER_POSITION);
        NUMBER_FORMAT.format(seq, sb, HELPER_POSITION);
        if (seq == MAX) {
            seq = 0;
        } else {
            seq++;
        }
        String uid = MD5Utils.toMD5(sb.append(username).toString());
        LogNewUtils.info("username " + username + " is for uid " + uid);

        return uid;
    }
}

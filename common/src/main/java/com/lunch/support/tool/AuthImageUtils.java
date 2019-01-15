package com.lunch.support.tool;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 产生验证码图片的工具类
 */
public class AuthImageUtils {
    public static final String CODE = "code";
    public static final String IMAGE = "image";

    // 验证码图片的宽度。
    private static int width = 50;
    // 验证码图片的高度。
    private static int height = 20;
    // 验证码字符个数
    private static int codeCount = 4;
    private static int x = 0;
    // 字体高度
    private static int fontHeight;
    private static int codeY;
    static char[] codeSequence = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    //返回image_code
    //key "code" value （String）code
    //key "image" value BufferedImage
    public static Map<String, Object> getAuthImage() {
        Map<String, Object> result = new HashMap<>(2);
        initImage();
        // 定义图像buffer
        BufferedImage buffImg = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();
        // 创建一个随机数生成器类
        Random random = new Random();
        // 将图像填充为白色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        // 创建字体，字体的大小应该根据图片的高度来定。
        Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
        // 设置字体。
        g.setFont(font);
        // 画边框。
        g.setColor(Color.LIGHT_GRAY);
        g.drawRect(0, 0, width - 1, height - 1);
        // 随机产生10条干扰线，使图象中的认证码不易被其它程序探测到。
        g.setColor(Color.BLACK);
        for (int i = 0; i < 10; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }
        // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
        StringBuilder randomCode = new StringBuilder();
        int red = 0, green = 0, blue = 0;
        // 随机产生codeCount数字的验证码。
        for (int i = 0; i < codeCount; i++) {
            // 得到随机产生的验证码数字。
            String strRand = String.valueOf(codeSequence[random.nextInt(10)]);
            // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            // 用随机产生的颜色将验证码绘制到图像中。
            g.setColor(new Color(red, green, blue));
            g.drawString(strRand, (i + 1) * x, codeY);
            // 将产生的四个随机数组合在一起。
            randomCode.append(strRand);
        }
        result.put(IMAGE, buffImg);
        result.put(CODE, randomCode.toString());
        return result;
    }

    /**
     * 初始化验证图片属性
     */
    public static void initImage() {
        // 从web.xml中获取初始信息
        // 宽度
        String strWidth = "80";
        // 高度
        String strHeight = "30";
        // 字符个数
        String strCodeCount = "4";
        // 将配置的信息转换成数值
        try {
            if (strWidth != null && strWidth.length() != 0) {
                width = Integer.parseInt(strWidth);
            }
            if (strHeight != null && strHeight.length() != 0) {
                height = Integer.parseInt(strHeight);
            }
            if (strCodeCount != null && strCodeCount.length() != 0) {
                codeCount = Integer.parseInt(strCodeCount);
            }
        } catch (NumberFormatException e) {
        }
        x = width / (codeCount + 2);
        fontHeight = height - 2;
        codeY = height - 4;
    }
}

package com.moyear.neatgis.Utils;

import android.graphics.Color;

import java.util.Random;

public class ColorUtils {


    /**
     * 将int值的颜色值转换为十六进制的颜色
     *
     * @param color
     * @return
     */
    public static String toHexEncoding(int color) {
        String R, G, B;
        StringBuffer sb = new StringBuffer();

        R = Integer.toHexString(Color.red(color));
        G = Integer.toHexString(Color.green(color));
        B = Integer.toHexString(Color.blue(color));

        //判断获取到的R,G,B值的长度 如果长度等于1 给R,G,B值的前边添0
        R = R.length() == 1 ? "0" + R : R;
        G = G.length() == 1 ? "0" + G : G;
        B = B.length() == 1 ? "0" + B : B;

        sb.append("0x");
        sb.append(R);
        sb.append(G);
        sb.append(B);

        return sb.toString();
    }

    /**
     * 产生一个随机的int类的颜色
     *
     * @return
     */
    public static int randomColor() {
        int randomColor = -1;
        randomColor = Color.rgb(randomColorCode(), randomColorCode(), randomColorCode());

        return randomColor;
    }

    private static int randomColorCode() {
        int min = 0;
        int max = 255;

        Random r = new Random();
        int colorCode = r.nextInt(max - min + 1) + min;
        return colorCode;
    }


}

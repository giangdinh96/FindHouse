package com.giangdinh.returnnotfound.findhouse.Utils;

import android.text.Html;

import java.text.DecimalFormat;

/**
 * Created by GiangDinh on 08/02/2018.
 */

public class TextUtils {
    public static CharSequence formatHtml(String title, String content, String titleColor) {
        return Html.fromHtml("<font color='" + titleColor + "'>" + title + ":</font> " + content);
    }

    public static CharSequence formatHtml(String title, String content, String titleColor, String contentColor, boolean titleBold) {
        if (titleBold)
            return Html.fromHtml(String.format("<b><font color='%s'>%s</font></b>: <font color='%s'>%s</font>", titleColor, title, contentColor, content));
        return Html.fromHtml(String.format("<font color='%s'>%s</font>: <font color='%s'>%s</font>", titleColor, title, contentColor, content));
    }

    public static CharSequence formatHtmlNoti(String title, String content, String titleColor) {
        return Html.fromHtml("<b><font color='" + titleColor + "'>" + title + "</font></b> " + content);
    }

    public static String formatAddress(String fullAddress) {
        return fullAddress.replace("Tỉnh ", "")
                .replace("Thành phố ", "")
                .replace("Quận ", "")
                .replace("Huyện ", "")
                .replace("Thị xã ", "")
                ;
    }

    public static String formatPrice(long price) {
        DecimalFormat decimalFormat = new DecimalFormat(",###");
        return decimalFormat.format(price) + " VND";
    }

    public static String formatPrice(String price) {
        long priceNumber = Long.parseLong(price);
        DecimalFormat decimalFormat = new DecimalFormat(",###");
        return decimalFormat.format(priceNumber) + " VND";
    }

    public static String formatStretch(double stretch) {
        DecimalFormat decimalFormat = new DecimalFormat(".#");
        String relsult = decimalFormat.format(stretch);
        if (relsult.endsWith(",0")) {
            relsult = relsult.substring(0, relsult.length() - 2);
        }
        relsult = relsult + " m2";
        return relsult;
    }

    public static String formatStretch(String stretch) {
        double stretchNumber = Double.parseDouble(stretch);
        DecimalFormat decimalFormat = new DecimalFormat(".#");
        String relsult = decimalFormat.format(stretchNumber);
        if (relsult.endsWith(",0")) {
            relsult = relsult.substring(0, relsult.length() - 2);
        }
        relsult = relsult + " m2";
        return relsult;
    }
}
package rx.android.samples;
/**
 * Created by relicemxd on 15/12/15.
 */

import android.content.Context;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Leon.Zhu on 2015/9/7.
 * <p/>
 * 功能 :字符串的工具类
 */
public class Utils {

    /**
     * 返回 一个格式化的字符串，设置最大长度，超出 则用 moreStr 代替
     * <p/>
     * 例如
     * trimString(“imageview”, 3, "...")  ==> ima...
     *
     * @param src      字符串
     * @param maxLenth 最大长度
     * @param moreStr  代替超过长度的字符
     * @return
     */
    public static String trimString(String src, int maxLenth, String moreStr) {
        if (TextUtils.isEmpty(src)) {
            return "";
        }
        if (src.length() <= maxLenth) {
            return src;
        }
        if (src.length() > maxLenth) {
            return src.substring(0, maxLenth - 1) + moreStr;
        }
        return src;

    }

    /**
     * 对String.xml  字符 的格式化替换
     * <p/>
     * 例如   <string name="brand_review_satisfaction_ok">  一般(%1$d)  </string>   ==>  一般(30)
     *
     * @param stringId
     * @param args
     * @return
     */
    public static String formatResourceString(Context ctx, int stringId, Object... args) {
        if (ctx.getResources().getString(stringId) != null && args != null) {
            try {
                String resourceString = ctx.getResources().getString(stringId);
                return String.format(resourceString, args);
            } catch (Exception e) {
                return "";
            }

        }
        return "";
    }

    /**
     * 获取 简单的 GETURL的 参数  简单的情况 ，通常 是一个key 对应一个 字符串，不会在嵌套一个 url的 且包含& 的符号
     *
     * @param getURL xiuApp://xiu.app.topicgoodslist/openwith?id=13212122&name=阿迪达斯热卖
     * @param key
     * @return 返回 字符value 字符串
     */
    public static String getSimpleGetUrlParamValue(String getURL, String key) {
        /*不存在？ 不是正常的 get请求地址*/
        if (TextUtils.isEmpty(getURL) || !getURL.contains("?")) {
            return "";
        }
        String keyparm = key + "=";
        if (getURL.indexOf(keyparm) > -1) {
            String[] parts = getURL.split(key + "=");
            if (parts != null && parts.length == 2) {
                String valuePart = parts[1];
                //检查是否包含下一个参数，如果没有，就是整个，如果有 截取 ，返回前一段
                if (valuePart.indexOf("&") > -1) {
                    return valuePart.split("&")[0];
                } else {
                    //只有一个参数
                    return valuePart;
                }

            }
        }

        return "";
    }



    public static String byte2HexString(byte[] b) {
        int i;
        StringBuffer buf = new StringBuffer("");
        for (int offset = 0; offset < b.length; offset++) {
            i = b[offset];
            if (i < 0)
                i += 256;
            if (i < 16)
                buf.append("0");
            buf.append(Integer.toHexString(i));
        }
        return buf.toString();

    }

    /**
     * 检测邮箱格式
     *
     * @param emailAddress
     */
    public static boolean checkEmail(String emailAddress) {
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\." +
                                          "[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(emailAddress);
        return matcher.matches();
    }

    /**
     * 控制显示原价
     *
     * @param newPrice
     * @param oldPrice
     * @return
     */
    public static boolean comparePrice2Hide(String newPrice, String oldPrice) {
        double np = Double.parseDouble(newPrice);
        double op = Double.parseDouble(oldPrice);
        if (op <= 0 || op >= np) {
            return true;
        } else
            return false;
    }

    /**
     * 价格统一base方法:
     * 格式化价格字符串,去掉小数点后所有数字
     *
     * @param price
     * @return String 返回整数
     */
    public static String formatPrice(String price) {
        if (!price.endsWith(".00") && !price.endsWith(".0")) {
            return price;
        }
        if (price.contains(".")) {
            String endstring = price.substring(price.indexOf("."), price.length());
            double decPo = Double.parseDouble(endstring);
            if (decPo > 0) {//说明价格是小与万元的,产品说不会有分的情况
                return price;
            } else {//包含.0和.00情况直接过滤
                return price.substring(0, price.indexOf("."));
            }
        } else
            return price;
    }

    /**
     * 格式化价格字符串，如果小数点后为0或00直接去掉
     *
     * @param price
     * @return String 返回整数
     */
    public static String formatPriceToZ(String price) {
        return formatPrice(price);
    }

    /**
     * 格式化汽车类型价格 for；10000.00 -> 1万
     *
     * @param price
     * @return
     */
    public static String formatCarPrice(String price) {
        double p = Double.parseDouble(price);
        String tmp = String.valueOf(p / 10000);
        return  formatPrice(tmp)+"万";
    }


    /**
     * CPS格式化价格字符串，清除小数后面带0情况
     * 如36.10 -> 36.1
     *
     * @param price
     * @return String 返回整数
     */
    public static String formatPriceToNum(String price) {
        if (price.contains(".")) {
            if (price.endsWith(".00") || price.endsWith(".0"))
                return price.substring(0, price.indexOf("."));
            else {
                double p = Double.parseDouble(price);
                String tmp = String.valueOf(p / 1.0);
                return tmp;
            }
        } else {
            return price;
        }
    }

    /**
     * 格式化商品详情价格大于10W处理 for；106300.00 -> 10.63万
     *
     * @param price
     * @return
     */
    public static String formatGoodsDetailPrice(String price) {
        String substring = null;
        if (price.contains(".")) {
            String endstring = price.substring(price.indexOf("."), price.length());
            double decPo = Double.parseDouble(endstring);
            if (decPo > 0) {
                return price;
            } else {
                substring = price.substring(0, price.indexOf("."));
            }
        } else
            substring = price;
        if (substring.length() >= 6) {
            double p = Double.parseDouble(price);
            String tmp = String.valueOf(p / 10000.0);
            return tmp + "万";
        } else {
            return formatPriceToZ(price);
        }
    }
}

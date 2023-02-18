package com.java110.job.printer.manufactor;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrintUtil4 {
    static PrintUtil4 p = new PrintUtil4();
    public static final String URL = "http://api.feieyun.cn/Api/Open/";// 不需要修改
    public static final String USER = "547974317@qq.com";// *必填*：飞鹅云后台注册账号
    public static final String UKEY = "cr5ZJZEksthQpUgz";// *必填*: 飞鹅云后台注册账号后生成的UKEY 【备注：这不是填打印机的KEY】
    public static final String SN = "930531498";// *必填*：打印机编号，必须要在管理后台里添加打印机或调用API接口添加之后，才能调用API

    public static void main(String[] args) {
//    ======================1.多个打印机同时打印======================================
//    List<String> list = new ArrayList<>();
//    list.add(SN);
//    list.add(SN2);
//    for (String sn : list) {
//      String method1 = p.print(sn);
//      System.out.println(method1);
//    }

//    ======================2单个打印机打印======================================
    String result = p.print(SN);
    System.out.println(result);

//    p.writeFile("E:/retlog.txt", result);
//    System.out.println("返回json数据已保存至 E:/retlog.txt 文件,有需要请查看");

    }
    // =====================打印订单排版Demo==========================
    private static String print(String sn) {
//    =====================1.字体大小效果测试=====================================
//    String s1 = "<C><DB>放大两倍</DB></C><BR>";
//    String s2 = "<C><B>放大一倍</B></C><BR>";
//    String s3 = "<C><L>变高一倍</L></C><BR>";
//    String s4 = "<C><W>变宽一倍</W></C><BR>";
//    String s5 = "<C><BOLD>字体加粗</BOLD></C><BR>";
//    String s6 = "<C>默认不加标签最小效果</C><BR>";
//    String s7 = "<C><L><BOLD>变高一倍加粗</BOLD></L></C><BR>";
//    String s8 = "<C><W><BOLD>变宽一倍加粗</BOLD></W></C><BR>";
//
//    String content = s1+s2+s3+s4+s5+s6+s7+s8;
//    =====================1.end===================================

//    =====================2.字体行间距测试======================================
//    String content = "<CB>飞鹅云测试</CB><BR>";
//    content += "名称           单价  数量 金额<BR>";
//    content += "--------------------------------<BR>";
//    content += "鸡蛋炒饭1　　　100.0  1 100.0<BR>";
//    content += "鸡蛋炒饭2　　　100.0  2 200.0<BR>";
//    content += "鸡蛋炒饭3　　　100.0  3 300.0<BR>";
//    byte[] spaces = new byte[3];
//    spaces[0] = 0x1b;
//    spaces[1] = 0x33;
//    spaces[2] = 0x30;//7f => 50   行距距离设置最小值为\x50 最大值为\x7f
//    String ls = new String(spaces);//行距开始
//    byte[] spacee = new byte[2];
//    spacee[0] = 0x1b;
//    spacee[1] = 0x32;
//    String le = new String(spacee);//行距结束
//    content += ls+content+le;
//    =====================2.end======================================

//    =====================3.字体大小测试===========================================
//    String content = "鸡蛋炒饭 数量:1 单价:100.0 总额:100.0<BR>";
//    byte[] start = new byte[3];
//    start[0] = 0x1d;
//    start[1] = 0x21;
//    start[2] = 0x11;//7f => 50   行距距离设置最小值为\x50 最大值为\x7f
//    String ls = new String(start);//行距开始
//    byte[] end = new byte[4];
//    end[0] = 0x0d;
//    end[1] = 0x0a;
//    end[2] = 0x1b;
//    end[3] = 0x40;
//    String le = new String(end);//行距结束
//    content += ls+content+le;
//    =====================3.end=======================================


//    *********************4.排版测试*******************************************************************
        Order order1 = new Order("荣耀 畅玩7 2+16G(全网通)-黑(爱施德集采)/Android", "100.4", "10");
        Order order2 = new Order("小蘑菇音乐铃 JSN-3022 -文创 UP+", "10.3", "10");
        Order order3 = new Order("功夫小子 手机座 JSN-1002-文创 UP+", "10.5", "10");
        Order order4 = new Order("zsfhjksgh菜名四dk", "10.0", "8");
        Order order5 = new Order("zsfhjksghd菜名五hjk", "100.2", "8");

        List<Order> orderList = new ArrayList<>();
        orderList.add(order1);
        orderList.add(order2);
        orderList.add(order3);
        orderList.add(order4);
        orderList.add(order5);
        String content = p.getOrder(orderList, 14, 6, 3, 6);//orderList为数组 b1代表名称列占用（14个字节）  b2单价列（6个字节） b3数量列（3个字节） b4金额列（6个字节）-->这里的字节数可按自己需求自由改写，14+6+3+6再加上代码写的3个空格就是32了，58mm打印机一行总占32字节

        // 通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)// 读取超时
                .setConnectTimeout(30000)// 连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        HttpPost post = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user", USER));
        String STIME = String.valueOf(System.currentTimeMillis() / 1000);
        nvps.add(new BasicNameValuePair("stime", STIME));
        nvps.add(new BasicNameValuePair("sig", p.signature(USER, UKEY, STIME)));
        nvps.add(new BasicNameValuePair("apiname", "Open_printMsg"));// 固定值,不需要修改
        nvps.add(new BasicNameValuePair("sn", sn));
        nvps.add(new BasicNameValuePair("content", content));
        nvps.add(new BasicNameValuePair("times", "1"));// 打印联数

        CloseableHttpResponse response = null;
        String result = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if (statecode == 200) {
                HttpEntity httpentity = response.getEntity();
                if (httpentity != null) {
                    // 服务器返回的JSON字符串，建议要当做日志记录起来
                    result = EntityUtils.toString(httpentity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            p.close(response, post, httpClient);
        }
        return result;
    }

    public void writeFile(String path,String content) {
        content = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒").format(new Date()) +",保存的订单日志信息为: "+content;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path, true);
            fos.write(content.getBytes());
            fos.write("\r<BR>".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(fos != null){
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String signature(String USER, String UKEY, String STIME) {
        return DigestUtils.sha1Hex(USER + UKEY + STIME);
    }


    //飞鹅技术支持
    //#########################################################################################################

    //进行订单的多列排版demo，实现商品超出字数的自动换下一行对齐处理，同时保持各列进行对齐

    //排版原理是统计字符串字节数，补空格换行处理

    //58mm的机器,一行打印16个汉字,32个字母;80mm的机器,一行打印24个汉字,48个字母

    //#########################################################################################################

    //orderList为数组  b1代表名称列占用字节  b2单价列 b3数量列 b4金额列-->这里的字节数可按自己需求自由改写，详细往上看112行调用实际例子运用
    public static String getOrder(List<Order> orderList, int b1, int b2, int b3, int b4) {
        String orderInfo = "<CB>飞鹅云测试</CB><BR>";
        orderInfo += "名称           单价  数量 金额<BR>";
        orderInfo += "--------------------------------<BR>";
        double totals = 0.0;
        for (int i = 0; i < orderList.size(); i++) {
            String title = orderList.get(i).getTitle();
            String price = orderList.get(i).getPrice();
            String num = orderList.get(i).getNum();
            String total = "" + Double.valueOf(price) * Integer.parseInt(num);
            totals += Double.parseDouble(total);
            price = p.addSpace(price, b2);
            num = p.addSpace(num, b3);
            total = p.addSpace(total, b4);
            String otherStr =  " " + price + num+ " " + total;

            int tl = 0;
            try {
                tl = title.getBytes("GBK").length;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            int spaceNum = (tl / b1 + 1) * b1 - tl;
            if (tl < b1) {
                for (int k = 0; k < spaceNum; k++) {
                    title += " ";
                }
                title += otherStr;
            } else if (tl == b1) {
                title += otherStr;
            } else {
                List<String> list = null;
                if (p.isEn(title)) {
                    list = p.getStrList(title, b1);
                } else {
                    list = p.getStrList(title, b1 / 2);
                }
                String s0 = p.titleAddSpace(list.get(0));
                title = s0 + otherStr + "<BR>";// 添加 单价 数量 总额
                String s = "";
                for (int k = 1; k < list.size(); k++) {
                    s += list.get(k);
                }
                try {
                    s = p.getStringByEnter(b1, s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                title += s;
            }
            orderInfo += title + "<BR>";
        }
        orderInfo += "--------------------------------<BR>";
        orderInfo += "合计：" + totals + "元<BR>";
        orderInfo += "送货地点：广州市南沙区xx路xx号<BR>";
        orderInfo += "联系电话：020-39004606<BR>";
        orderInfo += "订餐时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "<BR>";
        orderInfo += "备注：加辣<BR>";
        orderInfo += "<QR>http://www.feieyun.com</QR>";
        return orderInfo;
    }

    public String titleAddSpace(String str) {
        int k=0;
        int b = 14;
        try {
            k = str.getBytes("GBK").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < b-k; i++) {
            str += " ";
        }
        return str;
    }

    public static String getStringByEnter(int length, String string) throws Exception {
        for (int i = 1; i <= string.length(); i++) {
            if (string.substring(0, i).getBytes("GBK").length > length) {
                return string.substring(0, i - 1) + "<BR>" + getStringByEnter(length, string.substring(i - 1));
            }
        }
        return string;
    }

    public static String addSpace(String str, int size) {
        int len = str.length();
        if (len < size) {
            for (int i = 0; i < size - len; i++) {
                str += " ";
            }
        }
        return str;
    }

    public static Boolean isEn(String str) {
        Boolean b = false;
        try {
            b = str.getBytes("GBK").length == str.length();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return b;
    }

    public static List<String> getStrList(String inputString, int length) {
        int size = inputString.length() / length;
        if (inputString.length() % length != 0) {
            size += 1;
        }
        return getStrList(inputString, length, size);
    }

    public static List<String> getStrList(String inputString, int length, int size) {
        List<String> list = new ArrayList<String>();
        for (int index = 0; index < size; index++) {
            String childStr = substring(inputString, index * length, (index + 1) * length);
            list.add(childStr);
        }
        return list;
    }

    public static String substring(String str, int f, int t) {
        if (f > str.length())
            return null;
        if (t > str.length()) {
            return str.substring(f, str.length());
        } else {
            return str.substring(f, t);
        }
    }

    public static void close(CloseableHttpResponse response, HttpPost post, CloseableHttpClient httpClient) {
        try {
            if (response != null) {
                response.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            post.abort();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

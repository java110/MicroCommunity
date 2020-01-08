package com.java110.core.factory;

import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.util.StringUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Function: 支付工厂类 <br/>
 * date: 2018-01-18 <br/>
 *
 * @author att
 * @version 1.0
 * @see
 * @since JDK1.8
 */
public class PaymentFactory {

    static Logger log = LogManager.getLogger(PaymentFactory.class);

    /**
     * 获取当前机器的ip
     *
     * @return String
     */
    public static String getLocalIp() {
        InetAddress ia = null;
        String localip = null;
        try {
            ia = ia.getLocalHost();
            localip = ia.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localip;

    }

    /**
     * Map转换为 Xml
     *
     * @return Xml
     * @throws Exception
     */
    public static String mapToXml(SortedMap<String, String> map) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
//防止XXE攻击
        documentBuilderFactory.setXIncludeAware(false);
        documentBuilderFactory.setExpandEntityReferences(false);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        org.w3c.dom.Document document = documentBuilder.newDocument();
        org.w3c.dom.Element root = document.createElement("xml");
        document.appendChild(root);
        for (String key : map.keySet()) {
            String value = map.get(key);
            if (value == null) {
                value = "";
            }
            value = value.trim();
            org.w3c.dom.Element filed = document.createElement(key);
            filed.appendChild(document.createTextNode(value));
            root.appendChild(filed);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String output = writer.getBuffer().toString();
        try {
            writer.close();
        } catch (Exception ex) {
        }
        return output;
    }


    /**
     * 创建签名Sign
     *
     * @param key
     * @param parameters
     * @return
     */
    public static String createSign(SortedMap<String, String> parameters, String key) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator<?> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            if (entry.getValue() != null || !"".equals(entry.getValue())) {
                String v = String.valueOf(entry.getValue());
                if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                    sb.append(k + "=" + v + "&");
                }
            }
        }
        sb.append("key=" + key);
        String sign = AuthenticationFactory.md5(sb.toString()).toUpperCase();
        return sign;
    }


    /**
     * XML转换为Map
     *
     * @param strXML
     * @return Map
     * @throws Exception
     */
    public static Map<String, Object> getMapFromXML(String strXML) throws Exception {
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
//防止XXE攻击
            documentBuilderFactory.setXIncludeAware(false);
            documentBuilderFactory.setExpandEntityReferences(false);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
            org.w3c.dom.Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            try {
                stream.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return data;
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * 生成随机数
     *
     * @return
     */
    public static String makeUUID(int len) {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, len);
    }

    /**
     * 获取当前的Timestamp
     *
     * @return
     */
    public static String getCurrentTimeStamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    /**
     * 获取当前的时间
     *
     * @return
     */
    public static long getCurrentTimestampMs() {
        return System.currentTimeMillis();
    }

    /**
     * 生成订单号
     *
     * @return
     */
    public static String generateOrderNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        return sdf.format(new Date()) + makeUUID(16);
    }

    /**
     * 获取当前工程url
     *
     * @param request
     * @return
     */
    public static String getCurrentUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    /**
     * Xml字符串转换为Map
     *
     * @param xmlStr
     * @return
     */
    public static Map<String, String> xmlStrToMap(String xmlStr) {
        Map<String, String> map = new HashMap<String, String>();
        Document doc;
        try {
            doc = DocumentHelper.parseText(xmlStr);
            Element root = doc.getRootElement();
            List children = root.elements();
            if (children != null && children.size() > 0) {
                for (int i = 0; i < children.size(); i++) {
                    Element child = (Element) children.get(i);
                    map.put(child.getName(), child.getTextTrim());
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static String getSceneInfo(String wapUrl, String name) {
        Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
        if (!StringUtils.isEmpty(wapUrl) && !StringUtils.isEmpty(name)) {
            /*{"h5_info": {"type":"Wap","wap_url": "https://pay.qq.com","wap_name": "腾讯充值"}}*/
            Map<String, String> childmap = new TreeMap<String, String>();
            childmap.put("type", "Wap");
            childmap.put("wap_url", wapUrl);
            childmap.put("wap_name", name);
            map.put("h5_info", childmap);
            return JSONObject.toJSONString(map);
        }
        return null;
    }

    /**
     * 转换金额型到整型
     *
     * @param money
     * @return
     */
    public static String moneyToIntegerStr(Double money) {
        BigDecimal decimal = new BigDecimal(money);
        int amount = decimal.multiply(new BigDecimal(100))
                .setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        return String.valueOf(amount);
    }

    /**
     * 除去数组中的空值和签名参数
     *
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
                    || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

    /**
     * 根据不同环境生成支付金额
     *
     * @param env
     * @param money
     * @return
     */
    public static double getPayAmountByEnv(String env, Double money) {
        double pay_money = 0.01;
//测试环境
        if ("ON".equals(env)) {
            if (money > 10000) {
                pay_money = 0.03;
            } else if (money > 1000) {
                pay_money = 0.02;
            } else {
                pay_money = 0.01;
            }
            return pay_money;
        } else {
//生成环境
            return money;
        }
    }
}
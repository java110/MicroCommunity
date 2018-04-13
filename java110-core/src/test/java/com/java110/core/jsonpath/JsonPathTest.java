package com.java110.core.jsonpath;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import org.junit.Test;

/**
 * Created by wuxw on 2017/9/13.
 */
public class JsonPathTest {

    @Test
    public void test() throws Exception {
       /* stringRedisTemplate.opsForValue().set("aaa", "111");
        Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));*/
    }

    @Test
    public void testObj() throws Exception {
        String  req = "{\n" +
                "    \"ContractRoot\": {\n" +
                "        \"SvcCont\": {\n" +
                "\t \"CustOrderInfo\": {\n" +
                "                \"CustId\": \"713005098855\",\n" +
                "                \"StaffCode\": \"QH1001\",\n" +
                "                \"ChannelNbr\": \"6301041000000\",\n" +
                "                \"RegionCode\": \"8630101\",\n" +
                "                \"OrderId\": \"No Requsted\",\n" +
                "\"AttrInfos\":[\n" +
                "{\n" +
                "\"AttrSpecId\":\"40020005\",\n" +
                "\"AttrValue\":\"\",\n" +
                "\"AttrType\":\"1\"\n" +
                "},\n" +
                "{\n" +
                "\"AttrSpecId\":\"40020006\",\n" +
                "\"AttrValue\":\"\",\n" +
                "\"AttrType\":\"1\"\n" +
                "},\n" +
                "{\n" +
                "\"AttrSpecId\":\"40020007\",\n" +
                "\"AttrValue\":\"\",\n" +
                "\"AttrType\":\"1\"\n" +
                "}\n" +
                "]\n" +
                "            },\n" +
                "            \"ProdOfferInfo\": [\n" +
                "                {\n" +
                "                    \"ProdOfferNbr\": \"800009346\",\n" +
                "                    \"ProdOfferInstId\": \"-1\",\n" +
                "                    \"Action\": \"ADD\",\n" +
                "\"startDt\":\"20170426094245\",\n" +
                "\"endDt\":\"30000000000000\",\n" +
                "                    \"AttrInfos\": [{\n" +
                "\"AttrSpecId\":\"800009346\",\n" +
                "\"AttrValue\":\"800009346\"\n" +
                "}],\n" +
                "                    \"OfferProdRelInfo\": [\n" +
                "                        {\n" +
                "                            \"ProdInstId\": \"713030122316\",\n" +
                "                            \"RoleCd\": \"7006\"\n" +
                "                        }\n" +
                "                    ]\n" +
                "                }\n" +
                "            ]  \n" +
                "        },\n" +
                "        \"TcpCont\": {\n" +
                "            \"AppKey\": \"1001000102\",\n" +
                "            \"DstSysID\": \"6004050001\",\n" +
                "            \"Method\": \"order.offer.addprodoffer\",\n" +
                "            \"Sign\": \"123\",\n" +
                "            \"Version\": \"1.0\",\n" +
                "            \"TransactionID\": \"6001000102201703250000014160\",\n" +
                "            \"ReqTime\": \"20170325115245788\"\n" +
                "        }\n" +
                "    }\n" +
                "}";

        Object obj = JSONPath.eval(JSONObject.parseObject(req),"$.ContractRoot.SvcCont.ProdOfferInfo.AttrInfos[AttrSpecId not in ('40020005','40020006','40020007')]");
        System.out.println(obj.toString());
    }
}

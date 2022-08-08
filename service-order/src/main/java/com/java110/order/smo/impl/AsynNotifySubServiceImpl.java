package com.java110.order.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.client.RestTemplate;
import com.java110.core.context.Environment;
import com.java110.core.context.SecureInvocation;
import com.java110.dto.businessDatabus.BusinessDatabusDto;
import com.java110.dto.businessTableHis.BusinessTableHisDto;
import com.java110.dto.order.OrderDto;
import com.java110.dto.order.OrderItemDto;
import com.java110.entity.order.Business;
import com.java110.intf.job.IDataBusInnerServiceSMO;
import com.java110.order.dao.ICenterServiceDAO;
import com.java110.order.smo.IAsynNotifySubService;
import com.java110.utils.cache.DatabusCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.DomainContant;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AsynNotifySubServiceImpl implements IAsynNotifySubService {
    private static Logger logger = LoggerFactory.getLogger(AsynNotifySubServiceImpl.class);
    //databus 业务类型
    private static final String DATABUS_SWITCH = "DATABUS_SWITCH";
    private static final String DATABUS_SWITCH_ON = "ON"; // 开关打开
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private ICenterServiceDAO centerServiceDAOImpl;

    @Autowired
    private IDataBusInnerServiceSMO dataBusInnerServiceSMOImpl;

    public static final String FALLBACK_URL = "http://SERVICE_NAME/businessApi/fallBack";

    public static final String BOOT_FALLBACK_URL = "http://127.0.0.1:8008/businessApi/fallBack";


    public static final String SERVICE_NAME = "SERVICE_NAME";

    @Override
    @Async
    public void notifySubService(OrderItemDto orderItemDto, BusinessTableHisDto businessTableHisDto) {
        HttpEntity<String> httpEntity = null;
        HttpHeaders header = new HttpHeaders();
        try {
            JSONArray params = generateBusinessParam(orderItemDto, businessTableHisDto);
            httpEntity = new HttpEntity<String>(params.toJSONString(), header);
            //通过fallBack 的方式生成Business

            if(Environment.isStartBootWay()){
                outRestTemplate.exchange(BOOT_FALLBACK_URL, HttpMethod.POST, httpEntity, String.class);
            }else {
                restTemplate.exchange(FALLBACK_URL.replace(SERVICE_NAME, orderItemDto.getServiceName()), HttpMethod.POST, httpEntity, String.class);
            }
        } catch (Exception e) {
            logger.error("生成business失败", e);

        }
    }

    @Override
    public void notifyDatabus(List<Map> orderItemMaps, OrderDto orderDto) {


        if (orderItemMaps == null || orderItemMaps.size() < 1) {
            return ;
        }
        //触发databug
        //查询 事务项
        Map orderItem = new HashMap();
        orderItem.put("oId", orderDto.getoId());
        List<Map> businesses = centerServiceDAOImpl.getBusinessByOId(orderItem);
        if (businesses == null || businesses.size() < 1) {
            return;
        }

        String databusSwitch = MappingCache.getValue(DomainContant.COMMON_DOMAIN, DATABUS_SWITCH);

        if (!DATABUS_SWITCH_ON.equals(databusSwitch)) {
            return ;
        }
        List<BusinessDatabusDto> databusDtos = DatabusCache.getDatabuss();

        if (!hasTypeCd(databusDtos, businesses) || !SecureInvocation.secure(this.getClass())) {
            return ;
        }

        try {
            //同步databus
            dataBusInnerServiceSMOImpl.exchange(freshBusesses(businesses, orderItemMaps));
        } catch (Exception e) {
            logger.error("传输databus 失败", e);
        }
    }

    /**
     * @param businesses
     * @param orderItemMaps
     * @return
     */
    private List<Business> freshBusesses(List<Map> businesses, List<Map> orderItemMaps) {
        List<Business> businessObjs = new ArrayList<>();
        Business businessObj = null;
        /**
         * select t.b_id bId,t.o_id oId,t.create_time createTime,t.action,t.action_obj actionObj,
         *         t.finish_time finishTime,t.remark,ul.service_name serviceName,ul.log_text logText
         *         from c_order_item t
         *         LEFT JOIN unitem_log ul on t.b_id = ul.b_id and t.o_id = ul.o_id and ul.status_cd = '0'
         *         where 1=1
         *
         *         {"afterValue":[{"share_id":"'502021080429730002'","start_time":"'2021-08-01 00:00:35'","ad_type":"'10000'",
         *         "advert_type":"'3'","ads_id":"'782021080454590009'","ad_name":"'广告'","end_time":"'2022-02-03 02:10:35'",
         *         "state":"'2000'","seq":"'1'","url":"'img/20210804/01a629f9-a267-43b8-8d74-9c83b00ed2a7.png'"}],"preValue":[]}
         */
        JSONObject data = null;
        for (Map business : businesses) {
            for (Map orderItem : orderItemMaps) {
                if (!business.get("b_id").equals(orderItem.get("bId"))) {
                    continue;
                }
                businessObj = new Business();
                businessObj.setoId(business.get("o_id").toString());
                businessObj.setbId(business.get("b_id").toString());
                businessObj.setBusinessTypeCd(business.get("business_type_cd").toString());
                JSONObject logText = JSONObject.parseObject(orderItem.get("logText").toString());
                if ("ADD".equals(orderItem.get("action")) || "MOD".equals(orderItem.get("action"))) {
                    data = logText.getJSONArray("afterValue").getJSONObject(0);
                    data = StringUtil.lineToHump(data);
                    businessObj.setData(data);
                }
                if ("DEL".equals(orderItem.get("action"))) {
                    data = logText.getJSONArray("preValue").getJSONObject(0);
                    data = StringUtil.lineToHump(data);
                    businessObj.setData(data);
                }
                businessObjs.add(businessObj);
            }
        }

        return businessObjs;
    }


    private boolean hasTypeCd(List<BusinessDatabusDto> databusDtos, List<Map> businesses) {

        for (BusinessDatabusDto databusDto : databusDtos) {
            for (Map business : businesses) {
                if (databusDto.getBusinessTypeCd().equals(business.get("business_type_cd"))) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 生成回滚sql
     *
     * @param orderItemDto
     * @return
     */
    private JSONArray generateBusinessParam(OrderItemDto orderItemDto, BusinessTableHisDto businessTableHisDto) {
        JSONArray params = null;
        switch (orderItemDto.getAction()) {
            case "ADD":
                params = generateBusinessInsertInsertSql(orderItemDto, businessTableHisDto);
                break;
            case "MOD":
                params = new JSONArray();
                JSONArray paramDels = generateBusinessDelInsertSql(orderItemDto, businessTableHisDto);
                for(int delIndex = 0 ; delIndex < paramDels.size(); delIndex ++){
                    params.add(paramDels.getJSONObject(delIndex));
                }
                JSONArray paramAdds = generateBusinessInsertInsertSql(orderItemDto, businessTableHisDto);
                for(int addIndex = 0 ; addIndex < paramAdds.size(); addIndex ++){
                    params.add(paramAdds.getJSONObject(addIndex));
                }
                break;
            case "DEL":
                params = generateBusinessDelInsertSql(orderItemDto, businessTableHisDto);
                break;
        }

        return params;
    }


    /**
     * 生成添加的insert语句
     *
     * @param orderItemDto
     * @return
     */
    private JSONArray generateBusinessInsertInsertSql(OrderItemDto orderItemDto, BusinessTableHisDto businessTableHisDto) {
        JSONArray params = new JSONArray();
        JSONObject param = null;
        JSONObject updateParam = null;
        String sql = "";
        String updateSql = "";
        String logText = orderItemDto.getLogText();

        JSONObject logTextObj = JSONObject.parseObject(logText);
        JSONArray afterValues = logTextObj.getJSONArray("afterValue");
        for (int afterValueIndex = 0; afterValueIndex < afterValues.size(); afterValueIndex++) {
            sql = "insert into " + businessTableHisDto.getActionObjHis() + " ";
            updateSql = "update " + businessTableHisDto.getActionObj() +" set b_id='"+orderItemDto.getbId()+"' where 1=1 ";

            param = new JSONObject();
            updateParam = new JSONObject();
            JSONObject keyValue = afterValues.getJSONObject(afterValueIndex);
            if (keyValue.isEmpty()) {
                continue;
            }
            String keySql = "( ";
            String valueSql = " values (";
            for (String key : keyValue.keySet()) {
                if ("status_cd".equals(key) || "create_time".equals(key) || "b_id".equals(key)) {
                    continue;
                }
                keySql += (key + ",");
                valueSql += (keyValue.getString(key) + ",");

                updateSql += (" and "+key +"=" + keyValue.getString(key));
            }
            keySql += "operate,b_id";
            valueSql += "'ADD','" + orderItemDto.getbId() + "'";
            if (keySql.endsWith(",")) {
                keySql = keySql.substring(0, keySql.length() - 1);
            }
            if (valueSql.endsWith(",")) {
                valueSql = valueSql.substring(0, valueSql.length() - 1);
            }
            sql = sql + keySql + ") " + valueSql + ") ";
            param.put("fallBackSql", sql);
            updateParam.put("fallBackSql", updateSql);
            params.add(param);
            params.add(updateParam);
        }

        return params;
    }


    /**
     * 生成删除的insert语句
     *
     * @param orderItemDto
     * @return
     */
    private JSONArray generateBusinessDelInsertSql(OrderItemDto orderItemDto, BusinessTableHisDto businessTableHisDto) {
        JSONArray params = new JSONArray();
        JSONObject param = null;
        String sql = "";
        String logText = orderItemDto.getLogText();

        JSONObject logTextObj = JSONObject.parseObject(logText);
        JSONArray preValues = logTextObj.getJSONArray("preValue");
        for (int preValueIndex = 0; preValueIndex < preValues.size(); preValueIndex++) {
            sql = "insert into " + businessTableHisDto.getActionObjHis() + " ";
            param = new JSONObject();
            JSONObject keyValue = preValues.getJSONObject(preValueIndex);
            if (keyValue.isEmpty()) {
                continue;
            }
            String keySql = "( ";
            String valueSql = " values (";
            for (String key : keyValue.keySet()) {
                if ("status_cd".equals(key) || "create_time".equals(key) || "b_id".equals(key)) {
                    continue;
                }
                keySql += (key + ",");
                valueSql += (keyValue.getString(key) + ",");
            }
            keySql += "operate,b_id";
            valueSql += "'DEL','" + orderItemDto.getbId() + "'";
            if (keySql.endsWith(",")) {
                keySql = keySql.substring(0, keySql.length() - 1);
            }
            if (valueSql.endsWith(",")) {
                valueSql = valueSql.substring(0, valueSql.length() - 1);
            }
            sql = sql + keySql + ") " + valueSql + ") ";
            param.put("fallBackSql", sql);
            params.add(param);
        }

        return params;
    }

}

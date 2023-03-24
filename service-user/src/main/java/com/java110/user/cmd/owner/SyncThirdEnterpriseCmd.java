package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.client.OutRestTemplate;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.doc.annotation.*;
import com.java110.dto.owner.OwnerAttrDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.user.IOwnerAttrInnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.po.owner.OwnerAttrPo;
import com.java110.po.owner.OwnerPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;


@Java110CmdDoc(title = "招商平台企业信息同步",
        description = "同步企业信息",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/owner.syncThirdEnterprise",
        resource = "userDoc",
        author = "吴学文",
        serviceCode = "owner.syncThirdEnterprise",
        seq = 16
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "communityId", length = 30, remark = "物业系统小区ID"),
        @Java110ParamDoc(name = "enterpriseArea", length = 64, remark = "办公面积"),
        @Java110ParamDoc(name = "enterpriseAssets", length = 64, remark = "企业资产"),
        @Java110ParamDoc(name = "enterpriseIncome", length = 64, remark = "企业收入"),
        @Java110ParamDoc(name = "enterpriseMember", length = 64, remark = "办公人数"),
        @Java110ParamDoc(name = "enterpriseOutput", length = 64, remark = "企业产值"),
        @Java110ParamDoc(name = "enterpriseSize", length = 64, remark = "企业规模"),
        @Java110ParamDoc(name = "enterpriseTax", length = 64, remark = "企业税收"),
        @Java110ParamDoc(name = "enterpriseType", length = 64, remark = "企业类型(未定义，暂定1，五百强，2上市)"),
        @Java110ParamDoc(name = "financingInfo", length = 64, remark = "融资信息"),
        @Java110ParamDoc(name = "inTime", length = 64, remark = "入驻时间"),
        @Java110ParamDoc(name = "industry", length = 64, remark = "所属行业"),
        @Java110ParamDoc(name = "label", length = 64, remark = "企业标签"),
        @Java110ParamDoc(name = "legalPerson", length = 64, remark = "法人"),
        @Java110ParamDoc(name = "scienceEnterprise", length = 64, remark = "科技型企业：如民营科技企业"),
        @Java110ParamDoc(name = "shopName", length = 64, remark = "店铺名称"),
        @Java110ParamDoc(name = "superAdmin", length = 64, remark = "超级管理员号码"),
        @Java110ParamDoc(name = "website", length = 64, remark = "官网地址"),
        @Java110ParamDoc(name = "years", length = 64, remark = "成立年限：如1-3年"),
        @Java110ParamDoc(name = "contactInfo", length = 64, remark = "联系方式"),
        @Java110ParamDoc(name = "creditCode", length = 64, remark = "统一信用代码"),
        @Java110ParamDoc(name = "enterpriseAddress", length = 64, remark = "企业地址"),
        @Java110ParamDoc(name = "enterpriseName", length = 64, remark = "企业名称"),
        @Java110ParamDoc(name = "introduction", length = 256, remark = "简介"),
        @Java110ParamDoc(name = "licenseImg", length = 64, remark = "营业执照图片"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@Java110ExampleDoc(
        reqBody = "{'communityId':'123123','enterpriseArea':'11','enterpriseAssets':'11','enterpriseIncome':'11','enterpriseMember':'11','enterpriseOutput':'11','enterpriseSize':'11'," +
                "'enterpriseTax':'11','enterpriseType':'11','financingInfo':'11','inTime':'2023-01-01','industry':'11','label':'11','label':'11'," +
                "'legalPerson':'11','scienceEnterprise':'11','shopName':'11','superAdmin':'11','website':'11','contactInfo':'11','creditCode':'11'," +
                "'enterpriseAddress':'11','enterpriseName':'11','introduction':'11','licenseImg':'11',}",
        resBody = "{'code':0,'msg':'成功'}"
)

/***
 * 提供招商平台
 */
@Java110Cmd(serviceCode = "owner.syncThirdEnterprise")
public class SyncThirdEnterpriseCmd extends Cmd {

    @Autowired
    private IOwnerAttrInnerServiceSMO ownerAttrInnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    public static final String DEFAULT_COMMUNITY_ID = "123"; //特殊化需求 这里写死

    @Autowired
    private OutRestTemplate outRestTemplate;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含communityId");
        Assert.hasKeyAndValue(reqJson, "enterpriseArea", "未包含enterpriseArea");
        Assert.hasKeyAndValue(reqJson, "enterpriseAssets", "未包含enterpriseAssets");
        Assert.hasKeyAndValue(reqJson, "enterpriseIncome", "未包含enterpriseIncome");
        Assert.hasKeyAndValue(reqJson, "enterpriseMember", "未包含enterpriseMember");
        Assert.hasKeyAndValue(reqJson, "enterpriseOutput", "未包含enterpriseOutput");
        Assert.hasKeyAndValue(reqJson, "enterpriseSize", "未包含enterpriseSize");
        Assert.hasKeyAndValue(reqJson, "enterpriseTax", "未包含enterpriseTax");
        Assert.hasKeyAndValue(reqJson, "enterpriseType", "未包含enterpriseType");
        Assert.hasKeyAndValue(reqJson, "enterpriseOutput", "未包含enterpriseOutput");
        Assert.hasKeyAndValue(reqJson, "financingInfo", "未包含financingInfo");
        Assert.hasKeyAndValue(reqJson, "inTime", "未包含inTime");
        Assert.hasKeyAndValue(reqJson, "industry", "未包含industry");
        Assert.hasKeyAndValue(reqJson, "label", "未包含label");
        Assert.hasKeyAndValue(reqJson, "legalPerson", "未包含legalPerson");
        Assert.hasKeyAndValue(reqJson, "scienceEnterprise", "未包含scienceEnterprise");
        Assert.hasKeyAndValue(reqJson, "shopName", "未包含shopName");
        Assert.hasKeyAndValue(reqJson, "superAdmin", "未包含superAdmin");
        Assert.hasKeyAndValue(reqJson, "website", "未包含website");
        Assert.hasKeyAndValue(reqJson, "years", "未包含years");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        OwnerAttrDto ownerAttrDto = new OwnerAttrDto();
        ownerAttrDto.setSpecCd(OwnerAttrDto.SPEC_CD_EXT_OWNER_ID);
        ownerAttrDto.setValue(reqJson.getString("userId"));
        List<OwnerAttrDto> ownerAttrDtos = ownerAttrInnerServiceSMOImpl.queryOwnerAttrs(ownerAttrDto);

        String ownerId = "";

        if (ownerAttrDtos == null || ownerAttrDtos.size() < 1) {
            ownerId = saveOwner(reqJson);
        }

        context.setResponseEntity(ResultVo.createResponseEntity(ownerId));
    }

    /**
     * 添加业主
     *
     * @param reqJson
     * @return
     */
    private String saveOwner(JSONObject reqJson) {

        JSONObject data = reqJson;

        OwnerPo ownerPo = new OwnerPo();
        ownerPo.setOwnerId(GenerateCodeFactory.getGeneratorId("99"));
        ownerPo.setMemberId(ownerPo.getOwnerId());
        ownerPo.setAge("1");
        ownerPo.setOwnerFlag(OwnerDto.OWNER_FLAG_TRUE);
        ownerPo.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
        ownerPo.setAddress(reqJson.getString("enterpriseAddress"));
        ownerPo.setCommunityId(reqJson.getString("communityId"));
        ownerPo.setIdCard(data.getString("creditCode"));
        ownerPo.setLink(data.getString("contactInfo"));
        ownerPo.setName(data.getString("enterpriseName"));
        ownerPo.setRemark(data.getString("introduction"));
        ownerPo.setSex(data.getString("sex") == null ? "1" : "0");
        ownerPo.setState(OwnerDto.STATE_FINISH);
        ownerPo.setUserId("-1");
        int flag = ownerV1InnerServiceSMOImpl.saveOwner(ownerPo);
        if (flag < 1) {
            throw new CmdException("保存业主失败");
        }

        saveAttr(reqJson, ownerPo, OwnerAttrDto.SPEC_CD_EXT_OWNER_ID, reqJson.getString("createBy"));

        saveAttr(reqJson, ownerPo, "enterpriseArea", reqJson.getString("enterpriseArea")); // 办公面积
        saveAttr(reqJson, ownerPo, "enterpriseAssets", reqJson.getString("enterpriseAssets")); // 企业资产
        saveAttr(reqJson, ownerPo, "enterpriseIncome", reqJson.getString("enterpriseIncome")); // 企业收入
        saveAttr(reqJson, ownerPo, "enterpriseMember", reqJson.getString("enterpriseMember")); // 办公人数
        saveAttr(reqJson, ownerPo, "enterpriseOutput", reqJson.getString("enterpriseOutput")); // 企业产值
        saveAttr(reqJson, ownerPo, "enterpriseSize", reqJson.getString("enterpriseSize")); // 企业规模
        saveAttr(reqJson, ownerPo, "enterpriseTax", reqJson.getString("enterpriseTax")); // 企业税收
        saveAttr(reqJson, ownerPo, "enterpriseType", reqJson.getString("enterpriseType")); // 企业类型
        saveAttr(reqJson, ownerPo, "financingInfo", reqJson.getString("financingInfo")); // 融资信息
        saveAttr(reqJson, ownerPo, "inTime", reqJson.getString("inTime")); // 入驻时间
        saveAttr(reqJson, ownerPo, "industry", reqJson.getString("industry")); // 所属行业
        saveAttr(reqJson, ownerPo, "label", reqJson.getString("label")); // 企业标签
        saveAttr(reqJson, ownerPo, "legalPerson", reqJson.getString("legalPerson")); // 法人
        saveAttr(reqJson, ownerPo, "scienceEnterprise", reqJson.getString("scienceEnterprise")); // 科技型企业
        saveAttr(reqJson, ownerPo, "shopName", reqJson.getString("shopName")); // 店铺名称
        saveAttr(reqJson, ownerPo, "superAdmin", reqJson.getString("superAdmin")); // 超级管理员号码
        saveAttr(reqJson, ownerPo, "website", reqJson.getString("website")); // 官网地址
        saveAttr(reqJson, ownerPo, "years", reqJson.getString("years")); // 成立年限：如1-3年

        return ownerPo.getOwnerId();
    }

    private void saveAttr(JSONObject reqJson, OwnerPo ownerPo, String specCd, String value) {

        if (StringUtil.isEmpty(value)) {
            return;
        }
        OwnerAttrPo ownerAttrPo = new OwnerAttrPo();
        ownerAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId("11"));
        ownerAttrPo.setCommunityId(reqJson.getString("communityId"));
        ownerAttrPo.setValue(value);
        ownerAttrPo.setMemberId(ownerPo.getMemberId());
        ownerAttrPo.setSpecCd(specCd);
        int flag = ownerAttrInnerServiceSMOImpl.saveOwnerAttr(ownerAttrPo);
        if (flag < 1) {
            throw new CmdException("保存业主失败");
        }
    }

}

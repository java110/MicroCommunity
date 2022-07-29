package com.java110.store.cmd.wechatMenu;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.WechatFactory;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.dto.wechatMenu.WechatMenuDto;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.store.IWechatMenuInnerServiceSMO;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Java110Cmd(serviceCode = "smallWeChat.publicWechatMenu")
public class PublishWechatMenuCmd extends Cmd {

    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private IWechatMenuInnerServiceSMO wechatMenuInnerServiceSMOImpl;

    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
    }
    /**
     * {
     * "button":[
     * {
     * "type":"click",
     * "name":"今日歌曲",
     * "key":"V1001_TODAY_MUSIC"
     * },
     * {
     * "name":"菜单",
     * "sub_button":[
     * {
     * "type":"view",
     * "name":"搜索",
     * "url":"http://www.soso.com/"
     * },
     * {
     * "type":"miniprogram",
     * "name":"wxa",
     * "url":"http://mp.weixin.qq.com",
     * "appid":"wx286b93c14bbf93aa",
     * "pagepath":"pages/lunar/index"
     * },
     * {
     * "type":"click",
     * "name":"赞一下我们",
     * "key":"V1001_GOOD"
     * }]
     * }]
     * }
     *
     * @param event   事件对象
     * @param context 数据上文对象
     * @param reqJson 请求报文
     */
    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
//查询一级菜单
        WechatMenuDto wechatMenuDto = BeanConvertUtil.covertBean(reqJson, WechatMenuDto.class);
        wechatMenuDto.setMenuLevel(WechatMenuDto.MENU_LEVEL_ONE);
        List<WechatMenuDto> wechatMenuDtos = wechatMenuInnerServiceSMOImpl.queryWechatMenus(wechatMenuDto);

        if (wechatMenuDtos == null || wechatMenuDtos.size() < 1 || wechatMenuDtos.size() > 3) {
            throw new IllegalArgumentException("一级菜单数量为" + wechatMenuDtos.size() + "不符合公众号菜单数量要求");
        }
        JSONObject wechatMenuObj = new JSONObject();
        JSONArray button = new JSONArray();
        for (WechatMenuDto tmpWechatMenuDto : wechatMenuDtos) {
            wechatMenuDto = new WechatMenuDto();
            wechatMenuDto.setMenuLevel(WechatMenuDto.MENU_LEVEL_TWO);
            wechatMenuDto.setCommunityId(tmpWechatMenuDto.getCommunityId());
            wechatMenuDto.setParentMenuId(tmpWechatMenuDto.getWechatMenuId());
            List<WechatMenuDto> tmpWechatMenuDtos = wechatMenuInnerServiceSMOImpl.queryWechatMenus(wechatMenuDto);
            JSONObject menuButton = new JSONObject();
            menuButton.put("name", tmpWechatMenuDto.getMenuName());
            if (tmpWechatMenuDtos != null && tmpWechatMenuDtos.size() > 0) {
                addSubMenu(menuButton, tmpWechatMenuDtos);
                button.add(menuButton);
                continue;
            }

            menuButton.put("type", tmpWechatMenuDto.getMenuType());
            if (WechatMenuDto.MENU_TYPE_VIEW.equals(tmpWechatMenuDto.getMenuType())) {
                menuButton.put("url", tmpWechatMenuDto.getMenuValue());
            } else if (WechatMenuDto.MENU_TYPE_MINIPROGRAM.equals(tmpWechatMenuDto.getMenuType())) {
                menuButton.put("url", tmpWechatMenuDto.getMenuValue());
                menuButton.put("appid", tmpWechatMenuDto.getAppId());
                menuButton.put("pagepath", tmpWechatMenuDto.getPagepath());
            }
            button.add(menuButton);
        }
        wechatMenuObj.put("button", button);
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setObjType(SmallWeChatDto.OBJ_TYPE_COMMUNITY);
        smallWeChatDto.setObjId(reqJson.getString("communityId"));
        smallWeChatDto.setWeChatType(SmallWeChatDto.WECHAT_TYPE_PUBLIC);
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);

        if (smallWeChatDtos == null || smallWeChatDtos.size() < 1) {
            throw new IllegalArgumentException("当前小区未配置公众号 请先配置");
        }
        String token = WechatFactory.getAccessToken(smallWeChatDtos.get(0).getAppId(), smallWeChatDtos.get(0).getAppSecret());
        String url = WechatConstant.CREATE_MENU.replace("ACCESS_TOKEN", token);
        ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, wechatMenuObj.toJSONString(), String.class);

        JSONObject wechatOutObj = JSONObject.parseObject(responseEntity.getBody());
        context.setResponseEntity(ResultVo.createResponseEntity(wechatOutObj.getInteger("errcode"), wechatOutObj.getString("errmsg")));
    }

    private void addSubMenu(JSONObject wechatMenuObj, List<WechatMenuDto> wechatMenuDtos) {
        JSONArray subButtons = new JSONArray();
        JSONObject subButton = null;
        for (WechatMenuDto wechatMenuDto : wechatMenuDtos) {
            subButton = new JSONObject();
            subButton.put("name", wechatMenuDto.getMenuName());
            subButton.put("type", wechatMenuDto.getMenuType());
            if (WechatMenuDto.MENU_TYPE_VIEW.equals(wechatMenuDto.getMenuType())) {
                subButton.put("url", wechatMenuDto.getMenuValue());
            } else if (WechatMenuDto.MENU_TYPE_MINIPROGRAM.equals(wechatMenuDto.getMenuType())) {
                subButton.put("url", wechatMenuDto.getMenuValue());
                subButton.put("appid", wechatMenuDto.getAppId());
                subButton.put("pagepath", wechatMenuDto.getPagepath());
            }
            subButtons.add(subButton);
        }
        wechatMenuObj.put("sub_button", subButtons);
    }
}

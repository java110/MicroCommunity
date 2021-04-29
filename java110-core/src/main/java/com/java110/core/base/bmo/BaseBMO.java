package com.java110.core.base.bmo;

import com.java110.core.base.AppBase;

/**
 * 业务类基类
 *
 * 公用代码实现
 * Created by wuxw on 2017/3/16.
 */
public class BaseBMO extends AppBase {

    /**
     * 根据User_id 查询用户信息
     * @param userId
     * @param iUserServiceSMO
     * @return

    protected User getUser(String userId, IUserServiceSMO iUserServiceSMO,IQueryServiceSMO iQueryServiceSMO){

            User newUser = null;
            try {
                //从缓存中获取数据
                newUser = JSONObject.parseObject(CacheUtil.get(NameSpaceHandler.getNameSpaceHandler(NameSpaceHandler.USER_DOMAIN),
                        CacheUtil.KEY_USER_PREFIX + userId,String.class),User.class);
                if (UserUtil.isBlank(newUser)) {
                    User tmpUser = new User();
                    tmpUser.setUser_id(userId);
                    String requestParam = ProtocolUtil.createRequestJsonString(ProtocolUtil.SERVICE_CODE_USER_QUERY, tmpUser);
                    logger.debug("------[BaseBMO.getUser]------ 请求参数：" + requestParam);
                    String responseParam = iUserServiceSMO.getUserInfo(requestParam);
                    TcpCont tcpCont = ProtocolUtil.getTcpCont(responseParam);

                    logger.debug("------[BaseBMO.getUser]------ 返回参数：" + responseParam);

                    if (tcpCont != null && ProtocolUtil.RETURN_MSG_SUCCESS.equals(tcpCont.getResultCode())) {
                        newUser = ProtocolUtil.getObject(responseParam, User.class);
                        //如果用户信息为空
                        if (UserUtil.isBlank(newUser)) {
                            throw new RuntimeException("用户[user_id = "+userId+",未绑定号码]");
                        }
                        assertUser(iUserServiceSMO,iQueryServiceSMO, newUser);
                    }
                    String offerExpirtTime = CustomizedPropertyPlaceholderConfigurer.getContextPropertyString("offers.cache.alive.time");
                    //保存数据至 缓存中
                    CacheUtil.set(NameSpaceHandler.getNameSpaceHandler(NameSpaceHandler.USER_DOMAIN),CacheUtil.KEY_USER_PREFIX + userId,
                            JSONObject.toJSONString(newUser),
                            StringUtils.isEmpty(offerExpirtTime) || !NumberUtils.isNumber(offerExpirtTime)
                                    ? 0 : CommonUtil.multiplicativeStringToInteger(offerExpirtTime));
                }
            }catch (Exception e){
                logger.error("获取不到用户信息[userId = "+userId+"]",e);
                return null;
            }
            return newUser;
    }*/

    /**
     * 判断用户是否为空，如果为空直接跑出异常
     * 如果用户不为空时，保存数据至数据库
     * @param iQueryServiceSMO
     * @param newUser 及时入参也是出参
     * @throws RuntimeException
     */
   /* private void assertUser(IUserServiceSMO iUserServiceSMO,IQueryServiceSMO iQueryServiceSMO, User newUser) throws RuntimeException {
        *//**
         * 说明绑定用户的时候从crm拉去用户资料失败，这里重新拉去一遍
         *//*
        if(UserUtil.isBlank(newUser)){
            User tUser = new User();
            tUser.setAcc_nbr(newUser.getAcc_nbr());
            String requestP = ProtocolUtil.createRequestJsonString(ProtocolUtil.SERVICE_CODE_USER_QUERY,tUser);
            //从crm 查询数据
            String responseP = iQueryServiceSMO.queryCrmUserInfo(requestP);

            TcpCont tmpTcpCont = ProtocolUtil.getTcpCont(responseP);

            if(tmpTcpCont == null || !ProtocolUtil.RETURN_MSG_SUCCESS.equals(tmpTcpCont.getResultCode())
                    || UserUtil.isBlank(ProtocolUtil.getObject(responseP,User.class))) {
                logger.debug("--------------------[BaseBMO.assertUser]---------------------crm返回用户信息："+responseP);
                throw  new RuntimeException("号码[acc_nbr="+newUser.getAcc_nbr()+"]在crm中为查到数据");
            }

            newUser.pullAll(ProtocolUtil.getObject(responseP,User.class));

            //保存或修改数据
            String requestParam = ProtocolUtil.createRequestJsonString(ProtocolUtil.SERVICE_CODE_USER_SAVE, newUser);
            logger.debug("------[UserBMOImpl.addUserInfo]------ 请求参数：" + requestParam);
            String responseParam = iUserServiceSMO.addUserInfo(requestParam);
            logger.debug("------[UserBMOImpl.addUserInfo]------ 返回参数：" + responseParam);
        }
    }*/
}

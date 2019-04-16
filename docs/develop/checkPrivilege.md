## 用户权限校验

前台服务开发时必须要校验当前用户是否有权限操作数据，只需在SMO实现类方法中加入如下代码：

> //权限校验

> checkUserHasPrivilege(pd,restTemplate, PrivilegeCodeConstant.PRIVILEGE_ENTER_COMMUNITY);


举例：

```
    @Override
    public ResponseEntity<String> listMyCommunity(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        JSONObject _paramObj = JSONObject.parseObject(pd.getReqData());
        //权限校验
        checkUserHasPrivilege(pd,restTemplate, PrivilegeCodeConstant.PRIVILEGE_ENTER_COMMUNITY);
        responseEntity = super.getStoreInfo(pd,restTemplate);
        if(responseEntity.getStatusCode() != HttpStatus.OK){
            return responseEntity;
        }
        Assert.jsonObjectHaveKey(responseEntity.getBody().toString(),"storeId","根据用户ID查询商户ID失败，未包含storeId节点");

        String storeId = JSONObject.parseObject(responseEntity.getBody().toString()).getString("storeId");
        String storeTypeCd = JSONObject.parseObject(responseEntity.getBody().toString()).getString("storeTypeCd");

        //修改用户信息
        responseEntity = this.callCenterService(restTemplate,pd,"",
                ServiceConstant.SERVICE_API_URL+"/api/query.myCommunity.byMember?memberId="+storeId+
                        "&memberTypeCd="+MappingCache.getValue(MappingConstant.DOMAIN_STORE_TYPE_2_COMMUNITY_MEMBER_TYPE,storeTypeCd),
                HttpMethod.GET);

        if(responseEntity.getStatusCode() != HttpStatus.OK){
            return responseEntity;
        }
        JSONArray tmpCommunitys = JSONObject.parseObject(responseEntity.getBody().toString()).getJSONArray("communitys");
        freshCommunityAttr(tmpCommunitys);
        responseEntity = new ResponseEntity<String>(tmpCommunitys.toJSONString(),
                HttpStatus.OK);
        return responseEntity;
    }
```
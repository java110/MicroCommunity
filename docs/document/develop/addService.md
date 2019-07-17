## 添加服务表

>  insert into c_service(service_id,service_code,business_type_cd,name,seq,is_instance,url,method,provide_app_id)
>  values('78','delete.privilege.userPrivilege','API','删除用户权限','1','NT','http://order-service/privilegeApi/deleteStaffPrivilegeOrPrivilegeGroup','POST','8000418002');

## 服务绑定应用

> insert into c_route(app_id,service_id,order_type_cd,invoke_model)
> values('8000418004','78','Q','S');

## 添加查询类 服务

> INSERT INTO c_service_sql
> (service_code,`name`,params,query_model,`sql`,template)
> VALUES
> (
> 'query.myCommunity.byMember',
> '查询商户入驻小区信息',
> 'memberId,memberTypeCd',
> '1',
> '{"param1":"SELECT
>   sc.`community_id` communityId,
>   sc.`name`,
>   sc.`address`,
>   sc.`nearby_landmarks` nearbyLandmarks,
>   sc.`city_code` cityCode,
>   sc.`map_x` mapX,
>   sc.`map_y` mapY,
>   scm.`member_id` memberId,
>   scm.`member_type_cd` memberTypeCd,
>   scm.`status_cd` statusCd
> FROM
>   s_community sc,
>   s_community_member scm
> WHERE sc.`community_id` = scm.`community_id`
>   AND sc.`status_cd` = ''0''
>   AND scm.`member_id` = #memberId#
>   AND scm.`member_type_cd` = #memberTypeCd#
>   AND scm.`status_cd` IN (''0'', ''1000'')"}',
> '{"PARAM": {"param1": "$.#communitys#Array"},"TEMPLATE": {}}'
> );




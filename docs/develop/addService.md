## 添加服务表

>  insert into c_service(service_id,service_code,business_type_cd,name,seq,is_instance,url,method,provide_app_id)
>  values('78','delete.privilege.userPrivilege','API','删除用户权限','1','NT','http://order-service/privilegeApi/deleteStaffPrivilegeOrPrivilegeGroup','POST','8000418002');

## 服务绑定应用

> insert into c_route(app_id,service_id,order_type_cd,invoke_model)
> values('8000418004','78','Q','S');



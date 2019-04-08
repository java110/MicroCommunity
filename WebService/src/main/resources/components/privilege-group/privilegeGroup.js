/**
    权限组
**/
(function(vc){

    vc.extends({
        data:{
            privilegeGroupInfo:{
                groups:[]
            }
        },
        _initMethod:function(){
            vc.component.loadPrivilegeGroup();
        },
        methods:{
            loadPrivilegeGroup:function(){
                var param = {
                    msg:234
                };

                //发送get请求
               vc.http.get('privilegeGroup',
                            'listPrivilegeGroup',
                             param,
                             function(json){
                                var _groupsInfo = JSON.parse(json);
                                vc.component.privilegeGroupInfo.groups = _groupsInfo;
                                if(_groupsInfo.length > 0){
                                    vc.component.$emit('privilege_group_event',{
                                                                    _pgId:_groupsInfo[0].pgId,
                                                                    _pgName:_groupsInfo[0].name
                                                                });
                                }


                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            notifyQueryPrivilege:function(_pGroup){
                vc.component.$emit('privilege_group_event',{
                    _pgId:_pGroup.pgId,
                    _pgName:_pGroup.name
                });
            },
            openPrivilegeGroup:function(){

            }
        }
    });

})(window.vc);
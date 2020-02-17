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
        _initEvent:function(){
             vc.component.$on('privilegeGroup_loadPrivilegeGroup',function(_params){
                vc.component.loadPrivilegeGroup();
            });
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
                                                                    _pgName:_groupsInfo[0].name,
                                                                    _storeId:_groupsInfo[0].storeId
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
                    _pgName:_pGroup.name,
                    _storeId:_pGroup.storeId
                });
            },
            openPrivilegeGroupModel:function(){
                vc.component.$emit('addPrivilegeGroup_openPrivilegeGroupModel',{});
            },
            openEditPrivilegeGroupModel:function(_pGroup){
                vc.emit('editPrivilegeGroup','openPrivilegeGroupModel',_pGroup);
            },
            openDeletePrivilegeGroupModel:function(_pGroup){
                vc.component.$emit('deletePrivilegeGroup_openDeletePrivilegeGroupModel',_pGroup);
            }
        }
    });

})(window.vc);
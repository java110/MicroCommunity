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
//                                vc.component.$emit('pagination_info_event',{
//                                    total:_staffInfo.records,
//                                    currentPage:_staffInfo.page
//                                });

                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            notifyQueryPrivilege:function(_pGroup){
                console.log("当前点击权限组",_pGroup)
            }
        }
    });

})(window.vc);
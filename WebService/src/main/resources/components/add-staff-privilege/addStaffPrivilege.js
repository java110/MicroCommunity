(function(vc,vm){

    vc.extends({
        data:{
            addStaffPrivilegeInfo:{
                _currentUserId:'',
                name:'',
                description:'',
                errorInfo:'',
                _noAddPrivilege:[],
                _noAddPrivilegeGroup:[],
                _currentTab:1
            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('addStaffPrivilege','addStaffPrivilegeModel',function(_params){
                $('#addStaffPrivilegeModel').modal('show');
                vc.component._refreshData(_params);
            });
        },
        methods:{
            _refreshData:function(_params){
                vc.component.addStaffPrivilegeInfo._currentUserId = _params.userId;
                vc.component.addStaffPrivilegeInfo._currentTab = 1;
                vc.component.listNoAddPrivilegeGroup();
            },
            changeTab:function(_tempTab){
                vc.component.addStaffPrivilegeInfo._currentTab= _tempTab;
                if(_tempTab == 2){
                    vc.component.listNoAddPrivilege();
                    return ;
                }
                vc.component.listNoAddPrivilegeGroup();
            },
            listNoAddPrivilegeGroup:function(){
                vc.component.addStaffPrivilegeInfo._noAddPrivilegeGroup = [];
                var param = {
                    params:{
                        userId:vc.component.addStaffPrivilegeInfo._currentUserId
                    }
                };
                vc.http.get(
                    'addStaffPrivilege',
                    'listNoAddPrivilegeGroup',
                     param,
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            vc.component.addStaffPrivilegeInfo._noAddPrivilegeGroup = JSON.parse(json);
                            return ;
                        }
                        vc.component.addStaffPrivilegeInfo.errorInfo = json;
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.component.addStaffPrivilegeInfo.errorInfo = errInfo;
                });

            },
            listNoAddPrivilege:function(){
                vc.component.addStaffPrivilegeInfo._noAddPrivilege=[];
                var param = {
                    params:{
                        userId:vc.component.addStaffPrivilegeInfo._currentUserId
                    }
                }
                vc.http.get(
                            'addStaffPrivilege',
                            'listNoAddPrivilege',
                             param,
                             function(json,res){
                                //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                                if(res.status == 200){
                                    vc.component.addStaffPrivilegeInfo._noAddPrivilege = JSON.parse(json);
                                    return ;
                                }
                                vc.component.addStaffPrivilegeInfo.errorInfo = json;
                             },
                             function(errInfo,error){
                                console.log('请求失败处理');

                                vc.component.addStaffPrivilegeInfo.errorInfo = errInfo;
                             });
            },
            addStaffPrivilege:function(_pId,_privilegeFlag){
                vc.component.addStaffPrivilegeInfo.errorInfo = "";
                var param = {
                    userId:vc.component.addStaffPrivilegeInfo._currentUserId,
                    pId:_pId,
                    pFlag:_privilegeFlag
                };
                vc.http.post(
                    'addStaffPrivilege',
                    'addStaffPrivilegeOrPrivilegeGroup',
                    JSON.stringify(param),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#addStaffPrivilegeModel').modal('hide');
                            vc.emit('staffPrivilege','_loadStaffPrivileges',{
                                staffId:vc.component.addStaffPrivilegeInfo._currentUserId
                            });
                            return ;
                        }
                        vc.component.addStaffPrivilegeInfo.errorInfo = json;
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.component.addStaffPrivilegeInfo.errorInfo = errInfo;
                     });
            },
            userAddPrivilegeGroup:function(_pgId){
                console.log("需要添加权限：",_pgId);
                vc.component.addStaffPrivilege(_pgId,1)
            },
            userAddPrivilege:function(_pId){
                console.log("需要添加权限：",_pId);
                vc.component.addStaffPrivilege(_pId,2)
            }
        }
    });

})(window.vc,window.vc.component);
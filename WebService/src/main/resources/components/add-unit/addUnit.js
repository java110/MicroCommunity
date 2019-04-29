(function(vc,vm){

    vc.extends({
        data:{
            addUnitInfo:{
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
             vc.on('addUnit','addUnitModel',function(_params){
                $('#addUnitModel').modal('show');
                vc.component._refreshData(_params);
            });
        },
        methods:{
            _refreshData:function(_params){
                vc.component.addUnitInfo._currentUserId = _params.userId;
                vc.component.addUnitInfo._currentTab = 1;
                vc.component.listNoAddPrivilegeGroup();
            },
            changeTab:function(_tempTab){
                vc.component.addUnitInfo._currentTab= _tempTab;
                if(_tempTab == 2){
                    vc.component.listNoAddPrivilege();
                    return ;
                }
                vc.component.listNoAddPrivilegeGroup();
            },
            listNoAddPrivilegeGroup:function(){
                vc.component.addUnitInfo._noAddPrivilegeGroup = [];
                var param = {
                    params:{
                        userId:vc.component.addUnitInfo._currentUserId
                    }
                };
                vc.http.get(
                    'addUnit',
                    'listNoAddPrivilegeGroup',
                     param,
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            vc.component.addUnitInfo._noAddPrivilegeGroup = JSON.parse(json);
                            return ;
                        }
                        vc.component.addUnitInfo.errorInfo = json;
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.component.addUnitInfo.errorInfo = errInfo;
                });

            },
            listNoAddPrivilege:function(){
                vc.component.addUnitInfo._noAddPrivilege=[];
                var param = {
                    params:{
                        userId:vc.component.addUnitInfo._currentUserId
                    }
                }
                vc.http.get(
                            'addUnit',
                            'listNoAddPrivilege',
                             param,
                             function(json,res){
                                //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                                if(res.status == 200){
                                    vc.component.addUnitInfo._noAddPrivilege = JSON.parse(json);
                                    return ;
                                }
                                vc.component.addUnitInfo.errorInfo = json;
                             },
                             function(errInfo,error){
                                console.log('请求失败处理');

                                vc.component.addUnitInfo.errorInfo = errInfo;
                             });
            },
            addUnit:function(_pId,_privilegeFlag){
                vc.component.addUnitInfo.errorInfo = "";
                var param = {
                    userId:vc.component.addUnitInfo._currentUserId,
                    pId:_pId,
                    pFlag:_privilegeFlag
                };
                vc.http.post(
                    'addUnit',
                    'addUnitOrPrivilegeGroup',
                    JSON.stringify(param),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#addUnitModel').modal('hide');
                            vc.emit('staffPrivilege','_loadUnits',{
                                staffId:vc.component.addUnitInfo._currentUserId
                            });
                            return ;
                        }
                        vc.component.addUnitInfo.errorInfo = json;
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.component.addUnitInfo.errorInfo = errInfo;
                     });
            },
            userAddPrivilegeGroup:function(_pgId){
                console.log("需要添加权限：",_pgId);
                vc.component.addUnit(_pgId,1)
            },
            userAddPrivilege:function(_pId){
                console.log("需要添加权限：",_pId);
                vc.component.addUnit(_pId,2)
            }
        }
    });

})(window.vc,window.vc.component);
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
                _currentTab:1,
                selectPrivileges:[],
                selectPrivilegeGroups:[]
            }
        },
        watch: { // 监视双向绑定的数据数组
            addStaffPrivilegeInfo: {
                handler(){ // 数据数组有变化将触发此函数
                    if(vc.component.addStaffPrivilegeInfo.selectPrivileges.length == vc.component.addStaffPrivilegeInfo._noAddPrivilege.length){
                        document.querySelector('#quan').checked = true;
                    }else {
                        document.querySelector('#quan').checked = false;
                    }

                    if(vc.component.addStaffPrivilegeInfo.selectPrivilegeGroups.length == vc.component.addStaffPrivilegeInfo._noAddPrivilegeGroup.length){
                        document.querySelector('#quanGroup').checked = true;
                    }else {
                        document.querySelector('#quanGroup').checked = false;
                    }
                },
                deep: true // 深度监视
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
            addStaffPrivilege:function(){
                vc.component.addStaffPrivilegeInfo.errorInfo = "";
                var _pIds = [];
                var _selectPrivilegeGroups = vc.component.addStaffPrivilegeInfo.selectPrivilegeGroups;
                var _selectPrivileges = vc.component.addStaffPrivilegeInfo.selectPrivileges;
                if(vc.component.addStaffPrivilegeInfo._currentTab == 1){
                    for(var _pIndex = 0;pIndex < _selectPrivilegeGroups.length;pIndex++){
                        var _pgId = {
                            pId: _selectPrivilegeGroups[pIndex]
                        }
                        _pIds.push(_pgId);
                    }
                }else{
                    for(var _pIndex = 0;pIndex < _selectPrivileges.length;pIndex++){
                        var _pId = {
                            pId: _selectPrivileges[pIndex]
                        }
                        _pIds.push(_pId);
                    }
                }
                if(_pIds.length < 1){
                    vc.toast('未选择相应权限或权限组');
                    return ;
                }
                var param = {
                    userId:vc.component.addStaffPrivilegeInfo._currentUserId,
                    pIds:_pIds,
                    pFlag:vc.component.addStaffPrivilegeInfo._currentTab
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
            }
        }
    });

})(window.vc,window.vc.component);
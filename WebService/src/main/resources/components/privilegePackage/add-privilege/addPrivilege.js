(function(vc){

    vc.extends({
        data:{
            addPrivilegeInfo:{
                _currentPgId:'',
                _pName:'',
                name:'',
                description:'',
                errorInfo:'',
                _noAddPrivilege:[],
                selectPrivileges:[]
            }
        },
        watch: { // 监视双向绑定的数据数组
            addPrivilegeInfo: {
                handler(){ // 数据数组有变化将触发此函数
                    if(vc.component.addPrivilegeInfo.selectPrivileges.length == vc.component.addPrivilegeInfo._noAddPrivilege.length){
                        document.querySelector('#quan').checked = true;
                    }else {
                        document.querySelector('#quan').checked = false;
                    }
                },
                deep: true // 深度监视
            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.component.$on('addPrivilege_openPrivilegeModel',function(_params){
                $('#addPrivilegeModel').modal('show');
                vc.component.addPrivilegeInfo._currentPgId = _params.pgId;
                //查询没有添加的权限
                vc.component.listNoAddPrivilege();
            });
        },
        methods:{
            listNoAddPrivilege:function(){
                vc.component.addPrivilegeInfo._noAddPrivilege=[];
                var param = {
                    params:{
                        pgId:vc.component.addPrivilegeInfo._currentPgId,
                        pName:vc.component.addPrivilegeInfo._pName
                    }
                }
                vc.http.get(
                            'addPrivilege',
                            'listNoAddPrivilege',
                             param,
                             function(json,res){
                                //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                                if(res.status == 200){
                                    vc.component.addPrivilegeInfo._noAddPrivilege = JSON.parse(json);
                                    return ;
                                }
                                vc.component.addPrivilegeInfo.errorInfo = json;
                             },
                             function(errInfo,error){
                                console.log('请求失败处理');

                                vc.component.addPrivilegeInfo.errorInfo = errInfo;
                             });
            },
            addPrivilegeToPrivilegeGroup:function(){

                vc.component.addPrivilegeInfo.errorInfo = "";
                var _selectPrivileges = vc.component.addPrivilegeInfo.selectPrivileges;

                if(_selectPrivileges.length < 1){
                    vc.toast("请先选择权限");
                    return ;
                }
                var _pIds = [];
                for(var selectIndex = 0;selectIndex < _selectPrivileges.length;selectIndex ++){
                    var _pId = {
                        pId: _selectPrivileges[selectIndex]
                    };
                    _pIds.push(_pId);
                }
                var _objData = {
                    pgId:vc.component.addPrivilegeInfo._currentPgId,
                    pIds:_pIds
                };
                vc.http.post(
                    'addPrivilege',
                    'addPrivilegeToPrivilegeGroup',
                    JSON.stringify(_objData),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            vc.component.listNoAddPrivilege();
                            $('#addPrivilegeModel').modal('hide');
                            vc.component.$emit('privilege_loadPrivilege',vc.component.addPrivilegeInfo._currentPgId);
                            return ;
                        }
                        vc.component.addPrivilegeInfo.errorInfo = json;
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.component.addPrivilegeInfo.errorInfo = errInfo;
                     });
            },
            checkAll:function(e){
                    var checkObj = document.querySelectorAll('.checkItem'); // 获取所有checkbox项
                    if(e.target.checked){ // 判定全选checkbox的勾选状态
                        for(var i=0;i<checkObj.length;i++){
                            if(!checkObj[i].checked){ // 将未勾选的checkbox选项push到绑定数组中
                                vc.component.addPrivilegeInfo.selectPrivileges.push(checkObj[i].value);
                            }
                        }
                    }else { // 如果是去掉全选则清空checkbox选项绑定数组
                        vc.component.addPrivilegeInfo.selectPrivileges = [];
                    }
            }
        }
    });

})(window.vc);
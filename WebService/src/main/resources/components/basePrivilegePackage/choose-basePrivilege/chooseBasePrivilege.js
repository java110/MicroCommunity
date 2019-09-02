(function(vc){
    vc.extends({
        propTypes: {
           emitChooseBasePrivilege:vc.propTypes.string,
           emitLoadData:vc.propTypes.string
        },
        data:{
            chooseBasePrivilegeInfo:{
                basePrivileges:[],
                _currentBasePrivilegeName:'',
            }
        },
        _initMethod:function(){
        },
        _initEvent:function(){
            vc.on('chooseBasePrivilege','openChooseBasePrivilegeModel',function(_param){
                $('#chooseBasePrivilegeModel').modal('show');
                vc.component._refreshChooseBasePrivilegeInfo();
                vc.component._loadAllBasePrivilegeInfo(1,10,'');
            });
        },
        methods:{
            _loadAllBasePrivilegeInfo:function(_page,_row,_name){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        name:_name
                    }
                };

                //发送get请求
               vc.http.get('chooseBasePrivilege',
                            'list',
                             param,
                             function(json){
                                var _basePrivilegeInfo = JSON.parse(json);
                                vc.component.chooseBasePrivilegeInfo.basePrivileges = _basePrivilegeInfo.basePrivileges;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            chooseBasePrivilege:function(_basePrivilege){
                if(_basePrivilege.hasOwnProperty('name')){
                     _basePrivilege.basePrivilegeName = _basePrivilege.name;
                }
                vc.emit($props.emitChooseBasePrivilege,'chooseBasePrivilege',_basePrivilege);
                vc.emit($props.emitLoadData,'listBasePrivilegeData',{
                    basePrivilegeId:_basePrivilege.basePrivilegeId
                });
                $('#chooseBasePrivilegeModel').modal('hide');
            },
            queryBasePrivileges:function(){
                vc.component._loadAllBasePrivilegeInfo(1,10,vc.component.chooseBasePrivilegeInfo._currentBasePrivilegeName);
            },
            _refreshChooseBasePrivilegeInfo:function(){
                vc.component.chooseBasePrivilegeInfo._currentBasePrivilegeName = "";
            }
        }

    });
})(window.vc);

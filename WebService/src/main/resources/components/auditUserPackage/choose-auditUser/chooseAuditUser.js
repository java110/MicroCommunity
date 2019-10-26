(function(vc){
    vc.extends({
        propTypes: {
           emitChooseAuditUser:vc.propTypes.string,
           emitLoadData:vc.propTypes.string
        },
        data:{
            chooseAuditUserInfo:{
                auditUsers:[],
                _currentAuditUserName:'',
            }
        },
        _initMethod:function(){
        },
        _initEvent:function(){
            vc.on('chooseAuditUser','openChooseAuditUserModel',function(_param){
                $('#chooseAuditUserModel').modal('show');
                vc.component._refreshChooseAuditUserInfo();
                vc.component._loadAllAuditUserInfo(1,10,'');
            });
        },
        methods:{
            _loadAllAuditUserInfo:function(_page,_row,_name){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        name:_name
                    }
                };

                //发送get请求
               vc.http.get('chooseAuditUser',
                            'list',
                             param,
                             function(json){
                                var _auditUserInfo = JSON.parse(json);
                                vc.component.chooseAuditUserInfo.auditUsers = _auditUserInfo.auditUsers;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            chooseAuditUser:function(_auditUser){
                if(_auditUser.hasOwnProperty('name')){
                     _auditUser.auditUserName = _auditUser.name;
                }
                vc.emit($props.emitChooseAuditUser,'chooseAuditUser',_auditUser);
                vc.emit($props.emitLoadData,'listAuditUserData',{
                    auditUserId:_auditUser.auditUserId
                });
                $('#chooseAuditUserModel').modal('hide');
            },
            queryAuditUsers:function(){
                vc.component._loadAllAuditUserInfo(1,10,vc.component.chooseAuditUserInfo._currentAuditUserName);
            },
            _refreshChooseAuditUserInfo:function(){
                vc.component.chooseAuditUserInfo._currentAuditUserName = "";
            }
        }

    });
})(window.vc);

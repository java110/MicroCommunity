(function(vc){
    vc.extends({
        propTypes: {
           emitChooseOrg:vc.propTypes.string,
           emitLoadData:vc.propTypes.string
        },
        data:{
            chooseOrgInfo:{
                orgs:[],
                _currentOrgName:'',
                orgLevel:'',
                parentOrgId:'',
            }
        },
        _initMethod:function(){
        },
        _initEvent:function(){
            vc.on('chooseOrg','openChooseOrgModel',function(_param){
                $('#chooseOrgModel').modal('show');
                vc.copyObject(_param,vc.component.chooseOrgInfo);
                vc.component._refreshChooseOrgInfo();
                vc.component._loadAllOrgInfo(1,10,'');
            });
        },
        methods:{
            _loadAllOrgInfo:function(_page,_row,_name){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        name:_name,
                        orgLevel:vc.component.chooseOrgInfo.orgLevel,
                        parentOrgId:vc.component.chooseOrgInfo.parentOrgId,
                    }
                };

                //发送get请求
               vc.http.get('chooseOrg',
                            'list',
                             param,
                             function(json){
                                var _orgInfo = JSON.parse(json);
                                vc.component.chooseOrgInfo.orgs = _orgInfo.orgs;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            chooseOrg:function(_org){
                if(_org.hasOwnProperty('name')){
                     _org.orgName = _org.name;
                }
                vc.emit($props.emitChooseOrg,'chooseOrg',_org);
                vc.emit($props.emitLoadData,'listOrgData',{
                    orgId:_org.orgId
                });
                $('#chooseOrgModel').modal('hide');
            },
            queryOrgs:function(){
                vc.component._loadAllOrgInfo(1,10,vc.component.chooseOrgInfo._currentOrgName);
            },
            _refreshChooseOrgInfo:function(){
                vc.component.chooseOrgInfo._currentOrgName = "";
            }
        }

    });
})(window.vc);

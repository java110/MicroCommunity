(function(vc){
    vc.extends({
        data:{
            addOrgCommunityInfo:{
                communitys:[],
                communityName:'',
                selectCommunitys:[]
            }
        },
        _initMethod:function(){
        },
        _initEvent:function(){
            vc.on('addOrgCommunity','openAddOrgCommunityModal',function(_param){
                $('#addOrgCommunityModel').modal('show');
                vc.copyObject(_param,vc.component.addOrgCommunityInfo);
                vc.component._refreshChooseOrgInfo();
                vc.component._loadAllCommunityInfo(1,10,'');
            });
        },
        methods:{
            _loadAllCommunityInfo:function(_page,_row,_name){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        name:_name,
                        orgLevel:vc.component.addOrgCommunityInfo.orgLevel,
                        parentOrgId:vc.component.addOrgCommunityInfo.parentOrgId,
                    }
                };

                //发送get请求
               vc.http.get('addOrgCommunity',
                            'list',
                             param,
                             function(json){
                                var _orgInfo = JSON.parse(json);
                                vc.component.addOrgCommunityInfo.orgs = _orgInfo.orgs;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            addOrgCommunity:function(_org){
                if(_org.hasOwnProperty('name')){
                     _org.orgName = _org.name;
                }
                vc.emit($props.emitChooseOrg,'addOrgCommunity',_org);
                vc.emit($props.emitLoadData,'listOrgData',{
                    orgId:_org.orgId
                });
                $('#addOrgCommunityModel').modal('hide');
            },
            queryCommunitys:function(){
                vc.component._loadAllCommunityInfo(1,10,vc.component.addOrgCommunityInfo.communityName);
            },
            _refreshChooseOrgInfo:function(){
                vc.component.addOrgCommunityInfo={
                    communitys:[],
                    communityName:'',
                    selectCommunitys:[]
                };
            }
        }

    });
})(window.vc);

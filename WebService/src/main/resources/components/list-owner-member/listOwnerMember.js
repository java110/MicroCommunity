(function(vc){
    //员工权限
    vc.extends({
        data:{
            memberInfo:{
                members:[],
                _currentOwnerId:'',
            }
        },
        _initMethod:function(){

        },
        _initEvent:function(){

            vc.on('member','loadOwner',function(_param){
                vc.component._loadOwners(_param);
            });
            vc.on('member','loadData',function(_param){
                vc.component._loadOwners(_param);
            });
        },
        methods:{
            _loadOwners:function(_param){
                vc.component.memberInfo._currentOwnerId=_param.ownerId;
                var param = {
                    params:{
                        page:1,
                        row:15,
                        ownerId:_param.ownerId,
                        communityId:vc.getCurrentCommunity().communityId,
                        ownerTypeCd:'1002'
                    }
                };
             //发送get请求
            vc.http.get('listOwnerMember',
                         'list',
                          param,
                          function(json){
                             var _memberInfo = JSON.parse(json);
                             vc.component.memberInfo.members = _memberInfo.owners;

                          },function(){
                             console.log('请求失败处理');
                          });
            },
            _openDeleteOwnerModel:function(_member){
                _member.ownerId = vc.component.memberInfo._currentOwnerId;
                vc.emit('deleteOwner','openOwnerModel',_member);
            },
            _openEditOwnerModel:function(_member){
                _member.ownerId = vc.component.memberInfo._currentOwnerId;
                vc.emit('editOwner','openOwnerModel',_member);
            }
        }
    });

})(window.vc);
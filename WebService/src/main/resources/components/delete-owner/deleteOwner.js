(function(vc){
    vc.extends({
        data:{
            deleteOwnerInfo:{}
        },
        _initEvent:function(){
             vc.on('deleteOwner','openOwnerModel',function(_ownerInfo){
                    vc.component.deleteOwnerInfo = _ownerInfo;
                    $('#deleteOwnerModel').modal('show');
                });
        },
        methods:{
            closeDeleteOwnerModel:function(){
                $('#deleteOwnerModel').modal('hide');
            },
            deleteOwner:function(){

                vc.component.deleteOwnerInfo.communityId = vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'deleteOwner',
                    'delete',
                    JSON.stringify(vc.component.deleteOwnerInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deleteOwnerModel').modal('hide');
                            vc.emit('listOwner','listOwnerData',{});
                            return ;
                        }
                        vc.component.deleteOwnernfo.errorInfo = json;
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.component.deleteOwnernfo.errorInfo = errInfo;
                     });
            }
        }
    });
})(window.vc);
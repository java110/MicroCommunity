(function(vc){
    vc.extends({
        data:{
            storeExitCommunityInfo:{
                communityInfo:{}
            }
        },
        _initEvent:function(){
             vc.on('storeExitCommunity','openStoreExitCommunityModal',function(_communityInfo){
                    vc.component.storeExitCommunityInfo.communityInfo = _communityInfo;
                    $('#storeExitCommunityModal').modal('show');
                });
        },
        methods:{
            closeStoreExitCommunityModal:function(){
                $('#storeExitCommunityModal').modal('hide');
            },
            deleteExitCommunity:function(){
                console.log("开始删除工号：",vc.component.storeExitCommunityInfo);
                vc.http.post(
                    'storeExitCommunity',
                    'exit',
                    JSON.stringify(vc.component.storeExitCommunityInfo.communityInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deleteStaffModel').modal('hide');
                            vc.component.$emit('deleteStaff_reload_event',{});
                            return ;
                        }
                        vc.component.deleteStaffInfo.errorInfo = json;
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.component.deleteStaffInfo.errorInfo = errInfo;
                     });
            }
        }
    });
})(window.vc);
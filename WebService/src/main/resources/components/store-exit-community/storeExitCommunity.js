(function(vc){
    vc.extends({
        data:{
            storeExitCommunityInfo:{
                communityInfo:{},
                errorInfo:''
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
                            $('#storeExitCommunityModal').modal('hide');
                            vc.emit('enterCommunity','listMyCommunity',{});
                            return ;
                        }
                        vc.component.storeExitCommunityInfo.errorInfo = json;
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.component.storeExitCommunityInfo.errorInfo = errInfo;
                     });
            }

        }
    });
})(window.vc);
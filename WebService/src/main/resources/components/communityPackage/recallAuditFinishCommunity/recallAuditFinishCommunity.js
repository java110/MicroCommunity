(function(vc,vm){

    vc.extends({
        data:{
            recallAuditFinishCommunityInfo:{

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('recallAuditFinishCommunity','openRecallAuditFinishCommunityModal',function(_params){

                vc.component.recallAuditFinishCommunityInfo = _params;
                $('#recallAuditFinishCommunityModel').modal('show');

            });
        },
        methods:{
            recallAuditFinishCommunity:function(){
                var _paramObj = {
                    communityId:vc.component.recallAuditFinishCommunityInfo.communityId,
                    state:'1000',
                    remark:'撤回审核'
                };
                vc.http.post(
                    'recallAuditFinishCommunity',
                    'recall',
                    JSON.stringify(_paramObj),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#recallAuditFinishCommunityModel').modal('hide');
                            vc.emit('communityManage','listCommunity',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(json);

                     });
            },
            closeRecallAuditFinishCommunityModel:function(){
                $('#recallAuditFinishCommunityModel').modal('hide');
            }
        }
    });

})(window.vc,window.vc.component);

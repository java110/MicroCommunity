(function(vc,vm){

    vc.extends({
        data:{
            recallAuditEnterFinishCommunityInfo:{

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('recallAuditEnterFinishCommunity','openRecallAuditEnterFinishCommunityModal',function(_params){

                vc.component.recallAuditEnterFinishCommunityInfo = _params;
                $('#recallAuditEnterFinishCommunityModel').modal('show');

            });
        },
        methods:{
            recallAuditEnterFinishCommunity:function(){
                var _paramObj = {
                    communityMemberId:vc.component.recallAuditEnterFinishCommunityInfo.communityMemberId,
                    state:'1000',
                    remark:'撤回审核'
                };
                vc.http.post(
                    'recallAuditEnterFinishCommunity',
                    'recall',
                    JSON.stringify(_paramObj),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#recallAuditEnterFinishCommunityModel').modal('hide');
                            vc.emit('auditEnterCommunityManage','listCommunity',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(json);

                     });
            },
            closeRecallAuditEnterFinishCommunityModel:function(){
                $('#recallAuditEnterFinishCommunityModel').modal('hide');
            }
        }
    });

})(window.vc,window.vc.component);

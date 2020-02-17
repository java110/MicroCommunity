(function(vc,vm){

    vc.extends({
        data:{
            deleteInspectionPlanInfo:{

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('deleteInspectionPlan','openDeleteInspectionPlanModal',function(_params){

                vc.component.deleteInspectionPlanInfo = _params;
                $('#deleteInspectionPlanModel').modal('show');

            });
        },
        methods:{
            deleteInspectionPlan:function(){
                vc.component.deleteInspectionPlanInfo.communityId=vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'deleteInspectionPlan',
                    'delete',
                    JSON.stringify(vc.component.deleteInspectionPlanInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deleteInspectionPlanModel').modal('hide');
                            vc.emit('inspectionPlanManage','listInspectionPlan',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(json);

                     });
            },
            closeDeleteInspectionPlanModel:function(){
                $('#deleteInspectionPlanModel').modal('hide');
            }
        }
    });

})(window.vc,window.vc.component);

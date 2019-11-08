(function(vc,vm){

    vc.extends({
        data:{
            deleteMachineInfo:{

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('deleteMachine','openDeleteMachineModal',function(_params){

                vc.component.deleteMachineInfo = _params;
                $('#deleteMachineModel').modal('show');

            });
        },
        methods:{
            deleteMachine:function(){
                vc.component.deleteMachineInfo.communityId=vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'deleteMachine',
                    'delete',
                    JSON.stringify(vc.component.deleteMachineInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deleteMachineModel').modal('hide');
                            vc.emit('machineManage','listMachine',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(json);

                     });
            },
            closeDeleteMachineModel:function(){
                $('#deleteMachineModel').modal('hide');
            }
        }
    });

})(window.vc,window.vc.component);

(function(vc,vm){

    vc.extends({
        data:{
            deleteMachineTranslateInfo:{

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('deleteMachineTranslate','openDeleteMachineTranslateModal',function(_params){

                vc.component.deleteMachineTranslateInfo = _params;
                $('#deleteMachineTranslateModel').modal('show');

            });
        },
        methods:{
            deleteMachineTranslate:function(){
                vc.component.deleteMachineTranslateInfo.communityId=vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'deleteMachineTranslate',
                    'delete',
                    JSON.stringify(vc.component.deleteMachineTranslateInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deleteMachineTranslateModel').modal('hide');
                            vc.emit('machineTranslateManage','listMachineTranslate',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(json);

                     });
            },
            closeDeleteMachineTranslateModel:function(){
                $('#deleteMachineTranslateModel').modal('hide');
            }
        }
    });

})(window.vc,window.vc.component);

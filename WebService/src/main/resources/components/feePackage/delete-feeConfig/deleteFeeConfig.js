(function(vc,vm){

    vc.extends({
        data:{
            deleteFeeConfigInfo:{

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('deleteFeeConfig','openDeleteFeeConfigModal',function(_params){

                vc.component.deleteFeeConfigInfo = _params;
                $('#deleteFeeConfigModel').modal('show');

            });
        },
        methods:{
            deleteFeeConfig:function(){
                vc.component.deleteFeeConfigInfo.communityId=vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'deleteFeeConfig',
                    'delete',
                    JSON.stringify(vc.component.deleteFeeConfigInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deleteFeeConfigModel').modal('hide');
                            vc.emit('feeConfigManage','listFeeConfig',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(json);

                     });
            },
            closeDeleteFeeConfigModel:function(){
                $('#deleteFeeConfigModel').modal('hide');
            }
        }
    });

})(window.vc,window.vc.component);

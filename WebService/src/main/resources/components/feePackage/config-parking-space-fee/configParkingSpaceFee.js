(function(vc,vm){

    vc.extends({
        data:{
            changeFeeConfigInfo:{
                configId:"",
                feeName:'',
                squarePrice:'0.00',
                additionalAmount:"",
                feeTypeCd:'',
            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('configParkingSpaceFee','openConfigParkingSpaceFeeModel',function(_params){
                vc.copyObject(_params, vc.component.changeFeeConfigInfo);
                $('#configParkingSpaceFeeModel').modal('show');
            });
        },
        methods:{
            /**
                根据楼ID加载房屋
            **/

            changeFeeConfigValidate:function(){
                        return vc.validate.validate({
                            changeFeeConfigInfo:vc.component.changeFeeConfigInfo
                        },{

                            'changeFeeConfigInfo.additionalAmount':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"附加费不能为空"
                                },
                                {
                                    limit:"money",
                                    param:"",
                                    errInfo:"必须是金额，如300.00"
                                },
                            ],


                        });
             },
            saveParkingSpaceConfigFee:function(){
                if(!vc.component.changeFeeConfigValidate()){
                    vc.toast(vc.validate.errInfo);
                    return ;
                }
                vc.component.changeFeeConfigInfo.communityId = vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'configParkingSpaceFee',
                    'change',
                    JSON.stringify(vc.component.changeFeeConfigInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#configParkingSpaceFeeModel').modal('hide');
                            vc.emit('viewParkingSpaceFeeConfig','loadParkingSpaceConfigFee',vc.component.changeFeeConfigInfo);
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.message(errInfo);
                     });
            }
        }
    });

})(window.vc,window.vc.component);
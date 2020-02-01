(function(vc,vm){

    vc.extends({
        propTypes: {
            emitReloadFee:vc.propTypes.string
        },
        data:{
            propertyPayInfo:{
                cycles:'',
                receivableAmount:'0.00',
                receivedAmount:'0.00',
                remark:'',
                feeId:'',
                configId:'',
                builtUpArea:'',
                squarePrice:'',
                additionalAmount:'',
                communityId:''
            }
        },
        watch:{
            "propertyPayInfo.cycles":{//深度监听，可监听到对象、数组的变化
                handler(val, oldVal){
                    vc.component.propertyPayInfo.receivableAmount = ((parseFloat(vc.component.propertyPayInfo.builtUpArea)
                                                                    * parseFloat(vc.component.propertyPayInfo.squarePrice)
                                                                    + parseFloat(vc.component.propertyPayInfo.additionalAmount)) * parseFloat(val)).toFixed(2);
                    vc.component.propertyPayInfo.receivedAmount = vc.component.propertyPayInfo.receivableAmount;
                },
                deep:true
            }
         },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('propertyPay','openPayModel',function(_params){
                vc.component.refreshPropertyPayInfo();

                $('#propertyPayModel').modal('show');
                vc.component.propertyPayInfo.feeId = _params.feeId;
                vc.component.propertyPayInfo.configId = _params.configId;
                vc.component.propertyPayInfo.builtUpArea = _params.builtUpArea;
                vc.component.propertyPayInfo.communityId = vc.getCurrentCommunity().communityId;

                 vc.component.loadPropertyConfigFee();
            });
        },
        methods:{

            payFeeValidate:function(){
                        return vc.validate.validate({
                            propertyPayInfo:vc.component.propertyPayInfo
                        },{
                            'propertyPayInfo.feeId':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"费用ID不能为空"
                                }
                            ],
                            'propertyPayInfo.cycles':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"缴费周期不能为空"
                                }
                            ],
                            'propertyPayInfo.receivableAmount':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"应收金额不能为空"
                                },
                                {
                                    limit:"money",
                                    param:"",
                                    errInfo:"应收金额不是有效的金额"
                                }
                            ],
                            'propertyPayInfo.receivedAmount':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"实收金额不能为空"
                                },
                                {
                                    limit:"money",
                                    param:"",
                                    errInfo:"实收金额不是有效的金额"
                                }
                            ],

                            'propertyPayInfo.remark':[
                                {
                                    limit:"maxLength",
                                    param:"200",
                                    errInfo:"备注长度不能超过200位"
                                },
                            ]

                        });
             },
            payFee:function(){
                if(!vc.component.payFeeValidate()){
                    vc.toast(vc.validate.errInfo);
                    return ;
                }

                vc.http.post(
                    'propertyPay',
                    'payFee',
                    JSON.stringify(vc.component.propertyPayInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#propertyPayModel').modal('hide');
                            vc.emit('propertyFee','listFeeDetail',vc.component.propertyPayInfo);
                            vc.emit($props.emitReloadFee,'reloadFee',vc.component.propertyPayInfo);
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(errInfo);
                     });
            },
            refreshPropertyPayInfo:function(){
                vc.component.propertyPayInfo={
                                             cycles:'',
                                             receivableAmount:'0.00',
                                             receivedAmount:'0.00',
                                             remark:'',
                                             builtUpArea:'',
                                             feeId:'',
                                             configId:'',
                                             squarePrice:'',
                                             additionalAmount:'',
                                             communityId:''
                                         };


            },
            //加载配置数据
            loadPropertyConfigFee:function(){
                var param = {
                    params:{
                        communityId:vc.getCurrentCommunity().communityId,
                        configId:vc.component.propertyPayInfo.configId,
                        page:1,
                        row:1
                    }
                };
                vc.http.get(
                    'propertyPay',
                    'loadPropertyConfigData',
                     param,
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            var _feeConfig  = JSON.parse(json).feeConfigs[0];
                            vc.copyObject(_feeConfig, vc.component.propertyPayInfo);
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
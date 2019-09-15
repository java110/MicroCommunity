/**
    出租车辆费用
**/
(function(vc){

    vc.extends({
        propTypes: {
            callBackComponent:vc.propTypes.string,
            callBackFunction:vc.propTypes.string
        },
        data:{
            sellParkingSpaceFeeInfo:{
                flowComponent:'sellParkingSpaceFeeInfo',
                receivableAmount: "0.00",
                receivedAmount:"0.00",
                additionalAmount:'0.00',
                sellOrHire:"S",
                typeCd:'',
            }
        },
        watch:{
            sellParkingSpaceFeeInfo:{
                deep: true,
                handler:function(){
                    //console.log("hireParkingSpaceFeeInfo 被调用")
                    vc.component.saveSellParkingSpaceFee();
                }
             }
        },
        _initMethod:function(){

        },
        _initEvent:function(){
            vc.on('sellParkingSpaceFee', 'onIndex', function(_index){
                vc.component.sellParkingSpaceFeeInfo.index = _index;
            });

            vc.on('sellParkingSpaceFee', 'callBackOwnerInfo', function(_info){
                vc.component.saveSellParkingSpaceFee();
            });

            vc.on('sellParkingSpaceFee', 'parkingSpaceInfo',function(_parkingSpaceInfo){
                vc.component.sellParkingSpaceFeeInfo.typeCd = _parkingSpaceInfo.typeCd;
                vc.component._loadFireParkingSpaceFee();
            });

        },
        methods:{
            sellParkingSpaceFeeValidate:function(){
                    return vc.validate.validate({
                            sellParkingSpaceFeeInfo:vc.component.sellParkingSpaceFeeInfo
                        },{
                            'sellParkingSpaceFeeInfo.receivedAmount':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"实收金额不能为空"
                                },
                                {
                                    limit:"money",
                                    param:"",
                                    errInfo:"实收金额格式错误，如3.00"
                                }
                            ]
                        });
            },
            saveSellParkingSpaceFee:function(){
                if(vc.component.sellParkingSpaceFeeValidate()){
                    //侦听回传
                    vc.emit($props.callBackComponent,$props.callBackFunction, vc.component.sellParkingSpaceFeeInfo);
                    return ;
                }
            },
            _loadFireParkingSpaceFee:function(){
                //
                var param = {
                        params:{
                            communityId:vc.getCurrentCommunity().communityId,
                            typeCd:vc.component.sellParkingSpaceFeeInfo.typeCd
                        }
                    };
                    vc.http.get(
                        'sellParkingSpaceFee',
                        'loadSellParkingSpaceConfigData',
                         param,
                         function(json,res){
                            //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                            if(res.status == 200){
                                //关闭model
                                var configFee = JSON.parse(json);
                                vc.component.sellParkingSpaceFeeInfo.receivableAmount = configFee.additionalAmount;
                                vc.component.sellParkingSpaceFeeInfo.additionalAmount = configFee.additionalAmount;
                                vc.component.sellParkingSpaceFeeInfo.receivedAmount = configFee.additionalAmount;

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

})(window.vc);
/**
    权限组
**/
(function(vc){

    vc.extends({
        data:{
            feeConfigUpHireInfo:{
                configId:"",
                additionalAmount:"",
                feeName:"出租地上停车费",
                feeTypeCd:'888800010004'
            },
            feeConfigDownHireInfo:{
                configId:"",
                additionalAmount:"",
                feeName:"出租地下停车费",
                feeTypeCd:'888800010005'
            },
            feeConfigUpSellInfo:{
                configId:"",
                additionalAmount:"",
                feeName:"出售地上停车费",
                feeTypeCd:'888800010002'
            },
            feeConfigDownSellInfo:{
                configId:"",
                additionalAmount:"",
                feeName:"出售地下停车费",
                feeTypeCd:'888800010003'
            },
             feeConfigDownTempInfo:{
                 configId:"",
                 additionalAmount:"",
                 squarePrice:"",
                 feeName:"临时地下停车费",
                 feeTypeCd:'888800010007'
             }
        },
        _initMethod:function(){
                vc.component.loadParkingSpaceConfigFee(vc.component.feeConfigUpHireInfo);
                vc.component.loadParkingSpaceConfigFee(vc.component.feeConfigDownHireInfo);
                vc.component.loadParkingSpaceConfigFee(vc.component.feeConfigUpSellInfo);
                vc.component.loadParkingSpaceConfigFee(vc.component.feeConfigDownSellInfo);
                vc.component.loadParkingSpaceConfigFee(vc.component.feeConfigDownTempInfo);
        },
        _initEvent:function(){
            vc.on('viewParkingSpaceFeeConfig','loadParkingSpaceConfigFee',function(_param){
                vc.component.loadParkingSpaceConfigFee(_param);
            });

        },
        methods:{

            openConfigParkingSpaceFeeModel:function(_feeInfo){
                vc.emit('configParkingSpaceFee','openConfigParkingSpaceFeeModel',_feeInfo);
            },
            openConfigParkingSpaceTempFeeModel:function(_feeInfo){
                vc.emit('configFeeTempConfigInfo','openConfigParkingSpaceFeeModel',_feeInfo);
            },
            loadParkingSpaceConfigFee:function(_feeInfo){
                var param = {
                    params:{
                        communityId:vc.getCurrentCommunity().communityId,
                        configId:_feeInfo.configId,
                        feeTypeCd:_feeInfo.feeTypeCd
                    }
                };
                vc.http.get(
                    'viewParkingSpaceFeeConfig',
                    'loadData',
                     param,
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            if(_feeInfo.feeTypeCd == vc.component.feeConfigUpHireInfo.feeTypeCd){
                                   vc.copyObject(JSON.parse(json), vc.component.feeConfigUpHireInfo);
                            }else if(_feeInfo.feeTypeCd == vc.component.feeConfigDownHireInfo.feeTypeCd){
                                   vc.copyObject(JSON.parse(json), vc.component.feeConfigDownHireInfo);
                            }else if(_feeInfo.feeTypeCd == vc.component.feeConfigUpSellInfo.feeTypeCd){
                                   vc.copyObject(JSON.parse(json), vc.component.feeConfigUpSellInfo);
                           }else if(_feeInfo.feeTypeCd == vc.component.feeConfigDownTempInfo.feeTypeCd){
                                   vc.copyObject(JSON.parse(json), vc.component.feeConfigDownTempInfo);
                            }else if(_feeInfo.feeTypeCd == vc.component.feeConfigUpTempInfo.feeTypeCd){
                                   vc.copyObject(JSON.parse(json), vc.component.feeConfigUpTempInfo);
                            }else{
                                   vc.copyObject(JSON.parse(json), vc.component.feeConfigDownSellInfo);
                            }
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
/**
    权限组
**/
(function(vc){

    vc.extends({
        propTypes: {
            feeName:vc.propTypes.string,
            payName:vc.propTypes.string
        },
        data:{
            mainParkingSpaceFeeInfo:{
                feeName:$props.feeName,
                feeId:"",
                feeTypeCd:"",
                psId:"",
                num:"",
                typeCd:"",
                carNum:"",
                carBrand:"",
                carType:"",
                startTime:"",
                endTime:"",
                amount:"-1.00"
            }
        },
        _initMethod:function(){

        },
        _initEvent:function(){
            vc.on('viewMainParkingSpaceFee','chooseParkingSpace',function(_parkingSPace){
                  vc.component.loadMainParkingSpaceFeeInfo(_parkingSPace);
            });

            vc.on('viewMainFee','reloadFee',function(_room){
                if(vc.component.mainParkingSpaceFeeInfo.roomId != ''){
                      vc.component.loadMainFeeInfo({
                            roomId:vc.component.mainParkingSpaceFeeInfo.roomId
                      });

                }
            });
        },
        methods:{

            openSearchParkingSpaceModel:function(){
                vc.emit('searchParkingSpace','openSearchParkingSpaceModel',{});
            },
            openPayModel:function(){
                vc.emit($props.payName,'openPayModel',{
                    feeId:vc.component.mainParkingSpaceFeeInfo.feeId,
                    feeTypeCd:vc.component.mainParkingSpaceFeeInfo.feeTypeCd,
                    builtUpArea:"0.00"
                });
            },
            loadMainParkingSpaceFeeInfo:function(_parkingSPace){
                //vc.copyObject(_fee,vc.component.mainParkingSpaceFeeInfo);
                var param = {
                    params:{
                        communityId:vc.getCurrentCommunity().communityId,
                        psId:_parkingSPace.psId,
                    }
                };

                //发送get请求
               vc.http.get('viewMainParkingSpaceFee',
                            'getFee',
                             param,
                             function(json,res){
                               var _fee =JSON.parse(json);
                               vc.copyObject(_fee,vc.component.mainParkingSpaceFeeInfo);
                               vc.emit('propertyFee','listFeeDetail',{
                                    feeId:_fee.feeId
                               });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            }

        }
    });

})(window.vc);
/**
    权限组
**/
(function(vc){

    vc.extends({
        propTypes: {
            feeName:vc.propTypes.string,
            feeTypeCd:vc.propTypes.string,
            payName:vc.propTypes.string
        },
        data:{
            mainFeeInfo:{
                feeName:$props.feeName,
                feeId:"",
                feeTypeCd:'',
                floorNum:"",
                roomId:"",
                roomNum:"",
                builtUpArea:"",
                ownerId:"",
                ownerName:"",
                link:"",
                startTime:"",
                endTime:"",
                amount:"-1.00",
                feeFlagName:'',
                feeTypeCdName:'',
                configId:'',
                stateName:''

            }
        },
        _initMethod:function(){
             //加载 业主信息
            var _feeId = vc.getParam('feeId')

            if(vc.notNull(_feeId)){
                vc.component.loadMainFeeInfo({
                    feeId:_feeId
                });
            }

        },
        _initEvent:function(){
            vc.on('viewMainFee','chooseRoom',function(_room){
                  vc.component.loadMainFeeInfo(_room);
            });

            vc.on('viewMainFee','reloadFee',function(_room){
                if(vc.component.mainFeeInfo.roomId != ''){
                      vc.component.loadMainFeeInfo({
                            roomId:vc.component.mainFeeInfo.roomId
                      });

                }
            });
        },
        methods:{

            openSearchRoomModel:function(){
                vc.emit('searchRoom','openSearchRoomModel',{});
            },
            openPayModel:function(){
                vc.emit($props.payName,'openPayModel',{
                    feeId:vc.component.mainFeeInfo.feeId,
                    configId:vc.component.mainFeeInfo.configId,
                    builtUpArea:vc.component.mainFeeInfo.builtUpArea
                });
            },
            loadMainFeeInfo:function(_fee){
                //vc.copyObject(_fee,vc.component.mainFeeInfo);
                var param = {
                    params:{
                        communityId:vc.getCurrentCommunity().communityId,
                        feeId:_fee.feeId,
                        row:1,
                        page:1
                    }
                };

                //发送get请求
               vc.http.get('viewMainFee',
                            'getFee',
                             param,
                             function(json,res){
                               var _fee =JSON.parse(json).fees[0];
                               vc.copyObject(_fee,vc.component.mainFeeInfo);
                               vc.emit('propertyFee','listFeeDetail',{
                                    feeId:_fee.feeId
                               });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _openCallBackOwner:function(){
                vc.getBack();
            }

        }
    });

})(window.vc);
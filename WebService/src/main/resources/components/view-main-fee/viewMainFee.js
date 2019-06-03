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
                floorNum:"",
                roomId:"",
                roomNum:"",
                builtUpArea:"",
                ownerId:"",
                ownerName:"",
                link:"",
                startTime:"",
                endTime:"",
                amount:"-1.00"
            }
        },
        _initMethod:function(){

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
                    builtUpArea:vc.component.mainFeeInfo.builtUpArea
                });
            },
            loadMainFeeInfo:function(_room){
                //vc.copyObject(_fee,vc.component.mainFeeInfo);
                var param = {
                    params:{
                        communityId:vc.getCurrentCommunity().communityId,
                        roomId:_room.roomId,
                        feeTypeCd:$props.feeTypeCd
                    }
                };

                //发送get请求
               vc.http.get('viewMainFee',
                            'getFee',
                             param,
                             function(json,res){
                               var _fee =JSON.parse(json);
                               vc.copyObject(_fee,vc.component.mainFeeInfo);
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
/**
    入驻小区
**/
(function(vc){
    vc.extends({
        data:{
            sellParkingSpaceInfo:{
                $step:{},
                index:0,
                infos:[],
            }
        },
        _initMethod:function(){
            vc.component._initStep();
        },

        _initEvent:function(){

           vc.on("sellParkingSpace", "notify", function(_info){
                vc.component.sellParkingSpaceInfo.infos[vc.component.sellParkingSpaceInfo.index] = _info;

                /*if(vc.component.sellParkingSpaceInfo.index == 0){
                    vc.emit('searchRoom','listenerFloorInfo',_info);
                }*/
            });

        },
        methods:{
            _initStep:function(){
                vc.component.sellParkingSpaceInfo.$step = $("#step");
                vc.component.sellParkingSpaceInfo.$step.step({
                    index: 0,
                    time: 500,
                    title: ["选择车位","业主信息","车辆信息","收费信息"]
                });
                vc.component.sellParkingSpaceInfo.index = vc.component.sellParkingSpaceInfo.$step.getIndex();
            },
            _prevStep:function(){
                vc.component.sellParkingSpaceInfo.$step.prevStep();
                vc.component.sellParkingSpaceInfo.index = vc.component.sellParkingSpaceInfo.$step.getIndex();

                vc.emit('viewSelectParkingSpace', 'onIndex', vc.component.sellParkingSpaceInfo.index);
                vc.emit('viewOwnerInfo', 'onIndex', vc.component.sellParkingSpaceInfo.index);
                vc.emit('addCar', 'onIndex', vc.component.sellParkingSpaceInfo.index);
                vc.emit('parkingSpaceFee', 'onIndex', vc.component.sellParkingSpaceInfo.index);

                if(vc.component.sellParkingSpaceInfo.index == 1){
                    vc.emit('viewOwnerInfo','callBackOwnerInfo',{});
                }

            },
            _nextStep:function(){
                var _currentData = vc.component.sellParkingSpaceInfo.infos[vc.component.sellParkingSpaceInfo.index];
                if( _currentData == null || _currentData == undefined){
                    vc.message("请选择或填写必选信息");
                    return ;
                }
                vc.component.sellParkingSpaceInfo.$step.nextStep();
                vc.component.sellParkingSpaceInfo.index = vc.component.sellParkingSpaceInfo.$step.getIndex();

                vc.emit('viewSelectParkingSpace', 'onIndex', vc.component.sellParkingSpaceInfo.index);
                vc.emit('viewOwnerInfo', 'onIndex', vc.component.sellParkingSpaceInfo.index);
                vc.emit('addCar', 'onIndex', vc.component.sellParkingSpaceInfo.index);
                vc.emit('parkingSpaceFee', 'onIndex', vc.component.sellParkingSpaceInfo.index);
                if(vc.component.sellParkingSpaceInfo.index == 1){
                    vc.emit('viewOwnerInfo','callBackOwnerInfo',{});
                }

            },
            _finishStep:function(){


                var _currentData = vc.component.sellParkingSpaceInfo.infos[vc.component.sellParkingSpaceInfo.index];
                if( _currentData == null || _currentData == undefined){
                    vc.message("请选择或填写必选信息");
                    return ;
                }

                var param = {
                    communityId:vc.getCurrentCommunity().communityId,
                    data:vc.component.sellParkingSpaceInfo.infos
                }

               vc.http.post(
                   'sellParkingSpace',
                   'sell',
                   JSON.stringify(param),
                   {
                       emulateJSON:true
                    },
                    function(json,res){
                       //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                       if(res.status == 200){
                           //关闭model
                           vc.jumpToPage("/flow/ownerParkingSpaceFlow?" + vc.objToGetParam(JSON.parse(json)));
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
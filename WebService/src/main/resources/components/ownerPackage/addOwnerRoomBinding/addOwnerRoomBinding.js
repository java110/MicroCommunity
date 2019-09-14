/**
    入驻小区
**/
(function(vc){
    vc.extends({
        data:{
            addOwnerRoomBindingInfo:{
                $step:{},
                index:0,
                infos:[]
            }
        },
        _initMethod:function(){
            vc.component._initStep();
        },
        _initEvent:function(){
            vc.on("addOwnerRoomBinding", "notify", function(_info){
                vc.component.addOwnerRoomBindingInfo.infos[vc.component.addOwnerRoomBindingInfo.index] = _info;

                if(vc.component.addOwnerRoomBindingInfo.index == 0){
                    vc.emit('searchRoom','listenerFloorInfo',_info);
                }
            });

        },
        methods:{
            _initStep:function(){
                vc.component.addOwnerRoomBindingInfo.$step = $("#step");
                vc.component.addOwnerRoomBindingInfo.$step.step({
                    index: 0,
                    time: 500,
                    title: ["选择楼栋","选择房屋","业主信息"]
                });
                vc.component.addOwnerRoomBindingInfo.index = vc.component.addOwnerRoomBindingInfo.$step.getIndex();
            },
            _prevStep:function(){
                vc.component.addOwnerRoomBindingInfo.$step.prevStep();
                vc.component.addOwnerRoomBindingInfo.index = vc.component.addOwnerRoomBindingInfo.$step.getIndex();

                vc.emit('viewFloorInfo', 'onIndex', vc.component.addOwnerRoomBindingInfo.index);
                vc.emit('sellRoomSelectRoom', 'onIndex', vc.component.addOwnerRoomBindingInfo.index);
                vc.emit('viewOwnerInfo', 'onIndex', vc.component.addOwnerRoomBindingInfo.index);
                if(vc.component.hireParkingSpaceInfo.index == 2){
                    vc.emit('viewOwnerInfo','callBackOwnerInfo',{});
                }

            },
            _nextStep:function(){
                var _currentData = vc.component.addOwnerRoomBindingInfo.infos[vc.component.addOwnerRoomBindingInfo.index];
                if( _currentData == null || _currentData == undefined){
                    vc.message("请选择或填写必选信息");
                    return ;
                }
                vc.component.addOwnerRoomBindingInfo.$step.nextStep();
                vc.component.addOwnerRoomBindingInfo.index = vc.component.addOwnerRoomBindingInfo.$step.getIndex();

                 vc.emit('viewFloorInfo', 'onIndex', vc.component.addOwnerRoomBindingInfo.index);
                vc.emit('sellRoomSelectRoom', 'onIndex', vc.component.addOwnerRoomBindingInfo.index);
                vc.emit('viewOwnerInfo', 'onIndex', vc.component.addOwnerRoomBindingInfo.index);
                if(vc.component.hireParkingSpaceInfo.index == 2){
                    vc.emit('viewOwnerInfo','callBackOwnerInfo',{});
                }

            },
            _finishStep:function(){


                var _currentData = vc.component.addOwnerRoomBindingInfo.infos[vc.component.addOwnerRoomBindingInfo.index];
                if( _currentData == null || _currentData == undefined){
                    vc.message("请选择或填写必选信息");
                    return ;
                }

                var param = {
                    communityId:vc.getCurrentCommunity().communityId,
                    data:vc.component.addOwnerRoomBindingInfo.infos
                }

               vc.http.post(
                   'addOwnerRoomBinding',
                   'binding',
                   JSON.stringify(param),
                   {
                       emulateJSON:true
                    },
                    function(json,res){
                       if(res.status == 200){

                           vc.message('处理成功',true);
                           //关闭model
                           vc.jumpToPage("/flow/roomFlow?" + vc.objToGetParam(JSON.parse(json)));
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

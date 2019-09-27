/**
    入驻小区
**/
(function(vc){
    vc.extends({
        data:{
            addRoomBindingInfo:{
                $step:{},
                index:0,
                infos:[]
            }
        },
        _initMethod:function(){
            vc.component._initStep();
        },
        _initEvent:function(){
            vc.on("addRoomBinding", "notify", function(_info){
                vc.component.addRoomBindingInfo.infos[vc.component.addRoomBindingInfo.index] = _info;
            });

        },
        methods:{
            _initStep:function(){
                vc.component.addRoomBindingInfo.$step = $("#step");
                vc.component.addRoomBindingInfo.$step.step({
                    index: 0,
                    time: 500,
                    title: ["选择楼","选择单元","添加房屋"]
                });
                vc.component.addRoomBindingInfo.index = vc.component.addRoomBindingInfo.$step.getIndex();
            },
            _prevStep:function(){
                vc.component.addRoomBindingInfo.$step.prevStep();
                vc.component.addRoomBindingInfo.index = vc.component.addRoomBindingInfo.$step.getIndex();

                vc.emit('viewFloorInfo', 'onIndex', vc.component.addRoomBindingInfo.index);
                vc.emit('viewUnitInfo', 'onIndex', vc.component.addRoomBindingInfo.index);
                vc.emit('addRoomView', 'onIndex', vc.component.addRoomBindingInfo.index);

            },
            _nextStep:function(){
                var _currentData = vc.component.addRoomBindingInfo.infos[vc.component.addRoomBindingInfo.index];
                if( _currentData == null || _currentData == undefined){
                    vc.message("请选择或填写必选信息");
                    return ;
                }
                vc.component.addRoomBindingInfo.$step.nextStep();
                vc.component.addRoomBindingInfo.index = vc.component.addRoomBindingInfo.$step.getIndex();

                 vc.emit('viewFloorInfo', 'onIndex', vc.component.addRoomBindingInfo.index);
                vc.emit('viewUnitInfo', 'onIndex', vc.component.addRoomBindingInfo.index);
                vc.emit('addRoomView', 'onIndex', vc.component.addRoomBindingInfo.index);

            },
            _finishStep:function(){


                var _currentData = vc.component.addRoomBindingInfo.infos[vc.component.addRoomBindingInfo.index];
                if ('' == vc.component.addRoomViewInfo.unitPrice || null == vc.component.addRoomViewInfo.unitPrice){
                    vc.component.addRoomViewInfo.unitPrice='0';
                }
                if( _currentData == null || _currentData == undefined){
                    vc.message("请选择或填写必选信息");
                    return ;
                }

                var param = {
                    data:vc.component.addRoomBindingInfo.infos
                }

               vc.http.post(
                   'addRoomBindingBinding',
                   'binding',
                   JSON.stringify(param),
                   {
                       emulateJSON:true
                    },
                    function(json,res){
                       if(res.status == 200){

                           vc.message('处理成功',true);
                           //关闭model
                           var _tmpResJson = JSON.parse(json);
                          /* _tmpResJson[floorName] = vc.component._getFloorName();*/
                           vc.jumpToPage("/flow/roomFlow");
                           return ;
                       }
                       vc.message(json);
                    },
                    function(errInfo,error){
                       console.log('请求失败处理');

                       vc.message(errInfo);
                    });
            },

            _getFloorName:function(){
                var _tmpInfos = vc.component.addRoomBindingInfo.infos;

                for(var _tmpIndex = 0 ; _tmpIndex < _tmpInfos.length; _tmpIndex ++){
                    if(_tmpInfos[_tmpIndex].flowComponent == 'viewFloorInfo'){
                        return _tmpInfos[_tmpIndex].floorName;
                    }
                }

                return "";
            }
        }
    });
})(window.vc);

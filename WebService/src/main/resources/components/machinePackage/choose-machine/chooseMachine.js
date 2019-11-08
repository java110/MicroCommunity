(function(vc){
    vc.extends({
        propTypes: {
           emitChooseMachine:vc.propTypes.string,
           emitLoadData:vc.propTypes.string
        },
        data:{
            chooseMachineInfo:{
                machines:[],
                _currentMachineName:'',
            }
        },
        _initMethod:function(){
        },
        _initEvent:function(){
            vc.on('chooseMachine','openChooseMachineModel',function(_param){
                $('#chooseMachineModel').modal('show');
                vc.component._refreshChooseMachineInfo();
                vc.component._loadAllMachineInfo(1,10,'');
            });
        },
        methods:{
            _loadAllMachineInfo:function(_page,_row,_name){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        name:_name
                    }
                };

                //发送get请求
               vc.http.get('chooseMachine',
                            'list',
                             param,
                             function(json){
                                var _machineInfo = JSON.parse(json);
                                vc.component.chooseMachineInfo.machines = _machineInfo.machines;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            chooseMachine:function(_machine){
                if(_machine.hasOwnProperty('name')){
                     _machine.machineName = _machine.name;
                }
                vc.emit($props.emitChooseMachine,'chooseMachine',_machine);
                vc.emit($props.emitLoadData,'listMachineData',{
                    machineId:_machine.machineId
                });
                $('#chooseMachineModel').modal('hide');
            },
            queryMachines:function(){
                vc.component._loadAllMachineInfo(1,10,vc.component.chooseMachineInfo._currentMachineName);
            },
            _refreshChooseMachineInfo:function(){
                vc.component.chooseMachineInfo._currentMachineName = "";
            }
        }

    });
})(window.vc);

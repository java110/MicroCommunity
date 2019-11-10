(function(vc){
    vc.extends({
        propTypes: {
           emitChooseMachineTranslate:vc.propTypes.string,
           emitLoadData:vc.propTypes.string
        },
        data:{
            chooseMachineTranslateInfo:{
                machineTranslates:[],
                _currentMachineTranslateName:'',
            }
        },
        _initMethod:function(){
        },
        _initEvent:function(){
            vc.on('chooseMachineTranslate','openChooseMachineTranslateModel',function(_param){
                $('#chooseMachineTranslateModel').modal('show');
                vc.component._refreshChooseMachineTranslateInfo();
                vc.component._loadAllMachineTranslateInfo(1,10,'');
            });
        },
        methods:{
            _loadAllMachineTranslateInfo:function(_page,_row,_name){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        name:_name
                    }
                };

                //发送get请求
               vc.http.get('chooseMachineTranslate',
                            'list',
                             param,
                             function(json){
                                var _machineTranslateInfo = JSON.parse(json);
                                vc.component.chooseMachineTranslateInfo.machineTranslates = _machineTranslateInfo.machineTranslates;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            chooseMachineTranslate:function(_machineTranslate){
                if(_machineTranslate.hasOwnProperty('name')){
                     _machineTranslate.machineTranslateName = _machineTranslate.name;
                }
                vc.emit($props.emitChooseMachineTranslate,'chooseMachineTranslate',_machineTranslate);
                vc.emit($props.emitLoadData,'listMachineTranslateData',{
                    machineTranslateId:_machineTranslate.machineTranslateId
                });
                $('#chooseMachineTranslateModel').modal('hide');
            },
            queryMachineTranslates:function(){
                vc.component._loadAllMachineTranslateInfo(1,10,vc.component.chooseMachineTranslateInfo._currentMachineTranslateName);
            },
            _refreshChooseMachineTranslateInfo:function(){
                vc.component.chooseMachineTranslateInfo._currentMachineTranslateName = "";
            }
        }

    });
})(window.vc);

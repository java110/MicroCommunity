(function(vc){
    vc.extends({
        propTypes: {
           emitChooseFloor:vc.propTypes.string,
           emitLoadData:vc.propTypes.string
        },
        data:{
            chooseFloorInfo:{
                floors:[],
                _currentFloorName:'',
            }
        },
        _initMethod:function(){
        },
        _initEvent:function(){
            vc.on('chooseFloor','openChooseFloorModel',function(_param){
                $('#chooseFloorModel').modal('show');
                vc.component._refreshChooseFloorInfo();
                vc.component._loadAllFloorInfo(1,10,'');
            });
        },
        methods:{
            _loadAllFloorInfo:function(_page,_row,_name){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        name:_name
                    }
                };

                //发送get请求
               vc.http.get('chooseFloor',
                            'list',
                             param,
                             function(json){
                                var _floorInfo = JSON.parse(json);
                                vc.component.chooseFloorInfo.floors = _floorInfo.apiFloorDataVoList;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            chooseFloor:function(_floor){
                vc.emit($props.emitChooseFloor,'chooseFloor',_floor);
                vc.emit($props.emitLoadData,'listFloorData',{
                    floorId:_floor.floorId
                });
                $('#chooseFloorModel').modal('hide');
            },
            queryFloors:function(){
                vc.component._loadAllFloorInfo(1,10,vc.component.chooseFloorInfo._currentFloorName);
            },
            _refreshChooseFloorInfo:function(){
                vc.component.chooseFloorInfo._currentFloorName = "";
            }
        }

    });
})(window.vc);

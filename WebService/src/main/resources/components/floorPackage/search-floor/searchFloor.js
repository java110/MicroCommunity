(function(vc){
    vc.extends({
        propTypes: {
           emitChooseFloor:vc.propTypes.string,
           emitLoadData:vc.propTypes.string
        },
        data:{
            searchFloorInfo:{
                floors:[],
                _currentFloorNum:'',
            }
        },
        _initMethod:function(){

        },
        _initEvent:function(){
            vc.on('searchFloor','openSearchFloorModel',function(_param){
                console.log("打开定位小区楼界面")
                $('#searchFloorModel').modal('show');
                vc.component._refreshSearchFloorData();
                vc.component._loadAllFloorInfo(1,10);
            });
        },
        methods:{
            _loadAllFloorInfo:function(_page,_rows,_floorNum){
                var param = {
                    params:{
                        page:_page,
                        row:_rows,
                        communityId:vc.getCurrentCommunity().communityId,
                        floorNum:_floorNum
                    }
                };

                //发送get请求
               vc.http.get('searchFloor',
                            'listFloor',
                             param,
                             function(json){
                                var _floorInfo = JSON.parse(json);
                                vc.component.searchFloorInfo.floors = _floorInfo.apiFloorDataVoList;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            chooseFloor:function(_floor){
                vc.emit($props.emitChooseFloor,'chooseFloor',_floor);
                vc.emit($props.emitLoadData,'loadData',{
                    floorId:_floor.floorId
                });
                $('#searchFloorModel').modal('hide');
            },
            searchFloors:function(){
                vc.component._loadAllFloorInfo(1,10,vc.component.searchFloorInfo._currentFloorNum);
            },
            _refreshSearchFloorData:function(){
                vc.component.searchFloorInfo._currentFloorNum = "";
            }
        }

    });
})(window.vc);
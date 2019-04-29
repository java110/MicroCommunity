(function(vc){
    vc.extends({
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
                console.log("打开定位员工界面")
                $('#searchFloorModel').modal('show');
                vc.component._refreshSearchFloorData();
                vc.component._loadAllFloorInfo(1,10);
            });
        },
        methods:{
            _loadAllFloorInfo:function(_page,_rows,_staffName){
                var param = {
                    params:{
                        page:_page,
                        rows:_rows,
                        staffName:_staffName
                    }
                };

                //发送get请求
               vc.http.get('searchFloor',
                            'listFloor',
                             param,
                             function(json){
                                var _floorInfo = JSON.parse(json);
                                vc.component.searchFloorInfo.floors = _floorInfo.datas;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            chooseStaff:function(_floor){
                vc.emit('floorInfo','chooseFloor',_floor);
                vc.emit('unit','_loadUnits',{
                    floorId:_floor.floorId
                });
                $('#searchFloorModel').modal('hide');
            },
            searchFloors:function(){
                vc.component._loadAllFloorInfo(1,10,vc.component.searchFloorInfo._currentFloorNum);
            },
            _refreshSearchFloorData:function(){
                vc.component.searchFloorInfo._currentFloorName = "";
            }
        }

    });
})(window.vc);
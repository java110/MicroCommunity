(function(vc){
    vc.extends({
        propTypes: {
           emitChooseParkingArea:vc.propTypes.string,
           emitLoadData:vc.propTypes.string
        },
        data:{
            chooseParkingAreaInfo:{
                parkingAreas:[],
                _currentParkingAreaName:'',
            }
        },
        _initMethod:function(){
        },
        _initEvent:function(){
            vc.on('chooseParkingArea','openChooseParkingAreaModel',function(_param){
                $('#chooseParkingAreaModel').modal('show');
                vc.component._refreshChooseParkingAreaInfo();
                vc.component._loadAllParkingAreaInfo(1,10,'');
            });
        },
        methods:{
            _loadAllParkingAreaInfo:function(_page,_row,_name){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        name:_name
                    }
                };

                //发送get请求
               vc.http.get('chooseParkingArea',
                            'list',
                             param,
                             function(json){
                                var _parkingAreaInfo = JSON.parse(json);
                                vc.component.chooseParkingAreaInfo.parkingAreas = _parkingAreaInfo.parkingAreas;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            chooseParkingArea:function(_parkingArea){
                if(_parkingArea.hasOwnProperty('name')){
                     _parkingArea.parkingAreaName = _parkingArea.name;
                }
                vc.emit($props.emitChooseParkingArea,'chooseParkingArea',_parkingArea);
                vc.emit($props.emitLoadData,'listParkingAreaData',{
                    parkingAreaId:_parkingArea.parkingAreaId
                });
                $('#chooseParkingAreaModel').modal('hide');
            },
            queryParkingAreas:function(){
                vc.component._loadAllParkingAreaInfo(1,10,vc.component.chooseParkingAreaInfo._currentParkingAreaName);
            },
            _refreshChooseParkingAreaInfo:function(){
                vc.component.chooseParkingAreaInfo._currentParkingAreaName = "";
            }
        }

    });
})(window.vc);

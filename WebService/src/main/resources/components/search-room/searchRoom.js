(function(vc){
    vc.extends({
        propTypes: {
           emitChooseRoom:vc.propTypes.string,
           emitLoadData:vc.propTypes.string
        },
        data:{
            searchRoomInfo:{
                rooms:[],
                _currentRoomNum:'',
                _currentFloorNum:'',
            }
        },
        _initMethod:function(){

        },
        _initEvent:function(){
            vc.on('searchRoom','openSearchRoomModel',function(_param){
                console.log("打开业主成员界面")
                $('#searchRoomModel').modal('show');
                vc.component._refreshSearchRoomData();
                //vc.component._loadAllRoomInfo(1,10);
            });
        },
        methods:{
            _loadAllRoomInfo:function(_page,_row,_roomNum){

                if(vc.component.searchRoomInfo._currentFloorNum == ''){
                    vc.message("未填写小区楼编号");
                    return ;
                }

                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        roomNum:_roomNum,
                        floorNum:vc.component.searchRoomInfo._currentFloorNum
                    }
                };

                //发送get请求
               vc.http.get('searchRoom',
                            'listRoom',
                             param,
                             function(json){
                                var _roomInfo = JSON.parse(json);
                                vc.component.searchRoomInfo.rooms = _roomInfo.rooms;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            chooseRoom:function(_room){
                vc.emit($props.emitChooseRoom,'chooseRoom',_room);
                vc.emit($props.emitLoadData,'listRoomData',{
                    roomId:_room.roomId
                });
                $('#searchRoomModel').modal('hide');
            },
            searchRooms:function(){
                vc.component._loadAllRoomInfo(1,15,vc.component.searchRoomInfo._currentRoomNum);
            },
            _refreshSearchRoomData:function(){
                vc.component.searchRoomInfo._currentRoomNum = "";
            }
        }

    });
})(window.vc);
/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROW = 10;
    vc.extends({
        data:{
            roomInfo:{
                rooms:[],
                total:0,
                records:1,
                floorId:'',
            }
        },
        _initMethod:function(){

        },
        _initEvent:function(){
            vc.on('room','listRoom',function(_param){
                  vc.component.listRoom();
            });
            vc.on('room','loadData',function(_param){
                vc.component.roomInfo.floorId = _param.floorId;
                vc.component.listRoom(DEFAULT_PAGE,DEFAULT_ROW);
            });
            vc.on('pagination','page_event',function(_currentPage){
                vc.component.listRoom(_currentPage,DEFAULT_ROW);
            });
        },
        methods:{
            listRoom:function(_page,_row){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        floorId:vc.component.roomInfo.floorId
                    }
                }
               //发送get请求
               vc.http.get('room',
                            'listRoom',
                             param,
                             function(json,res){
                                var listRoomData =JSON.parse(json);

                                vc.component.roomInfo.total = listRoomData.total;
                                vc.component.roomInfo.records = listRoomData.records;
                                vc.component.roomInfo.rooms = listRoomData.rooms;

                                vc.emit('pagination','init',{
                                    total:vc.component.roomInfo.records,
                                    currentPage:_page
                                });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _openEditRoomModel:function(_room){
                _room.floorId = vc.component.roomInfo.floorId;
                vc.emit('editRoom','openEditRoomModal',_room);
            },
            _openDelRoomModel:function(_room){
                 _room.floorId = vc.component.roomInfo.floorId;
                 vc.emit('editRoom','openDeleteRoomModal',_room);
            }
        }
    });
})(window.vc);
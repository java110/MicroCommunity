(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            listParkingSpaceInfo:{
                parkingSpaces:[],
                total:0,
                records:1,
                num:''
            }
        },
        _initMethod:function(){
            vc.component._listParkingSpaceData(DEFAULT_PAGE,DEFAULT_ROWS);
        },
        _initEvent:function(){
            vc.on('listParkingSpace','listParkingSpaceData',function(){
                vc.component._listParkingSpaceData(DEFAULT_PAGE,DEFAULT_ROWS);
                vc.component.listParkingSpaceInfo.num = '';
            });
            vc.on('pagination','page_event',function(_currentPage){
                vc.component._listParkingSpaceData(_currentPage,DEFAULT_ROWS);
            });
        },
        methods:{
            _listParkingSpaceData:function(_page,_row){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        num:vc.component.listParkingSpaceInfo.num
                    }
                }

               //发送get请求
               vc.http.get('listParkingSpace',
                            'list',
                             param,
                             function(json,res){
                                var listParkingSpaceData =JSON.parse(json);

                                vc.component.listParkingSpaceInfo.total = listParkingSpaceData.total;
                                vc.component.listParkingSpaceInfo.records = listParkingSpaceData.records;
                                vc.component.listParkingSpaceInfo.parkingSpaces = listParkingSpaceData.parkingSpaces;

                                vc.emit('pagination','init',{
                                    total:vc.component.listParkingSpaceInfo.records,
                                    currentPage:_page
                                });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );

            },
            _openAddParkingSpaceModal:function(){ //打开添加框
                vc.emit('addParkingSpace','openAddParkingSpaceModal',-1);
            },
            _openDelParkingSpaceModel:function(_parkingSpace){ // 打开删除对话框
                vc.emit('deleteParkingSpace','openParkingSpaceModel',_parkingSpace);
            },
            _openEditParkingSpaceModel:function(_parkingSpace){
                vc.emit('editParkingSpace','openEditParkingSpaceModal',_parkingSpace);
            },
            _viewParkingSpaceState:function(state){
                if(state == 'F'){
                    return "空闲";
                }else if(state == 'S'){
                    return "已售卖";
                }else if(state == 'H'){
                    return "已出租";
                }else{
                    return "未知";
                }
            },
            queryParkingSpaceMethod:function(){
                vc.component._listParkingSpaceData(DEFAULT_PAGE,DEFAULT_ROWS);
            }
        }
    })
})(window.vc);
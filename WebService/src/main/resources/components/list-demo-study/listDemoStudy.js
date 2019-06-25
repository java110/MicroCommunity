(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            listDemoStudyInfo:{
                demos:[],
                total:0,
                records:1
            }
        },
        _initMethod:function(){
            vc.component._listDemoStudyData(DEFAULT_PAGE,DEFAULT_ROWS);
        },
        _initEvent:function(){
            /*vc.on('listFloor','listFloorData',function(){
                vc.component._listFloorData(DEFAULT_PAGE,DEFAULT_ROWS);
            });*/
            vc.on('pagination','page_event',function(_currentPage){
                vc.component._listDemoStudyData(_currentPage,DEFAULT_ROWS);
            });
        },
        methods:{
            _listDemoStudyData:function(_page,_rows){
                var param = {
                    params:{
                        page:_page,
                        row:_rows,
                        communityId:vc.getCurrentCommunity().communityId
                    }
                }

               //发送get请求
               vc.http.get('listDemoStudy',
                            'list',
                             param,
                             function(json,res){
                                var listData =JSON.parse(json);

                                vc.component.listDemoStudyInfo.total = listData[0].total;
                                vc.component.listDemoStudyInfo.records = listData[0].records;
                                vc.component.listDemoStudyInfo.demos = listData;

                                vc.emit('pagination','init',{
                                    total:vc.component.listDemoStudyInfo.records,
                                    currentPage:_page
                                });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );

            },
            _openAddDemoStudyModal:function(){ //打开添加框
                //vc.emit('addFloor','openAddFloorModal',{});
            },
            _openDelDemoStudyModel:function(_floor){ // 打开删除对话框
                //vc.emit('deleteFloor','openFloorModel',_floor);
            },
            _openEditDemoStudyModel:function(_floor){
                //vc.emit('editFloor','openEditFloorModal',_floor);
            }
        }
    })
})(window.vc);
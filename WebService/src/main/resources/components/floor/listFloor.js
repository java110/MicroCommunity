(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 15;
    vc.extends({
        data:{
            listFloorInfo:{
                floors:[],
                total:0,
                errorInfo:""
            }
        },
        _initMethod:function(){
            vc.component._listFloorData();
        },
        _initEvent:function(){
            vc.on('listFloor','listFloorData',function(){
                vc.component._listFloorData();
            });
        },
        methods:{
            _listFloorData:function(){
                var param = {
                    params:{
                        page:DEFAULT_PAGE,
                        rows:DEFAULT_ROWS,
                        communityId:vc.getCurrentCommunity().communityId
                    }
                }

               //发送get请求
               vc.http.get('listFloor',
                            'list',
                             param,
                             function(json,res){
                                var listFloorData =JSON.parse(json);

                                vc.component.listFloorInfo.total = listFloorData.total;
                                vc.component.listFloorInfo.floors = listFloorData.apiFloorDataVoList;
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );

            },
            _openAddFloorModal:function(){ //打开添加框
                vc.emit('addFloor','openAddFloorModal',{});
            },
            _openDelFloorModel:function(_floor){ // 打开删除对话框

            }
        }
    })
})(window.vc);
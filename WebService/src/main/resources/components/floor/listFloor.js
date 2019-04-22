(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 15;
    vc.extends({
        data:{
            listFloorInfo:{
                floors:[],
                errorInfo:""
            }
        },
        _initMethod:function(){
            vc.component._listFloorData();
        },
        _initEvent:function(){

        },
        methods:{
            _listFloorData(){
                var param = {
                    params:{
                        page:DEFAULT_PAGE,
                        rows:DEFAULT_ROWS
                    }
                }

               //发送get请求
               vc.http.get('listFloor',
                            'list',
                             param,
                             function(json,res){
                                vc.component.listFloorInfo.floors=JSON.parse(json);
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );

            },
            _openAddFloorModal:function(){ //打开添加框

            },
            _openDelFloorModel:function(_floor){ // 打开删除对话框

            }
        }
    })
})(window.vc);
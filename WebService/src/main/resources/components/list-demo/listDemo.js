(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            listDemoInfo:{
                demos:[],
                total:0,
                records:1
            }
        },
        _initMethod:function(){
            vc.component._listOwnerData(DEFAULT_PAGE,DEFAULT_ROWS);
        },
        _initEvent:function(){
            vc.on('listDemo','listDemoData',function(){
                vc.component._listOwnerData(DEFAULT_PAGE,DEFAULT_ROWS);
            });
            vc.on('pagination','page_event',function(_currentPage){
                vc.component._listOwnerData(_currentPage,DEFAULT_ROWS);
            });
        },
        methods:{
            _listOwnerData:function(_page,_row){
                var param = {
                    params:{
                        page:_page,
                        row:_row
                    }
                }

               //发送get请求
               vc.http.get('listDemo',
                            'list',
                             param,
                             function(json,res){
                                var listDemoDate =JSON.parse(json);

                                vc.component.listDemoInfo.total = listDemoDate.total;
                                vc.component.listDemoInfo.records = listDemoDate.records;
                                vc.component.listDemoInfo.demos = listDemoDate.demos;

                                vc.emit('pagination','init',{
                                    total:vc.component.listOwnerInfo.records,
                                    currentPage:_page
                                });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );

            },
            _openAddOwnerModal:function(){ //打开添加框
                vc.emit('addDemo','openAddDemoModal',-1);
            },
            _openDelOwnerModel:function(_owner){ // 打开删除对话框
                vc.emit('deleteOwner','openOwnerModel',_owner);
            },
            _openEditOwnerModel:function(_owner){
                vc.emit('editOwner','openEditOwnerModal',_owner);
            }
        }
    })
})(window.vc);
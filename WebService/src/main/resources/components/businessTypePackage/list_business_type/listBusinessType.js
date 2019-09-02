(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            listBusinessTypeInfo:{
                BusinessType:[],
                total:0,
                records:1
            }
        },
        _initMethod:function(){
            vc.component._listBusinessTypeData(DEFAULT_PAGE,DEFAULT_ROWS);
        },
        _initEvent:function(){
            vc.on('listOwner','listOwnerData',function(){
                vc.component._listBusinessTypeData(DEFAULT_PAGE,DEFAULT_ROWS);
            });
            vc.on('pagination','page_event',function(_currentPage){
                vc.component._listBusinessTypeData(_currentPage,DEFAULT_ROWS);
            });
        },
        methods:{
            _listBusinessTypeData:function(_page,_row){
                var param = {
                    params:{
                        page:_page,
                        row:_row
                    }
                }

               //发送get请求
               vc.http.get('listBusinessType',
                            'list',
                             param,
                             function(json,res){
                                var listBusinessTypeData =JSON.parse(json);

                                vc.component.listBusinessTypeInfo.BusinessType = listBusinessTypeData;

                                vc.emit('pagination','init',{
                                    total:vc.component.listBusinessTypeInfo.records,
                                    currentPage:_page
                                });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );

            },
            _openAddBusinessTypeModal:function(){ //打开添加框
                vc.emit('addBusinessType','openAddBusinessTypeModal',-1);
            },
            _openDelBusinessTypeModel:function(_owner){ // 打开删除对话框
                vc.emit('deleteBusinessType','openBusinessTypeModel',_owner);
            },
            _openEditBusinessTypeModel:function(_owner){
                vc.emit('editBusinessType','openEditBusinessTypeModal',_owner);
            }
        }
    })
})(window.vc);
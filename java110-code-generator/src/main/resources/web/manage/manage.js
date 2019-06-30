/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            @@templateCode@@ManageInfo:{
                @@templateCode@@s:[],
                total:0,
                records:1
            }
        },
        _initMethod:function(){
            vc.component.list@@TemplateCode@@(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent:function(){
            vc.on('@@templateCode@@Manage','list@@TemplateCode@@',function(_param){
                  vc.component._list@@TemplateCode@@s(DEFAULT_PAGE, DEFAULT_ROWS);
            });
             vc.on('pagination','page_event',function(_currentPage){
                vc.component._list@@TemplateCode@@s(_currentPage,DEFAULT_ROWS);
            });
        },
        methods:{
            _list@@TemplateCode@@s:function(_page, _rows){
                var param = {
                    params:{
                        page:_page,
                        row:_rows
                    }

               }
               //发送get请求
               vc.http.get('@@templateCode@@Manage',
                            'list',
                             param,
                             function(json,res){
                                var _@@templateCode@@ManageInfo=JSON.parse(json);
                                vc.component.@@templateCode@@ManageInfo.total = _@@templateCode@@ManageInfo.total;
                                vc.component.@@templateCode@@ManageInfo.records = _@@templateCode@@ManageInfo.records;
                                vc.component.@@templateCode@@ManageInfo.@@templateCode@@s = _@@templateCode@@ManageInfo.@@templateCode@@s;
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _openAdd@@TemplateCode@@Modal:function(){
                vc.emit('add@@TemplateCode@@','openAdd@@TemplateCode@@',{});
            },
            _openEdit@@TemplateCode@@Model:function(_@@templateCode@@){
                vc.emit('edit@@TemplateCode@@','openEdit@@TemplateCode@@Modal',_@@templateCode@@);
            },
            _openDelete@@TemplateCode@@Model:function(_@@templateCode@@){
                vc.emit('delete@@TemplateCode@@','openDelete@@TemplateCode@@Modal',_@@templateCode@@);
            }
        }
    });
})(window.vc);
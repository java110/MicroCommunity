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
                records:1,
                moreCondition:false,
                @@searchCode@@:'',
                conditions:{
                    @@conditions@@
                }
            }
        },
        _initMethod:function(){
            vc.component._list@@TemplateCode@@s(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent:function(){
            @@extendEvent@@
            vc.on('@@templateCode@@Manage','list@@TemplateCode@@',function(_param){
                  vc.component._list@@TemplateCode@@s(DEFAULT_PAGE, DEFAULT_ROWS);
            });
             vc.on('pagination','page_event',function(_currentPage){
                vc.component._list@@TemplateCode@@s(_currentPage,DEFAULT_ROWS);
            });
        },
        methods:{
            _list@@TemplateCode@@s:function(_page, _rows){

                vc.component.@@templateCode@@ManageInfo.conditions.page = _page;
                vc.component.@@templateCode@@ManageInfo.conditions.row = _rows;
                var param = {
                    params:vc.component.@@templateCode@@ManageInfo.conditions
               };

               //发送get请求
               vc.http.apiGet('@@templateCode@@.list@@TemplateCode@@s',
                             param,
                             function(json,res){
                                var _@@templateCode@@ManageInfo=JSON.parse(json);
                                vc.component.@@templateCode@@ManageInfo.total = _@@templateCode@@ManageInfo.total;
                                vc.component.@@templateCode@@ManageInfo.records = _@@templateCode@@ManageInfo.records;
                                vc.component.@@templateCode@@ManageInfo.@@templateCode@@s = _@@templateCode@@ManageInfo.@@templateCode@@s;
                                vc.emit('pagination','init',{
                                     total:vc.component.@@templateCode@@ManageInfo.records,
                                     currentPage:_page
                                 });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _openAdd@@TemplateCode@@Modal:function(){
                vc.emit('add@@TemplateCode@@','openAdd@@TemplateCode@@Modal',{});
            },
            _openEdit@@TemplateCode@@Model:function(_@@templateCode@@){
                vc.emit('edit@@TemplateCode@@','openEdit@@TemplateCode@@Modal',_@@templateCode@@);
            },
            _openDelete@@TemplateCode@@Model:function(_@@templateCode@@){
                vc.emit('delete@@TemplateCode@@','openDelete@@TemplateCode@@Modal',_@@templateCode@@);
            },
            _query@@TemplateCode@@Method:function(){
                vc.component._list@@TemplateCode@@s(DEFAULT_PAGE, DEFAULT_ROWS);

            },
            _moreCondition:function(){
                if(vc.component.@@templateCode@@ManageInfo.moreCondition){
                    vc.component.@@templateCode@@ManageInfo.moreCondition = false;
                }else{
                    vc.component.@@templateCode@@ManageInfo.moreCondition = true;
                }
            }

             @@extendMethods@@
        }
    });
})(window.vc);
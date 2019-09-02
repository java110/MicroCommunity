/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            appManageInfo:{
                apps:[],
                total:0,
                records:1
            }
        },
        _initMethod:function(){
            vc.component._listApps(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent:function(){
            vc.on('appManage','listApp',function(_param){
                  vc.component._listApps(DEFAULT_PAGE, DEFAULT_ROWS);
            });
             vc.on('pagination','page_event',function(_currentPage){
                vc.component._listApps(_currentPage,DEFAULT_ROWS);
            });
        },
        methods:{
            _listApps:function(_page, _rows){
                var param = {
                    params:{
                        page:_page,
                        row:_rows
                    }

               }
               //发送get请求
               vc.http.get('appManage',
                            'list',
                             param,
                             function(json,res){
                                var _appManageInfo=JSON.parse(json);
                                vc.component.appManageInfo.total = _appManageInfo.total;
                                vc.component.appManageInfo.records = _appManageInfo.records;
                                vc.component.appManageInfo.apps = _appManageInfo.apps;
                                vc.emit('pagination','init',{
                                     total:vc.component.appManageInfo.records,
                                     currentPage:_page
                                 });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _openAddAppModal:function(){
                vc.emit('addApp','openAddAppModal',{});
            },
            _openEditAppModel:function(_app){
                vc.emit('editApp','openEditAppModal',_app);
            },
            _openDeleteAppModel:function(_app){
                vc.emit('deleteApp','openDeleteAppModal',_app);
            }
        }
    });
})(window.vc);

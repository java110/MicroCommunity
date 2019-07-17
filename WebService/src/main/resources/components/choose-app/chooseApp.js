(function(vc){
    vc.extends({
        propTypes: {
           emitChooseApp:vc.propTypes.string,
           emitLoadData:vc.propTypes.string
        },
        data:{
            chooseAppInfo:{
                apps:[],
                _currentAppName:'',
            }
        },
        _initMethod:function(){
        },
        _initEvent:function(){
            vc.on('chooseApp','openChooseAppModel',function(_param){
                console.log("打开业主成员界面")
                $('#chooseAppModel').modal('show');
                vc.component._refreshChooseAppInfo();
                vc.component._loadAllAppInfo(1,10,'');
            });
        },
        methods:{
            _loadAllAppInfo:function(_page,_row,_name){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        name:_name
                    }
                };

                //发送get请求
               vc.http.get('chooseApp',
                            'list',
                             param,
                             function(json){
                                var _appInfo = JSON.parse(json);
                                vc.component.chooseAppInfo.apps = _appInfo.apps;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            chooseApp:function(_app){
                vc.emit($props.emitChooseApp,'chooseApp',_app);
                vc.emit($props.emitLoadData,'listAppData',{
                    appId:_app.appId
                });
                $('#chooseAppModel').modal('hide');
            },
            queryApps:function(){
                vc.component._loadAllAppInfo(1,10,vc.component.chooseAppInfo._currentAppName);
            },
            _refreshChooseAppInfo:function(){
                vc.component.chooseAppInfo._currentAppName = "";
            }
        }

    });
})(window.vc);
(function(vc){
    vc.extends({
        propTypes: {
           emitChooseServiceImpl:vc.propTypes.string,
           emitLoadData:vc.propTypes.string
        },
        data:{
            chooseServiceImplInfo:{
                serviceImpls:[],
                _currentServiceImplName:'',
            }
        },
        _initMethod:function(){
        },
        _initEvent:function(){
            vc.on('chooseServiceImpl','openChooseServiceImplModel',function(_param){
                $('#chooseServiceImplModel').modal('show');
                vc.component._refreshChooseServiceImplInfo();
                vc.component._loadAllServiceImplInfo(1,10,'');
            });
        },
        methods:{
            _loadAllServiceImplInfo:function(_page,_row,_name){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        name:_name
                    }
                };

                //发送get请求
               vc.http.get('chooseServiceImpl',
                            'list',
                             param,
                             function(json){
                                var _serviceImplInfo = JSON.parse(json);
                                vc.component.chooseServiceImplInfo.serviceImpls = _serviceImplInfo.serviceImpls;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            chooseServiceImpl:function(_serviceImpl){
                if(_serviceImpl.hasOwnProperty('name')){
                     _serviceImpl.serviceImplName = _serviceImpl.name;
                }
                vc.emit($props.emitChooseServiceImpl,'chooseServiceImpl',_serviceImpl);
                vc.emit($props.emitLoadData,'listServiceImplData',{
                    serviceImplId:_serviceImpl.serviceImplId
                });
                $('#chooseServiceImplModel').modal('hide');
            },
            queryServiceImpls:function(){
                vc.component._loadAllServiceImplInfo(1,10,vc.component.chooseServiceImplInfo._currentServiceImplName);
            },
            _refreshChooseServiceImplInfo:function(){
                vc.component.chooseServiceImplInfo._currentServiceImplName = "";
            }
        }

    });
})(window.vc);

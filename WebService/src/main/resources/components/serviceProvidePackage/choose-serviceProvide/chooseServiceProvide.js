(function(vc){
    vc.extends({
        propTypes: {
           emitChooseServiceProvide:vc.propTypes.string,
           emitLoadData:vc.propTypes.string
        },
        data:{
            chooseServiceProvideInfo:{
                serviceProvides:[],
                _currentServiceProvideName:'',
            }
        },
        _initMethod:function(){
        },
        _initEvent:function(){
            vc.on('chooseServiceProvide','openChooseServiceProvideModel',function(_param){
                $('#chooseServiceProvideModel').modal('show');
                vc.component._refreshChooseServiceProvideInfo();
                vc.component._loadAllServiceProvideInfo(1,10,'');
            });
        },
        methods:{
            _loadAllServiceProvideInfo:function(_page,_row,_name){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        name:_name
                    }
                };

                //发送get请求
               vc.http.get('chooseServiceProvide',
                            'list',
                             param,
                             function(json){
                                var _serviceProvideInfo = JSON.parse(json);
                                vc.component.chooseServiceProvideInfo.serviceProvides = _serviceProvideInfo.serviceProvides;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            chooseServiceProvide:function(_serviceProvide){
                if(_serviceProvide.hasOwnProperty('name')){
                     _serviceProvide.serviceProvideName = _serviceProvide.name;
                }
                vc.emit($props.emitChooseServiceProvide,'chooseServiceProvide',_serviceProvide);
                vc.emit($props.emitLoadData,'listServiceProvideData',{
                    serviceProvideId:_serviceProvide.serviceProvideId
                });
                $('#chooseServiceProvideModel').modal('hide');
            },
            queryServiceProvides:function(){
                vc.component._loadAllServiceProvideInfo(1,10,vc.component.chooseServiceProvideInfo._currentServiceProvideName);
            },
            _refreshChooseServiceProvideInfo:function(){
                vc.component.chooseServiceProvideInfo._currentServiceProvideName = "";
            }
        }

    });
})(window.vc);

(function(vc){
    vc.extends({
        propTypes: {
           emitChooseServiceRegister:vc.propTypes.string,
           emitLoadData:vc.propTypes.string
        },
        data:{
            chooseServiceRegisterInfo:{
                serviceRegisters:[],
                _currentServiceRegisterName:'',
            }
        },
        _initMethod:function(){
        },
        _initEvent:function(){
            vc.on('chooseServiceRegister','openChooseServiceRegisterModel',function(_param){
                $('#chooseServiceRegisterModel').modal('show');
                vc.component._refreshChooseServiceRegisterInfo();
                vc.component._loadAllServiceRegisterInfo(1,10,'');
            });
        },
        methods:{
            _loadAllServiceRegisterInfo:function(_page,_row,_name){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        name:_name
                    }
                };

                //发送get请求
               vc.http.get('chooseServiceRegister',
                            'list',
                             param,
                             function(json){
                                var _serviceRegisterInfo = JSON.parse(json);
                                vc.component.chooseServiceRegisterInfo.serviceRegisters = _serviceRegisterInfo.serviceRegisters;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            chooseServiceRegister:function(_serviceRegister){
                if(_serviceRegister.hasOwnProperty('name')){
                     _serviceRegister.serviceRegisterName = _serviceRegister.name;
                }
                vc.emit($props.emitChooseServiceRegister,'chooseServiceRegister',_serviceRegister);
                vc.emit($props.emitLoadData,'listServiceRegisterData',{
                    serviceRegisterId:_serviceRegister.serviceRegisterId
                });
                $('#chooseServiceRegisterModel').modal('hide');
            },
            queryServiceRegisters:function(){
                vc.component._loadAllServiceRegisterInfo(1,10,vc.component.chooseServiceRegisterInfo._currentServiceRegisterName);
            },
            _refreshChooseServiceRegisterInfo:function(){
                vc.component.chooseServiceRegisterInfo._currentServiceRegisterName = "";
            }
        }

    });
})(window.vc);

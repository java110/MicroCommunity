(function(vc){
    vc.extends({
        propTypes: {
           emitChooseApplicationKey:vc.propTypes.string,
           emitLoadData:vc.propTypes.string
        },
        data:{
            chooseApplicationKeyInfo:{
                applicationKeys:[],
                _currentApplicationKeyName:'',
            }
        },
        _initMethod:function(){
        },
        _initEvent:function(){
            vc.on('chooseApplicationKey','openChooseApplicationKeyModel',function(_param){
                $('#chooseApplicationKeyModel').modal('show');
                vc.component._refreshChooseApplicationKeyInfo();
                vc.component._loadAllApplicationKeyInfo(1,10,'');
            });
        },
        methods:{
            _loadAllApplicationKeyInfo:function(_page,_row,_name){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        name:_name
                    }
                };

                //发送get请求
               vc.http.get('chooseApplicationKey',
                            'list',
                             param,
                             function(json){
                                var _applicationKeyInfo = JSON.parse(json);
                                vc.component.chooseApplicationKeyInfo.applicationKeys = _applicationKeyInfo.applicationKeys;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            chooseApplicationKey:function(_applicationKey){
                if(_applicationKey.hasOwnProperty('name')){
                     _applicationKey.applicationKeyName = _applicationKey.name;
                }
                vc.emit($props.emitChooseApplicationKey,'chooseApplicationKey',_applicationKey);
                vc.emit($props.emitLoadData,'listApplicationKeyData',{
                    applicationKeyId:_applicationKey.applicationKeyId
                });
                $('#chooseApplicationKeyModel').modal('hide');
            },
            queryApplicationKeys:function(){
                vc.component._loadAllApplicationKeyInfo(1,10,vc.component.chooseApplicationKeyInfo._currentApplicationKeyName);
            },
            _refreshChooseApplicationKeyInfo:function(){
                vc.component.chooseApplicationKeyInfo._currentApplicationKeyName = "";
            }
        }

    });
})(window.vc);

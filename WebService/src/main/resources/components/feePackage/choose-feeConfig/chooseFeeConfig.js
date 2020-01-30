(function(vc){
    vc.extends({
        propTypes: {
           emitChooseFeeConfig:vc.propTypes.string,
           emitLoadData:vc.propTypes.string
        },
        data:{
            chooseFeeConfigInfo:{
                feeConfigs:[],
                _currentFeeConfigName:'',
            }
        },
        _initMethod:function(){
        },
        _initEvent:function(){
            vc.on('chooseFeeConfig','openChooseFeeConfigModel',function(_param){
                $('#chooseFeeConfigModel').modal('show');
                vc.component._refreshChooseFeeConfigInfo();
                vc.component._loadAllFeeConfigInfo(1,10,'');
            });
        },
        methods:{
            _loadAllFeeConfigInfo:function(_page,_row,_name){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        name:_name
                    }
                };

                //发送get请求
               vc.http.get('chooseFeeConfig',
                            'list',
                             param,
                             function(json){
                                var _feeConfigInfo = JSON.parse(json);
                                vc.component.chooseFeeConfigInfo.feeConfigs = _feeConfigInfo.feeConfigs;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            chooseFeeConfig:function(_feeConfig){
                if(_feeConfig.hasOwnProperty('name')){
                     _feeConfig.feeConfigName = _feeConfig.name;
                }
                vc.emit($props.emitChooseFeeConfig,'chooseFeeConfig',_feeConfig);
                vc.emit($props.emitLoadData,'listFeeConfigData',{
                    feeConfigId:_feeConfig.feeConfigId
                });
                $('#chooseFeeConfigModel').modal('hide');
            },
            queryFeeConfigs:function(){
                vc.component._loadAllFeeConfigInfo(1,10,vc.component.chooseFeeConfigInfo._currentFeeConfigName);
            },
            _refreshChooseFeeConfigInfo:function(){
                vc.component.chooseFeeConfigInfo._currentFeeConfigName = "";
            }
        }

    });
})(window.vc);

(function(vc){
    vc.extends({
        propTypes: {
           emitChooseCarBlackWhite:vc.propTypes.string,
           emitLoadData:vc.propTypes.string
        },
        data:{
            chooseCarBlackWhiteInfo:{
                carBlackWhites:[],
                _currentCarBlackWhiteName:'',
            }
        },
        _initMethod:function(){
        },
        _initEvent:function(){
            vc.on('chooseCarBlackWhite','openChooseCarBlackWhiteModel',function(_param){
                $('#chooseCarBlackWhiteModel').modal('show');
                vc.component._refreshChooseCarBlackWhiteInfo();
                vc.component._loadAllCarBlackWhiteInfo(1,10,'');
            });
        },
        methods:{
            _loadAllCarBlackWhiteInfo:function(_page,_row,_name){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        name:_name
                    }
                };

                //发送get请求
               vc.http.get('chooseCarBlackWhite',
                            'list',
                             param,
                             function(json){
                                var _carBlackWhiteInfo = JSON.parse(json);
                                vc.component.chooseCarBlackWhiteInfo.carBlackWhites = _carBlackWhiteInfo.carBlackWhites;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            chooseCarBlackWhite:function(_carBlackWhite){
                if(_carBlackWhite.hasOwnProperty('name')){
                     _carBlackWhite.carBlackWhiteName = _carBlackWhite.name;
                }
                vc.emit($props.emitChooseCarBlackWhite,'chooseCarBlackWhite',_carBlackWhite);
                vc.emit($props.emitLoadData,'listCarBlackWhiteData',{
                    carBlackWhiteId:_carBlackWhite.carBlackWhiteId
                });
                $('#chooseCarBlackWhiteModel').modal('hide');
            },
            queryCarBlackWhites:function(){
                vc.component._loadAllCarBlackWhiteInfo(1,10,vc.component.chooseCarBlackWhiteInfo._currentCarBlackWhiteName);
            },
            _refreshChooseCarBlackWhiteInfo:function(){
                vc.component.chooseCarBlackWhiteInfo._currentCarBlackWhiteName = "";
            }
        }

    });
})(window.vc);

(function(vc){
    vc.extends({
        propTypes: {
           emitChooseAdvert:vc.propTypes.string,
           emitLoadData:vc.propTypes.string
        },
        data:{
            chooseAdvertInfo:{
                adverts:[],
                _currentAdvertName:'',
            }
        },
        _initMethod:function(){
        },
        _initEvent:function(){
            vc.on('chooseAdvert','openChooseAdvertModel',function(_param){
                $('#chooseAdvertModel').modal('show');
                vc.component._refreshChooseAdvertInfo();
                vc.component._loadAllAdvertInfo(1,10,'');
            });
        },
        methods:{
            _loadAllAdvertInfo:function(_page,_row,_name){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        name:_name
                    }
                };

                //发送get请求
               vc.http.get('chooseAdvert',
                            'list',
                             param,
                             function(json){
                                var _advertInfo = JSON.parse(json);
                                vc.component.chooseAdvertInfo.adverts = _advertInfo.adverts;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            chooseAdvert:function(_advert){
                if(_advert.hasOwnProperty('name')){
                     _advert.advertName = _advert.name;
                }
                vc.emit($props.emitChooseAdvert,'chooseAdvert',_advert);
                vc.emit($props.emitLoadData,'listAdvertData',{
                    advertId:_advert.advertId
                });
                $('#chooseAdvertModel').modal('hide');
            },
            queryAdverts:function(){
                vc.component._loadAllAdvertInfo(1,10,vc.component.chooseAdvertInfo._currentAdvertName);
            },
            _refreshChooseAdvertInfo:function(){
                vc.component.chooseAdvertInfo._currentAdvertName = "";
            }
        }

    });
})(window.vc);

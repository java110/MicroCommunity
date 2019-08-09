(function(vc){
    vc.extends({
        propTypes: {
           emitChoose@@TemplateCode@@:vc.propTypes.string,
           emitLoadData:vc.propTypes.string
        },
        data:{
            choose@@TemplateCode@@Info:{
                @@templateCode@@s:[],
                _current@@TemplateCode@@Name:'',
            }
        },
        _initMethod:function(){
        },
        _initEvent:function(){
            vc.on('choose@@TemplateCode@@','openChoose@@TemplateCode@@Model',function(_param){
                $('#choose@@TemplateCode@@Model').modal('show');
                vc.component._refreshChoose@@TemplateCode@@Info();
                vc.component._loadAll@@TemplateCode@@Info(1,10,'');
            });
        },
        methods:{
            _loadAll@@TemplateCode@@Info:function(_page,_row,_name){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        name:_name
                    }
                };

                //发送get请求
               vc.http.get('choose@@TemplateCode@@',
                            'list',
                             param,
                             function(json){
                                var _@@templateCode@@Info = JSON.parse(json);
                                vc.component.choose@@TemplateCode@@Info.@@templateCode@@s = _@@templateCode@@Info.@@templateCode@@s;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            choose@@TemplateCode@@:function(_@@templateCode@@){
                if(_@@templateCode@@.hasOwnProperty('name')){
                     _@@templateCode@@.@@templateCode@@Name = _@@templateCode@@.name;
                }
                vc.emit($props.emitChoose@@TemplateCode@@,'choose@@TemplateCode@@',_@@templateCode@@);
                vc.emit($props.emitLoadData,'list@@TemplateCode@@Data',{
                    @@templateCode@@Id:_@@templateCode@@.@@templateCode@@Id
                });
                $('#choose@@TemplateCode@@Model').modal('hide');
            },
            query@@TemplateCode@@s:function(){
                vc.component._loadAll@@TemplateCode@@Info(1,10,vc.component.choose@@TemplateCode@@Info._current@@TemplateCode@@Name);
            },
            _refreshChoose@@TemplateCode@@Info:function(){
                vc.component.choose@@TemplateCode@@Info._current@@TemplateCode@@Name = "";
            }
        }

    });
})(window.vc);
(function(vc){
    vc.extends({
        propTypes: {
           emitChooseMenu:vc.propTypes.string,
           emitLoadData:vc.propTypes.string
        },
        data:{
            chooseMenuInfo:{
                menus:[],
                _currentMenuName:'',
            }
        },
        _initMethod:function(){
        },
        _initEvent:function(){
            vc.on('chooseMenu','openChooseMenuModel',function(_param){
                $('#chooseMenuModel').modal('show');
                vc.component._refreshChooseMenuInfo();
                vc.component._loadAllMenuInfo(1,10,'');
            });
        },
        methods:{
            _loadAllMenuInfo:function(_page,_row,_name){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        name:_name
                    }
                };

                //发送get请求
               vc.http.get('chooseMenu',
                            'list',
                             param,
                             function(json){
                                var _menuInfo = JSON.parse(json);
                                vc.component.chooseMenuInfo.menus = _menuInfo.menus;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            chooseMenu:function(_menu){
                if(_menu.hasOwnProperty('name')){
                     _menu.menuName = _menu.name;
                }
                vc.emit($props.emitChooseMenu,'chooseMenu',_menu);
                vc.emit($props.emitLoadData,'listMenuData',{
                    menuId:_menu.menuId
                });
                $('#chooseMenuModel').modal('hide');
            },
            queryMenus:function(){
                vc.component._loadAllMenuInfo(1,10,vc.component.chooseMenuInfo._currentMenuName);
            },
            _refreshChooseMenuInfo:function(){
                vc.component.chooseMenuInfo._currentMenuName = "";
            }
        }

    });
})(window.vc);

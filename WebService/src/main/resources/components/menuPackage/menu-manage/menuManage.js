/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            menuManageInfo:{
                menus:[],
                total:0,
                records:1,
                moreCondition:false,
                name:'',
                conditions:{
                    gName:'',
                    gId:'',
                    mId:'',
                    mName:'',
                    pId:'',
                    pName:'',
                    domain:'',
                    uName:'',

                }
            }
        },
        _initMethod:function(){
            vc.component.menuManageInfo.conditions.mId = vc.getParam("mId");
            vc.component._listMenus(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent:function(){
            vc.on('menuManage','chooseMenuGroup',function(_param){
            if(_param.hasOwnProperty("name")){
                _param.gName = _param.name;
            }
              vc.copyObject(_param,vc.component.menuManageInfo.conditions);
            });

            vc.on('menuManage','listMenu',function(_param){
                  vc.component._listMenus(DEFAULT_PAGE, DEFAULT_ROWS);
            });
             vc.on('pagination','page_event',function(_currentPage){
                vc.component._listMenus(_currentPage,DEFAULT_ROWS);
            });
        },
        methods:{
            _listMenus:function(_page, _rows){

                vc.component.menuManageInfo.conditions.page = _page;
                vc.component.menuManageInfo.conditions.row = _rows;
                var param = {
                    params:vc.component.menuManageInfo.conditions
               };

               //发送get请求
               vc.http.get('menuManage',
                            'list',
                             param,
                             function(json,res){
                                var _menuManageInfo=JSON.parse(json);
                                vc.component.menuManageInfo.total = _menuManageInfo.total;
                                vc.component.menuManageInfo.records = _menuManageInfo.records;
                                vc.component.menuManageInfo.menus = _menuManageInfo.menus;
                                vc.emit('pagination','init',{
                                     total:vc.component.menuManageInfo.records,
                                     currentPage:_page
                                 });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _openAddMenuModal:function(){
                //vc.emit('addMenu','openAddMenuModal',{});
                vc.jumpToPage("/flow/configMenuFlow");
            },
            _openEditMenuModel:function(_menu){
                vc.emit('editMenu','openEditMenuModal',_menu);
            },
            _openDeleteMenuModel:function(_menu){
                vc.emit('deleteMenu','openDeleteMenuModal',_menu);
            },
            _queryMenuMethod:function(){
                vc.component._listMenus(DEFAULT_PAGE, DEFAULT_ROWS);

            },
            _moreCondition:function(){
                if(vc.component.menuManageInfo.moreCondition){
                    vc.component.menuManageInfo.moreCondition = false;
                }else{
                    vc.component.menuManageInfo.moreCondition = true;
                }
            }

             ,
 _openChooseMenuGroupMethod:function(){
                vc.emit('chooseMenuGroup','openChooseMenuGroupModel',{});

            }
        }
    });
})(window.vc);

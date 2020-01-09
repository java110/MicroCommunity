/**
 菜单 处理
 **/
(function (vc) {
    var vm = new Vue({
        el: '#menu-nav',
        data: {
            menus: [],
            logo: '',
        },
        mounted: function () {
            this._initSysInfo();
            this.getMenus();
        },
        methods: {
            _initSysInfo: function () {
                var sysInfo = vc.getData("_sysInfo");
                if (sysInfo == null) {
                    this.logo = "HC";
                    return;
                }
                this.logo = sysInfo.logo;
            },
	     _gotoIndex:function(){
                vc.jumpToPage("/")
           },
            getMenus: function () {

                var _tmpMenus = vc.getMenus();
                //浏览器缓存中能获取到
                if (_tmpMenus != null && _tmpMenus != undefined) {
                    this.miniMenu();
                    this.menus = _tmpMenus;
                    return;
                }

                var param = {
                    params: {
                        msg: this.message
                    }

                }
                //发送get请求
                vc.http.get('menu',
                    'getMenus',
                    param,
                    function (json, res) {
                        var _menus = JSON.parse(json);
                        if (_menus == null || _menus.length == 0) {
                            return;
                        }
                        _menus.sort(function (a, b) {
                            return a.seq - b.seq
                        });
                        var _currentMenusId = vc.getCurrentMenu() == null ? _menus[0].id : vc.getCurrentMenu();
                        vm.menus = vm.refreshMenuActive(_menus, _currentMenusId);
                        vc.setMenus(vm.menus);
                        vm.miniMenu();
                    }, function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
            },
            refreshMenuActive: function (jsonArray, _id) {
                for (var menuIndex = 0; menuIndex < jsonArray.length; menuIndex++) {

                    if (jsonArray[menuIndex].hasOwnProperty('childs')) {
                        var _childs = jsonArray[menuIndex].childs;
                        _childs.sort(function (_child, _newChild) {
                            return _child.seq - _newChild.seq
                        });
                        jsonArray[menuIndex].childs = _childs;
                    }

                    if (_id === jsonArray[menuIndex].id) {
                        if (jsonArray[menuIndex].active === true) {
                            //如果当前本身是打开状态，说明 需要关闭
                            jsonArray[menuIndex].active = false;
                            continue;
                        }
                        jsonArray[menuIndex].active = true;
                        continue;
                    }
                    jsonArray[menuIndex].active = false;
                }


                return jsonArray;
            },
            switchMenu: function (_id) {
                //设置菜单ID
                vc.setCurrentMenu(_id);
                vm.menus = vm.refreshMenuActive(vm.menus, _id);
                vc.setMenus(vm.menus);
            },
            miniMenu: function () {


                if(!vc.notNull(vc.getMenuState())){
                    vc.setMenuState('ON');
                }

                if (vc.notNull(vc.getMenuState()) && vc.getMenuState() == 'ON') {
                    return;
                }

                $("body").toggleClass("mini-navbar");
                vc.setMenuState('OFF');
            }

        },

    });

})(window.vc)
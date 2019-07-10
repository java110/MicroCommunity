/**
    菜单 处理
**/
(function(vc){
    var vm = new Vue({
       el:'#breadcrumb',
       data:{
                breadCrumbs:[]
       },
       mounted:function(){
           this._freshBreadCrumbByUrl();
       },
       methods:{
           _freshBreadCrumbByUrl:function(){

                var _tmpMenus = vc.getMenus();
                var _url = vc.getUrl();

                /**
                    正常情况下是走不到这里的，
                    因为系统登录时，就已经加载菜单信息缓存到本地了

                **/
                if(_tmpMenus == null || _tmpMenus == undefined){
                    return ;
                }
                for(var menuIndex =0 ; menuIndex < _tmpMenus.length;menuIndex ++){
                    //两层结构的情况
                    if(_tmpMenus[menuIndex].hasOwnProperty('childs')){
                        var _childs = _tmpMenus[menuIndex].childs;
                        for(var _childIndex = 0; _childIndex < _childs.length; _childIndex ++){
                            if(this._getRealUrl(_childs[_childIndex].href) == _url){
                                var _tmpBreadCrumbInf = {
                                    parentPageName: "",
                                    pageName: _tmpMenus[menuIndex].name
                                };
                                this.breadCrumbs.push(_tmpBreadCrumbInf);
                                _tmpBreadCrumbInf = {
                                    parentPageName: _tmpMenus[menuIndex].name,
                                    pageName: _childs[_childIndex].name
                                };
                               this.breadCrumbs.push(_tmpBreadCrumbInf);
                                break;
                            }
                        }
                    }else{
                        if(this._getRealUrl(_tmpMenus[menuIndex].href) == url){
                            var _tmpBreadCrumbInf = {
                                parentPageName: "首页",
                                pageName: _tmpMenus[menuIndex].name
                            };
                            this.breadCrumbs.push(_tmpBreadCrumbInf);
                        }
                    }
                }
           },

            _getRealUrl:function(_url){
                if(_url.indexOf('?') != -1){
                    return _url.substring(0, _url.indexOf('?'));
                }
                return _url;
            }
       },

    });

})(window.vc)
(function(vc) {
    var vm = new Vue({
        el: '#component',
        data: {
            docInfo:{
                title:'',
                description:'',
                version:'',
                company:''
           },
            menus: [],
            pages: [],
            curMenuName: '',
            content: {
                title:'',
                url:'',
                httpMethod:'',
                headers:[],
                reqParam:[],
                resParam:[],
                reqBody:'',
                resBody:''
            },
        },
        mounted: function() {
            this.getDocumentAndMenus();
        },
        methods: {
            getDocumentAndMenus: function(_catalog) {
                let _that = this;
                let _param = {
                        params: {

                        }
                    }
                    //发送get请求
                Vue.http.get('/doc/api', _param)
                //Vue.http.get('mock/api.json', _param)
                .then(function(res) {
                    _that.docInfo = res.data.api;
                    _that.menus = res.data.mappings;
                    _that.switchMenu(_that.menus[0])
                }, function(res) {

                });
            },
            _activeMenu: function (_menuName) {
               this.menus.forEach(item => {
                    item.active = false;
                    if (_menuName == item.name) {
                        item.active = true;
                    }
                });

                console.log(this.menus)
            },
             switchMenu: function(_menu){
                this.pages = [];
                this.curMenuName = _menu.name;
                this._activeMenu(_menu.name);
                this._listDocumentPages(_menu);
            },

            _gotoPage: function(_page){
               let _that  = this;
               let _param = {
                   params: {
                       name: this.curMenuName,
                       serviceCode:_page.serviceCode,
                       resource:_page.resource
                   }
               };

               //发送get请求
             Vue.http.get('/doc/api/pageContent', _param)
                //Vue.http.get('mock/page.json', _param)
              .then(function(res) {
                       _that.content = res.data;
                   },
                   function (errInfo, error) {
                       console.log('请求失败处理');
                   }
               );
            },
            _listDocumentPages: function (_menu) {
                let _that  = this;
                let _param = {
                    params: {
                        name: _menu.name,
                        resource:_menu.resource
                    }
                };

                //发送get请求
               Vue.http.get('/doc/api/page', _param)
                 //Vue.http.get('mock/pages.json', _param)
               .then(function(res) {
                        _that.pages = res.data;
                        if (_that.pages.length < 1) {
                            return;
                        }
                        _that._gotoPage(res.data[0]);
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
            },
        },

    });

})(window.vc)
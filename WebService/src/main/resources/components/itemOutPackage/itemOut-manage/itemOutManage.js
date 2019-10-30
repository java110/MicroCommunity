/**
 入驻小区
 **/
(function (vc) {
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data: {
            resourceStoreManageInfo: {
                resourceStores: [],
                total: 0,
                records: 1,
                moreCondition: false,
                resName: '',
                conditions: {
                    resId: '',
                    resName: '',
                    resCode: '',
                    description:'',
                    stock:''

                }
            }
        },
        _initMethod: function () {
            vc.component._listResourceStores(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent: function () {

            vc.on('resourceStoreManage', 'listResourceStore', function (_param) {
                vc.component._listResourceStores(DEFAULT_PAGE, DEFAULT_ROWS);
            });
            vc.on('pagination', 'page_event', function (_currentPage) {
                vc.component._listResourceStores(_currentPage, DEFAULT_ROWS);
            });
        },
        methods: {
            _resourceStoreLess:function(stock){
                resourceStoreManageInfo.conditions.stock=parseInt(stock--);
            },

            _listResourceStores: function (_page, _rows) {

                vc.component.resourceStoreManageInfo.conditions.page = _page;
                vc.component.resourceStoreManageInfo.conditions.row = _rows;
                var param = {
                    params: vc.component.resourceStoreManageInfo.conditions
                };

                //发送get请求
                vc.http.get('resourceStoreManage',
                    'list',
                    param,
                    function (json, res) {
                        var _resourceStoreManageInfo = JSON.parse(json);
                        vc.component.resourceStoreManageInfo.total = _resourceStoreManageInfo.total;
                        vc.component.resourceStoreManageInfo.records = _resourceStoreManageInfo.records;
                        vc.component.resourceStoreManageInfo.resourceStores = _resourceStoreManageInfo.resourceStores;
                        vc.emit('pagination', 'init', {
                            total: vc.component.resourceStoreManageInfo.records,
                            currentPage: _page
                        });
                    }, function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
            },
            _openAddResourceStoreModal: function () {
                vc.emit('addResourceStore', 'openAddResourceStoreModal', {});
            },
            _openEditResourceStoreModel: function (_resourceStore) {
                vc.emit('editItemNumberStore', 'openEditItemNumberStoreModal', _resourceStore);
            },
            _openDeleteResourceStoreModel: function (_resourceStore) {
                vc.emit('deleteResourceStore', 'openDeleteResourceStoreModal', _resourceStore);
            },
            _queryResourceStoreMethod: function () {
                vc.component._listResourceStores(DEFAULT_PAGE, DEFAULT_ROWS);

            },
            _moreCondition: function () {
                if (vc.component.resourceStoreManageInfo.moreCondition) {
                    vc.component.resourceStoreManageInfo.moreCondition = false;
                } else {
                    vc.component.resourceStoreManageInfo.moreCondition = true;
                }
            }


        }
    });
})(window.vc);

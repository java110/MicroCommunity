/**
 入驻小区
 **/
(function (vc) {
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data: {
            listStoreManageInfo: {
                listStores: [],
                total: 0,
                records: 1,
                moreCondition: false,
                conditions: {
                    name: '',
                    storeTypeCd: '',
                    tel: ''
                }
            }
        },
        _initMethod: function () {
            vc.component._listListStores(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent: function () {

            vc.on('listStoreManage', 'listListStore', function (_param) {
                vc.component._listListStores(DEFAULT_PAGE, DEFAULT_ROWS);
            });
            vc.on('pagination', 'page_event', function (_currentPage) {
                vc.component._listListStores(_currentPage, DEFAULT_ROWS);
            });
        },
        methods: {
            _listListStores: function (_page, _rows) {
                vc.component.listStoreManageInfo.conditions.page = _page;
                vc.component.listStoreManageInfo.conditions.row = _rows;
                var param = {
                    params: vc.component.listStoreManageInfo.conditions
                };

                //发送get请求
                vc.http.get('listStoreManage',
                    'list',
                    param,
                    function (json, res) {
                        var _listStoreManageInfo = JSON.parse(json);
                        vc.component.listStoreManageInfo.total = _listStoreManageInfo.total;
                        vc.component.listStoreManageInfo.records = _listStoreManageInfo.records;
                        vc.component.listStoreManageInfo.listStores = _listStoreManageInfo.listStores;
                        vc.emit('pagination', 'init', {
                            total: vc.component.listStoreManageInfo.records,
                            currentPage: _page
                        });
                    }, function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
            },
            _openDeleteListStoreModel: function (_listStore) {
                vc.emit('deleteListStore', 'openDeleteListStoreModal', _listStore);
            },
            _queryListStoreMethod: function () {
                vc.component._listListStores(DEFAULT_PAGE, DEFAULT_ROWS);

            },
            _moreCondition: function () {
                if (vc.component.listStoreManageInfo.moreCondition) {
                    vc.component.listStoreManageInfo.moreCondition = false;
                } else {
                    vc.component.listStoreManageInfo.moreCondition = true;
                }
            }


        }
    });
})(window.vc);

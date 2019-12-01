/**
 入驻小区
 **/
(function (vc) {
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data: {
            cacheManageInfo: {
                caches: [],
                total: 0,
                records: 1,
                moreCondition: false,
                name: '',
                conditions: {
                    id: '',
                    cacheCode: '',
                    name: ''
                }
            }
        },
        _initMethod: function () {
            vc.component._listCaches(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent: function () {

            vc.on('cacheManage', 'listCache', function (_param) {
                vc.component._listCaches(DEFAULT_PAGE, DEFAULT_ROWS);
            });
            vc.on('pagination', 'page_event', function (_currentPage) {
                vc.component._listCaches(_currentPage, DEFAULT_ROWS);
            });
        },
        methods: {
            _listCaches: function (_page, _rows) {

                vc.component.cacheManageInfo.conditions.page = _page;
                vc.component.cacheManageInfo.conditions.row = _rows;
                vc.component.cacheManageInfo.conditions.communityId = vc.getCurrentCommunity().communityId;
                var param = {
                    params: vc.component.cacheManageInfo.conditions
                };

                //发送get请求
                vc.http.get('cacheManage',
                    'list',
                    param,
                    function (json, res) {
                        var _cacheManageInfo = JSON.parse(json);
                        vc.component.cacheManageInfo.total = _cacheManageInfo.total;
                        vc.component.cacheManageInfo.records = _cacheManageInfo.records;
                        vc.component.cacheManageInfo.caches = _cacheManageInfo.caches;
                        vc.emit('pagination', 'init', {
                            total: vc.component.cacheManageInfo.records,
                            currentPage: _page
                        });
                    }, function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
            },
            _queryCacheMethod: function () {
                vc.component._listCaches(DEFAULT_PAGE, DEFAULT_ROWS);
            },
            _flushCache: function (_cache) {

                var param = {
                    params: {
                        id: _cache.id
                    }
                }

                vc.http.get(
                    'cacheManage',
                    'flushCache',
                    param,
                    function (json, res) {
                        if (res.status == 200) {
                            vc.toast("刷新缓存成功");
                            return;
                        }
                        vc.message(json);
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');
                        vc.message(errInfo);
                    });
            }
        }
    });
})(window.vc);

/**
 物品管理 组件
 **/
(function (vc) {

    vc.extends({
        propTypes: {
            callBackListener: vc.propTypes.string, //父组件名称
            callBackFunction: vc.propTypes.string //父组件监听方法
        },
        data: {
            viewResourceStoreInfo: {
                index: 0,
                flowComponent: 'viewResourceStoreInfo',
                resName: '',
                resCode: '',
                price: '',
                stock: '',
                description: '',

            }
        },
        _initMethod: function () {
            //根据请求参数查询 查询 业主信息
            vc.component._loadResourceStoreInfoData();
        },
        _initEvent: function () {
            vc.on('viewResourceStoreInfo', 'chooseResourceStore', function (_app) {
                vc.copyObject(_app, vc.component.viewResourceStoreInfo);
                vc.emit($props.callBackListener, $props.callBackFunction, vc.component.viewResourceStoreInfo);
            });

            vc.on('viewResourceStoreInfo', 'onIndex', function (_index) {
                vc.component.viewResourceStoreInfo.index = _index;
            });

        },
        methods: {

            _openSelectResourceStoreInfoModel() {
                vc.emit('chooseResourceStore', 'openChooseResourceStoreModel', {});
            },
            _openAddResourceStoreInfoModel() {
                vc.emit('addResourceStore', 'openAddResourceStoreModal', {});
            },
            _loadResourceStoreInfoData: function () {

            }
        }
    });

})(window.vc);

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
            viewResourceStoreInfo2: {
                index: 0,
                flowComponent: 'viewResourceStoreInfo2',
                resName: '',
                resCode: '',
                price: '',
                stock: '',
                description: '',
                resourceStores:''

            }
        },
        _initMethod: function () {
            //根据请求参数查询 查询 业主信息
            vc.component._loadResourceStoreInfoData();
        },
        _initEvent: function () {
            vc.on('viewResourceStoreInfo2', 'chooseResourceStore', function (_app) {
                vc.copyObject(_app, vc.component.viewResourceStoreInfo2);
                vc.emit($props.callBackListener, $props.callBackFunction, vc.component.viewResourceStoreInfo2);
            });

            vc.on('viewResourceStoreInfo2', 'onIndex', function (_index) {
                vc.component.viewResourceStoreInfo2.index = _index;
            });

            vc.on('viewResourceStoreInfo2', 'setSelectResourceStores', function (resourceStores) {
                vc.component.viewResourceStoreInfo2.resourceStores = resourceStores;
            });

            vc.on('viewResourceStoreInfo2', 'getSelectResourceStores', function (resourceStores) {
                //vc.component.viewResourceStoreInfo2.resourceStores = resourceStores;
                vc.emit($props.callBackListener, $props.callBackFunction, vc.component.viewResourceStoreInfo2);
            });


        },
        methods: {

            _openSelectResourceStoreInfoModel() {
                vc.emit('chooseResourceStore2', 'openChooseResourceStoreModel2', {});
            },
            _openAddResourceStoreInfoModel() {
                vc.emit('addResourceStore', 'openAddResourceStoreModal', {});
            },
            _loadResourceStoreInfoData: function () {

            }
        }
    });

})(window.vc);

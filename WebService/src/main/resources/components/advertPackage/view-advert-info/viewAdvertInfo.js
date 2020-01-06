/**
 发布广告 组件
 **/
(function (vc) {

    vc.extends({
        propTypes: {
            callBackListener: vc.propTypes.string, //父组件名称
            callBackFunction: vc.propTypes.string //父组件监听方法
        },
        data: {
            viewAdvertInfo: {
                index: 0,
                flowComponent: 'viewAdvertInfo',
                adName: '',
                adTypeCd: '',
                classify: '',
                locationTypeCd: '',
                locationObjId: '',
                state: '',
                seq: '',
                startTime: '',
                endTime: '',

            }
        },
        _initMethod: function () {
            //根据请求参数查询 查询 业主信息
            vc.component._loadAdvertInfoData();
        },
        _initEvent: function () {
            vc.on('viewAdvertInfo', 'chooseAdvert', function (_app) {
                vc.copyObject(_app, vc.component.viewAdvertInfo);
                vc.emit($props.callBackListener, $props.callBackFunction, vc.component.viewAdvertInfo);
            });

            vc.on('viewAdvertInfo', 'onIndex', function (_index) {
                vc.component.viewAdvertInfo.index = _index;
            });

        },
        methods: {

            _openSelectAdvertInfoModel() {
                vc.emit('chooseAdvert', 'openChooseAdvertModel', {});
            },
            _openAddAdvertInfoModel() {
                vc.emit('addAdvert', 'openAddAdvertModal', {});
            },
            _loadAdvertInfoData: function () {

            }
        }
    });

})(window.vc);

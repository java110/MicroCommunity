/**
    停车场 组件
**/
(function(vc) {

    vc.extends({
        propTypes: {
            callBackListener: vc.propTypes.string,
            //父组件名称
            callBackFunction: vc.propTypes.string //父组件监听方法
        },
        data: {
            viewParkingAreaInfo: {
                index: 0,
                flowComponent: 'viewParkingAreaInfo',
                num: '',
                typeCd: '',
                remark: '',

            }
        },
        _initMethod: function() {
            //根据请求参数查询 查询 业主信息
            vc.component._loadParkingAreaInfoData();
        },
        _initEvent: function() {
            vc.on('viewParkingAreaInfo', 'chooseParkingArea',
            function(_app) {
                vc.copyObject(_app, vc.component.viewParkingAreaInfo);
                vc.emit($props.callBackListener, $props.callBackFunction, vc.component.viewParkingAreaInfo);
            });

            vc.on('viewParkingAreaInfo', 'onIndex',
            function(_index) {
                vc.component.viewParkingAreaInfo.index = _index;
            });

        },
        methods: {

            _openSelectParkingAreaInfoModel() {
                vc.emit('chooseParkingArea', 'openChooseParkingAreaModel', {});
            },
            _openAddParkingAreaInfoModel() {
                vc.emit('addParkingArea', 'openAddParkingAreaModal', {});
            },
            _loadParkingAreaInfoData: function() {

}
        }
    });

})(window.vc);
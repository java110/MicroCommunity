/**
 入驻小区
 **/
(function (vc) {
    vc.extends({
        data: {
            serviceBindingInfo: {
                $step: {},
                index: 0,
                infos: []
            }
        },
        _initMethod: function () {
            vc.component._initStep();
        },
        _initEvent: function () {
            vc.on("serviceBinding", "notify", function (_info) {
                vc.component.serviceBindingInfo.infos[vc.component.serviceBindingInfo.index] = _info;
            });

        },
        methods: {
            _initStep: function () {
                vc.component.serviceBindingInfo.$step = $("#step");
                vc.component.serviceBindingInfo.$step.step({
                    index: 0,
                    time: 500,
                    title: ["选择应用", "选择服务", "扩展信息"]
                });
                vc.component.serviceBindingInfo.index = vc.component.serviceBindingInfo.$step.getIndex();
            },
            _prevStep: function () {
                vc.component.serviceBindingInfo.$step.prevStep();
                vc.component.serviceBindingInfo.index = vc.component.serviceBindingInfo.$step.getIndex();

                vc.emit('viewAppInfo', 'onIndex', vc.component.serviceBindingInfo.index);
                vc.emit('viewServiceInfo', 'onIndex', vc.component.serviceBindingInfo.index);
                vc.emit('addRouteView', 'onIndex', vc.component.serviceBindingInfo.index);

            },
            _nextStep: function () {
                var _currentData = vc.component.serviceBindingInfo.infos[vc.component.serviceBindingInfo.index];
                if (_currentData == null || _currentData == undefined) {
                    vc.message("请选择或填写必选信息");
                    return;
                }
                vc.component.serviceBindingInfo.$step.nextStep();
                vc.component.serviceBindingInfo.index = vc.component.serviceBindingInfo.$step.getIndex();

                vc.emit('viewAppInfo', 'onIndex', vc.component.serviceBindingInfo.index);
                vc.emit('viewServiceInfo', 'onIndex', vc.component.serviceBindingInfo.index);
                vc.emit('addRouteView', 'onIndex', vc.component.serviceBindingInfo.index);

            },
            _finishStep: function () {


                var _currentData = vc.component.serviceBindingInfo.infos[vc.component.serviceBindingInfo.index];
                if (_currentData == null || _currentData == undefined) {
                    vc.message("请选择或填写必选信息");
                    return;
                }

                var param = {
                    data: vc.component.serviceBindingInfo.infos
                }

                vc.http.post(
                    'serviceBinding',
                    'binding',
                    JSON.stringify(param),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        if (res.status == 200) {

                            vc.message('处理成功', true);
                            //关闭model
                            vc.jumpToPage("/flow/serviceRegisterFlow?" + vc.objToGetParam(JSON.parse(json)));
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
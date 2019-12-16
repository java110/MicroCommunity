/**
 入驻小区
 **/
(function (vc) {

    vc.extends({
        data: {
            writeAdvertMachineInfo: {
                machineCode: '',
            }
        },
        _initMethod: function () {

        },
        _initEvent: function () {
            vc.on('writeAdvertMachine', 'openWriteAdvertMachineModal', function () {
                $('#writeAdvertMachineModal').modal('show');
            });

        },
        methods: {
            _viewMachineAdvertInfo: function () {
                if (!vc.notNull(vc.component.writeAdvertMachineInfo.machineCode)) {
                    vc.toast("设备码为空！");
                    return;
                }

                vc.jumpToPage("/flow/advertVedioFlow?machineCode="
                    + vc.component.writeAdvertMachineInfo.machineCode
                    + "&communityId=" + vc.getCurrentCommunity().communityId);
            },

        }
    });
})(window.vc);
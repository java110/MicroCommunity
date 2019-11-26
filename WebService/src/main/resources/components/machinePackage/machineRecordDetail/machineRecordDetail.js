(function (vc, vm) {

    vc.extends({
        data: {
            machineRecordDetailInfo: {
                machineRecordId: '',
                machineCode: '',
                machineId: '',
                name: '',
                openTypeCd: '',
                tel: '',
                idCard: '',
            }
        },
        _initMethod: function () {

        },
        _initEvent: function () {
            vc.on('machineRecordDetail', 'openMachineRecordDetailModal', function (_params) {
                vc.component.refreshEditMachineRecordInfo();
                $('#editMachineRecordModel').modal('show');
                vc.copyObject(_params, vc.component.machineRecordDetailInfo);
                vc.component.machineRecordDetailInfo.communityId = vc.getCurrentCommunity().communityId;
            });
        },
        methods: {
            refreshEditMachineRecordInfo: function () {
                vc.component.machineRecordDetailInfo = {
                    machineRecordId: '',
                    machineCode: '',
                    machineId: '',
                    name: '',
                    openTypeCd: '',
                    tel: '',
                    idCard: '',

                }
            }
        }
    });

})(window.vc, window.vc.component);

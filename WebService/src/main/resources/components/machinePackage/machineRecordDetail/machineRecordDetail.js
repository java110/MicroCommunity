(function (vc, vm) {
    var _fileUrl = '/callComponent/download/getFile/fileByObjId';
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
                photo:''
            }
        },
        _initMethod: function () {

        },
        _initEvent: function () {
            vc.on('machineRecordDetail', 'openMachineRecordDetailModal', function (_params) {
                vc.component.refreshEditMachineRecordInfo();
                $('#editMachineRecordModel').modal('show');
                vc.copyObject(_params, vc.component.machineRecordDetailInfo);
                vc.component._loadMachineRecordImage();
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
                    photo:'',

                }
            },
            errorLoadImg:function(){
                vc.component.machineRecordDetailInfo.photo="/img/noPhoto.gif";
            },
            _loadMachineRecordImage:function () {
                vc.component.machineRecordDetailInfo.photo = _fileUrl+"?objId="+
                    vc.component.machineRecordDetailInfo.machineRecordId +"&communityId="+vc.getCurrentCommunity().communityId+"&fileTypeCd=60000&time="+new Date();
            }
        }
    });

})(window.vc, window.vc.component);

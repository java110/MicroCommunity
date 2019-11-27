(function(vc){
    vc.extends({
       /* propTypes: {
           emitFloorSelect2:vc.propTypes.string,
           emitLoadData:vc.propTypes.string
        },*/
        data:{
            floorSelect2Info:{
                floors:[],
                floorId:'',
                floorNum:'',
                floorName:'',
            }
        },
        _initMethod:function(){
        },
        _initEvent:function(){

        },
        methods: {
            _initFloorSelect2: function () {
                $('.floorSelector').select2({
                    placeholder: '必填，请选择楼栋',
                    ajax: {
                        url: "sdata.json",
                        dataType: 'json',
                        delay: 250,
                        data: function (params) {
                            return {
                                floorNum: vc.component.addMachineInfo.floorNum,
                                page: 1,
                                row:10,
                                communityId:vc.getCurrentCommunity().communityId
                            };
                        },
                        processResults: function (data) {
                            return {
                                results: data
                            };
                        },
                        cache: true
                    },
                    minimumInputLength: 2
                });
            }
        }
    });
})(window.vc);

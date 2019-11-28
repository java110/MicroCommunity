(function(vc){
    vc.extends({
        data:{
            floorSelect2Info:{
                floors:[],
                floorId:'',
                floorNum:'',
                floorName:'',
            }
        },
        _initMethod:function(){
            vc.component._initFloorSelect2();
        },
        _initEvent:function(){

        },
        methods: {
            _initFloorSelect2: function () {
                $('.floorSelector').select2({
                    placeholder: '必填，请选择楼栋',
                    ajax: {
                        url: "/floorSelect2/list",
                        dataType: 'json',
                        delay: 250,
                        data: function (params) {
                            return {
                                floorNum: vc.component.floorSelect2Info.floorNum,
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
                $('.floorSelector').on("select2:select", function (evt) {
                    //这里是选中触发的事件
                    //evt.params.data 是选中项的信息
                });

                $('.floorSelector').on("select2:unselect", function (evt) {
                    //这里是取消选中触发的事件
                    //如配置allowClear: true后，触发
                });
            }
        }
    });
})(window.vc);

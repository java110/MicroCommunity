/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            serviceBindingInfo:{
                step:1,
            }
        },
        _initMethod:function(){
            vc.component._listServices(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent:function(){

        },
        methods:{
            _listServices:function(_page, _rows){

            }
        }
    });
})(window.vc);

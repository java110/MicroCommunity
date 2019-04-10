/**
    权限组
**/
(function(vc){

    vc.extends({
        data:{
            privilegeStaffInfo:{
                userId:"",
                name:"",
                tel:"",
                email:"",
                sex:"",
                address:"",
            }
        },
        _initMethod:function(){

        },
        _initEvent:function(){
            vc.on('privilegeStaffInfo','chooseStaff',function(_staff){
                vc.component.privilegeStaffInfo = _staff;
            })

        },
        methods:{
            loadStaffInfo:function(_name){

            },
            openSearchStaffModel(){
                vc.emit('searchStaff','openSearchStaffModel',{});
            }
        }
    });

})(window.vc);
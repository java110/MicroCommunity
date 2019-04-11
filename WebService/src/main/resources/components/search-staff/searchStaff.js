(function(vc){
    vc.extends({
        data:{
            searchStaffInfo:{
                staffs:[],
                _currentStaffName:'',
            }
        },
        _initMethod:function(){

        },
        _initEvent:function(){
            vc.on('searchStaff','openSearchStaffModel',function(_param){
                console.log("打开定位员工界面")
                $('#searchStaffModel').modal('show');
                vc.component._loadAllStaffInfo(1,10);
            });
        },
        methods:{
            _loadAllStaffInfo:function(_page,_rows,_staffName){
                var param = {
                    params:{
                        page:_page,
                        rows:_rows,
                        staffName:_staffName
                    }
                };

                //发送get请求
               vc.http.get('searchStaff',
                            'listStaff',
                             param,
                             function(json){
                                var _staffInfo = JSON.parse(json);
                                vc.component.searchStaffInfo.staffs = _staffInfo.datas;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            chooseStaff:function(_staff){
                vc.emit('privilegeStaffInfo','chooseStaff',_staff);
                $('#searchStaffModel').modal('hide');
            },
            searchStaffs:function(){
                vc.component._loadAllStaffInfo(1,10,vc.component.searchStaffInfo._currentStaffName);
            }
        }

    });
})(window.vc);
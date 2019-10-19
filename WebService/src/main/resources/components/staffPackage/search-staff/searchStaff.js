(function(vc){
    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string='privilegeStaffInfo', //父组件名称
           callBackFunction:vc.propTypes.string='chooseStaff' //父组件监听方法
        },
        data:{
            searchStaffInfo:{
                staffs:[],
                _currentStaffName:'',
                orgId:'',
            }
        },
        _initMethod:function(){

        },
        _initEvent:function(){
            vc.on('searchStaff','openSearchStaffModel',function(_param){
                console.log("打开定位员工界面")
                $('#searchStaffModel').modal('show');
                vc.component._refreshSearchStaffData();
                if(_param.hasOwnProperty('orgId')){
                    vc.component.searchStaffInfo.orgId = _param.orgId;
                }
                vc.component._loadAllStaffInfo(1,10);
            });
        },
        methods:{
            _loadAllStaffInfo:function(_page,_rows,_staffName){
                var param = {
                    params:{
                        page:_page,
                        rows:_rows,
                        staffName:_staffName,
                        orgId:vc.component.searchStaffInfo.orgId
                    }
                };

                //发送get请求
               vc.http.get('searchStaff',
                            'listStaff',
                             param,
                             function(json){
                                var _staffInfo = JSON.parse(json);
                                vc.component.searchStaffInfo.staffs = _staffInfo.staffs;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            chooseStaff:function(_staff){
                //vc.emit('privilegeStaffInfo','chooseStaff',_staff);
                vc.emit($props.callBackListener,$props.callBackFunction,_staff);

                vc.emit('staffPrivilege','_loadStaffPrivileges',{
                    staffId:_staff.userId
                });
                $('#searchStaffModel').modal('hide');
            },
            searchStaffs:function(){
                vc.component._loadAllStaffInfo(1,10,vc.component.searchStaffInfo._currentStaffName);
            },
            _refreshSearchStaffData:function(){
                vc.component.searchStaffInfo._currentStaffName = "";
            }
        }

    });
})(window.vc);
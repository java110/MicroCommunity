/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            auditUserManageInfo:{
                auditUsers:[],
                total:0,
                records:1,
                moreCondition:false,
                userName:'',
                conditions:{
                    auditUserId:'',
userName:'',
auditLink:'',

                }
            }
        },
        _initMethod:function(){
            vc.component._listAuditUsers(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent:function(){
            
            vc.on('auditUserManage','listAuditUser',function(_param){
                  vc.component._listAuditUsers(DEFAULT_PAGE, DEFAULT_ROWS);
            });
             vc.on('pagination','page_event',function(_currentPage){
                vc.component._listAuditUsers(_currentPage,DEFAULT_ROWS);
            });
        },
        methods:{
            _listAuditUsers:function(_page, _rows){

                vc.component.auditUserManageInfo.conditions.page = _page;
                vc.component.auditUserManageInfo.conditions.row = _rows;
                var param = {
                    params:vc.component.auditUserManageInfo.conditions
               };

               //发送get请求
               vc.http.get('auditUserManage',
                            'list',
                             param,
                             function(json,res){
                                var _auditUserManageInfo=JSON.parse(json);
                                vc.component.auditUserManageInfo.total = _auditUserManageInfo.total;
                                vc.component.auditUserManageInfo.records = _auditUserManageInfo.records;
                                vc.component.auditUserManageInfo.auditUsers = _auditUserManageInfo.auditUsers;
                                vc.emit('pagination','init',{
                                     total:vc.component.auditUserManageInfo.records,
                                     currentPage:_page
                                 });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _openAddAuditUserModal:function(){
                vc.emit('addAuditUser','openAddAuditUserModal',{});
            },
            _openEditAuditUserModel:function(_auditUser){
                vc.emit('editAuditUser','openEditAuditUserModal',_auditUser);
            },
            _openDeleteAuditUserModel:function(_auditUser){
                vc.emit('deleteAuditUser','openDeleteAuditUserModal',_auditUser);
            },
            _queryAuditUserMethod:function(){
                vc.component._listAuditUsers(DEFAULT_PAGE, DEFAULT_ROWS);

            },
            _moreCondition:function(){
                if(vc.component.auditUserManageInfo.moreCondition){
                    vc.component.auditUserManageInfo.moreCondition = false;
                }else{
                    vc.component.auditUserManageInfo.moreCondition = true;
                }
            }

             
        }
    });
})(window.vc);

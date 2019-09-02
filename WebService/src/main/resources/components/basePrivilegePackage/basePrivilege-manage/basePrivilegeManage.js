/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            basePrivilegeManageInfo:{
                basePrivileges:[],
                total:0,
                records:1,
                moreCondition:false,
                name:'',
                conditions:{
                    name:'',
pId:'',
domain:'',

                }
            }
        },
        _initMethod:function(){
            vc.component._listBasePrivileges(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent:function(){
            
            vc.on('basePrivilegeManage','listBasePrivilege',function(_param){
                  vc.component._listBasePrivileges(DEFAULT_PAGE, DEFAULT_ROWS);
            });
             vc.on('pagination','page_event',function(_currentPage){
                vc.component._listBasePrivileges(_currentPage,DEFAULT_ROWS);
            });
        },
        methods:{
            _listBasePrivileges:function(_page, _rows){

                vc.component.basePrivilegeManageInfo.conditions.page = _page;
                vc.component.basePrivilegeManageInfo.conditions.row = _rows;
                var param = {
                    params:vc.component.basePrivilegeManageInfo.conditions
               };

               //发送get请求
               vc.http.get('basePrivilegeManage',
                            'list',
                             param,
                             function(json,res){
                                var _basePrivilegeManageInfo=JSON.parse(json);
                                vc.component.basePrivilegeManageInfo.total = _basePrivilegeManageInfo.total;
                                vc.component.basePrivilegeManageInfo.records = _basePrivilegeManageInfo.records;
                                vc.component.basePrivilegeManageInfo.basePrivileges = _basePrivilegeManageInfo.basePrivileges;
                                vc.emit('pagination','init',{
                                     total:vc.component.basePrivilegeManageInfo.records,
                                     currentPage:_page
                                 });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _openAddBasePrivilegeModal:function(){
                vc.emit('addBasePrivilege','openAddBasePrivilegeModal',{});
            },
            _openEditBasePrivilegeModel:function(_basePrivilege){
                vc.emit('editBasePrivilege','openEditBasePrivilegeModal',_basePrivilege);
            },
            _openDeleteBasePrivilegeModel:function(_basePrivilege){
                vc.emit('deleteBasePrivilege','openDeleteBasePrivilegeModal',_basePrivilege);
            },
            _queryBasePrivilegeMethod:function(){
                vc.component._listBasePrivileges(DEFAULT_PAGE, DEFAULT_ROWS);

            },
            _moreCondition:function(){
                if(vc.component.basePrivilegeManageInfo.moreCondition){
                    vc.component.basePrivilegeManageInfo.moreCondition = false;
                }else{
                    vc.component.basePrivilegeManageInfo.moreCondition = true;
                }
            }

             
        }
    });
})(window.vc);

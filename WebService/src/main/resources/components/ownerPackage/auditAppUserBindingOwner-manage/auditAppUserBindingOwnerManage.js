/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            auditAppUserBindingOwnerManageInfo:{
                auditAppUserBindingOwners:[],
                total:0,
                records:1,
                moreCondition:false,
                currentAppUserId:'',
                name:'',
                conditions:{
                    appUserName:'',
                    idCard:'',
                    link:'',
                    state:''
                }
            }
        },
        _initMethod:function(){
            vc.component._listAuditAppUserBindingOwners(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent:function(){
            
            vc.on('auditAppUserBindingOwnerManage','listAuditAppUserBindingOwner',function(_param){
                  vc.component._listAuditAppUserBindingOwners(DEFAULT_PAGE, DEFAULT_ROWS);
            });

             vc.on('auditAppUserBindingOwnerManage', 'auditMessage', function (_auditInfo) {
                vc.component._auditAppUserBindingOwner(_auditInfo);
            });
             vc.on('pagination','page_event',function(_currentPage){
                vc.component._listAuditAppUserBindingOwners(_currentPage,DEFAULT_ROWS);
            });
        },
        methods:{
            _listAuditAppUserBindingOwners:function(_page, _rows){

                vc.component.auditAppUserBindingOwnerManageInfo.conditions.page = _page;
                vc.component.auditAppUserBindingOwnerManageInfo.conditions.row = _rows;
                vc.component.auditAppUserBindingOwnerManageInfo.conditions.communityId = vc.getCurrentCommunity().communityId;
                var param = {
                    params:vc.component.auditAppUserBindingOwnerManageInfo.conditions
               };

               //发送get请求
               vc.http.get('auditAppUserBindingOwnerManage',
                            'list',
                             param,
                             function(json,res){
                                var _auditAppUserBindingOwnerManageInfo=JSON.parse(json);
                                vc.component.auditAppUserBindingOwnerManageInfo.total = _auditAppUserBindingOwnerManageInfo.total;
                                vc.component.auditAppUserBindingOwnerManageInfo.records = _auditAppUserBindingOwnerManageInfo.records;
                                vc.component.auditAppUserBindingOwnerManageInfo.auditAppUserBindingOwners = _auditAppUserBindingOwnerManageInfo.auditAppUserBindingOwners;
                                vc.emit('pagination','init',{
                                     total:vc.component.auditAppUserBindingOwnerManageInfo.records,
                                     currentPage:_page
                                 });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },

            _openAuditAppUserBindingOwnerModel:function(_auditAppUserBindingOwner){
                vc.component.auditAppUserBindingOwnerManageInfo.currentAppUserId = _auditAppUserBindingOwner.appUserId;
                vc.emit('audit', 'openAuditModal', {});
            },

            _auditAppUserBindingOwner: function (_auditInfo) {
                _auditInfo.communityId = vc.getCurrentCommunity().communityId;
                _auditInfo.appUserId = vc.component.auditAppUserBindingOwnerManageInfo.currentAppUserId;
                //发送get请求
                vc.http.post('auditAppUserBindingOwnerManage',
                    'audit',
                    JSON.stringify(_auditInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        vc.message("处理成功");
                        vc.component._listAuditAppUserBindingOwners(DEFAULT_PAGE, DEFAULT_ROWS);
                    }, function (errInfo, error) {
                        console.log('请求失败处理');
                        vc.message("处理失败：" + errInfo);
                    }
                );
            },
            _moreCondition:function(){
                if(vc.component.auditAppUserBindingOwnerManageInfo.moreCondition){
                    vc.component.auditAppUserBindingOwnerManageInfo.moreCondition = false;
                }else{
                    vc.component.auditAppUserBindingOwnerManageInfo.moreCondition = true;
                }
            },
            _queryAuditAppUserBindingOwnerMethod:function () {
                vc.component._listAuditAppUserBindingOwners(DEFAULT_PAGE, DEFAULT_ROWS);
            }

             
        }
    });
})(window.vc);

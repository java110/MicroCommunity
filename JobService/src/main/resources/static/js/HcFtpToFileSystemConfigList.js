$(document).ready(function(){
　　// 开始加载数据（任务数据）
    loadTaskData();
});
/**
 * 加载任务数据（分页查询）
 */
function loadTaskDataPage(curPage){
	$.ajax({
	      type: 'POST',
	      url: '/HcFtpToFileSystemConfigList/queryFtpItems',
	      data: {
	    	curPage:curPage
	      },
	      dataType: 'json',
	      success: function(data){
	        // 成功时运行
	        dealQueryData(data);
	      },
	      error:function(data){
	        // 失败时运行,目前不处理
	          $('#alertContext').html('网络超时！查询数据失败'+data);
	          $('#alertModal').modal('show');
	          return;
	      }
	    });
}

// 加载任务数据（主要是查询首页）
function loadTaskData(){
	// 查询首页
	loadTaskDataPage(1);
}
/**
 * 处理查询数据
 */
function dealQueryData(data){
    var rows = data.rows;
    var total = data.total;
    if(rows == '[]' || rows.length == 0){
    	 $('#tasksContext').html('');
    	return ;
    }
    var trStrs = "";
    for(var i = 0 ; i< rows.length;i++){
        trStrs += createHtmlContext(rows[i]);
        $('#tasksContext').html(trStrs);
    }
    //不显示分页
    if(total == null || total == 1){
    	return ;
    }
    // 处理分页问题
    dealQueryPage(data);
}
// 处理分页问题
function dealQueryPage(data){
    var total = data.total;
    var currentPage = data.currentPage;
		var options = {
            currentPage: currentPage,
            totalPages: total,
            bootstrapMajorVersion: 3,
            onPageClicked: function(event, originalEvent, type,page){
			loadTaskDataPage(page);
		}
        }
        $('#saopPagination').bootstrapPaginator(options);
	
}
function createHtmlContext(taskInfo){
            var taskId = taskInfo.TASKID;
            var taskName = taskInfo.TASKNAME;
            var taskFileName = taskInfo.FILE_NAME;
            var taskUOrD = taskInfo.U_OR_D;
            var taskUOrDName = taskInfo.U_OR_D_NAME;
            var taskRunState = taskInfo.RUN_STATE;
            var taskRUNFlag = taskInfo.RUN_FLAG;
            var taskTnum = taskInfo.TNUM;
            var taskTaskcron = taskInfo.TASKCRON;
            var taskErrPhone = taskInfo.ERRPHONE;
            var taskUpnull = taskInfo.UPNULL;
            var taskPreFlag = taskInfo.PREFLAG;
            var taskPreFunction = taskInfo.PREFUNCTION;
            var taskAfterFlag = taskInfo.AFTERFLAG;
            var taskAfterFunction = taskInfo.AFTERFUNCTION;
            var taskCreateDate = taskInfo.CREATE_DATE
            var taskRunStateName = "运行失败";

            if(taskRunState == 'R'){
                taskRunStateName = "正在执行";
            }else if(taskRunState == 'T'){
                taskRunStateName = "等待运行";
            }
      var trStr = "<tr>"
                   +"<td class='text-center'><input type='checkbox' name='choiceCheckBox' id='choice_"+taskId+"' value='"+taskId+"'></td>"
                   +"<td class='text-center'>"+taskId+"</td>"
                   +"<td class='text-center'>"+taskName+"</td>"
                   +"<td class='text-center'>"+taskTaskcron+"</td>"
                   +"<td class='text-center'>"+taskUOrDName+"</td>"
                   +"<td class='text-center'>"+taskCreateDate+"</td>"
                   +"<td class='text-center'>"+taskRunStateName+"</td>";
       if(taskRUNFlag == "1"){
                trStr += "<td class='text-center'><span class='glyphicon glyphicon-ok-sign task_run' >运行</span></td>";
       }else{
                trStr +=  "<td class='text-center'><span class='glyphicon glyphicon-remove-sign task_stop' >停止</span></td>"
       }

       trStr = trStr
                + "<td class='text-center'>"+taskTnum+"</td>"
                + "<td class='text-center'>"
                + "            <a class='label label-info' href='../prvncFtpfileConfig!queryTaskLogList.action?taskid="+taskId+"' target='_blank'>日志</a>"
                + "</td>";
       // 将后台传过来的数据影藏放到页面上
       trStr = trStr
                + "<input type='hidden' name='taskName_"+taskId+"' id='taskName_"+taskId+"' value='"+taskName+"'/>"
                + "<input type='hidden' name='taskFileName_"+taskId+"' id='taskFileName_"+taskId+"' value='"+taskFileName+"'/>"
                + "<input type='hidden' name='taskUOrD_"+taskId+"' id='taskUOrD_"+taskId+"' value='"+taskUOrD+"'/>"
                + "<input type='hidden' name='taskUOrDName_"+taskId+"' id='taskUOrDName_"+taskId+"' value='"+taskUOrDName+"'/>"
                + "<input type='hidden' name='taskRunState_"+taskId+"' id='taskRunState_"+taskId+"' value='"+taskRunState+"'/>"
                + "<input type='hidden' name='taskRUNFlag_"+taskId+"' id='taskRUNFlag_"+taskId+"' value='"+taskRUNFlag+"'/>"
                + "<input type='hidden' name='taskTnum_"+taskId+"' id='taskTnum_"+taskId+"' value='"+taskTnum+"'/>"
                + "<input type='hidden' name='taskTaskcron_"+taskId+"' id='taskTaskcron_"+taskId+"' value='"+taskTaskcron+"'/>"
                + "<input type='hidden' name='taskErrPhone_"+taskId+"' id='taskErrPhone_"+taskId+"' value='"+taskErrPhone+"'/>"
                + "<input type='hidden' name='taskUpnull_"+taskId+"' id='taskUpnull_"+taskId+"' value='"+taskUpnull+"'/>"
                + "<input type='hidden' name='taskPreFlag_"+taskId+"' id='taskPreFlag_"+taskId+"' value='"+taskPreFlag+"'/>"
                + "<input type='hidden' name='taskPreFunction_"+taskId+"' id='taskPreFunction_"+taskId+"' value='"+taskPreFunction+"'/>"
                + "<input type='hidden' name='taskAfterFlag_"+taskId+"' id='taskAfterFlag_"+taskId+"' value='"+taskAfterFlag+"'/>"
                + "<input type='hidden' name='taskAfterFunction_"+taskId+"' id='taskAfterFunction_"+taskId+"' value='"+taskAfterFunction+"'/>"
                + "<input type='hidden' name='taskCreateDate_"+taskId+"' id='taskCreateDate_"+taskId+"' value='"+taskCreateDate+"'/>";
       return trStr;
}
/**
 * 启动任务
 */
function startJob(){
    // 显示加载框
    fadeLoader("ON");
    // 加载所有checkbox {"tasks":[{"taskId":1},{"taskId":2}],"state":"START"}
    var requestParam = '{"tasks":['
    var requestTaskId = '';
    var runTaskId = '';
    $("input[name='choiceCheckBox']").each(
        function(){
            if($(this).prop('checked')){
                    // 判断选中的是否已经是启动状态
                    var taskRUNFlag = $('#taskRUNFlag_'+$(this).val()).val();
                    // 说明已经启动，提示错误
                    if(taskRUNFlag == 1){
                        runTaskId += ($(this).val() + ',');
                    }
                   requestTaskId += '{"taskId":'+$(this).val()+'},';
            }
        }
    );
    if(runTaskId != ''){
        runTaskId = runTaskId.substring(0,runTaskId.length-1)
         $('#alertContext').html('任务ID【'+runTaskId+'】已经启动，无需再次启动');
          $('#alertModal').modal('show');
          // 关闭加载框
          fadeLoader("OFF");
          return;
    }
    // 没有选中任何任务
    if(requestTaskId == ''){
        $('#alertContext').html('没有选中任何任务');
            $('#alertModal').modal('show');
            // 关闭加载框
            fadeLoader("OFF");
            return;
    }
    requestTaskId = requestTaskId.substring(0,requestTaskId.length-1);

    requestParam += requestTaskId + '],"state":"START"}';
    // 请求处理
     $.ajax({
          type: 'POST',
          url: '/HcFtpToFileSystemConfigList/startJob',
          data: {
                ftpItemJson:requestParam
          },
          dataType: 'json',
          success: function(data){
            // 重新加载数据
            if(data.RESULT_CODE == '0000'){
                loadTaskData();
            }else{
                $('#alertContext').html(data.RESULT_MSG);
                $('#alertModal').modal('show');
            }

            fadeLoader("OFF");
          },
          error:function(data){
            // 失败时运行,目前不处理
            $('#alertContext').html('启动任务失败！'+data);
            $('#alertModal').modal('show');
            fadeLoader("OFF");
          }
        });
    // 关闭加载框

}

/**
 * 停止任务
 */
function stopJob(){
    // 显示加载框
   // 显示加载框
       fadeLoader("ON");
       // 加载所有checkbox {"tasks":[{"taskId":1},{"taskId":2}],"state":"STOP"}
       var requestParam = '{"tasks":['
       var requestTaskId = '';
       var stopTaskId = '';
       $("input[name='choiceCheckBox']").each(
           function(){
               if($(this).prop('checked')){
                       var taskRUNFlag = $('#taskRUNFlag_'+$(this).val()).val();
                       // 说明已经启动，提示错误
                       if(taskRUNFlag == 0){
                           stopTaskId += ($(this).val() + ',');
                       }
                      requestTaskId += '{"taskId":'+$(this).val()+'},';
               }
           }
       );

        if(stopTaskId != ''){
               stopTaskId = stopTaskId.substring(0,stopTaskId.length-1)
                $('#alertContext').html('任务ID【'+stopTaskId+'】已经停止，无需再次停止');
                 $('#alertModal').modal('show');
                 // 关闭加载框
                 fadeLoader("OFF");
                 return;
        }
       // 没有选中任何任务
       if(requestTaskId == ''){
           $('#alertContext').html('没有选中任何任务');
               $('#alertModal').modal('show');
               // 关闭加载框
               fadeLoader("OFF");
               return;
       }
       requestTaskId = requestTaskId.substring(0,requestTaskId.length-1);

       requestParam += requestTaskId + '],"state":"STOP"}';
       // 请求处理
        $.ajax({
             type: 'POST',
             url: '/HcFtpToFileSystemConfigList/stopJob',
             data: {
                   ftpItemJson:requestParam
             },
             dataType: 'json',
             success: function(data){
               // 重新加载数据
               if(data.RESULT_CODE == '0000'){
                   loadTaskData();
               }else{
                   $('#alertContext').html(data.RESULT_MSG);
                   $('#alertModal').modal('show');
               }

               fadeLoader("OFF");
             },
             error:function(data){
               // 失败时运行,目前不处理
               $('#alertContext').html('停止任务失败！'+data);
               $('#alertModal').modal('show');
               fadeLoader("OFF");
             }
           });
       // 关闭加载框
}
/**
 * 删除任务配置信息
 */
function deleteTask(){
    // 显示加载框
        fadeLoader("ON");
        // 加载所有checkbox {"tasks":[{"taskId":1},{"taskId":2}],"state":"DELETE"}
        var requestParam = '{"tasks":['
        var requestTaskId = '';
        var runTaskId = '';
        $("input[name='choiceCheckBox']").each(
            function(){
                if($(this).prop('checked')){
                        // 判断选中的是否已经是启动状态
                        var taskRUNFlag = $('#taskRUNFlag_'+$(this).val()).val();
                        // 说明已经启动，提示错误
                        if(taskRUNFlag == 1){
                            runTaskId += ($(this).val() + ',');
                        }
                       requestTaskId += '{"taskId":'+$(this).val()+'},';
                }
            }
        );
        if(runTaskId != ''){
            runTaskId = runTaskId.substring(0,runTaskId.length-1)
             $('#alertContext').html('任务ID【'+runTaskId+'】正在启动，请停止后再操作删除！');
              $('#alertModal').modal('show');
              // 关闭加载框
              fadeLoader("OFF");
              return;
        }
        // 没有选中任何任务
        if(requestTaskId == ''){
            $('#alertContext').html('没有选中任何任务');
                $('#alertModal').modal('show');
                // 关闭加载框
                fadeLoader("OFF");
                return;
        }
        requestTaskId = requestTaskId.substring(0,requestTaskId.length-1);

        requestParam += requestTaskId + '],"state":"DELETE"}';
        // 请求处理
         $.ajax({
              type: 'POST',
              url: '/HcFtpToFileSystemConfigList/deleteFtpItem',
              data: {
                    ftpItemJson:requestParam
              },
              dataType: 'json',
              success: function(data){
                // 重新加载数据
                if(data.RESULT_CODE == '0000'){
                    loadTaskData();
                }else{
                    $('#alertContext').html(data.RESULT_MSG);
                    $('#alertModal').modal('show');
                }

                fadeLoader("OFF");
              },
              error:function(data){
                // 失败时运行,目前不处理
                $('#alertContext').html('删除任务失败！'+data);
                $('#alertModal').modal('show');
                fadeLoader("OFF");
              }
            });
        // 关闭加载框
}
/** 新增任务* */
function newTask(){
    $('#newTaskModal').modal('show').css({
                        width: 'auto'
                    });
    // 清除老值
    clearNewTaskInputValue();
}
/**
 * 打开新的新增任务时需要清除原前的值。
 */
function clearNewTaskInputValue(){
    $('#taskNameNew').val('');
    $('#taskCronNew').val('');
    $('#tNumNew').val('');
    $('#upNulNew').val('');
    $('#errPhoneNew').val('');
    $('#preFlagNew').val('');
    $('#preFunctionNew').val('');
    $('#afterFlagNew').val('');
    $('#afterFunctionNew').val('');
    $('#uOrDNew').val('');
    $('#fileNameNew').val('');

    $("div[name='taskAttrsNew']").remove();

    $('#error_alert_divNew').attr('class','alert alert-danger alert-dismissible hidden');
}
/** 编辑任务* */
function editTask(){
    var choiceCheckBoxCount = 0;
    var taskId = 0;
     $("input[name='choiceCheckBox']").each(
                function(){
                    if($(this).prop('checked')){
                       choiceCheckBoxCount ++;
                       taskId = $(this).val();
                    }
                }
            );
     if(choiceCheckBoxCount > 1){
         $('#alertContext').html('编辑时只能选中一个任务操作，目前选中了【'+choiceCheckBoxCount+'】');
          $('#alertModal').modal('show');
          return ;
     }
     if(choiceCheckBoxCount < 1){
         $('#alertContext').html('请选中一个任务，进行编辑！');
         $('#alertModal').modal('show');
         return ;
     }

    // 清除以前的数据
    clearEditTaskInputValue();

    // 将选中的数据填充值编辑面板
    fillEditTaskInputValue(taskId);

    $('#editTaskModal').modal('show').css({
                            width: 'auto'
                        });
}

/**
 * 打开新的编辑任务时需要清除原前的值。
 */
function clearEditTaskInputValue(){
    $('#taskIdEdit').val('');
    $('#taskNameEdit').val('');
    $('#taskCronEdit').val('');
    $('#tNumEdit').val('');
    $('#upNulEdit').val('');
    $('#errPhoneEdit').val('');
    $('#preFlagEdit').val('');
    $('#preFunctionEdit').val('');
    $('#afterFlagEdit').val('');
    $('#afterFunctionEdit').val('');
    $('#uOrDEdit').val('');
    $('#fileNameEdit').val('');

    $("div[name='taskAttrsEdit']").remove();
    $('#error_alert_divEdit').attr('class','alert alert-danger alert-dismissible hidden');
}
/**
 * 将选中的数据填充值编辑面板
 */
function fillEditTaskInputValue(taskId){
    $('#taskIdEdit').val(taskId);
    $('#taskNameEdit').val($('#taskName_'+taskId).val());
    $('#taskCronEdit').val($('#taskTaskcron_'+taskId).val());
    $('#tNumEdit').val($('#taskTnum_'+taskId).val());
    $('#upNulEdit').val($('#taskUpNul_'+taskId).val());
    $('#errPhoneEdit').val($('#taskErrPhone_'+taskId).val());
    $('#upNulEdit').val($('#taskUpnull_'+taskId).val());
    $('#preFlagEdit').val($('#taskPreFlag_'+taskId).val());
    $('#preFunctionEdit').val($('#taskPreFunction_'+taskId).val());
    $('#afterFlagEdit').val($('#taskAfterFlag_'+taskId).val());
    $('#afterFunctionEdit').val($('#taskAfterFunction_'+taskId).val());
    $('#uOrDEdit').val($('#taskUOrD_'+taskId).val());
    $('#fileNameEdit').val($('#taskFileName_'+taskId).val());
    // 根据当前的模板查询
    var obj = new Object();
    obj.value=$('#taskUOrD_'+taskId).val();
    obj.taskId = taskId;
    queryTaskAttr(obj,'Edit');  
}
/**
 * 获取属性值
 */
function queryTaskAttrData(taskId){

        var requestParam = "{'taskId':"+taskId+"}";

        $.ajax({
          type: 'POST',
          url: '/HcFtpToFileSystemConfigList/queryTaskAttrs',
          data: {
                ftpItemJson:requestParam
          },
          dataType: 'json',
          success: function(data){
            // 重新加载数据
            if(data.RESULT_CODE == '0000'){
                // 将模板显示在新建任务面板上
                fillEditTaskAttrInputValue(data);
            }else{
                $('#alertContext').html(data.RESULT_MSG);
                $('#alertModal').modal('show');
            }
            fadeLoader("OFF");
          },
          error:function(data){
            // 失败时运行,目前不处理
            $('#alertContext').html('加载模板失败，可能是没有配置模板！'+data);
            $('#alertModal').modal('show');
            fadeLoader("OFF");
          }
        });
}

function fillEditTaskAttrInputValue(data){
    var resultInfo = eval("("+data.RESULT_INFO+")");

    var taskAttrs = resultInfo.TASK_ATTRS;

    for(var taskAttrIndex = 0 ;taskAttrIndex < taskAttrs.length;taskAttrIndex ++){
        var taskAttr = taskAttrs[taskAttrIndex];
        var itemSpecCd = taskAttr.ITEM_SPEC_ID;
        var value = taskAttr.VALUE;

        $('#'+itemSpecCd+'Edit').val(value);
    }
}
/**
 * 获取任务属性
 */
function queryTaskAttr(obj,doType){

    if(obj == null) {
        return ;
    }

    var taskTample = obj.value;
    if ('U' == taskTample){
        return "改功能未开放请等待!!!!!!!!!!!!!!";
    }

    var requestParam = "{'uOrD':'"+taskTample+"'}";
    // 请求处理
     $.ajax({
          type: 'POST',
          url: '/HcFtpToFileSystemConfigList/questTaskTample',
          data: {
                ftpItemJson:requestParam
          },
          dataType: 'json',
          success: function(data){
            // 重新加载数据
            if(data.RESULT_CODE == '0000'){
                // 将模板显示在新建任务面板上
                showTample(eval("("+data.RESULT_INFO+")"),doType);
                if(doType == "Edit"){ // 如果是编辑，填充数据
                	// 调用后台查询对应数据填充属性
                    queryTaskAttrData(obj.taskId);  	
                }
            }else{
                $('#alertContext').html(data.RESULT_MSG);
                $('#alertModal').modal('show');
            }
            fadeLoader("OFF");
          },
          error:function(data){
            // 失败时运行,目前不处理
            $('#alertContext').html('加载模板失败，可能是没有配置模板！'+data);
            $('#alertModal').modal('show');
            fadeLoader("OFF");
          }
        });
}
/**
 * 显示模板
 */
function showTample(obj,doType){
    var taskTample = obj.U_OR_D;
    var taskItems = obj.TASK_ITEMS;
    var taskAttrs = "";
    for(var itemIntex = 0 ;itemIntex < taskItems.length; itemIntex++){
        if(itemIntex % 3 == 0){
            taskAttrs += "<div class='row' name='taskAttrs"+doType+"'>";
        }
        taskAttrs = taskAttrs
                  + "<div class='col-md-4 form-group form-horizontal'>"
                  + "<label class='col-md-4 control-label'>"+taskItems[itemIntex].NAME+"：</label>"
                  + "<div class='col-md-8'>"
                  + "<input type='text' class='form-control' name='"+taskItems[itemIntex].ITEM_SPEC_CD+doType+"' id='"+taskItems[itemIntex].ITEM_SPEC_CD+doType+"'  placeholder='"+taskItems[itemIntex].DESCRIBE+"'/>"
                  + "</div>"
                  + "</div>";
        if(itemIntex % 3 == 2){
             taskAttrs += "</div>";
         }
    }
    // 判断最后一行如果不够三个时需要加</div>的情况
    if(taskItems.length % 3 != 0){
        taskAttrs += "</div>";
    }
    $("div[name='taskAttrs"+doType+"']").remove();
    $('#lastTastInfoDiv'+doType).after(taskAttrs);
}
/**
 * 保存任务信息
 */
function saveTaskInfo(doType){

    var taskNameNew = $('#taskName'+doType).val();
    var taskCronNew = $('#taskCron'+doType).val();
    var tNumNew = $('#tNum'+doType).val();
    var upNulNew = $('#upNul'+doType).val();
    var errPhoneNew = $('#errPhone'+doType).val();
    var preFlagNew = $('#preFlag'+doType).val();
    var preFunctionNew = $('#preFunction'+doType).val();
    var afterFlagNew = $('#afterFlag'+doType).val();
    var afterFunctionNew = $('#afterFunction'+doType).val();
    var uOrDNew = $('#uOrD'+doType).val();
    var fileNameNew = $('#fileName'+doType).val();
    var taskId = -1;
    var url = "/HcFtpToFileSystemConfigList/addFtpItem";// 新增时的URL
    if(doType == 'Edit'){
        url = '/HcFtpToFileSystemConfigList/editFtpItem';// 编辑时的URL
        taskId = $('#taskId'+doType).val();
    }
    var taskAttrsInputs = $("div[name='taskAttrs"+doType+"'] input");
    var taskAttrs = "["; // 任务属性#itemSpecId#,#value#
    for(var taskAttrInputIndex = 0 ; taskAttrInputIndex < taskAttrsInputs.length;taskAttrInputIndex++){
           var taskAttrsInput = taskAttrsInputs[taskAttrInputIndex];
           taskAttrs += ('{"itemSpecId":"'+$(taskAttrsInput).attr('name').substring(0,$(taskAttrsInput).attr('name').length-doType.length)+'","value":"'+$(taskAttrsInput).val()+'"},');
    }
    // 模板配置有问题，无法获取属性信息
    if(taskAttrs.length == 1){
        $('#errorAlertInfo'+doType).html("模板配置有问题，无法获取属性信息，请联系管理员");
        $('#error_alert_div'+doType).attr('class','alert alert-danger alert-dismissible show');
        return;
    }

    taskAttrs = taskAttrs.substring(0,taskAttrs.length-1) + "]";

     // 组装参数
    var taskInfo = '{"taskId":'+taskId+',"taskName":"'+taskNameNew+'","taskCron":"'+taskCronNew+'","tNum":"'
                        +tNumNew+'","upNull":"'+upNulNew+'","errPhone":"'+errPhoneNew+'","preFlag":"'
                        +preFlagNew+'","preFunction":"'+preFunctionNew+'","afterFlag":"'+afterFlagNew+'","afterFunction":"'
                        +afterFunctionNew+'","uOrD":"'+uOrDNew+'","fileName":"'+fileNameNew+'"}';
    var requestParam = '{"taskInfo":'+taskInfo+',"taskAttrs":'+taskAttrs+'}'
    var validateObj = validate(requestParam);
    if(validateObj.RESULT_CODE != 0){ // 校验失败
        $('#errorAlertInfo'+doType).html(validateObj.RESULT_MSG);
        $('#error_alert_div'+doType).attr('class','alert alert-danger alert-dismissible show');
        return;
    }
    fadeLoader("ON");

    // 请求处理
     $.ajax({
          type: 'POST',
          url: url,
          data: {
                ftpItemJson:requestParam
          },
          dataType: 'json',
          success: function(data){
            // 重新加载数据
            if(data.RESULT_CODE == '0000'){
                // 将模板显示在新建任务面板上
                if(doType == 'Edit'){
                    $('#editTaskModal').modal('hide');
                }else{
                    $('#newTaskModal').modal('hide');
                }
                loadTaskData();
            }else{
                $('#errorAlertInfo'+doType).html(data.RESULT_MSG);
                $('#error_alert_div'+doType).attr('class','alert alert-danger alert-dismissible show');
            }
            fadeLoader("OFF");
          },
          error:function(data){
            // 失败时运行,目前不处理
            $('#errorAlertInfo'+doType).html(data.RESULT_MSG);
            $('#error_alert_div'+doType).attr('class','alert alert-danger alert-dismissible show');
            fadeLoader("OFF");
          }
        });
}
/**
 * 根据任务名称或任务ID模糊查询
 * @return
 */
function searchTaskByNameOrId(){
	var taskName = $('#taskName').val();
	
	if(taskName == null || taskName == ''){
		loadTaskDataPage(1);
		return ;
	}
	var requestParam = '{"taskName":"'+taskName+'"}';
	$.ajax({
	      type: 'POST',
	      url: '../FtpToFileSystem_searchTaskByNameOrId.action',
	      data: {
			ftpItemJson:requestParam
	      },
	      dataType: 'json',
	      success: function(data){
	        // 成功时运行
	        dealQueryData(data);
	      },
	      error:function(data){
	        // 失败时运行,目前不处理
	          $('#alertContext').html('网络超时！查询数据失败'+data);
	          $('#alertModal').modal('show');
	          return;
	      }
	    });

}
/**
 * 校验
 */
function validate(requestParam){
    var validateObj = new Object();
    validateObj.RESULT_CODE = 0;
    validateObj.RESULT_MSG = '文件名称错误，请仔细检查';
    return validateObj;
}

/** 加载层* */
function fadeLoader(data){
    if(data == 'ON'){
        $('#loader').show();
    }else{
        $('#loader').hide();
    }
}


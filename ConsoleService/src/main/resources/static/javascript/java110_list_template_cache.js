function flush(obj,cacheCode){
    preFlush(obj);

    var reqJson = JSON.stringify(createRequestData("cacheOne",{"cacheCode":cacheCode,"pageSign":""}));

    $.ajax({
           type: "POST",
           url: "/console/flushCache",
           data: reqJson,
           contentType: "application/text",
           dataType:"text",
           success: function(data){
                   finishFlush(obj);
                   processCacheResponse(data);
              },
           error: function(data){
               //alertText("网络超时，请稍后再试");

               finishFlush(obj);
               alert('网络超时，请稍后再试');
           }
    });
}

function processCacheResponse(data){
       var data = getResponseInfo(data)
       if(data.code != "0000"){
           alert(data.msg+"，错误ID："+data.id);
           return ;
       }
       alert("刷新缓存成功");
}

function preFlush(obj){
    $(obj).html('刷新中 ...');
    $(obj).attr('class','btn btn-secondary');
}

function finishFlush(obj){
    $(obj).html('刷新缓存');
    $(obj).attr('class','btn btn-warning');
}
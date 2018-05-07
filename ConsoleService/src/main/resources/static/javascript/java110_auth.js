function auth(){
    var userCode = $('#userCode').val();
    var userPwd = $('#userPwd').val();

    if(userCode == '' || userPwd == ''){
        alertText("用户编码或密码不能为空")
        return ;
    }

    // 清除提示
    clearAlertText();

    doLogin(userCode,userPwd);

}


function clearAlertText(){
    $("#errorInfo").html('');
    $("#errorInfo").hide();
}

function alertText(message){
    $("#errorInfo").html(message);
    $("#errorInfo").show();
}

function doLogin(userCode,userPwd){
    var reqJson = JSON.stringify(createRequestData("login",{"userCode":userCode,"userPwd":userPwd,"pageSign":""}));
      $.ajax({
               type: "POST",
               url: "/loginRest/login",
               data: reqJson,
               contentType: "application/text",
               dataType:"text",
               success: function(data){
                       processResponse(data);
                  },
               error: function(data){
                   alertText("网络超时，请稍后再试");
               }
            });
}

function processResponse(data){
    var data = getResponseInfo(data)

    if(data.code != "0000"){
        alertText(data.msg+"，错误ID："+data.id);
        return ;
    }
    $(location).attr('href', '/');
}
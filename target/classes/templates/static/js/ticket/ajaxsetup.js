var flag = true;//防止重复点击
var domain = document.domain;
// var ip = /^(\d+)\.(\d+)\.(\d+)\.(\d+)$/;
// var path = (ip.test(domain))?("http://"+domain+":8081/WSS"):("http://"+document.domain);
// console.log(path);
var basePath = getHostBasePath(true);
// alert(basePath);

$.ajaxSetup({
	type: 'POST',
	complete: function(xhr,status) {
		var sessionStatus = xhr.getResponseHeader('sessionstatus');
		if(sessionStatus == 'timeout' && flag ) {
			// alert(sessionStatus);
			flag = false;
			var top = getTopWinow();
			top.location.href = basePath+"/index";
			// $.TipsBox.Alert("由于您长时间没有操作, session已过期, 请重新登录","error","",function(){
			// 	//成功回调
			// 	flag = true;
			// 	top.location.href = path+"/sys/login.htm";
			// });
		}
	}
});

/**
 * 获取请求的基本路径
 * @return 基本路径
 */
function getHostBasePath(isProjectName){
	var url = document.location.toString();
	var arrUrl = url.split("//");
	var arrUrl1_Arr =  arrUrl[1].split("/");
	if(isProjectName && isProjectName == true){
		return "http://"+arrUrl1_Arr[0]+"/"+arrUrl1_Arr[1];
	}else{
		return "http://"+arrUrl1_Arr[0];
	}
}

/**
 * 在页面中任何嵌套层次的窗口中获取顶层窗口
 * @return 当前页面的顶层窗口对象
 */
function getTopWinow(){
	var p = window;
	while(p != p.parent){
		p = p.parent;
	}
	return p;
}


//表格数据转为object
function str2Json(formSelector){
	var arr=$(formSelector).serializeArray();//"#pageCfg_form"
	var data={};
	$.each(arr,function(i,obj){
		//[{name:"a",value:"b"},{name:"f",value:"zuqiu"},{name:f,value:"lanqiu"},{name:"uname",value:"赵道稳"},{name:"record",value:"<a href='adf'..."}]
		if(data[obj["name"]]){//复选框可能提交多个
			data[obj["name"]]=data[obj["name"]]+","+obj["value"];
		}else{
			data[obj["name"]]=obj["value"];
		}
	});
	return data;
};

//表格默认参数配置
$.fn.bt=function(options){
	var defaults={
		sortable: true,	
		sortOrder : 'desc',
		method: 'post',
		contentType: "application/x-www-form-urlencoded",
		dataType: "json",
		pagination: true,   
		sidePagination: "server",
		pageNum:1, 
		pageSize:10, 
		pageList:[10,20,30], 
		singleSelect: false, 
		clickToSelect: true, 
		paginationVAlign:"bottom",
		paginationHAlign:"right",
		queryParams: function queryParams(params) {
			var basic = {
				limit: params.limit,//即：pageSize
				offset: params.offset,//即:(pageNumber-1)*pageSize,可用于分页
				sortOrder: params.order,//排序
				sortName: params.sort,//排序字段
			};
			return basic;
	   }
	};
	//options: sortName url columns
	var opts=$.extend(defaults,options);
	this.bootstrapTable(opts);
};

//提示类弹窗
function showTipModal(tip){
	$("#tip").text(tip);
	$('#tipModal').modal("show");
};

//获取URL地址栏参数
function UrlSearch(){
	var name,value;
	var str=location.href; //取得整个地址栏
	var num=str.indexOf("?");
	str = str.substr(num+1); //取得所有参数
	var arr=str.split("&"); //各个参数放到数组里
	for(var i=0;i < arr.length;i++){
		num=arr[i].indexOf("=");
		if(num>0){
			name=arr[i].substring(0,num);
			value=arr[i].substr(num+1);
			this[name]=value;
		}
	}
}

//返回顶部
function backTop() {
	$('body').append('<a id="backTop" href="javascript:;" title="返回顶部"><i></i></a>');
	//计算页面滚动条高度
	var scrollHeight = '';
	scrollHeight = $(document).height() - $(window).height();
	$(window).scroll(function() {
		if ($(this).scrollTop() > scrollHeight*0.1) {
			$('#backTop').show();
		} else {
			$('#backTop').hide();
		}
	});
	$('body').on('click', '#backTop', function() {
		$('html ,body').animate({scrollTop: 0}, 300);
		return false;
	})
}
$(function(){
	setTimeout('backTop()', 2000);
	;
});
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
	.menu button{
		width:158px;
	}
	.navbar{
		border: none;
		border-radius: 0px;
		background: #FFFFFF;
		box-shadow: 0 4px 16px 0 rgba(211,220,230,0.26);
	}
	.navbar a{
		font-size: 12px;
	}
	.nav .icon{
		display: inline-block;
		width: 8px;
		height: 4px;
		background: url('../../resources/imgs/down.svg') no-repeat;
		vertical-align: top;
		margin: 08px 0 0 2px;
	}
	.navbar-brand{
		width: 274px;
		/* width: 2.74rem; */
		height: 50px;
		margin-left: 0px !important;
		background: url('../../resources/imgs/logo_h.svg') no-repeat center;
		text-indent: -99em;
		background-size: contain;
	}
	.head-navbar-btn{
		width: 50px;
		height: 50px;
		text-align: center;
		margin-left: -15px !important;
		border-right: 1px solid #ECF1F8;
		cursor: pointer;
	}
	.head-navbar-btn img{
    display: inline-block;
		width: 24px;
    margin-top: 14px;
	}
	.navbar-nav{
		position: relative;
	}
	.navbar-nav .box{
		position: absolute;
		right: 15px;
		top: 86%;
		min-width: 150px;
		padding: 5px 0;
		background: #FFFFFF;
		box-shadow: 2px 2px 12px 0 rgba(211,220,230,0.36);
		border-radius: 2px;
		z-index: 9;
	}
	.navbar-nav .box a{
		display: block;
		padding: 0 20px;
		line-height: 36px;
		color: #687993;
		font-size: 12px;
	}
	.navbar-nav .box a:hover{
		background: #F4F8FF;
		color: #687993;
		text-decoration: none;
	}
	.navbar-nav .box a i{
		float: right;
		width: 4px;
		height: 7px;
		margin-top: 14px;
		background: url('../../resources/imgs/right.svg') no-repeat center;
	}
	.navbar-form{
		position: relative;
	}
	.navbar-form .navsearch{
		position: absolute;
		left:290px;
		top:8px;
		display: inline-block;
		width: 14px;
		height: 16px;
		background: url('../../resources/imgs/search.svg') no-repeat center;
		cursor: pointer;
	}
	.navbar-form .form-group{
		width: 315px;
	}
	.navbar-form input,	.navbar-form input:focus{
		border-radius: 100px;
		padding: 6px 40px 6px 105px;
	}
	.navbar-form select, .navbar-form select:focus{
		position: absolute;
		left: 12px;
		border: none;
		background: none;
	}
	.inline{
		display: inline-block;
		height: 25px;
		width:100px;
		margin-bottom:8px;
	}
</style>
<section id="head">
	<!-- 导航条 -->
	<!-- <div class="row" > -->
	  <!-- <div class="col-md-12"> -->
	    <nav class="navbar navbar-default">
				<div class="container-fluid">
					<div class="navbar-header head-navbar-tab" style="display: none;">
						<a class="navbar-brand" href="">X-Fraud</a>
					</div>
					<div class="navbar-header head-navbar-btn">
							<img src="resources/imgs/menu_back.svg" alt="" />
					</div>
					<ul class="nav navbar-nav navbar-right user">
						<li>
							<a href="#">你好，${userinfo.real_name }<i class="icon"></i></a>
							<div class="box" style="display: none;">
								<!-- <a href="logout">修改密码<i></i></a> -->
								<a href="logout">退出<i></i></a>
							</div>
						</li>
					</ul>
					<form id="header-form" action="toSearchResult" class="navbar-form navbar-right">
						<div class="form-group">
							<select class="form-control type" name="type">
								<option value="certi_no" selected="selected">身份证号</option>
								<option value="email">邮箱</option>
								<option value="bankcardnumber">银行卡</option>
								<option value="phonenumber">手机</option>
							</select>
							<input type="text" name="key" class="form-control" placeholder="Search">
							<input type="hidden" name="from" class="form-control" placeholder="Search" value="basic">
						</div>
						<i class="navsearch" id="navsearch"></i>
						<a href="#" id="navAdSearch">高级搜索</a>
					</form>
				</div><!-- /.container-fluid -->
			</nav>	
	  <!-- </div> -->
	<!-- </div> -->
	<!-- 高级搜索框 -->
	<div class="modal fade" data-backdrop="static" data-keyboard="false" tabindex="-1" id="advancedSearchModal">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" ><span>&times;</span></button>
	        <h4 class="modal-title">高级搜索</h4>
	      </div>
	      <div class="modal-body">
	          <form class="form-horizontal" action="toAdSearchResult?from=ad" method="post" id="advancedForm">
				  <div class="form-group">
				    <label for="inputEmail3" class="col-sm-3 control-label">个人信息</label>
				    <div class="col-sm-9">
					    <input type="text" class="form-control input-sm inline" name="name" placeholder="姓名"> 
					    <input style="width:250px" type="text" class="form-control input-sm inline" name="certi_no" placeholder="身份证"> 
					    <input style="width:250px" type="text" class="form-control input-sm inline" name="mobile" placeholder="手机号"> 
					    <input style="width:250px" type="text" class="form-control input-sm inline" name="relativename" placeholder="紧急联系人姓名"> 
					    <input style="width:250px" type="text" class="form-control input-sm inline" name="relativephone" placeholder="紧急联系人手机号"> 
					    <input style="width:250px" type="text" class="form-control input-sm inline" name="bankcardnumber" placeholder="银行卡号"> 
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="inputPassword3" class="col-sm-3 control-label">贷款信息</label>
				    <div class="col-sm-9">
				      <input style="width:250px;display: block;" type="text" class="form-control input-sm inline" name="product_name" placeholder="贷款产品名称">
				      <input style="width:200px;" type="text" class="form-control input-sm inline" 
				      id="loan_apply_date_start" name="loan_apply_date_start" placeholder="申请时间开始"
				       onfocus="cfgStart()">
				      -&nbsp;<input style="width:200px;" type="text" class="form-control input-sm inline" 
				      id="loan_apply_date_end" name="loan_apply_date_end" placeholder="申请时间结束"
				       onfocus="cfgEnd()">
				      <input style="width:200px;" type="text" class="form-control input-sm inline" name="apply_amount_start" placeholder="借款金额范围开始">
				      -&nbsp;<input style="width:200px;" type="text" class="form-control input-sm inline" name="apply_amount_end" placeholder="借款金额范围结束">
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="inputPassword3" class="col-sm-3 control-label">群组标签</label>
				    <div class="col-sm-9" id="systemtaghead"></div>
				  </div>
				  <div class="form-group">
				    <label for="inputPassword3" class="col-sm-3 control-label">是否在关注名单</label>
				    <div class="col-sm-9">
				       <label class="radio-inline">
						  <input type="radio" name="followlist" value="noctl" checked="checked"> 不限制
						</label>
				       <label class="radio-inline">
						  <input type="radio" name="followlist" value="yes"> 是
						</label>
						<label class="checkbox-inline">
						  <input type="radio" name="followlist" value="no"> 否
						</label>
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="col-sm-offset-3 col-sm-6">
				      <input type="submit" id="btnAdd" class="btn btn-info" style="margin-left:40px" value="确认查询">
				      <input type="reset" class="btn btn-default" style="margin-left:40px" value="清除条件">
				    </div>
				  </div>
				</form>
	      </div>
	    </div><!-- /.modal-content -->
	  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->		
	<!-- 确认是否使用上次查询条件-->
	<div class="modal fade" data-backdrop="static" data-keyboard="false" tabindex="-1" id="resetCondModal">
	  <div class="modal-dialog modal-sm">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" ><span>&times;</span></button>
	        <h4 class="modal-title" style="color:#fff">是否使用上次的搜索条件？</h4>
	      </div>
	      <div class="modal-body" style="padding-bottom: 15px;">
	         <h2 style="text-align: center;">是否使用上次的搜索条件？</h2>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-info" id="btnUseLastCond">是</button>
	        <button type="button" class="btn btn-default" id="resetCondBtn">否，重新设置</button>
	      </div>
	    </div><!-- /.modal-content -->
	  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->		
</section>
<script type="text/javascript">
$("#navAdSearch").click(function(){
	$("#resetCondModal").modal("show");
});
$('.user').click(function() {
	if ($(this).find('.box').is(':hidden')) {
		$(this).find('.box').show();
	} else {
		$(this).find('.box').hide();
	}
})
//重新设置上次搜索条件
$("#resetCondBtn").click(function(){
	$("#resetCondModal").modal("hide");
	var html=$("#systemtaghead").html();
	if(!html){//系统标签为空，未加载就去加载
		$.ajax({
			type:"post",
			url:"getSystemTags",
		    data:str2Json("#loginform"),
		    success:function(data){
		    	html="";
		    	$.each(data,function(i,obj){
		    		html+='<label class="checkbox-inline">';
			    	html+='<input type="checkbox" name="systemtag" value="'+obj["id"]+'"> '+obj["name"];
			    	html+='</label>';
		    	});
		    	$("#systemtaghead").html(html);
		    }
		});
	}
	$("#advancedSearchModal").modal("show");
});
//使用上次搜索条件
$("#btnUseLastCond").click(function(){
	location.href="toAdSearchResult";
	
});
$("#navsearch").click(function(){
	$('#header-form').submit();
});
function cfgStart(){
    WdatePicker({
        dateFmt:'yyyy-MM-dd', 
        autoUpdateOnChanged:true, 
        maxDate:"#F{$dp.$D('loan_apply_date_end')}",
        isShowOK:false,//不显示确定按钮
        isShowOthers:false//不显示本月之外的日期
    });
}
function cfgEnd(){
    WdatePicker({
        dateFmt:'yyyy-MM-dd',
        autoUpdateOnChanged:true,
        minDate:"#F{$dp.$D('loan_apply_date_start')}",
        isShowOK:false,//不显示确定按钮
        isShowOthers:false//不显示本月之外的日期
    });
}


</script>

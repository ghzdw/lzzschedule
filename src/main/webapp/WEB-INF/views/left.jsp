<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <link rel="stylesheet" href="resources/css/base.css">
        <!--初始化文件-->
        <link rel="stylesheet" href="resources/css/menu.css">
        <!--主样式-->
        <link rel="stylesheet" href="resources/font-awesome-4.7.0/css/font-awesome.min.css">
        <!--主样式-->
        <script src="resources/js/adapter.js"></script>
        <!--rem适配js-->
        <div id="menu">
            <!--隐藏菜单-->
            <div id="ensconce">
                <h2>
                    <span class="menu-tab">
                        <img src="resources/imgs/menu_draw.svg" alt="">
                    </span>
                    <b class="fa fa-home"></b>
                    <b class="fa fa-desktop"></b>
                    <b class="fa fa-calculator"></b>
                    <b class="fa fa-cloud"></b>
                    <b class="fa fa-line-chart"></b>
                    <b class="fa fa-newspaper-o"></b>
                </h2>
            </div>

            <!--显示菜单-->
            <div id="open">
                <div class="navH">
                    <!-- X-fraud反欺诈
                    <span>
                        <img class="obscure" src="resources/images/obscure.png" alt="">
                    </span> -->
                    <img class="obscure" src="resources/imgs/logo_v.svg" width="60%" alt="X-fraud反欺诈" />
                </div>
                <div class="navBox">
                    <ul>
                        <li class="activedark">
                            <div class="stick"></div>
                            <a href="index">
                                <span class="item obtain">
                                    <b class="fa fa-home"></b>
                                   		用户首页</span>
                            </a>
                        </li>
                        <li>
                            <div class="stick opacity0"></div>
                            <span class="item obtain">
                                <b class="fa fa-desktop"></b>
                                	我的
                                <i class="fa fa-angle-right"></i>
                            </span>
                            <div class="secondary">
                                <a href="toRechargeManageUser">
                                    <span class="subitem">充值记录</span>
                                </a>
                                <a href="toUseLogManageUser">
                                    <span class="subitem">调用接口记录</span>
                                </a>
                            </div>
                        </li>
                        <li>
                            <div class="stick opacity0"></div>
                            <span class="item obtain">
                                <b class="fa fa-line-chart"></b>
                                	系统管理
                                <i class="fa fa-angle-right"></i>
                            </span>
                            <div class="secondary">
                                <a href="toUserManage">
                                    <span class="subitem">账户管理</span>
                                </a>
                                <a href="toCompanyManage">
                                    <span class="subitem">公司管理</span>
                                </a>
                                <a href="toRechargeManage">
                                    <span class="subitem">充值管理</span>
                                </a>
                                <a href="toInterManage">
                                    <span class="subitem">接口信息</span>
                                </a>
                                <a href="toUseLogManage">
                                    <span class="subitem">调用接口记录</span>
                                </a>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <script src="resources/js/menu.js"></script>
        <!--控制js-->
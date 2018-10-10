window.onload = function () {
    var flag = true;
    var liC = document.querySelectorAll(".navBox li span.item");
    var href = (location.pathname+location.search+location.hash).slice(1);
    var position = [];
    for (var j=0; j<$('.secondary').length; j++){
        $('.secondary').eq(j).attr('data-height', $('.secondary').eq(j).height());
        if ($('.secondary').eq(j).find('a').length > 0) {
            for(var i=0; i<$('.secondary').eq(j).find('a').length; i++) {
                var _this = $('.secondary').eq(j).find('a').eq(i);
                if (href.indexOf($(_this).attr('href'))>-1) {
                    position.push(j);
                    position.push(i);
                }
            }
        }
        if (j==($('.secondary').length-1)){
            $('.secondary').css({'display': 'block', 'height':'0px'});
            if ( position.length > 0) {
                var _thisNav = $('.secondary').eq(position[0]).find('a').eq(position[1]);
                $('.navBox li').removeClass("activedark");
                // $(_thisNav).parents('li').addClass('activedark');
                $(_thisNav).parent().css({'display':'block', 'height': $(_thisNav).parent().data('height') + "px"});
                $(_thisNav).parent().siblings('span.item').addClass('obFocus');
                $(_thisNav).parent().siblings('span.item').find('i.fa').addClass('arrowRot');
                $(_thisNav).find('span').addClass('active');
            }
        }
    }
    // 主导航nav点击事件
    for (var i = 0; i < liC.length; i++) {
        liC[i].onclick = function () {

        	var cxt=$(this).parents("li");
        	// cxt.addClass("activedark");
        	// cxt.siblings("li").removeClass("activedark");
        	// cxt.find("div.stick").removeClass("opacity0");
        	// cxt.siblings("li").find("div.stick").addClass("opacity0");
            if (flag) {
                // 节流阀
                flag = false;
                setTimeout(function () {
                    flag = true;
                }, 500);
                // 已经展开
                if (this.className.indexOf("obFocus")>-1) {
                    this.querySelector("i").classList.remove("arrowRot");
                    getNext(this).style.height = "0";
                    this.classList.add("obtain");
                    this.classList.remove("obFocus");
                    return;
                }

                var sec = getNext(this);
                if(!sec) return;//有的菜单并没有子菜单
                var sib = siblings(sec.parentNode);
                var otherArr = [];
                var arrowClass = [];
                // 排他 secondary arrowRot obFocus
                for (var j = 0; j < sib.length; j++) {
                    var sibSec = sib[j].getElementsByTagName('*');
                    for (var i = 0; i < sibSec.length; i++) {
                        if ($(sibSec[i]).hasClass("secondary")) {
                            otherArr.push(sibSec[i]);
                        }
                        if ($(sibSec[i]).hasClass("arrowRot")) {
                            arrowClass.push(sibSec[i]);
                        }
                        // if (sibSec[i].className.indexOf("obFocus")>-1) {
                        //     sibSec[i].classList.remove("obFocus");
                        //     sibSec[i].classList.add("obtain");
                        // }
                    }
                }
                for (var i = 0; i < otherArr.length; i++) {
                    // otherArr[i].style.height = "0";
                    $('.secondary').css({'height':'0px'});
                }
                console.log(arrowClass)
                if (arrowClass[0]) {
                    arrowClass[0].classList.remove("arrowRot");
                }

                // 留自己
                $(sec).css({'display':'block', 'height': $(sec).data('height') + "px"});
                // sec.style.height = 2.5078 + "rem";
                this.getElementsByTagName("i")[0].classList.add("arrowRot");
                this.classList.remove("obtain");
                this.classList.add("obFocus");
            }
        };
    }

    // 子导航点击事件
    // var seconC = document.querySelectorAll(".secondary h3");
    // for (var i = 0; i < seconC.length; i++) {
    //     seconC[i].onclick = function () {
    //         for (var i = 0; i < seconC.length; i++) {
    //             seconC[i].classList.remove("seconFocus");
    //         }
    //         this.classList.add("seconFocus");
    //     };
    // }
    
    // 隐藏菜单
    var obscure = document.querySelector(".head-navbar-btn");
    var open = document.querySelector("#open");
    var ensconce = document.querySelector("#ensconce");
    obscure.onclick = function () {
        open.style.marginLeft = "-300px";
        setTimeout(function () {
            ensconce.style.display = "block";
        }, 350);
        // var width=$("#cont").width();
        // $("#cont").width(width+220);
        $("#cont").css({'marginLeft': '50px'});
        $('.head-navbar-tab').show();
        $('.head-navbar-btn').hide();
    };
    //显示菜单
    var showC = document.querySelector("#ensconce h2");
    showC.onclick = function () {
        open.style.marginLeft = "0px";
        setTimeout(function () {
            ensconce.style.display = "none";
        }, 100);
        // var width=$("#cont").width();
        // $("#cont").width(width-220);
        $("#cont").css({'marginLeft': '260px'});
        $('.head-navbar-tab').hide();
        $('.head-navbar-btn').show();
    };
};

function getByClass(clsName, parent) {
    var oParent = parent ? document.getElementById(parent) : document,
        boxArr = new Array(),
        oElements = oParent.getElementsByTagName('*');
    for (var i = 0; i < oElements.length; i++) {
        if (oElements[i].className == clsName) {
            boxArr.push(oElements[i]);
        }
    }
    return boxArr;
}
// 获取下一个兄弟元素
function getNext(node) {
    if (!node.nextSibling) return null;
    var nextNode = node.nextSibling;
    if (nextNode.nodeType == 1) {
        return nextNode;
    }
    return getNext(node.nextSibling);
}

// 获取除了自己以外的其他亲兄弟元素
function siblings(elem) {
    var r = [];
    var n = elem.parentNode.firstChild;
    for (; n; n = n.nextSibling) {
        if (n.nodeType === 1 && n !== elem) {
            r.push(n);
        }
    }
    return r;
}
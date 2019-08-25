
//回复功能方法封装
function CommentFunction(parentId,type,content) {
    if (content == null || content == ""){
        alert("回复内容不能为空！");
        return;
    }

    $.ajax({
        type:"POST",
        url:"/comment",
        contentType:'application/json',
        data:JSON.stringify({
            "parentId":parentId,
            "content":content,
            "type":type
        }),
        dataType:"json",
        success:function (response) {
            if (response.code == 200) {
                //回复成功刷新页面
                window.location.reload();
            }else if (response.code == 2003) {
                //检测没有登录，需要登录
                var isAccepted = confirm(response.message);
                if (isAccepted) {
                    window.open("https://github.com/login/oauth/authorize?client_id=2e1254eee222b0befd52&redirect_uri=http://www.yuyang666.club/callback&scope=user&state=1");
                    window.localStorage.setItem("closable",true);
                }
            }else {
                alert(response.message);
            }
        }
    });
}

//回复问题
function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    CommentFunction(questionId,1,content);
}


//回复评论
function comment(e) {
   var commentId = e.getAttribute("data-id");
    console.log(commentId);
    var content = $("#input-" + commentId).val();
    console.log(content);
    CommentFunction(commentId,2,content);
}

//展开二级评论
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comment = $("#comment-" + id);
    //获取一下二级评论的展开状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        //存在说明已展开此时想要折叠
        comment.removeClass("in");
        //移除展开标记
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {
        var subCommentContainer = $("#comment-" + id);
        if (subCommentContainer.children().length != 1) {
            //展开二级评论
            comment.addClass("in");
            // 标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        } else {
            $.getJSON("/comment/" + id, function (data) {
                $.each(data.data.reverse(), function (index, comment) {
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(comment.gmtCreate).format('YYYY-MM-DD')
                    })));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });
                //不存在说明此时点击想要展开
                comment.addClass("in");
                //标记评论展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }
    }
}

//实现点击标签输入框弹出标签库,点击空白隐藏
function showSelectTag() {
    $("#select-tag").show();
}
function hideSelectTag() {
    $("#select-tag").hide();
}

//实现标签库标签点击显示在输入框
function selectTag(e){
    var value = e.getAttribute("data-tag");
    var previous = $("#tag").val();
   //先判断输入框是否已经写入该标签
   if (previous.indexOf(value) == -1) {
       if (previous){
           $("#tag").val(previous + ',' + value);
       }else{
           $("#tag").val(value);
       }
   }
}

// 点赞功能
function thumb(e) {
    var questionId = e.getAttribute("data-questionId");
    var userId = e.getAttribute("data-userId");
    var state = e.getAttribute("data-state");
    if (state == "0") {
        alert("请先登录才能点赞！")
    } else if (state == "1") {
        $.ajax({
            type: "POST",
            url: "/thumb",
            data: ({
                "questionId": questionId,
                "userId": userId,
                "state": '1'
            }),
            success: function (response) {
                if (response != null) {
                    e.setAttribute("data-state", '2');
                    $("#thumb-up").addClass("thumbOk");
                    $("#thumb-val").text("已点赞(");
                    $("#thumb-num").text(response);
                }
            }
        });
    } else if (state == "2") {
        $.ajax({
            type: "POST",
            url: "/thumb",
            data: ({
                "questionId": questionId,
                "userId": userId,
                "state": '2'
            }),
            success: function (response) {
                e.setAttribute("data-state", '1');
                $("#thumb-up").removeClass("thumbOk");
                $("#thumb-val").text("点赞(");
                $("#thumb-num").text(response);
            }
        });
    }
}

//收藏功能
function collect(e){
    var questionId = e.getAttribute("data-questionId");
    var userId = e.getAttribute("data-userId");
    var state = e.getAttribute("data-state");
    if (state == "0") {
        alert("请先登录才能收藏！")
    }else if (state == "1") {
        $.ajax({
            type:"POST",
            url:"/collect",
            data:({
                "questionId":questionId,
                "userId":userId,
                "state":'1'
            }),
            success:function (response) {
                if (response != null){
                    e.setAttribute("data-state",'2');
                    $("#collect").addClass("thumbOk");
                    $("#collect-val").text("已收藏(");
                    $("#collect-num").text(response);
                }
            }
        });
    }else if (state == "2") {
        $.ajax({
            type: "POST",
            url: "/collect",
            data: ({
                "questionId": questionId,
                "userId": userId,
                "state": '2'
            }),
            success: function (response) {
                e.setAttribute("data-state", '1');
                $("#collect").removeClass("thumbOk");
                $("#collect-val").text("收藏(");
                $("#collect-num").text(response);
            }
        });
    }
}

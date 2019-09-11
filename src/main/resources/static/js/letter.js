$(function(){
	$("#sendBtn").click(send_letter);
	$("#deleteBtn").click(delete_letter);
	$(".close").click(delete_msg);
});

function delete_letter() {
    // $("#sendModal").modal("hide");
    var deleteId = $("#delete-id").val();
    var content = $("#message-text").val();
    $.post(
        CONTEXT_PATH + "/letter/delete",
        {"deleteId": deleteId},
        function (data) {
            data = $.parseJSON(data);
            if (data.code == 0) {
                $("#hintBody").text("删除成功");
            } else {
                $("#hintBody").text(data.msg);
            }
            $("#hintModal").modal("show");
            setTimeout(function(){
                $("#hintModal").modal("hide");
                location.reload();
            }, 2000);
        }
    )
}

function send_letter() {
	$("#sendModal").modal("hide");
    var toName = $("#recipient-name").val();
    var content = $("#message-text").val();
    $.post(
        CONTEXT_PATH + "/letter/send",
		{"toName":toName, "content": content},
		function (data) {
            data = $.parseJSON(data);
            if (data.code == 0) {
                $("#hintBody").text("发送成功");
            } else {
                $("#hintBody").text(data.msg);
			}
            $("#hintModal").modal("show");
            setTimeout(function(){
                $("#hintModal").modal("hide");
                location.reload();
            }, 2000);
        }
	)
}

function delete_msg() {
	// TODO 删除数据
	$(this).parents(".media").remove();
}
let form = document.getElementById("loginForm");
form.addEventListener("submit", event => {
    event.preventDefault();

    $(document).ready(function () {

        const email = $("#email").val()
        const password = $("#password").val()

        // 모든 항목 작성 검사
        if (!email || !password) {
            alert("모든 항목을 작성해주세요.");
            return;
        }

        const loginRequest = {
            email : email,
            password : password
        }

        $.ajax({
            type: 'POST',
            url: "/member/login",
            dataType:"json",
            contentType : "application/json; charset=utf-8",
            data: JSON.stringify(loginRequest),

            success: function (response) {
                console.log(response);
            },
            error: function (error) {
                console.log(error);
                console.log(error.responseText);
            }
        });
    });
});

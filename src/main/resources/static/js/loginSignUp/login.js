let form = document.getElementById("loginForm");
form.addEventListener("submit", event => {
    event.preventDefault();

    $(document).ready(function () {

        // 폼 데이터를 추출합니다.
        const username = $('#email').val()
        const password = $('#password').val()

        const loginDto = {"username": username, "password": password};

        $.ajax({
            type: 'POST',
            url: '/user/auth/login',
            headers: {
                'content-type': 'application/json'
            },
            data: JSON.stringify(loginDto),
            success: function (response) {
                console.log('Login successful:', response);
                window.location.replace("/");
            },
            error: function (error) {
                // 요청이 실패하면 이 함수가 호출됩니다.
                console.error('Login failed:', error);
                alert("로그인에 실패하였습니다.")
            }
        });
    });
});

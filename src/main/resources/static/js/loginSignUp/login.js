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
            url: '/member/login',
            headers: {
                'content-type': 'application/json'
            },
            data: JSON.stringify(loginDto),
            success: function (response) {
                console.log('Login successful:', response);
                // localStorage.setItem('access_token', response.accessToken);

                // 쿠키에 access_token 저장
                // var now = new Date();
                // now.setTime(now.getTime() + 1 * 3600 * 1000); // 1시간 후 만료
                // document.cookie = "access_token=" + response.accessToken + ";expires=" + now.toUTCString() + ";path=/";
                window.location.replace("/");
            },
            error: function (error) {
                // 요청이 실패하면 이 함수가 호출됩니다.
                console.error('Login failed:', error.responseText);
                alert("로그인에 실패하였습니다.")
                // 로그인 실패 시의 처리를 여기에 작성할 수 있습니다.
            }
        });
    });
});

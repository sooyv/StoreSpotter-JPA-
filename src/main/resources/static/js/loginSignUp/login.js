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
                // 요청이 성공적으로 완료되면 이 함수가 호출됩니다.
                console.log('Login successful:', response);
                localStorage.setItem('access_token', response.accessToken);
                window.location.replace("/");
            },
            error: function (error) {
                // 요청이 실패하면 이 함수가 호출됩니다.
                console.error('Login failed:', xhr.responseText);
                // 로그인 실패 시의 처리를 여기에 작성할 수 있습니다.
            }
        });
    });
});

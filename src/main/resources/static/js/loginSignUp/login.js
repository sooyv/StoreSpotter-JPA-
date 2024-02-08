let form = document.getElementById("loginForm");

form.addEventListener("submit", event => {
    event.preventDefault();

    $(document).ready(function () {

        // 폼 데이터를 추출합니다.
        const username = $('#email').val()
        const password = $('#password').val()


        // AJAX를 사용하여 서버에 데이터를 전송합니다.
        $.ajax({
            url: '/member/login', // 요청을 보낼 서버의 URL 주소입니다.
            type: 'POST', // HTTP 요청 방식입니다.
            data: {
                username: username,
                password: password
            },
            success: function (response) {
                // 요청이 성공적으로 완료되면 이 함수가 호출됩니다.
                console.log('Login successful:', response);

                localStorage.setItem('access_token', response.token);
                window.location.replace("/");

            },
            error: function (xhr, status, error) {
                // 요청이 실패하면 이 함수가 호출됩니다.
                console.error('Login failed:', xhr.responseText);
                // 로그인 실패 시의 처리를 여기에 작성할 수 있습니다.
            }
        });
    });
});

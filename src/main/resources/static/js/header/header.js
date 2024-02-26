document.getElementById("menu-bar-btn").addEventListener("click", function () {
    let header_content = document.getElementById("header-content");
    header_content.classList.toggle("active");
});


// 로그아웃
$(document).ready(function() {
    $('#logout').click(function(event) {
        // event.preventDefault(); // 기본 동작을 막습니다.
        console.log("logout 실행")
        // POST 요청을 보냅니다.
        $.ajax({
            type: 'POST',
            url: '/member/logout',
            success: function(response) {
                console.log('로그아웃이 완료되었습니다.', response);

            },
            error: function(xhr, status, error) {
                console.error('로그아웃 요청이 실패했습니다.', error);
            }
        });
    });
});
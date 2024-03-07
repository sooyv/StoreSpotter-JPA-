// 토글 클릭시 900px 이하의 화면에서는 "000님 반갑습니다" 나오지 않도록 수정
window.addEventListener("load", function () {
    let menuBarButton = document.getElementById("menu-bar-btn");
    let headerContent = document.getElementById("header-content");
    let userNicknameMsg = document.getElementById("userNickname-msg");

    menuBarButton.addEventListener("click", function () {
        headerContent.classList.toggle("active");
        toggleUserNicknameMsgDisplay();
    });

    window.addEventListener("resize", function () {
        toggleUserNicknameMsgDisplay();
    });

    function toggleUserNicknameMsgDisplay() {
        if (window.innerWidth <= 900 && userNicknameMsg) {
            userNicknameMsg.style.display = "none";
        } else if (userNicknameMsg) {
            userNicknameMsg.style.display = ""; // 기본값으로 설정하여 CSS에 따라 표시됩니다.
        }
    }
});



// 로그아웃
$(document).ready(function() {
    $('#logout').click(function(event) {
        event.preventDefault();
        $.ajax({
            type: 'POST',
            url: '/member/logout',
            success: function(response) {
                console.log('로그아웃이 완료되었습니다.', response);
                window.location.replace("/");
            },
            error: function(xhr, status, error) {
                console.error('로그아웃 요청이 실패했습니다.', error);
            }
        });
    });
});



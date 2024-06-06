// 토글 클릭시 900px 이하의 화면에서는 "000님 반갑습니다" 나오지 않도록 수정
window.addEventListener("load", function () {
    let menuBarButton = document.getElementById("menu-bar-btn");
    let headerContent = document.querySelector(".header-content");
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
            userNicknameMsg.style.display = "";
        }
    }
});

// 로그아웃
$(document).ready(function() {
    $('#logout').click(function(event) {
        event.preventDefault();
        $.ajax({
            type: 'POST',
            url: '/user/auth/logout',
            success: function(response) {
                window.location.replace("/");
            },
            error: function(xhr, status, error) {
            }
        });
    });
});



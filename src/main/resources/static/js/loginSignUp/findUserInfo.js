let findEmail = $("#find-email");
let findPassword = $("#find-password");
let findEmailForm = $("#find-email-form");
let findPasswordForm = $("#find-password-form");

// 첫화면 password 찾기 hide(); 이메일 찾기만 보임
findPasswordForm.hide();
findEmailForm.show();
findEmail.addClass("clicked");
findPassword.removeClass("clicked");
findEmail.css({
    background: "#23567d",
    color: "#fff"
});
// find-password 요소에 적용된 스타일 제거
findPassword.css({
    background: "#fff",
    color: "black"
});

// 이메일 찾기 클릭
findEmail.on("click", function() {
    findEmailForm.show();
    findPasswordForm.hide();
    findEmail.addClass("clicked");
    findPassword.removeClass("clicked");


    $(this).css({
        background: "#23567d",
        color: "#fff"
    });

    // find-password 요소에 적용된 스타일 제거
    findPassword.css({
        background: "#fff",
        color: "black"
    });
});

// find-email에 호버 효과 추가
findEmail.on("mouseenter", function() {
    $(this).css({
        background: "#6796b4",
        transition: "0.2s ease"
    });
});
findEmail.on("mouseleave", function() {
    // 클릭된 상태인지 확인
    if (findEmail.hasClass("clicked")) {
        // 클릭되어 있다면 클릭시 색으로 변경
        $(this).css({
            background: "#23567d",
            color: "#fff"
        });
    } else if (!findEmail.hasClass("clicked")) {
        // 클릭되어 있지 않으면 기본 스타일로 변경
        $(this).css({
            background: "#fff",
            color: "#black"
        });
    }
});


// 비밀번호 찾기 클릭
findPassword.on("click", function() {
    findPasswordForm.show();
    findEmailForm.hide();
    findPassword.addClass("clicked");
    findEmail.removeClass("clicked");


    $(this).css({
        background: "#23567d",
        color: "#fff"
    });

    findEmail.css({
        background: "#fff",
        color: "black"
    });
});

// find-password에 호버 효과 추가
findPassword.on("mouseenter", function() {
    $(this).css({
        background: "#6796b4",
        transition: "0.2s ease"
    });
});
findPassword.on("mouseleave", function() {
    // 클릭된 상태인지 확인
    if (findPassword.hasClass("clicked")) {
        // 클릭되어 있다면 클릭시 색으로 변경
        $(this).css({
            background: "#23567d",
            color: "#fff"
        });
    } else if (!findPassword.hasClass("clicked")) {
        // 클릭되어 있지 않으면 기본 스타일로 변경
        $(this).css({
            background: "#fff",
            color: "#black"
        });
    }
});



// ------------------ 이메일 찾기 ---------------------



// ------------------ 비밀번호 재발급 ------------------
$("#reissue-password").on("click", function() {
    const email = $("#email").val();
    console.log("비밀번호 재발급 : " + email);
    if (email == "") {
        alert("이메일을 입력하세요");
        email.focus();
        return;
    }
    $.ajax({
        type: 'POST',
        url: "",
        data: {
            email : email
        },
        success: function(response) {
            console.log(response)
            alert("이메일로 새로운 비밀번호가 발송되었습니다." +
                "다시 로그인해주세요.");
        },
        error: function(error) {
            console.log(error)
        }
    });
});



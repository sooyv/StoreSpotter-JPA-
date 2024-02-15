let findEmail = $("#find-email");
let findPassword = $("#find-password");
let findEmailForm = $("#find-email-form");
let findPasswordForm = $("#find-password-form");

// 첫화면 password 찾기 hide(); 이메일 찾기만 보임
findPasswordForm.hide();
findEmailForm.show();
findEmail.css({
    background: "#23567d",
    color: "#fff"
});
// find-password 요소에 적용된 스타일 제거
findPassword.css({
    background: "#fff",
    color: "black"
});

// 이메일 찾기 선택
findEmail.on("click", function() {
    findEmailForm.show();
    findPasswordForm.hide();

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


// 비밀번호 찾기 선택
findPassword.on("click", function() {
    findPasswordForm.show();
    findEmailForm.hide();

    $(this).css({
        background: "#23567d",
        color: "#fff"
    });

    findEmail.css({
        background: "#fff",
        color: "black"
    });
});




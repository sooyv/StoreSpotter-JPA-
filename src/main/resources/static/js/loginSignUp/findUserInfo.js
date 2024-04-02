let findEmail = $("#find-email");
let findPassword = $("#find-password");
let findEmailForm = $("#find-email-form");
let findPasswordForm = $("#find-password-form");

let emailModal = $(".email-modal");
let modalContent = $("#modal-content");

// 첫화면 modal 화면 표시 안함
emailModal.hide();

// 첫화면 password 찾기 hide(); 이메일 찾기를 보여주기
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

// find-email에 hover 효과 추가
findEmail.on("mouseenter", function() {
    $(this).css({
        background: "#c3e7ef",
        transition: "0.2s ease",

        color: "#fff"
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
    }
    if (!findEmail.hasClass("clicked")) {
        // 클릭되어 있지 않으면 기본 스타일로 변경
        $(this).css({
            background: "#fff",
            color: "black"
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

// find-password에 hover 효과 추가
findPassword.on("mouseenter", function() {
    $(this).css({
        background: "#c3e7ef",
        transition: "0.2s ease",

        color: "#fff"
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
    }
    if (!findPassword.hasClass("clicked")) {
        // 클릭되어 있지 않으면 기본 스타일로 변경
        $(this).css({
            background: "#fff",
            color: "black"
        });
    }
});



// ------------------ 이메일 찾기 ---------------------
let findEmailBtn = $("#find-email-btn");
findEmailBtn.on("click", function() {

    const userNickname = $("#name").val();
    const userPhone = $("#phone").val();

    if(!userNickname || !userPhone) {
        alert("모든 항목을 입력하세요");
        return;
    }
    $.ajax({
        type: 'POST',
        url: "/user/account",
        data: {
            userNickname: userNickname,
            phone : userPhone,
        },
        success: function (response) {
            if (response === "") {
                modalContent.html('');    // modal 초기화
                let notFoundEmail = $("<p>").text("가입 정보가 없습니다.");
                modalContent.append(notFoundEmail);
                emailModal.show();
            } else {
                modalContent.html('');    // modal 초기화
                for (let i = 0; i < response.length; i++) {
                    const userEmail = response[i];
                    const userEmails = $("<p>").text(userEmail);
                    modalContent.append(userEmails);

                }
                emailModal.show();
            }
        }
    });
});

// 모달 close
let modalClose = $("#modal-close");
modalClose.on("click", function() {
    emailModal.hide();
});



// ------------------ 비밀번호 인증메일 전송 ------------------
$("#pwd-send-mail").on("click", function() {
    const email = $("#email").val();
    if (email === "") {
        alert("이메일을 입력하세요");
        email.focus();
        return;
    }
    $.ajax({
        type: 'POST',
        url: "/user/password/mail-send",
        data: {
            email : email
        },
        success: function(response) {
            alert("이메일로 인증번호가 발송되었습니다. ");
        },
        error: function(error) {
            if (error.responseText === "UserNotFound") {
                alert("등록되지 않은 이메일 주소입니다. 다시 입력해주세요.");
            }
            if (error.responseText === "FailedUpdatePassword") {
                alert("메일전송에 실패했습니다. 다시 시도해주세요.");
            }
        }
    });
});

// ------------------ 비밀번호 재발급 ------------------
$("#reissue-password").on("click", function() {
    const email = $("#email").val();
    const mailCode = $("#mail-code").val();
    if (email === "") {
        alert("이메일을 입력하세요");
        email.focus();
        return;
    }
    if (mailCode === ""){
        alert("인증코드를 입력하세요.")
        mailCode.focus();
        return;
    }
    $.ajax({
        type: 'POST',
        url: "/user/password/reissue",
        data: {
            email : email,
            mailCode : mailCode
        },
        success: function(response) {
            alert("이메일로 새로운 비밀번호가 발송되었습니다. " +
                "다시 로그인해주세요.");
            window.location.replace("/login");
        },
        error: function(error) {
            if (error.responseText === "UserNotFound") {
                alert("등록되지 않은 이메일 주소입니다. 다시 입력해주세요.");
            }
            if (error.responseText === "notEqualMailCode") {
                alert("메일코드가 일치하지 않습니다.")
            }
            if (error.responseText === "expirationMailCode"){
                alert("만료된 인증코드입니다.")
            }
            if (error.responseText === "FailedUpdatePassword") {
                alert("비밀번호 재설정에 실패했습니다. 다시 시도해주세요.");
            }
        }
    });
});



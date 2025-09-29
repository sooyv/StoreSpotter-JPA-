let form = document.getElementById("signUpForm");
$("#passwordCheckHelp").hide();
$("#passwordHelp").hide();
$("#phoneCheckHelp").hide();

let pwRegExp = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,}$/;
let phoneRegExp = /^\d{11}$/;

// 비밀번호 형식 정규화(최소 8자, 영문 숫자 특수문자)
$("#password").on("keyup", function(event) {
    if (!pwRegExp.test($("#password").val())) {
        $("#passwordHelp").show();
    } else {
        $("#passwordHelp").hide();
    }
});

// 비밀번호 일치 검사
$("#checkPassword").on("keyup", function(event) {
    if ($("#password").val() !== $("#checkPassword").val()) {
        $("#passwordCheckHelp").show();
    } else {
        $("#passwordCheckHelp").hide();
    }
});

// 전화번호 정규식 검사
$("#phone").on("keyup", function(event) {
    if (!phoneRegExp.test($("#phone").val())) {
        $("#phoneCheckHelp").show();
    } else {
        $("#phoneCheckHelp").hide();
    }
});

// 회원가입
form.addEventListener("submit", event => {
    event.preventDefault();

    $(document).ready(function () {

        const name = $("#name").val();
        const email = $("#email").val();
        const mailCode = $("#mail-code").val();
        const password = $("#password").val();
        const checkPassword = $("#checkPassword").val();
        const phone = $("#phone").val();

        // 모든 항목 작성 검사
        if (!name || !email || !password || !checkPassword || !mailCode) {
            alert("모든 항목을 작성해주세요");
            return;
        }

        if ($("#password").val() !== $("#checkPassword").val()) {
            alert("비밀번호를 확인해주세요");
            return;
        }

        if (!phoneRegExp.test($("#phone").val())) {
            alert("전화번호는 '-'를 제외한 숫자 11자리 입니다. ex) 01012340000");
            return;
        }
        if (!pwRegExp.test($("#password").val())) {
            alert("비밀번호는 영문, 숫자, 특수문자를 포함하여 최소 8자 이상이어야합니다.");
        }

        const userDto = {"nickname": name, "username": email, "mailCode" : mailCode,
            "password" : password, "checkPassword" : checkPassword, "phone" : phone};

        $.ajax({
            type: 'POST',
            url: "/user/auth/signup",
            headers: {
                'content-type': 'application/json'
            },
            data: JSON.stringify(userDto),
            success: function (response) {
                window.location.replace("/login");
            },
            error: function(xhr) {
                console.log(xhr.responseText)
                const obj = JSON.parse(xhr.responseText);
                const code = obj.code;
                const message = obj.message
                switch(code) {
                    case "USER-DUPLICATE-400":
                        alert("이미 존재하는 회원입니다. 다른 이메일을 사용해주세요.");
                        email.focus();
                    case "MAIL-CODE-NOT-EQUAL-400":
                        alert("메일 코드가 일치하지 않습니다.");
                        mailCode.focus();
                    case "MAIL-CODE-EXPIRED-400":
                        alert("메일 인증을 재시도 해주세요. 만료된 메일 코드입니다.");
                        mailCode.focus();
                }
                $("#signUpBtn").addClass('shake');
                setTimeout(function() {
                    $("#signUpBtn").removeClass('shake'); // 0.8초 후 shake 클래스 제거
                }, 800);
            }
        });
    });
});

// 메일 인증 smtp
$("#send-mail").on("click", function() {
    const email = $("#email").val();
    if (email === "") {
        alert("이메일을 입력하세요");
        email.focus();
        return;
    }

    $.ajax({
        type: 'POST',
        url: "/signup/mail-code",
        data: {
            email : email
        },
        success: function(response) {
            console.log()
            alert("이메일로 인증번호가 발송되었습니다.");
        },
        error: function(xhr) {
            const obj = JSON.parse(xhr.responseText);
            const code = obj.code;
            const message = obj.message
            switch (code) {
              case "USER-DUPLICATE-400":
                alert("이미 존재하는 회원입니다. 다른 이메일을 사용해주세요.");
                email.focus();
                break;
              case "SMTP-SEND-FAILED-421":
                alert("인증번호 전송을 실패하였습니다. 잠시 후 다시 시도해주세요.");
                break;
              default:
                alert("요청 처리 중 오류가 발생했습니다.");
            }
        }
    })
});



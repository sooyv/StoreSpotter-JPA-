let form = document.getElementById("signUpForm");
console.log("회원가입 페이지 접근");
$("#passwordCheckHelp").hide();
$("#passwordHelp").hide();
$("#phoneCheckHelp").hide();


// 비밀번호 형식 정규화(최소 8자, 영문 숫자 특수문자)
$("#password").on("keyup", function(event) {
    console.log("pw keyup 발생")

    const pwRegExp = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,}$/;

    if (!pwRegExp.test($("#password").val())) {       // 비밀번호 정규화
        $("#passwordHelp").show();
    } else {
        $("#passwordHelp").hide();
    }
});

// 비밀번호 일치 검사
$("#checkPassword").on("keyup", function(event) {
    console.log("pw 일치 검사");

    if ($("#password").val() !== $("#checkPassword").val()) {
        $("#passwordCheckHelp").show();
    } else {
        $("#passwordCheckHelp").hide();
    }
});

// 전화번호 정규식 검사
$("#phone").on("keyup", function(event) {
    console.log("phone keyup 발생")

    const phoneRegExp = /^\d{11}$/;

    if (!phoneRegExp.test($("#phone").val())) {       // 비밀번호 정규화
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
            alert("모든 항목을 작성해주세요.");
            return;
        }

        const userDto = {"nickname": name, "username": email, "mailCode" : mailCode,
            "password" : password, "checkPassword" : checkPassword, "phone" : phone};

        $.ajax({
            type: 'POST',
            url: "/member/signup",
            headers: {
                'content-type': 'application/json'
            },
            data: JSON.stringify(userDto),
            success: function (response) {
                window.location.replace("/login");
            },
            error: function (error) {
                console.log("회원가입 에러 확인 : "+ error);
                // back 모든 항목 검사
                if (error.responseText == "memberInfoNull") {
                    alert("모든 항목을 입력해주세요.");
                }

                if (error.responseText == "duplicateEmail") {
                    alert("이미 존재하는 회원입니다. 다른 이메일을 사용해주세요.");
                    email.focus();
                }

                if (error.responseText == "expirationMailCode") {
                    alert("메일 인증을 재시도 해주세요. 만료된 메일 코드입니다.");
                    mailCode.focus();
                }

                if (error.responseText == "notEqualMailCode") {
                    alert("메일 코드가 일치하지 않습니다.");
                    mailCode.focus();
                }

                if (error.responseText == "notEqualPassword") {
                    alert("비밀번호 확인을 체크해주세요.");
                    checkPassword.focus();
                }

                if (error.responseText == "passwordRegExp") {
                    alert("비밀번호는 영문, 숫자, 특수문자를 포함하여 최소 8자 이상이어야합니다.");
                    password.focus();
                }

                if (error.responseText == "phoneRegExp") {
                    alert("전화번호는 '-'를 제외한 숫자 11자리 입니다. ex) 01012340000");
                    phone.focus();
                }

                $("#signUpBtn").addClass('shake');
                setTimeout(function() {
                    $("#signUpBtn").removeClass('shake'); // 0.8초 후 shake 클래스 제거
                }, 800);
                // console.log(error.responseText);
            }
        });
    });
});

// 메일 인증 smtp
$("#send-mail").on("click", function() {
    const email = $("#email").val();
    console.log("메일 인증 전송 : " + email);
    if (email == "") {
        alert("이메일을 입력하세요");
        console.log("이메일을 입력하세요")
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
            console.log(response)
            alert("이메일로 인증번호가 발송되었습니다.");
            console.log("이메일로 인증번호가 발송되었습니다.")
        },
        error: function(error) {
            console.log(error)
            // 메일 중복검사
            if (error.responseText == "duplicateEmail") {
                alert("이미 존재하는 회원입니다. 다른 이메일을 사용해주세요.");
                email.focus();
            }
        }
    });
});



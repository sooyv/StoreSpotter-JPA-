let form = document.getElementById("signUpForm");
$("#passwordCheckHelp").hide();
$("#passwordHelp").hide();



// 비밀번호 형식 정규화(최소 8자, 영문 숫자 특수문자)
$("#password").on("keyup", function(event) {
    console.log("pw keyup 발생")

    var pwRegExp = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,}$/;

    if (!pwRegExp.test($("#password").val())) {       // 비밀번호 정규화
        $("#passwordHelp").show();
    } else {
        $("#passwordHelp").hide();
    }
});

// password passwordcheck 일치 검사
$("#checkPassword").on("keyup", function(event) {
    console.log("pw 일치 검사");

    if ($("#password").val() !== $("#checkPassword").val()) {
        $("#passwordCheckHelp").show();
    } else {
        $("#passwordCheckHelp").hide();
    }
});

form.addEventListener("submit", event => {
    event.preventDefault();

    $(document).ready(function () {

        const name = $("#name").val();
        const email = $("#email").val();
// const authNum = $("#emailAuthNum").val();
        const password = $("#password").val();
        const checkPassword = $("#checkPassword").val();
        const phone = $("#phone").val();

        // 모든 항목 작성 검사
        if (!name || !email || !password || !checkPassword || !phone) {
            alert("모든 항목을 작성해주세요.");
            return;
        }

        console.log("/member/signup");

        $.ajax({
            type: 'POST',
            url: "/member/signup",
            data: {
                name: name,
                email: email,
                // authNum : authNum,
                password: password,
                checkPassword: checkPassword,
                phone : phone
            },
            success: function (response) {
                console.log(response);

                // window.location.replace("/");
            },
            error: function (error) {
                console.log(error);

                // back 모든 항목 검사
                if (error.responseText == "memberInfo") {
                    alert("모든 항목을 입력해주세요.");
                }

                if (error.responseText == "notEqualPassword") {
                    alert("비밀번호 확인을 체크해주세요.");
                    checkPassword.focus();
                }

                if (error.responseText == "passwordRegExp") {
                    alert("비밀번호는 영문, 숫자, 특수문자를 포함하여 최소 8자 이상이어야합니다.");
                    password.focus();
                }

                // if (error.responseText == "authNum") {
                //     alert("인증번호를 확인해주세요.");
                //     emailAuthInput.focus();
                // } else if (error.responseText == "codeNull") {
                //     alert("세션이 만료되었습니다. 처음부터 다시 시도해주세요.");
                //     emailAuthInput.focus();
                // }

                $("#signUpBtn").addClass('shake');
                setTimeout(function() {
                    $("#signUpBtn").removeClass('shake'); // 0.8초 후 shake 클래스 제거
                }, 800);
                console.log(error.responseText);
            }
        });
    });
});


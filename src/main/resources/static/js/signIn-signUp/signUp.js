let form = document.getElementById("signUpForm");
$("#passwordCheckHelp").hide();
$("#passwordHelp").hide();
// $("#emailHelp").hide();
let idchk = false;
let mailchk = false;

let email = $("#email");
// 이메일 중복 검사
$("#email").on("keyup", function (event) {
    console.log("email 검사")

    const emailRegExp = /^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]+$/;

    // 이메일 정규식 적용
    if (!emailRegExp.test($("#email").val())) {
        idchk = false;
        var emailHelp = document.getElementById("emailHelp");
        emailHelp.innerHTML = "이메일 형식에 맞게 작성해주세요"
        $("#emailHelp").css({
            "color": "#FA3E3E",
        })

    } else {            // 중복체크
        idchk = true;
        $.ajax({
            type : "POST",
            url : "/signup/checkid",
            data : {
                "id" : email.val(),
                "type" : "email"
            },
            success : function(data) {
                if (data === 1) {                // 1이면 이메일 중복
                    console.log(data);
                    mailchk = false;
                    var elements = document.getElementById("emailHelp");
                    elements.innerHTML = "이미 사용중인 이메일입니다"
                    $("#emailHelp").css({
                        "color": "#FA3E3E",
                    })

                } else if (data === 0) {                // 아니면 중복아님
                    console.log(data);
                    mailchk = true;
                    var emailHelp = document.getElementById("emailHelp");
                    emailHelp.innerHTML = "사용 가능한 이메일입니다"
                    $("#emailHelp").css({
                        "color": "green",
                    })
                }
            }
        })
    }
});


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

// 비밀번호 일치 검사
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
                window.location.replace("/");
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

                if (error.responseText == "validateDuplicateMember") {
                    alert("이미 존재하는 회원입니다. 다른 이메일을 사용해주세요.")
                    email.focus();
                }

                $("#signUpBtn").addClass('shake');
                setTimeout(function() {
                    $("#signUpBtn").removeClass('shake'); // 0.8초 후 shake 클래스 제거
                }, 800);
                console.log(error.responseText);
            }
        });
    });
});


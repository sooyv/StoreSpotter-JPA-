let form = document.getElementById("signUpForm");
console.log("회원가입 페이지 접근");
$("#passwordCheckHelp").hide();
$("#passwordHelp").hide();
$("#phoneCheckHelp").hide();
// $("#emailHelp").hide();
// let idchk = false;
// let mailchk = false;
//
// let email = $("#email");
// // 이메일 중복 검사
// // $("#email").on("keyup", function (event) {
// //     console.log("email 검사")
// //
// //     const emailRegExp = /^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]+$/;
// //
// //     // 이메일 정규식 적용
// //     if (!emailRegExp.test($("#email").val())) {
// //         idchk = false;
// //         var emailHelp = document.getElementById("emailHelp");
// //         emailHelp.innerHTML = "이메일 형식에 맞게 작성해주세요"
// //         $("#emailHelp").css({
// //             "color": "#FA3E3E",
// //         })
// //
// //     } else {            // 중복체크
// //         idchk = true;
// //         $.ajax({
// //             type : "POST",
// //             url : "/signup/check-email",
// //             data : {
// //                 "id" : email.val(),
// //                 "type" : "email"
// //             },
// //             success : function(data) {
// //                 if (data === 1) {                // 1이면 이메일 중복
// //                     console.log(data);
// //                     mailchk = false;
// //                     var elements = document.getElementById("emailHelp");
// //                     elements.innerHTML = "이미 사용중인 이메일입니다"
// //                     $("#emailHelp").css({
// //                         "color": "#FA3E3E",
// //                     })
// //
// //                 } else if (data === 0) {                // 아니면 중복아님
// //                     console.log(data);
// //                     mailchk = true;
// //                     var emailHelp = document.getElementById("emailHelp");
// //                     emailHelp.innerHTML = "사용 가능한 이메일입니다"
// //                     $("#emailHelp").css({
// //                         "color": "green",
// //                     })
// //                 }
// //             }
// //         })
// //     }
// // });


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

    const phoneRegExp = /^d{11}/;

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
        const password = $("#password").val();
        const checkPassword = $("#checkPassword").val();
        const phone = $("#phone").val();

        // 모든 항목 작성 검사
        if (!name || !email || !password || !checkPassword) {
            alert("모든 항목을 작성해주세요.");
            return;
        }

        const userDto = {"nickname": name, "username": email,
            "password" : password, "checkPassword" : checkPassword, "phone" : phone};

        $.ajax({
            type: 'POST',
            url: "/signup",
            headers: {
                'content-type': 'application/json'
            },
            data: JSON.stringify(userDto),
            success: function (response) {
                console.log(response);
                window.location.replace("/login");
            },
            error: function (error) {
                console.log(error);

                // back 모든 항목 검사
                if (error.responseText == "memberInfoNull") {
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

                if (error.responseText == "duplicateEmail") {
                    alert("이미 존재하는 회원입니다. 다른 이메일을 사용해주세요.");
                    email.focus();
                }

                if (error.responseText == "phoneRegExp") {
                    alert("전화번호는 '-'를 제외한 숫자 11자리 입니다. ex) 01012340000");
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

    // if(!idchk) {
    //     alert("이메일을 형식에 맞게 입력해주세요");
    //     email.focus();
    //     return;
    // }
    //
    // if(!mailchk) {
    //     alert("이미 존재하는 이메일입니다. 사용하실 수 없습니다.");
    //     email.focus();
    //     return;
    // }

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
        }
    });
});



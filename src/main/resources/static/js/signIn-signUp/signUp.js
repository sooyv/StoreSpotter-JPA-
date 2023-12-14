// let form = document.getElementById("signUpForm");
//
// form.addEventListener("submit", event => {
//     event.preventDefault();
//
//     // const userName = usernameInput.value;
//     // const email = emailInput.value;
//     // const emailAuth = emailAuthInput.value;
//     // const password = passwordInput.value;
//     // const checkPassword = checkPasswordInput.value;
//     // const userPhone = userPhoneInput.value;
//
//     // 모든 항목 작성
//     // if (!userName || !email || !emailAuth || !password || !checkPassword || !userPhone) {
//     //     alert("모든 항목을 작성해주세요.");
//     // }
//
//     $(document).ready(function () {
//         const userName = $("#name").val();
//         const email = $("#email").val();
//         // const authNum = $("#emailAuthNum").val();
//         const password = $("#password").val();
//         const checkPassword = $("#checkPassword").val();
//         const userPhone = $("#phone").val();
//         console.log("/member/signup");
//         $.ajax({
//             type: 'POST',
//             url: "/member/signup",
//             data: {
//                 userName: userName,
//                 email: email,
//                 // authNum : authNum,
//                 password: password,
//                 checkPassword: checkPassword,
//                 userPhone : userPhone
//             },
//             success: function (response) {
//                 console.log(response);
//                 // window.location.href="/login";
//                 console.log(userName, email, password, checkPassword, userPhone)
//                 // alert(`Sign-up successful!\nUsername: ${userName}\nEmail: ${email}\nPhone: ${userPhone}`);
//             },
//             error: function (error) {
//                 if (error.responseText == "password") {
//                     alert("비밀번호 확인을 체크해주세요.");
//                     checkPasswordInput.focus();
//                 }
//
//                 if (error.responseText == "passwordRegExp") {
//                     alert("비밀번호는 영문, 숫자, 특수문자를 포함하여 최소 8자 이상이어야합니다.");
//                     passwordInput.focus();
//                 }
//
//                 if (error.responseText == "authNum") {
//                     alert("인증번호를 확인해주세요.");
//                     emailAuthInput.focus();
//                 } else if (error.responseText == "codeNull") {
//                     alert("세션이 만료되었습니다. 처음부터 다시 시도해주세요.");
//                     emailAuthInput.focus();
//                 }
//
//                 $("#signUpBtn").addClass('shake');
//                 setTimeout(function() {
//                     $("#signUpBtn").removeClass('shake'); // 0.8초 후 shake 클래스 제거
//                 }, 800);
//                 console.log(error.responseText);
//             }
//         });
//     });
// });

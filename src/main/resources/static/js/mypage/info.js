$(document).ready(function (){
    $("#name_btn").on("click", function () {
        updateNickname();
    })
    $("#phone_btn").on("click", function () {
        updatePhone();
    });
    $("#pwd_btn").on("click", function () {
        updatePwd();
    });
    $("#accountClosing").on("click", function () {
        const result = confirm("정말로 계정을 탈퇴하시겠습니까?");
        if (result) {
            withdrawAccount();
        }
    });
})

// ---------- 이름 수정 ----------
function updateNickname() {
        const nickname = document.getElementById('change_name').value;
        $.ajax({
            type: 'POST',
            url: "/mypage/info/modify/nickname",
            data: {
                nickname: nickname
            },
            success: function (response) {
                location.reload();
            }, error: function (error) {
                alert("이름 수정 실패");
            }
        });
}


// ---------- 번호 수정 ----------
function updatePhone() {
const phoneRegExp = /^d{11}/;

if (!phoneRegExp){
    alert("전화번호는 '-'를 제외한 숫자 11자리 입니다. ex) 01012340000");
    return null;
}
var phone = document.getElementById('change_phone').value;
    $.ajax({
        type: 'POST',
        url: "/mypage/info/modify/phone",
        data: {
            phone: phone
        },
        success: function (response) {
            location.reload();
        }, error: function (error){
            if(error.responseText === "phoneRegExp"){
                alert("전화번호는 '-'를 제외한 숫자 11자리 입니다. ex) 01012340000");
            }
        }
    });
}


// ---------- 비밀번호 수정 ----------
function updatePwd() {
    const current_pwd = document.getElementById('current_pwd').value;
    const change_pwd = document.getElementById('change_pwd').value;
    const change_chk_pwd = document.getElementById('change_chk_pwd').value;
    const pwRegExp = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,}$/;

    if (!current_pwd || !change_pwd || !change_chk_pwd) {
        alert("모든 항목을 작성해주세요.");
        return;
    }
    if (change_pwd !== change_chk_pwd) {
        alert("새 비밀번호를 확인해주세요.");
        return;
    }
    if (!pwRegExp.test(change_pwd)) {
        alert("최소 8자, 영문, 숫자, 특수문자를 사용하여 비밀번호를 생성하세요.");
        return;
    }
    const userPwdDto = {"currentPwd" : current_pwd, "changePwd" : change_pwd, "changeChkPwd" : change_chk_pwd};

    $.ajax({
        type: 'POST',
        url: "/mypage/info/modify/password",
        headers: {
            'content-type': 'application/json'
        },
        data: JSON.stringify(userPwdDto),
        success: function (response) {
            alert("비밀번호가 변경되었습니다.")
            location.reload();
        }, error: function (error) {
            if (error.responseText === "incorrect password") {
                alert("비밀번호가 일치하지 않습니다.")
            }
            if (error.responseText === "notEqualPassword") {
                alert("새 비밀번호를 확인해주세요.")
            }
            if (error.responseText === "passwordRegExp") {
                alert("최소 8자, 영문, 숫자, 특수문자를 사용하여 비밀번호를 생성하세요.")
            }
        }
    });
}


// ---------- 계정 탈퇴 ----------
function withdrawAccount() {
    $.ajax({
        type: 'POST',
        url: "/mypage/info/modify/withdraw",
        success: function (response) {
            alert("탈퇴 되었습니다.");
            window.location.replace("/");
        }, error: function (error) {
            if (error.responseText === "withdrawFailed") {
                alert("회원 탈퇴를 실패하였습니다.");
            }
        }
    });
}
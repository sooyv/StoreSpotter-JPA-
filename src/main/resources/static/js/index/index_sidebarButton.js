/**
 * sidebar
 */
const sideBar = document.getElementById("side-bar");
const slideBtn = document.getElementById("slide-btn");
const sideElems = document.querySelectorAll(".side-elem");

// 업종명 받아와서 indust_id 변환하기 Ajax
$(".select-industry-detail").click(function() {
    let indust_id = "";

    if ($(this).text() === "편의점") {
        indust_id = "G20405";
    } else if ($(this).text() === "카페") {
        indust_id = "I21201";
    }

    // AJAX 요청
    $.ajax({
        type: "POST",
        url: "/api/industry",
        // contentType: "application/json;charset=UTF-8",
        data: {
            indust_id: indust_id
        },
        success: function(response) {
            console.log("서버 응답: " + response);
        },
        error: function(error) {
            console.error("에러 발생: " + JSON.stringify(error));
        }
    });
});




// 초기 상태 설정
sideBar.style.width = "378px";
slideBtn.style.transform = "rotate(-180deg)";
sideElems.forEach(function(elem) {
    elem.style.display = "block";
});

document.getElementById("side-bar-slide-btn").addEventListener("click", function() {
    // side-bar의 너비를 0으로 만들어서 왼쪽으로 숨깁니다.
    if (sideBar.style.width === "378px") {
        sideBar.style.width = "0";
        slideBtn.style.transform = "rotate(0deg)"; // 버튼 아이콘을 원래대로 회전시킵니다.
        // side-elem 클래스를 가진 요소도 숨기기
        sideElems.forEach(function(elem) {
            elem.style.display = "none";
        });

    } else {
        // side-bar를 보이게 하고 너비를 378px으로 설정합니다.
        sideBar.style.width = "378px";
        slideBtn.style.transform = "rotate(-180deg)"; // 버튼 아이콘을 180도 회전시켜 화살표 방향을 바꿉니다.
        // side-elem 클래스를 가진 요소 보이기
        sideElems.forEach(function(elem) {
            elem.style.display = "block";
        });
    }
});




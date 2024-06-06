function handleEditLikedClick(likedName) {

    // 모달 표시 로직
    var modal = document.getElementById("inputModal");
    var span = document.getElementsByClassName("close")[0];
    var input = document.getElementById("likedName");

    modal.style.display = "block";
    input.value = likedName;

    span.onclick = function() {
        modal.style.display = "none";
    };

    document.onkeydown = function(event) {
        // event.key 대신 event.keyCode를 사용하는 경우 ESC 키는 27입니다.
        if (event.key === "Escape") { // ESC 키를 눌렀을 때
            modal.style.display = "none";
        }
    };

    // '제출' 버튼 클릭 또는 엔터 키 입력 이벤트
    document.getElementById("submitEdit").onclick = function (){
        submitEditLiked(likedName);
    }

    // '삭제' 버튼 클릭 시 이벤트
    document.getElementById("removeLiked").onclick = function (){
        submitRemoveLiked(likedName);
    }
}

// 수정 버튼 클릭 시
function submitEditLiked(likedName) {
    var editName = document.getElementById("likedName").value;
    EditLiked(likedName, editName); // AJAX 요청
    document.getElementById("inputModal").style.display = "none"; // 모달 닫기
}


// 수정버튼 클릭 시 ajax
function EditLiked(likedName, editName) {
    $.ajax({
            type: "POST",
            url: "/mypage/liked/edit",
            data: {
                likedName: likedName,
                editName: editName
            },
            success: function (response) {
                location.reload();
                },
            error: function (error) {
                if (error.responseText === "DuplicateLikedName"){
                    alert("중복된 이름이 존재합니다")
                }
            }
        });
}

// 삭제 버튼 클릭 시
function submitRemoveLiked(likedName) {
    removeLiked(likedName); // AJAX 요청
    document.getElementById("inputModal").style.display = "none"; // 모달 닫기
}


// 삭제버튼 클릭 시 ajax
function removeLiked(likedName) {
    $.ajax({
        type: "POST",
        url: "/mypage/liked/remove",
        data: {
            likedName: likedName
        },
        success: function (response) {
            alert("항목이 삭제되었습니다")
            location.reload();
        },
        error: function (error) {}
    });
}

$(document).ready(function() {
    // 'edit-liked' 버튼에 대한 클릭 이벤트 리스너를 추가합니다.
    $('body').on('click', '#edit-liked', function() {

        // 클릭된 버튼의 가장 가까운 카드 바디를 찾습니다.
        var cardBody = $(this).closest('.card-body');

        // 카드 바디 내의 'likedName' 클래스를 가진 요소의 텍스트를 가져옵니다.
        var likedName = cardBody.find('.mantine-hm7bzg').first().text(); // 첫 번째 'mantine-hm7bzg' 클래스를 가진 요소를 가정

        handleEditLikedClick(likedName)

    });
});
// 찜 목록 수정하기 끝 //


// 찜 바로가기 //

// 바로가기 버튼 클릭 시
$(document).ready(function(e) {
    // 'shortcut-btn' 버튼에 대한 클릭 이벤트 리스너 추가
    $('body').on('click', '#shortcut-btn', function() {

        // 클릭된 버튼의 가장 가까운 카드 바디를 찾기
        var cardBody = $(this).closest('.card');
        var industry = cardBody.find('.mantine-hm7bzg').eq(1).text(); // 두 번째 mantine-hm7bzg 클래스의 텍스트 (industry)
        var address = cardBody.find('.mantine-hm7bzg').eq(2).text(); // 세 번째 mantine-hm7bzg 클래스의 텍스트 (address)
        var dist = cardBody.find('.mantine-hm7bzg').eq(3).text(); // 세 번째 mantine-hm7bzg 클래스의 텍스트 (address)
        shortCutLiked(industry, address, dist);
    });
});


// 찜 바로가기
function shortCutLiked(industry, address, dist){
    // 로컬 스토리지에 'industry'와 'address' 값을 저장
    localStorage.setItem('industry', industry);
    localStorage.setItem('address', address);
    localStorage.setItem('dist', dist);

    // 메인 페이지로 리디렉션
    window.location.href = "/";
}

// 찜 바로가기 끝 //

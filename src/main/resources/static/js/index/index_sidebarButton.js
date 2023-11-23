/**
 * sidebar
 */
const sideBar = document.getElementById("side-bar");
const slideBtn = document.getElementById("slide-btn");
const sideElems = document.querySelectorAll(".side-elem");
const searchBtn = document.getElementById("search-btn");



let indust = "";

// 업종명 받아오기
$(".select-industry-detail").click(function() {
   indust = $(this).text();
});

$("#submit").click(function() {

    // let indust = $('#select-industry .select-industry-detail.selected').text();
    // let region = $('#address').val();
    let indust = "G20405";
    let region = "11";
    let dist = "200";

    // AJAX 요청
    $.ajax({
        type: "GET",
        url: "/search/recommend",
        data: {
            indust: indust,
            region: region,
            dist: dist
        },
        success: function(response) {
            console.log("서버 응답: " + "success");
            var coordinates = response.map(function(item) {

                var coordinatesString = item.center_coor.match(/\(([^)]+)\)/)[1];
                var coordinatesArray = coordinatesString.split(' ');

                return {
                    x: parseFloat(coordinatesArray[0]),  // 경도
                    y: parseFloat(coordinatesArray[1])   // 위도
                };
            });
            console.log(coordinates)


            function drawCirclesOnMap(coordinates) {
                for (var i = 0; i < coordinates.length; i++) {
                    var circle = new naver.maps.Circle({
                        map: map,
                        center: new naver.maps.LatLng(coordinates[i].y, coordinates[i].x),
                        radius: 100,
                        fillColor: 'crimson',
                        fillOpacity: 0.8
                    });
                }

            }

            // 여기서 coordinates를 이용하여 지도에 원을 그리는 로직을 추가할 수 있습니다.
            drawCirclesOnMap(coordinates);



        },
        error: function(error) {
            console.error("에러 발생: " + JSON.stringify(error));
        }
    });
});

$(document).ready(function() {
    // 업종 선택을 위한 이벤트 리스너 추가
    $('#select-industry .select-industry-detail').click(function() {
        // 선택된 업종 초기화
        $('#select-industry .select-industry-detail').removeClass('selected');

        // 선택한 업종에 노란색 배경 적용
        $(this).addClass('selected');

        // 선택한 업종의 텍스트 색상을 흰색으로 변경
        $(this).css('color', 'white');

        // 나머지 업종의 텍스트 색상 초기화
        $('#select-industry .select-industry-detail:not(.selected)').css('color', '');
    });

    // 검색하기 버튼 클릭 시 동작할 함수
    $('#submit').click(function() {
        // 선택된 업종 확인
        var selectedIndustry = $('#select-industry .select-industry-detail.selected').text();
        let region = $('#address').val();


        // 선택된 업종이 없을 경우 알림 또는 다른 동작 수행
        if (!selectedIndustry) {
            alert('업종을 선택해주세요.');
            return;
        }

        if (!region) {
            alert('주소를 선택해주세요.');
            return;
        }

        // 여기에 선택된 업종에 따른 추가적인 동작을 수행할 수 있습니다.
        // console.log('선택된 업종:', selectedIndustry);
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

// const distSelect = document.getElementById('dist-select');
// const distValue = document.getElementById('dist-value');
//
// distSelect.addEventListener('input', function() {
//     distValue.value = this.value;
// });




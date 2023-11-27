/**
 * sidebar
 */
const sideBar = document.getElementById("side-bar");
const slideBtn = document.getElementById("slide-btn");
const sideElems = document.querySelectorAll(".side-elem");

// const searchBtn = document.getElementById("search-btn");


let indust = ""
const addbox = document.getElementById('address');
const originalBorderStyle = addbox.style.border;

function handleInputValueChange() {
    // 입력 상자의 값이 변경되면 원래 상태로 되돌림
    addbox.style.border = originalBorderStyle;
}
addbox.addEventListener('input', handleInputValueChange);


// 로딩중
function loading(){
    const modalOverlay = document.getElementById('modal-overlay');
    const modal = document.getElementById('modal');

    modalOverlay.style.display = 'block';
    modal.style.display = 'block';

    // 모달에서 이벤트 전파 방지
    modal.addEventListener('click', function(event) {
        event.stopPropagation();
    });

    setTimeout(function() {
        modalOverlay.style.display = 'none';
        modal.style.display = 'none';
    }, 2000);
}

// 지도에 원 그리기


$("#submit").click(function() {

    let indust = $('#select-industry .select-industry-detail.selected').text();
    let region = $('#address').val();
    let dist = $('#dist-value').text();
    const addbox = document.getElementById('address');

    // 선택된 업종이 없을 경우 알림 또는 다른 동작 수행
    if (!indust) {
        return alert('업종을 선택해주세요.');
    }
    if (!region) {
        return alert('주소를 선택해주세요.');
    }
    if (getComputedStyle(addbox).border !== "2px solid rgb(65, 99, 125)") {
        return alert("주소 검색을 해주세요.")
    }

    else{
        loading()
        // 지도 초기화
        var map = new naver.maps.Map('map');


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

                // 지도 초기화에 움직이는 지도 좌표 검색된 좌표로 재설정
                naver.maps.Service.geocode({
                    query: region
                }, function (status, response) {
                    item = response.v2.addresses[0];
                    point = new naver.maps.Point(item.x, item.y);

                    map.setCenter(point); // 중심 좌표 이동
                    map.setZoom(15);     // 줌 레벨 변경
                })

                var coordinates = response.map(function(item) {

                    var coordinatesString = item.center_coor.match(/\(([^)]+)\)/)[1];
                    var coordinatesArray = coordinatesString.split(' ');

                    return {
                        x: parseFloat(coordinatesArray[0]),  // 경도
                        y: parseFloat(coordinatesArray[1])   // 위도
                    };
                });

                //원 그리기
                function drawCirclesOnMap(coordinates) {
                    for (var i = 0; i < coordinates.length; i++) {
                        var circle = new naver.maps.Circle({
                            map: map,
                            center: new naver.maps.LatLng(coordinates[i].y, coordinates[i].x),
                            radius: dist / 2,
                            fillColor: 'crimson',
                            fillOpacity: 0.8
                        });
                    }
                }

                drawCirclesOnMap(coordinates);



            },
            error: function(error) {
                console.error("에러 발생: " + JSON.stringify(error));
            }
        });
    }


});

$(document).ready(function() {
    // 업종 선택을 위한 이벤트 리스너 추가
    $('#select-industry .select-industry-detail').click(function () {
        // 선택된 업종 초기화
        $('#select-industry .select-industry-detail').removeClass('selected');

        // 선택한 업종에 노란색 배경 적용
        $(this).addClass('selected');

        // 선택한 업종의 텍스트 색상을 흰색으로 변경
        $(this).css('color', 'white');

        // 나머지 업종의 텍스트 색상 초기화
        $('#select-industry .select-industry-detail:not(.selected)').css('color', '');
    });


// 초기 상태 설정
    sideBar.style.width = "378px";
    slideBtn.style.transform = "rotate(-180deg)";
    sideElems.forEach(function (elem) {
        elem.style.display = "block";
    });

    document.getElementById("side-bar-slide-btn").addEventListener("click", function () {
        // side-bar의 너비를 0으로 만들어서 왼쪽으로 숨깁니다.
        if (sideBar.style.width === "378px") {
            sideBar.style.width = "0";
            slideBtn.style.transform = "rotate(0deg)"; // 버튼 아이콘을 원래대로 회전시킵니다.
            // side-elem 클래스를 가진 요소도 숨기기
            sideElems.forEach(function (elem) {
                elem.style.display = "none";
            });

        } else {
            // side-bar를 보이게 하고 너비를 378px으로 설정합니다.
            sideBar.style.width = "378px";
            slideBtn.style.transform = "rotate(-180deg)"; // 버튼 아이콘을 180도 회전시켜 화살표 방향을 바꿉니다.
            // side-elem 클래스를 가진 요소 보이기
            sideElems.forEach(function (elem) {
                elem.style.display = "block";
            });
        }
    });

    const distSlider = document.getElementById('dist-slider');
    const distValue = document.getElementById('dist-value');

    distSlider.addEventListener('input', function () {
        distValue.value = this.value;
    });
})




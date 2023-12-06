/**
 * sidebar
 */
const sideBar = document.getElementById("side-bar");
const slideBtn = document.getElementById("slide-btn");
const sideElems = document.querySelectorAll(".side-elem");

// distExplain.hide();

let indust = ""
const addbox = document.getElementById('address');
const originalBorderStyle = addbox.style.border;

function handleInputValueChange() {
    // 입력 상자의 값이 변경되면 원래 상태로 되돌림
    addbox.style.border = originalBorderStyle;
}
addbox.addEventListener('input', handleInputValueChange);


// 로딩중
function loading() {
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
        return alert("주소 검색을 해주세요.");
    }

    else {
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
                    console.log(response)
                    item = response.v2.addresses[0];
                    point = new naver.maps.Point(item.x, item.y);

                    map.setCenter(point); // 중심 좌표 이동
                    map.setZoom(15);     // 줌 레벨 변경
                })

                var coordinates = response.map(function(item) {
                    var stCoorString = item.st_coor.match(/\(([^)]+)\)/)[1];
                    var stCoorArray = stCoorString.split(' ');

                    var comCoorString = item.com_coor.match(/\(([^)]+)\)/)[1];
                    var comCoorArray = comCoorString.split(' ');

                    var centerCoorString = item.center_coor.match(/\(([^)]+)\)/)[1];
                    var centerCoorArray = centerCoorString.split(' ');

                    return {
                        st_x : parseFloat(stCoorArray[0]), // 기준경도
                        st_y : parseFloat(stCoorArray[1]), // 기준위도
                        com_x : parseFloat(comCoorArray[0]), // 대상경도
                        com_y : parseFloat(comCoorArray[1]), // 대상위도
                        center_x: parseFloat(centerCoorArray[0]),  // 중점경도
                        center_y: parseFloat(centerCoorArray[1])   // 중점위도
                     };
                });

                // 두 원이 겹치는지 확인하는 함수
                function getDist(lat1,lng1,lat2,lng2) {
                    function deg2rad(deg) { return deg * (Math.PI/180) }
                    var R = 6371;
                    var dLat = deg2rad(lat2-lat1);
                    var dLon = deg2rad(lng2-lng1);
                    var a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *   Math.sin(dLon/2) * Math.sin(dLon/2);
                    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
                    return R * c;
                }

                let circles=[]
                //원 그리기
                function drawCirclesOnMap(coordinates) {
                    for (let i = 0; i < coordinates.length; i++) {
                        let circle = new naver.maps.Circle({
                            map: map,
                            center: new naver.maps.LatLng(coordinates[i].center_y, coordinates[i].center_x),
                            radius: dist / 2,
                            fillColor: '#6775E0',
                            fillOpacity: 0.8,
                            clickable: true,
                            stroke: null,
                        });


                    // 원 클릭 시 Event
                    naver.maps.Event.addListener(circle, 'click', function(e) {
                        var latlng = e.center
                        naver.maps.Service.reverseGeocode({
                            coords: circle.center,
                            orders: [
                                naver.maps.Service.OrderType.ADDR,
                                naver.maps.Service.OrderType.ROAD_ADDR
                            ].join(',')
                        }, function(status, response) {
                            if (status !== naver.maps.Service.Status.OK) {
                                // 재접속 시도
                                console.log("에러났다")
                            }
                            var items = response.v2.results
                                , address = ''
                                , htmlAddresses = [];

                            for (var i = 0, ii = items.length, item, addrType; i < ii; i++) {
                                item = items[i];
                                address = makeAddress(item) || '';
                                addrType = item.name === 'roadaddr' ? '[도로명 주소]' : '[지번 주소]';

                                htmlAddresses.push((i + 1) + '. ' + addrType + ' ' + address);
                            }
                            let contentString = [
                                '<div class="iw_inner">',
                                '<div style="margin-top: 5px; margin-right: 10px; text-align: right; ">', '❤️', '</div>',
                                '<h2>주소</h2>',
                                htmlAddresses.join('<br />'),
                                '<div>', '</div>',
                                '<button id="near-button" type="button">근처 상권보기</button>',
                                '</div>'
                            ].join('');

                            let infoWindow = new naver.maps.InfoWindow({
                                anchorSkew: true,
                                content: contentString,
                                position: circle.center,
                                anchorSize: new naver.maps.Size(10, 20),
                                // maxWidth: 250,
                                // height: 100,
                                // backgroundColor: "white",
                                // borderColor: "black",
                                // borderWidth: 2,
                                borderWidth: 0,
                                disableAnchor: true,
                                textAlign: "center",
                                marginBottom: 20,

                            });
                            if (infoWindow.getMap()) {
                                infowindow.close();
                            } else {
                                infoWindow.open(circle.map, latlng);
                            }
                            // 근처 상가 좌표 찍어주기
                            // var nearCircle1 = new naver.maps.Marker({
                            //     map : map,
                            //     position : new naver.maps.LatLng(coordinates[i].st_y, coordinates[i].st_x)
                            // })
                            // var nearCircle2 = new naver.maps.Marker({
                            //     map : map,
                            //     position : new naver.maps.LatLng(coordinates[i].com_y, coordinates[i].com_x)
                            // })
                        });

                        })





                    // 중복 확인
                    if (circles.length > 0){
                        for (var j = 0; j < circles.length; j++) {
                            let distance = getDist(circle.center.y, circle.center.x, circles[j].center.y, circles[j].center.x)
                            if (distance < 0.005) {
                                circle.setMap(null);
                            }
                        }
                    }
                    circles.push(circle);
                    }
                }

                drawCirclesOnMap(coordinates);
                circles=[]

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

// 거리 선택 설명 물음표 버튼
document.addEventListener("DOMContentLoaded", function() {
    var distInfo = document.getElementById("dist-info");
    var distExplain = document.getElementById("dist-explain");

    // 초기 상태 설정
    var isOpen = false;

    // 토글 함수 정의
    function toggleDistExplain() {
        isOpen = !isOpen; // 상태 반전

        // 상태에 따라 dist-explain 표시 또는 숨김
        if (isOpen) {
            distExplain.style.display = "flex";
        } else {
            distExplain.style.display = "none";
        }
    }

    // 클릭 이벤트에 토글 함수 연결
    distInfo.addEventListener("click", toggleDistExplain);
});







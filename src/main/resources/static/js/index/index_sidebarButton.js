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
    modal.addEventListener('click', function (event) {
        event.stopPropagation();
    });

    setTimeout(function () {
        modalOverlay.style.display = 'none';
        modal.style.display = 'none';
    }, 2000);
}


let markers = [];
// 지도에 원 그리기
$("#submit").click(function () {
    let indust = $('#select-industry .select-industry-detail.selected').text();
    console.log("indust 확인 : " +  indust);
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
    } else {
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

                success: function (response) {
                    console.log("서버 응답: " + "success");
                    // 지도 초기화에 움직이는 지도 좌표 검색된 좌표로 재설정
                    naver.maps.Service.geocode({
                        query: region
                    }, function (status, response) {
                        item = response.v2.addresses[0];
                        console.log(item)
                        point = new naver.maps.Point(item.x, item.y);

                        map.setCenter(point); // 중심 좌표 이동
                        map.setZoom(15);     // 줌 레벨 변경
                    })
                var coordinates = response.map(function (item) {

                    var stNm = item.stNm;
                    var comNm = item.comNm;

                    var stCoorString = item.stCoor.match(/\(([^)]+)\)/)[1];
                    var stCoorArray = stCoorString.split(' ');

                    var comCoorString = item.comCoor.match(/\(([^)]+)\)/)[1];
                    var comCoorArray = comCoorString.split(' ');

                    var centerCoorString = item.centerCoor.match(/\(([^)]+)\)/)[1];
                    var centerCoorArray = centerCoorString.split(' ');

                    console.log(Object.entries(item))


                    console.log("st_nm"+ stNm);
                    console.log("st_x" + parseFloat(stCoorArray[0])) // 기준경도
                    console.log("st_y" + parseFloat(stCoorArray[1])) // 기준위도
                    console.log("com_nm" + comNm)
                    console.log("com_x" + parseFloat(comCoorArray[0])) // 대상경도
                    console.log("com_y" + parseFloat(comCoorArray[1])) // 대상위도
                    console.log("center_x" + parseFloat(centerCoorArray[0]))  // 중점경도
                    console.log("center_y" + parseFloat(centerCoorArray[1]))   // 중점위도


                    return {
                        st_nm: stNm,
                        st_x: parseFloat(stCoorArray[1]), // 기준경도
                        st_y: parseFloat(stCoorArray[0]), // 기준위도
                        com_nm: comNm,
                        com_x: parseFloat(comCoorArray[1]), // 대상경도
                        com_y: parseFloat(comCoorArray[0]), // 대상위도
                        center_x: parseFloat(centerCoorArray[1]),  // 중점경도
                        center_y: parseFloat(centerCoorArray[0])   // 중점위도
                    };
                });

                // 두 원이 겹치는지 확인하는 함수
                function getDist(lat1, lng1, lat2, lng2) {
                    function deg2rad(deg) {
                        return deg * (Math.PI / 180)
                    }

                    var R = 6371;
                    var dLat = deg2rad(lat2 - lat1);
                    var dLon = deg2rad(lng2 - lng1);
                    var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
                    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                    return R * c;
                }

                let circles = [];

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
                        console.log("서클의 센터" + circle.center)

                        // 원 클릭 시 Event
                        naver.maps.Event.addListener(circle, 'click', function (e) {
                            var latlng = e.center
                            naver.maps.Service.reverseGeocode({
                                coords: circle.center,
                                orders: [
                                    naver.maps.Service.OrderType.ADDR,
                                    naver.maps.Service.OrderType.ROAD_ADDR
                                ].join(',')
                            }, function (status, response) {
                                if (status !== naver.maps.Service.Status.OK) {
                                    // 재접속 시도
                                    console.log("에러났다")
                                }
                                var items = response.v2.results
                                    , address = ''
                                    , htmlAddresses = [];

                                for (let i = 0, ii = items.length, item, addrType; i < ii; i++) {
                                    item = items[i];
                                    address = makeAddress(item) || '';
                                    addrType = item.name === 'roadaddr' ? '[도로명 주소]' : '[지번 주소]';
                                    if (i === 0) {
                                        htmlAddresses.push(addrType + ' ' + address + ' ' + '<span class="material-symbols-outlined" id="areacopy1" style="cursor: pointer">content_copy</span>');
                                    } else {
                                        htmlAddresses.push(addrType + ' ' + address + ' ' + '<span class="material-symbols-outlined" id="areacopy2" style="cursor: pointer">content_copy</span>');
                                    }
                                }

                                // 전체 html
                                let contentString = [
                                    '<div class="iw_inner" style="border-radius: 15px">',
                                    '<div class = "mantine-Text-root mantine-dx3wmj" style="display: flex; justify-content: space-between; margin-bottom: 10px;">',
                                    '<span class = "mantine-Text-root mantine-dx3wmj" style="display: flex; align-items: center; font-size: 23px;">주소</span>',
                                    '<button id="add-liked" class="add-liked" style="cursor: pointer; user-select: none;">저장하기</button>',
                                    '</div>',
                                    '<div>', htmlAddresses.join('<br />'), '</div>',
                                    '<button id="near-button" type="button">근처 상권보기</button>',
                                    '</div>'
                                ].join('');

                                let infoWindow = new naver.maps.InfoWindow({
                                    anchorSkew: true,
                                    content: contentString,         // 여기 넣음
                                    position: circle.center,        // 좌표
                                    textAlign: "center",
                                    borderWidth: 1
                                });

                                // map 클릭 시 이전 선택 마커와 infoWindow 지우기
                                map.addListener('click', function (e) {
                                    infoWindow.close();
                                });

                                let nearCircle1 = new naver.maps.Marker();
                                let nearCircle2 = new naver.maps.Marker();

                                for (let marker of markers) {
                                    marker.setMap(null);
                                }

                                infoWindow.close();

                                infoWindow.open(circle.map, latlng);
                                // 근처 상가 좌표 찍어주기
                                let nearButton = document.getElementById("near-button")
                                nearButton.onclick = () => {
                                    nearCircle1.setMap(map)
                                    nearCircle1.setPosition(new naver.maps.LatLng(coordinates[i].st_y, coordinates[i].st_x))
                                    markers.push(nearCircle1);
                                    nearCircle2.setMap(map)
                                    nearCircle2.setPosition(new naver.maps.LatLng(coordinates[i].com_y, coordinates[i].com_x))
                                    markers.push(nearCircle2);

                                    console.log(coordinates[i].st_nm, coordinates[i].com_nm)
                                }

                                ////// 찜버튼 기능 ///////

                                let likedAddress = ""
                                // 주소 추출 및 처리를 위한 함수
                                function extractLikedAddress() {
                                    let likedAddress1 = "", likedAddress2 = "";

                                    try {likedAddress1 = extractTextFromHTML(htmlAddresses[0].replace(/\[지번 주소\]/g, '').trim()).replace('content_copy', '');
                                    } catch (error) {likedAddress1 = "";}

                                    try {likedAddress2 = extractTextFromHTML(htmlAddresses[1].replace(/\[도로명 주소\]/g, '').trim()).replace('content_copy', '');
                                    } catch (error) {likedAddress2 = "";}

                                    return likedAddress1 || likedAddress2; // 첫 번째 주소가 유효하면 반환, 그렇지 않으면 두 번째 주소 반환
                                }

                                // 'add-liked' 버튼 클릭 이벤트를 처리하는 함수
                                function handleAddLikedClick() {
                                    likedAddress = extractLikedAddress(); // 주소 추출

                                    // 모달 표시 로직
                                    var modal = document.getElementById("inputModal");
                                    var span = document.getElementsByClassName("close")[0];

                                    modal.style.display = "block";

                                    span.onclick = function() {
                                        modal.style.display = "none";
                                    };

                                    window.onclick = function(event) {
                                        if (event.target == modal) {
                                            modal.style.display = "none";
                                        }
                                    };

                                    // '제출' 버튼 클릭 또는 엔터 키 입력 이벤트
                                    document.getElementById("submitLiked").onclick = submitLikedName;
                                    document.getElementById("likedName").addEventListener("keypress", function(event) {
                                        if (event.keyCode === 13) {
                                            submitLikedName();
                                            event.preventDefault(); // 폼 제출에 의한 페이지 새로고침 방지
                                        }
                                    });

                                }

                                function submitLikedName() {
                                    let likedName = document.getElementById("likedName").value;
                                    console.log("제출된 찜 이름: ", likedName);
                                    StoreLiked(likedName); // AJAX 요청
                                    document.getElementById("inputModal").style.display = "none"; // 모달 닫기
                                }

                                // 찜버튼 클릭시 AJAX 요청을 처리하는 함수
                                let ajax_chk_flg = false;

                                function StoreLiked(likedName) {
                                    if (!ajax_chk_flg) {
                                        ajax_chk_flg = true;
                                        $.ajax({
                                            type: "POST",
                                            url: "mypage/liked/add",
                                            data: {
                                                indust: indust,
                                                likedAddress: likedAddress,
                                                dist: parseFloat(dist),
                                                likedName: likedName
                                            },
                                            success: function (response) {
                                                console.log("찜 저장 성공");
                                            },
                                            error: function (error) {
                                                if (error.responseText === "DuplicateLikedName"){
                                                    alert("중복된 이름이 존재합니다")
                                                }
                                            }
                                        });
                                    }
                                }

                                // 'add-liked' 버튼에 이벤트 리스너 추가
                                const addLiked = document.getElementById('add-liked');
                                addLiked.addEventListener('click', handleAddLikedClick);

                                /////// 찜버튼 기능 끝 ///////


                                // 클립보드 주소 복사
                                function extractTextFromHTML(html) {
                                    // 임시 요소를 생성하여 HTML을 파싱합니다.
                                    var tempElement = document.createElement("div");
                                    tempElement.innerHTML = html;

                                    // 텍스트를 추출하고 공백을 제거한 후 반환합니다.
                                    return tempElement.innerText.trim();
                                }

                                const areacopy1 = document.getElementById("areacopy1");
                                const areacopy2 = document.getElementById("areacopy2");
                                // button 클릭 이벤트
                                areacopy1.onclick = () => {
                                    var add = extractTextFromHTML(htmlAddresses[0].replace(/\[지번 주소\]/g, '').trim()).replace('content_copy', '');
                                    console.log(add)
                                    // 주소를 복사
                                    navigator.clipboard.writeText(add).then(
                                        () => {
                                            // 클립보드에 write이 성공했을 때 불리는 핸들러
                                            var copyNotification = document.createElement('div');
                                            copyNotification.className = 'copy-notification';
                                            copyNotification.textContent = '복사되었습니다';
                                            document.body.appendChild(copyNotification);

                                            // 1.5초 후에 메시지를 제거
                                            setTimeout(function () {
                                                document.body.removeChild(copyNotification);
                                            }, 1500);
                                        });
                                };
                                if (areacopy2) {
                                    areacopy2.onclick = () => {
                                        var add = extractTextFromHTML(htmlAddresses[1].replace(/\[도로명 주소\]/g, '').trim()).replace('content_copy', '');
                                        // 주소를 복사
                                        navigator.clipboard.writeText(add).then(
                                            () => {
                                                // 클립보드에 write이 성공했을 때 불리는 핸들러
                                                var copyNotification = document.createElement('div');
                                                copyNotification.className = 'copy-notification';
                                                copyNotification.textContent = '복사되었습니다';
                                                document.body.appendChild(copyNotification);

                                                // 1.5초 후에 메시지를 제거
                                                setTimeout(function () {
                                                    document.body.removeChild(copyNotification);
                                                }, 1500);
                                            });
                                    };
                                }

                            });
                        })


                        // 중복 확인
                        if (circles.length > 0) {
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
                circles = []

            },
            error: function (error) {
                console.error("에러 발생: " + JSON.stringify(error));
            }
        });
    }
});


$(document).ready(function () {

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
document.addEventListener("DOMContentLoaded", function () {
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

// 저장목록 바로가기 이벤트
// $(document).ready(function() {
//     // 로컬 스토리지에서 'industry'와 'address' 값을 읽기
//     var industry = localStorage.getItem('industry');
//     var address = localStorage.getItem('address');
//     var dist = localStorage.getItem("dist")
//
//     if (dist && address && industry){
//         // 읽어온 값을 사용하여 필요한 작업 수행
//         console.log(industry, address, dist);
//
//         // main 페이지에서 industry(편의점 or 카페) industry 변수에 따라 클릭
//         var industryDetails = document.querySelectorAll('#select-industry .select-industry-detail');
//         // 찾은 div들을 순회하면서
//         industryDetails.forEach(function(detail) {
//             // 해당 div의 텍스트가 'industry' 변수의 값과 일치하는지 확인
//             if (detail.textContent === industry) {
//                 // 일치한다면 해당 div를 클릭합니다.
//                 detail.click();
//             }
//         });
//
//         // 주소 값에 주소 입력
//         document.getElementById('address').value = address;
//         // 'address-search' 버튼을 찾아 클릭
//         document.getElementById('address-search').click();
//
//         // dist 선택
//         document.getElementById('dist-value').value = 50;
//
//         // 제출버튼 클릭
//         document.getElementById('submit').click();
//
//         // 사용 후 로컬 스토리지에서 해당 항목 삭제
//         localStorage.removeItem('industry');
//         localStorage.removeItem('address');
//         localStorage.removeItem('dist');
//     }
// });



function clickIndustry(industry) {
    return new Promise((resolve) => {
        var industryDetails = document.querySelectorAll('#select-industry .select-industry-detail');
        industryDetails.forEach(function(detail) {
            if (detail.textContent === industry) {
                detail.click();
            }
        });
        resolve(); // 비동기 작업이 아니므로 바로 resolve
    });
}

function enterAddress(address) {
    return new Promise((resolve) => {
        document.getElementById('address').value = address;
        resolve(); // 비동기 작업이 아니므로 바로 resolve
    });
}

function clickAddressSearch() {
    return new Promise((resolve, reject) => {
        // 'address-search' 버튼 클릭 처리
        document.getElementById('address-search').click();

        // 클릭 후 비동기 작업이 완료될 때까지 기다리는 로직 필요
        // 예: AJAX 요청 완료, 특정 요소의 로딩 완료 등
        // 이 예시에서는 setTimeout을 사용해 모의합니다
        setTimeout(() => {
            resolve(); // 비동기 작업 완료 시 resolve
        }, 1000); // 실제 코드에서는 적절한 비동기 완료 조건을 사용해야 합니다.
    });
}

function selectDist(dist) {
    return new Promise((resolve) => {
        document.getElementById('dist-value').value = parseInt(dist);
        resolve(); // 비동기 작업이 아니므로 바로 resolve
    });
}

function clickSubmit() {
    return new Promise((resolve) => {
        document.getElementById('submit').click();
        resolve(); // 비동기 작업이 아니므로 바로 resolve
    });
}

$(document).ready(function() {
    // 로컬 스토리지에서 'industry'와 'address' 값을 읽기
    var industry = localStorage.getItem('industry');
    var address = localStorage.getItem('address');
    var dist = localStorage.getItem("dist")
    if (dist && address && industry) {
        // industry 클릭
        clickIndustry(industry);

        // 주소 값에 주소 입력 후 일정 시간 대기
        setTimeout(function() {
            enterAddress(address);

            // 'address-search' 버튼 클릭 후 일정 시간 대기
            setTimeout(function() {
                clickAddressSearch();

                // dist 선택 후 일정 시간 대기
                setTimeout(function() {
                    selectDist(dist);

                    // 제출버튼 클릭
                    setTimeout(function() {
                        clickSubmit();
                    }, 1000); // dist 선택 후 제출버튼 클릭까지 대기 시간
                }, 1000); // 'address-search' 클릭 후 dist 선택까지 대기 시간
            }, 1000); // 주소 입력 후 'address-search' 클릭까지 대기 시간
        }, 1000); // industry 클릭 후 주소 입력까지 대기 시간
    }

    // 사용 후 로컬 스토리지에서 해당 항목 삭제
    localStorage.removeItem('industry');
    localStorage.removeItem('address');
    localStorage.removeItem('dist');
});

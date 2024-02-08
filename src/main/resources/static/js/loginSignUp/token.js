const token = searchParam('token')

if (token) {
    localStorage.setItem("access_token", token);
    console.log("토큰 확인 콘솔" + localStorage.getItem("access_token"), token)
}

function searchParam(key) {
    return new URLSearchParams(location.search).get(key);
}

// HTTP 요청을 보내고 엑세스 토큰을 헤더에서 추출하여 변수에 저장
// fetch('/member/login', {
//     method: 'POST',  // 예시로 POST 메서드를 사용
//     // 기타 필요한 설정들을 추가 (헤더, 바디 등)
// })
//     .then(response => {
//         // 응답 헤더에서 Authorization 헤더 값을 추출
//         const accessTokenHeader = response.headers.get('Authorization');
//
//         // 헤더 값이 있다면 엑세스 토큰 추출
//         if (accessTokenHeader) {
//             const accessToken = accessTokenHeader.split('Bearer ')[1];
//
//             // 추출한 엑세스 토큰을 변수에 저장
//             const privateAccessToken = accessToken;
//
//             // 저장된 토큰을 사용하여 다른 작업 수행 가능
//             console.log('엑세스 토큰:', privateAccessToken);
//         }
//
//         // 나머지 응답 처리
//         return response.json();
//     })
//     .then(data => {
//         // 응답 데이터에 대한 추가 작업
//         console.log('응답 데이터:', data);
//     })
//     .catch(error => {
//         // 오류 처리
//         console.error('에러 발생:', error);
//     });

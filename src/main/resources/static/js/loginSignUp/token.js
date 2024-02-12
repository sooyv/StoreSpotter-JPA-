// const token = searchParam('token')
//
// if (token) {
//     localStorage.setItem("access_token", token);
//     console.log("토큰 확인 콘솔" + localStorage.getItem("access_token"), token)
// }
//
// function searchParam(key) {
//     return new URLSearchParams(location.search).get(key);
// }

// 엑세스 토큰을 로컬 스토리지에서 가져오기
const accessToken = localStorage.getItem('access_token');
console.log(accessToken)

// HTTP 요청 헤더에 엑세스 토큰을 추가
const headers = {
    'Authorization': `Bearer ${accessToken}`
};

// HTTP GET 요청
fetch('/api/token', {
    method: 'GET',
    headers: headers
})
    .then(response => {
        // 응답을 처리합니다.
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
    })
    .catch(error => {
        console.error('There was a problem with your fetch operation:', error);
    })




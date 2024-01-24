const token = searchParam('token')

if (token) {
    localStorage.setItem("access_token", token);
    console.log("토큰 확인 콘솔" + localStorage.getItem("access_token"), token)
}

function searchParam(key) {
    return new URLSearchParams(location.search).get(key);
}
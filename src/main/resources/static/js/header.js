document.getElementById("menu-bar-btn").addEventListener("click", function () {
    let header_content = document.getElementById("header-content");
    console.log("Menu-bar-btn click")
    header_content.classList.toggle("active");
});


const token = localStorage.getItem('accessToken');
console.log(token)
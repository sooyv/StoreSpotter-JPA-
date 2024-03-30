$("#indust-reset").click(function() {
    if (!confirm("모든 상가정보가 최신화 됩니다. 계속 하시겠습니까?")) {
        alert("아니오를 누르셨습니다.");
    } else {
        $.ajax({
            type: "POST",
            url: "/admin/apiDataSave",
            success: function (response) {
                console.log("서버 응답: " + "success");
            }, error: function (error) {
                if (error.responseText === "API DATA NOT FOUND"){
                    alert("데이터 초기화를 실패했습니다.")
                }
            }
        })
    }
})

$("#datapair-reset").click(function() {
    if (!confirm("모든 DataPair 정보가 최신화 됩니다. 계속 하시겠습니까?")) {
        alert("아니오를 누르셨습니다.");
    } else {
        $.ajax({
            type: "POST",
            url: "/admin/DataPair",
            success: function (response) {
                console.log("서버 응답: " + "success");
            }, error: function (error) {
                console.error("에러 발생: " + JSON.stringify(error));
            }
        })
    }
})

// Wait for the DOM to be ready
document.addEventListener("DOMContentLoaded", function () {
// Get the tabs and content elements
    const tabs = document.querySelectorAll(".nav-link");
    const tabContents = document.querySelectorAll(".tab-pane");

    // Add click event listeners to each tab
    tabs.forEach(function (tab, index) {
        tab.addEventListener("click", function (event) {
            // Prevent the default link behavior
            event.preventDefault();

            // Remove the 'show' class from all tab contents
            tabContents.forEach(function (content) {
                content.classList.remove("show", "active");
            });

            // Add the 'show' class to the clicked tab content
            tabContents[index].classList.add("show", "active");

            // Remove the 'active' class from all tabs
            tabs.forEach(function (t) {
                t.classList.remove("active");
            });

            tab.classList.add("active");
        });
    });
});




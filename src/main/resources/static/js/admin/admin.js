$("#indust-reset").click(function() {
    if (!confirm("모든 상가정보가 최신화 됩니다. 계속 하시겠습니까?")) {
        alert("아니오를 누르셨습니다.");
    } else {
        $.ajax({
            type: "POST",
            url: "/admin/apiDataSave",
            success: function (response) {
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
            url: "/admin/dataPair",
            success: function (response) {
            }, error: function (error) {
                if (error.responseText === "DATA-PAIR-CREATE-FAILED"){
                    alert("페어 데이터 저장에 실패했습니다. ")
                }
            }
        })
    }
})

document.addEventListener("DOMContentLoaded", function () {
    const tabs = document.querySelectorAll(".nav-link");
    const tabContents = document.querySelectorAll(".tab-pane");

    tabs.forEach(function (tab, index) {
        tab.addEventListener("click", function (event) {
            event.preventDefault();

            tabContents.forEach(function (content) {
                content.classList.remove("show", "active");
            });

            tabContents[index].classList.add("show", "active");

            tabs.forEach(function (t) {
                t.classList.remove("active");
            });

            tab.classList.add("active");
        });
    });
});




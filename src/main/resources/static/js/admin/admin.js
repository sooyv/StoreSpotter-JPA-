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

$("#conv-indust-reset").click(function() {
    if (!confirm("상가정보가 최신화 됩니다. 계속 하시겠습니까?")) {
        alert("아니오를 누르셨습니다.");
    } else {
        $.ajax({
            type: "POST",
            url: "/admin/conv-api",
            success: function (response) {
            }, error: function (error) {
                if (error.responseText === "API DATA NOT FOUND"){
                    alert("데이터 초기화를 실패했습니다.")
                }
            }
        })
    }
})

$("#cafe-indust-reset").click(function() {
    if (!confirm("상가정보가 최신화 됩니다. 계속 하시겠습니까?")) {
        alert("아니오를 누르셨습니다.");
    } else {
        $.ajax({
            type: "POST",
            url: "/admin/cafe-api",
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

$("#datapair-reset-conv").click(function() {
    if (!confirm("편의점 DataPair 정보가 최신화 됩니다. 계속 하시겠습니까?")) {
        alert("아니오를 누르셨습니다.");
    } else {
        $.ajax({
            type: "POST",
            url: "/admin/conv-dataPair",
            success: function (response) {
            }, error: function (error) {
                if (error.responseText === "DATA-PAIR-CREATE-FAILED"){
                    alert("페어 데이터 저장에 실패했습니다. ")
                }
            }
        })
    }
})

$("#datapair-reset-cafe").click(function() {
    if (!confirm("카페 DataPair 정보가 최신화 됩니다. 계속 하시겠습니까?")) {
        alert("아니오를 누르셨습니다.");
    } else {
        $.ajax({
            type: "POST",
            url: "/admin/cafe-dataPair",
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




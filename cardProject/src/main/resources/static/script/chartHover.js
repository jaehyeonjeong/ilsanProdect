document.addEventListener("DOMContentLoaded", () => {

    // 마우스 올리면 차트 박스 확장
    document.querySelectorAll(".chart-box").forEach(box => {

        box.addEventListener("mouseenter", () => {
            box.classList.add("expand");
        });

        box.addEventListener("mouseleave", () => {
            box.classList.remove("expand");
        });

    });

});

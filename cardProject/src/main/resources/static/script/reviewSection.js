document.addEventListener("DOMContentLoaded", function() {

    const slides = document.querySelectorAll(".slide");
    const dotsContainer = document.querySelector(".slider-dots");
    const leftBtn = document.querySelector(".arrow.left");
    const rightBtn = document.querySelector(".arrow.right");

    let current = 0;

    // dots 생성
    slides.forEach((_, idx) => {
        const dot = document.createElement("span");
        if (idx === 0) dot.classList.add("active");
        dot.addEventListener("click", () => goTo(idx));
        dotsContainer.appendChild(dot);
    });

    const dots = dotsContainer.querySelectorAll("span");

    function update() {
        slides.forEach(s => s.classList.remove("active"));
        dots.forEach(d => d.classList.remove("active"));

        slides[current].classList.add("active");
        dots[current].classList.add("active");
    }

    function next() {
        current = (current + 1) % slides.length;
        update();
    }

    function prev() {
        current = (current - 1 + slides.length) % slides.length;
        update();
    }

    function goTo(idx) {
        current = idx;
        update();
    }

    rightBtn.addEventListener("click", next);
    leftBtn.addEventListener("click", prev);

    // 자동슬라이드 (원하면 꺼줄 수 있음)
    setInterval(next, 5000);
});

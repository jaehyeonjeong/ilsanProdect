document.addEventListener("DOMContentLoaded", () => {
    const slider = document.querySelector(".slider");
    const slides = document.querySelectorAll(".slider img");
    const prevBtn = document.querySelector(".prev-btn");
    const nextBtn = document.querySelector(".next-btn");

    let index = 0;
    const slideCount = slides.length;

    // ✅ 클론 생성 (양끝 무한 순환용)
    const firstClone = slides[0].cloneNode(true);
    const lastClone = slides[slideCount - 1].cloneNode(true);

    slider.appendChild(firstClone);
    slider.insertBefore(lastClone, slides[0]);

    let allSlides = document.querySelectorAll(".slider img");
    let slideWidth = allSlides[0].clientWidth + 18; // gap 고려
    let position = -slideWidth; // 첫 번째 진짜 슬라이드 위치로

    slider.style.transform = `translateX(${position}px)`;

    const moveToNext = () => {
        if (index >= slideCount) return;
        index++;
        position -= slideWidth;
        slider.style.transition = "transform 0.6s ease-in-out";
        slider.style.transform = `translateX(${position}px)`;

        if (index === slideCount) {
            setTimeout(() => {
                slider.style.transition = "none";
                index = 0;
                position = -slideWidth;
                slider.style.transform = `translateX(${position}px)`;
            }, 600);
        }
    };

    const moveToPrev = () => {
        if (index <= -1) return;
        index--;
        position += slideWidth;
        slider.style.transition = "transform 0.6s ease-in-out";
        slider.style.transform = `translateX(${position}px)`;

        if (index === -1) {
            setTimeout(() => {
                slider.style.transition = "none";
                index = slideCount - 1;
                position = -(slideWidth * slideCount);
                slider.style.transform = `translateX(${position}px)`;
            }, 600);
        }
    };

    nextBtn.addEventListener("click", moveToNext);
    prevBtn.addEventListener("click", moveToPrev);

    // ✅ 자동 슬라이드 (3초마다)
    setInterval(moveToNext, 3000);

    // ✅ 창 크기 변경 시 슬라이드 크기 재계산
    window.addEventListener("resize", () => {
        slideWidth = allSlides[0].clientWidth + 18;
        position = -slideWidth * (index + 1);
        slider.style.transform = `translateX(${position}px)`;
    });
});

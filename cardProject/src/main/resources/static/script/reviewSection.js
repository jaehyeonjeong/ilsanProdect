document.addEventListener("DOMContentLoaded", function() {
    const slider = document.querySelector('.slider');
    const items = Array.from(document.querySelectorAll('.slide-item'));
    const prevBtn = document.querySelector('.prev-btn');
    const nextBtn = document.querySelector('.next-btn');

    let currentIndex = 0;
    let autoTimer = null;

    function getGapPx() {
        const gap = getComputedStyle(slider).gap || '0px';
        return parseFloat(gap);
    }

    function getItemWidthPx() {
        // 각 카드의 실제 렌더링 너비(px)
        const first = items[0];
        return first ? first.getBoundingClientRect().width : 0;
    }

    function getVisibleCount() {
        const containerWidth = slider.parentElement.getBoundingClientRect().width;
        const itemWidth = getItemWidthPx();
        const gap = getGapPx();
        if (itemWidth === 0) return 5; // fallback
        return Math.max(1, Math.floor((containerWidth + gap) / (itemWidth + gap)));
    }

    function updateTransform() {
        const itemWidth = getItemWidthPx();
        const gap = getGapPx();
        const offset = currentIndex * (itemWidth + gap);
        slider.style.transform = `translateX(-${offset}px)`;
    }

    function goNext() {
        const visible = getVisibleCount();
        const maxIndex = Math.max(0, items.length - visible);
        if (currentIndex < maxIndex) {
            currentIndex += 1;
        } else {
            // 끝에 도달하면 처음으로
            currentIndex = 0;
        }
        updateTransform();
    }

    function goPrev() {
        const visible = getVisibleCount();
        const maxIndex = Math.max(0, items.length - visible);
        if (currentIndex > 0) {
            currentIndex -= 1;
        } else {
            // 처음이면 끝으로
            currentIndex = maxIndex;
        }
        updateTransform();
    }

    function startAutoSlide() {
        stopAutoSlide();
        autoTimer = setInterval(goNext, 4000); // 4초마다 자동 이동
    }

    function stopAutoSlide() {
        if (autoTimer) {
            clearInterval(autoTimer);
            autoTimer = null;
        }
    }

    // 버튼이 제대로 동작하도록 이벤트 연결
    nextBtn.addEventListener('click', () => {
        goNext();
        startAutoSlide(); // 클릭 후에도 자동 슬라이드 유지
    });

    prevBtn.addEventListener('click', () => {
        goPrev();
        startAutoSlide();
    });

    // 마우스 오버 시 일시정지, 떠나면 재시작
    slider.addEventListener('mouseenter', stopAutoSlide);
    slider.addEventListener('mouseleave', startAutoSlide);

    // 초기 렌더 후 계산값으로 정렬
    window.addEventListener('resize', updateTransform);
    updateTransform();
    startAutoSlide();
});
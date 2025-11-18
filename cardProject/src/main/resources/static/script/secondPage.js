document.addEventListener('DOMContentLoaded', () => {

    /* ========= 드롭다운 UI ========= */
    const containers = document.querySelectorAll('.custom-dropdown-container');

    containers.forEach(container => {
        const header = container.querySelector('.dropdown-header');
        const list = container.querySelector('.dropdown-list');
        const checkboxes = list.querySelectorAll('input[type="checkbox"]');
        const counter = header.querySelector('.selected-count');

        function updateCount() {
            let n = [...checkboxes].filter(c => c.checked).length;
            const type = counter.getAttribute("data-filter-type");
            counter.textContent = `${type} ${n}개 선택됨`;
        }

        updateCount();

        header.addEventListener('click', e => {
            e.stopPropagation();
            document.querySelectorAll('.dropdown-list').forEach(l => {
                if (l !== list) l.style.display = "none";
            });
            list.style.display = list.style.display === "block" ? "none" : "block";
        });

        checkboxes.forEach(chk => chk.addEventListener('change', updateCount));
    });

    document.addEventListener('click', () => {
        document.querySelectorAll('.dropdown-list').forEach(l => l.style.display = 'none');
    });


    /* ========= 바 형태 슬라이더 ========= */
    const annualMin = document.createElement("input");
    const annualMax = document.createElement("input");
    const perfMin = document.createElement("input");
    const perfMax = document.createElement("input");

    const numberInputs = document.querySelectorAll("input[type=number]");

    // range input 생성
    [annualMin, annualMax, perfMin, perfMax].forEach(el => {
        el.type = "range";
        el.min = 0;
        el.max = 5;
        el.value = 0;
        el.step = 1;
        el.classList.add("range-input");
    });

    // 연회비 바 삽입
    const annualGroup = numberInputs[0].closest("div");
    annualGroup.innerHTML = `
        <div class="range-wrapper">
            <div class="range-title">연회비</div>
            <div class="range-bar" id="annualBar"></div>
            <div class="range-labels">
                <span>0</span><span>1만</span><span>3만</span><span>5만</span><span>10만</span><span>10만+</span>
            </div>
        </div>
    `;

    document.getElementById("annualBar").appendChild(annualMin);
    document.getElementById("annualBar").appendChild(annualMax);

    // 전월실적 바 삽입
    const perfGroup = numberInputs[2].closest("div");
    perfGroup.innerHTML = `
        <div class="range-wrapper">
            <div class="range-title">전월 실적</div>
            <div class="range-bar" id="perfBar"></div>
            <div class="range-labels">
                <span>0</span><span>30만</span><span>50만</span><span>0+</span><span>30+</span><span>50+</span>
            </div>
        </div>
    `;

    document.getElementById("perfBar").appendChild(perfMin);
    document.getElementById("perfBar").appendChild(perfMax);

    // 범위 제한
    function sync(minEl, maxEl) {
        if (parseInt(minEl.value) > parseInt(maxEl.value)) {
            [minEl.value, maxEl.value] = [maxEl.value, minEl.value];
        }
    }

    annualMin.oninput = () => sync(annualMin, annualMax);
    annualMax.oninput = () => sync(annualMin, annualMax);
    perfMin.oninput = () => sync(perfMin, perfMax);
    perfMax.oninput = () => sync(perfMin, perfMax);
});

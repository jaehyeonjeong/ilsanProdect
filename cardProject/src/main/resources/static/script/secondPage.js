document.addEventListener('DOMContentLoaded', () => {

    /* ===============================
       드롭다운 UI
       =============================== */
    const containers = document.querySelectorAll('.custom-dropdown-container');

    containers.forEach(container => {
        const header = container.querySelector('.dropdown-header');
        const list = container.querySelector('.dropdown-list');
        const checkboxes = list.querySelectorAll('input[type="checkbox"]');
        const counter = header.querySelector('.selected-count');

        function updateCount() {
            const count = [...checkboxes].filter(c => c.checked).length;
            const type = counter.getAttribute("data-filter-type");
            counter.textContent = `${type} ${count}개 선택됨`;
        }

        updateCount();

        header.addEventListener('click', e => {
            e.stopPropagation();
            document.querySelectorAll('.dropdown-list').forEach(other => {
                if (other !== list) other.style.display = "none";
            });
            list.style.display = list.style.display === "block" ? "none" : "block";
        });

        checkboxes.forEach(chk =>
            chk.addEventListener('change', updateCount)
        );
    });

    document.addEventListener('click', () => {
        document.querySelectorAll('.dropdown-list').forEach(l => l.style.display = 'none');
    });


    /* ===============================
       바 형태 슬라이더 (연회비/전월실적)
       =============================== */

    const numberInputs = document.querySelectorAll("input[type=number]");

    const createRangePair = (minId, maxId, labelHtml, parentDiv) => {
        parentDiv.innerHTML = `
            <div class="range-wrapper">
                <div class="range-title">${labelHtml}</div>
                <div class="range-bar" id="${minId}Bar"></div>
                <div class="range-labels">${parentDiv.dataset.labels}</div>
            </div>
        `;

        const bar = parentDiv.querySelector(".range-bar");

        const min = document.createElement("input");
        const max = document.createElement("input");

        [min, max].forEach(el => {
            el.type = "range";
            el.min = 0;
            el.max = 5;
            el.step = 1;
            el.value = 0;
            el.classList.add("range-input");
        });

        bar.appendChild(min);
        bar.appendChild(max);

        const sync = () => {
            if (Number(min.value) > Number(max.value)) {
                [min.value, max.value] = [max.value, min.value];
            }
        };

        min.oninput = sync;
        max.oninput = sync;
    };


    /* 연회비 */
    const annualDiv = numberInputs[0].closest("div");
    annualDiv.dataset.labels = `
        <span>0</span><span>1만</span><span>3만</span><span>5만</span><span>10만</span><span>10만+</span>
    `;
    createRangePair("annual", "annualMax", "연회비", annualDiv);

    /* 전월 실적 */
    const perfDiv = numberInputs[2].closest("div");
    perfDiv.dataset.labels = `
        <span>0</span><span>30만</span><span>50만</span><span>70만</span><span>100만</span><span>100만+</span>
    `;
    createRangePair("perf", "perfMax", "전월 실적", perfDiv);

});

const itemList = document.querySelector("#itemList");

async function loadCardInfo() {
    const res = await fetch("/json/card.json");
    const data = await res.json();
    let output = "";
    data.items.forEach((item)=> {
        output += `<li class="swiper-slide"><div class="img"><img src="${item.img}" onerror="this.src='/images/default.png';"/></div></li>`;
        // console.log(item.img);
    });
    itemList.innerHTML = output;
    // console.log(data);

    const swiper = new Swiper("#main", {
        loop: true,
        slidesPerView: "auto",
        effect:"coverflow",
        centeredSlides: true,
        coverflowEffect:{
            rotate: 0,
            depth: 1000,
        },

        // If we need pagination
        pagination: {
            el: "#main .pagination",
            clickable: true,
        },
        mousewheel: true,

        // Navigation arrows
        navigation: {
            nextEl: ".swiper-button-next",
            prevEl: ".swiper-button-prev",
        },

        // And if we need scrollbar
        scrollbar: {
            el: ".swiper-scrollbar",
        },
    });
}


loadCardInfo();
// fetch("../data/mario.json")
//   .then(res=>res.json())
//   .then(data=>{
//     console.log(data);
//   })
let currentPage = 0;
const pageSize = 4;

document.addEventListener("DOMContentLoaded", () => {
    loadMemberships(currentPage);

    document.getElementById("pagination").addEventListener("click", (e) => {
        if (e.target.classList.contains("page-btn")) {
            const newPage = parseInt(e.target.dataset.page);
            currentPage = newPage;
            loadMemberships(currentPage);
        } else if (e.target.id === "prev-btn") {
            if (currentPage > 0) {
                currentPage--;
                loadMemberships(currentPage);
            }
        } else if (e.target.id === "next-btn") {
            const total = parseInt(e.target.dataset.total);
            if (currentPage < total - 1) {
                currentPage++;
                loadMemberships(currentPage);
            }
        }
    });
});

function loadMemberships(page) {
    fetch(`/api/Membership?page=${page}&size=${pageSize}`)
        .then(res => res.json())
        .then(data => {
            renderMemberships(data.content);
            renderPagination(data.totalPages, data.number);
        })
        .catch(err => console.error("Error cargando membresías:", err));
}

function renderMemberships(memberships) {
    const container = document.querySelector(".container");
    container.innerHTML = "";

    memberships.forEach(m => {
        const card = document.createElement("div");
        card.classList.add("card");

        card.innerHTML = `
            <div class="contenido"> //si no uso thymeleaf no me va, revisar
                <h2>${m.nombre}</h2> 
                <p>Precio: $${m.precio}</p>
                <p class="lead">${m.descripcion}</p>
                <a href="/Membership/${m.id}">INFO</a>
            </div>
        `;
        container.appendChild(card);
    });

    VanillaTilt.init(document.querySelectorAll(".card"), {
        max: 25,
        speed: 400,
        glare: true,
        "max-glare": 1,
    });
}

function renderPagination(totalPages, currentPage) {
    const pagination = document.getElementById("pagination");
    pagination.innerHTML = "";

    // Botón anterior
    const prev = document.createElement("button");
    prev.textContent = "Anterior";
    prev.id = "prev-btn";
    prev.disabled = currentPage === 0;
    pagination.appendChild(prev);

    // Botón siguiente
    const next = document.createElement("button");
    next.textContent = "Siguiente";
    next.id = "next-btn";
    next.dataset.total = totalPages;
    next.disabled = currentPage === totalPages - 1;
    pagination.appendChild(next);
}

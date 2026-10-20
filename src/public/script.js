const url = "http://localhost:7070";

async function Cadastrar() {
  const nome = document.getElementById("nome").value;
  const descricao = document.getElementById("descricao").value;
  const categoria = document.getElementById("categoria").value;
  const status = document.getElementById("status").value;
  const enviar = {
    nome,
    descricao,
    categoria,
    status,
  };
  console.log(enviar);

  await fetch(`${url}/api/projeto`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(enviar),
  });

  document.getElementById("formCadastro").reset();
  Exibir();
}

async function CarregarEditar(id) {
  const resposta = await fetch(`${url}/api/projeto/${id}`);
  const projeto = await resposta.json();

  let elementoPai = document.getElementById("popup");
  elementoPai.innerHTML = `<section class="card">
          <h1 class="card-title text-center mt-5">Gerenciamento de projetos</h1>
          <div class="card-body">
            <form id="formEditar" class="row gap-3">
              <div>
                <label for="nomeEditar" class="form-label">Nome</label>
                <input
                  type="text"
                  class="form-control"
                  name="nome"
                  id="nomeEditar"
                  placeholder="${projeto.nome}"
                />
              </div>

              <div>
                <label for="descricaoEditar" class="form-label">Descrição</label>
                <input
                  type="text"
                  class="form-control"
                  name="descricao"
                  id="descricaoEditar"
                  placeholder="${projeto.descricao}"
                />
              </div>

              <div>
                <label for="categoriaEditar" class="form-label">Categoria</label>
                <select class="form-select" name="categoria" id="categoriaEditar">
                  <option value="Web" ${projeto.categoria === "Web" ? "selected" : ""}>Web</option>
                  <option value="Mobile" ${projeto.categoria === "Mobile" ? "selected" : ""}>Mobile</option>
                  <option value="Desktop" ${projeto.categoria === "Desktop" ? "selected" : ""}>Desktop</option>
                  <option value="Outro" ${projeto.categoria === "Outro" ? "selected" : ""}>Outro</option>
                </select>
              </div>

              <div>
                <label for="statusEditar" class="form-label">Status</label>
                <select class="form-select" name="status" id="statusEditar">
                  <option value="Em planejamento" ${projeto.status === "Em planejamento" ? "selected" : ""}>Em planejamento</option>
                  <option value="Em desenvolvimento" ${projeto.status === "Em desenvolvimento" ? "selected" : ""}>Em desenvolvimento</option>
                  <option value="Concluido" ${projeto.status === "Concluido" ? "selected" : ""}>Concluído</option>
                </select>
              </div>

              <div class="d-flex justify-content-center gap-3">
                <button
                  type="button"
                  class="btn btn-success"
                  onclick="Editar(${id})"
                >
                  Alterar
                </button>
              </div>
            </form>
          </div>
        </section>`;

  const meuModal = new bootstrap.Modal(document.getElementById("modalEditar"));
  meuModal.show();
}

async function Editar(id) {
  let nome = document.getElementById("nomeEditar").value;
  let descricao = document.getElementById("descricaoEditar").value;
  const categoria = document.getElementById("categoriaEditar").value;
  const status = document.getElementById("statusEditar").value;

  const enviar = {
    nome,
    descricao,
    categoria,
    status,
  };
  console.log(enviar);

  await fetch(`${url}/api/projeto/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(enviar),
  });

  Exibir();

  let modal = bootstrap.Modal.getInstance(document.getElementById("modalEditar"))
  modal.hide();
}

async function Deletar(id) {
  await fetch(`${url}/api/projeto/${id}`, {
    method: "DELETE",
  });
  Exibir();
}

async function Exibir() {
  const resposta = await fetch(`${url}/api/projetos`);
  const projetos = await resposta.json();
  let corpo = document.getElementById("tableBody");

  corpo.replaceChildren();

  for (const projeto of projetos) {
    let btnEditar = `<button class="btn btn-primary" onclick="CarregarEditar(${projeto.id})">Editar</button>`;

    let btnApagar = `<button class="btn btn-danger" onclick="Deletar(${projeto.id})">Apagar</button>`;

    corpo.insertAdjacentHTML(
      "beforeend",
      `<tr>
        <th scope="col">${projeto.id}</th>
        <th scope="col">${projeto.nome}</th>
        <th scope="col">${projeto.descricao}</th>
        <th scope="col">${projeto.categoria}</th>
        <th scope="col">${projeto.status}</th>
        <th scope="col">${btnEditar} ${btnApagar}</th>
        </tr>`,
    );
  }
}

Exibir();

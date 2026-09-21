package service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dao.ProjetoCSV;
import model.Projeto;

public class ProjetoService {
    private List<Projeto> projetos;
    private ProjetoCSV dao;

    public ProjetoService() {
        projetos = new ArrayList<>();
        dao = new ProjetoCSV();

    }

    public void carregar() throws IOException {
        projetos = dao.listar();
    }

    public void salvar() throws IOException {
        dao.salvar(projetos);
    }

    public boolean adicionar(Projeto projeto) {
        if (
            projeto.getNome() == null ||
            projeto.getNome().isBlank()
        ) {
            return false;
        }

        if (buscarPorId(projeto.getId()) != null) {
            return false;
        }

        projetos.add(projeto);
        return true;
    }

    public boolean alterar(Projeto projetoAtualizado) {
        Projeto projeto = buscarPorId(projetoAtualizado.getId());

        if (projeto == null) {
            return false;
        }

        projeto.setNome(
            projetoAtualizado.getNome()
        );

        projeto.setDescricao(
            projetoAtualizado.getDescricao()
        );

        projeto.setCategoria(
            projetoAtualizado.getCategoria()
        );

        projeto.setStatus(
            projetoAtualizado.getStatus()
        );

        return true;
    }

    public boolean remover(int id) {
        Projeto projeto = buscarPorId(id);

        if(projeto != null) {
            projetos.remove(projeto);

            return true;
        }

        return false;
    }

    public List<Projeto> listar() {
        return projetos;
    }

    public Projeto buscarPorId(int id) {
        for(Projeto projeto : projetos) {
            if(projeto.getId() == id) {
                return projeto;
            }
        }

        return null;
    }

    public List<Projeto> buscarPorCategoria(String categoria) {
        List<Projeto> resultado = new ArrayList<>();

        for(Projeto projeto : projetos) {
            if (projeto.getCategoria()
                .equalsIgnoreCase(categoria)
            ) {
                resultado.add(projeto);
            }
        }

        return resultado;
    }

    public List<Projeto> buscarPorStatus(String status) {
        List<Projeto> resultado = new ArrayList<>();

        for(Projeto projeto : projetos) {
            if(projeto.getStatus().
                equalsIgnoreCase(status)
            ) {
                resultado.add(projeto);
            }
        }

        return resultado;
    }

    public List<Projeto> buscarPorNome(String nome){
        List<Projeto> resultado = new ArrayList<>();

        for(Projeto projeto : projetos) {
            if (
                projeto.getNome()
                    .toLowerCase()
                    .contains(nome.toLowerCase())
            ) {
                resultado.add(projeto);
            }
        }

        return resultado;
    }

    public int contarPorCategoria(String categoria) {
        return buscarPorCategoria(categoria).size();
    }

    public boolean alterarStatus(int id, String novoStatus) {
        Projeto projeto = buscarPorId(id);

        if(projeto == null) {
            return false;
        }

        projeto.setStatus(novoStatus);
        return true;
    }
}

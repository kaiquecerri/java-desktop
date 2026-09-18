import model.Projeto;
import service.ProjetoService;

import java.io.IOException;

import dao.ProjetoCSV;

public class Main {
    public static void main(String[] args) throws IOException{
        System.out.println("==============================");
        System.out.println("      SISTEMA DE PROJETOS     ");
        System.out.println("==============================");
        System.out.println();

        ProjetoService service = new ProjetoService();
        service.carregar();

        ProjetoCSV dao = new ProjetoCSV();
    
        for(Projeto projeto : dao.listar()) {
            projeto.exibirDados();
        }
        
    }

}
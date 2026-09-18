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

        ProjetoCSV dao = new ProjetoCSV();

        Projeto projeto1 = new Projeto(
            1, 
            "Sistema Acadêmico",
            "Sistema para gerenciamento acadêmico", 
            "Software", 
            "Em desenvolvimento"
        );

        Projeto projeto2 = new Projeto(
            2,
            "Website Institucional",
            "Um site para a Fatec",
            "Web",
            "Concluído"
        );

        service.adicionar(projeto1);
        service.adicionar(projeto2);
        
        dao.salvar(service.listar());
        System.out.println("Dados salvos.");

        for(Projeto projeto : dao.listar()) {
            projeto.exibirDados();
        }
        
    }

}
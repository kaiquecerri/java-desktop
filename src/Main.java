import model.Projeto;
import service.ProjetoService;

public class Main {
    public static void main(String[] args) {
        System.out.println("==============================");
        System.out.println("      SISTEMA DE PROJETOS     ");
        System.out.println("==============================");
        System.out.println();

        ProjetoService service = new ProjetoService();

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

        service.removerPorId(2);

        System.out.println("Total de projetos: " + service.listar().size());
        System.out.println("---------------------------------");

        for(
            Projeto projeto :
            service.buscarPorStatus("concluído")
        ) {
            projeto.exibirDados();
        }
        
        for(Projeto projeto : service.listar()) {
            projeto.exibirDados();
            System.out.println("---------------------------------");
        }
    }
}
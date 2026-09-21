import model.Projeto;
import service.ProjetoService;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException{
        Scanner sc = new Scanner(System.in);

        ProjetoService service = new ProjetoService();
        service.carregar();
        System.out.println("Projetos carregados: " + service.listar().size());
        System.out.println();

        int selecionado =  -1;  

        while(selecionado != 0) {
            System.out.println();
            System.out.println("==============================");
            System.out.println("      SISTEMA DE PROJETOS     ");
            System.out.println("==============================");
            System.out.println();
            System.out.println("1 - Listar projetos");
            System.out.println("2 - Buscar projeto");
            System.out.println("3 - Cadastrar projeto");
            System.out.println("4 - Alterar projeto");
            System.out.println("5 - Excluir projeto");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma das opções: ");

            selecionado = sc.nextInt();

            switch (selecionado) {
                case 0:
                    System.out.println("Finalizando o programa...");
                    break;

                case 1:
                    System.out.println("---- LISTAR PROJETOS ----");
                    for(Projeto projeto : service.listar()) {
                        projeto.exibirDados();
                        System.out.println();
                    }

                    for(int i = 0; i < 3; i++)
                        System.out.println();
                    break;

                case 2:
                    System.out.println("---- BUSCAR PROJETO ----");
                    System.out.print("Insira o ID do projeto: ");  
                    int idBusca = sc.nextInt();
                    
                    Projeto projetoBusca = service.buscarPorId(idBusca);

                    if (projetoBusca != null) {
                        projetoBusca.exibirDados();
                    } else {
                        System.out.println("Projeto não encontrado");
                    }
                    
                    for(int i = 0; i < 3; i++)
                        System.out.println();
                    break;

                case 3:
                    System.out.println("---- CADASTRAR PROJETO ----");
                    System.out.println("Insira os dados solicitados a seguir.");

                    System.out.print("ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Nome: ");
                    String nome = sc.nextLine();

                    System.out.print("Descrição: ");
                    String descricao = sc.nextLine();

                    System.out.print("Categoria: ");
                    String categoria = sc.nextLine();

                    System.out.print("Status: ");
                    String status = sc.nextLine();

                    Projeto projetoNovo = new Projeto (
                        id,
                        nome,
                        descricao,
                        categoria,
                        status
                    );

                    boolean cadastrado = service.adicionar(projetoNovo);

                    if (cadastrado) {
                        service.salvar();

                        System.out.println("Projeto cadastrado com sucesso.");
                    } else {
                        System.out.println("Não foi possível cadastrar.");
                    }
                    
                    break;

                case 4:
                    System.out.println("Alterar");
                    break;

                case 5:
                    System.out.println("Excluir");
                    break;
            
                default:
                    System.out.println("Invalido");
                    break;
            }
        }
          
    }

}
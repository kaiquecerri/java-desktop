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
                    int idCriar = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Nome: ");
                    String nomeCriar = sc.nextLine();

                    System.out.print("Descrição: ");
                    String descricaoCriar = sc.nextLine();

                    System.out.print("Categoria: ");
                    String categoriaCriar = sc.nextLine();

                    System.out.print("Status: ");
                    String statusCriar = sc.nextLine();

                    Projeto projetoNovo = new Projeto (
                        idCriar,
                        nomeCriar,
                        descricaoCriar,
                        categoriaCriar,
                        statusCriar
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
                    System.out.println("---- ALTERAR PROJETO ----");
                    System.out.print("Insira o ID do projeto que deseja alterar: ");
                    int idAlterar = sc.nextInt();
                    sc.nextLine();
                    
                    Projeto existente = service.buscarPorId(idAlterar);
                    if (existente == null) {
                        System.out.println("Projeto não encontrado.");
                        break;
                    }

                    System.out.println("Projeto atual: ");
                    existente.exibirDados();

                    System.out.print("Novo nome: ");
                    String nomeAlterar = sc.nextLine();

                    System.out.print("Nova descrição: ");
                    String descricaoAlterar = sc.nextLine();

                    System.out.print("Nova categoria: ");
                    String categoriaAlterar = sc.nextLine();

                    System.out.print("Novo status: ");
                    String statusAlterar = sc.nextLine();

                    Projeto atualizado = new Projeto(
                        idAlterar, 
                        nomeAlterar, 
                        descricaoAlterar, 
                        categoriaAlterar, 
                        statusAlterar
                    );

                    boolean alterado = service.alterar(atualizado);

                    if(alterado) {
                        service.salvar();

                        System.out.println("Projeto alterado com sucesso.");
                    } else {
                        System.out.println("Não foi possível alterar.");
                    }

                    break;

                case 5:
                    System.out.println("---- EXCLUIR PROJETO ----");
                    System.out.print("Insira o ID do projeto que deseja excluir: ");
                    int idExcluir = sc.nextInt();
                    sc.nextLine();

                    Projeto existe = service.buscarPorId(idExcluir);

                    if(existe == null) {
                        System.out.println("Este projeto não foi encontrado.");

                        break;
                    }
                    
                    existe.exibirDados();
                    System.out.println("Tem certeza que deseja excluir este projeto?");
                    System.out.print("S (Sim) ou N (Não): ");
                    String confirmacao = sc.nextLine();

                    if(confirmacao.equalsIgnoreCase("s")) {
                        boolean removido = service.remover(idExcluir);
                        
                        if(removido) {
                            service.salvar();
                            System.out.println("Projeto excluído com sucesso.");
                        } else {
                            System.out.println("Não foi possível excluir este projeto.");
                        }
                    } else {
                        System.out.println("Exclusão cancelada.");
                    }


                    break;
            
                default:
                    System.out.println("Invalido");
                    break;
            }
        }

        sc.close();
    }

}
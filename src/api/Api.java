package api;

import java.io.IOException;
import java.util.List;

import io.javalin.Javalin;

import model.Projeto;
import service.ProjetoService;
import util.ErroResponse;

public class Api {
    public static void main(String[] args) {
        ProjetoService service = new ProjetoService();
        
        try {
            service.carregar();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Javalin.create(config -> {

            //
            // METODOS GET
            //

            //PROJETOS
            config.routes.get("/", ctx -> {
                ctx.result("Projeto");
            });

            config.routes.get("/api/projetos", ctx -> {
                List<Projeto> projetos = service.listar();
                ctx.json(projetos);
            });

            config.routes.get("/api/projeto/{id}", ctx -> {
                int id = Integer.parseInt(
                    ctx.pathParam("id")
                );

                Projeto projeto = service.buscarPorId(id);

                if (projeto == null) {
                    ctx.status(404);

                    return;
                }

                ctx.json(projeto);
            });
            
            //CATEGORIAS

            config.routes.get("/api/categorias", ctx -> {
                String categorias[] = new String[4];
                categorias[0] = "Web";
                categorias[1] = "Software";
                categorias[2] = "Mobile";
                categorias[3] = "Outros";
                
                ctx.json(categorias);
            });

            //STATUS

            config.routes.get("/api/status", ctx -> {
                String statuts[] = new String[3];
                statuts[0] = "Planejado";
                statuts[1] = "Em desenvolvimento";
                statuts[2] = "Concluído";
                
                ctx.json(statuts);
            });

            //
            // METODOS POST
            //

            //CRIAR NOVO PROJETO
            config.routes.post("/api/projetos", ctx -> {
                Projeto projeto = ctx.bodyAsClass(Projeto.class);

                if (projeto.getNome() == null ||
                    projeto.getNome().isBlank()) {

                    ctx.status(400); //ERRO

                    ctx.json(
                        new ErroResponse("Nome é obrigatório")
                    ); //MOTIVO

                    return;
                }

                service.adicionar(projeto);
                service.salvar();

                ctx.status(201); //DEU CERTO
                ctx.json(projeto); //DEVOLVE O PROJETO
            });

            //
            // METODOS PUT
            //

            //EDITAR PROJETO

            config.routes.put(
            "/api/projetos/{id}",
                ctx -> {

                    int id = Integer.parseInt(
                        ctx.pathParam("id")
                    );

                    Projeto projeto = ctx.bodyAsClass(Projeto.class);
                    projeto.setId(id);

                    boolean alterou = service.alterar(id, projeto);

                    if (!alterou) {
                        ctx.status(404);
                        ctx.json(
                            new ErroResponse("Não foi possível alterar este projeto")
                        );
                        return;
                    }

                    service.salvar();

                    ctx.json(projeto);
                }
            );

            //
            // METODOS DELETE
            //

            //EDITAR PROJETO
            config.routes.delete(
                "/api/projetos/{id}",
                ctx -> {

                    int id = Integer.parseInt(
                        ctx.pathParam("id")
                    );

                    Projeto projeto = service.buscarPorId(id);

                    if (projeto == null) {
                        ctx.status(404);
                        ctx.json(
                            new ErroResponse("Não foi possível localizar este projeto")
                        );
                        return;
                    }

                    service.remover(id);
                    service.salvar();

                    ctx.status(204);
                }
            );

        }).start(7070);
    }
}

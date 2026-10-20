package api;

import java.io.IOException;
import java.util.List;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;   

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

            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "src/public";  // caminho relativo ao projeto
                staticFiles.location = Location.EXTERNAL;
            });

            //
            // METODOS GET
            //

            //PROJETOS
            config.routes.get("/", ctx -> {
                ctx.result("Projeto");
            });

            config.routes.get("/api/projetos", ctx -> {
                try {
                    service.carregar();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                List<Projeto> projetos = service.listar();
                ctx.json(projetos);
            });

            config.routes.get("/api/projeto/{id}", ctx -> {
                try {
                    service.carregar();
                } catch (IOException e) {
                    e.printStackTrace();
                }

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
                statuts[0] = "Em planejamento";
                statuts[1] = "Em desenvolvimento";
                statuts[2] = "Concluido";
                
                ctx.json(statuts);
            });

            //
            // METODOS POST
            //

            //CRIAR NOVO PROJETO
            config.routes.post("/api/projeto", ctx -> {
                try {
                    service.carregar();
                } catch (IOException e) {
                    e.printStackTrace();
                }

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
            "/api/projeto/{id}",
                ctx -> {
                    try {
                    service.carregar();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    int id = Integer.parseInt(
                        ctx.pathParam("id")
                    );

                    Projeto projeto = ctx.bodyAsClass(Projeto.class);
                    projeto.setId(id);

                    if(projeto.getNome().length() <= 0) {
                        projeto.setNome(service.buscarPorId(id).getNome());
                    }

                    if(projeto.getDescricao().length() <= 0) {
                        projeto.setDescricao(service.buscarPorId(id).getDescricao());
                    }

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

            //DELETAR PROJETO
            config.routes.delete(
                "/api/projeto/{id}",
                ctx -> {
                    try {
                    service.carregar();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

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

package api;

import java.io.IOException;
import java.util.List;

import io.javalin.Javalin;

import model.Projeto;
import service.ProjetoService;

public class Api {
    public static void main(String[] args) {
        ProjetoService service = new ProjetoService();
    try {
        service.carregar();
    } catch (IOException e) {
        e.printStackTrace();
    }

        var app = Javalin.create(config -> {

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

        }).start(7070);
    }
}

package view;

import java.io.IOException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import service.ProjetoService;
import model.Projeto;

public class TelaPrincipal extends JFrame {
        private JLabel labelNome;
        private JTextField campoNome;

        private JLabel labelDescricao;
        private JTextField campoDescricao; 

        private JLabel labelCategoria;
        private JComboBox<String> comboCategoria;

        private JLabel labelStatus;
        private JComboBox<String> comboStatus;

        private JButton botaoCadastrar;
        private JButton botaoLimpar;
        private JButton botaoExcluirSelecionado;

        private DefaultTableModel modelo;
        private JTable tabela;

        private JPanel painelFormulario;

        private ProjetoService service;


    public TelaPrincipal () {
        service = new ProjetoService();
        try {
            service.carregar();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setTitle("Sistema de Projetos");
        setSize(800, 500);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        criarComponentes();
        criarEventos();

        carregarTabela();
    }

    private void criarComponentes() {
        labelNome = new JLabel("Nome:");
        campoNome = new JTextField(30);

        labelDescricao = new JLabel("Descrição:");
        campoDescricao = new JTextField(30);

        labelCategoria = new JLabel("Categoria:");
        comboCategoria = new JComboBox<>();

        comboCategoria.addItem("Web");
        comboCategoria.addItem("Mobile");
        comboCategoria.addItem("Desktop");
        comboCategoria.addItem("Outro");

        labelStatus = new JLabel("Status:");
        comboStatus = new JComboBox<>();

        comboStatus.addItem("Em planejamento");
        comboStatus.addItem("Em desenvolvimento");
        comboStatus.addItem("Concluído");

        botaoCadastrar = new JButton("Cadastrar");
        
        botaoLimpar = new JButton("Limpar");

        botaoExcluirSelecionado = new JButton("Excluir selecionado");

        modelo = new DefaultTableModel();

        modelo.addColumn("ID");
        modelo.addColumn("Nome");
        modelo.addColumn("Descrição");
        modelo.addColumn("Categoria");
        modelo.addColumn("Status");

        tabela = new JTable(modelo);

        painelFormulario = new JPanel();

        painelFormulario.add(labelNome);
        painelFormulario.add(campoNome);

        painelFormulario.add(labelDescricao);
        painelFormulario.add(campoDescricao);

        painelFormulario.add(labelCategoria);
        painelFormulario.add(comboCategoria);

        painelFormulario.add(labelStatus);
        painelFormulario.add(comboStatus);

        painelFormulario.add(botaoCadastrar);
        painelFormulario.add(botaoLimpar);
        painelFormulario.add(botaoExcluirSelecionado);

        setLayout(new BoxLayout(
            getContentPane(), 
            BoxLayout.Y_AXIS
        ));

        add(painelFormulario);
        add(new JScrollPane(tabela));
    }

    private void criarEventos() {
        botaoCadastrar.addActionListener(e -> cadastrar());
        botaoLimpar.addActionListener(e -> limparFormulario());
        botaoExcluirSelecionado.addActionListener(e -> excluirSelecionadoTabela());
    }

    private void cadastrar() {
        String nome = campoNome.getText();
        if (nome.isBlank()) {
            JOptionPane.showMessageDialog(
                this, 
                "Informe o nome."
            );

            return;
        }

        String descricao = campoDescricao.getText();
        if (descricao.isBlank()) {
            JOptionPane.showMessageDialog(
                this, 
                "Informe a descrição."
            );

            return;
        }

        String categoria = comboCategoria.getSelectedItem().toString();
        String status = comboStatus.getSelectedItem().toString();

        Projeto projeto = new Projeto(
            service.listar().size() + 1,
            nome,
            descricao,
            categoria,
            status
        );

        service.adicionar(projeto);
        try {
            service.salvar();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(
            this,
            "Projeto " + nome + " cadastrado com sucesso."
        );

        limparFormulario();
        carregarTabela();
    }

    private void limparFormulario() {
        campoNome.setText("");
        campoDescricao.setText("");
        comboCategoria.setSelectedIndex(0);
        comboStatus.setSelectedIndex(0);
        campoNome.requestFocus();
    }

    private void excluirSelecionadoTabela() {
        int linha = tabela.getSelectedRow();

        if(linha != -1) {
            Object nomeProjetoSelecionado = tabela.getValueAt(linha, 1);
            int selecionado = JOptionPane.showConfirmDialog(this, "Projeto selecionado: " + nomeProjetoSelecionado.toString() + "\nVocê tem certeza que deseja excluir? Não é possível reverter.");
            
            if(selecionado == 0) {
                int idProjetoSelecionado = (Integer) tabela.getValueAt(linha, 0);
                service.remover(idProjetoSelecionado);
                try {
                    service.salvar();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                carregarTabela();
                
            }

        } else {
            JOptionPane.showMessageDialog(
                this,
                "Selecione um projeto."
            );
        }
    }
    
    private void carregarTabela() {
        modelo.setRowCount(0);
        for(Projeto projeto : service.listar()){
            modelo.addRow(
                new Object[]{
                    projeto.getId(),
                    projeto.getNome(),
                    projeto.getDescricao(),
                    projeto.getCategoria(),
                    projeto.getStatus()
                }
            );
        }        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaPrincipal tela = new TelaPrincipal();
            tela.setVisible(true);
        });
    }
}

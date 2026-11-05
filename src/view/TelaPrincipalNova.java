package view;

import java.awt.*;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import service.ProjetoService;
import model.Projeto;

public class TelaPrincipalNova extends JFrame {

    // ── Paleta de cores ──────────────────────────────────────────────────────
    private static final Color COR_FUNDO        = new Color(245, 247, 250);
    private static final Color COR_HEADER_BG    = new Color(37,  99,  235);  // azul
    private static final Color COR_HEADER_TEXT  = Color.WHITE;
    private static final Color COR_CARD_BG      = Color.WHITE;
    private static final Color COR_BORDA        = new Color(226, 232, 240);
    private static final Color COR_LABEL        = new Color(71,  85, 105);
    private static final Color COR_BTN_CADASTRAR = new Color(37,  99, 235);
    private static final Color COR_BTN_LIMPAR   = new Color(100, 116, 139);
    private static final Color COR_BTN_ALTERAR  = new Color(245, 158,  11);
    private static final Color COR_BTN_EXCLUIR  = new Color(239,  68,  68);
    private static final Color COR_BTN_RELOAD   = new Color(16,  185, 129);
    private static final Color COR_TABELA_HEADER = new Color(37,  99, 235);
    private static final Color COR_LINHA_PAR    = new Color(248, 250, 252);

    // ── Componentes ──────────────────────────────────────────────────────────
    private JTextField campoNome;
    private JTextField campoDescricao;
    private JComboBox<String> comboCategoria;
    private JComboBox<String> comboStatus;

    private JButton botaoCadastrar;
    private JButton botaoLimpar;
    private JButton botaoAlterar;
    private JButton botaoExcluir;
    private JButton botaoRecarregar;

    private DefaultTableModel modelo;
    private JTable tabela;

    private ProjetoService service;

    // ════════════════════════════════════════════════════════════════════════
    public TelaPrincipalNova() {
        service = new ProjetoService();
        try {
            service.carregar();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setTitle("Sistema de Gerenciamento de Projetos");
        setSize(950, 650);
        setMinimumSize(new Dimension(800, 550));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Fundo geral
        getContentPane().setBackground(COR_FUNDO);
        setLayout(new BorderLayout(0, 0));

        add(criarHeader(),       BorderLayout.NORTH);
        add(criarPainelCentro(), BorderLayout.CENTER);

        criarEventos();
        carregarTabela();
    }

    // ── Header ───────────────────────────────────────────────────────────────
    private JPanel criarHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 18));
        header.setBackground(COR_HEADER_BG);
        header.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel titulo = new JLabel("Sistema de Gerenciamento de Projetos");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(COR_HEADER_TEXT);
        header.add(titulo);

        return header;
    }

    // ── Painel central (formulário + tabela) ─────────────────────────────────
    private JPanel criarPainelCentro() {
        JPanel centro = new JPanel();
        centro.setBackground(COR_FUNDO);
        centro.setLayout(new BorderLayout(0, 16));
        centro.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        centro.add(criarCardFormulario(), BorderLayout.NORTH);
        centro.add(criarCardTabela(),     BorderLayout.CENTER);

        return centro;
    }

    // ── Card Formulário ──────────────────────────────────────────────────────
    private JPanel criarCardFormulario() {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(COR_CARD_BG);
        card.setBorder(new CompoundBorder(
            new LineBorder(COR_BORDA, 1, true),
            BorderFactory.createEmptyBorder(20, 24, 20, 24)
        ));

        // Título do card
        JLabel cardTitulo = new JLabel("Novo Projeto");
        cardTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cardTitulo.setForeground(COR_HEADER_BG);
        card.add(cardTitulo, BorderLayout.NORTH);

        // Painel dos campos (grid 2 colunas)
        JPanel campos = new JPanel(new GridBagLayout());
        campos.setBackground(COR_CARD_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(6, 8, 6, 8);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.anchor  = GridBagConstraints.WEST;

        // Nome
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        campos.add(criarLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        campoNome = criarTextField("Nome do projeto");
        campos.add(campoNome, gbc);

        // Descrição
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        campos.add(criarLabel("Descrição:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        campoDescricao = criarTextField("Descrição do projeto");
        campos.add(campoDescricao, gbc);

        // Categoria
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        campos.add(criarLabel("Categoria:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        comboCategoria = new JComboBox<>(new String[]{"Web", "Mobile", "Desktop", "Outro"});
        estilizarCombo(comboCategoria);
        campos.add(comboCategoria, gbc);

        // Status
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        campos.add(criarLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        comboStatus = new JComboBox<>(new String[]{"Em planejamento", "Em desenvolvimento", "Concluido"});
        estilizarCombo(comboStatus);
        campos.add(comboStatus, gbc);

        card.add(campos, BorderLayout.CENTER);
        card.add(criarPainelBotoes(), BorderLayout.SOUTH);

        return card;
    }

    // ── Painel de botões ─────────────────────────────────────────────────────
    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        painel.setBackground(COR_CARD_BG);

        botaoRecarregar = criarBotao("Recarregar",  COR_BTN_RELOAD);
        botaoExcluir    = criarBotao("Excluir",     COR_BTN_EXCLUIR);
        botaoAlterar    = criarBotao("Alterar",     COR_BTN_ALTERAR);
        botaoLimpar     = criarBotao("Limpar",      COR_BTN_LIMPAR);
        botaoCadastrar  = criarBotao("Cadastrar",   COR_BTN_CADASTRAR);

        painel.add(botaoRecarregar);
        painel.add(botaoExcluir);
        painel.add(botaoAlterar);
        painel.add(botaoLimpar);
        painel.add(botaoCadastrar);

        return painel;
    }

    // ── Card Tabela ──────────────────────────────────────────────────────────
    private JPanel criarCardTabela() {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(COR_CARD_BG);
        card.setBorder(new CompoundBorder(
            new LineBorder(COR_BORDA, 1, true),
            BorderFactory.createEmptyBorder(16, 24, 16, 24)
        ));

        // Título
        JLabel tituloTabela = new JLabel("Projetos Cadastrados");
        tituloTabela.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tituloTabela.setForeground(COR_HEADER_BG);
        card.add(tituloTabela, BorderLayout.NORTH);

        // Modelo da tabela
        modelo = new DefaultTableModel(
            new String[]{"ID", "Nome", "Descrição", "Categoria", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tabela = new JTable(modelo);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabela.setRowHeight(32);
        tabela.setGridColor(COR_BORDA);
        tabela.setSelectionBackground(new Color(219, 234, 254));
        tabela.setSelectionForeground(Color.BLACK);
        tabela.setShowVerticalLines(false);
        tabela.setIntercellSpacing(new Dimension(0, 1));
        tabela.setFillsViewportHeight(true);

        // Cabeçalho da tabela
        JTableHeader cabecalho = tabela.getTableHeader();
        cabecalho.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cabecalho.setBackground(COR_TABELA_HEADER);
        cabecalho.setForeground(Color.WHITE);
        cabecalho.setPreferredSize(new Dimension(0, 36));
        cabecalho.setReorderingAllowed(false);

        // Renderer de linhas alternadas
        tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                if (!sel) {
                    setBackground(row % 2 == 0 ? Color.WHITE : COR_LINHA_PAR);
                    setForeground(new Color(30, 41, 59));
                }
                return this;
            }
        });

        // Larguras das colunas
        int[] larguras = {50, 180, 220, 110, 150};
        for (int i = 0; i < larguras.length; i++) {
            tabela.getColumnModel().getColumn(i).setPreferredWidth(larguras[i]);
        }

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(new LineBorder(COR_BORDA, 1));
        scroll.getViewport().setBackground(Color.WHITE);

        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    // ── Utilitários de criação de componentes ────────────────────────────────
    private JLabel criarLabel(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(COR_LABEL);
        l.setPreferredSize(new Dimension(90, 28));
        return l;
    }

    private JTextField criarTextField(String placeholder) {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(new CompoundBorder(
            new LineBorder(COR_BORDA, 1, true),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        tf.setPreferredSize(new Dimension(0, 32));
        return tf;
    }

    private void estilizarCombo(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setBackground(Color.WHITE);
        combo.setBorder(new LineBorder(COR_BORDA, 1, true));
        combo.setPreferredSize(new Dimension(0, 32));
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 36));

        // Efeito hover escurece levemente
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            final Color original = cor;
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(original.darker());
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(original);
            }
        });

        return btn;
    }

    // ── Eventos ──────────────────────────────────────────────────────────────
    private void criarEventos() {
        botaoCadastrar.addActionListener(e -> cadastrar());
        botaoLimpar.addActionListener(e -> limparFormulario());
        botaoAlterar.addActionListener(e -> alterarSelecionadoTabela());
        botaoExcluir.addActionListener(e -> excluirSelecionadoTabela());

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarSelecionadoTabelaForm();
            }
        });

        botaoRecarregar.addActionListener(e -> {
            try { service.carregar(); } catch (IOException er) { er.printStackTrace(); }
            carregarTabela();
        });
    }

    // ── Lógica de negócio (igual à TelaPrincipal original) ──────────────────
    private void cadastrar() {
        String nome = campoNome.getText();
        if (nome.isBlank()) {
            mostrarAviso("Informe o nome.");
            return;
        }
        String descricao = campoDescricao.getText();
        if (descricao.isBlank()) {
            mostrarAviso("Informe a descrição.");
            return;
        }

        String categoria = comboCategoria.getSelectedItem().toString();
        String status    = comboStatus.getSelectedItem().toString();

        Projeto projeto = new Projeto(nome, descricao, categoria, status);

        if (!service.adicionar(projeto)) {
            mostrarAviso("Não foi possível cadastrar o projeto.");
            return;
        }

        try { service.salvar(); } catch (IOException e) { e.printStackTrace(); }

        JOptionPane.showMessageDialog(this,
            "Projeto \"" + nome + "\" cadastrado com sucesso! ✔",
            "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        limparFormulario();
        carregarTabela();
    }

    private void limparFormulario() {
        campoNome.setText("");
        campoDescricao.setText("");
        comboCategoria.setSelectedIndex(0);
        comboStatus.setSelectedIndex(0);
        tabela.clearSelection();
        campoNome.requestFocus();
    }

    private void excluirSelecionadoTabela() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            mostrarAviso("Selecione um projeto na tabela.");
            return;
        }

        String nomeProjeto = tabela.getValueAt(linha, 1).toString();
        int confirmacao = JOptionPane.showConfirmDialog(this,
            "Excluir projeto \"" + nomeProjeto + "\"?\nEsta ação não pode ser revertida.",
            "Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmacao == JOptionPane.YES_OPTION) {
            int id = (Integer) tabela.getValueAt(linha, 0);
            service.remover(id);
            try { service.salvar(); } catch (IOException e) { e.printStackTrace(); }
            limparFormulario();
            carregarTabela();
        }
    }

    private void carregarSelecionadoTabelaForm() {
        int linha = tabela.getSelectedRow();
        if (linha != -1) {
            campoNome.setText(String.valueOf(tabela.getValueAt(linha, 1)));
            campoDescricao.setText(String.valueOf(tabela.getValueAt(linha, 2)));
            comboCategoria.setSelectedItem(tabela.getValueAt(linha, 3));
            comboStatus.setSelectedItem(tabela.getValueAt(linha, 4));
            campoNome.requestFocus();
        }
    }

    private void alterarSelecionadoTabela() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            mostrarAviso("Selecione um projeto na tabela.");
            return;
        }

        String nome = campoNome.getText();
        if (nome.isBlank()) { mostrarAviso("Informe o nome."); return; }

        String descricao = campoDescricao.getText();
        if (descricao.isBlank()) { mostrarAviso("Informe a descrição."); return; }

        String categoria = comboCategoria.getSelectedItem().toString();
        String status    = comboStatus.getSelectedItem().toString();
        int id           = (Integer) tabela.getValueAt(linha, 0);

        Projeto projeto = new Projeto(nome, descricao, categoria, status);

        if (!service.alterar(id, projeto)) {
            mostrarAviso("Não foi possível alterar o projeto.");
            return;
        }

        try { service.salvar(); } catch (IOException e) { e.printStackTrace(); }

        JOptionPane.showMessageDialog(this,
            "Projeto \"" + nome + "\" alterado com sucesso! ✔",
            "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        limparFormulario();
        carregarTabela();
    }

    private void carregarTabela() {
        modelo.setRowCount(0);
        for (Projeto p : service.listar()) {
            modelo.addRow(new Object[]{
                p.getId(), p.getNome(), p.getDescricao(), p.getCategoria(), p.getStatus()
            });
        }
    }

    private void mostrarAviso(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Atenção", JOptionPane.WARNING_MESSAGE);
    }

    // ── Main ─────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            TelaPrincipalNova tela = new TelaPrincipalNova();
            tela.setVisible(true);
        });
    }
}


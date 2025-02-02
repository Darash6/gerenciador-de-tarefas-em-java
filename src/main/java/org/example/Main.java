package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        // Criar a janela principal
        JFrame frame = new JFrame("Gerenciador de Tarefas");
        frame.setSize(750, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Criar o componente de abas
        JTabbedPane tabbedPane = new JTabbedPane();

        // ---- ABA 1: Tarefas para fazer ----
        JPanel aba1 = new JPanel(new BorderLayout());
        DefaultListModel<String> tarefasModel = new DefaultListModel<>();
        JList<String> tarefasList = new JList<>(tarefasModel);
        tarefasList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTextField campoTexto = new JTextField(20);
        JButton botaoAdicionar = new JButton("Adicionar");
        JButton botaoExcluir = new JButton("Excluir");
        JButton botaoFeito = new JButton("Feito");
        JButton botaoIncompleto = new JButton("Incompleto");

        JPanel painelInferiorAba1 = new JPanel();
        painelInferiorAba1.add(new JLabel("Nova tarefa:"));
        painelInferiorAba1.add(campoTexto);
        painelInferiorAba1.add(botaoAdicionar);
        painelInferiorAba1.add(botaoExcluir);
        painelInferiorAba1.add(botaoFeito);
        painelInferiorAba1.add(botaoIncompleto);

        aba1.add(new JScrollPane(tarefasList), BorderLayout.CENTER);
        aba1.add(painelInferiorAba1, BorderLayout.SOUTH);

        // ---- ABA 2: Tarefas incompletas ----
        JPanel aba2 = new JPanel(new BorderLayout());
        DefaultListModel<String> incompletasModel = new DefaultListModel<>();
        JList<String> incompletasList = new JList<>(incompletasModel);

        JPanel painelInferiorAba2 = new JPanel();
        aba2.add(new JScrollPane(incompletasList), BorderLayout.CENTER);
        aba2.add(painelInferiorAba2, BorderLayout.SOUTH);

        // ---- ABA 3: Tarefas concluídas ----
        JPanel aba3 = new JPanel(new BorderLayout());
        DefaultListModel<String> concluidasModel = new DefaultListModel<>();
        JList<String> concluidasList = new JList<>(concluidasModel);

        JPanel painelInferiorAba3 = new JPanel();
        aba3.add(new JScrollPane(concluidasList), BorderLayout.CENTER);
        aba3.add(painelInferiorAba3, BorderLayout.SOUTH);

        // ---- ABA 4: Observações ----
        JPanel aba4 = new JPanel(new BorderLayout());
        DefaultListModel<String> observacoesModel = new DefaultListModel<>();
        JList<String> observacoesList = new JList<>(observacoesModel);

        aba4.add(new JScrollPane(observacoesList), BorderLayout.CENTER);

        // ---- Adicionar as abas ----
        tabbedPane.addTab("Tarefas para fazer", aba1);
        tabbedPane.addTab("Tarefas incompletas", aba2);
        tabbedPane.addTab("Tarefas concluídas", aba3);
        tabbedPane.addTab("Observações", aba4);

        // ---- Funcionalidades ----
        // Botão "Adicionar" na aba 1
        botaoAdicionar.addActionListener(e -> adicionarTarefa(campoTexto, tarefasModel, frame));

        // Botão "Excluir" na aba 1
        botaoExcluir.addActionListener(e -> excluirTarefa(tarefasList, tarefasModel, frame));

        // Botão "Feito" na aba 1
        botaoFeito.addActionListener(e -> moverParaConcluidas(tarefasList, tarefasModel, concluidasModel));

        // Botão "Incompleto" na aba 1
        botaoIncompleto.addActionListener(e -> moverParaIncompletas(tarefasList, tarefasModel, incompletasModel));

        // Atalho Enter para adicionar tarefas
        campoTexto.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    adicionarTarefa(campoTexto, tarefasModel, frame);
                }
            }
        });

        // Adicionar funcionalidade de atalhos de teclado para tarefasList
        tarefasList.addKeyListener(criarAtalhosTeclado(tarefasList, tarefasModel, concluidasModel, incompletasModel, frame));
        incompletasList.addKeyListener(criarAtalhosTeclado(incompletasList, incompletasModel, concluidasModel, null, frame));
        concluidasList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_O) {
                    criarObservacao(concluidasList, concluidasModel, observacoesModel, frame);
                }
            }
        });

        // Alterar a fonte do texto das abas
        Font fonteGrande = new Font("Arial", Font.BOLD, 16);
        tabbedPane.setFont(fonteGrande);

        // Adicionar o componente de abas à janela
        frame.add(tabbedPane);

        // Tornar a janela visível
        frame.setVisible(true);
    }

    private static KeyAdapter criarAtalhosTeclado(JList<String> lista, DefaultListModel<String> origemModel,
                                                  DefaultListModel<String> concluidasModel, DefaultListModel<String> incompletasModel, JFrame frame) {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown()) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_E: // Ctrl + E: Excluir
                            excluirTarefa(lista, origemModel, frame);
                            break;
                        case KeyEvent.VK_I: // Ctrl + I: Incompleto
                            if (incompletasModel != null) {
                                moverParaIncompletas(lista, origemModel, incompletasModel);
                            }
                            break;
                        case KeyEvent.VK_C: // Ctrl + C: Concluído
                            moverParaConcluidas(lista, origemModel, concluidasModel);
                            break;
                    }
                }
            }
        };
    }

    private static void adicionarTarefa(JTextField campoTexto, DefaultListModel<String> tarefasModel, JFrame frame) {
        String novaTarefa = campoTexto.getText().trim();
        if (!novaTarefa.isEmpty()) {
            String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            tarefasModel.addElement(novaTarefa + " (Adicionado em: " + dataHora + ")");
            campoTexto.setText("");
        } else {
            JOptionPane.showMessageDialog(frame, "Por favor, insira uma tarefa válida.");
        }
    }

    private static void excluirTarefa(JList<String> lista, DefaultListModel<String> model, JFrame frame) {
        int selecionado = lista.getSelectedIndex();
        if (selecionado != -1) {
            model.remove(selecionado);
        } else {
            JOptionPane.showMessageDialog(frame, "Selecione uma tarefa para excluir.");
        }
    }

    private static void moverParaConcluidas(JList<String> lista, DefaultListModel<String> origemModel, DefaultListModel<String> destinoModel) {
        int selecionado = lista.getSelectedIndex();
        if (selecionado != -1) {
            String tarefa = origemModel.getElementAt(selecionado);
            origemModel.remove(selecionado);
            String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            destinoModel.addElement(tarefa + " (Feito em: " + dataHora + ")");
        } else {
            JOptionPane.showMessageDialog(null, "Selecione uma tarefa para marcar como concluída.");
        }
    }

    private static void moverParaIncompletas(JList<String> lista, DefaultListModel<String> origemModel, DefaultListModel<String> destinoModel) {
        int selecionado = lista.getSelectedIndex();
        if (selecionado != -1) {
            String tarefa = origemModel.getElementAt(selecionado);
            origemModel.remove(selecionado);
            destinoModel.addElement(tarefa);
        } else {
            JOptionPane.showMessageDialog(null, "Selecione uma tarefa para marcar como incompleta.");
        }
    }

    private static void criarObservacao(JList<String> lista, DefaultListModel<String> origemModel, DefaultListModel<String> destinoModel, JFrame frame) {
        int selecionado = lista.getSelectedIndex();
        if (selecionado != -1) {
            String tarefa = origemModel.getElementAt(selecionado);
            String observacao = JOptionPane.showInputDialog(frame, "Digite a observação para esta tarefa:");
            if (observacao != null && !observacao.trim().isEmpty()) {
                destinoModel.addElement(tarefa + " | Observação: " + observacao);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Selecione uma tarefa para adicionar uma observação.");
        }
    }
}


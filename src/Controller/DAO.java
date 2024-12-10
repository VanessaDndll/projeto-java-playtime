/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Chamado;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Adriano
 */
public class DAO {

    // CADASTRAR FUNCIONARIO
    public void cadastrar_funcionario(Chamado chamado) throws ClassNotFoundException, SQLException {
        PreparedStatement ps;
        Connection conexao = new Conexao().getConnection();
        ps = conexao.prepareStatement("insert into funcionario (id_cargo,user_funcionario, nome_funcionario, "
                + "email_funcionario, senha_funcionario, salario_funcionario) values (?,?,?,?,?,?)");

        ps.setInt(1, chamado.getId_cargo());
        ps.setString(2, chamado.getUser_funcionario());
        ps.setString(3, chamado.getNome_funcionario());
        ps.setString(4, chamado.getEmail_funcionario());
        ps.setString(5, chamado.getSenha_funcionario());
        ps.setDouble(6, chamado.getSalario_funcionario());

        ps.execute();
    }

    // AUTENTICAÇÃO DE LOGIN
    public boolean login(String user, String senha) throws SQLException, ClassNotFoundException {
        String sql = "SELECT id_cargo FROM funcionario WHERE user_funcionario = ? AND senha_funcionario = ?";
        try (Connection conexao = new Conexao().getConnection(); PreparedStatement st = conexao.prepareStatement(sql)) {

            st.setString(1, user);
            st.setString(2, senha);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    int idCargo = rs.getInt("id_cargo");

                    if (idCargo == 3) {
                        return true;  // Permite o acesso
                    } else {
                        JOptionPane.showMessageDialog(null, "Acesso restrito: apenas Gerentes podem acessar.");
                        return false;  // Acesso negado
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Usuário ou senha inválidos.");
                    return false;
                }
            }
        }
    }

    // DEMITIR 👾
    public void pesquisar_demitir(String userFuncionario, Chamado chamado) throws SQLException, ClassNotFoundException {
        String sql = "SELECT c.nome_cargo, f.nome_funcionario, f.email_funcionario "
                + "FROM funcionario f "
                + "JOIN cargo c ON f.id_cargo = c.id_cargo "
                + "WHERE f.user_funcionario = ?";

        try (Connection conexao = new Conexao().getConnection(); PreparedStatement st = conexao.prepareStatement(sql)) {

            st.setString(1, userFuncionario);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    chamado.setNome_cargo(rs.getString("nome_cargo"));
                    chamado.setNome_funcionario(rs.getString("nome_funcionario"));
                    chamado.setEmail_funcionario(rs.getString("email_funcionario"));
                } else {
                    JOptionPane.showMessageDialog(null, "Funcionário não encontrado.");
                }
            }
        }
    }

    public void demicao(Chamado chamado) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM funcionario WHERE user_funcionario = ?";
        try (Connection con = new Conexao().getConnection(); PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, chamado.getUser_funcionario());

            int linhasAfetadas = st.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new SQLException("Funcionário não encontrado para exclusão.");
            }
        }
    }

    // UPDATE
    public void pesquisar_update(String userFuncionario, Chamado chamado) throws SQLException, ClassNotFoundException {
        String sql = "SELECT c.nome_cargo, f.nome_funcionario, f.email_funcionario, f.salario_funcionario "
                + "FROM funcionario f "
                + "JOIN cargo c ON f.id_cargo = c.id_cargo "
                + "WHERE f.user_funcionario = ?";

        try (Connection conexao = new Conexao().getConnection(); PreparedStatement st = conexao.prepareStatement(sql)) {

            st.setString(1, userFuncionario);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    chamado.setNome_cargo(rs.getString("nome_cargo"));
                    chamado.setNome_funcionario(rs.getString("nome_funcionario"));
                    chamado.setEmail_funcionario(rs.getString("email_funcionario"));

                    double salario = rs.getDouble("salario_funcionario");
                    chamado.setSalario_funcionario(salario);

                } else {
                    JOptionPane.showMessageDialog(null, "Funcionário não encontrado.");
                }
            }
        }
    }

    public void atualizar_funcionario(Chamado chamado) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE funcionario SET id_cargo = ?, salario_funcionario = ? WHERE user_funcionario = ?";

        try (Connection conexao = new Conexao().getConnection(); PreparedStatement st = conexao.prepareStatement(sql)) {

            st.setInt(1, chamado.getId_cargo());
            st.setDouble(2, chamado.getSalario_funcionario());
            st.setString(3, chamado.getUser_funcionario());

            int linhasAfetadas = st.executeUpdate();

            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(null, "Funcionário atualizado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Funcionário não encontrado para atualização.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar funcionário: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Erro de conexão com o banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // exibir estoque 
    public List<Chamado> listarEstoque() throws SQLException, ClassNotFoundException {
        List<Chamado> itens = new ArrayList<>();
        String sql = "SELECT \n"
                + "    p.nome_produto AS nome, \n"
                + "    e.endereco AS endereco, \n"
                + "    e.quantidade_total AS quantidade,\n"
                + "    p.valor_unitario AS valor \n"
                + "FROM estoque e\n"
                + "JOIN produto p ON e.id_produto = p.id_produto;";

        try (Connection conexao = new Conexao().getConnection(); PreparedStatement st = conexao.prepareStatement(sql); ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Chamado item = new Chamado();
                item.setNome_produto(rs.getString("nome"));
                item.setEndereco(rs.getString("endereco"));
                item.setQuantidade(rs.getInt("quantidade"));
                item.setValor_unitario(rs.getDouble("valor"));
                itens.add(item);
            }
        }

        return itens;
    }

}

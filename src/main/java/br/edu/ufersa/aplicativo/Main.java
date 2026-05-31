package br.edu.ufersa.aplicativo;

import br.edu.ufersa.aplicativo.model.DAO.ProfessorDAO;
import br.edu.ufersa.aplicativo.util.Conexao;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            Connection conexao = Conexao.abrirConexao();
            ProfessorDAO dao = new ProfessorDAO(conexao);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

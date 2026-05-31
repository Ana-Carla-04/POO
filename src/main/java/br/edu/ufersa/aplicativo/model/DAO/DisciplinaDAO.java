package br.edu.ufersa.aplicativo.model.DAO;


import br.edu.ufersa.aplicativo.model.entities.Assunto;
import br.edu.ufersa.aplicativo.model.entities.Disciplina;
import br.edu.ufersa.aplicativo.model.entities.Professor;
import java.sql.*; //import tudo do sql
import java.util.*;

public class DisciplinaDAO implements DAO<Disciplina>{
    //sql querys
    private static final String sql_inserir = "INSERT INTO disciplina (nome, codigo, professor_id) VALUES (?,?,?);";
    private static final String sql_alterar = "UPDATE disciplina SET nome = ?, professor_id = ? WHERE codigo = ?;";
    private static final String sql_deletar = "DELETE FROM disciplina WHERE codigo = ?;";
    private static final String sql_listar = "SELECT * FROM disciplina";

    private static final String sql_buscarPorCodigo = "SELECT * FROM disciplina WHERE codigo = ?;";
    private static final String sql_buscarPorProfessor = "SELECT * FROM disciplina WHERE professor_id = ?;";

    //criar a conexao
    private Connection conexao; //atributo conexao

    //construtor da conexao
    public DisciplinaDAO(Connection conexao){
        this.conexao = conexao;
    }

    @Override //sobrescrita do metodo da interface
    public void inserir(Disciplina disciplina) throws SQLException {
        if (disciplina == null) {
            throw new IllegalArgumentException("Disciplina não pode ser nula");
        }

        //inserindo a disciplina
        PreparedStatement ps = conexao.prepareStatement(sql_inserir);
        ps.setString(1, disciplina.getNome());
        ps.setString(2,disciplina.getCodigo());
        ps.setInt(3,disciplina.getProfessor().getId());
        ps.executeUpdate();
        ps.close();
    }
    
    @Override //sobrecrita do metodo da interface
    public void deletar(Disciplina disciplina) throws SQLException{
        if (disciplina == null) {
            throw new IllegalArgumentException("Disciplina não pode ser nula");
        }
        //deleta o assunto com base no Id da disciplina
        PreparedStatement ps = conexao.prepareStatement(sql_deletar);
        ps.setString(1, disciplina.getCodigo());
        int linhasAfetadas = ps.executeUpdate();
            
        if (linhasAfetadas == 0) {
            throw new SQLException("Disciplina com codigo " + disciplina.getCodigo() + " não encontrada");
        }
    }

    @Override
    public void alterar(Disciplina disciplina) throws SQLException {
        if (disciplina == null) {
            throw new IllegalArgumentException("Disciplina não pode ser nula");
        }
        //atualiza os dados da disciplina
        PreparedStatement ps = conexao.prepareStatement(sql_alterar);
        ps.setString(1, disciplina.getNome());
        ps.setInt(2, disciplina.getProfessor().getId());
        ps.setString(3, disciplina.getCodigo());
        int linhasAfetadas = ps.executeUpdate();
            
        if (linhasAfetadas == 0) {
            throw new SQLException("Disciplina com codigo " + disciplina.getCodigo() + " não encontrada");
        }
    }

    
    @Override //sobrescrita do método da interface
    public List<Disciplina> listar() throws SQLException {
        Statement st = conexao.createStatement();
        ResultSet rs = st.executeQuery(sql_listar);
        List<Disciplina> disciplinas = new ArrayList<>();

        AssuntoDAO assuntoDAO = new AssuntoDAO(conexao);
        while (rs.next()) {
            Disciplina disciplina = criarDisciplinaDoResultSet(rs);
            disciplinas.add(disciplina);
        }
            
        return disciplinas;
    }

    //buscar disciplina por codigo
    public Disciplina buscarPorCodigo(String codigo) throws SQLException {
        PreparedStatement ps = conexao.prepareStatement(sql_buscarPorCodigo);
        ps.setString(1, codigo);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
             return criarDisciplinaDoResultSet(rs);
        }
        return null;
    }

    //buscar disciplina por professor expecífico pelo id dele
    public List<Disciplina> buscarPorProfessor(Professor professor) throws SQLException {
        if (professor == null) {
            throw new IllegalArgumentException("Professor não pode ser nulo");
        }
        
        PreparedStatement ps = conexao.prepareStatement(sql_buscarPorProfessor);
        ps.setInt(1, professor.getId());
            
        ResultSet rs = ps.executeQuery();
        List<Disciplina> disciplinas = new LinkedList<>();
        while (rs.next()) {
            disciplinas.add(criarDisciplinaDoResultSet(rs));
        }
        return disciplinas;
    }

    //método para criar objeto Disciplina a partir do ResultSet
    private Disciplina criarDisciplinaDoResultSet(ResultSet rs) throws SQLException {
        if (rs == null) {
            throw new IllegalArgumentException("ResultSet não pode ser nulo");
        }
        // criando o objeto Professor (que é dono da disciplina)
        Professor professor = new Professor(
            rs.getInt("professor_id"),
            rs.getString("professor_nome"),
            rs.getString("professor_email"),
            rs.getString("professor_senha")
        );
        
        //criando a Disciplina
        Disciplina disciplina = new Disciplina(
            rs.getString("nome"),
            rs.getString("codigo"),
            professor,
            null  // os assuntos serão carregados separadamente
        );
        
        // carregando os assuntos da disciplina
        AssuntoDAO assuntoDAO = new AssuntoDAO(conexao);
        disciplina.setAssuntos(assuntoDAO.listarAssuntosEmDisciplina(disciplina));
        
        return disciplina;
    }
}

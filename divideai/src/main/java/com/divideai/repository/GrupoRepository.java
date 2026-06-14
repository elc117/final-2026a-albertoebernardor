package com.divideai.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.divideai.database.DatabaseConnection;
import com.divideai.model.Grupo;
import com.divideai.model.Usuario;

public class GrupoRepository {

    public Long criar(Grupo grupo){
        String sql = "INSERT INTO grupo (nome, descricao, data_criacao) VALUES (?, ?, ?)";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, grupo.getNome());
            ps.setString(2, grupo.getDescricao());
            ps.setDate(3, java.sql.Date.valueOf(grupo.getDataCriacao()));
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    Long idGerado = rs.getLong("id");
                    grupo.setId(idGerado);
                    return idGerado;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar grupo", e);
        }

        return null;
    }


    public Optional<Grupo> buscarPorId(Long id){
        String sql = "SELECT id, nome, descricao, data_criacao FROM grupo WHERE id = ?";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Grupo grupo = new Grupo(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getDate("data_criacao").toLocalDate()
                    );
                    return Optional.of(grupo);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar grupo por ID", e);
        }

        return Optional.empty();
    }

    public List<Grupo> listar(){
        String sql = "SELECT id, nome, descricao, data_criacao FROM grupo";
        List<Grupo> grupos = new ArrayList<>();

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                grupos.add(new Grupo(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getString("descricao"),
                    rs.getDate("data_criacao").toLocalDate()
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar grupos", e);
        }

        return grupos;
    }

    public boolean atualizar(Grupo grupo) {
        String sql = "UPDATE grupo SET nome = ?, descricao = ? WHERE id = ?";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, grupo.getNome());
            ps.setString(2, grupo.getDescricao());
            ps.setLong(3, grupo.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar grupo", e);
        }
    }

    public boolean deletar(Long id) {
        String sql = "DELETE FROM grupo WHERE id = ?";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar grupo", e);
        }
    }

    public List<Grupo> listarPorUsuario(Long usuarioId) {
        String sql = "SELECT g.id, g.nome, g.descricao, g.data_criacao"
                   + " FROM grupo g"
                   + " JOIN grupo_usuario gu ON gu.grupo_id = g.id"
                   + " WHERE gu.usuario_id = ?";
        List<Grupo> grupos = new ArrayList<>();

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setLong(1, usuarioId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    grupos.add(new Grupo(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getDate("data_criacao").toLocalDate()
                    ));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar grupos do usuário", e);
        }

        return grupos;
    }

    public void adicionarMembro(Long grupoId, Long usuarioId) {
        String sql = "INSERT INTO grupo_usuario (grupo_id, usuario_id, data_entrada) VALUES (?, ?, ?)";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setLong(1, grupoId);
            ps.setLong(2, usuarioId);
            ps.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar membro ao grupo", e);
        }
    }

    public boolean removerMembro(Long grupoId, Long usuarioId) {
        String sql = "DELETE FROM grupo_usuario WHERE grupo_id = ? AND usuario_id = ?";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setLong(1, grupoId);
            ps.setLong(2, usuarioId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover membro do grupo", e);
        }
    }

    public List<Usuario> listarMembros(Long grupoId) {
        String sql = "SELECT u.id, u.nome, u.email, u.senha_hash"
                   + " FROM usuario u"
                   + " JOIN grupo_usuario gu ON gu.usuario_id = u.id"
                   + " WHERE gu.grupo_id = ?";
        List<Usuario> membros = new ArrayList<>();

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setLong(1, grupoId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    membros.add(new Usuario(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha_hash")
                    ));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar membros do grupo", e);
        }

        return membros;
    }
}

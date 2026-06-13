package com.divideai.repository;

import com.divideai.database.DatabaseConnection;
import com.divideai.model.Usuario;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;


public class UsuarioRepository {

    public Long criar(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, email, senha_hash) VALUES (?, ?, ?)";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getSenhaHash());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    Long idGerado = rs.getLong("id");
                    usuario.setId(idGerado);
                    return idGerado;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar usuário", e);
        }

        return null;
    }

   
    public Optional<Usuario> buscarPorId(Long id) {
        String sql = "SELECT id, nome, email, senha_hash FROM usuario WHERE id = ?";
        
        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Long usuarioId = rs.getLong("id");
                    String nome = rs.getString("nome");
                    String email = rs.getString("email");
                    String senhaHash = rs.getString("senha_hash");

                    Usuario usuario = new Usuario(usuarioId, nome, email, senhaHash);
                    return Optional.of(usuario);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por ID", e);
        }
        
        return Optional.empty();
    }

  
    public Optional<Usuario> buscarPorEmail(String email) {
        String sql = "SELECT id, nome, email, senha_hash FROM usuario WHERE email = ?";
        
        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Long usuarioId = rs.getLong("id");
                    String nome = rs.getString("nome");
                    String senhaHash = rs.getString("senha_hash");

                    Usuario usuario = new Usuario(usuarioId, nome, email, senhaHash);
                    return Optional.of(usuario);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por email", e);
        }
        
        return Optional.empty();
    }

  
    public List<Usuario> listar() {
        String sql = "SELECT id, nome, email, senha_hash FROM usuario";
        List<Usuario> usuarios = new ArrayList<>();

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Long usuarioId = rs.getLong("id");
                String nome = rs.getString("nome");
                String email = rs.getString("email");
                String senhaHash = rs.getString("senha_hash");

                Usuario usuario = new Usuario(usuarioId, nome, email, senhaHash);
                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários", e);
        }
        
        return usuarios;
    }

  
    public boolean atualizar(Usuario usuario) {
        String sql = "UPDATE usuario SET nome = ?, email = ?, senha_hash = ? WHERE id = ?";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getSenhaHash());
            ps.setLong(4, usuario.getId());

            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário", e);
        }
    }

 
    public boolean deletar(Long id) {
        String sql = "DELETE FROM usuario WHERE id = ?";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setLong(1, id);

            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar usuário", e);
        }
    }
}
    

   
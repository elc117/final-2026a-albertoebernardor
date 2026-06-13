package com.divideai.repository;

import com.divideai.model.Despesa;
import com.divideai.model.DivisaoDespesa;
import com.divideai.model.Categoria;
import com.divideai.model.TipoDivisao;
import com.divideai.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class DespesaRepository {

    public List<Despesa> listarPorGrupo(Long grupoId) {
        String sql = "SELECT * FROM despesa WHERE grupo_id = ?";
        List<Despesa> lista = new ArrayList<>();

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setLong(1, grupoId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Long id = rs.getLong("id");
                        Long grupo_id = rs.getLong("grupo_id");
                        Long pagador_id = rs.getLong("pagador_id");
                        String descricao = rs.getString("descricao");
                        BigDecimal valor = rs.getBigDecimal("valor");
                        LocalDate data = rs.getDate("data").toLocalDate();
                        Categoria categoria = Categoria.valueOf(rs.getString("categoria"));
                        TipoDivisao tipoDivisao = TipoDivisao.valueOf(rs.getString("tipo_divisao"));

                        Despesa obj = new Despesa(id, grupo_id, pagador_id, descricao, valor, data, categoria, tipoDivisao);

                        lista.add(obj);
                    }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    public List<DivisaoDespesa> listarDivisoesPorGrupo(long grupoId) {
        String sql = """
            SELECT dd.*
            FROM divisao_despesa dd
            JOIN despesa d ON d.id = dd.despesa_id
            WHERE d.grupo_id = ?
            """;

        List<DivisaoDespesa> lista = new ArrayList<>();

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setLong(1, grupoId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Long id = rs.getLong("id");
                        Long despesa_id = rs.getLong("despesa_id");
                        Long usuario_id = rs.getLong("usuario_id");
                        BigDecimal valor_devido = rs.getBigDecimal("valor_devido");
                        Boolean quitada = rs.getBoolean("quitada");

                        DivisaoDespesa obj = new DivisaoDespesa(id, despesa_id, usuario_id, valor_devido, quitada);

                        lista.add(obj);
                    }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    public List<DivisaoDespesa> listarDivisoesPorDespesa(long despesaId) {
        String sql =  "SELECT * FROM divisao_despesa WHERE despesa_id = ?";

        List<DivisaoDespesa> lista = new ArrayList<>();

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setLong(1, despesaId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Long id = rs.getLong("id");
                        Long despesa_id = rs.getLong("despesa_id");
                        Long usuario_id = rs.getLong("usuario_id");
                        BigDecimal valor_devido = rs.getBigDecimal("valor_devido");
                        Boolean quitada = rs.getBoolean("quitada");

                        DivisaoDespesa obj = new DivisaoDespesa(id, despesa_id, usuario_id, valor_devido, quitada);

                        lista.add(obj);
                    }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }
}
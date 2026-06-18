package com.divideai.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.time.LocalDate;

import com.divideai.repository.DespesaRepository;
import com.divideai.repository.GrupoRepository;
import com.divideai.repository.UsuarioRepository;
import com.divideai.model.Grupo;
import com.divideai.model.TipoDivisao;
import com.divideai.model.Usuario;
import com.divideai.model.Categoria;
import com.divideai.model.Despesa;
import com.divideai.model.DivisaoDespesa;
import com.divideai.dto.DespesaRequest;

public class DespesaService {
    private final GrupoRepository grupoRepository;
    private final DespesaRepository despesaRepository;
    private final UsuarioRepository usuarioRepository;

    public DespesaService(GrupoRepository grupoRepository, DespesaRepository despesaRepository, UsuarioRepository usuarioRepository) {
        this.grupoRepository = grupoRepository;
        this.despesaRepository = despesaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    private boolean ehParticipante (Grupo grupo, Usuario usuario){
        List<Usuario> list = grupoRepository.listarMembros(grupo.getId());
        Long id = usuario.getId();
        for(Usuario usuarios : list){
            if(usuarios.getId().equals(id)){
                return true;
            }
        }
        return false;
    }

    public Despesa registrar(Long grupoId, DespesaRequest request){
        Grupo grupo = grupoRepository.buscarPorId(grupoId)
                .orElseThrow(() -> new NoSuchElementException("Grupo nao encontrado"));

        Usuario pagador = usuarioRepository.buscarPorId(request.pagadorId())
                .orElseThrow(() -> new NoSuchElementException("Pagador não encontrado"));

        if (!ehParticipante(grupo, pagador)) {
            throw new IllegalArgumentException("Pagador não é participante do grupo");  
        } 
        
        List<Usuario> listUsuarios = grupoRepository.listarMembros(grupoId);
        int n = listUsuarios.size();

        BigDecimal valor = request.valor();
        BigDecimal cota = valor.divide(BigDecimal.valueOf(n), 2, RoundingMode.DOWN);
        BigDecimal totalDistribuido = cota.multiply(BigDecimal.valueOf(n));
        BigDecimal resto = valor.subtract(totalDistribuido);
        int qtdComCentavoExtra = resto.divide(new BigDecimal("0.01")).intValue();

        List<DivisaoDespesa> listDivisao = new ArrayList<>();

        for (int i = 0; i < listUsuarios.size(); i++) {
            Usuario participante = listUsuarios.get(i);
            
            BigDecimal valorDevido;
            if (i < qtdComCentavoExtra) {
                valorDevido = cota.add(new BigDecimal("0.01")); 
            } else {
                valorDevido = cota; 
            }

            DivisaoDespesa divisao = new DivisaoDespesa(null, null, participante.getId(), valorDevido);
            listDivisao.add(divisao);
        }
        
        Long pagadorId = pagador.getId();
        String descricao = request.descricao();
        LocalDate data = request.data();
        Categoria categoria = Categoria.valueOf(request.categoria());
        TipoDivisao tipoDivisao = TipoDivisao.valueOf(request.tipoDivisao());
        
        Despesa despesa = new Despesa(grupoId, pagadorId, descricao, valor, data, categoria, tipoDivisao);

        despesaRepository.salvar(despesa, listDivisao);
        return despesa;
    }

    public List<Despesa> historico(Long grupoId) {
        return despesaRepository.listarPorGrupo(grupoId);
    }
}

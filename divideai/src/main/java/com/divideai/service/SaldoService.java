package com.divideai.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.divideai.repository.DespesaRepository;
import com.divideai.repository.GrupoRepository;
import com.divideai.model.Despesa;
import com.divideai.model.DivisaoDespesa;
import com.divideai.model.Grupo;
import com.divideai.model.Usuario;
import com.divideai.dto.Debito;
import com.divideai.dto.ResumoGrupo;

public class SaldoService {
    private final GrupoRepository grupoRepository;
    private final DespesaRepository despesaRepository;

    public SaldoService (GrupoRepository grupoRepository, DespesaRepository despesaRepository){
        this.grupoRepository = grupoRepository;
        this.despesaRepository = despesaRepository;
    }

    public Map<Long, BigDecimal> calcularSaldos(Long grupoId) {
        // Buscar todas as despesas do grupo
        List<Despesa> listDespesas = despesaRepository.listarPorGrupo(grupoId);
        
        // Para cada despesa, somar o valor ao saldo do pagador
        Map<Long, BigDecimal> saldos = new HashMap<>();

        for(Despesa despesas : listDespesas){

            Long usuarioId = despesas.getPagadorId();
            BigDecimal valor = despesas.getValor();

            BigDecimal saldoAtual = saldos.getOrDefault(usuarioId, BigDecimal.ZERO);
            saldos.put(usuarioId, saldoAtual.add(valor));
        }

        // Buscar todas as divisões do grupo
        List<DivisaoDespesa> listDivisaoDespesas = despesaRepository.listarDivisoesPorGrupo(grupoId);

        // Para cada divisão, subtrair o valorDevido do saldo do usuário
        for(DivisaoDespesa divisoes : listDivisaoDespesas){
            if(divisoes.getQuitada())
                continue;

            Long usuarioId = divisoes.getUsuarioId();
            BigDecimal valor = divisoes.getValorDevido();

            BigDecimal saldoAtual = saldos.getOrDefault(usuarioId, BigDecimal.ZERO);
            saldos.put(usuarioId, saldoAtual.subtract(valor));
        }
        
        // Retornar o Map com os saldos
        return saldos;
    }

    public List<Debito> calcularDebitos(Long grupoId){
        // Separar os participantes em dois grupos: credores e devedores
        Map<Long, BigDecimal> saldos = calcularSaldos(grupoId);
        List<Usuario> participantes = grupoRepository.listarMembros(grupoId);

        List<Usuario> credores = new ArrayList<>();
        List<Usuario> devedores = new ArrayList<>();

        for(Usuario participante : participantes){
            Long id = participante.getId();

            BigDecimal valor = saldos.getOrDefault(id, BigDecimal.ZERO);

            if(valor.compareTo(BigDecimal.ZERO) > 0) credores.add(participante);
            if(valor.compareTo(BigDecimal.ZERO) < 0) devedores.add(participante);
        }

        Map<Long, BigDecimal> saldosCredores = new HashMap<>();
        Map<Long, BigDecimal> saldosDevedores = new HashMap<>();

        for(Usuario credor : credores){
            saldosCredores.put(credor.getId(), saldos.get(credor.getId()));
        }

        for(Usuario devedor : devedores){
            saldosDevedores.put(devedor.getId(), saldos.get(devedor.getId()).abs());;
        }
        
        List<Debito> debitos = new ArrayList<>();

        while (!credores.isEmpty() && !devedores.isEmpty()) {
            // pega o maior credor e maior devedor
            Usuario maiorCredor = Collections.max(credores, 
                (a, b) -> saldosCredores.get(a.getId()).compareTo(saldosCredores.get(b.getId()))
            );

            Usuario maiorDevedor = Collections.max(devedores,
                (a, b) -> saldosDevedores.get(a.getId()).compareTo(saldosDevedores.get(b.getId()))
            );

            // gera o debito
            BigDecimal saldoDevedor = saldosDevedores.get(maiorDevedor.getId());
            BigDecimal saldoCredor = saldosCredores.get(maiorCredor.getId());
            BigDecimal valorDebito = saldoDevedor.min(saldoCredor);

            Debito debito = new Debito(
                maiorDevedor.getId(),
                maiorDevedor.getNome(), 
                maiorCredor.getId(), 
                maiorCredor.getNome(), 
                valorDebito
            );

            debitos.add(debito);
            
            // abate os saldos
            saldosDevedores.put(maiorDevedor.getId(), saldoDevedor.subtract(valorDebito));
            saldosCredores.put(maiorCredor.getId(), saldoCredor.subtract(valorDebito));

            // remove quem zerou
            BigDecimal valorMaiorDevedor = saldosDevedores.get(maiorDevedor.getId());
            if(valorMaiorDevedor.compareTo(BigDecimal.ZERO) == 0)
                devedores.remove(maiorDevedor);

            BigDecimal valorMenorDevedor = saldosCredores.get(maiorDevedor.getId());
            if(valorMenorDevedor.compareTo(BigDecimal.ZERO) == 0)
                credores.remove(maiorCredor);
        }

        return debitos;
    }

    public ResumoGrupo montarResumo(Long grupoId){
        // Buscar o grupo 
        Grupo grupo = grupoRepository.buscarPorId(grupoId)
            .orElseThrow(() -> new NoSuchElementException("Grupo não encontrado"));
        
        // Buscar os participantes
        List<Usuario> participantes = grupoRepository.listarMembros(grupoId);

        // Buscar as despesas com despesaRepository.listarPorGrupo(grupoId)
        List<Despesa> despesas = despesaRepository.listarPorGrupo(grupoId);

        // Somar o total gasto percorrendo a lista de despesas com um BigDecimal acumulador
        BigDecimal acumulador = BigDecimal.ZERO;
        for(Despesa despesa : despesas){
            acumulador = acumulador.add(despesa.getValor());
        }
        
        // Chamar calcularSaldos(grupoId) e converter as chaves de Long (id) para String (nome do usuário)
        Map<Long, BigDecimal> saldos = calcularSaldos(grupoId);
        Map<Long, String> nomesPorId = new HashMap<>();
        
        for (Usuario participante : participantes) {
            nomesPorId.put(participante.getId(), participante.getNome());
        }

        Map<String, BigDecimal> saldoPorNome = new HashMap<>();
        for (Map.Entry<Long, BigDecimal> entry : saldos.entrySet()) {
            Long id = entry.getKey();
            BigDecimal saldo = entry.getValue();
            String nome = nomesPorId.get(id);
            saldoPorNome.put(nome, saldo);
        }

        // Chamar calcularDebitos(grupoId)
        List<Debito> debitos = calcularDebitos(grupoId);

        // Montar e retornar o ResumoGrupo
        return new ResumoGrupo(
            grupoId, 
            grupo.getNome(), 
            acumulador, 
            saldoPorNome, 
            debitos
        );
    }
}

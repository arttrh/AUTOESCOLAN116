package br.com.senain116.autoescolan116.application.core.usecase;

import br.com.senain116.autoescolan116.adapter.in.controller.mapper.EnderecoMapper;
import br.com.senain116.autoescolan116.adapter.in.controller.mapper.InstrutorMapper;
import br.com.senain116.autoescolan116.adapter.in.controller.request.instrutor.DadosAtualizacaoInstrutor;
import br.com.senain116.autoescolan116.adapter.in.controller.request.instrutor.DadosCadastroInstrutor;
import br.com.senain116.autoescolan116.adapter.in.controller.response.instrutor.DadosDetalhamentoInstrutor;
import br.com.senain116.autoescolan116.adapter.in.controller.response.instrutor.DadosListagemInstrutor;
import br.com.senain116.autoescolan116.application.core.domain.model.Instrutor;
import br.com.senain116.autoescolan116.application.port.out.InstrutorRepository;
import br.com.senain116.autoescolan116.exception.type.instrutor.InstrutorNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class InstrutorService {
    private final InstrutorRepository repository;
    private final InstrutorMapper mapper;
    private final EnderecoMapper enderecoMapper;

    public InstrutorService(
            InstrutorRepository repository,
            InstrutorMapper mapper,
            EnderecoMapper enderecoMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.enderecoMapper = enderecoMapper;
    }

    @Transactional
    public DadosDetalhamentoInstrutor cadastrarInstrutor(DadosCadastroInstrutor dados) {
        Instrutor instrutor = mapper.toDomain(dados);
        Instrutor saved = repository.save(instrutor);
        return mapper.toDetailsDTO(saved);
    }


    public Page<DadosListagemInstrutor> listarInstrutores(Pageable paginacao) {
        return repository
                .findAllByAtivoTrue(paginacao)
                .map(mapper::toListDTO);
    }

    @Cacheable(value = "instrutores", key = "#id")
    public DadosDetalhamentoInstrutor detalharInstrutor(Long id) {
        log.info("Consultando no banco de dados");
        Instrutor instrutor = repository.findById(id)
                .orElseThrow(() ->
                        new InstrutorNotFoundException("ID do instrutor informado não existe!"));
        return mapper.toDetailsDTO(instrutor);
    }

    @Transactional
    @CachePut(value = "instrutores", key = "#dados.id()")
    public DadosDetalhamentoInstrutor atualizarInstrutor(DadosAtualizacaoInstrutor dados) {
        Instrutor instrutor = repository.findById(dados.id())
                .orElseThrow(() ->
                        new InstrutorNotFoundException("ID do instrutor informado não existe!"));
        instrutor.atualizarInformacoes(
                dados.nome(),
                dados.email(),
                dados.telefone(),
                dados.especialidade(),
                enderecoMapper.toDomain(dados.endereco())
        );
        Instrutor saved = repository.save(instrutor);
        return mapper.toDetailsDTO(saved);
    }

    @Transactional
    @CacheEvict(value = "instrutores", key = "#id")
    public void excluirInstrutor(Long id) {
        Instrutor instrutor = repository.findById(id)
                .orElseThrow(() ->
                        new InstrutorNotFoundException("ID do instrutor informado não existe!"));
        instrutor.excluir();
        repository.save(instrutor);
    }
}
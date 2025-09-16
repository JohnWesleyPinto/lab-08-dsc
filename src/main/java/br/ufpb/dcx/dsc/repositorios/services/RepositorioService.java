package br.ufpb.dcx.dsc.repositorios.services;

import br.ufpb.dcx.dsc.repositorios.exceptions.BusinessException;
import br.ufpb.dcx.dsc.repositorios.exceptions.ResourceNotFoundException;
import br.ufpb.dcx.dsc.repositorios.models.Organizacao;
import br.ufpb.dcx.dsc.repositorios.models.Repositorio;
import br.ufpb.dcx.dsc.repositorios.repository.OrganizacaoRepository;
import br.ufpb.dcx.dsc.repositorios.repository.RepositorioRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RepositorioService {

    private RepositorioRepository repositorioRepository;
    private OrganizacaoRepository organizacaoRepository;
    RepositorioService(RepositorioRepository repositorioRepository, OrganizacaoRepository organizacaoRepository){
        this.repositorioRepository=repositorioRepository;
        this.organizacaoRepository = organizacaoRepository;
    }
    public Repositorio getRepositorio(Long id){
        if (id == null) {
            throw new BusinessException("O identificador do repositório não pode ser nulo.");
        }
        return repositorioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Repositório não encontrado para o id " + id + "."));
    }

    public List<Repositorio> listRepositorios() {
        return repositorioRepository.findAll();
    }

    public Repositorio saveRepositorio(Repositorio r, Long orgId) {
        if (r == null) {
            throw new BusinessException("Os dados do repositório são obrigatórios.");
        }
        if (orgId == null) {
            throw new BusinessException("O identificador da organização é obrigatório.");
        }
        Optional<Organizacao> oOpt = organizacaoRepository.findById(orgId);
        if(oOpt.isPresent()){
            r.setOrganizacao(oOpt.get());
            return repositorioRepository.save(r);
        }
        throw new ResourceNotFoundException("Organização não encontrada para o id " + orgId + ".");

    }

    public void deleteRepositorio(Long id) {if (id == null) {
        throw new BusinessException("O identificador do repositório não pode ser nulo.");
    }
        if (!repositorioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Repositório não encontrado para o id " + id + ".");
        }
        repositorioRepository.deleteById(id);
    }

    public Repositorio updateRepositorio(Long id, Repositorio rUpdated) {
        if (id == null) {
            throw new BusinessException("O identificador do repositório não pode ser nulo.");
        }
        if (rUpdated == null) {
            throw new BusinessException("Os dados do repositório são obrigatórios.");
        }
        Optional<Repositorio> r = repositorioRepository.findById(id);
        if(r.isPresent()){
            Repositorio toUpdate = r.get();
            toUpdate.setIsPrivate(rUpdated.getIsPrivate());
            toUpdate.setNome(rUpdated.getNome());
            return repositorioRepository.save(toUpdate);
        }
        throw new ResourceNotFoundException("Repositório não encontrado para o id " + id + ".");
    }
}

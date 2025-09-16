package br.ufpb.dcx.dsc.repositorios.services;

import br.ufpb.dcx.dsc.repositorios.exceptions.BusinessException;
import br.ufpb.dcx.dsc.repositorios.exceptions.ResourceNotFoundException;
import br.ufpb.dcx.dsc.repositorios.models.Organizacao;
import br.ufpb.dcx.dsc.repositorios.models.User;
import br.ufpb.dcx.dsc.repositorios.repository.OrganizacaoRepository;
import br.ufpb.dcx.dsc.repositorios.repository.UserRepository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class OrganizacaoService {
    private OrganizacaoRepository organizacaoRepository;
    private UserRepository userRepository;
    public OrganizacaoService( UserRepository userRepository, OrganizacaoRepository organizacaoRepository){
        this.organizacaoRepository = organizacaoRepository;
        this.userRepository = userRepository;
    }

    public List<Organizacao> listOrganizacoes() {
        return organizacaoRepository.findAll();
    }

    public Organizacao getOrganizacao(Long orgId) {
        if(orgId == null){
            throw new BusinessException("O identificador da organização não pode ser nulo.");
        }
        return organizacaoRepository.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organização não encontrada para o id " + orgId + "."));
    }

    public Organizacao createOrganizacao(Organizacao org, Long userId){
        if (org == null) {
            throw new BusinessException("Os dados da organização são obrigatórios.");
        }
        if (userId == null) {
            throw new BusinessException("O identificador do usuário é obrigatório.");
        }
        Optional<User> uOpt = userRepository.findById(userId);
        if(uOpt.isPresent()){
            Organizacao o = organizacaoRepository.save(org);
            User u = uOpt.get();
            u.getOrganizacaos().add(org);
            userRepository.save(u);
            return o;
        }
        throw new ResourceNotFoundException("Usuário não encontrado para o id " + userId + ".");
    }

    public Organizacao updateOrganizacao(Long orgId, Organizacao o) {
        if (orgId == null) {
            throw new BusinessException("O identificador da organização não pode ser nulo.");
        }
        if (o == null) {
            throw new BusinessException("Os dados da organização são obrigatórios.");
        }
        Optional<Organizacao> orgOpt = organizacaoRepository.findById(orgId);
        if(orgOpt.isPresent()){
            Organizacao org = orgOpt.get();
            org.setNome(o.getNome());
            return organizacaoRepository.save(org);
        }
        throw new ResourceNotFoundException("Organização não encontrada para o id " + orgId + ".");
    }

    public void deleteOrganizacao(Long orgId) {
        if (orgId == null) {
            throw new BusinessException("O identificador da organização não pode ser nulo.");
        }
        Optional<Organizacao> oOpt = organizacaoRepository.findById(orgId);
        if(oOpt.isEmpty()){
            throw new ResourceNotFoundException("Organização não encontrada para o id " + orgId + ".");
        }
        Organizacao o = oOpt.get();

        o.getUsers().forEach( user -> {
            user.getOrganizacaos().remove(o);
            userRepository.save(user);
        });
        o.getUsers().clear();
        organizacaoRepository.delete(o);

    }
}

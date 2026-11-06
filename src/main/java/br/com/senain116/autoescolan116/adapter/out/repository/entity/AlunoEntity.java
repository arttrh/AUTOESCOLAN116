package br.com.senain116.autoescolan116.adapter.out.repository.entity;

import br.com.senain116.autoescolan116.application.core.domain.vo.Endereco;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Aluno")
@Table(name = "alunos")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class AlunoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String cpf;

    @Embedded
    private Endereco endereco;
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.TINYINT)
    private boolean ativo = true;
}
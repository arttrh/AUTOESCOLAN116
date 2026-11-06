package br.com.senain116.autoescolan116.adapter.out.repository.entity;

import br.com.senain116.autoescolan116.application.core.domain.enums.Especialidade;
import br.com.senain116.autoescolan116.application.core.domain.vo.Endereco;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Instrutor")
@Table(name = "instrutores")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class InstrutorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String cnh;

    @Enumerated(EnumType.STRING)
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.VARCHAR)
    private Especialidade especialidade;

    @Embedded
    private Endereco endereco;
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.TINYINT)
    private boolean ativo = true;
}
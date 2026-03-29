package com.example.demo.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@ToString
public class Produto {

    private int id;
    private String nome;
    private String descricao;
    private double preco;
    private int quantidadeEstoque;
    private String categoria;
    private boolean ativo;

    public Produto() {}

    public Produto(int id, String nome, String descricao,
                   double preco, int quantidadeEstoque,
                   String categoria, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
        this.categoria = categoria;
        this.ativo = ativo;
    }
}

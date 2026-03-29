package com.example.demo.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class ProdutoResponse {

    private boolean sucesso;
    private String mensagem;
    private Produto produto;

    public ProdutoResponse() {}

    public ProdutoResponse(boolean sucesso, String mensagem, Produto produto) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
        this.produto = produto;
    }

    public ProdutoResponse(boolean sucesso, String mensagem) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
    }
}
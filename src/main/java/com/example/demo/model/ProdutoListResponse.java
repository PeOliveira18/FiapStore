package com.example.demo.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class ProdutoListResponse {

    private boolean sucesso;
    private String mensagem;
    private List<Produto> produtos;
    private int total;

    public ProdutoListResponse() {}

    public ProdutoListResponse(boolean sucesso, String mensagem, List<Produto> produtos) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
        this.produtos = produtos;
        this.total = produtos != null ? produtos.size() : 0;
    }
}

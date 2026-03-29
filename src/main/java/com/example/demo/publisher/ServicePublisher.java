package com.example.demo.publisher;

import com.example.demo.service.ProdutoService;
import jakarta.xml.ws.Endpoint;

public class ServicePublisher {

    public static void main(String[] args) {

        String url = "http://localhost:8080/produtos";

        System.out.println("===========================================");
        System.out.println("   FIAP Store - Produto WebService SOAP   ");
        System.out.println("===========================================");
        System.out.println("Iniciando publicacao do servico...");

        Endpoint.publish(url, new ProdutoService());

        System.out.println();
        System.out.println("[OK] WebService SOAP publicado com sucesso!");
        System.out.println();
        System.out.println("Endpoint : " + url);
        System.out.println("WSDL     : " + url + "?wsdl");
        System.out.println();
        System.out.println("Operacoes disponiveis:");
        System.out.println("  - listarProdutos()");
        System.out.println("  - buscarProdutoPorId(id)");
        System.out.println("  - buscarPorCategoria(categoria)");
        System.out.println("  - cadastrarProduto(nome, descricao, preco, quantidadeEstoque, categoria)");
        System.out.println("  - atualizarPreco(id, novoPreco)");
        System.out.println("  - atualizarEstoque(id, novaQuantidade)");
        System.out.println("  - desativarProduto(id)");
        System.out.println();
        System.out.println("Aguardando requisicoes... (Ctrl+C para parar)");
        System.out.println("===========================================");
    }
}
package com.example.demo.service;

import com.example.demo.model.Produto;
import com.example.demo.model.ProdutoListResponse;
import com.example.demo.model.ProdutoResponse;
import com.example.demo.repository.ProdutoRepository;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

import java.util.List;
import java.util.Optional;

@WebService(
        serviceName = "ProdutoService",
        portName = "ProdutoPort",
        targetNamespace = "http://service.soap.fiap.com.br/"
)
public class ProdutoService {

    private final ProdutoRepository repository = new ProdutoRepository();

    @WebMethod(operationName = "listarProdutos")
    public ProdutoListResponse listarProdutos() {
        List<Produto> produtos = repository.listarAtivos();
        return new ProdutoListResponse(true,
                "Produtos listados com sucesso. Total: " + produtos.size(),
                produtos);
    }

    @WebMethod(operationName = "buscarProdutoPorId")
    public ProdutoResponse buscarProdutoPorId(
            @WebParam(name = "id") int id) {

        Optional<Produto> produto = repository.buscarPorId(id);

        if (produto.isPresent()) {
            return new ProdutoResponse(true,
                    "Produto encontrado com sucesso.",
                    produto.get());
        }

        return new ProdutoResponse(false,
                "Produto com ID " + id + " nao encontrado.");
    }

    @WebMethod(operationName = "buscarPorCategoria")
    public ProdutoListResponse buscarPorCategoria(
            @WebParam(name = "categoria") String categoria) {

        if (categoria == null || categoria.trim().isEmpty()) {
            return new ProdutoListResponse(false,
                    "Categoria nao pode ser vazia.",
                    null);
        }

        List<Produto> produtos = repository.buscarPorCategoria(categoria);

        String mensagem = produtos.isEmpty()
                ? "Nenhum produto encontrado na categoria: " + categoria
                : "Produtos da categoria '" + categoria + "' listados. Total: " + produtos.size();

        return new ProdutoListResponse(true, mensagem, produtos);
    }

    @WebMethod(operationName = "cadastrarProduto")
    public ProdutoResponse cadastrarProduto(
            @WebParam(name = "nome")              String nome,
            @WebParam(name = "descricao")         String descricao,
            @WebParam(name = "preco")             double preco,
            @WebParam(name = "quantidadeEstoque") int quantidadeEstoque,
            @WebParam(name = "categoria")         String categoria) {

        if (nome == null || nome.trim().isEmpty()) {
            return new ProdutoResponse(false, "Nome do produto e obrigatorio.");
        }
        if (preco <= 0) {
            return new ProdutoResponse(false,
                    "Preco deve ser maior que zero. Valor informado: " + preco);
        }
        if (quantidadeEstoque < 0) {
            return new ProdutoResponse(false,
                    "Quantidade em estoque nao pode ser negativa.");
        }
        if (categoria == null || categoria.trim().isEmpty()) {
            return new ProdutoResponse(false, "Categoria e obrigatoria.");
        }

        Produto novoProduto = new Produto(0, nome.trim(), descricao,
                preco, quantidadeEstoque, categoria.trim(), true);

        Produto produtoCriado = repository.cadastrar(novoProduto);

        return new ProdutoResponse(true,
                "Produto '" + produtoCriado.getNome() + "' cadastrado com sucesso! ID: " + produtoCriado.getId(),
                produtoCriado);
    }

    @WebMethod(operationName = "atualizarPreco")
    public ProdutoResponse atualizarPreco(
            @WebParam(name = "id")       int id,
            @WebParam(name = "novoPreco") double novoPreco) {

        if (novoPreco <= 0) {
            return new ProdutoResponse(false,
                    "Novo preco deve ser maior que zero.");
        }

        Optional<Produto> optProduto = repository.buscarPorId(id);

        if (optProduto.isEmpty()) {
            return new ProdutoResponse(false,
                    "Produto com ID " + id + " nao encontrado.");
        }

        Produto produto = optProduto.get();

        if (!produto.isAtivo()) {
            return new ProdutoResponse(false,
                    "Nao e possivel atualizar preco de produto inativo.");
        }

        double precoAnterior = produto.getPreco();
        produto.setPreco(novoPreco);
        repository.atualizar(produto);

        return new ProdutoResponse(true,
                "Preco atualizado de R$" + precoAnterior + " para R$" + novoPreco,
                produto);
    }

    @WebMethod(operationName = "atualizarEstoque")
    public ProdutoResponse atualizarEstoque(
            @WebParam(name = "id")             int id,
            @WebParam(name = "novaQuantidade") int novaQuantidade) {

        if (novaQuantidade < 0) {
            return new ProdutoResponse(false,
                    "Quantidade em estoque nao pode ser negativa.");
        }

        Optional<Produto> optProduto = repository.buscarPorId(id);

        if (optProduto.isEmpty()) {
            return new ProdutoResponse(false,
                    "Produto com ID " + id + " nao encontrado.");
        }

        Produto produto = optProduto.get();

        if (!produto.isAtivo()) {
            return new ProdutoResponse(false,
                    "Nao e possivel atualizar estoque de produto inativo.");
        }

        int estoqueAnterior = produto.getQuantidadeEstoque();
        repository.atualizarEstoque(id, novaQuantidade);
        produto.setQuantidadeEstoque(novaQuantidade);

        return new ProdutoResponse(true,
                "Estoque atualizado de " + estoqueAnterior + " para " + novaQuantidade + " unidades.",
                produto);
    }

    @WebMethod(operationName = "desativarProduto")
    public ProdutoResponse desativarProduto(
            @WebParam(name = "id") int id) {

        Optional<Produto> optProduto = repository.buscarPorId(id);

        if (optProduto.isEmpty()) {
            return new ProdutoResponse(false,
                    "Produto com ID " + id + " nao encontrado.");
        }

        Produto produto = optProduto.get();

        if (!produto.isAtivo()) {
            return new ProdutoResponse(false,
                    "Produto com ID " + id + " ja esta inativo.");
        }

        repository.desativar(id);
        produto.setAtivo(false);

        return new ProdutoResponse(true,
                "Produto '" + produto.getNome() + "' desativado com sucesso.",
                produto);
    }
}
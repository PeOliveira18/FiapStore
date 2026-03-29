package com.example.demo.repository;

import com.example.demo.model.Produto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class ProdutoRepository {

    private static final AtomicInteger contadorId = new AtomicInteger(4);

    private static final List<Produto> banco = new ArrayList<>();

    static {
        banco.add(new Produto(1, "Notebook Dell Inspiron 15",
                "Notebook com processador Intel i7, 16GB RAM, SSD 512GB",
                4599.99, 15, "Informatica", true));

        banco.add(new Produto(2, "Mouse Logitech MX Master 3",
                "Mouse sem fio ergonomico, 4000 DPI, conexao USB-C",
                399.90, 42, "Perifericos", true));

        banco.add(new Produto(3, "Monitor LG 27 4K",
                "Monitor 4K UHD 27 polegadas, IPS, 60Hz, HDMI/DisplayPort",
                1899.00, 8, "Monitores", true));
    }

    public List<Produto> listarTodos() {
        return new ArrayList<>(banco);
    }

    public List<Produto> listarAtivos() {
        List<Produto> ativos = new ArrayList<>();
        for (Produto p : banco) {
            if (p.isAtivo()) {
                ativos.add(p);
            }
        }
        return ativos;
    }

    public Optional<Produto> buscarPorId(int id) {
        for (Produto p : banco) {
            if (p.getId() == id) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    public List<Produto> buscarPorCategoria(String categoria) {
        List<Produto> resultado = new ArrayList<>();
        for (Produto p : banco) {
            if (p.isAtivo() && p.getCategoria().equalsIgnoreCase(categoria)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public Produto cadastrar(Produto produto) {
        produto.setId(contadorId.getAndIncrement());
        produto.setAtivo(true);
        banco.add(produto);
        return produto;
    }

    public boolean atualizar(Produto produtoAtualizado) {
        for (int i = 0; i < banco.size(); i++) {
            if (banco.get(i).getId() == produtoAtualizado.getId()) {
                banco.set(i, produtoAtualizado);
                return true;
            }
        }
        return false;
    }

    public boolean desativar(int id) {
        for (Produto p : banco) {
            if (p.getId() == id) {
                p.setAtivo(false);
                return true;
            }
        }
        return false;
    }

    public boolean verificarEstoque(int id, int quantidade) {
        Optional<Produto> produto = buscarPorId(id);
        return produto.map(p -> p.isAtivo() && p.getQuantidadeEstoque() >= quantidade)
                .orElse(false);
    }

    public boolean atualizarEstoque(int id, int novaQuantidade) {
        for (Produto p : banco) {
            if (p.getId() == id) {
                p.setQuantidadeEstoque(novaQuantidade);
                return true;
            }
        }
        return false;
    }
}

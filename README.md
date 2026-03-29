#  Produto WebService SOAP — FIAP Store

> **Disciplina:** Arquitetura SOA / Web Services
> Grupo: Pedro Oliveira (99943), Débora Ivanowski (555694), Diego Cabral (557817)
> **Professor:** Salatiel Luz Marinho  
> **Tecnologia:** Java 21 · JAX-WS · Maven · JAXB  

##  Contexto de Implantação
A **FIAP Store** é um é um sistema de gerenciamento de produtos que
precisa expor seu catálogo via WebService SOAP para integração com sistemas
parceiros que utilizam esse protocolo.
```
##  Estrutura do Projeto

```
produto-webservice/                  ← Servidor (publica o serviço)
│
├── pom.xml
│
└── src/main/java/br/com/fiap/soap/
    │
    ├── model/
    │   ├── Produto.java             ← Entidade principal
    │   ├── ProdutoResponse.java     ← Response para operações simples
    │   └── ProdutoListResponse.java ← Response para listagens
    │
    ├── repository/
    │   └── ProdutoRepository.java   ← Acesso a dados (simula banco em memória)
    │
    ├── service/
    │   └── ProdutoService.java      ← Operações SOAP expostas (@WebService)
    |
    ├── demoAplicattion              ← Publica o endpoint via 
```

---

##  Dependências Maven

```xml
<!-- JAX-WS: cria WebServices, gera WSDL, publica endpoints SOAP -->
<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-webservices</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
        <dependency>
            <groupId>wsdl4j</groupId>
            <artifactId>wsdl4j</artifactId>
        </dependency>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>com.sun.xml.ws</groupId>
			<artifactId>jaxws-rt</artifactId>
			<version>4.0.3</version>
		</dependency>
```

## Operações disponíveis

| Operação | Descrição | Parâmetros |
|----------|-----------|-----------|
| `listarProdutos` | Lista todos os produtos ativos | — |
| `buscarProdutoPorId` | Busca produto pelo ID | `id` |
| `buscarPorCategoria` | Lista produtos de uma categoria | `categoria` |
| `cadastrarProduto` | Cadastra novo produto | `nome, descricao, preco, quantidadeEstoque, categoria` |
| `atualizarPreco` | Atualiza o preço de um produto | `id, novoPreco` |
| `atualizarEstoque` | Atualiza o estoque de um produto | `id, novaQuantidade` |
| `desativarProduto` | Desativa produto (soft delete) | `id` |

---

## 🌐 Acessando o WSDL

Após iniciar o servidor, acesse no navegador:

```
http://localhost:8080/produtos?wsdl
```

O **WSDL** é o contrato do serviço. Ele define:
- Operações disponíveis
- Tipos de dados (via XSD)
- Endereço do endpoint
- Protocolo de transporte (SOAP over HTTP)

---

## 🧪 Testando via Insomnia / Postman

**Configuração:**
- Método: `POST`
- URL: `http://localhost:8080/produtos`
- Header: `Content-Type: text/xml;charset=UTF-8`

### Listar todos os produtos

```
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ser="http://service.soap.fiap.com.br/">
    <soapenv:Header/>
    <soapenv:Body>
        <ser:listarProdutos/>
    </soapenv:Body>
</soapenv:Envelope>
```

### Buscar produto por ID

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ser="http://service.soap.fiap.com.br/">
    <soapenv:Header/>
    <soapenv:Body>
        <ser:buscarProdutoPorId>
            <id>1</id>
        </ser:buscarProdutoPorId>
    </soapenv:Body>
</soapenv:Envelope>
```

### Cadastrar produto

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ser="http://service.soap.fiap.com.br/">
    <soapenv:Header/>
    <soapenv:Body>
        <ser:cadastrarProduto>
            <nome>Headset Sony WH-1000XM5</nome>
            <descricao>Headset noise-cancelling sem fio, 30h bateria</descricao>
            <preco>1899.90</preco>
            <quantidadeEstoque>10</quantidadeEstoque>
            <categoria>Audio</categoria>
        </ser:cadastrarProduto>
    </soapenv:Body>
</soapenv:Envelope>
```

### Atualizar preço

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ser="http://service.soap.fiap.com.br/">
    <soapenv:Header/>
    <soapenv:Body>
        <ser:atualizarPreco>
            <id>1</id>
            <novoPreco>4299.99</novoPreco>
        </ser:atualizarPreco>
    </soapenv:Body>
</soapenv:Envelope>
```

### Desativar produto

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ser="http://service.soap.fiap.com.br/">
    <soapenv:Header/>
    <soapenv:Body>
        <ser:desativarProduto>
            <id>2</id>
        </ser:desativarProduto>
    </soapenv:Body>
</soapenv:Envelope>
```

### Resposta esperada (exemplo buscarProdutoPorId)

```xml
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
    <S:Body>
        <ns2:buscarProdutoPorIdResponse xmlns:ns2="http://service.soap.fiap.com.br/">
            <return>
                <mensagem>Produto encontrado com sucesso.</mensagem>
                <produto>
                    <id>1</id>
                    <nome>Notebook Dell Inspiron 15</nome>
                    <descricao>Notebook com processador Intel i7, 16GB RAM, SSD 512GB</descricao>
                    <preco>4599.99</preco>
                    <quantidadeEstoque>15</quantidadeEstoque>
                    <categoria>Informatica</categoria>
                    <ativo>true</ativo>
                </produto>
                <sucesso>true</sucesso>
            </return>
        </ns2:buscarProdutoPorIdResponse>
    </S:Body>
</S:Envelope>
```

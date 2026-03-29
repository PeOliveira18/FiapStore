# Produto WebService SOAP — FIAP Store

> **Disciplina:** Arquitetura SOA / Web Services  
> **Professor:** Salatiel Luz Marinho
> **Grupo: ** Pedro Oliveira (99943), Débora Ivanowski (555694), Diego Cabral (557817)
> **Tecnologia:** Java 21 · JAX-WS · Maven · JAXB  

---

## Contexto de Implantação

A **FIAP Store** é um sistema de gerenciamento de produtos que precisa
expor seu catálogo via WebService SOAP para integração com sistemas parceiros
que utilizam esse protocolo. O SOAP foi escolhido por garantir um
contrato forte via WSDL — qualquer sistema pode apontar para o WSDL
e gerar um cliente automaticamente, sem depender de documentação manual.
Alguns parceiros (Mercado Livre, Amazon) utilizam sistemas legados — muitos
construídos há mais de 10 anos — que falam SOAP/XML como protocolo padrão de
integração enterprise. A adoção de SOAP neste cenário é uma exigência
de negócio, não uma escolha tecnológica livre.

---

## Problemas que este serviço resolve

| Problema | Solução aplicada |
|----------|-----------------|
| Parceiros legados não falam REST/JSON | Protocolo SOAP universalmente suportado |
| Sem contrato formal entre sistemas | WSDL gerado automaticamente pelo JAX-WS |
| Inconsistência de tipos de dados | Tipagem estrita via XML Schema (XSD) no WSDL |
| Dificuldade de integração B2B | Envelope SOAP padronizado (ISO/W3C) |
| Risco de deletar dados críticos | Soft delete: campo `ativo` preserva histórico |
| Validações dispersas no código | Validações centralizadas na camada Service |

---

## 🏗️ Estrutura do Projeto

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
    │
    └── demoApplication/             ← Publica o endpoint via Endpoint.publish()
```

---

## Dependências Maven

```xml
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

---

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

## Como executar

### 1. Servidor — publicar o serviço

```
mvn spring-boot:run
```
---

## Acessando o WSDL

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

## Testando via Insomnia

**Configuração:**
- Método: `POST`
- URL: `http://localhost:8080/produtos`
- Header: `Content-Type: text/xml;charset=UTF-8`

### Listar todos os produtos

```xml
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
---

## Boas práticas aplicadas

### 1. Separação de responsabilidades (Layered Architecture)
Cada camada tem uma única responsabilidade:
- **Model** → representa dados
- **Repository** → acessa dados
- **Service** → lógica de negócio + exposição SOAP
- **Publisher** → inicialização do servidor

### 2. Soft Delete
Produtos nunca são removidos fisicamente. O campo `ativo = false`
desativa o produto, preservando histórico de pedidos e integridade
referencial. Padrão obrigatório em sistemas enterprise.

### 3. Response objects dedicados
`ProdutoResponse` e `ProdutoListResponse` encapsulam sempre:
- `sucesso` (boolean): indica se a operação deu certo
- `mensagem` (String): descrição do resultado
- `produto` / `produtos`: dados retornados

Isso permite evoluir o contrato sem quebrar clientes existentes.

### 4. Validações na camada de serviço
Todas as regras de negócio (preço > 0, nome obrigatório, etc.)
são validadas antes de tocar o repositório.

### 5. Dados iniciais via bloco static
O repositório popula dados de teste automaticamente ao carregar,
facilitando desenvolvimento e demonstrações.

### 6. Namespace explícito no @WebService
```java
@WebService(targetNamespace = "http://service.soap.fiap.com.br/")
```
Definir namespace explícito garante estabilidade do WSDL entre
versões e é essencial para integrações enterprise.

---

## Próximas features

- [ ] **Integração com banco de dados** — JPA + Hibernate + Oracle
- [ ] **Autenticação WS-Security** — UsernameToken ou certificado X.509
- [ ] **HTTPS (WSS)** — comunicação criptografada entre sistemas
- [ ] **Paginação** — listarProdutos com `pagina` e `tamanhoPagina`
- [ ] **Busca por nome** — `buscarPorNome(String termo)`
- [ ] **Log de auditoria** — registrar todas as operações realizadas
- [ ] **Geração de stubs wsimport** — cliente tipado gerado pelo WSDL
- [ ] **Deploy em container** — WildFly / WebSphere / JBoss EAP
- [ ] **Testes unitários** — JUnit 5 para Service e Repository
- [ ] **Versionamento de API** — `/produtos/v1` e `/produtos/v2`

---

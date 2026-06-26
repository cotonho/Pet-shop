# 🐾 Sistema de Gerenciamento de Pet Shop

[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-orange.svg)](https://dev.mysql.com/downloads/)

Sistema completo para gerenciamento de um pet shop, desenvolvido como trabalho da disciplina **Programação Orientada a Objetos** do curso de Ciência da Computação – UNIPAC Barbacena.  
A aplicação aplica os pilares de **POO** (encapsulamento, herança e polimorfismo) e segue a arquitetura **MVC**, utilizando **Spring Boot**, **Spring Data JPA** e **MySQL**.

---

## Funcionalidades

- **Cadastro de proprietários** (nome, telefone, email, endereço)
- **Cadastro de animais** (nome, espécie, raça, idade, sexo, peso e foto – opcional via URL)
- **Cadastro de serviços** com **tipos polimórficos** (Banho, Veterinário, Hospedagem) e preço base
- **Lançamento de serviços** prestados com **cálculo automático de valor** por meio de polimorfismo
- **Histórico por animal** com filtros combinados: período + tipo de serviço
- **Relatórios**:
  - Total por serviço e por data (agrupamento diário)
  - Dados do cliente com seus animais
- **Segurança** das credenciais do banco via variáveis de ambiente
- **Testes unitários e de integração** (JUnit 5 + Mockito + H2)

---

## Tecnologias

| Camada          | Tecnologia                                                                           |
|-----------------|--------------------------------------------------------------------------------------|
| Back‑end        | Java 21, Spring Boot 4.1.0, Spring Data JPA, Hibernate, Bean Validation, Lombok      |
| Banco de dados  | MySQL 8.0 (produção) / H2 em memória (testes)                                        |
| Front‑end       | HTML5, CSS3, JavaScript puro (SPA)                                                   |
| Testes          | JUnit 5, Mockito, H2                                                                  |
| Documentação    | Relatório no padrão SBC (Sociedade Brasileira de Computação)                         |

---

## Estrutura de Pacotes

src/main/java/petshop/
├── model/ # Entidades JPA (Proprietario, Animal, Servico, Lancamento, Pessoa)
├── repository/ # Interfaces Spring Data JPA
├── service/ # Lógica de negócio (polimorfismo, relatórios)
├── controller/ # Endpoints REST
├── dto/ # Objetos de transferência de dados (relatórios, entrada de lançamento)
└── PetshopApplication.java

src/main/resources/
├── static/ # Front‑end (index.html, style.css, script.js)
├── application.properties # Configurações com placeholders para variáveis de ambiente
└── application.properties.example # Modelo de configuração sem dados sensíveis


---

## Como executar o projeto

### Pré‑requisitos

- **JDK 21** (ou superior)
- **MySQL 8.0** instalado e em execução
- **Maven** (opcional, o projeto já inclui o wrapper `mvnw`)

### 1. Clone o repositório

```bash
git clone https://github.com/cotonho/Pet-shop.git
cd petshop
```

### 2. Configure application.properties
Em Pet-shop\src\main\resources\ a um arquivo chamado "application.properties.example".
Nele altere as suas credenciais do Mysql e o salve como "application.properties".


E no seu MySQL:
```bash
CREATE DATABASE petshop;
```

### 3. Execute a aplicação
```bash
./mvnw spring-boot:run     # Linux / macOS
mvnw.cmd spring-boot:run   # Windows
```
Acesse a interface em: http://localhost:8080

## Aplicação dos conceitos de POO
Encapsulamento: todos os atributos são privados, acessados via getters/setters (Lombok). Validações com Bean Validation.

Herança:

Pessoa (@MappedSuperclass) → Proprietario herda campos comuns.

Servico (abstrata, @Inheritance SINGLE_TABLE) → ServicoBanho, ServicoVeterinario, ServicoHospedagem.

Polimorfismo: método abstrato Servico.calcularValor(Animal) implementado de forma distinta em cada subclasse. O LancamentoService chama esse método sem conhecer o tipo concreto, garantindo o cálculo automático do valor com regras específicas (ex.: Banho mais caro para animais pesados, Veterinário acresce taxa fixa).

## Autor
Marco Antônio da Silva Milagres

Graduando em Ciência da Computação – UNIPAC Barbacena

📧 231-001036@aluno.unipac.br

package com.example.robottech;

// Classe Model representa um modelo de dados para itens da loja
public class Model {

    // Atributos da classe Model
    String image; // Armazena o caminho ou referência da imagem do item
    String nome;  // Armazena o nome do item
    String preco; // Armazena o preço do item

    // Método getter para obter o caminho da imagem do item
    public String getImage() {
        return image; // Retorna o caminho da imagem
    }

    // Método getter para obter o nome do item
    public String getNome() {
        return nome; // Retorna o nome do item
    }

    // Método getter para obter o preço do item
    public String getPreco() {
        return preco; // Retorna o preço do item
    }
}

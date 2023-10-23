package Util;

public class Nodo <T extends Comparable<T>> {

    public T [] chaves;
    private int quantidadeChaves;// um nó tem no máximo M - 1 chaves, sendo M o gra da árvore B
    public Nodo<T>[] filhos; // filhos do nó
    private boolean ehFolha; //verifica se é uma folha, elementos só podem ser inseridos em nós folhas

    public Nodo(int M) {
        this.chaves = (T[])new Comparable[(2 * M)-1]; // um nó pode ter até  (2 * M) - 1 chaves
       // this.quantidadeChaves = 0;
        this.filhos = (Nodo<T>[])new Nodo[2 * M]; // Um nó pode ter até (2 * M) - 1 filhos
        this.ehFolha = true;
    }



    public T buscaInterna(T chaveBuscada) {
        for (int i = 0; i < this.quantidadeChaves; i++) {
            if (chaveBuscada.compareTo(this.chaves[i])==0) {
                return (T) this.chaves[i];
            }
        }
        return null;
    }

    public T[] getChaves() {
        return chaves;
    }

    public void setChaves(T[] chaves) {
        this.chaves = chaves;
    }

    public int getQuantidadeChaves() {
        return quantidadeChaves;
    }

    public void setQuantidadeChaves(int quantidadeChaves) {
        this.quantidadeChaves = quantidadeChaves;
    }

    public Nodo<T>[] getFilhos() {
        return filhos;
    }

    public void setFilhos(Nodo<T>[] filhos) {
        this.filhos = filhos;
    }

    public boolean isEhFolha() {
        return ehFolha;
    }

    public void setEhFolha(boolean ehFolha) {
        this.ehFolha = ehFolha;
    }


  

}

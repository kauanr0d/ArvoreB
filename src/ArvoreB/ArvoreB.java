package ArvoreB;

import Util.ArvoreBExceptions;
import Util.Nodo;

public class ArvoreB<T extends Comparable<T>> {

    private Nodo raiz;
    private int T; // grau da árvore B

    public ArvoreB(int ordem) {
        if (ordem >= 2) {
            this.raiz = new Nodo(ordem); // Passa o valor de T para inicializar os arrays em Nodo
            this.T = ordem;
            this.raiz.setQuantidadeChaves(0);
        } else {
            throw new ArvoreBExceptions("O grau minímo deve ser um valor inteiro maior ou igual a 2");
        }
    }

    public Nodo getRaiz() {
        return raiz;

    }

    public int getOrdem() {
        return T;
    }

    private Nodo busca(Nodo<T> buscado, T chave) {
        int i = 0;

        if (buscado == null) {
            return buscado;
        }

        for (i = 0; i < buscado.getQuantidadeChaves(); i++) {

            int comparacao = chave.compareTo(buscado.chaves[i]);

            if (comparacao == -1) {
                break;
            }
            if (comparacao == 0) {
                return buscado;
            }
        }

        if (buscado.isEhFolha()) {
            return null;
        } else {
            return busca(buscado.getFilhos()[i], chave);
        }
    }

    public Object buscar(T chave) {
        Nodo aux = this.busca(this.raiz, chave);
        if (aux != null) {
            return (Object) aux.buscaInterna(chave).toString();
        }
        return null;
    }

    public boolean contem(T chave) {
        if (this.busca(this.raiz, chave) != null) {
            return true;
        } else {
            return false;
        }
    }

    private void split(Nodo x, int pos, Nodo y) {
        Nodo z = new Nodo(this.T);
        z.setEhFolha(y.isEhFolha());
        z.setQuantidadeChaves(T - 1);

        for (int j = 0; j < T - 1; j++) {
            z.chaves[j] = y.chaves[j + T];
        }
        if (!y.isEhFolha()) {
            for (int j = 0; j < T; j++) {
                z.filhos[j] = y.filhos[j + T];
            }
        }
        y.setQuantidadeChaves(T - 1);

        for (int j = x.getQuantidadeChaves(); j >= pos + 1; j--) {
            x.filhos[j + 1] = x.filhos[j];
        }

        x.filhos[pos + 1] = z;

        for (int j = x.getQuantidadeChaves() - 1; j >= pos; j--) {
            x.chaves[j + 1] = x.chaves[j];
        }
        x.chaves[pos] = y.chaves[T - 1];
        x.setQuantidadeChaves(x.getQuantidadeChaves() + 1);
    }

    public void inserir(T elemento) {
        Nodo r = this.raiz;
        if (r.getQuantidadeChaves() == (2 * this.T) - 1) {
            Nodo s = new Nodo(this.T);
            this.raiz = s;
            s.setEhFolha(false);
            s.setQuantidadeChaves(0);
            s.filhos[0] = r;
            split(s, 0, r);
            inserir(s, elemento);
        } else {
            inserir(r, elemento);
        }
    }

    final private void inserir(Nodo<T> no, T elemento) {
        if (no.isEhFolha() == true) {
            int i = 0;
            for (i = no.getQuantidadeChaves() - 1; i >= 0 && elemento.compareTo(no.chaves[i]) < 0; i--) {
                no.chaves[i + 1] = no.chaves[i];
            }

            no.chaves[i + 1] = elemento;
            no.setQuantidadeChaves(no.getQuantidadeChaves() + 1);
        } else {
            int i = 0;
            for (i = no.getQuantidadeChaves() - 1; i >= 0 && elemento.compareTo(no.chaves[i]) < 0; i--) {
            };

            i++;
            Nodo filho = no.filhos[i];
            if (filho.getQuantidadeChaves() == 2 * this.T - 1) {
                split(no, i, filho);
                if (0 > elemento.compareTo(no.chaves[i])) {
                    i++;
                }
            }
            inserir(no.filhos[i], elemento);
        }
    }

    public void imprimir() {
        imprimir(raiz, 0);
    }

    private void imprimir(Nodo no, int nivel) {
        System.out.print("Nível " + nivel + ": ");
        for (int i = 0; i < no.getQuantidadeChaves(); i++) {
            System.out.print(no.chaves[i] + " ");
        }
        System.out.println();

        if (!no.isEhFolha()) {
            for (int i = 0; i < no.getQuantidadeChaves() + 1; i++) {
                imprimir(no.filhos[i], nivel + 1);
            }
        }
    }

    public void remover(T chave) {
        remover(raiz, chave);
    }

    private void remover(Nodo<T> no, T chave) {
        if (no == null) {
            return;
        }

        int i = 0;
        while (i < no.getQuantidadeChaves() && chave.compareTo(no.chaves[i]) > 0) {
            i++;
        }

        if (i < no.getQuantidadeChaves() && chave.compareTo(no.chaves[i]) == 0) {
            // Caso 1: A chave está presente no nó
            if (no.isEhFolha()) {
                // Caso 1a: Se o nó é uma folha, simplesmente remova a chave
                for (int j = i; j < no.getQuantidadeChaves() - 1; j++) {
                    no.chaves[j] = no.chaves[j + 1];
                }
                no.setQuantidadeChaves(no.getQuantidadeChaves() - 1);
            } else {
                // Caso 1b: Se o nó não é uma folha, substitua a chave pelo sucessor e remova o sucessor
                T sucessorChave = encontrarSucessor(no, i);
                remover(no.filhos[i + 1], sucessorChave);
                no.chaves[i] = sucessorChave;
            }
        } else {
            // Caso 2: A chave não está presente no nó, então, procure no filho apropriado
            boolean ehUltimo = (i == no.getQuantidadeChaves());
            if (no.filhos[i] != null && no.filhos[i].getQuantidadeChaves() < T) {
                // Caso 2a: Se o filho é pequeno, primeiro preencha-o
                preencher(no, i);
            }
            if (ehUltimo && i > no.getQuantidadeChaves()) {
                // Caso especial: Se a chave está na última posição e o último filho foi preenchido
                remover(no.filhos[i - 1], chave);
            } else {
                // Caso 2b: Recursivamente remova a chave do filho apropriado
                remover(no.filhos[i], chave);
            }
        }
    }

    private void preencher(Nodo<T> no, int pos) {
        if (pos != 0 && no.filhos[pos - 1] != null
                && no.filhos[pos - 1].getQuantidadeChaves() >= T) {
            // Se o irmão à esquerda tem pelo menos T chaves, pegue uma chave dele
            emprestarDoIrmaoEsquerdo(no, pos);
        } else if (pos != no.getQuantidadeChaves() && no.filhos[pos + 1] != null
                && no.filhos[pos + 1].getQuantidadeChaves() >= T) {
            // Se o irmão à direita tem pelo menos T chaves, pegue uma chave dele
            emprestarDoIrmaoDireito(no, pos);
        } else {
            // Caso contrário, funda o nó com seu irmão
            if (pos != no.getQuantidadeChaves()) {
                fundir(no, pos);
            } else {
                fundir(no, pos - 1);
            }
        }
    }

    private void emprestarDoIrmaoEsquerdo(Nodo<T> no, int pos) {
        Nodo<T> filho = no.filhos[pos];
        Nodo<T> irmaoEsquerdo = no.filhos[pos - 1];

        // Desloque todas as chaves do filho uma posição à frente
        for (int i = filho.getQuantidadeChaves() - 1; i >= 0; i--) {
            filho.chaves[i + 1] = filho.chaves[i];
        }

        // Se o filho não é uma folha, desloque também os ponteiros dos filhos uma posição à frente
        if (!filho.isEhFolha()) {
            for (int i = filho.getQuantidadeChaves(); i >= 0; i--) {
                filho.filhos[i + 1] = filho.filhos[i];
            }
        }

        // Mova uma chave do nó pai para o filho
        filho.chaves[0] = no.chaves[pos - 1];

        // Mova a última chave do irmão esquerdo para o nó pai
        no.chaves[pos - 1] = irmaoEsquerdo.chaves[irmaoEsquerdo.getQuantidadeChaves() - 1];

        // Atualize as quantidades de chaves nos nós
        filho.setQuantidadeChaves(filho.getQuantidadeChaves() + 1);
        irmaoEsquerdo.setQuantidadeChaves(irmaoEsquerdo.getQuantidadeChaves() - 1);
    }

    private void emprestarDoIrmaoDireito(Nodo<T> no, int pos) {
        Nodo<T> filho = no.filhos[pos];
        Nodo<T> irmaoDireito = no.filhos[pos + 1];

        // Mova uma chave do nó pai para o filho
        filho.chaves[filho.getQuantidadeChaves()] = no.chaves[pos];

        // Mova a primeira chave do irmão direito para o nó pai
        no.chaves[pos] = irmaoDireito.chaves[0];

        // Desloque todas as chaves do irmão direito uma posição atrás
        for (int i = 1; i < irmaoDireito.getQuantidadeChaves(); i++) {
            irmaoDireito.chaves[i - 1] = irmaoDireito.chaves[i];
        }

        // Se o irmão direito não é uma folha, desloque também os ponteiros dos filhos uma posição atrás
        if (!irmaoDireito.isEhFolha()) {
            for (int i = 1; i <= irmaoDireito.getQuantidadeChaves(); i++) {
                irmaoDireito.filhos[i - 1] = irmaoDireito.filhos[i];
            }
        }

        // Atualize as quantidades de chaves nos nós
        filho.setQuantidadeChaves(filho.getQuantidadeChaves() + 1);
        irmaoDireito.setQuantidadeChaves(irmaoDireito.getQuantidadeChaves() - 1);
    }

    private void fundir(Nodo<T> no, int pos) {
        Nodo<T> filho = no.filhos[pos];
        Nodo<T> irmao = no.filhos[pos + 1];

        filho.chaves[T - 1] = no.chaves[pos];

        for (int i = 0; i < irmao.getQuantidadeChaves(); i++) {
            filho.chaves[i + T] = irmao.chaves[i];
        }

        //Se o filho não é uma folha é enecessário q os ponteiros dos filhos do irmão para o filho
        if (!filho.isEhFolha()) {
            for (int i = 0; i <= irmao.getQuantidadeChaves(); i++) {
                filho.filhos[i + T] = irmao.filhos[i];
            }
        }

        //Desloque as chaves do nó pai uma posição atrás para preencher o espaço vazio
        for (int i = pos; i < no.getQuantidadeChaves() - 1; i++) {
            no.chaves[i] = no.chaves[i + 1];
        }

        //Deslocar os ponteiros dos filhos do nó pai uma posição atrás
        for (int i = pos + 1; i < no.getQuantidadeChaves(); i++) {
            no.filhos[i] = no.filhos[i + 1];
        }

        //Atualizan as quantidades de chaves nos nós
        filho.setQuantidadeChaves(2 * T - 1);

        //Se o nó pai se tornou vazio é necessário atualizar sua folha e quantidade de chaves
        if (no.getQuantidadeChaves() == 1) {
            if (no == raiz) {
                raiz = filho;
            }
            no = null;
        } else {
            no.setQuantidadeChaves(no.getQuantidadeChaves() - 1);
        }
    }

    private T encontrarSucessor(Nodo<T> no, int pos) {
        Nodo<T> atual = no.filhos[pos + 1];
        while (!atual.isEhFolha()) {
            atual = atual.filhos[0];
        }
        return atual.chaves[0];
    }

}

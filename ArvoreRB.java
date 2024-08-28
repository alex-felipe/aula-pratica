public class ArvoreRB {
    // Cores
    private static final Cor VERMELHO = Cor.RED;
    private static final Cor PRETO = Cor.BLACK;

    // nil é um nó vazio
    static final RBNo nil = new RBNo(-1);

    // raiz da árvore
    private RBNo raiz = nil;

    public RBNo buscar(int key) {
        return buscar(new RBNo(key), raiz);
    }

    private RBNo buscar(RBNo no, RBNo no2) {
        if (raiz == nil) {
            return null;
        }

        if (no.valor < no2.valor) {
            if (no2.esquerdo != nil) {
                return buscar(no, no2.esquerdo);
            }
        } else if (no.valor > no2.valor) {
            if (no2.direito != nil) {
                return buscar(no, no2.direito);
            }
        } else if (no.valor == no2.valor) {
            return no2;
        }
        return null;
    }

    public void inserir(int key) {
        inserir(new RBNo(key, nil));
    }

    private void inserir(RBNo no) {
        RBNo temp = raiz;
        // Árvore vazia (Caso 01)
        if (raiz == nil) {
            raiz = no;
            no.cor = PRETO;
            no.pai = nil;
        } else {
            // Inserindo um nó vermelho
            no.cor = VERMELHO;

            // Poderia ser recursivo também
            while (true) {
                // Operação igual à ABB
                if (no.valor < temp.valor) {
                    // Achei um local
                    if (temp.esquerdo == nil) {
                        temp.esquerdo = no;
                        no.pai = temp;
                        break;
                    } else {
                        temp = temp.esquerdo;
                    }
                } else {
                    // Achei um local
                    if (temp.direito == nil) {
                        temp.direito = no;
                        no.pai = temp;
                        break;
                    } else {
                        temp = temp.direito;
                    }
                }
            }

            // Caso ocorra alguma anomalia
            ajustarArvore(no);
        }
    }

    private void ajustarArvore(RBNo no) {
        while (no.pai.cor == VERMELHO) {
            RBNo tio;

            if (no.pai == no.pai.pai.esquerdo) {
                tio = no.pai.pai.direito;

                // Caso 02
                if (tio != nil && tio.cor == VERMELHO) {
                    no.pai.cor = PRETO;
                    tio.cor = PRETO;
                    no.pai.pai.cor = VERMELHO;
                    no = no.pai.pai;
                    continue;
                }

                // Nó é um filho à direita (interno)?
                if (no == no.pai.direito) {
                    no = no.pai;
                    rotateLL(no);
                }

                no.pai.cor = PRETO;
                no.pai.pai.cor = VERMELHO;

                // Rotação simples
                rotateRR(no.pai.pai);
            } else {
                tio = no.pai.pai.esquerdo;

                // Caso 02
                if (tio != nil && tio.cor == VERMELHO) {
                    no.pai.cor = PRETO;
                    tio.cor = PRETO;
                    no.pai.pai.cor = VERMELHO;
                    no = no.pai.pai;
                    continue;
                }

                // Nó é um filho à esquerda (interno)?
                if (no == no.pai.esquerdo) {
                    no = no.pai;
                    rotateRR(no);
                }

                no.pai.cor = PRETO;
                no.pai.pai.cor = VERMELHO;

                // Rotação simples
                rotateLL(no.pai.pai);
            }
        }

        // Recolore a raiz
        raiz.cor = PRETO;
    }

    // Rotação à esquerda
    private void rotateLL(RBNo no) {
        if (no.pai != nil) {
            if (no == no.pai.esquerdo) {
                no.pai.esquerdo = no.direito;
            } else {
                no.pai.direito = no.direito;
            }
            no.direito.pai = no.pai;
            no.pai = no.direito;
            if (no.direito.esquerdo != nil) {
                no.direito.esquerdo.pai = no;
            }
            no.direito = no.direito.esquerdo;
            no.pai.esquerdo = no;
        } else {
            RBNo direito = raiz.direito;
            raiz.direito = direito.esquerdo;
            direito.esquerdo.pai = raiz;
            raiz.pai = direito;
            direito.esquerdo = raiz;
            direito.pai = nil;
            raiz = direito;
        }
    }

    // Rotação à direita
    private void rotateRR(RBNo no) {
        if (no.pai != nil) {
            if (no == no.pai.esquerdo) {
                no.pai.esquerdo = no.esquerdo;
            } else {
                no.pai.direito = no.esquerdo;
            }

            no.esquerdo.pai = no.pai;
            no.pai = no.esquerdo;
            if (no.esquerdo.direito != nil) {
                no.esquerdo.direito.pai = no;
            }
            no.esquerdo = no.esquerdo.direito;
            no.pai.direito = no;
        } else {
            RBNo esquerdo = raiz.esquerdo;
            raiz.esquerdo = raiz.esquerdo.direito;
            esquerdo.direito.pai = raiz;
            raiz.pai = esquerdo;
            esquerdo.direito = raiz;
            esquerdo.pai = nil;
            raiz = esquerdo;
        }
    }

    // Apagar árvore
    public void deleteArovore() {
        raiz = nil;
    }

    public boolean deletar(RBNo z) {
        z = buscar(z, raiz);
        if (z == null) return false;

        RBNo x;
        RBNo y = z;
        Cor yOriginalColor = y.cor;

        if (z.esquerdo == nil) {
            x = z.direito;
            transplant(z, z.direito);
        } else if (z.direito == nil) {
            x = z.esquerdo;
            transplant(z, z.esquerdo);
        } else {
            y = treeMinimum(z.direito);
            yOriginalColor = y.cor;
            x = y.direito;
            if (y.pai == z)
                x.pai = y;
            else {
                transplant(y, y.direito);
                y.direito = z.direito;
                y.direito.pai = y;
            }
            transplant(z, y);
            y.esquerdo = z.esquerdo;
            y.esquerdo.pai = y;
            y.cor = z.cor;
        }
        if (yOriginalColor == PRETO) deleteFixup(x);
        return true;
    }

    // Auxiliar do delete
    private void transplant(RBNo no, RBNo no2) {
        if (no.pai == nil) {
            raiz = no2;
        } else if (no == no.pai.esquerdo) {
            no.pai.esquerdo = no2;
        } else {
            no.pai.direito = no2;
        }
        no2.pai = no.pai;
    }

    // Auxiliar do delete
    private void deleteFixup(RBNo no) {
        while (no != raiz && no.cor == PRETO) {
            if (no == no.pai.esquerdo) {
                RBNo no2 = no.pai.direito;
                if (no2.cor == VERMELHO) {
                    no2.cor = PRETO;
                    no.pai.cor = VERMELHO;
                    rotateLL(no.pai);
                    no2 = no.pai.direito;
                }
                if (no2.esquerdo.cor == PRETO && no2.direito.cor == PRETO) {
                    no2.cor = VERMELHO;
                    no = no.pai;
                } else {
                    if (no2.direito.cor == PRETO) {
                        no2.esquerdo.cor = PRETO;
                        no2.cor = VERMELHO;
                        rotateRR(no2);
                        no2 = no.pai.direito;
                    }
                    if (no2.direito.cor == VERMELHO) {
                        no2.cor = no.pai.cor;
                        no.pai.cor = PRETO;
                        no2.direito.cor = PRETO;
                        rotateLL(no.pai);
                        no = raiz;
                    }
                }
            } else {
                RBNo no2 = no.pai.esquerdo;
                if (no2.cor == VERMELHO) {
                    no2.cor = PRETO;
                    no.pai.cor = VERMELHO;
                    rotateRR(no.pai);
                    no2 = no.pai.esquerdo;
                }
                if (no2.direito.cor == PRETO && no2.esquerdo.cor == PRETO) {
                    no2.cor = VERMELHO;
                    no = no.pai;
                } else {
                    if (no2.esquerdo.cor == PRETO) {
                        no2.direito.cor = PRETO;
                        no2.cor = VERMELHO;
                        rotateLL(no2);
                        no2 = no.pai.esquerdo;
                    }
                    if (no2.esquerdo.cor == VERMELHO) {
                        no2.cor = no.pai.cor;
                        no.pai.cor = PRETO;
                        no2.esquerdo.cor = PRETO;
                        rotateRR(no.pai);
                        no = raiz;
                    }
                }
            }
        }
        no.cor = PRETO;
    }

    // Auxiliar do delete
    private RBNo treeMinimum(RBNo raizSubArvore) {
        while (raizSubArvore.esquerdo != nil) {
            raizSubArvore = raizSubArvore.esquerdo;
        }
        return raizSubArvore;
    }

    public void preorder(RBNo node) {
        if (node == nil) {
            return;
        }
        System.out.print(((node.cor == VERMELHO) ? "Cor: Vermelho " : "Cor: Preto ") + " - valor: " + node.valor);
        preorder(node.esquerdo);
        preorder(node.direito);
    }
}

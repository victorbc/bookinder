package equipe.projetoes.models;

/**
 * Created by Isaque-Fontinele on 24/08/2016.
 */
public enum Filtros {

    NOME(1), ISBN(2),AUTOR(3), EDITORA(4),ANO(5);

    private final int valor;

    Filtros(int valor){
        this.valor = valor;
    }

}

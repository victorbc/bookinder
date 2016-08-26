package equipe.projetoes.models;

import equipe.projetoes.R;

/**
 * Created by Isaque-Fontinele on 24/08/2016.
 */
public enum Filtros {

    TITULO(1, "title", R.id.b_titulo), ISBN(2, "isbn", R.id.b_isbn), AUTOR(3, "author", R.id.b_autor), EDITORA(4, "publisher", R.id.b_editora), ANO(5, "year", R.id.b_ano);

    private final int valor;
    private final String name;
    private final int id;

    Filtros(int valor, String name, int id){
        this.id = R.id.b_ano;
        this.valor = valor;
        this.name = name;
    }

    public int getValor() {
        return valor;
    }

    public String getName() {
        return name;
    }

    public int getId() { return id; }

}

package equipe.projetoes.models;

/**
 * Created by Victor Batista on 31/03/2016.
 */
public class Livro {
    private int resId;
    private String nome;
    private String autor;
    private String editora;
    private int pg;
    private int readPg;
    private long id;
    private boolean isFav;
    private boolean isTradable;
    private String ISBN;

    public Livro(int resId, String nome, String autor, String editora, int pg) {
        this.resId = resId;
        this.nome = nome;
        this.autor = autor;
        this.editora = editora;
        this.pg = pg;
    }

    public Livro(int resId, String nome, String autor, String editora, int pg, int readPg, boolean isFav, boolean isTradable, String ISBN) {
        this.resId = resId;
        this.nome = nome;
        this.autor = autor;
        this.editora = editora;
        this.pg = pg;
        this.readPg = readPg;
        this.id = id;
        this.isFav = isFav;
        this.isTradable = isTradable;
        this.ISBN = ISBN;
    }

    public Livro() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getReadPg() {
        return readPg;
    }

    public void setReadPg(int readPg) {
        this.readPg = readPg;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public boolean isTradable() {
        return isTradable;
    }

    public void setTradable(boolean tradable) {
        isTradable = tradable;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public int getPg() {
        return pg;
    }

    public void setPg(int pg) {
        this.pg = pg;
    }
}

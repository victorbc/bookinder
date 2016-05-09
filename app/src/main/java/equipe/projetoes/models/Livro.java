package equipe.projetoes.models;

import android.graphics.Bitmap;

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
    private Bitmap drawable;
    private String urlImg;

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
        this.isFav = isFav;
        this.isTradable = isTradable;
        this.ISBN = ISBN;
    }

    public Livro(Bitmap drawable, String nome, String autor, String editora, int pg, int readPg, boolean isFav, boolean isTradable, String ISBN) {
        this.drawable = drawable;
        this.nome = nome;
        this.autor = autor;
        this.editora = editora;
        this.pg = pg;
        this.readPg = readPg;
        this.isFav = isFav;
        this.isTradable = isTradable;
        this.ISBN = ISBN;
    }

    public Livro(String urlImg, String nome, String autor, String editora, int pg, int readPg, boolean isFav, boolean isTradable, String ISBN) {
        this.urlImg = urlImg;
        this.nome = nome;
        this.autor = autor;
        this.editora = editora;
        this.pg = pg;
        this.readPg = readPg;
        this.isFav = isFav;
        this.isTradable = isTradable;
        this.ISBN = ISBN;
    }


    public Livro() {
    }

    public Bitmap getDrawable() {
        return drawable;
    }

    public void setDrawable(Bitmap drawable) {
        this.drawable = drawable;
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

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Livro)) return false;

        Livro livro = (Livro) o;

        if (getResId() != livro.getResId()) return false;
        if (getPg() != livro.getPg()) return false;
        if (getReadPg() != livro.getReadPg()) return false;
        if (getId() != livro.getId()) return false;
        if (isFav() != livro.isFav()) return false;
        if (isTradable() != livro.isTradable()) return false;
        if (!getNome().equals(livro.getNome())) return false;
        if (getAutor() != null ? !getAutor().equals(livro.getAutor()) : livro.getAutor() != null)
            return false;
        if (getEditora() != null ? !getEditora().equals(livro.getEditora()) : livro.getEditora() != null)
            return false;
        return getISBN().equals(livro.getISBN());

    }

    @Override
    public int hashCode() {
        int result = getResId();
        result = 31 * result + getNome().hashCode();
        result = 31 * result + (getAutor() != null ? getAutor().hashCode() : 0);
        result = 31 * result + (getEditora() != null ? getEditora().hashCode() : 0);
        result = 31 * result + getPg();
        result = 31 * result + getReadPg();
        result = 31 * result + (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (isFav() ? 1 : 0);
        result = 31 * result + (isTradable() ? 1 : 0);
        result = 31 * result + getISBN().hashCode();
        return result;
    }

    @Override
    public String toString() {
        String retorno = "";
        retorno += " " + nome;
        retorno += " " + autor;
        retorno += " " + editora;
        retorno += " " + pg;
        retorno += " " + readPg;
        retorno += " " + id;
        retorno += " " + isFav;
        retorno += " " + isTradable;
        retorno += " " + ISBN;
        retorno += " " + drawable;
        return retorno;
    }
}

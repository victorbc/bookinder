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

    public Livro(int resId, String nome, String autor, String editora, int pg) {
        this.resId = resId;
        this.nome = nome;
        this.autor = autor;
        this.editora = editora;
        this.pg = pg;
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

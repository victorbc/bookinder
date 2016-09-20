package equipe.projetoes.models;

/**
 * Created by stenio on 9/15/2016.
 */
public class LivroUser {
    private int id;
    private Livro livro;
    private boolean favorite;
    private boolean tradeable;
    private Integer readPages;

    public LivroUser(Livro livro) {
        this.livro = livro;
        this.setFavorite(false);
        this.setTradeable(false);
        this.setReadPages(0);
    }

    public Livro getLivro() {
        return livro;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isTradeable() {
        return tradeable;
    }

    public void setTradeable(boolean tradeable) {
        this.tradeable = tradeable;
    }

    public Integer getReadPages() {
        return readPages;
    }

    public void setReadPages(Integer readPages) {
        this.readPages = readPages;
    }
}

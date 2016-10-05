package equipe.projetoes.models;

/**
 * Created by stenio on 9/15/2016.
 */
public class LivroUser {
    private int id;
    private Livro livro;
    private boolean favorite;
    private boolean tradeable;
    private boolean blocked;
    private boolean liked;
    private boolean owned;
    private boolean interested;
    private Integer readPages;

    public LivroUser(Livro livro) {
        this.livro = livro;
        this.setFavorite(false);
        this.setTradeable(false);
        this.setBlocked(false);
        this.setLiked(false);
        this.setOwned(false);
        this.setInterested(false);
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

    public int getId() { return id;}

    public void setId(int id) { this.id = id;}

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isOwned() {
        return owned;
    }

    public void setOwned(boolean owned) {
        this.owned = owned;
    }

    public boolean isInterested() {
        return interested;
    }

    public void setInterested(boolean interested) {
        this.interested = interested;
    }
}

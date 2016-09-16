package equipe.projetoes.models;

/**
 * Created by Victor on 4/9/2016.
 */
public class Match {

    private Livro livro;
    private Integer distance;

    public Match(Livro livro, Integer distance){

        this.livro = livro;
        this.distance = distance;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }
}

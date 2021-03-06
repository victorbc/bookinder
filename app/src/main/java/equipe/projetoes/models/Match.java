package equipe.projetoes.models;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.LayerDrawable;

import java.util.List;

/**
 * Created by Victor on 4/9/2016.
 */
public class Match {

    private List<Livro> meusLivros;
    private List<Livro> matchLivros;
    private Integer distance;
    private String matchName; //temporario
    private AnimationDrawable thumbMyBooks;
    private AnimationDrawable thumbMatchBooks;
//    private Account myAccount; //Precisa ser fornecido pelo bd
//    private Account matchAccount; //Precisa ser fornecido pelo bd

    public Match(List<Livro> meusLivros, List<Livro> matchLivros, String matchName, Integer distance, AnimationDrawable thumbMyBooks, AnimationDrawable thumbMatchBooks){
        this.matchName = matchName;

        this.meusLivros = meusLivros;
        this.matchLivros = matchLivros;
        this.distance = distance;
        this.matchName = matchName;
        this.thumbMyBooks = thumbMyBooks;
        this.thumbMatchBooks = thumbMatchBooks;
    }


    public List<Livro> getMeusLivros() {
        return meusLivros;
    }

    public void setMeusLivros(List<Livro> meusLivros) {
        this.meusLivros = meusLivros;
    }

    public List<Livro> getMatchLivros() {
        return matchLivros;
    }

    public void setMatchLivros(List<Livro> matchLivros) {
        this.matchLivros = matchLivros;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public AnimationDrawable getThumbMyBooks() {
        return thumbMyBooks;
    }

    public void setThumbMyBooks(AnimationDrawable thumbMyBooks) {
        this.thumbMyBooks = thumbMyBooks;
    }

    public AnimationDrawable getThumbMatchBooks() {
        return thumbMatchBooks;
    }

    public void setThumbMatchBooks(AnimationDrawable thumbMatchBooks) {
        this.thumbMatchBooks = thumbMatchBooks;
    }

}

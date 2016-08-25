package equipe.projetoes.models;

import android.app.Activity;
import android.content.res.Resources;

import java.text.Normalizer;

/**
 * Created by Nicolas on 16/03/2016.
 */

/**
 * Enum que guarda caracteristicas das categorias dos livros
 */
public enum Categoria {

    ROMANCE("Romance"), fANTASIA("Fantasia"),MISTERIO("Mistério"),FICCAOCIENTIFICA("Ficção \n Científica","scifi"),
    INFANTIL("Infantil"),MANGA("Mangá"),DIDATICO("Didático"),QUADRINHOS("Quadrinhos"),AUTOAJUDA("Auto-Ajuda","autoajuda"),
    RELIGIOSO("religioso");

    private final String nome;
    private final String nomeImagem;
    private final static String IMAGEM_CATEGORIA = "categoria_img_";
    private final static String DRAWABLE = "drawable";

    Categoria(String nome){
        this.nome = nome;
        this.nomeImagem = nome;
    }

    Categoria(String nome , String nomeImagem){
        this.nome = nome;
        this.nomeImagem = nomeImagem;
    }


    private String getNomeImagem(){
        return nomeImagem;
    }


    /**
     *
     * @return String
     */
    public String getNome(){
        return nome;
    }

    /**
     * Fornece o id do tipo int da imagem associada a categoria
     * @param activity
     * @return id da imagem da categoria que é do tipo int
     */
    public int getImagem(Activity activity){
        Resources mResources = activity.getResources();
        String mPackageName = activity.getPackageName();



        final int categoryImageResource = mResources.getIdentifier(
                IMAGEM_CATEGORIA + (removerAcentos(getNomeImagem()).toLowerCase()), DRAWABLE, mPackageName);
        return  categoryImageResource;
    }

    public static String removerAcentos(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

}

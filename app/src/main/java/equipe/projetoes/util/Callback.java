package equipe.projetoes.util;

/**
 * Created by stenio on 9/18/16.
 */
public interface Callback<T> {
    /**
     * Essa função será executada quando o callback for chamado
     * @param result o objeto que será passado para o callback
     */
    public void execute(T result);
}
